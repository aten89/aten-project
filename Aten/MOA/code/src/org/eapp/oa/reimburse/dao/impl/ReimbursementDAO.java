package org.eapp.oa.reimburse.dao.impl;

// default package

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.flow.hbean.TaskAssign;
import org.eapp.oa.reimburse.dao.IReimbursementDAO;
import org.eapp.oa.reimburse.dto.ReimbursementQueryParameters;
import org.eapp.oa.reimburse.hbean.OutlayList;
import org.eapp.oa.reimburse.hbean.Reimbursement;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * A data access object (DAO) providing persistence and search support for Reimbursement entities. Transaction control
 * of the save(), update() and delete() operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these methods provides additional information
 * for how to configure it for the desired type of transaction control.
 * 
 * @see .Reimbursement
 * @author MyEclipse Persistence Tools
 */

public class ReimbursementDAO extends BaseHibernateDAO implements IReimbursementDAO {

    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(ReimbursementDAO.class);

    // property constants

    /**
     * 经费
     */
    public static final String FINANCE = "finance";

    /**
     * 申请人
     */
    public static final String APPLICANT = "applicant";

    /**
     * 申请日期
     */
    public static final String APPLY_DEPT = "applyDept";

    /**
     * 领款人
     */
    public static final String PAYEE = "payee";

    /**
     * 原因
     */
    public static final String CAUSA = "causa";

    /**
     * 出差地点
     */
    public static final String TRAVEL_PLACE = "travelPlace";

    /**
     * 出差日期
     */
    public static final String TRAVEL_DAYS = "travelDays";

    /**
     * coterielList
     */
    public static final String COTERIEL_LIST = "coterielList";

    /**
     * 借出金额
     */
    public static final String LOAN_SUM = "loanSum";

    /**
     * 预算项目
     */
    public static final String BUDGET_ITEM = "budgetItem";

    /**
     * 地区代码
     */
    public static final String REGION_CODE = "regionCode";

    /**
     * 报销金额
     */
    public static final String REIMBURSEMENT_SUM = "reimbursementSum";

    /**
     * 归档的
     */
    public static final String ARCHIVED = "archived";

