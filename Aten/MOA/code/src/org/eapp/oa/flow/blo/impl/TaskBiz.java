/**
 * 
 */
package org.eapp.oa.flow.blo.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.client.util.SystemProperties;
import org.eapp.client.util.UsernameCache;
import org.eapp.oa.address.dao.IAddressListDAO;
import org.eapp.oa.address.hbean.AddressList;
import org.eapp.oa.flow.blo.ITaskBiz;
import org.eapp.oa.flow.dao.ITaskDAO;
import org.eapp.oa.flow.dto.TaskQueryParameters;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.flow.hbean.TaskAssign;
import org.eapp.oa.system.exception.OaException;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;
import org.eapp.util.mail.JMailProxy;
import org.eapp.util.mail.MailMessage;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.def.Transition;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.PooledActor;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Hibernate;


/**
 * @author zsy
 * @version Nov 28, 2008
 */
public class TaskBiz implements ITaskBiz {
	/**
     * 日志
     */
    private static Log log = LogFactory.getLog(TaskBiz.class);
    
	private ITaskDAO taskDAO;
	private IAddressListDAO addressListDAO;
	
	private boolean isNoticeFlowTask;//等办任务是否邮件通知
	private String noticeSubject;
	private String noticeContent;

	public void setTaskDAO(ITaskDAO taskDAO) {
		this.taskDAO = taskDAO;
	}

	public void setAddressListDAO(IAddressListDAO addressListDAO) {
		this.addressListDAO = addressListDAO;
	}

	public void setNoticeFlowTask(boolean isNoticeFlowTask) {
		this.isNoticeFlowTask = isNoticeFlowTask;
	}

