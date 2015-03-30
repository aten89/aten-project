package org.eapp.oa.info.dao;

import java.util.List;

import org.eapp.oa.info.dto.InfoFormQueryParameters;
import org.eapp.oa.info.hbean.InfoForm;
import org.eapp.oa.system.dao.IBaseHibernateDAO;
import org.eapp.util.hibernate.ListPage;

public interface IInfoFormDAO extends IBaseHibernateDAO {

	public InfoForm findById(java.lang.String id);

	public List<InfoForm> findByExample(InfoForm instance);

	public List<InfoForm> findByProperty(String propertyName, Object value);

	public List<InfoForm> findBySubject(Object subject);

	public List<InfoForm> findByReceiveFrom(Object receiveFrom);
	
	public List<InfoForm> findByCopyInfoFormId(Object receiveFrom);

	public List<InfoForm> findAll();

	public List<InfoForm> queryDealingInfoForm(String userId);
	
	public ListPage<InfoForm> queryArchInfoForm(InfoFormQueryParameters rqp, String userAccountId);
	
	public ListPage<InfoForm> queryTrackInfoForm(InfoFormQueryParameters rqp, String userAccountId);
	
	public List<InfoForm> findInfoForm(String userAccountId, int infoStatus);
}