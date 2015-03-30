package org.eapp.oa.device.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.device.dao.IDeviceClassAssignDAO;
import org.eapp.oa.device.hbean.DeviceClassAssign;
import org.eapp.oa.device.hbean.DeviceClassAssignDetail;
import org.hibernate.Query;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

public class DeviceClassAssignDAO extends BaseHibernateDAO implements
		IDeviceClassAssignDAO {
	private static final Log log = LogFactory.getLog(DeviceClassAssignDAO.class);
	
	public DeviceClassAssign findDeviceClassAssignById(java.lang.String id) {
		log.debug("getting Budget DeviceClassAssign with id: " + id);
		try {
			DeviceClassAssign instance = (DeviceClassAssign) getSession().get(DeviceClassAssign.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceClassAssignDetail> findBindById(String id, Integer key,
			Integer flag) {
		log.debug("finding binding by id " + id);
		try {
			String queryString = "from DeviceClassAssignDetail as model where model.deviceClassAssign.id=:id " 
					+ " and model.type=:key and model.assignClass=:flag";
			
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setString("id",id);
			queryObject.setInteger("key", key);
			queryObject.setInteger("flag", flag);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("finding binding by id failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceClassAssign> findDeviceClassAssignList() {
		log.debug("finding DeviceClassAssign instance ");
		try {
			String queryString = "from DeviceClassAssign as d order by d.areaCode";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	public void delBindById(String id, Integer key, Integer flag) {
		log.debug("delete binding by id " + id);
		try {
			String queryString = "delete from DeviceClassAssignDetail as model where model.deviceClassAssign.id=:id " 
					+ " and model.type=:key and model.assignClass=:flag";
			
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setString("id",id);
			queryObject.setInteger("key", key);
			queryObject.setInteger("flag", flag);
			queryObject.executeUpdate();
		} catch (RuntimeException re) {
			log.error("delete binding by id failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceClassAssignDetail> findDeviceClassAssignDetail(String assginId,Integer flag) {
		log.debug("finding binding by id " + assginId);
		try {
			String queryString = "from DeviceClassAssignDetail as model where model.deviceClassAssign.id=:id " 
					+ " and model.type=:flag";
			
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setString("id",assginId);
			queryObject.setInteger("flag", flag);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("finding binding by id failed", re);
			throw re;
		}
	}
	
}
