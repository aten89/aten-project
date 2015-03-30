package org.eapp.oa.info.dao;

import java.util.List;

import org.eapp.oa.info.hbean.InfoLayout;
import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IInfoLayoutDAO extends IBaseHibernateDAO {
	
	public InfoLayout findById(java.lang.String id);

	public List<InfoLayout> findByExample(InfoLayout instance);

	public List<InfoLayout> findByProperty(String propertyName, Object value);

	public List<InfoLayout> findBySubject(Object subject);

	public List<InfoLayout> findByReceiveFrom(Object receiveFrom);

	public List<InfoLayout> findAll();
	
	public List<InfoLayout> findByName(Object name);
	
	public int getDisplayOrder();
	
	public List<InfoLayout> findAssignLayout(String userAccountId,
			List<String> groupNames, List<String> postNames, int flag, String name);
	
	public List<InfoLayout> findByDisplayOrder();

}