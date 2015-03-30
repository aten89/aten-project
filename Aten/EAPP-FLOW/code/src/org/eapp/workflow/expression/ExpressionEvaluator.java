/**
 * 
 */
package org.eapp.workflow.expression;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eapp.workflow.WfmException;
import org.eapp.workflow.def.VariableDeclare;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.FlowToken;

/**
 * @author 卓诗垚
 * @version 2.0 
 * Sep 25, 2008
 */
public class ExpressionEvaluator {
	/**
	 * 根据流程上下文，执行表达式
	 * @param expression
	 * @param execution
	 * @return
	 */
	public static <T> T evaluate(String expression, Execution execution, Class<T> returnType) {
		if (expression == null) {
			return null;
		}
		
		FlowToken flowToken = execution.getFlowToken();
		if(flowToken == null){
			throw new WfmException("流程上下文执行异常:表达式执行时，缺少流程令牌");
		}
		
		FlowInstance flowInstance = flowToken.getFlowInstance();
		if(flowInstance == null){
			throw new WfmException("流程上下文执行异常:表达式执行时，缺少流程实例");
		}	
		
		Map<String , ContextVariable> variablesMap = flowInstance.getContextVariables();
		
		Map<String, Object> vars = null;
		if (variablesMap != null && !variablesMap.isEmpty()) {
			vars = new HashMap<String, Object>();
			for (ContextVariable vt : variablesMap.values()) {
				vars.put(vt.getName(), vt.getTypeValue());
			}
		}
		try {
			return ExpressionEngine.eval(expression, vars, returnType);
		} catch(Exception e) {
			throw new IllegalExpressionException("表达式验证失败", e);
		}
	}
	
	/**
	 * 验证表达式合法性
	 * @param expression
	 * @param varTypes
	 * @throws IllegalExpressionException
	 */
	public static void test(String expression, Map<String, VariableDeclare> varTypeMap, Class<?> returnType) {
		if (StringUtils.isBlank(expression)) {
			throw new IllegalExpressionException("表达式为空");
		}
		
		Map<String, Object> vars = null;
		if (varTypeMap != null && !varTypeMap.isEmpty()) {
			vars = new HashMap<String, Object>();
			for (VariableDeclare vt : varTypeMap.values()) {
				vars.put(vt.getName(), vt.getTypeDefaultValue());
			}
		}
		try {
			ExpressionEngine.eval(expression, vars, returnType);
		} catch(Exception e) {
			throw new IllegalExpressionException(e);
		}
	}
}
