package org.eapp.oa.travel.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.travel.dao.IBusTripApplyDAO;
import org.eapp.oa.travel.dto.BusTripQueryParameters;
import org.eapp.oa.travel.hbean.BusTripApply;
import org.eapp.oa.travel.hbean.BusTripApplyDetail;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Query;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.flow.hbean.TaskAssign;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

public class BusTripApplyDAO extends BaseHibernateDAO implements
		IBusTripApplyDAO {

    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(BusTripApplyDAO.class);

    @SuppressWarnings("unchecked")
    @Override
    public List<BusTripApply> getDealingBusTripApply(String userAccountId) {

		String hql = "select distinct ha,t from BusTripApply as ha, FlowConfig as fc, " +
				"Task as t left join t.taskAssigns as p where fc.flowClass=:flowClass  and fc.draftFlag='2' and " +
				"fc.flowKey=t.flowKey and t.formID=ha.id and (t.taskState=:createState or t.taskState=:startState) " +
				"and ( (t.transactor=:userId) or " +
				"(p.assignKey=:userId and t.transactor is null and p.type=:type)" +
				") order by t.createTime";
        try {
        	Query query = getSession().createQuery(hql);
            query.setString("flowClass", SysConstants.FLOWCLASS_TRIP);
            query.setString("userId", userAccountId);
            query.setString("createState", TaskInstance.PROCESS_STATE_CREATE);
            query.setString("startState", TaskInstance.PROCESS_STATE_START);
            query.setString("type", TaskAssign.TYPE_USER);
            
            List<Object[]> list = query.list();
            List<BusTripApply> reis = new ArrayList<BusTripApply>();
            for (Object[] o : list) {
                BusTripApply bus = (BusTripApply) o[0];
                bus.setTask((Task) o[1]);
                reis.add(bus);
            }
            return reis;
        } catch (RuntimeException re) {
            log.error("getDealingBusTripApply faild", re);
            throw re;
        }

    }

    @Override
    public ListPage<BusTripApply> getTrackOrArchTripApply(BusTripQueryParameters rqp, String userAccount, boolean isArch) {
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }
        try {
            StringBuffer hql = new StringBuffer(
					"from BusTripApply as re, FlowConfig as fc, Task as t " +
					"where fc.flowClass=:flowClass  and fc.flowKey=t.flowKey and t.formID=re.id  " +
					"and (t.taskState=:endState and t.transactor=:userId or re.applicant=:userId)");
            rqp.addParameter("flowClass", SysConstants.FLOWCLASS_TRIP);
            rqp.addParameter("endState", TaskInstance.PROCESS_STATE_END);
            rqp.addParameter("userId", userAccount);

            if (isArch) {
                hql.append(" and re.archiveDate is not null");
            } else {
                hql.append(" and re.archiveDate is null ");
            }
            
            if (rqp.getApplicant() != null) {
            	hql.append(" and re.applicant = :applicant ");
            }

            if (rqp.getID() != null) {
                hql.append(" and re.id like :id ");
                rqp.toArountParameter("id");
            }

            return new CommQuery<BusTripApply>().queryListPage(rqp,
                    "select distinct re " + rqp.appendOrders(hql, "re"), "select count(distinct re) " + hql.toString() ,getSession());
        } catch (RuntimeException re) {
            log.error("getTrackOrArchTripApply faild", re);
            return new ListPage<BusTripApply>();
        }
    }

	@Override
    public String getMaxID() {
	    log.debug("getMaxID");
        try {
            Query query = getSession().createQuery("select max(rd.id) from BusTripApply rd where rd.id like :year");
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            query.setString("year", year + "%");
            return (String) query.uniqueResult();
        } catch (RuntimeException re) {
            log.error("getMaxID faild", re);
            throw re;
        }
    }

    @Override
    public BusTripApply findById(String id) {
        log.debug("getting BusTripApply instance with id: " + id);
        try {
            BusTripApply instance = (BusTripApply) getSession().get(BusTripApply.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public List<BusTripApply> findBusTripApplys(String userAccountId, int formStatus) {
    	 try {
             String queryString = "from BusTripApply as h where h.applyStatus=:applyStatus and h.applicant=:applicant order by h.applyDate desc";

             Query queryObject = getSession().createQuery(queryString);
             queryObject.setString("applicant", userAccountId);
             queryObject.setInteger("applyStatus", formStatus);
             return queryObject.list();
         } catch (RuntimeException re) {
             log.error("finding binding by userAccount failed", re);
             throw re;
         }
    }

    @Override
    public ListPage<BusTripApply> queryArchTripApply(BusTripQueryParameters rqp, Timestamp startDate, Timestamp endDate) {
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }
        try {
            StringBuffer hql = new StringBuffer(
					"from BusTripApply as re left join re.busTripApplyDetail as de " +
					" where re.applyStatus<>0");
            if (rqp.getApplicant() != null && rqp.getApplicant().length() > 0) {
                hql.append(" and re.applicant =:applicant ");
            }
            if (rqp.getID() != null && rqp.getID().length() > 0) {
                hql.append(" and re.id like:id ");
                rqp.toArountParameter("id");
            }
            if (startDate != null) {
                hql.append(" and de.endDate >=:startDate ");
                rqp.addParameter("startDate", startDate);
            }
            if (endDate != null) {
                hql.append(" and de.startDate <=:endDate ");
                rqp.addParameter("endDate", endDate);
            }
            if (rqp.getApplyDept() != null && rqp.getApplyDept().length() > 0) {
                hql.append(" and re.applyDept =:applyDept ");
            }
            // if(startDate != null && endDate != null){
            // hql.append(" and de.endDate >=:startDate and de.startDate <=:endDate" );
            // rqp.addParameter("startDate", startDate);
            // rqp.addParameter("endDate", endDate);
            // }
            return new CommQuery<BusTripApply>().queryListPage(rqp,
                    "select distinct re " + rqp.appendOrders(hql, "re"), "select count(distinct re) " + hql.toString() ,getSession());
        } catch (RuntimeException re) {
            re.printStackTrace();
            return new ListPage<BusTripApply>();
        }
    }

	@SuppressWarnings("unchecked")
    @Override
    public List<BusTripApplyDetail> getTripIsApply(Date startTime, Date endTime, String userAccount, String id) {
        try {
			StringBuffer hql = new StringBuffer("from BusTripApplyDetail as ha where ha.startDate<=:endTime and ha.endDate>=:startTime " +
						" and (ha.busTripApply.passed=1 or ha.busTripApply.passed is null) and ha.busTripApply.applyStatus in (1,2) " +
						" and ha.busTripApply.applicant=:userId ");				
            if (id != null && id.length() > 0) {
                hql.append("and ha.busTripApply.id<>:id");
            }
            Query query = getSession().createQuery(hql.toString());
            query.setParameter("endTime", endTime);
            query.setParameter("startTime", startTime);
            query.setParameter("userId", userAccount);
            if (id != null && id.length() > 0) {
                query.setParameter("id", id);
            }
            List<BusTripApplyDetail> list = query.list();
            return list;
        } catch (RuntimeException re) {
            re.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BusTripApplyDetail> getSourceApply(Date startTime, Date endTime, String sourceId) {
        try {
			StringBuffer hql = new StringBuffer("from BusTripApplyDetail as ha where ha.startDate<=:startTime and ha.endDate>=:endTime " +
			" and ha.busTripApply.passed=1 and ha.busTripApply.id=:sourceId ");
            Query query = getSession().createQuery(hql.toString());
            query.setParameter("endTime", endTime);
            query.setParameter("startTime", startTime);
            query.setParameter("sourceId", sourceId);
            List<BusTripApplyDetail> list = query.list();
            return list;
        } catch (RuntimeException re) {
            re.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BusTripApplyDetail> getArchDetails(String id, String applicant, Timestamp startDate, Timestamp endDate,
            String applyDept) {
        try {
            StringBuffer hql = new StringBuffer(
					"from BusTripApplyDetail as de" +
					" where de.busTripApply.passed=1 and de.busTripApply.applyStatus=1");
            if (id != null && id.length() > 0) {
                hql.append(" and de.busTripApply.id like:id ");
            }
            if (applicant != null && applicant.length() > 0) {
                hql.append(" and de.busTripApply.applicant =:applicant ");
            }
            if (startDate != null) {
                hql.append(" and de.endDate >=:startDate ");
            }
            if (endDate != null) {
                hql.append(" and de.startDate <=:endDate ");
            }
            if (applyDept != null && applyDept.length() > 0) {
                hql.append(" and de.busTripApply.applyDept =:applyDept ");
            }
            hql.append(" order by de.busTripApply.applyDate");
            Query query = getSession().createQuery(hql.toString());
            if (id != null && id.length() > 0) {
                query.setParameter("id", "%" + id + "%");
            }
            if (applicant != null && applicant.length() > 0) {
                query.setParameter("applicant", applicant);
            }
            if (startDate != null) {
                query.setParameter("startDate", startDate);
            }
            if (endDate != null) {
                query.setParameter("endDate", endDate);
            }
            if (applyDept != null && applyDept.length() > 0) {
                query.setParameter("applyDept", applyDept);
            }
            List<BusTripApplyDetail> list = query.list();
            return list;
        } catch (RuntimeException re) {
            re.printStackTrace();
            return null;
        }
    }

}
