/**
 * 
 */
package org.eapp.workflow.def;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eapp.workflow.WfmServices;
import org.eapp.workflow.asyn.AsynActionJob;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.FlowToken;
import org.eapp.workflow.svr.MessageService;


/**
 * 流程定义基础元素——其它流程定义元素的基类
 * 该类实现 流程元素 完整的 事件触发 及 传递 模型
 * 
 * @author 林良益
 * 2008-08-26
 * @version 1.0
 */
public abstract class FlowElement implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1465553001427752596L;

	
	//物理ID
	String id;
	//名称
	protected String name;
	//元素描述
	protected String description = null;
	//所属流程定义
	protected FlowDefine flowDefine;
	//绑定的事件——预留实现
	protected Map<String , Event> events = null;
	//异常处理 —— 预留实现
	//protected List exceptionHandlers = null;

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public FlowDefine getFlowDefine() {
		return this.flowDefine;
	}

	public void setFlowDefine(FlowDefine flowDefine) {
		this.flowDefine = flowDefine;
	}	
	
	/**
	 * @param events the events to set
	 */
	public void setEvents(Map<String, Event> events) {
		this.events = events;
	}
	
	/**
	 * 
	 */
	public Map<String , Event> getEvents() {
		return events;
	}
	
	/*
	 * ************************
	 * 流程引擎事件模型
	 * ************************
	 */
	
	/**
	 * 判断是否元素绑定了事件
	 * @return
	 */
	public boolean hasEvents() {
		return ( (events!=null) && (events.size()>0) );
	}
	/**
	 * 获取指定类型的事件
	 * @param eventType
	 * @return
	 */
	public Event getEvent(String eventType) {
		Event event = null;
		if (events!=null) {
			event = events.get(eventType);
		}
		return event;
	}
	/**
	 * 判断是否存在指定类型的事件
	 * @param eventType
	 * @return
	 */	  
	public boolean hasEvent(String eventType) {
		boolean hasEvent = false;
		if (events!=null) {
			hasEvent = events.containsKey(eventType);
		}
		return hasEvent;
	}
	/**
	 * 添加事件
	 * 约束：
	 * 1.不能向流程元素绑定空(NULL)的事件
	 * 2.不能向流程元素绑定类型为空(NULL)的事件
	 * @param event
	 * @return
	 */	  
	public Event addEvent(Event event) {
		if (event == null){
			throw new IllegalArgumentException("不能向流程元素绑定空(NULL)的事件");
		}
		if (event.getEventType() == null){
			throw new IllegalArgumentException("不能向流程元素绑定类型为空(NULL)的事件");
		}
		if (events == null){
			events = new HashMap<String,Event>();
		}
		events.put(event.getEventType(), event);
		event.setFlowElement(this);
		return event;
	}
	/**
	 * 移除事件
	 * 约束：
	 * 1.不能从流程元素中移除空(null)的事件
	 * 2.不能从流程元素中移除类型为空(null)的事件
	 * @param event
	 * @return
	 */	  
	public Event removeEvent(Event event) {
		Event removedEvent = null;
		if (event == null){
			throw new IllegalArgumentException("不能从流程元素中移除空(null)的事件");
		}
		if (event.getEventType() == null){
			throw new IllegalArgumentException("不能从流程元素中移除类型为空(null)的事件");
		}
		if (events != null) {
			removedEvent = events.remove(event.getEventType());
			if (removedEvent!=null) {
				event.setFlowElement(null);
			}
		}
		return removedEvent;
	}
	
	
	/*
	 * ************************
	 * 流程引擎事件模型——事件处理
	 * ************************
	 */	
	/**
	 * 事件触发
	 */
	public void fireEvent(String eventType, Execution execution) {
		
		try {
			execution.setEventFirer(this);
			fireAndPropagateEvent(eventType, execution);
		} finally {
			execution.setEventFirer(null);
		}
	}
	
	/**
	 * 触发并传播事件
	 * @param eventType 事件类型
	 * @param execution 流程执行上下文
	 */
	public void fireAndPropagateEvent(String eventType, Execution execution){
		//对照事件发起元素和当前元素，判定事件是否是传播的，如果是传播的，则isPropagated=true，
		boolean isPropagated = !(this.equals(execution.getEventFirer()));
		
	    // 根据事件类型，获取当前元素绑定的事件对象
	    Event event = getEvent(eventType);
	    //当前元素设有指定的事件
	    if (event!=null) {
	        //设定执行上下文的当前事件
	        execution.setCurrentEvent(event);
	        //执行当前事件绑定的动作
	        executeActions(event.getActions(), execution, isPropagated);
	    }
	    //移除执行上下文的当前事件
	    execution.setCurrentEvent(null);
	    
	    //向上层元素传播事件
	    FlowElement parent = getParent();
	    if (parent!=null) {
	      parent.fireAndPropagateEvent(eventType, execution);
	    }
	}
	
	/**
	 * 执行动作列表
	 * @param actions
	 * @param execution
	 * @param isPropagated
	 */
	void executeActions(List<Action> actions, Execution execution, boolean isPropagated) {
		//循环执行动作列表
		if (actions!=null) {
			Iterator<Action> iter = actions.iterator();
			while (iter.hasNext()) {
				Action action = iter.next();
				//接受传播事件触发，或者事件不是传播事件
				if (action.isAcceptPropagationEvent() || (!isPropagated)) {
					if (action.getIsAsync()) {
						//异步执行Action
				    	//生成异步执行节点任务
				    	AsynActionJob actionJob = new AsynActionJob(execution.getFlowToken());
				    	actionJob.setAction(action);
				    	actionJob.setDueDate(new Date());
				    	//发送异步任务
				    	MessageService msgService = WfmServices.getMessageSerivce();
				    	msgService.send(actionJob);
				    } else {
						executeAction(action, execution);
					}
				}
			}
		}
	}
	
	/**
	 * 执行单个动作
	 * @param action
	 * @param execution
	 */
	public void executeAction(Action action, Execution execution) {
		FlowToken token = execution.getFlowToken();

		//如果当前的Action是在Event中触发执行的，则必须锁定token;
		//如果Action在Node中执行，或者token已经锁定，则不必再次锁定
		boolean tokenMustBeLocked = (execution.getCurrentEvent()!=null) && (!token.hasLocked());
		String lockOwnerId = "token["+token.getId()+"]"; 
		try {
		    //将动作绑定执行上下文
			execution.setCurrentAction(action);
	        if(tokenMustBeLocked){
				//锁定当前FlowToken
				token.lock(lockOwnerId);
	        }
	        //执行动作
	        action.execute(execution);
		} finally {
			//解锁当前FlowToken
			if (tokenMustBeLocked) {
				token.unlock(lockOwnerId);
			}
			//解除执行上下文的动作绑定
			execution.setCurrentAction(null);
		}
	}
	
	/*
	 * 处理流程定义的级联关系
	 */
	
	/**
	 * 获取当前流程元素的父元素
	 * 即关联的流程定义
	 * 对子流程内的元素，将返回子流程定义
	 * @return 
	 */
	public FlowElement getParent(){
		return flowDefine;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
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
		if (!(obj instanceof FlowElement))
			return false;
		final FlowElement other = (FlowElement) obj;
		if (id == null || other.getId() == null) {
			return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public String toString() {
		String className = getClass().getName(); 
		className = className.substring(className.lastIndexOf('.')+1);
		if (name!=null) {
			className = className+"("+name+")";
		} else {
			className = className+"("+Integer.toHexString(System.identityHashCode(this))+")";
		}
		return className;
	}
	
}
