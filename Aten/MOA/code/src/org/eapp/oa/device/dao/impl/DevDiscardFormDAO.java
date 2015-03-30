package org.eapp.oa.device.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.device.dao.IDevDiscardFormDAO;
import org.eapp.oa.device.dto.DevDiscardQueryParameters;
import org.eapp.oa.device.hbean.DevDiscardForm;
import org.eapp.oa.device.hbean.DevFlowApplyProcess;
import org.eapp.oa.device.hbean.DiscardDevList;
import org.eapp.oa.flow.hbean.TaskAssign;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Query;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * Description: 设备报废单数据访问层
 * 
 * @author sds
 * @version 2009-08-05
 */
@SuppressWarnings("unchecked")
public class DevDiscardFormDAO extends BaseHibernateDAO implements IDevDiscardFormDAO {
    
    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(DevDiscardFormDAO.class);
    // property constants
    
    /**
     * 废弃类型
     */
    public static final String DISCARD_TYPE = "discardType";
    
    /**
     * 用户账号
     */
    public static final String ACCOUNT_ID = "accountId";
    
    /**
     * groupName
     */
    public static final String GROUP_NAME = "groupName";
    
    /**
     * 申请日期
     */
    public static final String APPLY_DATE = "applyDate";
    
    /**
     * 原因
     */
    public static final String REASON = "reason";
    
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
     * 起草人
     */
    public static final String DRAFTSMAN = "draftsman";

