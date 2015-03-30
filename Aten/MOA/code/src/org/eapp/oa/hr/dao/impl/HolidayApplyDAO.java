package org.eapp.oa.hr.dao.impl;

// default package
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.flow.hbean.TaskAssign;
import org.eapp.oa.hr.dao.IHolidayApplyDAO;
import org.eapp.oa.hr.dto.HolidayQueryParameters;
import org.eapp.oa.hr.hbean.HolidayApply;
import org.eapp.oa.hr.hbean.HolidayDetail;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Query;
import org.hibernate.criterion.Example;


public class HolidayApplyDAO extends BaseHibernateDAO implements IHolidayApplyDAO {

    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(HolidayApplyDAO.class);

    public HolidayApply findById(java.lang.String id) {
        log.debug("getting HolidayApply instance with id: " + id);
        try {
            HolidayApply instance = (HolidayApply) getSession().get(HolidayApply.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<HolidayApply> findByExample(HolidayApply instance) {
        log.debug("finding HolidayApply instance by example");
        try {
            List<HolidayApply> results = getSession().createCriteria("HolidayApply").add(Example.create(instance))
                    .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<HolidayApply> findHolidayApplys(String userAccountId, int formStatus) {
        try {
            String queryString = "from HolidayApply as h where h.applyStatus=:applyStatus and h.applicant=:applicant order by h.applyDate desc";

            Query queryObject = getSession().createQuery(queryString);
            queryObject.setString("applicant", userAccountId);
            queryObject.setInteger("applyStatus", formStatus);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("finding binding by userAccount failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<HolidayApply> getDealingHolidayApply(String userAccountId, List<String> userRoles) {

        String hql = "select distinct ha,t from HolidayApply as ha, FlowConfig as fc, "
                + "Task as t left join t.taskAssigns as p where fc.flowClass=:flowClass and fc.draftFlag='2' and "
                + "fc.flowKey=t.flowKey and t.formID=ha.id and (t.taskState=:createState or t.taskState=:startState) "
                + "and ( (t.transactor=:userId) or "
                + "(p.assignKey=:userId and t.transactor is null and p.type=:userType) or " 
                + "(p.assignKey in (:userRoles) and t.transactor is null and p.type=:roleType)  ) order by t.createTime";
        try {
            Query query = getSession().createQuery(hql);
            query.setString("flowClass", SysConstants.FLOWCLASS_QJ);
//            query.setString("xjFlowClass", SysConstants.FLOWCLASS_XJ);
            query.setString("userId", userAccountId);
            query.setString("createState", TaskInstance.PROCESS_STATE_CREATE);
            query.setString("startState", TaskInstance.PROCESS_STATE_START);
            query.setString("userType", TaskAssign.TYPE_USER);
            query.setParameterList("userRoles", userRoles);
            query.setString("roleType", TaskAssign.TYPE_ROLE);
            List<Object[]> list = query.list();
            List<HolidayApply> has = new ArrayList<HolidayApply>();
            for (Object[] o : list) {
                HolidayApply ha = (HolidayApply) o[0];
                ha.setTask((Task) o[1]);
                has.add(ha);
            }
            return has;
        } catch (RuntimeException re) {
            log.error("getDealingHolidayApply faild", re);
            throw re;
        }

    }

    public ListPage<HolidayApply> getTrackOrArchHolidayApply(HolidayQueryParameters rqp, String userAccountId,
            Boolean isArch) {
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }
        try {
            StringBuffer hql = new StringBuffer(
                    "from HolidayApply as re, FlowConfig as fc, Task as t "
                            + "where fc.flowClass=:flowClass and fc.flowKey=t.flowKey and t.formID=re.id "
                            + "and (t.taskState=:endState and t.transactor=:userId or re.applicant=:userId)");
            rqp.addParameter("flowClass", SysConstants.FLOWCLASS_QJ);
            rqp.addParameter("endState", TaskInstance.PROCESS_STATE_END);
            rqp.addParameter("userId", userAccountId);
            
            if (isArch) {
                hql.append(" and re.archiveDate is not null");
            } else {
//                hql.append(" and re.archiveDate is null ");
            	hql.append(" and re.applyStatus=1");//销假是原来表单重新开启，不能以归档时间来判断
            }
            
            if (rqp.getApplicant() != null) {
            	hql.append(" and re.applicant = :applicant ");
            }
            if (rqp.getID() != null) {
                hql.append(" and re.id like :id ");
                rqp.toArountParameter("id");
            }

            return new CommQuery<HolidayApply>().queryListPage(rqp,
                    "select distinct re " + rqp.appendOrders(hql, "re"), "select count(distinct re) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            log.error("getTrackOrArchHolidayApply faild", re);
            return new ListPage<HolidayApply>();
        }
    }

    public String getMaxID() {
        try {
            Query query = getSession().createQuery("select max(rd.id) from HolidayApply rd where rd.id like :year");
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            query.setString("year", year + "%");
            return (String) query.uniqueResult();
        } catch (RuntimeException re) {
            log.error("getMaxID faild", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<HolidayApply> findByDeptAndDate(Date startTime, Date endTime, String dept) {

        try {
            StringBuffer hql = new StringBuffer(
                    "from HolidayApply as ha where ha.holidayStartDate<=:endTime and ha.holidayEndDate>=:startTime and ha.applyDept=:dept and ha.passed=1");

            Query query = getSession().createQuery(hql.toString());
            query.setParameter("endTime", endTime);
            query.setParameter("startTime", startTime);
            query.setString("dept", dept);
            List<HolidayApply> list = query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("findByDeptAndDate faild", re);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<HolidayDetail> findByTime(Date startTime, Date endTime) {
        log.debug("findByTime with startTime: " + startTime + " and endTime: " + endTime);
        try {
            StringBuffer hql = new StringBuffer(
                    "from HolidayDetail as ha where ha.startDate<=:endTime and ha.endDate>=:startTime and ha.holidayApply.passed=1 "
                            + " order by ha.holidayApply.applyDept,ha.holidayApply.applicant");
            Query query = getSession().createQuery(hql.toString());
            query.setParameter("endTime", endTime);
            query.setParameter("startTime", startTime);
            List<HolidayDetail> list = query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("findByTime faild", re);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<HolidayDetail> findHoliday4PreMonth(Date startTime, Date endTime) {
        try {
            StringBuffer hql = new StringBuffer(
                    "from HolidayDetail as ha where ha.startDate<=:startTime and ha.holidayApply.passed=1 "
                            + " order by ha.holidayApply.applyDept,ha.holidayApply.applicant");
            Query query = getSession().createQuery(hql.toString());
            query.setParameter("endTime", endTime);
            query.setParameter("startTime", startTime);
            List<HolidayDetail> list = query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("findHoliday4PreMonth faild", re);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<HolidayApply> findUnArchivedHolidayApply() {
        log.debug("findUnArchivedHolidayApply");
        String hql = "select ha from HolidayApply as ha where ha.archiveDate is null";
        try {
            Query query = getSession().createQuery(hql);
            return query.list();
        } catch (RuntimeException re) {
            log.error("findUnArchivedHolidayApply faild", re);
            throw re;
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<HolidayDetail> findHolidayDetailByTime(String formID, Date startTime, Date endTime) {
        log.debug("findHolidayDetailByTime with formID: " + formID + " and startTime: " + startTime + " and endTime: "
                + endTime);
        try {
            StringBuffer hql = new StringBuffer(
                    "from HolidayDetail as ha where ha.startDate<=:endTime and ha.endDate>=:startTime "
                            + " and ha.holidayApply.passed=1 and ha.sourceId=:holidayAppId order by ha.holidayApply.applyDept,ha.holidayApply.applicant");
            Query query = getSession().createQuery(hql.toString());
            query.setParameter("endTime", endTime);
            query.setParameter("startTime", startTime);
            query.setParameter("holidayAppId", formID);
            List<HolidayDetail> list = query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("findHolidayDetailByTime faild", re);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<HolidayDetail> getHolidayIsApply(Date startTime, Date endTime, String userId) {
        try {
            StringBuffer hql = new StringBuffer(
                    "from HolidayDetail as ha where ha.startDate<=:endTime and ha.endDate>=:startTime "
                            + " and ha.holidayApply.applyStatus=1  and ha.holidayApply.applicant=:userId");
            Query query = getSession().createQuery(hql.toString());
            query.setParameter("endTime", endTime);
            query.setParameter("startTime", startTime);
            query.setParameter("userId", userId);
            List<HolidayDetail> list = query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("getHolidayIsApply faild", re);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<HolidayDetail> getHolidayIsCancel(Date startTime, Date endTime, String sourceId) {
        try {
            StringBuffer hql = new StringBuffer(
                    "from HolidayDetail as ha where ha.startDate<=:startTime and ha.endDate>=:endTime "
                            + " and ha.holidayApply.passed=1  and ha.sourceId=:sourceId");
            Query query = getSession().createQuery(hql.toString());
            query.setParameter("endTime", endTime);
            query.setParameter("startTime", startTime);
            query.setParameter("sourceId", sourceId);
            List<HolidayDetail> list = query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("getHolidayIsCancel faild", re);
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public List<HolidayDetail> findUserArchHolidays(String userAccountId, Date startDate) {
    	StringBuffer hql = new StringBuffer("from HolidayDetail as hd where hd.holidayApply.applicant=:applicant " +
				"and hd.holidayApply.applyStatus=:applyStatus and hd.endDate>=:startDate");
    	Query query = getSession().createQuery(hql.toString());
     	query.setString("applicant", userAccountId);
      	query.setInteger("applyStatus", HolidayApply.STATUS_ARCH);
      	query.setDate("startDate", startDate);
      	return query.list();
    }
    
    @Override
    public ListPage<HolidayDetail> findHolidayDetail(HolidayQueryParameters rqp) {
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }
        try {
            StringBuffer hql = new StringBuffer(
                    "from HolidayDetail as d where d.holidayApply.archiveDate is not null and d.holidayApply.passed = 1");
            
            if (rqp.getApplicant() != null) {
            	hql.append(" and d.holidayApply.applicant = :applicant ");
            }
            if (rqp.getBgnQueryDate() != null) {
            	hql.append(" and d.endDate >= :bgnQueryDate");//请假结束日期在开始时间之后
            }
            if (rqp.getEndQueryDate() != null) {
            	hql.append(" and d.startDate <= :endQueryDate ");//请假开始日期在结束时间之前
            }

            return new CommQuery<HolidayDetail>().queryListPage(rqp,
                    "select d " + rqp.appendOrders(hql, "d"), "select count(d) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            log.error("findHolidayDetail faild", re);
            return new ListPage<HolidayDetail>();
        }
    }
}
