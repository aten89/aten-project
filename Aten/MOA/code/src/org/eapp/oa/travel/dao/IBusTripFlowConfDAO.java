package org.eapp.oa.travel.dao;

import java.util.List;

import org.eapp.oa.system.dao.IBaseHibernateDAO;
import org.eapp.oa.travel.hbean.BusTripFlowConf;

public interface IBusTripFlowConfDAO extends IBaseHibernateDAO {
	
	public BusTripFlowConf findById(java.lang.String id);

	public BusTripFlowConf findByGroupName(String groupName);
	
	public List<BusTripFlowConf> findAll();
	
	public boolean checkGroupName(String groupName);
}