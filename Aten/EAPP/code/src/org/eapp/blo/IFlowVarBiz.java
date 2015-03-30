/**
 * 
 */
package org.eapp.blo;

import java.util.List;

import org.eapp.dao.param.FlowVarQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.FlowVar;
import org.eapp.util.hibernate.ListPage;


/**
 * @version
 */
public interface IFlowVarBiz {

	/**
	 * 查看
	 * @param varId
	 * @return
	 */
	public FlowVar getFlowVar(String varId);
	
	/**
	 * 删除
	 * @param varId
	 * @return
	 */
	public FlowVar deleteFlowVar(String varId);

	/**
	 * 新增
	 * @param flowClass
	 * @param name
	 * @param displayName
	 * @param type
	 * @param notNull
	 * @param globalFlag
	 * @return
	 * @throws EappException
	 */
	public FlowVar addFlowVar(String flowClass, String name, String displayName, String type, boolean notNull, boolean globalFlag) throws EappException;

	/**
	 * 修改
	 * @param varId
	 * @param flowClass
	 * @param name
	 * @param displayName
	 * @param type
	 * @param notNull
	 * @param globalFlag
	 * @throws EappException
	 */
	public FlowVar modifyFlowVar(String varId, String flowClass, String name, String displayName, String type, boolean notNull, boolean globalFlag) throws EappException;

	/**
	 * 查询
	 * @param aqp
	 * @return
	 */
	public ListPage<FlowVar> queryFlowVars(FlowVarQueryParameters aqp);

	/**
	 * 通过流程类别获取流程变量（包括全局的）
	 * @param flowClass
	 * @return
	 */
	public List<FlowVar> getFlowVars(String flowClass);
	
	/**
	 * 验证表达式
	 * @param expression
	 */
	public void testExpression(String expression, String returnType, String flowVarsJson) throws EappException;
	
}
