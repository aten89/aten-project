package org.eapp.oa.hr.dao.impl;

// default package
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.flow.hbean.TaskAssign;
import org.eapp.oa.hr.dao.IStaffFlowApplyDAO;
import org.eapp.oa.hr.dto.StaffFlowQueryParameters;
import org.eapp.oa.hr.hbean.StaffFlowApply;
import org.eapp.oa.hr.hbean.StaffFlowQueryAssign;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Query;
import org.hibernate.criterion.Example;


public class StaffFlowApplyDAO extends BaseHibernateDAO implements IStaffFlowApplyDAO {

    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(StaffFlowApplyDAO.class);

    @Override
    public StaffFlowApply findById(java.lang.String id) {
        log.debug("getting StaffFlowApply instance with id: " + id);
        try {
            StaffFlowApply instance = (StaffFlowApply)getSession().get(StaffFlowApply.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    @Override
    public StaffFlowApply findByUserAccountId(String userAccountID) {
    	String queryString = "from StaffFlowApply as h where h.applyType=:applyType and h.userAccountID=:userAccountID and h.passed=1";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setInteger("applyType", StaffFlowApply.TYPE_ENTRY);
        queryObject.setString("userAccountID", userAccountID);
//        queryObject.setBoolean("passed", true);
        queryObject.setMaxResults(1);
        return (StaffFlowApply)queryObject.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<StaffFlowApply> findByExample(StaffFlowApply instance) {
        log.debug("finding StaffFlowApply instance by example");
        try {
            List<StaffFlowApply> results = getSession().createCriteria("StaffFlowApply").add(Example.create(instance))
                    .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<StaffFlowApply> findStaffFlowApplys(String userAccountId, int formStatus) {
        try {
            String queryString = "from StaffFlowApply as h where h.applyStatus=:applyStatus and h.applicant=:applicant order by h.applyDate desc";

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
    public List<StaffFlowApply> getDealingStaffFlowApply(String userAccountId) {
        String hql = "select distinct ha,t from StaffFlowApply as ha, FlowConfig as fc, "
                + "Task as t left join t.taskAssigns as p where fc.flowClass=:flowClass and fc.draftFlag='2' and "
                + "fc.flowKey=t.flowKey and t.formID=ha.id and (t.taskState=:createState or t.taskState=:startState) "
                + "and ( (t.transactor=:userId) or "
                + "(p.assignKey=:userId and t.transactor is null and p.type=:type) ) order by t.createTime";
        try {
            Query query = getSession().createQuery(hql);
            query.setString("flowClass", SysConstants.FLOWCLASS_HR);
            query.setString("userId", userAccountId);
            query.setString("createState", TaskInstance.PROCESS_STATE_CREATE);
            query.setString("startState", TaskInstance.PROCESS_STATE_START);
            query.setString("type", TaskAssign.TYPE_USER);
            List<Object[]> list = query.list();
            List<StaffFlowApply> has = new ArrayList<StaffFlowApply>();
            for (Object[] o : list) {
                StaffFlowApply ha = (StaffFlowApply) o[0];
                ha.setTask((Task) o[1]);
                has.add(ha);
            }
            return has;
        } catch (RuntimeException re) {
            log.error("getDealingStaffFlowApply faild", re);
            throw re;
        }
    }

    public ListPage<StaffFlowApply> getTrackOrArchStaffFlowApply(StaffFlowQueryParameters rqp, String userAccountId,
            Boolean isArch) {
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }
        try {
            StringBuffer hql = new StringBuffer(
                    "from StaffFlowApply as re, FlowConfig as fc, Task as t "
                            + "where fc.flowClass=:flowClass and fc.flowKey=t.flowKey and t.formID=re.id "
                            + "and (t.taskState=:endState and t.transactor=:userId or re.applicant=:userId)");
            rqp.addParameter("flowClass", SysConstants.FLOWCLASS_HR);
            rqp.addParameter("endState", TaskInstance.PROCESS_STATE_END);
            rqp.addParameter("userId", userAccountId);
            
            if (isArch) {
                hql.append(" and re.archiveDate is not null");
            } else {
                hql.append(" and re.archiveDate is null ");
            }
            
            if (rqp.getApplicant() != null) {
            	hql.append(" and re.applicant = :applicant ");
            }
            if (rqp.getApplyType() != null) {
            	hql.append(" and re.applyType = :applyType ");
            }
            if (rqp.getID() != null) {
                hql.append(" and re.id like :id ");
                rqp.toArountParameter("id");
            }
            if (rqp.getCompanyArea() != null) {
            	hql.append(" and re.companyArea = :companyArea ");
            }

            return new CommQuery<StaffFlowApply>().queryListPage(rqp,
                    "select distinct re " + rqp.appendOrders(hql, "re"), "select count(distinct re) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            log.error("getTrackOrArchStaffFlowApply faild", re);
            return new ListPage<StaffFlowApply>();
        }
    }


    public String getMaxID() {
        try {
            Query query = getSession().createQuery("select max(rd.id) from StaffFlowApply rd where rd.id like :year");
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            query.setString("year", year + "%");
            return (String) query.uniqueResult();
        } catch (RuntimeException re) {
            log.error("getMaxID faild", re);
            throw re;
        }
    }
    
    public String queryMaxEmployeeNumber() {
        try {
            Query query = getSession().createQuery("select max(rd.employeeNumber) from StaffFlowApply rd where rd.applyStatus <>:applyStatus");
            query.setInteger("applyStatus", StaffFlowApply.STATUS_CANCELLATION);
            return (String) query.uniqueResult();
        } catch (RuntimeException re) {
            log.error("getMaxID faild", re);
            throw re;
        }
    }
    
    public boolean checkMaxEmployeeNumber(String employeeNumber, String id) {
        try {
        	String hql = "select count(rd) from StaffFlowApply rd where rd.employeeNumber=:employeeNumber and rd.applyStatus <>:applyStatus";
        	if (StringUtils.isNotBlank(id)) {
        		hql += " and rd.id <> :id";
            }
            Query query = getSession().createQuery(hql);
            query.setString("employeeNumber", employeeNumber);
            query.setInteger("applyStatus", StaffFlowApply.STATUS_CANCELLATION);
            if (StringUtils.isNotBlank(id)) {
            	query.setString("id", id);
            }
            Long count = (Long) query.uniqueResult();
            if (count != null && count.longValue() > 0) {
            	return false;
            }
            return true;
        } catch (RuntimeException re) {
            log.error("getMaxID faild", re);
            throw re;
        }
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public List<StaffFlowApply> queryStaffFlowApply(List<String> accountIDs) {
		if (accountIDs == null || accountIDs.isEmpty())  {
			return null;
		}
		String queryString = "from StaffFlowApply as al where al.applyType=:applyType and al.userAccountID in (:accountIDs) order by al.userAccountID";
		Query query = getSession().createQuery( queryString );
		query.setInteger("applyType", StaffFlowApply.TYPE_ENTRY);
		query.setParameterList("accountIDs", accountIDs);
		return query.list();
	}	
    
    @Override
    public ListPage<StaffFlowApply> queryStaffFlowApply(StaffFlowQueryParameters rqp) {
    	if (rqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }
        try {
            StringBuffer hql = new StringBuffer(
                    "from StaffFlowApply as re where re.passed=1");
            if (rqp.getApplyType() != null) {
            	hql.append(" and re.applyType = :applyType ");
            	if (rqp.getApplyType() == StaffFlowApply.TYPE_ENTRY) {
            		//入职要排除已离职的用户
            		hql.append(" and re.userAccountID not in (select t.userAccountID from StaffFlowApply as t where t.applyType=2 and t.passed=1) ");
            	}
            }
            if (rqp.getGroupName() != null) {
            	hql.append(" and re.groupName = :groupName ");
            }
            if (rqp.getGroupNames() != null) {
            	hql.append(" and re.groupName in (:groupNames) ");
            }
            if (rqp.getUserKeyword() != null) {
				hql.append(" and (re.userName like :userKeyword or re.userAccountID like :userKeyword)");
				rqp.toArountParameter("userKeyword");
			}
            
            if (rqp.getContractEndDate() != null) {
            	hql.append(" and re.contractEndDate < :contractEndDate ");
            }
            if (rqp.getFormalDate() != null) {
            	hql.append(" and re.formalDate < :formalDate ");
            }
            if (rqp.getBirthdate() != null) {
            	//月份<提醒月份 或 月份相等时日期 <提醒日期 或 生日刚好是12月提醒月份是1月应该也在区间内
            	hql.append(" and (month(re.birthdate) <:birthdayMonth or month(re.birthdate) =:birthdayMonth and day(re.birthdate) < :birthdayDay or month(re.birthdate) >:birthdayMonth and month(re.birthdate)=12 and 1=:birthdayMonth) " +
            			//月份>当前月份 或 月份相等时日期 >=当前日期 或 生日刚好是1月当前月份是12月应该也在区间内
            			"and (month(re.birthdate) >:todayMonth or month(re.birthdate) =:todayMonth and day(re.birthdate) >= :todayDay or month(re.birthdate) <:todayMonth and month(re.birthdate)=1 and 12=:todayMonth)");
            	Calendar cal = Calendar.getInstance();
            	rqp.addParameter("todayMonth", cal.get(Calendar.MONTH) + 1);
            	rqp.addParameter("todayDay", cal.get(Calendar.DAY_OF_MONTH));
            	
            	cal.setTime(rqp.getBirthdate());
//            	System.out.println(cal.get(Calendar.MONTH));
//            	System.out.println(cal.get(Calendar.DAY_OF_MONTH));
            	rqp.removeParameter("birthdate");
            	rqp.addParameter("birthdayMonth", cal.get(Calendar.MONTH) + 1);
            	rqp.addParameter("birthdayDay", cal.get(Calendar.DAY_OF_MONTH));
            }

            if (rqp.getStaffStatus() != null) {
            	hql.append(" and re.staffStatus = :staffStatus ");
            }
            
            
            if (rqp.getUserAccountID() != null) {
            	hql.append(" or re.userAccountID = :userAccountID ");
            }
            
            return new CommQuery<StaffFlowApply>().queryListPage(rqp,
                    "select distinct re " + rqp.appendOrders(hql, "re"), "select count(distinct re) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            log.error("getTrackOrArchStaffFlowApply faild", re);
            return new ListPage<StaffFlowApply>();
        }
    }

	@Override
	public void deleteQueryAssign(String userAccountId) {
		Query query = getSession().createQuery("delete from StaffFlowQueryAssign as sqa where sqa.userAccountID=:userAccountID");
		query.setString("userAccountID", userAccountId);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StaffFlowQueryAssign> findStaffFlowQueryAssigns(String userAccountId) {
		Query query = getSession().createQuery("from StaffFlowQueryAssign as sqa where sqa.userAccountID=:userAccountID");
		query.setString("userAccountID", userAccountId);
		return query.list();
	}
}
