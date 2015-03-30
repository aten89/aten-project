package org.eapp.oa.kb.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.info.hbean.InfoLayoutAssign;
import org.eapp.oa.kb.dao.IKnowledgeClassDAO;
import org.eapp.oa.kb.hbean.KnowledgeClass;
import org.eapp.oa.kb.hbean.KnowledgeClassAssign;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * 知识类别DAO
 */
public class KnowledgeClassDAO extends BaseHibernateDAO implements IKnowledgeClassDAO {

    /**
     * 日志
     */
    private static final Log LOG = LogFactory.getLog(KnowledgeClassDAO.class);

    public KnowledgeClass findById(String id) {
        LOG.debug("getting Knowledgeclass instance with id: " + id);
        try {
            return (KnowledgeClass) getSession().get(KnowledgeClass.class, id);
        } catch (RuntimeException re) {
            LOG.error("get failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<KnowledgeClass> findByExample(KnowledgeClass instance) {
        LOG.debug("finding Knowledgeclass instance by example");
        try {
            List<KnowledgeClass> results = getSession().createCriteria("Knowledgeclass").add(Example.create(instance))
                    .list();
            LOG.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            LOG.error("find by example failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<KnowledgeClass> findByProperty(String propertyName, Object value) {
        LOG.debug("finding Knowledgeclass instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from KnowledgeClass as knowledgeclass where knowledgeclass." + propertyName + "= ?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            LOG.error("find by property name failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<KnowledgeClass> findAll() {
        LOG.debug("finding all Knowledgeclass instances");
        try {
            String queryString = "from Knowledgeclass";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            LOG.error("find all failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<KnowledgeClass> findAssignClass(String userAccountId, List<String> groupNames, List<String> postNames,
            int flag) {
        if (userAccountId == null) {
            throw null;
        }

        try {
            Map<String, Object> params = new HashMap<String, Object>();
            StringBuffer queryString = new StringBuffer(
                    "select distinct(kca.knowledgeClass) from KnowledgeClassAssign as kca");
            queryString.append(" where kca.flag=:flag and (");
            params.put("flag", flag);
            queryString.append("(kca.type=:utype and kca.assignKey in (:userAccountId))");
            params.put("utype", InfoLayoutAssign.TYPE_USER);
            params.put("userAccountId", new String[] { userAccountId, KnowledgeClassAssign.ASSIGNKEY_ALL_USER });
            if (!groupNames.isEmpty()) {
                queryString.append(" or (kca.type=:gtype and kca.assignKey in (:groupNames))");
                params.put("gtype", InfoLayoutAssign.TYPE_GROUP);
                params.put("groupNames", groupNames);
            }
            if (!postNames.isEmpty()) {
                queryString.append(" or (kca.type=:ptype and kca.assignKey in (:postNames))");
                params.put("ptype", InfoLayoutAssign.TYPE_POST);
                params.put("postNames", postNames);
            }
            queryString.append(")");
            Query query = getSession().createQuery(queryString.toString());
            query.setProperties(params);
//            CommQuery.launchParamValues(query, params);
            return query.list();
        } catch (RuntimeException re) {
            LOG.error("query error", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<KnowledgeClass> findAllClass() {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            StringBuffer queryString = new StringBuffer(" from KnowledgeClass as kc ");
            Query query = getSession().createQuery(queryString.toString());
            query.setProperties(params);
//            CommQuery.launchParamValues(query, params);
            return query.list();
        } catch (RuntimeException re) {
            LOG.error("query error", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> findSearchableClassId(String userAccountId, List<String> groupNames, List<String> postNames) {
        if (userAccountId == null) {
            throw null;
        }

        try {
            Map<String, Object> params = new HashMap<String, Object>();
            StringBuffer queryString = new StringBuffer(
                    "select distinct(kca.knowledgeClass.id) from KnowledgeClassAssign as kca");
            queryString.append(" where kca.flag=:flag and (");
            params.put("flag", KnowledgeClassAssign.FLAG_KB_QUERY);
            queryString.append("(kca.type=:utype and kca.assignKey in (:userAccountId))");
            params.put("utype", InfoLayoutAssign.TYPE_USER);
            params.put("userAccountId", new String[] { userAccountId, KnowledgeClassAssign.ASSIGNKEY_ALL_USER });
            if (!groupNames.isEmpty()) {
                queryString.append(" or (kca.type=:gtype and kca.assignKey in (:groupNames))");
                params.put("gtype", InfoLayoutAssign.TYPE_GROUP);
                params.put("groupNames", groupNames);
            }
            if (!postNames.isEmpty()) {
                queryString.append(" or (kca.type=:ptype and kca.assignKey in (:postNames))");
                params.put("ptype", InfoLayoutAssign.TYPE_POST);
                params.put("postNames", postNames);
            }
            queryString.append(")");
            Query query = getSession().createQuery(queryString.toString());
            query.setProperties(params);
//            CommQuery.launchParamValues(query, params);
            return query.list();
        } catch (RuntimeException re) {
            LOG.error("query error", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Integer> findKnowledgeClassRight(String userAccountId, List<String> groupNames, List<String> postNames,
            String classId) {
        if (userAccountId == null) {
            throw null;
        }

        try {
            Map<String, Object> params = new HashMap<String, Object>();
            StringBuffer queryString = new StringBuffer("select distinct(kca.flag) from KnowledgeClassAssign as kca");
            queryString.append(" where kca.knowledgeClass.id=:classId and (");
            params.put("classId", classId);
            queryString.append("(kca.type=:utype and kca.assignKey in (:userAccountId))");
            params.put("utype", InfoLayoutAssign.TYPE_USER);
            params.put("userAccountId", new String[] { userAccountId, KnowledgeClassAssign.ASSIGNKEY_ALL_USER });
            if (!groupNames.isEmpty()) {
                queryString.append(" or (kca.type=:gtype and kca.assignKey in (:groupNames))");
                params.put("gtype", InfoLayoutAssign.TYPE_GROUP);
                params.put("groupNames", groupNames);
            }
            if (!postNames.isEmpty()) {
                queryString.append(" or (kca.type=:ptype and kca.assignKey in (:postNames))");
                params.put("ptype", InfoLayoutAssign.TYPE_POST);
                params.put("postNames", postNames);
            }
            queryString.append(")");
            Query query = getSession().createQuery(queryString.toString());
            query.setProperties(params);
//            CommQuery.launchParamValues(query, params);
            return query.list();
        } catch (RuntimeException re) {
            LOG.error("query error", re);
            throw re;
        }
    }

    public void updateOrder(KnowledgeClass parentKnowClass, int moveStatus, int oldOrder) {
        LOG.debug("update Knowledgeclass order ");
        try {
            StringBuffer hql = new StringBuffer(" update KnowledgeClass as kc set kc.displayOrder=kc.displayOrder");
            if (moveStatus == KnowledgeClass.MOVESTATUS_FIST) {
                hql.append(" +1 where kc.parentClass =? and kc.displayOrder < ?");
            } else if (moveStatus == KnowledgeClass.MOVESTATUS_LAST) {
                hql.append(" -1 where kc.parentClass =? and kc.displayOrder > ?");
            }
            Query query = getSession().createQuery(hql.toString());
            query.setParameter(0, parentKnowClass);
            query.setParameter(1, oldOrder);
            query.executeUpdate();
        } catch (RuntimeException re) {
            LOG.error("update failed", re);
            throw re;
        }
    }

    public int getMaxOrder(String parentKnowClassId) {
        LOG.debug("get Knowledgeclass max order ");
        try {
            StringBuffer hql = new StringBuffer("select max(kc.displayOrder) from KnowledgeClass as kc  where");
            if (parentKnowClassId == null) {
                hql.append(" kc.parentClass is null");
            } else {
                hql.append(" kc.parentClass.id=:pId");
            }
            Query query = getSession().createQuery(hql.toString());
            if (parentKnowClassId != null) {
                query.setString("pId", parentKnowClassId);
            }
            Integer value = (Integer) query.uniqueResult();
            if (value == null) {
                return 0;
            }
            return value.intValue();
        } catch (RuntimeException re) {
            LOG.error("get failed", re);
            throw re;
        }
    }

    public int getNodeNumByName(String parentId, String text) {
        LOG.debug("count Knowledgeclass by name");
        try {
            StringBuffer queryString = new StringBuffer(
                    "select count(*) from KnowledgeClass as kc where kc.name =:name");
            if (parentId == null) {
                queryString.append(" and kc.parentClass is null");
            } else {
                queryString.append(" and kc.parentClass.id= :pid");
            }
            Query query = getSession().createQuery(queryString.toString());
            query.setString("name", text);
            if (parentId != null) {
                query.setString("pid", parentId);
            }
            Long value = (Long) query.uniqueResult();
            if (value == null) {
                return 0;
            }
            return value.intValue();
        } catch (RuntimeException re) {
            LOG.error("count Knowledgeclass by name failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<KnowledgeClass> getChildList(String id) {
        try {
            StringBuffer queryString = new StringBuffer(" from KnowledgeClass as kca");
            queryString.append(" where kca.parentClass.id=:id");
            Query query = getSession().createQuery(queryString.toString());
            query.setString("id", id);
            return query.list();
        } catch (RuntimeException re) {
            LOG.error("query error", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<KnowledgeClass> getAllChildList(String id) {
        try {
            String sql = " select t.* from OA_KnowledgeClass t start with t.id_ = :id connect by prior t.id_= t.parentid_ ";
            Query query = getSession().createSQLQuery(sql).addEntity("t", KnowledgeClass.class).setString("id", id);
            return query.list();
        } catch (RuntimeException re) {
            LOG.error("query error", re);
            throw re;
        }
    }

    @Override
    public KnowledgeClass getByParentPropertyAndThisProperty(String parentPropertyName, Object parentValue,
            boolean pLikeFlag, String propertyName, Object value, boolean likeFlag) {
        try {
            StringBuffer hql = new StringBuffer("select kc from KnowledgeClass as kc where 1=1");
            if (StringUtils.isNotEmpty(parentPropertyName) || parentValue != null) {
                hql.append(" and kc.parentClass.").append(parentPropertyName);
                if (pLikeFlag) {
                    hql.append(" like :parentValue");
                } else {
                    hql.append(" = :parentValue");
                }
            }
            hql.append(" and kc.").append(propertyName);
            if (likeFlag) {
                hql.append(" like :value");
            } else {
                hql.append(" = :value");
            }
            Query queryObject = getSession().createQuery(hql.toString());
            // 父类型字段不为空
            if (StringUtils.isNotEmpty(parentPropertyName) || parentValue != null) {
                if (pLikeFlag) {
                    queryObject.setParameter("parentValue", "%" + parentValue + "%");
                } else {
                    queryObject.setParameter("parentValue", parentValue);
                }
            }
            if (likeFlag) {
                queryObject.setParameter("value", "%" + value + "%");
            } else {
                queryObject.setParameter("value", value);
            }
            // 设置最大结果集
            queryObject.setMaxResults(1);
            return (KnowledgeClass) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            LOG.error("getByKnowledgeName error ", re);
            throw re;
        }
    }
}
