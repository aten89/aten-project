package org.eapp.workflow.def.node;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;
import org.eapp.workflow.def.Node;
import org.eapp.workflow.def.TaskDefine;
import org.eapp.workflow.def.Transition;
import org.eapp.workflow.def.parse.WfdlXmlReader;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.FlowToken;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.exe.TaskManagement;

/**
 * 任务节点
 * @author 林良益
 * 2008-08-27
 * @version 1.0
 */
public class TaskNode extends Node{

	/**
	 * 
	 */
	private static final long serialVersionUID = 686744018954772596L;
	
	/*
	 *  任务节点触发方式常量
	 */
	//无论任务是否执行，节点下行
	public static final int SIGNAL_ALWAYS	= 0;
	//无论任务是否执行，节点都不往下走
	public static final int SIGNAL_NEVER	= 1;
	//当节点中的第一个任务完成，节点往下走
	public static final int SIGNAL_FIRST	= 2;
	//当节点中的最后一个任务完成，节点往下走
	public static final int SIGNAL_LAST		= 3;
	
	//任务集合
	Set<TaskDefine> tasks = null;
	//节点下行触发方式
	int signalType = SIGNAL_LAST;
	//预留标识——true：在进入节点是自动创建任务，false：有节点的进入事件触发Action建任务
	boolean autoCreateTasks = true;
	//预留标识——true：在节点离开时，强制结束所有没有完成的任务；false：不强制结束没完成的任务
	boolean autoEndTasks = false;

	
	public TaskNode(){
		
	}
	
	/**
	 * 向任务节点添加任务
	 * @param task
	 */
	public void addTask(TaskDefine task) {
		if (tasks==null){
			tasks = new HashSet<TaskDefine>();
		}
		tasks.add(task);
		task.setTaskNode(this);
	}
	
	/**
	 * 获取任务与名称映射表
	 * @return
	 */
	public Map<String , TaskDefine> getTasksMap() {
		Map<String , TaskDefine> tasksMap = new HashMap<String , TaskDefine>();
		if (tasks!=null) {
			Iterator<TaskDefine> iter = tasks.iterator();
			while (iter.hasNext()) {
				TaskDefine task = iter.next();
				tasksMap.put(task.getName(), task);
			}
		}
		return tasksMap;
	}
	/**
	 * 根据任务名称取任务对象
	 * @param taskName
	 * @return
	 */
	public TaskDefine getTask(String taskName) {
		return getTasksMap().get(taskName);
	}	

	public Set<TaskDefine> getTasks() {
		return tasks;
	}

	public void setTasks(Set<TaskDefine> tasks) {
		this.tasks = tasks;
	}

	public int getSignalType() {
		return signalType;
	}

	public void setSignalType(int signalType) {
		this.signalType = signalType;
	}
	
	/**
	 * TaskNode的执行过程
	 * 
	 */
	@Override
	public void execute(Execution execution) {		
		//1.根据节点上绑定的任务定义，实例化任务
		TaskManagement tm = execution.getFlowToken().getFlowInstance().getTaskManagement();
		if(autoCreateTasks && tasks!=null){
			Iterator<TaskDefine> iter = tasks.iterator();
			while (iter.hasNext()) {
				TaskDefine task = iter.next();
				//实例化任务
				//在实例话过程中，任务实例将于上下文及流程实例绑定
	            //tm.createTaskInstance(task, execution);
				//扩展了MutilExecTaskDefine后，让各个子类自己建实例
				task.createTaskInstance(tm, execution);
			}
		}		
		//2.根据节点设置的signalType，判断是否立刻离开节点
		boolean toBeCcontinue = false;
		switch (signalType) {
			case SIGNAL_ALWAYS:
				toBeCcontinue = true;
				break;
			case SIGNAL_NEVER:
				toBeCcontinue = false;
				break;
			case SIGNAL_FIRST:
			case SIGNAL_LAST:
				toBeCcontinue = !tm.hasSignallingTaskInstances(execution.getFlowToken());
		}

		if (toBeCcontinue) {
			leaveNode(execution);
		}
	}


	@Override
	public void leaveNode(Execution execution, Transition transition) {
		TaskManagement tm = execution.getFlowToken().getFlowInstance().getTaskManagement();
		//判断是否有必完成的任务没有完成
		if (tm.hasBlockingTaskInstances(execution.getFlowToken()) ) { 
			throw new IllegalStateException("任务节点'"+name+"' 还有必须完成的任务未完成");
		}
		//停止节点上的所有其它任务实例
		removeTaskInstanceSynchronization(execution.getFlowToken());
		//调用Node类的方法，离开当前节点
		super.leaveNode(execution, transition);
	}
	

