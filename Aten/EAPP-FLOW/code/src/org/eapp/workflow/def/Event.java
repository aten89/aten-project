package org.eapp.workflow.def;

import java.util.ArrayList;
import java.util.List;


/**
 * 流程事件对象
 * 
 * @author 林良益
 * 2008-08-27
 * @version 2.0
 */
public class Event implements java.io.Serializable {
	
	/*
	 * 事件类型常量设定
	 */
	//流程开始
	public static final String EVENTTYPE_FLOW_START 	= "FLOW_START";
	//流程结束
	public static final String EVENTTYPE_FLOW_END 		= "FLOW_END";
	//进入节点
	public static final String EVENTTYPE_NODE_ENTER 	= "NODE_ENTER";
	//离开节点
	public static final String EVENTTYPE_NODE_LEAVE 	= "NODE_LEAVE";
	//节点间迁移
	public static final String EVENTTYPE_TRANSITION 	= "TRANSITION";
	//任务创建
	public static final String EVENTTYPE_TASK_CREATE 	= "TASK_CREATE";
	//任务通知
	public static final String EVENTTYPE_TASK_NOTIFY 	= "TASK_NOTIFY";
	//任务分派
	public static final String EVENTTYPE_TASK_ASSIGN 	= "TASK_ASSIGN";
	//任务开始（领用）
	public static final String EVENTTYPE_TASK_START 	= "TASK_START";
	//任务放弃领用
	public static final String EVENTTYPE_TASK_GIVEUP 	= "TASK_GIVEUP";	
	//任务完成（结束）
	public static final String EVENTTYPE_TASK_END 		= "TASK_END";
	//子流程创建
	public static final String EVENTTYPE_SUBFLOW_CREATE = "SUBFLOW_CREATE";
	//子流程结束
	public static final String EVENTTYPE_SUBFLOW_END = "SUBFLOW_END";
//	
//	public static final String EVENTTYPE_BEFORE_SIGNAL = "before-signal";
//	public static final String EVENTTYPE_AFTER_SIGNAL = "after-signal";
//	public static final String EVENTTYPE_SUPERSTATE_ENTER = "superstate-enter";
//	public static final String EVENTTYPE_SUPERSTATE_LEAVE = "superstate-leave";

//	public static final String EVENTTYPE_TIMER = "timer";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1154273152239416434L;
	// Fields
	
	//物理ID
	String id = null;
	//事件类型
	protected String eventType = null;
	//绑定的动作列表
	protected List<Action> actions = null;
	//事件发起元素
	protected FlowElement flowElement = null;
	
	// Constructors

	/** default constructor */
	public Event() {
	}
	
	public Event(String eventType) {
		this.eventType = eventType;
	}
	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	/**
	 * 是否有action
	 * @return
	 */
	public boolean hasActions() {
	    return (actions!=null && actions.size() > 0); 
	}

	/**
	 * 添加动作
	 * @param action
	 * @return
	 */
	public Action addAction(Action action) {
	    if (action == null) {
	    	throw new IllegalArgumentException("不能添加空的动作到事件");
	    }
	    if (actions==null) {
	    	actions = new ArrayList<Action>();
	    }
	    actions.add(action);
	    action.event = this;
	    return action;
	}

	/**
	 * 
	 * @param action
	 */
	public void removeAction(Action action) {
	    if (action==null) {
	    	throw new IllegalArgumentException("不能从事件中移除空的动作");
	    }
	    if (actions != null) {
	    	if (actions.remove(action)) {
	    		action.event = null;
	    	}
	    }
	}
	  
	public FlowElement getFlowElement() {
		return flowElement;
	}

	public void setFlowElement(FlowElement flowElement) {
		this.flowElement = flowElement;
	}
	
//	/**
//	 * 清空ID及级联保存的对像集合的ID
//	 */
//	public void cleanId() {
//		id = null;
//		//清空动作ID
//		if (actions != null) {
//			for (Action a : actions) {
//				a.cleanId();
//			}
//		}
//	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
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
		if (!(obj instanceof Event))
			return false;
		final Event other = (Event) obj;
		if (id == null || other.getId() == null) {
			return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

}