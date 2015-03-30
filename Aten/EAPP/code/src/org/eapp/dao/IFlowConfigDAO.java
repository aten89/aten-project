package org.eapp.dao;

import java.util.List;

import org.eapp.dao.param.FlowConfigQueryParameters;
import org.eapp.hbean.FlowConfig;
import org.eapp.util.hibernate.ListPage;


public interface IFlowConfigDAO extends IBaseHibernateDAO {

	/**
	 * 通过流程标识获取流程 
	 * @param flowKey
	 * @param flowFlag
	 * @return
	 */
	public FlowConfig findByFlowKey(String flowKey, int flowFlag);
	
	public List<FlowConfig> findByflowClass(String flowClass, int flowFlag);
	
	ListPage<FlowConfig> queryFlowConfig(FlowConfigQueryParameters qp);

    /**
     * 通过流程名称、标识及id判断名称是否重复
     */
    public boolean testFlowNameRepeat(String flowName, String flowKey);
    
    public boolean testFlowKeyRepeat(String flowKey);
}