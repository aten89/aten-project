package org.eapp.oa.travel.dao.impl;

// default package

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;
import org.eapp.oa.travel.dao.IBusTripFlowConfDAO;
import org.eapp.oa.travel.hbean.BusTripFlowConf;

/**
 */

public class BusTripFlowConfDAO extends BaseHibernateDAO implements IBusTripFlowConfDAO {
    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(BusTripFlowConfDAO.class);
    /*
     * (non-Javadoc)
     * 
     */
    public BusTripFlowConf findById(java.lang.String id) {
        log.debug("getting BusTripFlowConf instance with id: " + id);
        try {
        	BusTripFlowConf instance = (BusTripFlowConf) getSession().get(BusTripFlowConf.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    public BusTripFlowConf findByGroupName(String groupName) {
    	if (groupName == null) {
    		return null;
    	}
    	String queryString = "from BusTripFlowConf as rfc where rfc.groupName = :groupName";
    	Query queryObject = getSession().createQuery(queryString);
    	queryObject.setString("groupName", groupName);
    	queryObject.setMaxResults(1);
    	return (BusTripFlowConf)queryObject.uniqueResult();
         
    }
    /*
     * (non-Javadoc)
     * 
     */
    @SuppressWarnings("unchecked")
    public List<BusTripFlowConf> findAll() {
        log.debug("finding all BusTripFlowConf instances");
        try {
            String queryString = "from BusTripFlowConf";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
    
    public boolean checkGroupName(String groupName) {
    	String queryString = "select count(rfc) from BusTripFlowConf as rfc where rfc.groupName=:groupName";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setString("groupName", groupName);
        Long count = (Long)queryObject.uniqueResult();
        if (count == null || count.longValue() ==0) {
        	return true;
        }
        return false;
    }
}