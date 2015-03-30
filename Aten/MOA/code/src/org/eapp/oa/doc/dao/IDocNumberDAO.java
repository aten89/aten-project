package org.eapp.oa.doc.dao;

import java.util.List;

import org.eapp.oa.doc.hbean.DocNumber;
import org.eapp.oa.system.dao.IBaseHibernateDAO;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public interface IDocNumberDAO extends IBaseHibernateDAO {

	public DocNumber findById(java.lang.String id);

	public List<DocNumber> findByExample(DocNumber instance);

	public List<DocNumber> findByProperty(String propertyName, Object value);

	public List<DocNumber> findByDocWord(Object docword);

	public List<DocNumber> findByYearprefix(Object yearprefix);

	public List<DocNumber> findByCurrentyear(Object currentyear);

	public List<DocNumber> findByYearpostfix(Object yearpostfix);

	public List<DocNumber> findByOrderprefix(Object orderprefix);

	public List<DocNumber> findByOrdernumber(Object ordernumber);

	public List<DocNumber> findByOrderpostfix(Object orderpostfix);

	public List<DocNumber> findByDescription(Object description);

	public List<DocNumber> findAll();
}
