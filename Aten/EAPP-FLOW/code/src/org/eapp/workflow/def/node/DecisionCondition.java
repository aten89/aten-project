/**
 * 
 */
package org.eapp.workflow.def.node;

import java.io.Serializable;

/**
 * 判断节点条件
 * @author 卓诗垚
 * @version Sep 25, 2008
 * @version 2.0
 */
public class DecisionCondition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7665862976567774410L;
	
	String transitionName;//转向名称
	String expression;//表达式

	public DecisionCondition() {
	}

	public DecisionCondition(String transitionName, String expression) {
		this.transitionName = transitionName;
		this.expression = expression;
	}

	public String getTransitionName() {
		return transitionName;
	}

	public void setTransitionName(String transitionName) {
		this.transitionName = transitionName;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	
}
