/**
 * 
 */
package org.eapp.blo;


import java.util.List;

import org.eapp.dao.param.FlowHandlerQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.FlowHandler;
import org.eapp.util.hibernate.ListPage;


/**
 * @version
 */
public interface IFlowHandlerBiz {
	/**
	 * 查看
	 * @param varId
	 * @return
	 */
	public FlowHandler getFlowHandler(String handId);
	
	/**
	 * 删除
	 * @param handId
	 * @return
	 */
	public FlowHandler deleteFlowHandler(String handId);

	/**
	 * 新增
	 * @param flowClass
	 * @param name
	 * @param handlerClass
	 * @param type
	 * @param globalFlag
	 * @return
	 * @throws EappException
	 */
	public FlowHandler addFlowHandler(String flowClass, String name, String handlerClass, String type, boolean globalFlag) throws EappException;

	/**
	 * 修改
	 * @param varId
	 * @param flowClass
	 * @param name
	 * @param handlerClass
	 * @param type
	 * @param globalFlag
	 * @throws EappException
	 */
	public FlowHandler modifyFlowHandler(String varId, String flowClass, String name, String handlerClass, String type, boolean globalFlag) throws EappException;

	/**
	 * 查询
	 * @param aqp
	 * @return
	 */
	public ListPage<FlowHandler> queryFlowHandlers(FlowHandlerQueryParameters aqp);

	/**
	 * 通过流程类别获取流程Handler（包括全局的）
	 * @param flowClass
	 * @return
	 */
	public List<FlowHandler> getFlowHandlers(String flowClass, String handlerType);
	
}
