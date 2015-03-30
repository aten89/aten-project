/**
 * 
 */
package org.eapp.workflow.def.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.eapp.workflow.WfmException;
import org.eapp.workflow.def.Delegation;
import org.eapp.workflow.def.Node;
import org.eapp.workflow.def.Transition;
import org.eapp.workflow.def.parse.ParserException;
import org.eapp.workflow.def.parse.WfdlXmlReader;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.expression.ExpressionEvaluator;
import org.eapp.workflow.expression.IllegalExpressionException;
import org.eapp.workflow.handler.IDecisionHandler;

/**
 * 流程判断(选择)节点
 * @author 林良益
 * @author 卓诗垚
 * 2008-09-04
 * @version 2.0
 */
public class DecisionNode extends Node {
	
	List<DecisionCondition> decisionConditions = null;//判断节点条件
	Delegation decisionDelegation = null;//判断节点代理判断
	/**
	 * 
	 */
	private static final long serialVersionUID = -1429345828769249570L;
	
	public DecisionNode() {
		
	}
	
	@Override
	public void execute(Execution execution) {
		Transition transition = null;

		try {
			if (decisionDelegation != null) {
				if (decisionDelegation.getClassName() != null) {
					Class<?> c = Class.forName(decisionDelegation.getClassName());				
					IDecisionHandler decisionHandler = (IDecisionHandler)c.newInstance();
					String transitionName = decisionHandler.decide(execution);
			        transition = getLeavingTransition(transitionName);
			        if (transition==null) {
			          throw new RuntimeException("节点'" + name + "'选中的路径'"+transitionName+"'不存在" );
			        } 
				} else if (decisionDelegation.getExpression() != null) {
					//处理选择表达式
					String transitionName = ExpressionEvaluator.evaluate(decisionDelegation.getExpression(), execution, String.class);
			        if (transitionName == null) {
			        	throw new WfmException("条件表达式“"+decisionDelegation.getExpression()+"”执行结果为null");
			        }
			        transition = getLeavingTransition(transitionName);
			        if (transition == null) {
			        	throw new WfmException("判断节点“"+name+"”选择的转向“"+transitionName+"”不存在" );
			        }
				}
			} else if (decisionConditions != null && !decisionConditions.isEmpty()) {
				Iterator<DecisionCondition> iter = decisionConditions.iterator();
		        while (iter.hasNext() && (transition==null)) {
		        	DecisionCondition decisionCondition = iter.next();
		        	Boolean result = ExpressionEvaluator.evaluate(decisionCondition.getExpression(), execution, Boolean.class);
		        	if (result == null || result) {
		        		String transitionName = decisionCondition.getTransitionName();
		        		transition = getLeavingTransition(transitionName);
		        		break;
		        	}
		        }
			} else {
				//遍历节点的所有出口路径，寻找适合的
				Iterator<Transition> iter = leavingTransitions.iterator();
		        while (iter.hasNext() && (transition==null)) {
		        	Transition candidate = (Transition) iter.next();
		          
		        	String conditionExpression = candidate.getConditions();
		        	if (conditionExpression != null) {
		        		Boolean result = ExpressionEvaluator.evaluate(conditionExpression, execution, Boolean.class);
			            if (result != null && result) {
			            	transition = candidate;
			            	break;
			            }
		        	}
		        }
			}
			if (transition==null) {
				//选择节点默认的出口
				transition = getDefaultLeavingTransition();
			}
			transition.removeConditionEnforcement();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		execution.leaveNode(transition);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void read(Element nodeElement, WfdlXmlReader wfdlXmlReader) {
		 Element decisionHandlerElement = nodeElement.element("handler");
		 if (decisionHandlerElement != null) {
			 String expression = wfdlXmlReader.getElementText(decisionHandlerElement);
			 if (expression != null) {
		 		//表达式验证
				try {
		 	    	ExpressionEvaluator.test(expression, flowDefine.getVariableDeclares(), String.class);
		 		} catch (IllegalExpressionException e) {
		 			throw new ParserException("判断节点“" + name + "”中的表达式“" + expression + "”解析出错", e);
		 		}
			 }
			 
			 decisionDelegation = new Delegation(expression,
					 wfdlXmlReader.getElementAttribute(decisionHandlerElement, "class"));
		 } else {
			 if (decisionConditions == null) {
				 decisionConditions = new ArrayList<DecisionCondition>();
			 }
			 Iterator<Element> iter = nodeElement.elementIterator("condition");
			 String transitionName = null;
			 String expression = null;
			 while (iter.hasNext()) {
				 Element condElement = (Element) iter.next();
				 transitionName = wfdlXmlReader.getElementAttribute(condElement, "ref-transition");
				 expression = wfdlXmlReader.getElementText(condElement);
				 if (transitionName != null && expression != null) {
		 		    //表达式验证
		 	    	try {
		 	    		ExpressionEvaluator.test(expression, flowDefine.getVariableDeclares(), Boolean.class);
		 			} catch (IllegalExpressionException e) {
		 				throw new ParserException("判断节点“" + name + "”中的表达式“" + expression + "”解析出错", e);
		 			}
					decisionConditions.add(new DecisionCondition(transitionName, expression));
				 }
			 }
		 }
	}

	
	public List<DecisionCondition> getDecisionConditions() {
		return decisionConditions;
	}

	public void setDecisionConditions(List<DecisionCondition> decisionConditions) {
		this.decisionConditions = decisionConditions;
	}

	public Delegation getDecisionDelegation() {
		return decisionDelegation;
	}

	public void setDecisionDelegation(Delegation decisionDelegation) {
		this.decisionDelegation = decisionDelegation;
	}
}
