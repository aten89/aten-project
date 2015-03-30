package org.eapp.oa.kb.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.kb.dao.IKnowledgeDAO;
import org.eapp.oa.kb.dto.KnowledgeQueryParameters;
import org.eapp.oa.kb.hbean.Knowledge;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * 知识点DAO
 * 
 */
public class KnowledgeDAO extends BaseHibernateDAO implements IKnowledgeDAO {

    /**
     * 日志
     */
    private static final Log LOG = LogFactory.getLog(KnowledgeDAO.class);

    @Override
    public Knowledge findById(java.lang.String id) {
        LOG.debug("getting Knowledge instance with id: " + id);
        try {
            Knowledge instance = (Knowledge) getSession().get(Knowledge.class, id);
            return instance;
        } catch (RuntimeException re) {
            LOG.error("get failed", re);
            throw re;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Knowledge> findByExample(Knowledge instance) {
        LOG.debug("finding Knowledge instance by example");
        try {
            List<Knowledge> results = getSession().createCriteria("Knowledge").add(Example.create(instance)).list();
            LOG.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            LOG.error("find by example failed", re);
            throw re;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Knowledge> findByProperty(String propertyName, Object value) {
        LOG.debug("finding Knowledge instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from Knowledge as knowledge where knowledge." + propertyName + "= ?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            LOG.error("find by property name failed", re);
            throw re;
        }
    }

    @Override
    public int getCountByKnowledgeClass(String classId) {
        try {
            String queryString = "select count(*) from Knowledge as knowledge where knowledge.knowledgeClass.id = :classId";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setString("classId", classId);
            Long value = (Long) queryObject.uniqueResult();
            if (value == null) {
                return 0;
            }
            return value.intValue();

        } catch (RuntimeException re) {
            LOG.error("getCountByKnowledgeClass failed", re);
            throw re;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Knowledge> findAll() {
        LOG.debug("finding all Knowledge instances");
        try {
            String queryString = "from Knowledge";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            LOG.error("find all failed", re);
            throw re;
        }
    }

    @Override
    public ListPage<Knowledge> queryKnowledge(KnowledgeQueryParameters kqp) {
        if (kqp == null) {
            throw new IllegalArgumentException("查询参数对象为null");
        }
        try {
            StringBuffer hql = new StringBuffer("select kb from Knowledge as kb where 1=1 ");

            // 知识类别
            if (kqp.getKnowledgeClassIds() != null && kqp.getKnowledgeClassIds().size() > 0) {
                hql.append(" and kb.knowledgeClass.id in (:knowledgeClassIds)");
            }
            if (kqp.getSubject() != null) {
                hql.append(" and kb.subject like :subject ");
                kqp.toArountParameter("subject");
            }

            if (kqp.getBeginPublishDate() != null) {
                hql.append(" and kb.publishDate >= :beginPublishDate");
            }

            if (kqp.getEndPublishDate() != null) {
                hql.append(" and kb.publishDate <= :endPublishDate");
            }

            if (kqp.getPublisher() != null) {
                hql.append(" and kb.publisher = :publisher ");
            }
            
            if (kqp.getFirstType() != null) {
                hql.append(" and kb.firstType = :firstType ");
            }
            
            if (kqp.getSecondType() != null) {
                hql.append(" and kb.secondType = :secondType ");
            }

            // 知识类别
            if (kqp.getKnowledgeClass() != null) {
                hql.append(" and kb.knowledgeClass.id = :knowledgeClass ");
            }

            if (kqp.getStatus() != null) {
                hql.append(" and kb.status = :status ");
            }

            return new CommQuery<Knowledge>().queryListPage(kqp, kqp.appendOrders(hql, "kb"), getSession());

        } catch (RuntimeException re) {
            LOG.error("queryKnowledge faild", re);
            return new ListPage<Knowledge>();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> iteratorKnowledgeClassList(String KnowledgeClassId) {
        StringBuffer queryString = new StringBuffer("select kc.id from KnowledgeClass as kc where 1=1 ");
        List<String> listString = new ArrayList<String>();
        if (KnowledgeClassId != null) {
            queryString.append(" and kc.parentClass.id='" + KnowledgeClassId + "'");
            listString.add(KnowledgeClassId);
        }
        Query queryObject = getSession().createQuery(queryString.toString());
        List<String> list = queryObject.list();
        if (!list.isEmpty()) {
            for (String knowledgeClassId : list) {
                listString.addAll(iteratorKnowledgeClassList(knowledgeClassId));
            }
        }
        return listString;
    }

}
