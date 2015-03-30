package org.eapp.oa.device.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.device.dao.IDevicePropertyDAO;
import org.eapp.oa.device.dto.DeviceOptionQueryParameters;
import org.eapp.oa.device.hbean.DeviceProperty;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

public class DevicePropertyDAO extends BaseHibernateDAO implements
		IDevicePropertyDAO {
	private static final Log log = LogFactory.getLog(DevicePropertyDAO.class);
	@Override
	public DeviceProperty findDeviceProperty(String id) {
		try {
			DeviceProperty instance = (DeviceProperty) getSession().get(
					DeviceProperty.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@Override
	public ListPage<DeviceProperty> findDevicePropertyPage(
			DeviceOptionQueryParameters qp) {
		if (qp == null) {
			throw new IllegalArgumentException("非法参数:QP为空");
		}
		try {
			StringBuffer hql = new StringBuffer(
					"  from DeviceProperty as d  where 1=1 ");
			// 拼接查询条件
			
//			if (qp.getRemarks() != null) {
//				hql.append(" and m.remarks like:remarks");
//			}
			return new CommQuery<DeviceProperty>().queryListPage(qp,
					"select distinct d " + qp.appendOrders(hql, "d"),
					"select count(distinct d) "  + hql.toString(), getSession());
		} catch (RuntimeException re) {
			re.printStackTrace();
			return new ListPage<DeviceProperty>();
		}
	}

	@Override
	public int checkOptionExist(String optionName) {
		StringBuffer hql = new StringBuffer("select count(*) as num from DeviceProperty as d  where d.propertyName=:propertyName");
		Query queryObject = getSession().createQuery(hql.toString());
		queryObject.setParameter("propertyName", optionName);
		Long obj = (Long) queryObject.uniqueResult();
		int num = obj.intValue();
		return num;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceProperty> findDevicePropertyList() {
		StringBuffer hql = new StringBuffer("from DeviceProperty as d  order by propertyName");
		Query queryObject = getSession().createQuery(hql.toString());
		return queryObject.list();
	}

}
