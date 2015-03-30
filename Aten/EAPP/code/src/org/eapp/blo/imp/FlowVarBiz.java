/**
 * 
 */
package org.eapp.blo.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IFlowVarBiz;
import org.eapp.dao.IFlowVarDAO;
import org.eapp.dao.param.FlowVarQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.FlowVar;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.def.VariableDeclare;
import org.eapp.workflow.expression.ExpressionEvaluator;

import com.alibaba.fastjson.JSON;

/**
 * 动作业务逻辑层
 * @version 
 */
public class FlowVarBiz implements IFlowVarBiz {
	private static final Log log = LogFactory.getLog(FlowVarBiz.class);
	
	private IFlowVarDAO flowVarDAO;

	public void setFlowVarDAO(IFlowVarDAO flowVarDAO) {
		this.flowVarDAO = flowVarDAO;
	}
	
	@Override
	public FlowVar getFlowVar(String varId) {
		return flowVarDAO.findById(varId);
	}
	
	@Override
	public FlowVar deleteFlowVar(String varId) {
		FlowVar flowVar = flowVarDAO.findById(varId);
		if(flowVar == null){
			throw new IllegalArgumentException("ID在数据库中不存在");
		}
		flowVarDAO.delete(flowVar);
		return flowVar;
	}

	@Override
	public FlowVar addFlowVar(String flowClass, String name, String displayName, 
			String type, boolean notNull, boolean globalFlag) throws EappException {
		if(StringUtils.isBlank(name)){
			throw new IllegalArgumentException("变量名称不能为空");
		}
		if(StringUtils.isBlank(flowClass) && !globalFlag){
			throw new IllegalArgumentException("非全局变量的流程类别不能为空");
		}
		
		if(flowVarDAO.testNameRepeat(name, flowClass, globalFlag, null)){
			throw new EappException("变量名称不能重复");
		}
		FlowVar fh = new FlowVar();
		fh.setGlobalFlag(globalFlag);
		if (!globalFlag) {
			fh.setFlowClass(flowClass);
		}
		fh.setName(name);
		fh.setDisplayName(displayName);
		fh.setType(type);
		fh.setNotNull(notNull);
		flowVarDAO.save(fh);
		return fh;
	}

	public FlowVar modifyFlowVar(String varId, String flowClass, String name, String displayName, 
			String type, boolean notNull, boolean globalFlag) throws EappException {
		if(StringUtils.isBlank(varId) || StringUtils.isBlank(name)){
			throw new IllegalArgumentException();
		}
		if(StringUtils.isBlank(flowClass) && !globalFlag){
			throw new IllegalArgumentException("非全局变量的流程类别不能为空");
		}
		
		FlowVar fh = flowVarDAO.findById(varId);

		if(flowVarDAO.testNameRepeat(name, flowClass, globalFlag, fh.getVarId())){
			throw new EappException("变量名称不能重复");
		}

		fh.setGlobalFlag(globalFlag);
		if (!globalFlag) {
			fh.setFlowClass(flowClass);
		} else {
			fh.setFlowClass(null);
		}
		fh.setName(name);
		fh.setDisplayName(displayName);
		fh.setType(type);
		fh.setNotNull(notNull);
		flowVarDAO.update(fh);
		return fh;
	}


	@Override
	public ListPage<FlowVar> queryFlowVars(FlowVarQueryParameters aqp) {
		return flowVarDAO.queryFlowVars(aqp);
	}
	
	@Override
	public List<FlowVar> getFlowVars(String flowClass) {
		return flowVarDAO.findFlowVars(flowClass);
	}
	
	@Override
	public void testExpression(String expression, String returnType, String flowVarsJson) throws EappException {
		List<FlowVar> vars = JSON.parseArray(flowVarsJson, FlowVar.class);
//		List<FlowVar> vars = flowConfigDAO.findFlowVars(flowClass);
		Map<String, VariableDeclare> vds = new HashMap<String, VariableDeclare>();
		if (vars != null && !vars.isEmpty()) {
			for (FlowVar var : vars) {
				vds.put(var.getName(), new VariableDeclare(var.getName(), var.getType(), var.getNotNull(), var.getDisplayName()));
			}
		}
		try {
			Class<?> type = null;
			if ("string".equals(returnType)) {
				type = String.class;
			} else if ("boolean".equals(returnType)) {
				type = Boolean.class;
			} else {
				type = Object.class;
			}
			ExpressionEvaluator.test(expression, vds, type);
		} catch(Exception e) {
			log.warn("表达式验证失败", e);
			throw new EappException(e.getMessage());
		}
		
	}
}
