package org.eapp.oa.device.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.device.dao.IDevAllocateFormDAO;
import org.eapp.oa.device.dto.DevAllocateQueryParameters;
import org.eapp.oa.device.hbean.DevAllocateForm;
import org.eapp.oa.device.hbean.DevAllocateList;
import org.eapp.oa.device.hbean.DevFlowApplyProcess;
import org.eapp.oa.device.hbean.DevPurchaseForm;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * Description: 设备调拨单数据访问层
 * 
 * @author sds
 * @version 2009-08-05
 */
@SuppressWarnings("unchecked")
public class DevAllocateFormDAO extends BaseHibernateDAO implements IDevAllocateFormDAO {
    
    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(DevAllocateFormDAO.class);
    // property constants
    
    /**
     * moveType
     */
    public static final String MOVE_TYPE = "moveType";
    
    /**
     * outGroupName
     */
    public static final String OUT_GROUP_NAME = "outGroupName";
    
    /**
     * outAccountId
     */
    public static final String OUT_ACCOUNT_ID = "outAccountId";
    
    /**
     * inGroupName
     */
    public static final String IN_GROUP_NAME = "inGroupName";
    
    /**
     * inAccountId
     */
    public static final String IN_ACCOUNT_ID = "inAccountId";
    
    /**
     * moveDate
     */
    public static final String MOVE_DATE = "moveDate";
    
    /**
     * 流程实例id
     */
    public static final String FLOW_INSTANCE_ID = "flowInstanceId";
    
    /**
     * 通过的
     */
    public static final String PASSED = "passed";
    
    /**
     * 归档时间
     */
    public static final String ARCHIVE_DATE = "archiveDate";
    
    /**
     * 理由
     */
    public static final String REASON = "reason";

