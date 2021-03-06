package org.eapp.oa.hr.dao.impl;

// default package
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.flow.hbean.TaskAssign;
import org.eapp.oa.hr.dao.IPositiveApplyDAO;
import org.eapp.oa.hr.dto.PositiveQueryParameters;
import org.eapp.oa.hr.hbean.PositiveApply;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Query;


public class PositiveApplyDAO extends BaseHibernateDAO implements IPositiveApplyDAO {

    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(PositiveApplyDAO.class);

    public PositiveApply findById(java.lang.String id) {
        log.debug("getting PositiveApply instance with id: " + id);
        try {
        	PositiveApply instance = (PositiveApply) getSession().get(PositiveApply.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }


    @SuppressWarnings("unchecked")
    public List<PositiveApply> findPositiveApplys(String userAccountId, int formStatus) {
        try {
            String queryString = "from PositiveApply as h where h.applyStatus=:applyStatus and h.applicant=:applicant order by h.applyDate desc";

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
    public List<PositiveApply> getDealingPositiveApply(String userAccountId, List<String> userRoles) {

        String hql = "select distinct ha,t from PositiveApply as ha, FlowConfig as fc, "
                + "Task as t left join t.taskAssigns as p where fc.flowClass=:flowClass and fc.draftFlag='2' and "
                + "fc.flowKey=t.flowKey and t.formID=ha.id and (t.taskState=:createState or t.taskState=:startState) "
                + "and ( (t.transactor=:userId) or "
                + "(p.assignKey=:userId and t.transactor is null and p.type=:userType) or " 
                + "(p.assignKey in (:userRoles) and t.transactor is null and p.type=:roleType)  ) order by t.createTime";
        try {
            Query query = getSession().createQuery(hql);
            query.setString("flowClass", SysConstants.FLOWCLASS_POSI);
            query.setString("userId", userAccountId);
            query.setString("createState", TaskInstance.PROCESS_STATE_CREATE);
            query.setString("startState", TaskInstance.PROCESS_STATE_START);
            query.setString("userType", TaskAssign.TYPE_USER);
            query.setParameterList("userRoles", userRoles);
            query.setString("roleType", TaskAssign.TYPE_ROLE);
            List<Object[]> list = query.list();
            List<PositiveApply> has = new ArrayList<PositiveApply>();
            for (Object[] o : list) {
            	PositiveApply ha = (PositiveApply) o[0];
                ha.setTask((Task) o[1]);
                has.add(ha);
            }
            return has;
        } catch (RuntimeException re) {
            log.error("getDealingPositiveApply faild", re);
            throw re;
        }

    }

    public ListPage<PositiveApply> getTrackOrArchPositiveApply(PositiveQueryParameters rqp, String userAccountId,
            Boolean isArch) {
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }
        try {
            StringBuffer hql = new StringBuffer(
                    "from PositiveApply as re, FlowConfig as fc, Task as t "
                            + "where fc.flowClass=:flowClass and fc.flowKey=t.flowKey and t.formID=re.id "
                            + "and (t.taskState=:endState and t.transactor=:userId or re.applicant=:userId)");
            rqp.addParameter("flowClass", SysConstants.FLOWCLASS_POSI);
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

            return new CommQuery<PositiveApply>().queryListPage(rqp,
                    "select distinct re " + rqp.appendOrders(hql, "re"), "select count(distinct re) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            log.error("getTrackOrArchPositiveApply faild", re);
            return new ListPage<PositiveApply>();
        }
    }

    public String getMaxID() {
        try {
            Query query = getSession().createQuery("select max(rd.id) from PositiveApply rd where rd.id like :year");
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            query.setString("year", year + "%");
            return (String) query.uniqueResult();
        } catch (RuntimeException re) {
            log.error("getMaxID faild", re);
            throw re;
        }
    }
}
