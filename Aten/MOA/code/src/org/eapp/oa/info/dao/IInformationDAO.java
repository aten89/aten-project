package org.eapp.oa.info.dao;

import java.util.List;

import org.eapp.oa.info.dto.InformationQueryParameters;
import org.eapp.oa.info.hbean.Information;
import org.eapp.oa.system.dao.IBaseHibernateDAO;
import org.eapp.util.hibernate.ListPage;

public interface IInformationDAO extends IBaseHibernateDAO {

	public Information findById(java.lang.String id);

	public List<Information> findByExample(Information instance);

	public List<Information> findByProperty(String propertyName, Object value);

	public List<Information> findBySubject(Object subject);

	public List<Information> findByReceiveFrom(Object receiveFrom);

	public List<Information> findAll();
	
	public ListPage<Information> queryInformation(InformationQueryParameters qp);
	
	/**
	 * 查找所有已有信息的板块
	 * @return
	 */
	List<String> findAllInfoLayout();
}