/**
 * 
 */
package org.eapp.workflow.exe;


import java.util.Map;

import org.eapp.workflow.def.Action;
import org.eapp.workflow.def.Event;
import org.eapp.workflow.def.FlowElement;
import org.eapp.workflow.def.Node;
import org.eapp.workflow.def.TaskDefine;
import org.eapp.workflow.def.Transition;


/**
 * 流程引擎执行上下文
 * @author 林良益
 * 2008-08-27
 * @version 1.0	
 */
public class Execution {
	//流程令牌
	protected FlowToken flowToken;
	//事件发起元素
	protected FlowElement eventFirer;
	//上下文当前发生的事件
	protected Event currentEvent;
	//上下文当前执行的动作
	protected Action currentAction;
	//上下文当前绑定的转换
	protected Transition transition;
	//上下文当前绑定的任务定义
	protected TaskDefine taskDefine;
	//上下文当前绑定的任务实例
	protected TaskInstance taskInstance;
	//上下文绑定的子流程实例
	protected FlowInstance flowInstance;
	
	public Execution(FlowToken flowToken){
		this.flowToken = flowToken;
	}
	
	public Execution(Execution forCopy){
		this.flowToken = forCopy.flowToken;
		this.currentEvent = forCopy.currentEvent;
		this.currentAction = forCopy.currentAction;
	}

	public FlowToken getFlowToken(){
		return flowToken;
	}

	public void setFlowToken(FlowToken flowToken) {
		this.flowToken = flowToken;
	}
		
	public FlowElement getEventFirer() {
		return eventFirer;
	}
	
	public void setEventFirer(FlowElement eventFirer) {
		this.eventFirer = eventFirer;
	}
	
	public Event getCurrentEvent() {
		return currentEvent;
	}
	
	public void setCurrentEvent(Event currentEvent) {
		this.currentEvent = currentEvent;
	}
	
	public Action getCurrentAction() {
		return currentAction;
	}
	
	public void setCurrentAction(Action currentAction) {
		this.currentAction = currentAction;
	}
	
	public Transition getTransition() {
		return transition;
	}
	
	public void setTransition(Transition transition) {
		this.transition = transition;
	}

	public TaskDefine getTaskDefine() {
		return taskDefine;
	}

	public void setTaskDefine(TaskDefine taskDefine) {
		this.taskDefine = taskDefine;
	}

	public TaskInstance getTaskInstance() {
		return taskInstance;
	}

	public void setTaskInstance(TaskInstance taskInstance) {
		this.taskInstance = taskInstance;
	}

	public Node getNode() {
		return flowToken.getNode();
	}
	
	public void leaveNode(){
		getNode().leaveNode(this);
	}
	
	public void leaveNode(Transition transition) {
		getNode().leaveNode(this, transition);
	}

	public FlowInstance getFlowInstance() {
		return flowToken.getFlowInstance();
	}
	
	public FlowInstance getSubFlowInstance() {
		return flowInstance;
	}

	public void setSubFlowInstance(FlowInstance flowInstance) {
		this.flowInstance = flowInstance;
	}	
	
	public Map<String , ContextVariable> getContextVaribales(){
		if (flowToken == null) {
			return null;
		}
		FlowInstance fi = flowToken.getFlowInstance();
		if(fi == null){
			return null;
		}
		return fi.getContextVariables();
	}
}
