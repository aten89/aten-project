/**
 * 
 */
package org.eapp.flow.db;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.client.util.SystemProperties;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.WfmException;
import org.eapp.workflow.def.Transition;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.TaskInstance;


/**
 * 流程任务公用处理方法
 * @author zsy
 * @version Nov 28, 2008
 */
public abstract class FlowTaskBiz implements IFlowTaskBiz {
	private static Log log = LogFactory.getLog(FlowTaskBiz.class);
	private static String springBeanName;//Spring管理对象的Bean名称
	
	/**
	 * Spring初始化时设置Bean名称
	 * @param beanName
	 */
	public static void setSpringBeanName(String beanName) {
		springBeanName = beanName;
	}
	
	/**
	 * 
	 * @return
	 */
	public static FlowTaskBiz getInstance() {
		if (springBeanName == null) {
			throw new IllegalArgumentException("Spring未初始化对象");
		}
		return (FlowTaskBiz)SpringHelper.getBean(springBeanName);
	}
	
	
	/**
	 * 办理任务
	 * @param taskInstanceId
	 * @param comment
	 * @param transitionName
	 */
	public void dealTask(String taskInstanceId, String comment, String transitionName) {
		dealTask(taskInstanceId, comment, transitionName, null);
	}
	
	/**
	 * 办理任务
	 * @param taskInstanceId
	 * @param comment
	 * @param transitionName
	 * @param contextVariables
	 */
	public void dealTask(String taskInstanceId, String comment, String transitionName, Map<String, Object> contextVariables) {
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			TaskInstance ti = context.getTaskInstance(taskInstanceId);
			if (ti == null) {
				throw new IllegalArgumentException();
			}
			if (contextVariables != null && !contextVariables.isEmpty()) {
				for (String varName : contextVariables.keySet()) {
					ContextVariable var = new ContextVariable(varName, contextVariables.get(varName));
					ti.getFlowInstance().addContextVariable(var);
				}
			}
			ti.setComment(comment);
			if (transitionName != null) {
				ti.end(transitionName);
			} else {
				ti.end();
			}
			context.save(ti);
		} catch(RuntimeException e) {
			context.rollback();
			throw e;
		} finally {
			context.close();
		}
	}

	/**
	 * 任务重新授权
	 * @param taskInstanceId
	 * @param assignVal多个用逗号分开
	 */
	public void reAssignTask(String taskInstanceId, String assign) {
		if (assign == null || "".equals(assign)) {
			return;
		}
		String[] assigns = assign.split(",");
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			TaskInstance ti = context.getTaskInstance(taskInstanceId);
			if (ti == null) {
				throw new IllegalArgumentException();
			}
			if (assigns.length > 1) {
				//重新授权（会触发TASK_NOTIFY事件，同步到子系统的Task表中）
				ti.reSetPooledActors(assigns);
			} else {
				//单人授权（会触发TASK_ASSIGN事件，同步到子系统的Task表中）
				ti.setActorId(assign);
			}
			ti.setStartTime(null);
			context.save(ti);
		} catch(RuntimeException e) {
			context.rollback();
			throw e;
		} finally {
			context.close();
		}
	}
	
	/**
	 * 强制结束整个流程
	 * @param taskInstanceId
	 */
	public void forceEndFlow(String taskInstanceId) {
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			TaskInstance ti = context.getTaskInstance(taskInstanceId);
			if (ti == null) {
				throw new IllegalArgumentException("任务实例不存在：" + taskInstanceId);
			}
			FlowInstance fi = ti.getFlowInstance();
			if (fi == null) {
				throw new IllegalArgumentException("流程实例不存在");
			}
			fi.end();
		} catch (WfmException e) {
			context.rollback();
			throw e;
		} finally {
			context.close();
		}
		
	}
	
	/**
	 * 查询任务到达下一步的转向
	 * @param taskInstanceId
	 * @return
	 */
	public List<String> getTaskLeavingTransitions(String taskInstanceId) {
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			TaskInstance ti = context.getTaskInstance(taskInstanceId);
			List<Transition> trs = ti.getTaskDefine().getTaskNode().getLeavingTransitions();
			
			if (trs != null) {
				List<String> tnames = new ArrayList<String>(trs.size());
				for (Transition t : trs) {
					tnames.add(t.getName());
				}
				return tnames;
			}
		} finally {
			context.close();
		}
		return null;
	}
	
	/**
	 * 开始任务
	 * @param taskInstanceId 任务实例ID
	 * @param userAccountId 用户账号
	 * @param userRoles 用户角色
	 * @return 任务视图
	 */
	public String txStartTask(String taskInstanceId, String userAccountId, List<String> userRoles) {
		if (taskInstanceId == null) {
			throw new IllegalArgumentException("任务实例ID为空");
		}
		
		//设置为已读
		FlowTask ft = getByTaskInstanceId(taskInstanceId);
		if (ft == null) {
			throw new IllegalArgumentException();
		}
		if (ft.getViewFlag() == null || !ft.getViewFlag()) {
			ft.setViewFlag(Boolean.TRUE);
			modifyTask(ft);
		}
		if (ft.getTransactor() == null) {
			//FlowTask中的transactor为空，则取FlowTaskAssign表中的assignKey
			boolean assigned = false;
			for (FlowTaskAssign ta : ft.getTaskAssigns()) {
				if (ta == null || ta.getAssignKey() == null) {
					continue;
				}
				if (FlowTaskAssign.TYPE_USER.equals(ta.getType()) && ta.getAssignKey().equals(userAccountId)) {
					//用户授权
					assigned = true;
					break;
				} else if (userRoles != null) {
					//角色授权
					userRoles.contains(ta.getAssignKey());
					assigned = true;
					break;
				}
			}
			if (!assigned) {
				throw new WfmException("没有任务处理权限");
			}
		} else {
			if (!ft.getTransactor().equals(userAccountId)) {
				throw new WfmException("用户没有任务处理权限：" + userAccountId);
			}
		}
		
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			TaskInstance ti = context.getTaskInstance(ft.getTaskInstanceId());
			if (ti == null) {
				throw new IllegalArgumentException("任务不存在");
			}
			if (TaskInstance.PROCESS_STATE_CREATE.equals(ti.getProcessState())) {
				//若未认领，则认领
				ti.start(userAccountId);
				context.save(ti);
			}
			
			return ti.getView();
		} catch (WfmException e) {
			context.rollback();
			throw e;
		} finally {
			context.close();
		}
		
	}
	
	/**
	 * 待办任务到达通知（任务事件里调用）
	 * @param transactor
	 * @param taskAssigns
	 */
	public void notifyToDoTask(FlowTask task) {
		try {
			Set<String> userAccountIds = new HashSet<String>();
			if (task.getTransactor() != null) {
				//任务的办理人不为空时直接通知
				userAccountIds.add(task.getTransactor());
			} else if (task.getTaskAssigns() != null) {
				//通知有权限处理任务的人
				for (FlowTaskAssign ta : task.getTaskAssigns()) {
					if (ta.getAssignKey() == null) {
						continue;
					}
					if (FlowTaskAssign.TYPE_USER.equals(ta.getType())) {
						//用户类型，直接添加
						userAccountIds.add(ta.getAssignKey());
					} else {
						//角色类型，转为用户账号
						userAccountIds.addAll(getTaskAssignUsers(ta.getAssignKey(), task.getFlowClass()));
					}
				}
			}
			
			csSendTaskNotice(userAccountIds, task);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 将角色转为用户账号
	 * 子系统可以覆盖此方法的实现，比如报销流程不是用角色授权，而是用部门或职位授权，
	 * 这样就可以根据flowClass区分是要通过角色找用户账号还是部门或职位找用户账号
	 * @param roleAssignValue 角色
	 * @param flowClass 流程类别
	 * @return
	 */
	protected Set<String> getTaskAssignUsers(String roleAssignValue, String flowClass) {
		Set<String> userAccountIds = new HashSet<String>();
		try {
			UserAccountService uas = new UserAccountService();
			List<UserAccountInfo> users = uas.getUserAccountsByRole(roleAssignValue);
			if (users != null && !users.isEmpty()) {
				for (UserAccountInfo user : users) {
					userAccountIds.add(user.getAccountID());
				}
			}
		} catch (RpcAuthorizationException e) {
			log.error(e.getMessage(), e);
		} catch (MalformedURLException e) {
			log.error(e.getMessage(), e);
		}
		return userAccountIds;
	}
	
	/**
	 * 待办任务到达通知的具体实现
	 * 子系统可以覆盖此方法的实现，比如增加实现邮件通知
	 * @param userAccountIds 通知人员
	 * @param taskDescription 任务描述
	 */
	protected void csSendTaskNotice(Set<String> userAccountIds, FlowTask task) {
		if (userAccountIds == null || userAccountIds.isEmpty()) {
			return;
		}
		try {
			//系统消息框
			UserAccountService userAccountService = new UserAccountService();
			for (String userAccountId : userAccountIds) {
				userAccountService.sendSysMsg(SystemProperties.SYSTEM_ID, "系统", userAccountId, "待办任务：" + task.getDescription());
			}
			log.info("流程待办任务通知成功");
			//邮件通知
//			AddressList addr = addressListDAO.findByAccountId(userAccountId);
//			if (addr == null || addr.getUserEmail() == null) {
//				log.warn("在通讯录中未设置 "+userAccountId+" 邮箱");
//				return;
//			}
//			MailMessage msg = new MailMessage(addr.getUserEmail(), noticeSubject, msgContent);	
//			JMailProxy.daemonSend(msg);
		} catch (Exception e) {
			log.error("流程待办任务通知失败" + e.getMessage());
		}
	}
	
}