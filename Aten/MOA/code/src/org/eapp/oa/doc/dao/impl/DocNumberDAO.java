package org.eapp.oa.doc.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.doc.dao.IDocNumberDAO;
import org.eapp.oa.doc.hbean.DocNumber;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public class DocNumberDAO extends BaseHibernateDAO implements IDocNumberDAO {
	private static final Log log = LogFactory.getLog(DocNumberDAO.class);
	// property constants
	public static final String DOCWORD = "docWord";
	public static final String YEARPREFIX = "yearPrefix";
	public static final String CURRENTYEAR = "currentYear";
	public static final String YEARPOSTFIX = "yearPostfix";
	public static final String ORDERPREFIX = "orderPrefix";
	public static final String ORDERNUMBER = "orderNumber";
	public static final String ORDERPOSTFIX = "orderPostfix";
	public static final String DESCRIPTION = "description";

	
	/* (non-Javadoc)
	 */
	public DocNumber findById(java.lang.String id) {
		log.debug("getting DocNumber instance with id: " + id);
		try {
			DocNumber instance = (DocNumber) getSession().get(
					DocNumber.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	public List<DocNumber> findByExample(DocNumber instance) {
		log.debug("finding DocNumber instance by example");
		try {
			List<DocNumber> results = getSession().createCriteria("DocNumber").add(
					Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	public List<DocNumber> findByProperty(String propertyName, Object value) {
		log.debug("finding DocNumber instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from DocNumber as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 */
	public List<DocNumber> findByDocWord(Object docword) {
		return findByProperty(DOCWORD, docword);
	}

	/* (non-Javadoc)
	 */
	public List<DocNumber> findByYearprefix(Object yearprefix) {
		return findByProperty(YEARPREFIX, yearprefix);
	}

	/* (non-Javadoc)
	 */
	public List<DocNumber> findByCurrentyear(Object currentyear) {
		return findByProperty(CURRENTYEAR, currentyear);
	}

	/* (non-Javadoc)
	 */
	public List<DocNumber> findByYearpostfix(Object yearpostfix) {
		return findByProperty(YEARPOSTFIX, yearpostfix);
	}

	/* (non-Javadoc)
	 */
	public List<DocNumber> findByOrderprefix(Object orderprefix) {
		return findByProperty(ORDERPREFIX, orderprefix);
	}

	/* (non-Javadoc)
	 */
	public List<DocNumber> findByOrdernumber(Object ordernumber) {
		return findByProperty(ORDERNUMBER, ordernumber);
	}

	/* (non-Javadoc)
	 */
	public List<DocNumber> findByOrderpostfix(Object orderpostfix) {
		return findByProperty(ORDERPOSTFIX, orderpostfix);
	}

	/* (non-Javadoc)
	 */
	public List<DocNumber> findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	public List<DocNumber> findAll() {
		log.debug("finding all DocNumber instances");
		try {
			String queryString = "from DocNumber";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
}
