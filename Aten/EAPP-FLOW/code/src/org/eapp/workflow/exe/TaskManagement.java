/**
 * 
 */
package org.eapp.workflow.exe;

import java.util.Iterator;
import java.util.Set;

import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmException;
import org.eapp.workflow.def.IAssignable;
import org.eapp.workflow.def.TaskDefine;
import org.eapp.workflow.expression.ExpressionEvaluator;
import org.eapp.workflow.handler.IAssignmentHandler;



/**
 * 流程任务管理器
 * 
 * @author 林良益
 * 2008-08-28
 * @version 1.0
 */
public class TaskManagement {
	
	protected FlowInstance flowInstance;
	
	TaskManagement(FlowInstance flowInstance){
		this.flowInstance = flowInstance;
	}
	
	/**
	 * 
	 * @param task
	 * @param token
	 * @return
	 */
	public TaskInstance createTaskInstance(TaskDefine task, FlowToken token) {
		//测试环境中允许token和task都为null
//		if(token == null){
//			throw new IllegalArgumentException("流程令牌为空，无法生产任务实例");
//		}
		Execution execution = new Execution(token);
		execution.setTaskDefine(task);
		return createTaskInstance(task, execution);
	}
	
	
	/**
	 * 根据指定的任务定义，和执行上下文，生成任务实例
	 * @param taskDefine
	 * @param execution
	 * @return
	 */
	public TaskInstance createTaskInstance(TaskDefine taskDefine, Execution execution) {
		//新建任务实例
		TaskInstance taskInstance = new TaskInstance();

		//绑定任务实例和流程实例
		flowInstance.addTaskInstance(taskInstance);

		//绑定任务定义，同时复制任务定义的属性
		if(taskDefine != null){
			taskInstance.setTaskDefine(taskDefine);
		}

		//将任务实例保存到数据库，同时付给ID
		WfmConfiguration.getInstance().getWfmContext().save(taskInstance);

		//绑定执行上下文
		if (execution != null) {
			FlowToken token = execution.getFlowToken();
			//绑定令牌
			taskInstance.setFlowToken(token);
			
			//在任务分配的时候，锁定当前的token，使得在AssignmentHandler中不能调用token.signal
			String lockOwnerId = "token["+token.getId()+"]"; 
			
			try {
				execution.setTaskDefine(taskDefine);
				execution.setTaskInstance(taskInstance);
				execution.setEventFirer(taskDefine);
				
				//触发任务实例创建过程，包括事件
		        taskInstance.create(execution);

		        //分派任务实例
		        if (taskDefine!=null) {
		        	//锁定当前FlowToken
		        	if(!token.hasLocked()){
						token.lock(lockOwnerId);
		        	}
		        	taskInstance.assign(execution);
		        }
		        
			} finally {
				if(token.hasLocked()){
					token.unlock(lockOwnerId);
				}
				//清除上下文
				execution.setTaskDefine(null);
				execution.setTaskInstance(null);
				execution.setEventFirer(null);
			}

		} else {
			//在没有上下文的情况下创建并触发任务实例，包括事件
			taskInstance.create(null);
		}
		return taskInstance;
	}
	
	/**
	 * 判断当前流程实例上下文中，是否存在Signalling类型的任务
	 * 规则：
	 * 1.Signalling标志=true 且 
	 * 2.当前任务实例的token和执行上下文的token一致
	 * @param token
	 * @return
	 */
	public boolean hasSignallingTaskInstances(FlowToken token) {
		boolean hasSignallingTasks = false;
		
		Set<TaskInstance> taskInstances = flowInstance.getTaskInstances();
		if(taskInstances != null){
			Iterator<TaskInstance> iter = taskInstances.iterator();
			
			while(iter.hasNext() && (!hasSignallingTasks)){
				
				TaskInstance ti = iter.next();
				if(ti.isSignalling 
						&& token != null
						&& token.equals( ti.getFlowToken())){
					hasSignallingTasks = true;
				}
			}
		}
		return hasSignallingTasks;
	}
	
