package org.eapp.oa.reimburse.dao;

import java.util.List;

import org.eapp.oa.reimburse.hbean.ReimFlowConf;
import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IReimFlowConfDAO extends IBaseHibernateDAO {
	
	public ReimFlowConf findById(java.lang.String id);

	public ReimFlowConf findByGroupName(String groupName);
	
	public List<ReimFlowConf> findAll();
	
	public boolean checkGroupName(String groupName);
}