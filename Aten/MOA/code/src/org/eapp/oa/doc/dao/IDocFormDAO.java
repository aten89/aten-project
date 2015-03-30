package org.eapp.oa.doc.dao;

import java.util.List;

import org.eapp.oa.doc.dto.DocFormQueryParameters;
import org.eapp.oa.doc.hbean.DocForm;
import org.eapp.oa.system.dao.IBaseHibernateDAO;
import org.eapp.util.hibernate.ListPage;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public interface IDocFormDAO extends IBaseHibernateDAO {

	public DocForm findById(java.lang.String id);

	public List<DocForm> findByExample(DocForm instance);

	public List<DocForm> findByProperty(String propertyName, Object value);

	public List<DocForm> findByDraftsman(Object draftsman);

	public List<DocForm> findByGroupname(Object groupname);

	public List<DocForm> findByDocNumber(Object docnumber);

	public List<DocForm> findBySubmitto(Object submitto);

	public List<DocForm> findByCopyto(Object copyto);

	public List<DocForm> findBySubject(Object subject);

	public List<DocForm> findByFlowinstanceid(Object flowinstanceid);

	public List<DocForm> findByPassed(Object passed);

	public List<DocForm> findByUrgency(Object urgency);

	public List<DocForm> findBySecurityclass(Object securityclass);

	public List<DocForm> findAll();
	
	public List<DocForm> findDocForm(String userAccountId, int docStatus,int fileClass);
	
	public List<DocForm> queryDealingDocForm(String userId,int fileClass);
	
	public ListPage<DocForm> queryTrackDocForm(DocFormQueryParameters rqp, String userAccountId,int fileClass);
	
	public ListPage<DocForm> queryArchDocForm(DocFormQueryParameters rqp, String userAccountId,int fileClass);
}
