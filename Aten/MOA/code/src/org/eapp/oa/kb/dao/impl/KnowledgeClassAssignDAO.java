
package org.eapp.oa.kb.dao.impl;

// default package

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.kb.dao.IKnowledgeClassAssignDAO;
import org.eapp.oa.kb.hbean.KnowledgeClassAssign;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;


public class KnowledgeClassAssignDAO extends BaseHibernateDAO implements IKnowledgeClassAssignDAO {

	private static final Log log = LogFactory.getLog(KnowledgeClassAssignDAO.class);



	public KnowledgeClassAssign findById(java.lang.String id) {
		log.debug("getting Knowledgeclassassign instance with id: " + id);
		try {
			KnowledgeClassAssign instance = (KnowledgeClassAssign) getSession().get(KnowledgeClassAssign.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<KnowledgeClassAssign> findByExample(KnowledgeClassAssign instance) {
		log.debug("finding Knowledgeclassassign instance by example");
		try {
			List<KnowledgeClassAssign> results = getSession().createCriteria("Knowledgeclassassign")
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
	public List<KnowledgeClassAssign> findByProperty(String propertyName, Object value) {
		log.debug("finding Knowledgeclassassign instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Knowledgeclassassign as knowledgeclassassign where knowledgeclassassign."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<KnowledgeClassAssign> findAll() {
		log.debug("finding all Knowledgeclassassign instances");
		try {
			String queryString = "from Knowledgeclassassign";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<KnowledgeClassAssign> findBindById(String id, int key) {
		log.debug("finding binding by id " + id);
		try {
			String queryString = "from KnowledgeClassAssign as kac where kac.knowledgeClass.id=:id  and kac.type=:key ";
			
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setString("id",id);
			queryObject.setInteger("key", key);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("finding binding by id failed", re);
			throw re;
		}
	}
	public void delBindById(String id, int key) {
		log.debug("delete binding by id " + id);
		try {
			String queryString = "delete from KnowledgeClassAssign as model where model.knowledgeClass.id=:id " 
					+ " and model.type=:key ";
			
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setString("id",id);
			queryObject.setInteger("key", key);
			queryObject.executeUpdate();
		} catch (RuntimeException re) {
			log.error("delete binding by id failed", re);
			throw re;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<KnowledgeClassAssign> findBindById(String id) {
		log.debug("finding binding by id " + id);
		try {
			String queryString = "from KnowledgeClassAssign as kac where kac.knowledgeClass.id=:id";
			
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setString("id",id);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("finding binding by id failed", re);
			throw re;
		}
	}
	
	public void delBindById(String id) {
		log.debug("delete binding by id " + id);
		try {
			String queryString = "delete from KnowledgeClassAssign as model where model.knowledgeClass.id=:id " ;			
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setString("id",id);
			queryObject.executeUpdate();
		} catch (RuntimeException re) {
			log.error("delete binding by id failed", re);
			throw re;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<KnowledgeClassAssign> findBindByIdAndFlag(String id, int key, int flag) {
		log.debug("finding binding by id " + id);
		try {
			String queryString = "from KnowledgeClassAssign as kac where kac.knowledgeClass.id=:id  and kac.type=:key and kac.flag=:flag";
			
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
}