	public void setNoticeSubject(String noticeSubject) {
		this.noticeSubject = noticeSubject;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	@Override
	public void addTask(Task task) {
		if (task == null || task.getTaskInstanceID() == null || 
				task.getFlowKey() == null || task.getFormID() == null) {
			throw new IllegalArgumentException();
		}
		taskDAO.save(task);
	}
	
	@Override
	public void modifyTask(Task task) {
		if (task == null || task.getTaskInstanceID() == null || 
				task.getFlowKey() == null || task.getFormID() == null) {
			throw new IllegalArgumentException();
		}
		taskDAO.update(task);
	}
	
	@Override
	public Task getByTaskInstanceId(String taskInstanceId) {
		if (taskInstanceId == null) {
			return null;
		}
		List<Task> ts = taskDAO.findByTaskInstanceID(taskInstanceId);
		if (ts != null && ts.size() > 0) {
			return ts.get(0);
		}
		return null;
	}


	@Override
	public List<String> getTaskTransitions(String taskInstanceId) {
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
	
	@Override
	public ListPage<Task> getDealingTask(QueryParameters parms, String userAccountId) {
		if (userAccountId == null) {
			throw new IllegalArgumentException();
		}
		ListPage<Task> page = taskDAO.queryDealingTask(parms, userAccountId);
		return page;
	}
	
	@Override
	public String txStartTask(String taskId, String userAccountId, List<String> userRoles) throws OaException {
		if (taskId == null) {
			throw new IllegalArgumentException();
		}
		
		//设置为已读
		Task t = taskDAO.findById(taskId);
		if (t == null) {
			throw new IllegalArgumentException();
		}
		if (t.getViewFlag() == null || !t.getViewFlag()) {
			t.setViewFlag(Boolean.TRUE);
			taskDAO.saveOrUpdate(t);
		}
		if (t.getTransactor() == null) {
			Hibernate.initialize(t.getTaskAssigns());
			boolean assigned = false;
			//Task中的transactor为空，则取TaskAssign表中的assignKey
			for (TaskAssign ta : t.getTaskAssigns()) {
				if (ta == null || ta.getAssignKey() == null) {
					continue;
				}
				if (TaskAssign.TYPE_USER.equals(ta.getType()) && ta.getAssignKey().equals(userAccountId)) {
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
				throw new OaException("没有处理权限");
			}
		} else {
			if (!t.getTransactor().equals(userAccountId)) {
				throw new OaException("没有处理权限");
			}
		}
//		if(Task.WORKREPORT_NAME.equals(t.getFlowName())){
//			return "/m/work?act=todispose&workid="+t.getTaskInstanceID();
//		}
		
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			TaskInstance ti = context.getTaskInstance(t.getTaskInstanceID());
			if (ti == null) {
				throw new IllegalArgumentException("任务不存在");
			}
			if (TaskInstance.PROCESS_STATE_CREATE.equals(ti.getProcessState())) {
				//若未认领，则认领
				ti.start(userAccountId);
				context.save(ti);
			}
			
			return ti.getView();
		}catch(Exception e) {
			e.printStackTrace();
			context.rollback();
			throw new OaException(e.getMessage());
		} finally {
			context.close();
		}
		
	}
	
	@Override
	public void txDealApproveTask(String taskInstanceId, String comment, String transitionName) {
		txDealApproveTask(taskInstanceId, comment, transitionName, null);
	}
	
	@Override
	public void txDealApproveTask(String taskInstanceId, String comment, String transitionName, Map<String, String> contextVariables){
		
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			TaskInstance ti = context.getTaskInstance(taskInstanceId);
			if (ti == null) {
				throw new IllegalArgumentException();
			}
			if (contextVariables != null && !contextVariables.isEmpty()) {
				for (String varName : contextVariables.keySet()) {
					ContextVariable var = new ContextVariable(varName, 
							ContextVariable.DATATYPE_STRING, contextVariables.get(varName));
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
		}catch(RuntimeException e) {
			e.printStackTrace();
			context.rollback();
			throw e;
		} finally {
			context.close();
		}
	}

	@Override
	public ListPage<Task> getUnFinishTask(TaskQueryParameters parms) {
		ListPage<Task> list = taskDAO.queryUnFinishTask(parms);
		if (list.getDataList() != null) {
			for (Task t : list.getDataList()) {
				if (t != null) {
					String transactor = t.getTransactor() == null ? "" : t.getTransactor();
					if ("".equals(transactor)) {
						//Task中的transactor为空，则取TaskAssign表中的assignKey
						for (TaskAssign ta : t.getTaskAssigns()) {
							if (ta != null && ta.getAssignKey() != null) {
								if (transactor != null && !"".equals(transactor)) {
									transactor += ",";
								}
								transactor += UsernameCache.getDisplayName(ta.getAssignKey());
							}
						}
						t.setTransactor(transactor);
					}
				}
			}
		}
		return list;
	}
	@Override
	public void csSendTaskNotice(Task t, String taskDesc) {
		try{
			String userAccountId = t.getTransactor();
			if(isNoticeFlowTask){
				String msgContent = noticeContent.replaceAll("@taskTitle", taskDesc);
				//系统消息框
				UserAccountService userAccountService = new UserAccountService();
				userAccountService.sendSysMsg(SystemProperties.SYSTEM_ID, "系统", userAccountId, "待办任务：<span style='cursor:pointer;color:blue;' onclick=\"$.getMainFrame().addTab({id:'task+' + new Date(),title:'处理任务',url:'/oa/page/portlet?act=dispose&taskid=" + t.getId() + "&tiid=" + t.getTaskInstanceID() + "'});\">" + msgContent + "</span>");
				
				AddressList addr = addressListDAO.findByAccountId(userAccountId);
				if (addr == null || addr.getUserEmail() == null) {
					log.warn("在通讯录中未设置 "+userAccountId+" 邮箱");
					return;
				}
				
				MailMessage msg = new MailMessage(addr.getUserEmail(), noticeSubject, msgContent);	
				//发送邮件
				JMailProxy.daemonSend(msg);
				
				log.info("流程待办任务通知成功");
			}
		} catch (Exception e) {
			log.error("流程待办任务通知失败" + e.getMessage());
		}
	}
	
	@Override
	public void txTaskAssign(String taskInstanceId, String assign) {
		if (assign == null || "".equals(assign)) {
			return;
		}
		String[] assigns = assign.split(",");
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			//回写流程引擎数据
			TaskInstance ti = context.getTaskInstance(taskInstanceId);
			if (ti == null) {
				throw new IllegalArgumentException();
			}
			if (assigns.length > 1) {
				//多人授权
				ti.setPooledActors(PooledActor.createPool(assigns, ti));
			} else {
				//单人授权
				ti.setActorId(assign);
			}
			ti.setProcessState(TaskInstance.PROCESS_STATE_CREATE);
			ti.setStartTime(null);
			
			//处理本系统任务表及任务授权表
			List<Task> tasks = taskDAO.findByTaskInstanceID(taskInstanceId);
			if (tasks == null || tasks.size() == 0) {
				throw new IllegalArgumentException();
			}
			Task t = tasks.get(0);
			t.setViewFlag(false);
			t.setStartTime(null);
			t.setTaskState(TaskInstance.PROCESS_STATE_CREATE);
			if (t.getTaskAssigns() != null) {
				Iterator<TaskAssign> itr = t.getTaskAssigns().iterator();
				while (itr.hasNext()) {
					TaskAssign ta = itr.next();
					itr.remove();
					ta.setTask(null);
					taskDAO.delete(ta);
				}
			}
			if (assigns.length > 1) {
				//多人授权
				Set<TaskAssign> taskAssigns = new HashSet<TaskAssign>();
				for (String actor : assigns) {
					TaskAssign tp = new TaskAssign();
					tp.setType(TaskAssign.TYPE_USER);
					tp.setAssignKey(actor);
					taskAssigns.add(tp);
				}
				t.setTaskAssigns(taskAssigns);
				taskDAO.saveOrUpdate(t);
			}
			context.save(ti);
		}catch(RuntimeException e) {
			e.printStackTrace();
			context.rollback();
			throw e;
		} finally {
			context.close();
		}
	}
	
	@Override
	public List<Task> getEndedTasks(String flowInstanceID) {
		return taskDAO.findEndedTasks(flowInstanceID);
	}
}