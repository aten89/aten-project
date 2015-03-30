package org.eapp.oa.reimburse.dao.impl;

// default package

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;

import org.eapp.oa.reimburse.dao.IReimFlowConfDAO;
import org.eapp.oa.reimburse.hbean.ReimFlowConf;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 */

public class ReimFlowConfDAO extends BaseHibernateDAO implements IReimFlowConfDAO {
    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(ReimFlowConfDAO.class);
    /*
     * (non-Javadoc)
     * 
     */
    public ReimFlowConf findById(java.lang.String id) {
        log.debug("getting OutlayList instance with id: " + id);
        try {
        	ReimFlowConf instance = (ReimFlowConf) getSession().get(ReimFlowConf.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    public ReimFlowConf findByGroupName(String groupName) {
    	if (groupName == null) {
    		return null;
    	}
    	String queryString = "from ReimFlowConf as rfc where rfc.groupName = :groupName";
    	Query queryObject = getSession().createQuery(queryString);
    	queryObject.setString("groupName", groupName);
    	queryObject.setMaxResults(1);
    	return (ReimFlowConf)queryObject.uniqueResult();
         
    }
    /*
     * (non-Javadoc)
     * 
     */
    @SuppressWarnings("unchecked")
    public List<ReimFlowConf> findAll() {
        log.debug("finding all OutlayList instances");
        try {
            String queryString = "from ReimFlowConf";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
    
    public boolean checkGroupName(String groupName) {
    	String queryString = "select count(rfc) from ReimFlowConf as rfc where rfc.groupName=:groupName";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setString("groupName", groupName);
        Long count = (Long)queryObject.uniqueResult();
        if (count == null || count.longValue() ==0) {
        	return true;
        }
        return false;
    }
}