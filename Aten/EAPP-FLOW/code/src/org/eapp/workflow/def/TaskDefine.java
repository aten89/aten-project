package org.eapp.workflow.def;

import java.sql.Timestamp;

import org.eapp.workflow.def.node.TaskNode;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.exe.TaskManagement;



/**
 * 任务定义实体
 * 
 * @author 林良益
 * @version 1.0
 */
public class TaskDefine extends FlowElement implements java.io.Serializable {


	/*
	 * 任务优先级常量
	 * 
	 */
	public static final int PRIORITY_HIGHEST = 100;
	public static final int PRIORITY_HIGH = 200;
	public static final int PRIORITY_NORMAL = 300;
	public static final int PRIORITY_LOW = 400;
	public static final int PRIORITY_LOWEST = 500;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3302240609252827402L;

	// Fields

	protected TaskNode taskNode;
	//任务执行顺序
	protected Integer executeOrder;
	//任务优先级
	protected Integer priority;
	//到期日
	protected Timestamp dueDate;
	//任务分派
	protected Delegation taskAssignment;
	//任务是否对流程有阻塞能力，true：任务没有完成则流程不能下行 
	protected boolean isBlocking = true;
	//任务是否有触发信号的能力，true：表示任务实例的signal方法将触发流程下行 
	protected boolean isSignalling = true;
	
	//任务视图
	protected Delegation taskView;
	
	protected String funcType;
	
	// Constructors

	/** default constructor */
	public TaskDefine() {
	}


	// Property accessors

	public TaskNode getTaskNode() {
		return taskNode;
	}


	public void setTaskNode(TaskNode taskNode) {
		this.taskNode = taskNode;
	}


	public Integer getExecuteOrder() {
		return executeOrder;
	}


	public void setExecuteOrder(Integer executeOrder) {
		this.executeOrder = executeOrder;
	}


	public Integer getPriority() {
		return priority;
	}


	public void setPriority(Integer priority) {
		this.priority = priority;
	}


	public Timestamp getDueDate() {
		return dueDate;
	}


	public void setDueDate(Timestamp dueDate) {
		this.dueDate = dueDate;
	}


	public Delegation getTaskAssignment() {
		return taskAssignment;
	}


	public void setTaskAssignment(Delegation taskAssignment) {
		this.taskAssignment = taskAssignment;
	}


	public Delegation getTaskView() {
		return taskView;
	}


	public void setTaskView(Delegation taskView) {
		this.taskView = taskView;
	}

	@Override
	public FlowElement getParent() {
		if (taskNode!=null) {
			return taskNode;
		}
		return flowDefine;
	}	

	/**
	 * 根据任务定义创建任务实例
	 * @return
	 */
	public TaskInstance[] createTaskInstance(TaskManagement tm , Execution execution){
		TaskInstance singleInstance = tm.createTaskInstance(this, execution);
		return new TaskInstance[]{singleInstance};
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TaskDefine))
			return false;
		final TaskDefine other = (TaskDefine) obj;
		if (id == null || other.getId() == null) {
			return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}


	public boolean getIsBlocking() {
		return isBlocking;
	}


	public void setIsBlocking(boolean isBlocking) {
		this.isBlocking = isBlocking;
	}


	public boolean getIsSignalling() {
		return isSignalling;
	}


	public void setIsSignalling(boolean isSignalling) {
		this.isSignalling = isSignalling;
	}


	public String getFuncType() {
		return funcType;
	}


	public void setFuncType(String funcType) {
		this.funcType = funcType;
	}
	
	
	
//	/**
//	 * 清空ID及级联保存的对像集合的ID
//	 */
//	public void cleanId() {
//		//清空自已ID
//		id = null;
//		
//		//清空事件ID
//		if (events != null) {
//			for (Event e : events.values()) {
//				e.cleanId();
//			}
//		}
//		//清空任务分派ID
//		if (taskAssignment != null) {
//			taskAssignment.cleanId();
//		}
//		
//		//清空任务视图ID
//		if (taskView != null) {
//			taskView.cleanId();
//		}
//	}
}