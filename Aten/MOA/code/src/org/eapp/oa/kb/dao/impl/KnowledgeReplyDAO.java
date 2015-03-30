
package org.eapp.oa.kb.dao.impl;

// default package

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.kb.dao.IKnowledgeReplyDAO;
import org.eapp.oa.kb.dto.KnowledgeReplyQueryParameters;
import org.eapp.oa.kb.hbean.KnowledgeReply;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;


public class KnowledgeReplyDAO extends BaseHibernateDAO implements IKnowledgeReplyDAO {

	private static final Log log = LogFactory.getLog(KnowledgeReplyDAO.class);

	public KnowledgeReply findById(java.lang.String id) {
		log.debug("getting Knowledgereply instance with id: " + id);
		try {
			KnowledgeReply instance = (KnowledgeReply) getSession().get(KnowledgeReply.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<KnowledgeReply> findByExample(KnowledgeReply instance) {
		log.debug("finding Knowledgereply instance by example");
		try {
			List<KnowledgeReply> results = getSession().createCriteria("Knowledgereply")
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
	public List<KnowledgeReply> findByProperty(String propertyName, Object value) {
		log.debug("finding Knowledgereply instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from KnowledgeReply as knowledgereply where knowledgereply."
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
	public List<KnowledgeReply> findAll() {
		log.debug("finding all Knowledgereply instances");
		try {
			String queryString = "from Knowledgereply";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public ListPage<KnowledgeReply> queryKnowledgeReply(KnowledgeReplyQueryParameters krqp) {
		if (krqp == null)  {
			throw new IllegalArgumentException("查询参数对象为null");
		}
		try {
			StringBuffer queryString = new StringBuffer("from KnowledgeReply as knowledgereply where 1=1 ");
			if(krqp.getKnowledgeClass()!=null){
				queryString.append(" and knowledgereply.knowledge.id = :knowledge");
			}
			
			return new CommQuery<KnowledgeReply>().queryListPage(krqp, krqp.appendOrders(queryString, "knowledgereply"), getSession());
		} catch (RuntimeException re) {
			re.printStackTrace();
			return new ListPage<KnowledgeReply>();
		}
	}
}

