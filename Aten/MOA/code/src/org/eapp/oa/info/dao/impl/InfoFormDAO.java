package org.eapp.oa.info.dao.impl;

// default package

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.flow.hbean.TaskAssign;
import org.eapp.oa.info.dao.IInfoFormDAO;
import org.eapp.oa.info.dto.InfoFormQueryParameters;
import org.eapp.oa.info.hbean.InfoForm;
import org.eapp.oa.info.hbean.Information;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Query;
import org.hibernate.criterion.Example;


/**
 * A data access object (DAO) providing persistence and search support for
 * Archives entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Archives
 * @author MyEclipse Persistence Tools
 */

public class InfoFormDAO extends BaseHibernateDAO implements IInfoFormDAO {

    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(InfoFormDAO.class);
    // property constants
    /**
     *  subject
     */
    public static final String SUBJECT = "subject";
    /**
     * receiveFrom
     */
    public static final String RECEIVE_FROM = "receiveFrom";
    /**
     * copyInfoFormId
     */
    public static final String COPYINFOFORMID = "copyInfoFormId";

    /*
     * (non-Javadoc)
     * 
     */
    public InfoForm findById(java.lang.String id) {
        log.debug("getting InfoForm instance with id: " + id);
        try {
            InfoForm instance = (InfoForm) getSession().get(InfoForm.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @SuppressWarnings("unchecked")
    public List<InfoForm> findByExample(InfoForm instance) {
        log.debug("finding InfoForm instance by example");
        try {
            List<InfoForm> results = getSession().createCriteria("InfoForm").add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @SuppressWarnings("unchecked")
    public List<InfoForm> findByProperty(String propertyName, Object value) {
        log.debug("finding InfoForm instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from InfoForm as model where model." + propertyName + "= ?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<InfoForm> findBySubject(Object subject) {
        return findByProperty(SUBJECT, subject);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<InfoForm> findByReceiveFrom(Object receiveFrom) {
        return findByProperty(RECEIVE_FROM, receiveFrom);
    }

    public List<InfoForm> findByCopyInfoFormId(Object receiveFrom) {
        return findByProperty(COPYINFOFORMID, receiveFrom);
    }

    /*
     * (non-Javadoc)
     * 
     */
    @SuppressWarnings("unchecked")
    public List<InfoForm> findAll() {
        log.debug("finding all InfoForm instances");
        try {
            String queryString = "from InfoForm";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<InfoForm> queryDealingInfoForm(String userId) {
	    log.debug("queryDealingInfoForm with userId: " + userId);
		String hql = "select re,t from InfoForm as re, FlowConfig as fc, " +
		"Task as t left join t.taskAssigns as p where fc.flowClass in (select il.flowClass from InfoLayout as il where il.flowClass is not null) and fc.draftFlag='2' and " +
		"fc.flowKey=t.flowKey and t.formID=re.id and (t.taskState=:createState or t.taskState=:startState) " +
		"and ( (t.transactor=:userId) or " +
		"(p.assignKey=:userId and t.transactor is null and p.type=:type)" +
		") order by t.createTime";
        try {
            Query query = getSession().createQuery(hql);
            // query.setString("flowClass", SysConstants.FLOWCLASS_BX);
            query.setString("userId", userId);
            query.setString("createState", TaskInstance.PROCESS_STATE_CREATE);
            query.setString("startState", TaskInstance.PROCESS_STATE_START);
            query.setString("type", TaskAssign.TYPE_USER);
            List<Object[]> list = query.list();
            List<InfoForm> infoForms = new ArrayList<InfoForm>();
            for (Object[] o : list) {
                InfoForm info = (InfoForm) o[0];
                info.setTask((Task) o[1]);
                infoForms.add(info);
            }
            return infoForms;
        } catch (RuntimeException re) {
            log.error("queryDealingInfoForm faild", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public ListPage<InfoForm> queryArchInfoForm(InfoFormQueryParameters rqp, String userAccountId) {
        log.debug("queryArchInfoForm with InfoFormQueryParameters and userAccountId" + userAccountId);
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }
        try {
            // StringBuffer hql = new StringBuffer(
            // "from InfoForm as re, FlowConfig as fc, Task as t " +
            // "where ((fc.flowClass in (select il.flowClass from InfoLayout as il where il.flowClass is not null) and fc.flowKey=t.flowKey and t.formID=re.id "
            // +
            // "and t.taskState=:endState and t.transactor=:userId) or re.information.draftsMan=:userId)");
            // rqp.addParameter("endState", TaskInstance.PROCESS_STATE_END);
            StringBuffer hql = new StringBuffer(
					"from InfoForm as re, Task as t where (t.formID=re.id and t.taskState=:endState " +
					"and t.transactor=:userId or re.information.draftsMan=:userId) and re.archiveDate is not null");
            rqp.addParameter("endState", TaskInstance.PROCESS_STATE_END);
            rqp.addParameter("userId", userAccountId);

            if (rqp.getSubject() != null) {
                hql.append(" and re.information.subject like :subject ");
                rqp.toArountParameter("subject");
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

            return new CommQuery<InfoForm>().queryListPage(rqp, "select distinct re " + rqp.appendOrders(hql, "re"),
                    "select count(distinct re) " + hql.toString() ,getSession());
        } catch (RuntimeException re) {
            log.error("queryArchInfoForm faild", re);
            return new ListPage<InfoForm>();
        }
    }

    @Override
    public ListPage<InfoForm> queryTrackInfoForm(InfoFormQueryParameters rqp, String userAccountId) {
        log.debug("queryTrackInfoForm with InfoFormQueryParameters and userAccountId:" + userAccountId);
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }
        try {
            StringBuffer hql = new StringBuffer(
					"from InfoForm as re, Task as t where (t.formID=re.id and t.taskState=:endState " +
					"and t.transactor=:userId or re.information.draftsMan=:userId and re.information.infoStatus=:status) and re.archiveDate is null ");
            rqp.addParameter("endState", TaskInstance.PROCESS_STATE_END);
            rqp.addParameter("userId", userAccountId);
            rqp.addParameter("status", Information.STATUS_APPROVAL);

            if (rqp.getSubject() != null) {
                hql.append(" and re.information.subject like :subject ");
                rqp.toArountParameter("subject");
            }
            if (rqp.getBeginDraftDate() != null) {
                hql.append(" and re.information.draftDate >= :beginDraftDate ");
            }
            if (rqp.getEndDraftDate() != null) {
                hql.append(" and re.information.draftDate <= :endDraftDate ");
            }
            if (rqp.getPassed() != null) {
                hql.append(" and re.passed = :passed ");
            }
            return new CommQuery<InfoForm>().queryListPage(rqp, "select distinct re " + rqp.appendOrders(hql, "re"),
                    "select count(distinct re) " + hql.toString() ,getSession());
        } catch (RuntimeException re) {
            log.error("queryTrackInfoForm faild", re);
            return new ListPage<InfoForm>();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<InfoForm> findInfoForm(String userAccountId, int infoStatus) {
        log.debug("findInfoForm with userAccountId: " + userAccountId + " and infoStatus: " + infoStatus);
        if (userAccountId == null) {
            throw new IllegalArgumentException("非法参数");
        }
        try {
            Query query = getSession().createQuery(
					"from InfoForm as if where if.information.draftsMan=:userAccountId " +
					"and if.information.infoStatus=:infoStatus order by if.information.draftDate desc");
            query.setString("userAccountId", userAccountId);
            query.setInteger("infoStatus", infoStatus);
            return query.list();
        } catch (RuntimeException re) {
            log.error("findInfoForm faild", re);
            throw re;
        }
    }

}