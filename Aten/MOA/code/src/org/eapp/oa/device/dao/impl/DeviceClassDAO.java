package org.eapp.oa.device.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.device.dao.IDeviceClassDAO;
import org.eapp.oa.device.hbean.DeviceCfgItem;
import org.eapp.oa.device.hbean.DeviceClass;
import org.eapp.oa.device.hbean.DeviceClassAssign;
import org.eapp.oa.device.hbean.DeviceClassAssignDetail;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;
/**
 * Description: 设备分类数据访问层
 * 
 * @author sds
 * @version Sep 1, 2009
 */
@SuppressWarnings("unchecked")
public class DeviceClassDAO extends BaseHibernateDAO implements IDeviceClassDAO {
	private static final Log log = LogFactory.getLog(DeviceClassDAO.class);
	// property constants
	public static final String CLASS_KEY = "name";
	public static final String PURCHASE_FLOW_KEY = "purchaseFlowKey";
	public static final String USE_APPLY_FLOW_KEY = "useApplyFlowKey";
	public static final String ALLOCATE_FLOW_KEY = "allocateFlowKey";
	public static final String DISCARD_FLOW_KEY = "discardFlowKey";
	public static final String DISPLAY_ORDER = "displayOrder";
	public static final String DESCRIPTION = "description";

