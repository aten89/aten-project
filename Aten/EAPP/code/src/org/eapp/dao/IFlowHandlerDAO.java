package org.eapp.dao;

import java.util.List;

import org.eapp.dao.param.FlowHandlerQueryParameters;
import org.eapp.hbean.FlowHandler;
import org.eapp.util.hibernate.ListPage;


/**
 * 数据访问接口
 * @version
 */
public interface IFlowHandlerDAO extends IBaseHibernateDAO {

	/**
	 * 根据ID加载实例
	 * @param id
	 * @return
	 */
	public FlowHandler findById(String id);
	
	/**
	 * 根据模糊查找
	 * @param aqp
	 * @return
	 */
	public ListPage<FlowHandler> queryFlowHandlers(FlowHandlerQueryParameters aqp);
	
	/**
	 * 通过流程类别获取流程Handler（包括全局的）
	 * @param flowClass
	 * @return
	 */
	public List<FlowHandler> findFlowHandlers(String flowClass, String handlerType);
	
	public boolean testHandlerClassRepeat(String handlerClass, String flowClass, boolean globalFlag, String handId);
}