    public void save(DevAllocateForm transientInstance) {
        log.debug("saving DevAllocateForm instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(DevAllocateForm persistentInstance) {
        log.debug("deleting DevAllocateForm instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public DevAllocateForm findById(java.lang.String id) {
        log.debug("getting DevAllocateForm instance with id: " + id);
        try {
            DevAllocateForm instance = (DevAllocateForm) getSession().get(DevAllocateForm.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<DevAllocateForm> findByExample(DevAllocateForm instance) {
        log.debug("finding DevAllocateForm instance by example");
        try {
            List<DevAllocateForm> results = getSession().createCriteria("DevAllocateForm")
                    .add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List<DevAllocateForm> findByProperty(String propertyName, Object value) {
        log.debug("finding DevAllocateForm instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from DevAllocateForm as model where model." + propertyName + "= ?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List<DevAllocateForm> findByMoveType(Object moveType) {
        return findByProperty(MOVE_TYPE, moveType);
    }

    public List<DevAllocateForm> findByOutGroupName(Object outGroupName) {
        return findByProperty(OUT_GROUP_NAME, outGroupName);
    }

    public List<DevAllocateForm> findByOutAccountId(Object outAccountId) {
        return findByProperty(OUT_ACCOUNT_ID, outAccountId);
    }

    public List<DevAllocateForm> findByInGroupName(Object inGroupName) {
        return findByProperty(IN_GROUP_NAME, inGroupName);
    }

    public List<DevAllocateForm> findByInAccountId(Object inAccountId) {
        return findByProperty(IN_ACCOUNT_ID, inAccountId);
    }

    public List<DevAllocateForm> findByMoveDate(Object moveDate) {
        return findByProperty(MOVE_DATE, moveDate);
    }

    public List<DevAllocateForm> findByFlowInstanceId(Object flowInstanceId) {
        return findByProperty(FLOW_INSTANCE_ID, flowInstanceId);
    }

    public List<DevAllocateForm> findByPassed(Object passed) {
        return findByProperty(PASSED, passed);
    }

    public List<DevAllocateForm> findByArchiveDate(Object archiveDate) {
        return findByProperty(ARCHIVE_DATE, archiveDate);
    }

    public List<DevAllocateForm> findByMoveReason(Object reason) {
        return findByProperty(REASON, reason);
    }

    public List<DevAllocateForm> findAll() {
        log.debug("finding all DevAllocateForm instances");
        try {
            String queryString = "from DevAllocateForm";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public DevAllocateForm merge(DevAllocateForm detachedInstance) {
        log.debug("merging DevAllocateForm instance");
        try {
            DevAllocateForm result = (DevAllocateForm) getSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(DevAllocateForm instance) {
        log.debug("attaching dirty DevAllocateForm instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(DevAllocateForm instance) {
        log.debug("attaching clean DevAllocateForm instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public List<DevAllocateForm> findAllocateForm(String userAccountId, int formStatus) {
        log.debug("findAllocateForm with userAccountId: " + userAccountId + " and formStatus: " + formStatus);
        if (userAccountId == null) {
            throw new IllegalArgumentException("非法参数");
        }
        try {
            Query query = getSession().createQuery(
					"from DevAllocateForm as df where df.draftsman=:userAccountId " +
					"and df.formStatus=:formStatus order by df.draftDate desc");
            query.setString("userAccountId", userAccountId);
            query.setInteger("formStatus", formStatus);
            List<DevAllocateForm> list = query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("findAllocateForm faild", re);
            throw re;
        }
    }

    public List<DevAllocateForm> queryDealingAllocateForm(String userId) {
        log.debug("queryDealingAllocateForm with userId: " + userId);
        StringBuffer hql = new StringBuffer("from DevAllocateForm as f where f.formStatus="
                + DevPurchaseForm.FORMSTATUS_APPROVAL);
        if (userId != null) {
            hql.append(" and f.inAccountID='" + userId + "'");
        }
        try {
            Query query = getSession().createQuery(hql.toString());
            return (List<DevAllocateForm>) query.list();
        } catch (RuntimeException re) {
            log.error("queryDealingAllocateForm", re);
            throw re;
        }
    }

    public ListPage<DevAllocateForm> queryTrackAllocateForm(DevAllocateQueryParameters rqp, String userAccountId) {
        log.debug("queryTrackAllocateForm with DevAllocateQueryParameters and userAccountId: " + userAccountId);
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }
        try {
            StringBuffer hql = new StringBuffer(
					"from DevAllocateForm as re, Task as t where  t.formID=re.id and (t.taskState=:endState " +
					"and t.transactor=:userId  or re.draftsman=:userId and re.formStatus=:status)  and re.archiveDate is null ");

            rqp.addParameter("endState", TaskInstance.PROCESS_STATE_END);
            rqp.addParameter("userId", userAccountId);
            rqp.addParameter("status", DevAllocateForm.STATUS_APPROVAL);
            if (rqp.getDeviceName() != null) {
                hql.append(" and re.device.deviceName like :deviceName ");
                rqp.toArountParameter("deviceName");
            }
            if (rqp.getBeginDraftDate() != null) {
                hql.append(" and re.draftDate >= :beginDraftDate ");
            }
            if (rqp.getEndDraftDate() != null) {
                hql.append(" and re.draftDate <= :endDraftDate ");
            }
            if (rqp.getPassed() != null) {
                hql.append(" and re.passed = :passed ");
            }

            return new CommQuery<DevAllocateForm>().queryListPage(rqp,
                    "select distinct re " + rqp.appendOrders(hql, "re"), "select count(distinct re) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            log.error("queryTrackAllocateForm faild", re);
            return new ListPage<DevAllocateForm>();
        }

    }

    public ListPage<DevAllocateForm> queryArchAllocateForm(DevAllocateQueryParameters rqp, String userAccountId) {
        log.debug("queryArchAllocateForm with DevAllocateQueryParameters and userAccountId: " + userAccountId);
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }
        try {
            StringBuffer hql = new StringBuffer(
					"from DevAllocateForm as re, Task as t where (t.formID=re.id and t.taskState=:endState " +
					"and t.transactor=:userId or re.draftsman=:userId) and re.archiveDate is not null");
            rqp.addParameter("endState", TaskInstance.PROCESS_STATE_END);
            rqp.addParameter("userId", userAccountId);
            if (rqp.getDeviceName() != null) {
                hql.append(" and re.device.deviceName like :deviceName ");
                rqp.toArountParameter("deviceName");
            }
            if (rqp.getBeginArchDate() != null) {
                hql.append(" and re.archiveDate >= :beginArchDate ");
            }
            if (rqp.getEndArchDate() != null) {
                hql.append(" and re.archiveDate <= :endArchDate ");
            }
            if (rqp.getPassed() != null) {
                hql.append(" and re.passed = :passed ");
            }

            return new CommQuery<DevAllocateForm>().queryListPage(rqp,
                    "select distinct re " + rqp.appendOrders(hql, "re"), "select count(distinct re) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            log.error("queryArchAllocateForm faild", re);
            return new ListPage<DevAllocateForm>();
        }
    }

    public ListPage<DevAllocateForm> queryAllocateForm(DevAllocateQueryParameters rqp) {
        log.debug("queryAllocateForm with DevAllocateQueryParameters");
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }
        try {
            StringBuffer hql = new StringBuffer(
					"from DevAllocateForm as re, Task as t where (t.formID=re.id and t.taskState=:endState " +
					") and re.archiveDate is not null");
            rqp.addParameter("endState", TaskInstance.PROCESS_STATE_END);
            if (rqp.getDeviceName() != null) {
                hql.append(" and re.device.deviceName like :deviceName ");
                rqp.toArountParameter("deviceName");
            }
            if (rqp.getBeginArchDate() != null) {
                hql.append(" and re.archiveDate >= :beginArchDate ");
            }
            if (rqp.getEndArchDate() != null) {
                hql.append(" and re.archiveDate <= :endArchDate ");
            }
            if (rqp.getPassed() != null) {
                hql.append(" and re.passed = :passed ");
            }

            return new CommQuery<DevAllocateForm>().queryListPage(rqp,
                    "select distinct re " + rqp.appendOrders(hql, "re"), "select count(distinct re) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            log.error("queryAllocateForm faild", re);
            return new ListPage<DevAllocateForm>();
        }
    }

    @Override
    public Integer getMaxSequenceNo(Integer sequenceYear) {
        try {
            String queryString = "select max(d.applyFormNO) from DevAllocateForm d where d.sequenceYear=:sequenceYear";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("sequenceYear", sequenceYear);
            if (queryObject.uniqueResult() == null) {
                return 0;
            }
            return (Integer) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }

    }

    public List<DevAllocateList> queryArchDevAllotListByDeviceID(String deviceID, Boolean archiveDateOrder) {
        log.debug("queryArchDevAllotListByDeviceID");
        StringBuffer hql = new StringBuffer("select list from DevAllocateList as list"
                + " where list.devAllocateForm.formStatus=" + DevAllocateForm.FORMSTATUS_PUBLISH);
        if (deviceID != null && !"".equals(deviceID)) {
            hql.append(" and list.device.id='" + deviceID + "'");
        }
        if (archiveDateOrder != null) {
            hql.append(" order by list.devAllocateForm.archiveDate");
            if (!archiveDateOrder) {
                hql.append(" desc");
            }
        }
        try {
            Query query = getSession().createQuery(hql.toString());
            return (List<DevAllocateList>) query.list();
        } catch (RuntimeException re) {
            log.error("queryArchDevAllotListByDeviceID", re);
            throw re;
        }
    }

    public List<DevAllocateForm> queryDealDevAllocateFormByDeviceID(String deviceID) {
        log.debug("queryDealDevAllocateFormByDeviceID");
	    StringBuffer hql = new StringBuffer("select distinct(dev)  from DevAllocateForm as dev left join dev.devAllocateLists as list " +
				" where 1=1 and dev.formStatus ="+DevFlowApplyProcess.FORMSTATUS_APPROVAL);
        if (deviceID != null && !"".equals(deviceID)) {
            hql.append(" and list.device.id=:deviceID");
        }
        try {
            Query query = getSession().createQuery(hql.toString());
            if (deviceID != null && !"".equals(deviceID)) {
                query.setParameter("deviceID", deviceID);
            }
            return (List<DevAllocateForm>) query.list();
        } catch (RuntimeException re) {
            log.error("queryDealDevAllocateFormByDeviceID", re);
            throw re;
        }
    }

    @Override
    public List<DevAllocateList> getDeviceDealDevAllocateByPurpose(String deviceType, String areaCode,
            String deviceClass, String userId) {
        log.debug("getDeviceDealDevAllocateByPurpose");
        StringBuffer hql = new StringBuffer("from DevAllocateList as list  "
                + " where list.devAllocateForm.formStatus = " + DevFlowApplyProcess.FORMSTATUS_APPROVAL);
        hql.append(" and list.device.deviceType=:deviceType");
        hql.append(" and list.device.areaCode=:areaCode");
        hql.append(" and list.device.deviceClass.id=:deviceClass");
//        hql.append(" and list.purpose=:purpose");
        hql.append(" and list.devAllocateForm.applicant=:userId");
        try {
            Query query = getSession().createQuery(hql.toString());
            query.setParameter("deviceType", deviceType);
            query.setParameter("areaCode", areaCode);
            query.setParameter("deviceClass", deviceClass);
//            query.setParameter("purpose", purpose);
            query.setParameter("userId", userId);
            return (List<DevAllocateList>) query.list();
        } catch (RuntimeException re) {
            log.error("getDeviceDealDevAllocateByPurpose", re);
            throw re;
        }
    }

    @Override
	public List<DevAllocateForm> getDevAllocateFormDealDevAllocateByPurpose(
			String deviceType, String areaCode, String deviceClass,
			String userId) {
		log.debug("getDevAllocateFormDealDevAllocateByPurpose");
	    StringBuffer hql = new StringBuffer(" select distinct(list) from DevAllocateForm as list  left join list.devAllocateLists allList " +
				" where list.formStatus = "+DevFlowApplyProcess.FORMSTATUS_APPROVAL);
        hql.append(" and allList.device.deviceType=:deviceType");
        hql.append(" and allList.device.areaCode=:areaCode");
        hql.append(" and allList.device.deviceClass.id=:deviceClass");
//        hql.append(" and allList.purpose=:purpose");
        hql.append(" and list.applicant=:userId");
        try {
            Query query = getSession().createQuery(hql.toString());
            query.setParameter("deviceType", deviceType);
            query.setParameter("areaCode", areaCode);
            query.setParameter("deviceClass", deviceClass);
//            query.setParameter("purpose", purpose);
            query.setParameter("userId", userId);
            return (List<DevAllocateForm>) query.list();
        } catch (RuntimeException re) {
            log.error("getDevAllocateFormDealDevAllocateByPurpose", re);
            throw re;
        }
    }
}