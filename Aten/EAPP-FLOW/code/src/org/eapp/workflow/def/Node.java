package org.eapp.workflow.def;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;
import org.eapp.workflow.WfmServices;
import org.eapp.workflow.asyn.AsynNodeJob;
import org.eapp.workflow.def.action.ActionTypes;
import org.eapp.workflow.def.parse.WfdlXmlReader;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.FlowToken;
import org.eapp.workflow.svr.MessageService;


/**
 * 流程节点对象
 * 
 * @author 林良益  
 * @version 1.0
 * 2008-08-26
 */
public class Node extends FlowElement implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3086433673861426050L;
	// Fields
	
	//节点上绑定的Action
	protected Action action;
	//是否异步
	protected Boolean isAsync = Boolean.FALSE;	
	//到达当前节点的Transitions
	protected Set<Transition> arrivingTransitions;	
	//离开当前节点的Transitions
	protected List<Transition> leavingTransitions;
	//对应图形的ID
	protected String refGraphKey;
	
	//Transitions的命名映射
	transient Map<String , Transition> leavingTransitionMap = null;


	// Constructors

	/** default constructor */
	public Node() {
	}
	
	public Action getAction() {
		return this.action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Boolean getIsAsync() {
		return this.isAsync;
	}

	public void setIsAsync(Boolean isAsync) {
		this.isAsync = isAsync;
	}
	
	public Set<Transition> getArrivingTransitions() {
		return arrivingTransitions;
	}

	/**
	 * 读取普通Node的内容,普通节点可以绑定一个动作
	 * @param nodeElement
	 * @param wfdlXmlReader
	 */
	@SuppressWarnings("unchecked")
	public void read(Element nodeElement, WfdlXmlReader wfdlXmlReader) {
		Iterator<Element> iter = nodeElement.elementIterator();
	    while (iter.hasNext() && action == null) {//查找节点下第一个动作
	    	Element candidate = iter.next();
	    	if (ActionTypes.hasActionName(candidate.getName())) {
	    		action = wfdlXmlReader.createAction(candidate);
	    	}
	    }
	}
	
	/**
	 * 写入到XML文档
	 * @param nodeElement
	 */
//	public void write(Element nodeElement) {
//		
//	}
//	
	/**
	 * 给节点添加到达路径
	 * @param arrivingTransition
	 * @return
	 */
	public Transition addArrivingTransition(Transition arrivingTransition) {
		if (arrivingTransition == null){
			throw new IllegalArgumentException("不能向节点添加空的到达路径");
		}
		if (arrivingTransitions == null){
			arrivingTransitions = new HashSet<Transition>();		
		}
		arrivingTransitions.add(arrivingTransition);
		//关联节点和路径
		arrivingTransition.setToNode(this);
		return arrivingTransition;
	}

	/**
	 * 移除节点的到达路径
	 * @param arrivingTransition
	 */
	public void removeArrivingTransition(Transition arrivingTransition) {
		if (arrivingTransition == null){
			throw new IllegalArgumentException("不能从节点上移除空的到达路径");
		}
		if (arrivingTransitions != null) {
			if (arrivingTransitions.remove(arrivingTransition)) {
				//解除节点和达到路径间的关联
				arrivingTransition.setToNode(null);
			}
		}
	}	
	
	public List<Transition> getLeavingTransitions() {
		return leavingTransitions;
	}

	/**
	 * 向Node添加LeavingTransition，建立双向关联
	 * 规则：
	 * 1.不能向节点添加空(null)的LeavingTransition
	 * @param leavingTransition
	 * @return
	 */
	public Transition addLeavingTransition(Transition leavingTransition) {
		if (leavingTransition == null){
			throw new IllegalArgumentException("不能向节点添加空(null)的LeavingTransition");
		}
		if (leavingTransitions == null){
			leavingTransitions = new ArrayList<Transition>();
		}
		leavingTransitions.add(leavingTransition);
		//关联Transition的form节点
		leavingTransition.setFromNode(this);
		//清空leavingTransitionMap，迫使下次读取map的时候，再次计算Map的值
		leavingTransitionMap = null;
		return leavingTransition;
	}
	/**
	 * 从Node中移除leavingTransition，移除关联 
	 * @param leavingTransition
	 */
	public void removeLeavingTransition(Transition leavingTransition) {
		if (leavingTransition == null){
			throw new IllegalArgumentException("不能移除一个空(null)的LeavingTransition");
		}
		if (leavingTransitions != null) {
			if (leavingTransitions.remove(leavingTransition)) {
				//断开节点关联
				leavingTransition.setFromNode(null);
				//清空leavingTransitionMap，迫使下次读取map的时候，再次计算Map的值
				leavingTransitionMap = null;
			}
		}
	}	

	/**
	 * 获取LeavingTransitionsMap
	 * 改map不做持久化，通过对LeavingTransitions List的计算生成
	 * @return
	 */
	public Map<String , Transition> getLeavingTransitionsMap() {
		if ((leavingTransitionMap==null) && (leavingTransitions!=null) ){
			leavingTransitionMap = new HashMap<String , Transition>();
			//从列表的尾部反向遍历
			ListIterator<Transition> iter = leavingTransitions.listIterator(leavingTransitions.size());
			while (iter.hasPrevious()) {
				Transition leavingTransition = iter.previous();
				//根据ID映射leavingTransition
				//leavingTransitionMap.put(leavingTransition.getId(), leavingTransition);
				//根据命名映射leavingTransition
				leavingTransitionMap.put(leavingTransition.getName(), leavingTransition);
			}
		}
		return leavingTransitionMap;
	}
	
	/**
	 * 根据Transition的名称从map中查找
	 * 
	 * @param transitionName
	 * @return
	 */
	public Transition getLeavingTransition(String transitionName) {
		Transition transition = null;
		if (leavingTransitions!=null) {
			transition = getLeavingTransitionsMap().get(transitionName);
		}
		return transition;
	}
	
	/**
	 * 获取列表中存在的默认LeavingTransition
	 * 即第一条LeavingTransition
	 * @return
	 */
	public Transition getDefaultLeavingTransition() {
		Transition defaultTransition = null;
		if ((leavingTransitions!=null) && (leavingTransitions.size()>0) ){
			defaultTransition = leavingTransitions.get(0);
		} 
		return defaultTransition;
	}
	
	/*
	 * 重载FlowElement equals
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Node))
			return false;
		final Node other = (Node) obj;
		if (id == null || other.getId() == null) {
			return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}
	
	/**
	 * 进入节点
	 * @param execution
	 */
	public void enterNode(Execution execution) {
		FlowToken token = execution.getFlowToken();

	    //绑定令牌的节点位置
	    token.setNode(this);

	    //记录流转日志
    	WfmServices.getLogService().logEnterNode(this, execution);
    	
	    //触发进入节点事件
	    fireEvent(Event.EVENTTYPE_NODE_ENTER, execution);

	    //清除执行上下文绑定的Transition(转换)
	    execution.setTransition(null);

	    //执行节点
	    if (isAsync) {
	    	//异步执行
	    	//生成异步执行节点任务
	    	AsynNodeJob nodeJob = new AsynNodeJob(token);
	    	nodeJob.setNode(this);
	    	nodeJob.setDueDate(new Date());
	    	//发送异步任务
	    	MessageService msgService = WfmServices.getMessageSerivce();
	    	msgService.send(nodeJob);
	    	//锁定节点 
	    	token.lock(nodeJob.toString());
	    } else {
	    	//同步执行
	    	execute(execution);
	    }
	}
	
	/**
	 * 执行节点
	 * @param execution
	 */
	public void execute(Execution execution) {
		//如果节点绑定的动作不为空，则执行
		if (action!=null) {
			try {
		        //执行节点上绑定的动作(调用父类FlowElement的方法)
				executeAction(action, execution);
			} catch (Exception exception) {
				exception.printStackTrace();
			}

		} else {
			//当前节点没有绑定动作，则默认操作为离开当前节点
			leaveNode(execution);
		}
	}
	
	/**
	 * 使用默认Transition离开节点
	 * @param execution
	 */
	public void leaveNode(Execution execution) {
		leaveNode(execution, getDefaultLeavingTransition());
	}
	
	/**
	 * 使用指定名称的Transition离开节点
	 * @param execution
	 * @param transitionName
	 */
	public void leaveNode(Execution execution, String transitionName) {
		Transition transition = getLeavingTransition(transitionName);
		if (transition==null) {
			throw new RuntimeException("节点 " + this + " 没有名为 '"+transitionName+"' 的离开路径");
		}
		leaveNode(execution, transition);
	}
	
	/**
	 * 延指定Transition离开节点
	 * @param execution
	 * @param transition
	 */
	public void leaveNode(Execution execution, Transition transition) {
		if (transition==null){
			throw new RuntimeException("路径为空，无法离开当前节点"+this);
		}
		
		FlowToken token = execution.getFlowToken();
		token.setNode(this);
		
		//绑定上下文的转换路径
		execution.setTransition(transition);
		    
		//记录流转日志
    	WfmServices.getLogService().logLeaveNode(this, execution);
    	
		// 触发离开节点的事件
		fireEvent(Event.EVENTTYPE_NODE_LEAVE, execution);
		
		//运行转换路径
		transition.gotoNextNode(execution);
	}

	/**
	 * @param leavingTransitions the leavingTransitions to set
	 */
	public void setLeavingTransitions(List<Transition> leavingTransitions) {
		this.leavingTransitions = leavingTransitions;
	}

	/**
	 * @param arrivingTransitions the arrivingTransitions to set
	 */
	public void setArrivingTransitions(Set<Transition> arrivingTransitions) {
		this.arrivingTransitions = arrivingTransitions;
	}

	public String getRefGraphKey() {
		return refGraphKey;
	}

	public void setRefGraphKey(String refGraphKey) {
		this.refGraphKey = refGraphKey;
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
//
//		//清空离开当前节点的Transitions ID
//		if (leavingTransitions != null) {
//			for (Transition t : leavingTransitions) {
//				t.cleanId();
//			}
//		}
//
//		//清空动作ID
//		if (action != null) {
//			action.cleanId();
//		}
//	}
}