package org.eapp.oa.device.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.device.dao.IDevDiscardDisposeDAO;
import org.eapp.oa.device.hbean.DevDiscardDealForm;
import org.eapp.oa.device.hbean.DiscardDealDevList;
import org.hibernate.Query;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * Description: 设备借用 数据访问对象层
 * 
 * @author 郑超
 */

public class DevDiscardDisposeDAO extends BaseHibernateDAO implements
		IDevDiscardDisposeDAO {

    /**
     * 日志
     */
	private static final Log log = LogFactory.getLog(DevDiscardDisposeDAO.class);

    public DevDiscardDealForm findById(java.lang.String id) {
        log.debug("getting DevDiscardDealForm instance with id: " + id);
        try {
            DevDiscardDealForm instance = (DevDiscardDealForm) getSession().get(DevDiscardDealForm.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    @Override
    public Integer getMaxSequenceNo(Integer sequenceYear) {
        try {
			String queryString = "select max(f.formNO) from DevDiscardDealForm f where f.sequenceYear=:sequenceYear";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("sequenceYear", sequenceYear);
            if (queryObject.uniqueResult() == null) {
                return 0;
            }
            return (Integer) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find max sequence failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DiscardDealDevList> queryDevScrapDisposeListByDeviceID(String deviceID, Boolean regTimeOrder) {
        log.debug("queryDevScrapDisposeListByDeviceID with deviceID: " + deviceID + " and regTimeOrder: "
                + regTimeOrder);
        StringBuffer hql = new StringBuffer("select list from DiscardDealDevList as list where 1=1");
        if (deviceID != null && !"".equals(deviceID)) {
            hql.append(" and list.device.id='" + deviceID + "'");
        }
        if (regTimeOrder != null) {
            hql.append(" order by list.devDiscardDealForm.regTime");
            if (!regTimeOrder) {
                hql.append(" desc");
            }
        }
        try {
            Query query = getSession().createQuery(hql.toString());
            return (List<DiscardDealDevList>) query.list();
        } catch (RuntimeException re) {
            log.error("queryDevScrapDisposeListByDeviceID", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DevDiscardDealForm> queryDevDiscardDealFormByDeviceID(String deviceID) {
        log.debug("queryDevDiscardDealFormByDeviceID with deviceID:" + deviceID);
		StringBuffer hql = new StringBuffer("select distinct(df) from DevDiscardDealForm as df left join df.discardDealDevLists as dfl" +
				" where dfl.device.id='" + deviceID + "' order by df.regTime desc");
        try {
            Query query = getSession().createQuery(hql.toString());
            return (List<DevDiscardDealForm>) query.list();
        } catch (RuntimeException re) {
            log.error("queryDevDiscardDealFormByDeviceID faild", re);
            throw re;
        }
    }
}
