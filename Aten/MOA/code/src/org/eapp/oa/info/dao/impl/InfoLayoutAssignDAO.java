package org.eapp.oa.info.dao.impl;

// default package

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.info.dao.IInfoLayoutAssignDAO;
import org.eapp.oa.info.hbean.InfoLayoutAssign;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;
import org.hibernate.Query;
import org.hibernate.criterion.Example;


/**
 * A data access object (DAO) providing persistence and search support for
 * Archives entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Archives
 * @author MyEclipse Persistence Tools
 */

public class InfoLayoutAssignDAO extends BaseHibernateDAO implements IInfoLayoutAssignDAO {

	private static final Log log = LogFactory.getLog(InfoLayoutAssignDAO.class);

	/* (non-Javadoc)
	 */
	public InfoLayoutAssign findById(java.lang.String id) {
		log.debug("getting InfoLayoutAssign instance with id: " + id);
		try {
			InfoLayoutAssign instance = (InfoLayoutAssign) getSession().get(InfoLayoutAssign.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	public List<InfoLayoutAssign> findByExample(InfoLayoutAssign instance) {
		log.debug("finding InfoLayoutAssign instance by example");
		try {
			List<InfoLayoutAssign> results = getSession().createCriteria("InfoLayoutAssign")
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
	public List<InfoLayoutAssign> findByProperty(String propertyName, Object value) {
		log.debug("finding InfoLayoutAssign instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from InfoLayoutAssign as model where model."
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
	@SuppressWarnings("unchecked")
	public List<InfoLayoutAssign> findAll() {
		log.debug("finding all InfoLayoutAssign instances");
		try {
			String queryString = "from InfoLayoutAssign";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}


	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<InfoLayoutAssign> findBindById(String id, int key, int flag) {
		log.debug("finding binding by id " + id);
		try {
			String queryString = "from InfoLayoutAssign as model where model.infoLayout.id=:id " 
					+ " and model.type=:key and model.flag=:flag";
			
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setString("id",id);
			queryObject.setInteger("key", key);
			queryObject.setInteger("flag", flag);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("finding binding by id failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 */
	@Override
	public void delBindById(String id, int key, int flag) {
		log.debug("delete binding by id " + id);
		try {
			String queryString = "delete from InfoLayoutAssign as model where model.infoLayout.id=:id " 
					+ " and model.type=:key and model.flag=:flag";
			
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setString("id",id);
			queryObject.setInteger("key", key);
			queryObject.setInteger("flag", flag);
			queryObject.executeUpdate();
		} catch (RuntimeException re) {
			log.error("delete binding by id failed", re);
			throw re;
		}
	}
	
}