    /*
     * (non-Javadoc)
     * 
     */
    public Reimbursement findById(java.lang.String id) {
        log.debug("getting Reimbursement instance with id: " + id);
        try {
            Reimbursement instance = (Reimbursement) getSession().get(Reimbursement.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    @SuppressWarnings("unchecked")
	public List<Reimbursement> findReimbursements(String userAccountId, int formStatus) {
        if (userAccountId == null) {
            throw new IllegalArgumentException("非法参数");
        }
        try {
            Query query = getSession().createQuery(
					"from Reimbursement as df where df.applicant=:applicant " +
					"and df.formStatus=:formStatus order by df.applyDate desc");
            query.setString("applicant", userAccountId);
            query.setInteger("formStatus", formStatus);
            List<Reimbursement> list = query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("findDocForm faild", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @SuppressWarnings("unchecked")
    public List<Reimbursement> findByExample(Reimbursement instance) {
        log.debug("finding Reimbursement instance by example");
        try {
            List<Reimbursement> results = getSession().createCriteria("Reimbursement").add(Example.create(instance))
                    .list();
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
    public List<Reimbursement> findByProperty(String propertyName, Object value) {
        log.debug("finding Reimbursement instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from Reimbursement as model where model." + propertyName + "= ?";
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
    public List<Reimbursement> findByFinance(Object finance) {
        return findByProperty(FINANCE, finance);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<Reimbursement> findByApplicant(Object applicant) {
        return findByProperty(APPLICANT, applicant);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<Reimbursement> findByApplyDept(Object applyDept) {
        return findByProperty(APPLY_DEPT, applyDept);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<Reimbursement> findByPayee(Object payee) {
        return findByProperty(PAYEE, payee);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<Reimbursement> findByCausa(Object causa) {
        return findByProperty(CAUSA, causa);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<Reimbursement> findByTravelPlace(Object travelPlace) {
        return findByProperty(TRAVEL_PLACE, travelPlace);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<Reimbursement> findByTravelDays(Object travelDays) {
        return findByProperty(TRAVEL_DAYS, travelDays);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<Reimbursement> findByCoterielList(Object coterielList) {
        return findByProperty(COTERIEL_LIST, coterielList);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<Reimbursement> findByLoanSum(Object loanSum) {
        return findByProperty(LOAN_SUM, loanSum);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<Reimbursement> findByBudgetItem(Object budgetItem) {
        return findByProperty(BUDGET_ITEM, budgetItem);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<Reimbursement> findByRegionCode(Object regionCode) {
        return findByProperty(REGION_CODE, regionCode);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<Reimbursement> findByReimbursementSum(Object reimbursementSum) {
        return findByProperty(REIMBURSEMENT_SUM, reimbursementSum);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<Reimbursement> findByArchived(Object archived) {
        return findByProperty(ARCHIVED, archived);
    }

    /*
     * (non-Javadoc)
     * 
     */
    @SuppressWarnings("unchecked")
    public List<Reimbursement> findAll() {
        log.debug("finding all Reimbursement instances");
        try {
            String queryString = "from Reimbursement";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Reimbursement> queryDealingReimbursement(String userAccountId) {
        String hql = "select re,t from Reimbursement as re, FlowConfig as fc, "
                + "Task as t left join t.taskAssigns as p where fc.flowClass=:flowClass and fc.draftFlag='2' and "
                + "fc.flowKey=t.flowKey and t.formID=re.id and (t.taskState=:createState or t.taskState=:startState) "
                + "and ( (t.transactor=:userId) or "
                + "(p.assignKey=:userId and t.transactor is null and p.type=:type) ) order by t.createTime desc";
        try {
            Query query = getSession().createQuery(hql);
            query.setString("flowClass", SysConstants.FLOWCLASS_BX);
            query.setString("userId", userAccountId);
            query.setString("createState", TaskInstance.PROCESS_STATE_CREATE);
            query.setString("startState", TaskInstance.PROCESS_STATE_START);
            query.setString("type", TaskAssign.TYPE_USER);
            List<Object[]> list = query.list();
            List<Reimbursement> reis = new ArrayList<Reimbursement>();
            for (Object[] o : list) {
                Reimbursement rei = (Reimbursement) o[0];
                rei.setTask((Task) o[1]);
                reis.add(rei);
            }
            return reis;
        } catch (RuntimeException re) {
            log.error("queryDealingReimbursement", re);
            throw re;
        }
    }

 
    @SuppressWarnings("unchecked")
	public ListPage<Reimbursement> queryDealingReimbursement(ReimbursementQueryParameters rqp, String userAccountId) {
        StringBuffer hql = new StringBuffer("from Reimbursement as re, FlowConfig as fc, "
                + "Task as t left join t.taskAssigns as p where fc.flowClass=:flowClass and fc.draftFlag='2' and "
                + "fc.flowKey=t.flowKey and t.formID=re.id and (t.taskState=:createState or t.taskState=:startState) "
                + "and ( (t.transactor=:userId) or "
                + "(p.assignKey=:userId and t.transactor is null and p.type=:type) )");
        try {
            if (rqp.getID() != null) {
                hql.append(" and re.id like :id ");
                rqp.toArountParameter("id");
            }
            if (rqp.getBeginApplyDate() != null) {
                hql.append(" and re.applyDate >= :beginApplyDate ");
            }
            if (rqp.getEndApplyDate() != null) {
                hql.append(" and re.applyDate <= :endApplyDate ");
            }
            if (rqp.getApplicant() != null) {
                hql.append(" and re.applicant = :applicant ");
            }
            if (rqp.getFinance() != null) {
                hql.append(" and re.finance = :finance ");
            }
            if (rqp.getOrders() != null && rqp.getOrders().size() > 0) {
                rqp.appendOrders(hql, "re");
            } else {
                hql.append(" order by t.createTime");// 默认以任务到达时间降序排序
            }
            rqp.addParameter("flowClass", SysConstants.FLOWCLASS_BX);
            rqp.addParameter("userId", userAccountId);
            rqp.addParameter("createState", TaskInstance.PROCESS_STATE_CREATE);
            rqp.addParameter("startState", TaskInstance.PROCESS_STATE_START);
            rqp.addParameter("type", TaskAssign.TYPE_USER);
            ListPage listPage = new CommQuery<Reimbursement>().queryListPage(rqp,
                    "select distinct re ,t " + hql.toString(), "select count(distinct re) " + hql.toString(), getSession());
            if (listPage != null && listPage.getDataList() != null) {
                List<Object[]> list = listPage.getDataList();
                List<Reimbursement> reis = new ArrayList<Reimbursement>();
                for (Object[] o : list) {
                    Reimbursement rei = (Reimbursement) o[0];
                    rei.setTask((Task) o[1]);
                    reis.add(rei);
                }
                listPage.setDataList(reis);
            }

            return listPage;
        } catch (RuntimeException re) {
            log.error("queryDealingReimbursement", re);
            throw re;
        }
    }

    public ListPage<Reimbursement> queryReimbursement(ReimbursementQueryParameters rqp, String userAccountId,
            boolean isArch) {
        log.debug("queryReimbursement with ReimbursementQueryParameters and userAccountId" + userAccountId);
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }
        try {
            StringBuffer hql = new StringBuffer("from Reimbursement as re, FlowConfig as fc, Task as t "
                    + "where fc.flowClass=:flowClass and fc.flowKey=t.flowKey and t.formID=re.id "
                    + "and (t.taskState=:endState and t.transactor=:userId or re.applicant=:userId)");
            rqp.addParameter("flowClass", SysConstants.FLOWCLASS_BX);
            rqp.addParameter("endState", TaskInstance.PROCESS_STATE_END);
            rqp.addParameter("userId", userAccountId);

            if (isArch) {
                hql.append(" and re.archiveDate is not null");
            } else {
                hql.append(" and re.archiveDate is null ");
            }

            if (rqp.getBeginArchiveDate() != null) {
                hql.append(" and re.archiveDate >= :beginArchiveDate ");
            }
            if (rqp.getEndArchiveDate() != null) {
                hql.append(" and re.archiveDate <= :endArchiveDate ");
            }
            if (rqp.getPassed() != null) {
                hql.append(" and re.passed = :passed ");
            }
            if (rqp.getID() != null) {
                hql.append(" and re.id like :id ");
                rqp.toArountParameter("id");
            }
            if (rqp.getBeginApplyDate() != null) {
                hql.append(" and re.applyDate >= :beginApplyDate ");
            }
            if (rqp.getEndApplyDate() != null) {
                hql.append(" and re.applyDate <= :endApplyDate ");
            }
            if (rqp.getApplicant() != null) {
                hql.append(" and re.applicant = :applicant ");
            }
            if (rqp.getFinance() != null) {
                hql.append(" and re.finance = :finance ");
            }
            if (rqp.getOrders() != null && rqp.getOrders().size() > 0) {
                rqp.appendOrders(hql, "re");
            } else {
                hql.append(" order by re.archiveDate desc");// 默认以任务到达时间降序排序
            }

            return new CommQuery<Reimbursement>().queryListPage(rqp, "select distinct re " + hql.toString(),
                    "select count(distinct re) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            log.error("queryReimbursement faild", re);
            return new ListPage<Reimbursement>();
        }
    }

    @Override
    public ListPage<Reimbursement> queryReimbursement(ReimbursementQueryParameters rqp) {
        log.debug("queryReimbursement with ReimbursementQueryParameters");
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }

        try {
            StringBuffer hql = new StringBuffer("select r from Reimbursement as r where 1=1 ");
            if (rqp.getID() != null) {
                hql.append(" and r.id like :id ");
                rqp.toArountParameter("id");
            }

            if (rqp.getBeginApplyDate() != null) {
                hql.append(" and r.applyDate >= :beginApplyDate ");
            }

            if (rqp.getEndApplyDate() != null) {
                hql.append(" and r.applyDate <= :endApplyDate ");
            }

            if (rqp.getBeginArchiveDate() != null) {
                hql.append(" and r.archiveDate >= :beginArchiveDate ");
            }
            if (rqp.getEndArchiveDate() != null) {
                hql.append(" and r.archiveDate <= :endArchiveDate ");
            }
//            if (rqp.getYear() != null) {
//                hql.append(" and r.budget.year = :year");
//            }

            if (rqp.getApplicant() != null) {
                hql.append(" and r.applicant = :applicant ");
            }
            if (rqp.getBudgetItem() != null) {
                hql.append(" and r.applyDept = :budgetItem ");
            }
            if (rqp.getPassed() != null) {
                hql.append(" and r.passed = :passed ");
            }
            if (rqp.getFinance() != null) {
                hql.append(" and r.finance = :finance ");
            }
            return new CommQuery<Reimbursement>().queryListPage(rqp, rqp.appendOrders(hql, "r"), getSession());

        } catch (RuntimeException re) {
            log.error("queryReimbursement faild", re);
            return new ListPage<Reimbursement>();
        }
    }

    @Override
    public ListPage<Reimbursement> queryReimbursement(ReimbursementQueryParameters rqp, List<String> budgetIds) {
        log.debug("queryReimbursement with ReimbursementQueryParameters and budgetIds");
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }

        try {
            StringBuffer hql = new StringBuffer("select r from Reimbursement as r where 1=1 ");
            if (rqp.getID() != null) {
                hql.append(" and r.id like :id ");
                rqp.toArountParameter("id");
            }

            if (rqp.getBeginApplyDate() != null) {
                hql.append(" and r.applyDate >= :beginApplyDate ");
            }

            if (rqp.getEndApplyDate() != null) {
                hql.append(" and r.applyDate <= :endApplyDate ");
            }

            if (rqp.getBeginArchiveDate() != null) {
                hql.append(" and r.archiveDate >= :beginArchiveDate ");
            }
            if (rqp.getEndArchiveDate() != null) {
                hql.append(" and r.archiveDate <= :endArchiveDate ");
            }

            if (budgetIds != null && budgetIds.size() > 0) {
                hql.append(" and r.budget.id in (:budgetId) ");
            }
//            if (rqp.getYear() != null) {
//                hql.append(" and r.budget.year=:year");
//            }

            if (rqp.getApplicant() != null) {
                hql.append(" and r.applicant = :applicant ");
            }
            if (rqp.getPassed() != null) {
                hql.append(" and r.passed = :passed ");
            }
            if (rqp.getFinance() != null) {
                hql.append(" and r.finance = :finance ");
            }
            if (budgetIds != null && budgetIds.size() > 0) {
                rqp.addParameter("budgetId", budgetIds);
            }
            return new CommQuery<Reimbursement>().queryListPage(rqp, rqp.appendOrders(hql, "r"), getSession());

        } catch (RuntimeException re) {
            log.error("queryReimbursement", re);
            return new ListPage<Reimbursement>();
        }
    }

    @Override
    public ListPage<Reimbursement> queryReimbursement(ReimbursementQueryParameters rqp, String financeId) {
        log.debug("queryReimbursement with ReimbursementQueryParameters and financeId: " + financeId);
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }

        try {
            StringBuffer hql = new StringBuffer("select r from Reimbursement as r where 1=1 ");
            if (rqp.getID() != null) {
                hql.append(" and r.id like :id ");
                rqp.toArountParameter("id");
            }

            if (rqp.getBeginApplyDate() != null) {
                hql.append(" and r.applyDate >= :beginApplyDate ");
            }

            if (rqp.getEndApplyDate() != null) {
                hql.append(" and r.applyDate <= :endApplyDate ");
            }

            if (financeId != null && !"".equals(financeId)) {
                hql.append(" and r.finance='" + financeId + "'");
            }

            if (rqp.getBeginArchiveDate() != null) {
                hql.append(" and r.archiveDate >= :beginArchiveDate ");
            }
            if (rqp.getEndArchiveDate() != null) {
                hql.append(" and r.archiveDate <= :endArchiveDate ");
            }

            if (rqp.getApplicant() != null) {
                hql.append(" and r.applicant = :applicant ");
            }
            if (rqp.getPassed() != null) {
                hql.append(" and r.passed = :passed ");
            }
            ListPage<Reimbursement> reimbursementListPage = null;
            reimbursementListPage = new CommQuery<Reimbursement>().queryListPage(rqp, rqp.appendOrders(hql, "r"), getSession());
            return reimbursementListPage;
        } catch (RuntimeException re) {
            log.error("queryReimbursement faild", re);
            return new ListPage<Reimbursement>();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Reimbursement> queryDealingReimbursement() {
        try {
            Query query = getSession().createQuery("from Reimbursement as re where re.archiveDate is null");
            return query.list();
        } catch (RuntimeException re) {
            log.error("queryDealingReimbursement", re);
            throw re;
        }
    }

    @Override
    public String getMaxReimbursementID() {
        try {
            Query query = getSession().createQuery("select max(rd.id) from Reimbursement rd where rd.id like :year");
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            query.setString("year", year + "%");
            return (String) query.uniqueResult();
        } catch (RuntimeException re) {
            log.error("getMaxReimbursementID", re);
            throw re;
        }
    }

    @Override
    public Double getOutlayListByArgs(String budgetID, String outlayCategory, Date startDate, Date endDate) {
        try {
            Query query = getSession().createQuery(
                    "select  sum(ol.outlaySum) from OutlayList ol " + "where ol.reimbursement.budget.id =:budgetID "
                            + "and ol.outlayCategory =:outlayCategory and ol.reimbursement.passed=true "
                            + "and ol.reimbursement.archiveDate >=:startDate "
                            + "and ol.reimbursement.archiveDate <:endDate");
            query.setString("budgetID", budgetID);
            query.setString("outlayCategory", outlayCategory);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            return (Double) query.uniqueResult();
        } catch (RuntimeException re) {
            log.error("getMaxReimbursementID", re);
            throw re;
        }
    }

    @Override
    public Double getOutlayListByArgsId(String budgetId, String outlayCategory, Date startDate, Date endDate) {
        try {
            Query query = getSession().createQuery(
                    "select  sum(ol.outlaySum) from OutlayList ol " + "where ol.reimbursement.budget.id =:budgetId "
                            + "and ol.outlayCategory =:outlayCategory and ol.reimbursement.passed=true "
                            + "and ol.reimbursement.archiveDate >=:startDate "
                            + "and ol.reimbursement.archiveDate <:endDate");
            query.setString("budgetId", budgetId);
            query.setString("outlayCategory", outlayCategory);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            return (Double) query.uniqueResult();
        } catch (RuntimeException re) {
            log.error("getOutlayListByArgsId", re);
            throw re;
        }
    }

    @Override
    public Double getOutlayListByArgsId(String budgetId, String outlayCategory, Date startDate, Date endDate,
            String financeId) {
        try {
            String queryString = "select  sum(ol.outlaySum) from OutlayList ol "
                    + "where ol.reimbursement.budget.id =:budgetId "
                    + "and ol.outlayCategory =:outlayCategory and ol.reimbursement.passed=true "
                    + "and ol.reimbursement.archiveDate >=:startDate " + "and ol.reimbursement.archiveDate <:endDate";
            if (financeId != null) {
                queryString += " and ol.reimbursement.finance=:finance";
            }
            Query query = getSession().createQuery(queryString);
            query.setString("budgetId", budgetId);
            query.setString("outlayCategory", outlayCategory);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (financeId != null) {
                query.setString("finance", financeId);
            }
            return (Double) query.uniqueResult();
        } catch (RuntimeException re) {
            log.error("getOutlayListByArgsId", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<OutlayList> queryNoPassedOutlayList(String budgetID, String category, String reimID) {
        try {
            String hql = "select ol from OutlayList ol where ol.reimbursement.budget.id =:budgetID "
                    + "and ol.outlayCategory =:outlayCategory and ol.reimbursement.passed is null";

            if (reimID != null) {
                hql += " and ol.reimbursement.id !=:reimID";
            }

            Query query = getSession().createQuery(hql);
            query.setString("budgetID", budgetID);
            query.setString("outlayCategory", category);
            if (reimID != null) {
                query.setString("reimID", reimID);
            }
            return query.list();
        } catch (RuntimeException re) {
            log.error("queryNoPassedOutlayList", re);
            throw re;
        }
    }

    @Override
    public Double getSumNoPassCategoryInQuarter(String budgetID, String category, String reimId,
            Date startDate, Date endDate) {
        try {
            StringBuffer hql = new StringBuffer();
            hql.append("select sum(ol.outlaySum) from OutlayList ol where ol.reimbursement.budget.id =:budgetID ");
            hql.append(" and ol.outlayCategory =:outlayCategory and ol.reimbursement.applyDate >= :startDate ");
            hql.append(" and ol.reimbursement.applyDate <= :endDate and ol.reimbursement.passed is null ");
            if (reimId != null) {
                hql.append(" and ol.reimbursement.id !=:reimId ");
            }
            
            Query query = getSession().createQuery(hql.toString());
            query.setString("budgetID", budgetID);
            query.setString("outlayCategory", category);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (reimId != null) {
                query.setString("reimId", reimId);
            }
            
            Double sumNoPassCategoryInQuarter = (Double) query.setMaxResults(1).uniqueResult();
            if (sumNoPassCategoryInQuarter == null) {
                sumNoPassCategoryInQuarter = 0.0;
            }
            return sumNoPassCategoryInQuarter;
        } catch (RuntimeException re) {
            log.error("getSumNoPassCategoryInQuarter", re);
            throw re;
        }
    }

    @Override
    public Double getSumPassedCategoryInQuarter(String budgetID, String category, String reimId, Date startDate,
            Date endDate) {
        try {
            StringBuffer hql = new StringBuffer();
            hql.append("select sum(ol.outlaySum) from OutlayList ol where ol.reimbursement.budget.id =:budgetID ");
            hql.append(" and ol.outlayCategory =:outlayCategory and ol.reimbursement.applyDate >= :startDate ");
            hql.append(" and ol.reimbursement.applyDate <= :endDate and ol.reimbursement.passed = 1 ");
            if (reimId != null) {
                hql.append(" and ol.reimbursement.id !=:reimId ");
            }
            
            Query query = getSession().createQuery(hql.toString());
            query.setString("budgetID", budgetID);
            query.setString("outlayCategory", category);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (reimId != null) {
                query.setString("reimId", reimId);
            }
            
            Double sumPassedCategoryInQuarter = (Double) query.setMaxResults(1).uniqueResult();
            if (sumPassedCategoryInQuarter == null) {
                sumPassedCategoryInQuarter = 0.0;
            }
            return sumPassedCategoryInQuarter;
        } catch (RuntimeException re) {
            log.error("getSumPassedCategoryInQuarter", re);
            throw re;
        }
    }
}