package org.eapp.oa.hr.dao;

import java.util.List;

import org.eapp.oa.hr.hbean.HRFlowConf;
import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IHRFlowConfDAO extends IBaseHibernateDAO {
	
	public HRFlowConf findById(java.lang.String id);

	public HRFlowConf findByGroupName(String groupName);
	
	public List<HRFlowConf> findAll();
	
	public boolean checkGroupName(String groupName);
}