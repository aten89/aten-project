package org.eapp.oa.info.dao;

import java.util.List;

import org.eapp.oa.info.hbean.InfoLayoutAssign;
import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IInfoLayoutAssignDAO extends IBaseHibernateDAO {

	public InfoLayoutAssign findById(java.lang.String id);

	public List<InfoLayoutAssign> findByExample(InfoLayoutAssign instance);

	public List<InfoLayoutAssign> findByProperty(String propertyName, Object value);

	public List<InfoLayoutAssign> findAll();
	
	public List<InfoLayoutAssign> findBindById(String id , int key, int flag);
	
	public void delBindById(String id , int key, int flag);

}