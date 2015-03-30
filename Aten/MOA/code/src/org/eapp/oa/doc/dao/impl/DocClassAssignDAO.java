package org.eapp.oa.doc.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.doc.dao.IDocClassAssignDAO;
import org.eapp.oa.doc.hbean.DocClassAssign;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public class DocClassAssignDAO extends BaseHibernateDAO implements
		IDocClassAssignDAO {
	private static final Log log = LogFactory.getLog(DocClassAssignDAO.class);
	// property constants
	public static final String TYPE = "type";
	public static final String ASSIGNKEY = "assignkey";

	/* (non-Javadoc)
	 */
	public DocClassAssign findById(java.lang.String id) {
		log.debug("getting DocClassAssign instance with id: " + id);
		try {
			DocClassAssign instance = (DocClassAssign) getSession().get(
					DocClassAssign.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	public List<DocClassAssign> findByExample(DocClassAssign instance) {
		log.debug("finding DocClassAssign instance by example");
		try {
			List<DocClassAssign> results = getSession().createCriteria("DocClassAssign")
					.add(Example.create(instance)).list();
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
	public List<DocClassAssign> findByProperty(String propertyName, Object value) {
		log.debug("finding DocClassAssign instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from DocClassAssign as model where model."
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
	public List<DocClassAssign> findByType(Object type) {
		return findByProperty(TYPE, type);
	}

	/* (non-Javadoc)
	 */
	public List<DocClassAssign> findByAssignkey(Object assignkey) {
		return findByProperty(ASSIGNKEY, assignkey);
	}

	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	public List<DocClassAssign> findAll() {
		log.debug("finding all DocClassAssign instances");
		try {
			String queryString = "from DocClassAssign";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	@Override
	public void delBindById(String id, int type) {
		log.debug("delete binding by id " + id);
		try {
			String queryString = "delete from DocClassAssign as model where model.docClass.id=:id " 
					+ " and model.type=:type";
			
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setString("id",id);
			queryObject.setInteger("type", type);
//			queryObject.setInteger("flag", flag);
			queryObject.executeUpdate();
		} catch (RuntimeException re) {
			log.error("delete binding by id failed", re);
			throw re;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DocClassAssign> findBindById(String id, int type) {
		log.debug("finding binding by id " + id);
		try {
			String queryString = "from DocClassAssign as model where model.docClass.id=:id " 
					+ " and model.type=:type";
			
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setString("id",id);
			queryObject.setInteger("type", type);
//			queryObject.setInteger("flag", flag);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("finding binding by id failed", re);
			throw re;
		}
	}
}