	/**
	 * 判断当前流程实例上下文中，是否存在Blocking(阻挡性)的任务
	 * 规则
	 * 1.任务未结束 
	 * 2.任务是必办理的
	 * 3.当前任务实例的token和执行上下文的token一致
	 * @param token
	 * @return
	 */
	public boolean hasBlockingTaskInstances(FlowToken token) {
		boolean hasBlockingTasks = false;
		
		Set<TaskInstance> taskInstances = flowInstance.getTaskInstances();
		if (taskInstances!=null) {
			Iterator<TaskInstance> iter = taskInstances.iterator();
			
			while ( (iter.hasNext()) && (!hasBlockingTasks)) {
				
				TaskInstance taskInstance = (TaskInstance) iter.next();
				if ( (! taskInstance.hasEnded()) //任务未结束 
						&& (taskInstance.getIsBlocking()) //任务是必办理的
						&& (token!=null) //当前任务实例的token和执行上下文的token一致
						&& (token == taskInstance.getFlowToken() || token.equals(taskInstance.getFlowToken())) ) {
					hasBlockingTasks = true;
				}
			}
		}
		return hasBlockingTasks;
	}
	
	/**
	 * 对任务进行分派
	 * 规则：
	 * 1.有任务分配处理器时，使用处理器
	 * 2.其次是actorIdExpression和pooledActorsExpression共同生效
	 * @param assignmentHandler
	 * @param assignmentExpression
	 * @param assignable
	 * @param execution
	 */
	public void performAssignment(String assignmentHandler, 
			String assignmentExpression, 
			IAssignable assignable, 
			Execution execution) {
		try {
			if (assignmentHandler!=null) {
				performAssignmentHandler(assignmentHandler, assignable, execution);
			} else {
				//处理条件表达式授权
				performAssignmentExpression(assignmentExpression, assignable, execution);
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * 动态回调任务分配实现类
	 * @param assignmentHandler
	 * @param assignable
	 * @param execution
	 * @throws Exception
	 */
	void performAssignmentHandler(String assignmentHandler, IAssignable assignable, Execution execution) throws Exception {
		// 实例化任务分派类
		Class<?> c = Class.forName(assignmentHandler);
		IAssignmentHandler handlerInstance = (IAssignmentHandler)c.newInstance();
		// 调用分配方法
		handlerInstance.assign(assignable, execution);
	}
	
	/**
	 * 表达式处理
	 * 格式如ACTOR:zsy # POOLEDROLES:001,002 # POOLEDACTORS:001,002
	 * @param expression
	 * @param assignable
	 * @param execution
	 * @throws Exception
	 */
	void performAssignmentExpression(String expression, IAssignable assignable, Execution execution) throws Exception {
		//返回格式如：ACTORS:user2,user3;ROLES:role1,role2
		String result = ExpressionEvaluator.evaluate(expression, execution, String.class);
	    if (result == null) {
	    	throw new WfmException("条件表达式“"+expression+"”执行结果为null");
	    }
	    String[] strArr = result.split(";");
	    for (String assinStr : strArr) {
	    	String[] str = assinStr.split(":");
	    	if (str.length < 2) {
	    		continue;
	    	}
	    	if ("ACTORS".equals(str[0])) {
	    		String[] actorArrs = str[1].split(",");
	    		if (actorArrs.length == 1) {
	    			assignable.setActorId(actorArrs[0]);
	    		} else {
	    			assignable.setPooledActors(actorArrs);
	    		}
	    	} else if ("ROLES".equals(str[0])) {
	    		assignable.setPooledRoles(str[1].split(","));
	    	}
	    }
	}
	
	/**
	 * 关闭与指定的Token关联的任务实例的信号标志，是任务实例的signal方法不再有效
	 * @param flowToken
	 */
	public void removeSignalling(FlowToken flowToken) {
		Set<TaskInstance> taskInstances = flowInstance.getTaskInstances();
		if (flowInstance.taskInstances!=null) {
			Iterator<TaskInstance> iter = taskInstances.iterator();
			while (iter.hasNext()) {
				TaskInstance taskInstance = (TaskInstance) iter.next();
				if ( flowToken!=null && flowToken.equals(taskInstance.getFlowToken())) {
					taskInstance.setIsSignalling(false);
				}
			}
		}
	}	

	/**
	 * 挂起指定令牌分支上的任务实例
	 * @param flowToken
	 */
	public void suspend(FlowToken flowToken) {
		if (flowToken==null) {
			throw new RuntimeException("令牌对象为null，无法执行挂起操作");
		}
		Set<TaskInstance> taskInstances = flowInstance.getTaskInstances();
		if (taskInstances!=null) {
			Iterator<TaskInstance> iter = taskInstances.iterator();
			while (iter.hasNext()) {
				TaskInstance taskInstance = (TaskInstance) iter.next();
				if ((flowToken.equals(taskInstance.getFlowToken())) && (taskInstance.getIsOpen())) {
					taskInstance.suspend();
				}
			}
		}
	}
	
	/**
	 * 恢复指定令牌分支上的所有挂起的任务
	 */
	public void resume(FlowToken flowToken) {
		if (flowToken==null) {
			throw new RuntimeException("令牌对象为null，无法执行恢复挂起任务的操作");
		}
		Set<TaskInstance> taskInstances = flowInstance.getTaskInstances();
		if (taskInstances!=null) {
			Iterator<TaskInstance> iter = taskInstances.iterator();
			while (iter.hasNext()) {
				TaskInstance taskInstance = (TaskInstance) iter.next();
				if ((flowToken.equals(taskInstance.getFlowToken())) && (taskInstance.getIsOpen())) {
					taskInstance.resume();
				}
			}
	    }
	}
	
	
	public FlowInstance getFlowInstance() {
		return flowInstance;
	}	
	
	/**
	 * 向当前任务处于会审的流程实例添加一个任务实例
	 * @param actorIds
	 */
//	public void addTaskInstance(Collection<String> actorIds) {
//		if(flowInstance.getCurrentTaskInstances()==null || flowInstance.getCurrentTaskInstances().size()==0){
//			throw new WfmException("当前流程实例“"+flowInstance.getId()+"”未存在待办节点");
//		}
//		if(actorIds==null){
//			throw new WfmException("参与会审人员不能为空");
//		}
//		TaskInstance taskIns =flowInstance.getCurrentTaskInstances().get(0);
//		TaskDefine taskDefine = taskIns.getTaskDefine();
//		if(!(taskDefine.getFuncType().equals("PARALLEL"))){//判断是否多人并行
//			throw new WfmException("流程实例“"+flowInstance.getId()+"”当前待办不是会审任务");
//		}
//		FlowToken token = taskIns.getFlowToken();
//		Execution execution = new Execution(token);
//		execution.setTaskDefine(taskDefine);
//		for(String actorId : actorIds){
//			//新建任务实例
//			TaskInstance taskInstance = new TaskInstance();
//			
//			//绑定任务实例和流程实例
//			flowInstance.addTaskInstance(taskInstance);
//			
//			//绑定任务定义，同时复制任务定义的属性
//			if(taskDefine != null){
//				taskInstance.setTaskDefine(taskDefine);
//			}
//			
//			//将任务实例保存到数据库，同时付给ID
//			WfmConfiguration.getInstance().getWfmContext().save(taskInstance);
//			
//			//绑定执行上下文
//			if (execution != null) {
//				//绑定令牌
//				taskInstance.setFlowToken(token);
//				
//				//在任务分配的时候，锁定当前的token，使得在AssignmentHandler中不能调用token.signal
//				String lockOwnerId = "token["+token.getId()+"]"; 
//				
//				try {
//					execution.setTaskDefine(taskDefine);
//					execution.setTaskInstance(taskInstance);
//					execution.setEventFirer(taskDefine);
//					
//					//触发任务实例创建过程，包括事件
//					taskInstance.create(execution);
//					if(actorId!=null){
//						//锁定当前FlowToken
//						if(!token.hasLocked()){
//							token.lock(lockOwnerId);
//						}
//						taskInstance.setActorId(actorId);//授权
////						taskDefine.fireEvent(Event.EVENTTYPE_TASK_NOTIFY, execution);
//					}
//					
//				} finally {
//					if(token.hasLocked()){
//						token.unlock(lockOwnerId);
//					}
//					//清除上下文
//					execution.setTaskDefine(null);
//					execution.setTaskInstance(null);
//					execution.setEventFirer(null);
//				}
//				
//			} else {
//				//在没有上下文的情况下创建并触发任务实例，包括事件
//				taskInstance.create(null);
//			}
//		}
//	}
	
}
