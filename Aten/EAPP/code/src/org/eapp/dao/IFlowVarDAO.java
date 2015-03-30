package org.eapp.dao;


import java.util.List;

import org.eapp.dao.param.FlowVarQueryParameters;
import org.eapp.hbean.FlowVar;
import org.eapp.util.hibernate.ListPage;


/**
 * 数据访问接口
 * @version
 */
public interface IFlowVarDAO extends IBaseHibernateDAO {
	
	/**
	 * 根据ID加载实例
	 * @param id
	 * @return
	 */
	public FlowVar findById(String id);


	/**
	 * 根据动作名模糊查找
	 * @param aqp
	 * @return
	 */
	public ListPage<FlowVar> queryFlowVars(FlowVarQueryParameters aqp);
	
	/**
	 * 通过流程类别获取流程变量（包括全局的）
	 * @param flowClass
	 * @return
	 */
	public List<FlowVar> findFlowVars(String flowClass);
	
	public boolean testNameRepeat(String name, String flowClass, boolean globalFlag, String varId);
}