package org.eapp.oa.device.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.device.dao.IDeviceCheckItemDAO;
import org.eapp.oa.device.hbean.DeviceCheckItem;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;
import org.hibernate.Query;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

public class DeviceCheckItemDAO extends BaseHibernateDAO implements
		IDeviceCheckItemDAO {
	
	private static final Log log = LogFactory.getLog(DeviceCheckItem.class);
	
	@Override
	public DeviceCheckItem findById(String id) {
		log.debug("getting DeviceCheckItem instance with id: " + id);
		try {
			DeviceCheckItem instance = (DeviceCheckItem) getSession().get(DeviceCheckItem.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@Override
	public ListPage<DeviceCheckItem> queryDeviceCheckItemPage(QueryParameters qp) {
		if (qp == null) {
			throw new IllegalArgumentException("非法参数:QP为空");
		}
		try {
			StringBuffer hql = new StringBuffer("select dci from DeviceCheckItem as dci where 1=1 ");
			ListPage<DeviceCheckItem> deviceCheckItemPage = new CommQuery<DeviceCheckItem>().queryListPage(qp, qp.appendOrders(hql, "dci"), getSession());
			return deviceCheckItemPage;
		} catch (RuntimeException re) {
			re.printStackTrace();
			return new ListPage<DeviceCheckItem>();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceCheckItem> findByDeviceClassID(String deviceClassID) {
		if (deviceClassID == null || "".equals(deviceClassID)) {
			throw new IllegalArgumentException("参数错误");
		}
		log.debug("getting DeviceCheckItem instances with deviceClassID: " + deviceClassID);
		try {
			StringBuffer hql = new StringBuffer("select dci from DeviceCheckItem as dci where dci.deviceClass.id=:deviceClassID");
			Query queryObject = getSession().createQuery(hql.toString());
			queryObject.setParameter("deviceClassID", deviceClassID);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