	public void read(Element nodeElement, WfdlXmlReader wfdlXmlReader) {
		String signalText = wfdlXmlReader.getElementAttribute(nodeElement, "signal");
	    if (signalText != null) {
	    	signalType = new Integer(signalText);
	    }
	    // 自动创建任务标志，默认true
	    String createTasksText = nodeElement.attributeValue("auto-create-tasks");
	    if (createTasksText!=null) {
	      if (("no".equalsIgnoreCase(createTasksText))
	           || ("false".equalsIgnoreCase(createTasksText)) ) {
	    	  autoCreateTasks = false;
	      }
	    }	    
	    // Node离开时，自动结束未完成的任务，默认为false
	    String removeTasksText = nodeElement.attributeValue("auto-end-tasks");
	    if (removeTasksText!=null) {
	      if (("yes".equalsIgnoreCase(removeTasksText))
	           || ("true".equalsIgnoreCase(removeTasksText)) ) {
	    	  autoEndTasks = true;
	      }
	    }	    
	    
		wfdlXmlReader.readTasks(nodeElement, this);
	}
	
	
//	@Override
//	public void write(Element nodeElement) {
//		
//	}
	
//	@Override
//	public void cleanId() {
//		
//		super.cleanId();
//		
//		//添空任务ID
//		if (tasks != null) {
//			for (TaskDefine t : tasks) {
//				t.cleanId();
//			}
//		}
//	}
	
	/**
	 * 判断任务实例是否能触发节点流转
	 * @param taskInstance
	 * @return
	 */
	public boolean completionTriggersSignal(TaskInstance taskInstance) {
		boolean completionTriggersSignal = false;
		//如果信号类型是SIGNAL_FIRST，说明只要有一个任务完成，就能触发流转信号
		if ((signalType==SIGNAL_FIRST) ) {
			completionTriggersSignal = true;
		}else if (signalType==SIGNAL_LAST && isLastEnded(taskInstance)){
			//如果信号类型是SIGNAL_LAST,则必须判断当前的任务实例是节点中最后一个完成的任务实例
			completionTriggersSignal = true;
		}
		return completionTriggersSignal;
	}
	
	/**
	 * 判断任务实例是当前任务节点中最后一个完成的任务
	 */
	boolean isLastEnded(TaskInstance taskInstance) {
		FlowToken flowToken = taskInstance.getFlowToken();
		FlowInstance flowInstance = flowToken.getFlowInstance();
			    
		boolean isLastEnded = true;
		Iterator<TaskInstance> iter = flowInstance.getTaskInstances().iterator();
		//遍历当前节点中所有的任务实例
		//两个任务实例的Token相等，说明两个任务实例在同一个流程分支上
		//同时确定他们不是相同的两个任务
		//被比较的任务处于信号待发状态isSignalling=true
		//如果它依然没有完成，就说明还有未完成的任务，则当前任务不是最后一个完成的任务
		while (iter.hasNext() && isLastEnded ) {
		      TaskInstance other = iter.next();
		      if ( (flowToken != null)
		           && (flowToken.equals(other.getFlowToken()))
		           && (! other.equals(taskInstance))
		           && (other.getIsSignalling())
		           && (!other.hasEnded())
		          ) {
		    	  isLastEnded = false;
		      }
		}
		return isLastEnded;
	}
	
	/**
	 * 停止节点上的所有任务实例
	 * @param flowToken
	 */
	public void removeTaskInstanceSynchronization(FlowToken flowToken) {
		Set<TaskInstance> taskInstances = flowToken.getFlowInstance().getTaskInstances();		
		if (taskInstances!=null) {
			Iterator<TaskInstance> iter = taskInstances.iterator();
			while (iter.hasNext()) {
				TaskInstance taskInstance = iter.next();
				if (flowToken.equals(taskInstance.getFlowToken())) {
					//设置任务实例的信号已触发——这个很重要,设置为false，则下面的 taskInstance.end();调用才不会发生递归嵌套
					if (taskInstance.getIsSignalling()) {
						taskInstance.setIsSignalling(false);
					}
					//设置任务实例为非阻挡模式
					if (taskInstance.getIsBlocking()) {
						taskInstance.setIsBlocking(false);
					}
					//结束所有没完成的任务
					if (! taskInstance.hasEnded() && autoEndTasks ) {
						//判断任务是否是当前节点上的
						if (tasks.contains(taskInstance.getTaskDefine())) {
							//由于前面已经把taskInstance的isSignalling设为false;这里的end调用不会触发token.signal()
							//只是设置endTime，以及触发end event
							taskInstance.end();
						}
					}
				}
			}
		}
	}

	public boolean isAutoCreateTasks() {
		return autoCreateTasks;
	}

	public void setAutoCreateTasks(boolean autoCreateTasks) {
		this.autoCreateTasks = autoCreateTasks;
	}

	public boolean isAutoEndTasks() {
		return autoEndTasks;
	}

	public void setAutoEndTasks(boolean autoEndTasks) {
		this.autoEndTasks = autoEndTasks;
	}	
}
