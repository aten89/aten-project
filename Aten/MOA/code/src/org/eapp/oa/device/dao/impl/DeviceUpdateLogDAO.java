package org.eapp.oa.device.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.device.dao.IDeviceUpdateLogDAO;
import org.eapp.oa.device.hbean.DeviceUpdateLog;
import org.hibernate.Query;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

public class DeviceUpdateLogDAO extends BaseHibernateDAO implements
		IDeviceUpdateLogDAO {
	
	private static final Log log = LogFactory.getLog(DeviceUpdateLog.class);
	
	@Override
	public DeviceUpdateLog findById(String id) {
		log.debug("getting DeviceUpdateLog instance with id: " + id);
		try {
			DeviceUpdateLog instance = (DeviceUpdateLog) getSession().get(DeviceUpdateLog.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceUpdateLog> findByDeviceID(String deviceID, Boolean operateDateOrder) {
		if (deviceID == null || "".equals(deviceID)) {
			throw new IllegalArgumentException("参数错误");
		}
		log.debug("getting DeviceUpdateLog instances with deviceID: " + deviceID);
		
		try {
			StringBuffer hql = new StringBuffer("select log from DeviceUpdateLog as log where 1=1");
			if (deviceID != null && !"".equals(deviceID)) {
				hql.append(" and log.device.id ='" + deviceID + "'");
			}
			if (operateDateOrder != null) {
				hql.append(" order by log.operateDate");
				if (!operateDateOrder) {
					hql.append(" desc");
				}
			}
			Query queryObject = getSession().createQuery(hql.toString());
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
