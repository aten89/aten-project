package org.eapp.oa.device.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.device.dao.IDevRepairFormDAO;
import org.eapp.oa.device.dto.DeviceSimpleQueryParameters;
import org.eapp.oa.device.hbean.DevRepairForm;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * Description: 设备维修单数据访问层
 * 
 * @author sds
 * @version Aug 5, 2009
 */
@SuppressWarnings("unchecked")
public class DevRepairFormDAO extends BaseHibernateDAO implements IDevRepairFormDAO {
	private static final Log log = LogFactory.getLog(DevRepairFormDAO.class);
	// property constants
	public static final String REPAIR_FORM_NO = "repairFormNo";
	public static final String ACCOUNT_ID = "accountId";
	public static final String GROUP_NAME = "groupName";
	public static final String STATUS = "status";
	public static final String CREATE_TIME = "createTime";
	public static final String BUDGET_MONEY = "budgetMoney";
	public static final String REMARK = "remark";

	public void save(DevRepairForm transientInstance) {
		log.debug("saving DevRepairForm instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(DevRepairForm persistentInstance) {
		log.debug("deleting DevRepairForm instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DevRepairForm findById(java.lang.String id) {
		log.debug("getting DevRepairForm instance with id: " + id);
		try {
			DevRepairForm instance = (DevRepairForm) getSession().get(
					DevRepairForm.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<DevRepairForm> findByExample(DevRepairForm instance) {
		log.debug("finding DevRepairForm instance by example");
		try {
			List<DevRepairForm> results = getSession().createCriteria("DevRepairForm").add(
					Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<DevRepairForm> findByProperty(String propertyName, Object value) {
		log.debug("finding DevRepairForm instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from DevRepairForm as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	public List<DevRepairForm> findByDeviceID(String deviceID, Boolean createTimeOrder) {
		try {
			StringBuffer hql = new StringBuffer("from DevRepairForm as form where 1=1");
			if (deviceID != null && !"".equals(deviceID)) {
				hql.append("  and form.device.id ='" + deviceID + "'");
			}
			if (createTimeOrder != null) {
				hql.append(" order by form.createTime");
				if (!createTimeOrder) {
					hql.append(" desc");
				}
			}
			Query queryObject = getSession().createQuery(hql.toString());
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<DevRepairForm> findByRepairFormNo(Object repairFormNo) {
		return findByProperty(REPAIR_FORM_NO, repairFormNo);
	}

	public List<DevRepairForm> findByAccountId(Object accountId) {
		return findByProperty(ACCOUNT_ID, accountId);
	}

	public List<DevRepairForm> findByGroupName(Object groupName) {
		return findByProperty(GROUP_NAME, groupName);
	}

	public List<DevRepairForm> findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List<DevRepairForm> findByCreateTime(Object createTime) {
		return findByProperty(CREATE_TIME, createTime);
	}

	public List<DevRepairForm> findByBudgetMoney(Object budgetMoney) {
		return findByProperty(BUDGET_MONEY, budgetMoney);
	}

	public List<DevRepairForm> findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List<DevRepairForm> findAll() {
		log.debug("finding all DevRepairForm instances");
		try {
			String queryString = "from DevRepairForm";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public DevRepairForm merge(DevRepairForm detachedInstance) {
		log.debug("merging DevRepairForm instance");
		try {
			DevRepairForm result = (DevRepairForm) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(DevRepairForm instance) {
		log.debug("attaching dirty DevRepairForm instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DevRepairForm instance) {
		log.debug("attaching clean DevRepairForm instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	@Override
	public ListPage<DevRepairForm> queryDevRepairForm(
			DeviceSimpleQueryParameters qp) {
		if (qp == null) {
			throw new IllegalArgumentException("非法参数:QP为空");
		}
		
		try {
			StringBuffer hql = new StringBuffer(
					"from DevRepairForm drf where 1=1");
			// 拼接查询条件
			if (qp.getBeginTime() != null) {
				hql.append(" and drf.createTime >:beginTime");
			}
			if (qp.getEndTime() != null) {
				hql.append(" and drf.createTime <:endTime");
			}
			if (qp.getAccountId() != null) {
				hql.append(" and drf.accountId =:accountId");
			}
			if (qp.getDeviceName() != null) {
				hql.append(" and drf.device.deviceName like :deviceName");
				qp.toArountParameter("deviceName");
			}
			
			if (qp.getDeviceClass() != null) {
				hql.append(" and drf.device.deviceClass = :deviceClass");
			}
			if (qp.getStatus() != null) {
				hql.append(" and drf.status = :status");
			}
			return new CommQuery<DevRepairForm>().queryListPage(qp, 
					qp.appendOrders(hql, "drf"), 
					"select count(distinct drf) "  + hql.toString(), getSession());
		} catch (RuntimeException re) {
			re.printStackTrace();
			return new ListPage<DevRepairForm>();
		}
	}

	@Override
	public Integer getMaxSequenceNo(Integer sequenceYear) {
		try {
			String queryString = "select max(d.repairDeviceNO) from DevRepairForm d where d.sequenceYear=:sequenceYear";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter("sequenceYear", sequenceYear);
			if(queryObject.uniqueResult()==null){
				return 0;
			}
			return (Integer)queryObject.uniqueResult();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
			
	}
}