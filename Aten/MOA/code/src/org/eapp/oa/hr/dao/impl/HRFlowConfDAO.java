package org.eapp.oa.hr.dao.impl;

// default package

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;

import org.eapp.oa.hr.dao.IHRFlowConfDAO;
import org.eapp.oa.hr.hbean.HRFlowConf;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 */

public class HRFlowConfDAO extends BaseHibernateDAO implements IHRFlowConfDAO {
    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(HRFlowConfDAO.class);
    /*
     * (non-Javadoc)
     * 
     */
    public HRFlowConf findById(java.lang.String id) {
        log.debug("getting HRFlowConf instance with id: " + id);
        try {
        	HRFlowConf instance = (HRFlowConf) getSession().get(HRFlowConf.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    public HRFlowConf findByGroupName(String groupName) {
    	if (groupName == null) {
    		return null;
    	}
    	String queryString = "from HRFlowConf as rfc where rfc.groupName = :groupName";
    	Query queryObject = getSession().createQuery(queryString);
    	queryObject.setString("groupName", groupName);
    	queryObject.setMaxResults(1);
    	return (HRFlowConf)queryObject.uniqueResult();
         
    }
    /*
     * (non-Javadoc)
     * 
     */
    @SuppressWarnings("unchecked")
    public List<HRFlowConf> findAll() {
        log.debug("finding all HRFlowConf instances");
        try {
            String queryString = "from HRFlowConf";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
    
    public boolean checkGroupName(String groupName) {
    	String queryString = "select count(rfc) from HRFlowConf as rfc where rfc.groupName=:groupName";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setString("groupName", groupName);
        Long count = (Long)queryObject.uniqueResult();
        if (count == null || count.longValue() ==0) {
        	return true;
        }
        return false;
    }
}