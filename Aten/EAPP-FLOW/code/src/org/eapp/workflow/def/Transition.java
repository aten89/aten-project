package org.eapp.workflow.def;

import org.eapp.workflow.WfmException;
import org.eapp.workflow.WfmServices;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.FlowToken;
import org.eapp.workflow.expression.ExpressionEvaluator;



/**
 * 流程中，连接节点间的转换，关联两个节点的迁移
 * 
 * @author 林良益
 * 2008-08-27
 * @version 1.0
 */
public class Transition extends FlowElement implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6538923334133948715L;
	// Fields
	//目标节点
	protected Node toNode;
	//源节点
	protected Node fromNode;
	//条件表达式——预留实现
	protected String conditions;
	//是否驳回
	protected boolean isRejected;
	//对应图形的ID
	protected String refGraphKey;
	
	//是否执行表达式
	transient boolean isConditionEnforced = true;

	// Constructors

	/** default constructor */
	public Transition() {
	}

	public Node getToNode() {
		return toNode;
	}

	public void setToNode(Node toNode) {
		this.toNode = toNode;
	}

	public Node getFromNode() {
		return fromNode;
	}

	public void setFromNode(Node fromNode) {
		this.fromNode = fromNode;
	}

	public String getConditions() {
		return this.conditions;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	/* 
	 * 重载FlowElement 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Transition))
			return false;
		final Transition other = (Transition) obj;
		if (id == null || other.getId() == null) {
			return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

	public void removeConditionEnforcement() {
		isConditionEnforced = false;
	}
	/**
	 * 离开上一节点（源节点），进入下一节点（目标节点）
	 * @param execution
	 */
	public void gotoNextNode(Execution execution) {
	    //清空令牌中的关联节点，在转换路径中，令牌是不属于任何节点的 
	    FlowToken token = execution.getFlowToken();
	    token.setNode(null);
	    
	    if ( (conditions != null) && (isConditionEnforced) ) {
	         Boolean result = ExpressionEvaluator.evaluate(conditions, execution, Boolean.class);
	         if (result == null) {
	        	 throw new WfmException("条件表达式“"+conditions+"”执行结果为null");
	         } else if ( !result) {
	        	 return;
	         }
	    }
	      
	    //如果是驳回流转，要设计token状态为驳回
	    token.setIsRejected(isRejected);
	    
	    //记录流转日志
    	WfmServices.getLogService().logThroughTransition(this, execution);
    	
    	//触发路径转换事件
    	fireEvent(Event.EVENTTYPE_TRANSITION, execution);
      
    	
	    //进入目标节点
	    toNode.enterNode(execution);		
	}

	public boolean getIsRejected() {
		return isRejected;
	}

	public void setIsRejected(boolean isRejected) {
		this.isRejected = isRejected;
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
//		id = null;
//		//清空自已ID
//		id = null;
//		
//		//清空事件ID
//		if (events != null) {
//			for (Event e : events.values()) {
//				e.cleanId();
//			}
//		}
//	}
}