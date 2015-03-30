
package org.eapp.oa.kb.dao.impl;

// default package

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.kb.dao.IIndexTaskDAO;
import org.eapp.oa.lucene.IndexTask;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;
import org.hibernate.Query;
import org.hibernate.criterion.Example;



public class IndexTaskDAO extends BaseHibernateDAO implements IIndexTaskDAO {

	private static final Log log = LogFactory.getLog(IndexTaskDAO.class);

	public IndexTask findById(java.lang.String id) {
		log.debug("getting IndexTask instance with id: " + id);
		try {
			IndexTask instance = (IndexTask) getSession().get(IndexTask.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<IndexTask> findByExample(IndexTask instance) {
		log.debug("finding IndexTask instance by example");
		try {
			List<IndexTask> results = getSession().createCriteria(IndexTask.class)
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<IndexTask> findAll() {
		log.debug("finding all IndexTask instances");
		try {
			String queryString = "from IndexTask";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	@Override
	public long countIndexTask() {
		try {
			String queryString = "select count(*) from IndexTask";
			Query queryObject = getSession().createQuery(queryString);
			Long count = (Long)queryObject.uniqueResult();
			return count.longValue();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IndexTask> findIndexTask(int count) {
		try {
			String queryString = "from IndexTask";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setMaxResults(count);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
}