	public void save(DeviceClass transientInstance) {
		log.debug("saving DeviceClass instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(DeviceClass persistentInstance) {
		log.debug("deleting DeviceClass instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DeviceClass findById(java.lang.String id) {
		log.debug("getting DeviceClass instance with id: " + id);
		try {
			DeviceClass instance = (DeviceClass) getSession().get(
					DeviceClass.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<DeviceClass> findByExample(DeviceClass instance) {
		log.debug("finding DeviceClass instance by example");
		try {
			List<DeviceClass> results = getSession().createCriteria(
					"DeviceClass").add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<DeviceClass> findByProperty(String propertyName, Object value) {
		log.debug("finding DeviceClass instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from DeviceClass as model where model."
					+ propertyName + "= ? and model.status=status";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			queryObject.setParameter("status", DeviceClass.STATUS_NORMAL);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<DeviceClass> findByClassKey(Object classKey) {
		return findByProperty(CLASS_KEY, classKey);
	}

	public List<DeviceClass> findByPurchaseFlowKey(Object purchaseFlowKey) {
		return findByProperty(PURCHASE_FLOW_KEY, purchaseFlowKey);
	}

	public List<DeviceClass> findByUseApplyFlowKey(Object useApplyFlowKey) {
		return findByProperty(USE_APPLY_FLOW_KEY, useApplyFlowKey);
	}

	public List<DeviceClass> findByAllocateFlowKey(Object allocateFlowKey) {
		return findByProperty(ALLOCATE_FLOW_KEY, allocateFlowKey);
	}

	public List<DeviceClass> findByDiscardFlowKey(Object discardFlowKey) {
		return findByProperty(DISCARD_FLOW_KEY, discardFlowKey);
	}

	public List<DeviceClass> findByDisplayOrder(Object displayOrder) {
		return findByProperty(DISPLAY_ORDER, displayOrder);
	}

	public List<DeviceClass> findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	public List<DeviceClass> findAll(String deviceTypeCode) {
		log.debug("finding all DeviceClass instances");
		try {
			String queryString = "from DeviceClass as d where 1=1";
			if (deviceTypeCode != null && !"".equals(deviceTypeCode)) {
				queryString+=" and dc.deviceType=:deviceTypeCode";
			}
			Query queryObject = getSession().createQuery(queryString);
			if (deviceTypeCode != null && !"".equals(deviceTypeCode)) {
				queryObject.setParameter("deviceTypeCode", deviceTypeCode);
			}
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ListPage<DeviceClass> findCheckItemDeviceClass(QueryParameters qp) {
		log.debug("finding all device class instances who has check Item");
		try {
			StringBuffer hql = new StringBuffer("from DeviceClass as dc where dc.id in (select dci.deviceClass.id from DeviceCheckItem as dci where 1=1)");
			qp.addOrder("deviceType", true);
			qp.addOrder("name", true);
			return new CommQuery<DeviceClass>().queryListPage(qp,
					"select distinct dc " + qp.appendOrders(hql, "dc"),
					"select count(distinct dc) "  + hql.toString(), getSession());
		} catch (RuntimeException re) {
			log.error("find failed", re);
			return new ListPage<DeviceClass>();
		}
	}
	
	public DeviceClass merge(DeviceClass detachedInstance) {
		log.debug("merging DeviceClass instance");
		try {
			DeviceClass result = (DeviceClass) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(DeviceClass instance) {
		log.debug("attaching dirty DeviceClass instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DeviceClass instance) {
		log.debug("attaching clean DeviceClass instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	@Override
	public List<DeviceClass> findAssignClass(String userAccountId,
			List<String> groupNames, List<String> postNames, Integer flag,
			String name) {
		if (userAccountId == null) {
			throw null;
		}
		//查找有权限
		boolean isGroup = groupNames != null && groupNames.size() > 0;
		boolean isPost = postNames != null && postNames.size() > 0;
		boolean hasNmae = name != null;
		
		try {
			StringBuffer queryString = new StringBuffer("select distinct(dca.deviceClass) from DeviceClassAssign as dca");
			queryString.append(" where dca.flag=:flag and (");
			queryString.append("(dca.type=:utype and dca.assignKey=:userAccountId)");
			if (isGroup) {
				queryString.append(" or (dca.type=:gtype and dca.assignKey in (:groupNames))");
			}
			if (isPost) {
				queryString.append(" or (dca.type=:ptype and dca.assignKey in (:postNames))");
			}
			queryString.append(")");
			if (hasNmae) {
				queryString.append(" and dca.deviceClass.classKey=:name");
			}
			Query query = getSession().createQuery(queryString.toString());
			query.setInteger("flag", flag);
			query.setInteger("utype", DeviceClassAssignDetail.TYPE_USER);
			query.setString("userAccountId", userAccountId);
			
			if (isGroup) {
				query.setInteger("gtype", DeviceClassAssignDetail.TYPE_GROUP);
				query.setParameterList("groupNames", groupNames);
			}
			if (isPost) {
				query.setInteger("ptype", DeviceClassAssignDetail.TYPE_POST);
				query.setParameterList("postNames", postNames);
			}
			if (hasNmae) {
				query.setString("name", name);
			}
			return query.list();
		} catch (RuntimeException re) {
			log.error("delete binding by flow_key failed", re);
			throw re;
		}
	}


	@Override
	public List<DeviceClass> findByDeviceTypeCode(String deviceTypeCode,String areaCode) {
		log.debug("finding all DeviceClass instances by device type:" + deviceTypeCode);
		try {
			StringBuffer hql = new StringBuffer("select distinct dc from DeviceClass dc left join dc.areaDeviceCfgs as ac where 1=1 and dc.status=:status");
			if (deviceTypeCode != null && !"".equals(deviceTypeCode)) {
				hql.append(" and dc.deviceType=:deviceTypeCode");
			}
			if (areaCode != null && !"".equals(areaCode)) {
				hql.append(" and ac.areaCode=:areaCode");
			}
			hql.append(" order by dc.name");
			Query queryObject = getSession().createQuery(hql.toString());
			if (areaCode != null && !"".equals(areaCode)) {
				queryObject.setParameter("areaCode", areaCode);
			}
			if (deviceTypeCode != null && !"".equals(deviceTypeCode)) {
				queryObject.setParameter("deviceTypeCode", deviceTypeCode);
			}
			queryObject.setParameter("status", DeviceClass.STATUS_NORMAL);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	@Override
	public int getDisplayOrder() {
		try {
			String queryString = "select max(l.displayOrder) from DeviceClass l";
			Query queryObject = getSession().createQuery(queryString);
			Integer value = (Integer)queryObject.uniqueResult();
			if(value == null){
				return 0;
			}
			return value.intValue();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	@Override
	public List<DeviceCfgItem> findDeviceCfgItem(String Id) {
		log.debug("finding all DeviceCfgItem instances");
		try {
			String queryString = "from DeviceCfgItem d where d.deviceClass.id = :id order by d.displayOrder";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter("id", Id);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	@Override
	public ListPage<DeviceClass> findDeviceClassPage(
			QueryParameters qp) {
		if (qp == null) {
			throw new IllegalArgumentException("非法参数:QP为空");
		}
		try {
			StringBuffer hql = new StringBuffer(
					"  from DeviceClass as d  where 1=1 ");
			return new CommQuery<DeviceClass>().queryListPage(qp,
					"select distinct d " + qp.appendOrders(hql, "d"),
					"select count(distinct d) "  + hql.toString(), getSession());
		} catch (RuntimeException re) {
			re.printStackTrace();
			return new ListPage<DeviceClass>();
		}
	}

	@Override
	public List<DeviceClass> findByNameAndType(String deviceType, String name) {
		log.debug("finding all DeviceClass instances");
		try {
			String queryString = "from DeviceClass l where l.deviceType=:deviceType and l.name=:name order by l.name";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter("deviceType", deviceType);
			queryObject.setParameter("name", name);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<DeviceClass> findAssignDeciveClass(String deviceType,String areaCode,Integer assgintype,String userAccountId,
			List<String> groupNames, List<String> postNames) {
		if (userAccountId == null) {
			throw null;
		}
		//查找有权限
		boolean isGroup = groupNames != null && groupNames.size() > 0;
		boolean isPost = postNames != null && postNames.size() > 0;
		boolean hasType = assgintype != null;
		
		try {
			StringBuffer queryString = new StringBuffer("select distinct(dca.deviceClass) from DeviceClassAssignArea as dca right join dca.deviceClassAssign.deviceClassAssignDetails as assign");
			queryString.append(" where 1=1 ");
			if(deviceType!=null){
				queryString.append(" and dca.deviceClass.deviceType=:deviceType ");
			}
			queryString.append(" and(");
			queryString.append("(assign.type=:uassignType and assign.assignKey=:userAccountId)");
			if (isGroup) {
				queryString.append(" or (assign.type=:gassignType and assign.assignKey in (:groupNames))");
			}
			if (isPost) {
				queryString.append(" or (assign.type=:passignType and assign.assignKey in (:postNames))");
			}
			queryString.append(")");
			if (hasType) {
				queryString.append(" and assign.assignClass=:type");
			}
			if(areaCode!=null){
				queryString.append(" and dca.deviceClassAssign.areaCode=:areaCode");
			}else{
				queryString.append(" and dca.deviceClassAssign.areaCode in (0,1,2)");
			}
			queryString.append(" order by dca.deviceClass.name");
			Query query = getSession().createQuery(queryString.toString());
//			query.setString("userName", userName);
			query.setInteger("uassignType", DeviceClassAssignDetail.TYPE_USER);
			query.setString("userAccountId", userAccountId);
			if (isGroup) {
				query.setInteger("gassignType", DeviceClassAssignDetail.TYPE_GROUP);
				query.setParameterList("groupNames", groupNames);
			}
			if (isPost) {
				query.setInteger("passignType", DeviceClassAssignDetail.TYPE_POST);
				query.setParameterList("postNames", postNames);
			}
			if (hasType) {
				query.setInteger("type", assgintype);
			}
			if(areaCode!=null){
				query.setString("areaCode", areaCode);
			}
			if(deviceType!=null){
				query.setString("deviceType", deviceType);
			}
			return query.list();
		} catch (RuntimeException re) {
			log.error("finding deviceClass by plan failed", re);
			throw re;
		}
	}

	@Override
	public List<DeviceClass> findAssignClass(List<String> deviceType,
			List<String> areaCode, Integer assgintype, String userAccountId,
			List<String> groupNames, List<String> postNames) {
		if (userAccountId == null) {
			throw null;
		}
		//查找有权限
		boolean isGroup = groupNames != null && groupNames.size() > 0;
		boolean isPost = postNames != null && postNames.size() > 0;
		boolean hasType = assgintype != null;
		boolean isDeviceType = deviceType != null && deviceType.size() > 0;
		boolean isAreaCode = areaCode != null && areaCode.size() > 0;
		try {
			StringBuffer queryString = new StringBuffer("select distinct(dca.deviceClass) from DeviceClassAssignArea as dca right join dca.deviceClassAssign.deviceClassAssignDetails as assign");
			queryString.append(" where (");
			
			queryString.append("(assign.type=:uassignType and assign.assignKey=:userAccountId)");
			if (isGroup) {
				queryString.append(" or (assign.type=:gassignType and assign.assignKey in (:groupNames))");
			}
			if (isPost) {
				queryString.append(" or (assign.type=:passignType and assign.assignKey in (:postNames))");
			}
			queryString.append(")");
			if (hasType) {
				queryString.append(" and assign.assignClass=:type");
			}
			if (isDeviceType) {
				queryString.append(" and dca.deviceClass.deviceType in (:deviceType)");
			}
			if(isAreaCode){
				queryString.append(" and dca.deviceClassAssign.areaCode in (:areaCode)");
			}
			queryString.append(" order by dca.deviceClass.name");
			Query query = getSession().createQuery(queryString.toString());
//			query.setString("userName", userName);
			query.setInteger("uassignType", DeviceClassAssignDetail.TYPE_USER);
			query.setString("userAccountId", userAccountId);
			if (isGroup) {
				query.setInteger("gassignType", DeviceClassAssignDetail.TYPE_GROUP);
				query.setParameterList("groupNames", groupNames);
			}
			if (isPost) {
				query.setInteger("passignType", DeviceClassAssignDetail.TYPE_POST);
				query.setParameterList("postNames", postNames);
			}
			if (isDeviceType) {
				query.setParameterList("deviceType", deviceType);
			}
			if(isAreaCode){
				query.setParameterList("areaCode", areaCode);
			}
			query.setInteger("type", assgintype);
			return query.list();
		} catch (RuntimeException re) {
			log.error("finding deviceClass by plan failed", re);
			throw re;
		}
	}
	
	@Override
	public List<DeviceClassAssign> findSelectAssignClass(List<String> deviceType,
			List<String> areaCode, Integer assgintype, String userAccountId,
			List<String> groupNames, List<String> postNames) {
		if (userAccountId == null) {
			throw null;
		}
		//查找有权限
		boolean isGroup = groupNames != null && groupNames.size() > 0;
		boolean isPost = postNames != null && postNames.size() > 0;
		boolean hasType = assgintype != null;
		boolean isDeviceType = deviceType != null && deviceType.size() > 0;
		boolean isAreaCode = areaCode != null && areaCode.size() > 0;
		try {
			StringBuffer queryString = new StringBuffer(" select dca.deviceClassAssign from DeviceClassAssignArea as dca right join dca.deviceClassAssign.deviceClassAssignDetails as assign");
			queryString.append(" where (");
			
			queryString.append("(assign.type=:uassignType and assign.assignKey=:userAccountId)");
			if (isGroup) {
				queryString.append(" or (assign.type=:gassignType and assign.assignKey in (:groupNames))");
			}
			if (isPost) {
				queryString.append(" or (assign.type=:passignType and assign.assignKey in (:postNames))");
			}
			queryString.append(")");
			if (hasType) {
				queryString.append(" and assign.assignClass=:type");
			}
			if (isDeviceType) {
				queryString.append(" and dca.deviceClass.deviceType in (:deviceType)");
			}
			if(isAreaCode){
				queryString.append(" and dca.deviceClassAssign.areaCode in (:areaCode)");
			}
			queryString.append(" order by dca.deviceClass.name");
			Query query = getSession().createQuery(queryString.toString());
//			query.setString("userName", userName);
			query.setInteger("uassignType", DeviceClassAssignDetail.TYPE_USER);
			query.setString("userAccountId", userAccountId);
			if (isGroup) {
				query.setInteger("gassignType", DeviceClassAssignDetail.TYPE_GROUP);
				query.setParameterList("groupNames", groupNames);
			}
			if (isPost) {
				query.setInteger("passignType", DeviceClassAssignDetail.TYPE_POST);
				query.setParameterList("postNames", postNames);
			}
			if (isDeviceType) {
				query.setParameterList("deviceType", deviceType);
			}
			if(isAreaCode){
				query.setParameterList("areaCode", areaCode);
			}
			query.setInteger("type", assgintype);
			return query.list();
		} catch (RuntimeException re) {
			log.error("finding deviceClass by plan failed", re);
			throw re;
		}
	}

}