    public DevDiscardForm findById(java.lang.String id) {
        log.debug("getting DevDiscardForm instance with id: " + id);
        try {
            DevDiscardForm instance = (DevDiscardForm) getSession().get(DevDiscardForm.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<DevDiscardForm> findAll() {
        log.debug("finding all DevDiscardForm instances");
        try {
            String queryString = "from DevDiscardForm";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public List<DevDiscardForm> findDraftDiscardForm(String draftsman, int formStatus) {
        log.debug("findDraftDiscardForm with draftsman: " + draftsman + " and formStatus: " + formStatus);
        try {
            Query query = getSession()
                    .createQuery(
					"from DevDiscardForm as df where df.draftsman=:draftsman and df.formStatus=:formStatus order by df.applyDate desc");
            query.setString("draftsman", draftsman);
            query.setInteger("formStatus", formStatus);
            List<DevDiscardForm> list = query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("findDraftDiscardForm", re);
            throw re;
        }
    }

    @Override
    public List<DevDiscardForm> queryDealDiscardForm(String userId) {
        log.debug("queryDealDiscardForm with userId: " + userId);
        String hql = "select re,t from DevDiscardForm as re, FlowConfig as fc, " +
		"Task as t left join t.taskAssigns as p where fc.flowKey in (select dc.discardFlowKey from DeviceClass as dc where dc.discardFlowKey is not null) and fc.draftFlag='2' and " +
		"fc.flowKey=t.flowKey and t.formID=re.id and (t.taskState=:createState or t.taskState=:startState) " +
		"and ( (t.transactor=:userId) or " +
		"(p.assignKey=:userId and t.transactor is null and p.type=:type)" +
		") order by t.createTime desc";
        try {
            Query query = getSession().createQuery(hql);
            query.setString("userId", userId);
            query.setString("createState", TaskInstance.PROCESS_STATE_CREATE);
            query.setString("startState", TaskInstance.PROCESS_STATE_START);
            query.setString("type", TaskAssign.TYPE_USER);
            List<Object[]> list = query.list();
            List<DevDiscardForm> discardForms = new ArrayList<DevDiscardForm>();
            DevDiscardForm discardForm = null;
            for (Object[] o : list) {
                discardForm = (DevDiscardForm) o[0];
                // discardForm.setTask((Task)o[1]);
                discardForms.add(discardForm);
            }
            return discardForms;
        } catch (RuntimeException re) {
            log.error("queryDealDiscardForm", re);
            throw re;
        }
    }

    @Override
    public ListPage<DevDiscardForm> queryArchDiscardForm(DevDiscardQueryParameters rqp, String userAccountId) {
        log.debug("queryArchDiscardForm with DevDiscardQueryParameters and userAccountId: " + userAccountId);
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:报废单查询参数对象为空");
        }
        try {
            StringBuffer hql = new StringBuffer(
					"from DevDiscardForm as re, Task as t where (t.formID=re.id and t.taskState=:endState " +
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

            return new CommQuery<DevDiscardForm>().queryListPage(rqp,
                    "select distinct re " + rqp.appendOrders(hql, "re"), "select count(distinct re) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            log.error("queryArchDiscardForm faild", re);
            return new ListPage<DevDiscardForm>();
        }
    }

    @Override
    public ListPage<DevDiscardForm> queryTrackDiscardForm(DevDiscardQueryParameters rqp, String userAccountId) {
        log.debug("queryTrackDiscardForm with DevDiscardQueryParameters and userAccountId:" + userAccountId);
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:报废单查询参数对象为空");
        }
        try {
            StringBuffer hql = new StringBuffer(
					"from DevDiscardForm as re, Task as t where  t.formID=re.id and (t.taskState=:endState " +
					"and t.transactor=:userId  or re.draftsman=:userId and re.formStatus=:status)  and re.archiveDate is null ");

            rqp.addParameter("endState", TaskInstance.PROCESS_STATE_END);
            rqp.addParameter("userId", userAccountId);
            rqp.addParameter("status", DevDiscardForm.FORMSTATUS_APPROVAL);
            if (rqp.getDeviceName() != null) {
                hql.append(" and re.device.deviceName like :deviceName ");
                rqp.toArountParameter("deviceName");
            }
            if (rqp.getBeginDraftDate() != null) {
                hql.append(" and re.applyDate >= :beginDraftDate ");
            }
            if (rqp.getEndDraftDate() != null) {
                hql.append(" and re.applyDate <= :endDraftDate ");
            }

            return new CommQuery<DevDiscardForm>().queryListPage(rqp,
                    "select distinct re " + rqp.appendOrders(hql, "re"), "select count(distinct re) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            log.error("queryTrackDiscardForm faild", re);
            return new ListPage<DevDiscardForm>();
        }
    }

    @Override
    public ListPage<DevDiscardForm> queryAllArchedDiscardForm(DevDiscardQueryParameters rqp) {
        log.debug("queryAllArchedDiscardForm with DevDiscardQueryParameters");
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:报废单查询参数对象为空");
        }
        try {
            StringBuffer hql = new StringBuffer(
					"from DevDiscardForm as re, Task as t where (t.formID=re.id and t.taskState=:endState " +
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

            return new CommQuery<DevDiscardForm>().queryListPage(rqp,
                    "select distinct re " + rqp.appendOrders(hql, "re"), "select count(distinct re) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            log.error("queryAllArchedDiscardForm faild", re);
            return new ListPage<DevDiscardForm>();
        }
    }

    @Override
    public Integer getMaxSequenceNo(Integer sequenceYear, Integer formType) {
        try {
			String queryString = "select max(d.applyFormNO) from DevDiscardForm d where d.sequenceYear=:sequenceYear";
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

    @Override
    public List<DiscardDevList> queryArchDevScrapListByDeviceID(String deviceID, Boolean archiveDateOrder) {
        log.debug("queryArchDevScrapListByDeviceID with deviceID: " + deviceID + " and archiveDateOrder: "
                + archiveDateOrder);
        StringBuffer hql = new StringBuffer("select list from DiscardDevList as list" +
				" where list.devDiscardForm.formStatus=" + DevDiscardForm.FORMSTATUS_PUBLISH);
        if (deviceID != null && !"".equals(deviceID)) {
            hql.append(" and list.device.id='" + deviceID + "'");
        }
        if (archiveDateOrder != null) {
            hql.append(" order by list.devDiscardForm.archiveDate");
            if (!archiveDateOrder) {
                hql.append(" desc");
            }
        }
        try {
            Query query = getSession().createQuery(hql.toString());
            return (List<DiscardDevList>) query.list();
        } catch (RuntimeException re) {
            log.error("queryArchDevScrapListByDeviceID", re);
            throw re;
        }
    }

    @Override
    public List<DevDiscardForm> queryArchDevDiscardFormDeviceID(String deviceID) {
        log.debug("queryArchDevDiscardFormDeviceID with deviceID: " + deviceID);
	    StringBuffer hql = new StringBuffer("from DevDiscardForm as df left join df.discardDevLists as dfl" +
				" where dfl.device.id='" + deviceID + "' and df.formStatus=" + DevDiscardForm.FORMSTATUS_PUBLISH +
						" order by df.archiveDate desc");
        try {
            Query query = getSession().createQuery(hql.toString());
            return (List<DevDiscardForm>) query.list();
        } catch (RuntimeException re) {
            log.error("queryArchDevDiscardFormDeviceID", re);
            throw re;
        }
    }

    @Override
    public DiscardDevList findDiscardDevListByID(String ID) {
        log.debug("getting DiscardDevList instance with id: " + ID);
        try {
            DiscardDevList instance = (DiscardDevList) getSession().get(DiscardDevList.class, ID);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<DevDiscardForm> queryDealDevDiscardFormByDeviceID(String deviceID, Integer formType) {
        log.debug("queryDealDevDiscardFormByDeviceID with deviceID: " + deviceID + " and formType: " + formType);
        StringBuffer hql = new StringBuffer("select distinct(dev)  from DevDiscardForm as dev left join dev.discardDevLists as list " +
				" where 1=1 and dev.formStatus ="+DevFlowApplyProcess.FORMSTATUS_APPROVAL);
        if (deviceID != null && !"".equals(deviceID)) {
            hql.append(" and list.device.id=:deviceID");
        }
        if (formType != null) {
            hql.append(" and dev.formType=:formType");
        }
        try {
            Query query = getSession().createQuery(hql.toString());
            if (deviceID != null && !"".equals(deviceID)) {
                query.setParameter("deviceID", deviceID);
            }
            if (formType != null) {
                query.setParameter("formType", formType);
            }
            return (List<DevDiscardForm>) query.list();
        } catch (RuntimeException re) {
            log.error("queryDealDevDiscardFormByDeviceID", re);
            throw re;
        }
    }
}