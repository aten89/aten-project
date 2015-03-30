/**
 * 
 */
package org.eapp.workflow.def.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;
import org.eapp.workflow.def.Node;
import org.eapp.workflow.def.Transition;
import org.eapp.workflow.def.parse.ParserException;
import org.eapp.workflow.def.parse.WfdlXmlReader;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.FlowToken;
import org.eapp.workflow.expression.ExpressionEvaluator;
import org.eapp.workflow.expression.IllegalExpressionException;

/**
 * 流程的分支节点
 * @author 林良益
 * @author 卓诗垚
 * 2008-09-10
 * @version 2.0
 */
public class ForkNode extends Node {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4678505465490289884L;

	List<DecisionCondition> decisionConditions = null;//节点条件
	
	public ForkNode(){
		
	}
	
	public void execute(Execution execution) {
		FlowToken token = execution.getFlowToken();
		    
		//步骤1：收集所有的离开路径
		Set<String> transitionNames = null; 
		Map<String , FlowToken> forkedTokens = new HashMap<String , FlowToken>();
		
		//默认情况下，分支节点将对所有路径进行覆盖
		Map<String , Transition> leavingTransitionsMap = getLeavingTransitionsMap();
		if (decisionConditions != null && !decisionConditions.isEmpty()) {
			Iterator<DecisionCondition> iter = decisionConditions.iterator();
	        while (iter.hasNext()) {
	        	DecisionCondition decisionCondition = iter.next();
	        	Boolean result = ExpressionEvaluator.evaluate(decisionCondition.getExpression(), execution, Boolean.class);
	        	if (result != null && !result) {
	        		//条件为false移除
	        		leavingTransitionsMap.remove(decisionCondition.getTransitionName());
	        	}
	        }
		}
		
		transitionNames = leavingTransitionsMap.keySet();

		//步骤2：为每个分支路径创建一个Token
		Iterator<String> iter = transitionNames.iterator();
		while (iter.hasNext()) {
			String transitionName = (String) iter.next();
			String forkedTokenName = getTokenName(token , transitionName);
			forkedTokens.put(transitionName, new FlowToken(token ,forkedTokenName)); 
		}
		
		//步骤3：延每个分支出口下发token
		iter = forkedTokens.keySet().iterator();
		while( iter.hasNext() ) {			
			String leavingTransitionName = iter.next();
			FlowToken childToken = forkedTokens.get(leavingTransitionName);
			Execution childExecution = new Execution(childToken);
			if (leavingTransitionName!=null) {
				leaveNode(childExecution, leavingTransitionName);
			} else {
				leaveNode(childExecution);
			}
		}
	}

	protected String getTokenName(FlowToken parent, String transitionName) {
		String tokenName = null;
		if ( transitionName != null ) {
			if ( ! parent.hasChild( transitionName ) ) {
				tokenName = transitionName;
			} else {
				int i = 2;
				tokenName = transitionName + Integer.toString( i );
				while ( parent.hasChild( tokenName ) ) {
					i++;
					tokenName = transitionName + Integer.toString( i );
				}
			}
		} else { // no transition name
			int size = ( parent.getChildren()!=null ? parent.getChildren().size()+1 : 1 );
			tokenName = Integer.toString(size);
		}
		return tokenName;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void read(Element nodeElement, WfdlXmlReader wfdlXmlReader) {
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
			if (expression != null) {
	 		    //表达式验证
	 	    	try {
	 	    		ExpressionEvaluator.test(expression, flowDefine.getVariableDeclares(), Boolean.class);
	 			} catch (IllegalExpressionException e) {
	 				throw new ParserException("分支节点“" + name + "”中的表达式“" + expression + "”解析出错", e);
	 			}
	 	    }

			if (transitionName != null && expression != null) {
				decisionConditions.add(new DecisionCondition(transitionName, expression));
			}
		}
	}

	public List<DecisionCondition> getDecisionConditions() {
		return decisionConditions;
	}

	public void setDecisionConditions(List<DecisionCondition> decisionConditions) {
		this.decisionConditions = decisionConditions;
	}
}
