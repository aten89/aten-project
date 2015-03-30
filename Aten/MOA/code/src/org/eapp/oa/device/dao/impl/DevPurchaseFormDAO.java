package org.eapp.oa.device.dao.impl;


import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.device.dao.IDevPurchaseFormDAO;
import org.eapp.oa.device.hbean.DevPurchaseForm;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * Description: 设备申购单数据访问层
 * @author sds
 * @version Aug 5, 2009
 */
@SuppressWarnings("unchecked")
public class DevPurchaseFormDAO extends BaseHibernateDAO implements IDevPurchaseFormDAO {
	private static final Log log = LogFactory.getLog(DevPurchaseFormDAO.class);
	// property constants
	public static final String BARGAIN_NO = "bargainNo";
	public static final String ACCOUNT_ID = "accountId";
	public static final String GROUP_NAME = "groupName";
	public static final String BUY_DATE = "buyDate";
	public static final String PROJECT_NAME = "projectName";
	public static final String BUY_MODE = "buyMode";
	public static final String BUY_PURPOSE = "buyPurpose";
	public static final String BUDGET_MONEY = "budgetMoney";
	public static final String DELIVERY_DATE = "deliveryDate";
	public static final String FLOW_INSTANCE_ID = "flowInstanceId";
	public static final String PASSED = "passed";
	public static final String ARCHIVE_DATE = "archiveDate";
	public static final String REMARK = "remark";

	public void save(DevPurchaseForm transientInstance) {
		log.debug("saving DevPurchaseForm instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(DevPurchaseForm persistentInstance) {
		log.debug("deleting DevPurchaseForm instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DevPurchaseForm findById(java.lang.String id) {
		log.debug("getting DevPurchaseForm instance with id: " + id);
		try {
			DevPurchaseForm instance = (DevPurchaseForm) getSession().get(
					DevPurchaseForm.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<DevPurchaseForm> findByExample(DevPurchaseForm instance) {
		log.debug("finding DevPurchaseForm instance by example");
		try {
			List<DevPurchaseForm> results = getSession().createCriteria(
					"DevPurchaseForm").add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<DevPurchaseForm> findByProperty(String propertyName,
			Object value) {
		log.debug("finding DevPurchaseForm instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from DevPurchaseForm as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<DevPurchaseForm> findByBargainNo(Object bargainNo) {
		return findByProperty(BARGAIN_NO, bargainNo);
	}

	public List<DevPurchaseForm> findByAccountId(Object accountId) {
		return findByProperty(ACCOUNT_ID, accountId);
	}

	public List<DevPurchaseForm> findByGroupName(Object groupName) {
		return findByProperty(GROUP_NAME, groupName);
	}

	public List<DevPurchaseForm> findByBuyDate(Object buyDate) {
		return findByProperty(BUY_DATE, buyDate);
	}

	public List<DevPurchaseForm> findByProjectName(Object projectName) {
		return findByProperty(PROJECT_NAME, projectName);
	}

	public List<DevPurchaseForm> findByBuyMode(Object buyMode) {
		return findByProperty(BUY_MODE, buyMode);
	}

	public List<DevPurchaseForm> findByBuyPurpose(Object buyPurpose) {
		return findByProperty(BUY_PURPOSE, buyPurpose);
	}

	public List<DevPurchaseForm> findByBugetMoney(Object budgetMoney) {
		return findByProperty(BUDGET_MONEY, budgetMoney);
	}

	public List<DevPurchaseForm> findByDeliveryDate(Object deliveryDate) {
		return findByProperty(DELIVERY_DATE, deliveryDate);
	}

	public List<DevPurchaseForm> findByFlowInstanceId(Object flowInstanceId) {
		return findByProperty(FLOW_INSTANCE_ID, flowInstanceId);
	}

	public List<DevPurchaseForm> findByPassed(Object passed) {
		return findByProperty(PASSED, passed);
	}

	public List<DevPurchaseForm> findByArchiveDate(Object archiveDate) {
		return findByProperty(ARCHIVE_DATE, archiveDate);
	}

	public List<DevPurchaseForm> findByBuyExplain(Object buyExplain) {
		return findByProperty(REMARK, buyExplain);
	}

	public List<DevPurchaseForm> findAll() {
		log.debug("finding all DevPurchaseForm instances");
		try {
			String queryString = "from DevPurchaseForm";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public DevPurchaseForm merge(DevPurchaseForm detachedInstance) {
		log.debug("merging DevPurchaseForm instance");
		try {
			DevPurchaseForm result = (DevPurchaseForm) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(DevPurchaseForm instance) {
		log.debug("attaching dirty DevPurchaseForm instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DevPurchaseForm instance) {
		log.debug("attaching clean DevPurchaseForm instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
}