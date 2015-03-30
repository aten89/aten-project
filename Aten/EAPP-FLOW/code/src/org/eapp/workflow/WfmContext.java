/**
 * 
 */
package org.eapp.workflow;

import java.util.List;
import java.util.Map;

import org.eapp.workflow.db.AsynJobSession;
import org.eapp.workflow.db.FlowSession;
import org.eapp.workflow.db.HibernateSessionFactory;
import org.eapp.workflow.db.TaskSession;
import org.eapp.workflow.db.TraceSession;
import org.eapp.workflow.def.FlowDefine;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.FlowToken;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.trace.TracePoint;

/**
 * 流程引擎的上下文环境
 * @author 卓诗垚,林良益
 * @version 1.0
 */
public class WfmContext {
	private HibernateSessionFactory sessionFactory;
	private FlowSession flowSession;//
	private TaskSession taskSession;//
	private AsynJobSession jobSession;
	private TraceSession traceSession;
	
	boolean isClosed = false;
	
	public WfmContext(WfmConfiguration wfpmConfiguration) {
		sessionFactory = wfpmConfiguration.getSessionFactory();
	}
	
	/**
	 * 取得流程持久操作化类
	 * @return the defineSession
	 */
	public FlowSession getFlowSession() {
		if (flowSession == null) {
			flowSession = new FlowSession(sessionFactory);
		}
		return flowSession;
	}
	
	/**
	 * 取得任务持久化操作类
	 * @return 任务持久化操作类
	 */
	public TaskSession getTaskSession() {
		if (taskSession == null) {
			taskSession = new TaskSession(sessionFactory);
		}
		return taskSession;
	}
	
	/**
	 * 取得异步任务持久化操作类
	 * @return 异步任务持久化操作类
	 */
	public AsynJobSession getAsynJobSession() {
		if (jobSession == null) {
			jobSession = new AsynJobSession(sessionFactory);
		}
		return jobSession;
	}
	
	/**
	 * 取得异步任务持久化操作类
	 * @return 异步任务持久化操作类
	 */
	public TraceSession getTraceSession() {
		if (traceSession == null) {
			traceSession = new TraceSession(sessionFactory);
		}
		return traceSession;
	}
	
	/**
     * 持久化对像到数据库中
     * @param object hibernate对像
     */
    public void save(Object object) {
    	try {
    		sessionFactory.getSession(true).save(object);
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw re;
		}
    }
    
    /**
     * 将持久化对像从数据库中删除
     * @param object
     */
    public void delete(Object object) {
    	try {
    		sessionFactory.getSession(true).delete(object);
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw re;
		}
    }
	
	/**
	 * 发布流程定义，并持久化到数据库
	 * @param flowDefine
	 */
	public void deployFlowDefine(FlowDefine flowDefine) {
		if (flowDefine != null) {
			getFlowSession().saveFlowDefine(flowDefine);
		}
	}
	
	/**
	 * 通过用户ID取得必办任务列表
	 * @param actorId
	 * @return 任务实例列表
	 */
	public List<TaskInstance> getCompulsoryTasks(String actorId) {
		return getTaskSession().getCompulsoryTasks(actorId);
	}
	
	/**
	 * 通过用户ID集合取得必办任务列表
	 * @param actorIds
	 * @return 任务实例列表
	 */
	public List<TaskInstance> getCompulsoryTasks(List<String> actorIds) {
		return getTaskSession().getCompulsoryTasks(actorIds);
	}
	
	/**
	 * 通过用户ID取得待办任务列表
	 * @param actorId
	 * @return 任务实例列表
	 */
	public List<TaskInstance> getActorTasks(String actorId) {
		return getTaskSession().getActorTasks(actorId);
	}
	
	/**
	 * 通过用户ID集合取得待办任务列表
	 * @param actorIds
	 * @return 任务实例列表
	 */
	public List<TaskInstance> getActorTasks(List<String> actorIds) {
		return getTaskSession().getActorTasks(actorIds);
	}
	
	/**
	 * 通过角色ID取得待办任务列表
	 * @param roleId
	 * @return 任务实例列表
	 */
	public List<TaskInstance> getRoleTasks(String roleId) {
		return getTaskSession().getRoleTasks(roleId);
	}
	
	/**
	 * 通过角色ID集取得待办任务列表
	 * @param roleIds
	 * @return 任务实例列表
	 */
	public List<TaskInstance> getRoleTasks(List<String> roleIds) {
		return getTaskSession().getRoleTasks(roleIds);
	}
	
	
	/**
	 * 持久化流程实例到数据库
	 * @param flowInstance
	 */
	public void saveFlowInstance(FlowInstance flowInstance) {
		getFlowSession().saveFlowInstance(flowInstance);
		
	}
	
	/**
	 * 持久化任务实例到数据库
	 * @param taskInstance
	 */
	public void saveTaskInstance(TaskInstance taskInstance) {
		save(taskInstance.getFlowInstance());
	}
	
	/**
	 * 通过给定的流程定义的流程标识生成一个流程实例
	 * @param flowKey 流程标识
	 * @return 流程实例
	 */
	public FlowInstance newFlowInstance(String flowKey) {
		return newFlowInstance(flowKey, null);
	}
	
	/**
	 * 通过给定的流程定义的流程标识生成一个流程实例
	 * @param flowKey 流程标识
	 * @return 流程实例
	 */
	public FlowInstance newFlowInstance(String flowKey , Map<String, ContextVariable> contextVariables) {
		FlowDefine flowDefine = getFlowSession().findFlowDefine(flowKey);
		if (flowDefine == null) {
			throw new WfmException("流程定义不存在：" + flowKey);
		}
		return flowDefine.createFlowInstance(contextVariables);
	}	
	
	/**
	 * 取得流程实例
	 * @param flowInstanceId 流程实例ID
	 * @return 流程实例
	 */
	public FlowInstance getFlowInstance(String flowInstanceId) {
		return getFlowSession().findFlowInstance(flowInstanceId);
	}
	
	/**
	 * 取得任务实例
	 * @param taskInstanceId 任务实例ID
	 * @return 任务实例
	 */
	public TaskInstance getTaskInstance(String taskInstanceId) {
		return getFlowSession().findTaskInstance(taskInstanceId);
	}
	
	/**
	 * 通过ID取得令牌
	 * @param tokensId 令牌ID
	 * @return 流程令牌
	 */
	public FlowToken getFlowToken(String tokensId) {
		return getFlowSession().findFlowToken(tokensId);
	}
	
	/**
	 * 通过流程实例ID查找动行轨迹列表
	 * @param flowInstanceId
	 * @return
	 */
	public List<TracePoint> getTracePoints(String flowInstanceId) {
		return getTraceSession().getTracePoints(flowInstanceId);
	}
	
	/**
	 * 关闭上下文
	 * 提交事务，关闭连接
	 */
	public void close() {
		try{
			//关闭消息服务
			WfmServices.getMessageSerivce().close();
		} finally {
			isClosed = true;
			//关闭Hibernate服务
			sessionFactory.closeSession();
		}
	}
	
	/**
	 * 回滚事务
	 */
	public void rollback() {
		sessionFactory.setRollback(true);
	}

	/**
	 * 判断上下文是否关闭过
	 * 如果close方法被调用，则返回true
	 * @return
	 */
	public boolean isClosed() {
		return isClosed;
	}	
}
