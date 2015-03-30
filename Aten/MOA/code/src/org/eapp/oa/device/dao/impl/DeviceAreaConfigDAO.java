package org.eapp.oa.device.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.device.dao.IDeviceAreaConfigDAO;
import org.eapp.oa.device.hbean.AreaDeviceCfg;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;
import org.hibernate.Query;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public class DeviceAreaConfigDAO extends BaseHibernateDAO implements IDeviceAreaConfigDAO {
	
	private static final Log log = LogFactory.getLog(DeviceAreaConfigDAO.class);
	// property constants
	public static final String DEVICETYPE = "deviceType";
	public static final String YEARPREFIX = "yearPrefix";
	public static final String CURRENTYEAR = "currentYear";
	public static final String YEARPOSTFIX = "yearPostfix";
	public static final String ORDERPREFIX = "orderPrefix";
	public static final String ORDERNUMBER = "orderNumber";
	public static final String ORDERPOSTFIX = "orderPostfix";
	public static final String DESCRIPTION = "description";

	
	/* (non-Javadoc)
	 */
	public AreaDeviceCfg findById(java.lang.String id) {
		log.debug("getting AreaDeviceCfg instance with id: " + id);
		try {
			AreaDeviceCfg instance = (AreaDeviceCfg) getSession().get(
					AreaDeviceCfg.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	public List<AreaDeviceCfg> findByProperty(String propertyName, Object value) {
		log.debug("finding AreaDeviceCfg instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from AreaDeviceCfg as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 */
	public List<AreaDeviceCfg> findByDeviceType(Object deviceType) {
		return findByProperty(DEVICETYPE, deviceType);
	}


	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	public List<AreaDeviceCfg> findAll() {
		log.debug("finding all DeviceNo instances");
		try {
			String queryString = "from AreaDeviceCfg";
			Query queryObject = getSession().createQuery(queryString);
			return (List<AreaDeviceCfg>)queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AreaDeviceCfg> findMainDevAreaDeviceCfgs() {
		log.debug("finding all DeviceNo instances");
		try {
			String queryString = "from AreaDeviceCfg as c where c.mainDevFlag=true";
			Query queryObject = getSession().createQuery(queryString);
			return (List<AreaDeviceCfg>)queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public void updateByDeviceType(String deviceType,String orderPostfix){
		
		try {
			String queryString = "update AreaDeviceCfg  set  orderPostfix=:orderPostfix where deviceType=:deviceType";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter("orderPostfix", orderPostfix);
			queryObject.setParameter("orderPostfix", orderPostfix);
			queryObject.executeUpdate();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AreaDeviceCfg> findByAreaCodeAndClassId(String areaCode,
			String classId) {
		log.debug("finding all AreaDeviceCfg instances");
		try {
			String queryString = "from AreaDeviceCfg as d where d.deviceClass.id=:classId and d.areaCode=:areaCode";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter("areaCode", areaCode);
			queryObject.setParameter("classId", classId);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	@Override
	public ListPage<AreaDeviceCfg> queryAllAreaDeviceCfgPage(
			QueryParameters qp) {
		if (qp == null) {
			throw new IllegalArgumentException("非法参数:QP为空");
		}
		try {
			StringBuffer hql = new StringBuffer(
					"  from AreaDeviceCfg as d  where 1=1 ");
			return new CommQuery<AreaDeviceCfg>().queryListPage(qp,
					"select distinct d " + qp.appendOrders(hql, "d"),
					"select count(distinct d) "  + hql.toString(), getSession());
		} catch (RuntimeException re) {
			re.printStackTrace();
			return new ListPage<AreaDeviceCfg>();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AreaDeviceCfg> findAreaDeviceByAreaCode(String areaCode,String deviceType) {
		log.debug("finding all AreaDeviceCfg instances");
		try {
			String queryString = "from AreaDeviceCfg as d where  d.areaCode=:areaCode and d.deviceClass.deviceType=:deviceType";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter("areaCode", areaCode);
			queryObject.setParameter("deviceType", deviceType);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public List<DeviceAcptCountCfg> getDeviceAcptCountCfgByClassId(
//			String areaCode, String classID, String purpose) {
//		log.debug("finding all DeviceAcptCountCfg instances");
//		try {
//			String queryString = "from DeviceAcptCountCfg as d where  d.areaDeviceCfg.areaCode=:areaCode and d.areaDeviceCfg.deviceClass.id=:classID and d.devPurpose=:purpose";
//			Query queryObject = getSession().createQuery(queryString);
//			queryObject.setParameter("areaCode", areaCode);
//			queryObject.setParameter("classID", classID);
//			queryObject.setParameter("purpose", purpose);
//			return queryObject.list();
//		} catch (RuntimeException re) {
//			log.error("find all failed", re);
//			throw re;
//		}
//	}
}
