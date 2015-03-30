/**
 * 
 */
package org.eapp.workflow.exe;

import java.sql.Timestamp;
import java.util.Iterator;

import org.eapp.workflow.def.Event;
import org.eapp.workflow.def.Transition;


/**
 * 执行顺序的任务实例
 * 这种类型的任务实例结束时，将查看节点上是否有未启动的任务实例，如果有，则启动任务实例，如果没有，则启动节点下行
 * @author 林良益
 * 2008-10-16 
 * @version 2.0
 */
public class OrderExecTaskInstance extends TaskInstance {
	//扩展任务实例状态常量：
	
	//任务预创建
	public static final String PROCESS_STATE_PRECREATE = "ps_precreate";

	/**
	 * 默认情况下OrderExecTaskInstance的signal为false;
	 * processState=PROCESS_STATE_PRECREATE 
	 */
	public OrderExecTaskInstance(){
		super();
		this.processState = PROCESS_STATE_PRECREATE;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 7614000139703919822L;
	
	
	
	
	@Override
	public void end(Transition transition) {
		if (this.endTime!=null){
			throw new IllegalStateException("任务实例'"+id+"' 已经结束");
		}
		
//		if (this.isSuspended) {
//			throw new RuntimeException("task instance '"+id+"' is suspended");
//		}
		    
		//标记任务实例结束时间
		this.endTime = new Timestamp(System.currentTimeMillis());
		processState = PROCESS_STATE_END;
		this.isOpen = false;

		// 触发任务实例结束事件
		if ( (taskDefine!=null)&& (flowToken!=null)) {
			//初始化事件上下文
			Execution execution = new Execution(flowToken);
			execution.setTaskDefine(taskDefine);
			execution.setTaskInstance(this);
			//触发事件
			taskDefine.fireEvent(Event.EVENTTYPE_TASK_END, execution);
		}
		    
		    
		//验证任务是否触发流程节点向下流转
		if (isSignalling) {//当前任务是一个“信号任务”			
			this.isSignalling = false;//标为false，表示信号已经发出	      
			if ( (taskDefine!=null)
		                && (flowToken!=null)
		                && (taskDefine.getTaskNode()!=null)
		                && (taskDefine.getTaskNode().completionTriggersSignal(this))) {
				//如果没有指定的流转路径，则按默认路径；否则按指定的路径流转
				if (transition==null) {
					flowToken.signal();
				} else {
					flowToken.signal(transition);
				}
			}
		}else{
			if(flowToken != null){
				//获取当前任务实例的后续任务实例
				TaskInstance nextTaskInstance = getNextExecuteTaskInstance();
				if(nextTaskInstance != null){
					Execution execution = new Execution(flowToken);
					try {
						execution.setTaskDefine(taskDefine);
						execution.setTaskInstance(this);
						//设置后续任务实例的状态为PROCESS_STATE_CREATE,触发任务实例创建过程，包括事件
						nextTaskInstance.create(execution);
		     
					} finally {
						//清除上下文
						execution.setTaskDefine(null);
						execution.setTaskInstance(null);
					}
				}
			}
		}
	}
	
	/**
	 * 获取当前任务的下一个执行任务
	 * @return
	 */
	private TaskInstance getNextExecuteTaskInstance(){
		
		Iterator<TaskInstance> iter = flowInstance.getTaskInstances().iterator();
		//遍历当前节点中所有的任务实例
		//两个任务实例的Token相等，说明两个任务实例在同一个流程分支上
		//两个任务实例源自统一任务定义
		//同时确定他们不是相同的两个任务
		//确定是未完成的任务
		//确定任务的执行顺序是当前任务的下一个任务
		while (iter.hasNext()) {
		      TaskInstance other = iter.next();
		      if ( (flowToken != null)
		           && (flowToken.equals(other.getFlowToken()))
		           && (taskDefine.equals(other.getTaskDefine()))
		           && (! other.equals(this))
		           && (! other.hasEnded())
		           && (other.getExecuteOrder() == (this.executeOrder + 1))
		           ){
		           
		           return other;
		      }
		}		
		return null;
		
	}

}
