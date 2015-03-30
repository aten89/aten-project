/**
 * 
 */
package org.eapp.poss.blo.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.client.util.SystemProperties;
import org.eapp.oa.rmi.hessian.Contact;
import org.eapp.oa.rmi.hessian.IAddressListPoint;
import org.eapp.poss.blo.ITaskBiz;
import org.eapp.poss.dao.ITaskDAO;
import org.eapp.poss.hbean.Task;
import org.eapp.poss.hbean.TaskAssign;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;
import org.eapp.util.mail.JMailProxy;
import org.eapp.util.mail.MailMessage;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.def.Transition;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Hibernate;


/**
 * 流程任务服务接口实现
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2008-11-28	 zsy	新建
 * 2012-11-22	 何庆林	修改注释
 * </pre>
 */
public class TaskBiz implements ITaskBiz {

    /**
     * 日志
     */
    private static final Log LOG = LogFactory.getLog(TaskBiz.class);
    /**
     * 任务 DAO 接口
     */
    private ITaskDAO taskDAO;
    private IAddressListPoint addressListPoint;
    
    private boolean isNoticeFlowTask;//等办任务是否邮件通知
	private String noticeSubject;
	private String noticeContent;

    /**
     * 设置 taskDAO
     * 
     * @param taskDAO the taskDAO to set
     */
    public void setTaskDAO(ITaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public void setAddressListPoint(IAddressListPoint addressListPoint) {
		this.addressListPoint = addressListPoint;
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
	/*
     * (non-Javadoc)
     * 
     */
    @Override
    public void addTask(Task task) {
        if (task == null || task.getTaskInstanceId() == null || task.getFlowKey() == null || task.getFormId() == null) {
            throw new IllegalArgumentException();
        }
        taskDAO.save(task);
    }

    /*
     * (non-Javadoc)
     * 
     * java.lang.String)
     */
    @Override
    public void txStartTask(String taskInstanceId, String taskState, Date startTime, String transactor) {
        Task t = taskDAO.findByTaskInstanceId(taskInstanceId);
        if (t == null) {
            throw new IllegalArgumentException();
        }
        t.setTaskState(taskState);
        t.setStartTime(startTime);

        t.setTransactor(transactor);
        taskDAO.update(t);
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public String txStartTask(String taskId, String userAccountId, List<String> userRoles) throws Exception {
        if (taskId == null) {
            throw new IllegalArgumentException();
        }
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
            // Task中的transactor为空，则取TaskAssign表中的assignKey
            for (TaskAssign ta : t.getTaskAssigns()) {
				if (ta == null || ta.getAssignKey() == null) {
					continue;
				}
				if (TaskAssign.TYPE_USER == ta.getType() && ta.getAssignKey().equals(userAccountId)) {
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
                throw new Exception("没有处理权限");
            }
        } else {
            if (!t.getTransactor().equals(userAccountId)) {
                throw new Exception("没有处理权限");
            }
        }
        WfmContext context = WfmConfiguration.getInstance().getWfmContext();
        try {
            TaskInstance ti = context.getTaskInstance(t.getTaskInstanceId());
            if (ti == null) {
                throw new IllegalArgumentException("任务不存在");
            }
            if (TaskInstance.PROCESS_STATE_CREATE.equals(ti.getProcessState())) {
                // 若未认领，则认领
                ti.start(userAccountId);
                context.save(ti);
            }
            return ti.getView();
        } catch (Exception e) {
            // modify by fangwenwei 添加日志
            e.printStackTrace();
            context.rollback();
            throw e;
        } finally {
            context.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * java.lang.String)
     */
    @Override
    public void txEndTask(String taskInstanceId, String taskState, Date endTime, String comment) {
        Task t = taskDAO.findByTaskInstanceId(taskInstanceId);
        if (t == null) {
            throw new IllegalArgumentException();
        }
        t.setTaskState(taskState);
        t.setEndTime(endTime);
        t.setComment(comment);
        taskDAO.update(t);
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public void txReceiveTask(String taskInstanceId, String transactor) {
        Task t = taskDAO.findByTaskInstanceId(taskInstanceId);
        if (t == null) {
            throw new IllegalArgumentException();
        }
        t.setTransactor(transactor);
        taskDAO.update(t);
        
        //发送待办任务通知
		csSendTaskNotice(t, t.getDescription());
    }

    /**
     * 发送待办任务通知
     * @param userId
     * @param taskDesc
     */
	private void csSendTaskNotice(Task t, String taskDesc) {
		try{
			String userAccountId = t.getTransactor();
			if(isNoticeFlowTask){
				String msgContent = noticeContent.replaceAll("@taskTitle", taskDesc);
				//系统消息框
				UserAccountService userAccountService = new UserAccountService();
				userAccountService.sendSysMsg(SystemProperties.SYSTEM_ID, "系统", userAccountId, "待办任务：<span style='cursor:pointer;color:blue;' onclick=\"$.getMainFrame().addTab({id:'task+' + new Date(),title:'处理任务',url:'/poss/l/task/dispose?taskid=" + t.getId() + "&tiid=" + t.getTaskInstanceId() + "'});\">" + msgContent + "</span>");
				
				Contact addr = addressListPoint.getUserContact(userAccountId);
				if (addr == null || StringUtils.isBlank(addr.getEmail())) {
					LOG.warn("在通讯录中未设置 "+userAccountId+" 邮箱");
					return;
				}
				
				MailMessage msg = new MailMessage(addr.getEmail(), noticeSubject, msgContent);	
				//发送邮件
				JMailProxy.daemonSend(msg);
				
				LOG.info("流程待办任务通知成功");
			}
		} catch (Exception e) {
			LOG.error("流程待办任务通知失败", e);
		}
	}
    
    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public Task getByTaskInstanceId(String taskInstanceId) {
        if (taskInstanceId == null || "".equals(taskInstanceId)) {
            throw new IllegalArgumentException();
        }
        Task t = taskDAO.findByTaskInstanceId(taskInstanceId);
        if (t == null) {
            throw new IllegalArgumentException();
        }
        return t;
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public Task getById(String id) {
        if (id == null || "".equals(id)) {
            throw new IllegalArgumentException();
        }
        Task t = taskDAO.findById(id);
        if (t == null) {
            throw new IllegalArgumentException();
        }
        return t;
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public List<Task> getEndedTasks(String flowInstanceId) {
        return taskDAO.findEndedTasks(flowInstanceId);
    }

    /*
     * (non-Javadoc)
     * 
     */
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

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public void txDealApproveTask(String taskInstanceId, String comment, String transitionName) {

        WfmContext context = null;
        StringBuffer commentBuffer = new StringBuffer();
        if (StringUtils.isEmpty(taskInstanceId)) {
            throw new IllegalArgumentException("参数异常：id不能为空");
        }
        try {
            if (StringUtils.isNotEmpty(comment)) {
                commentBuffer.append(transitionName).append("，").append(comment);
            } else {
                commentBuffer.append(transitionName);
            }
            context = WfmConfiguration.getInstance().getWfmContext();
            TaskInstance ti = context.getTaskInstance(taskInstanceId);
            ti.setComment(commentBuffer.toString());
            if (transitionName == null) {
                ti.end();
            } else {
                ti.end(transitionName);
            }
            context.save(ti);
        } catch (RuntimeException e) {
            LOG.error(e.getMessage());
            context.rollback();
            throw e;
        } finally {
            if (context != null) {
                context.close();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public Task getDealingTasks(String formId) {
        return taskDAO.findDealingTasks(formId);
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public ListPage<Task> queryDealingTasks(QueryParameters tqp, String userAccountId) {
    	if (userAccountId == null) {
			throw new IllegalArgumentException();
		}
        ListPage<Task> taskPage = taskDAO.queryToDoTaskPage(tqp, userAccountId);
        return taskPage;
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public void txTaskAssign(String taskInstanceId, Set<TaskAssign> assigns) {
        Task t = taskDAO.findByTaskInstanceId(taskInstanceId);
        if (t == null) {
            throw new IllegalArgumentException();
        }
        t.setTaskAssigns(assigns);
        taskDAO.update(t);
    }

    @Override
    public String getContextVariable(String flowInstanceId, String contextVariableKey) throws Exception{
        if (StringUtils.isEmpty(flowInstanceId)) {
            throw new Exception("流程实例id不能为空!");
        }
        WfmContext context = null;
        try {
            context = WfmConfiguration.getInstance().getWfmContext();
            FlowInstance fi = context.getFlowInstance(flowInstanceId);
            ContextVariable cv =fi.getVariable(contextVariableKey);
            return cv.getValue();
        } catch(Exception e) {
            LOG.error("获取流程相关内容错误【flowInstanceId : " + flowInstanceId + ", contextVariableKey : " + contextVariableKey);
            return "";
        } finally {
            context.close();
        }
    }

    @Override
    public void txUpdateContextVariable(String flowInstanceId, ContextVariable variable) throws Exception {
        if (StringUtils.isEmpty(flowInstanceId)) {
            throw new Exception("流程实例id不能为空!");
        }
        WfmContext context = null;
        try {
            context = WfmConfiguration.getInstance().getWfmContext();
            FlowInstance fi = context.getFlowInstance(flowInstanceId);
            fi.addContextVariable(variable);
            context.save(fi);
        } catch (RuntimeException e) {
            LOG.error("txUpdateContextVariable",e);
            context.rollback();
            throw e;
        } finally {
            context.close();
        }
        
    }
}