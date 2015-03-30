package org.eapp.oa.flow.dao;


import java.util.List;

import org.eapp.oa.flow.dto.FlowConfigQueryParameters;
import org.eapp.oa.flow.hbean.FlowConfig;
import org.eapp.oa.system.dao.IBaseHibernateDAO;
import org.eapp.util.hibernate.ListPage;


public interface IFlowConfigDAO extends IBaseHibernateDAO {

	public FlowConfig findById(String id);

	public List<FlowConfig> findByflowClass(String flowClass);
	
	ListPage<FlowConfig> queryFlowConfig(FlowConfigQueryParameters qp);

	/**
	 * 通过流程标识获取流程 
	 * @param flowKey
	 * @param flowFlag
	 * @return
	 */
	public FlowConfig findFlowByFlowKey(String flowKey, int flowFlag);
	
    /**
     * 通过流程名称、标识及id判断名称是否重复
     * @param flowKey
     * @param flowFlag
     * @param id
     * @return
     */
    public boolean isFlowNameRepeat(String flowName, String flowKey);
}