package org.eapp.oa.reimburse.dao.impl;

// default package

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;
import org.hibernate.criterion.Example;


import org.eapp.oa.reimburse.dao.IOutlayListDAO;
import org.eapp.oa.reimburse.dto.OutlayListQueryParameters;
import org.eapp.oa.reimburse.hbean.OutlayList;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * A data access object (DAO) providing persistence and search support for OutlayList entities. Transaction control of
 * the save(), update() and delete() operations can directly support Spring container-managed transactions or they can
 * be augmented to handle user-managed Spring transactions. Each of these methods provides additional information for
 * how to configure it for the desired type of transaction control.
 * 
 * @see .OutlayList
 * @author MyEclipse Persistence Tools
 */

public class OutlayListDAO extends BaseHibernateDAO implements IOutlayListDAO {
    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(OutlayListDAO.class);
    // property constants
    /**
     * outlayCategory
     */
    public static final String OUTLAY_CATEGORY = "outlayCategory";
    /**
     * outlayName
     */
    public static final String OUTLAY_NAME = "outlayName";
    /**
     * 客户名称
     */
    public static final String CUSTOM_NAME = "customName";
    /**
     * 文档数量
     */
    public static final String DOCUMET_NUM = "documetNum";
    /**
     * outlaySum
     */
    public static final String OUTLAY_SUM = "outlaySum";
    /**
     * 描述
     */
    public static final String DESCRIPTION = "description";

    @SuppressWarnings("unchecked")
    public List<OutlayList> getOutlayListByReimItemID(String id) {
        try {
            String queryString = "from OutlayList as model where model.reimItem.id = :id and model.outlayCategory = '差旅费'";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setString("id", id);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<OutlayList> checkOutlayList(String accountId) {
        try {
            String queryString = "from OutlayList as model where model.reimbursement.applicant = :applicant and (model.reimbursement.passed is null or model.reimbursement.passed = 1) ";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setString("applicant", accountId);
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
    public OutlayList findById(java.lang.String id) {
        log.debug("getting OutlayList instance with id: " + id);
        try {
            OutlayList instance = (OutlayList) getSession().get(OutlayList.class, id);
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
    public List<OutlayList> findByExample(OutlayList instance) {
        log.debug("finding OutlayList instance by example");
        try {
            List<OutlayList> results = getSession().createCriteria("OutlayList").add(Example.create(instance)).list();
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
    public List<OutlayList> findByProperty(String propertyName, Object value) {
        log.debug("finding OutlayList instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from OutlayList as model where model." + propertyName + "= ?";
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
    public List<OutlayList> findByOutlayCategory(Object outlayCategory) {
        return findByProperty(OUTLAY_CATEGORY, outlayCategory);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<OutlayList> findByOutlayName(Object outlayName) {
        return findByProperty(OUTLAY_NAME, outlayName);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<OutlayList> findByCustomName(Object customName) {
        return findByProperty(CUSTOM_NAME, customName);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<OutlayList> findByDocumetNum(Object documetNum) {
        return findByProperty(DOCUMET_NUM, documetNum);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<OutlayList> findByOutlaySum(Object outlaySum) {
        return findByProperty(OUTLAY_SUM, outlaySum);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<OutlayList> findByDescription(Object description) {
        return findByProperty(DESCRIPTION, description);
    }

    /*
     * (non-Javadoc)
     * 
     */
    @SuppressWarnings("unchecked")
    public List<OutlayList> findAll() {
        log.debug("finding all OutlayList instances");
        try {
            String queryString = "from OutlayList";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    @Override
    public ListPage<OutlayList> queryOutlayListDAO(OutlayListQueryParameters oqp) {
        log.debug("queryOutlayListDAO with OutlayListQueryParameters");
        if (oqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }

        try {
            StringBuffer hql = new StringBuffer("select ol from OutlayList as ol where ol.reimbursement.passed=true ");

            if (oqp.getOutlayName() != null) {
                hql.append(" and ol.outlayName = :outlayName ");
            }

            if (oqp.getOutlayCategory() != null) {
                hql.append(" and ol.outlayCategory = :outlayCategory ");
            }

            if (oqp.getBeginApplyDate() != null) {
                hql.append(" and ol.reimbursement.applyDate >= :beginApplyDate ");
            }

            if (oqp.getEndApplyDate() != null) {
                hql.append(" and ol.reimbursement.applyDate < :endApplyDate ");
            }

            if (oqp.getBeginArchiveDate() != null) {
                hql.append(" and ol.reimbursement.archiveDate >= :beginArchiveDate ");
            }

            if (oqp.getEndArchiveDate() != null) {
                hql.append(" and ol.reimbursement.archiveDate < :endArchiveDate ");
            }

            if (oqp.getTravelBeginDate() != null) {
                hql.append(" and ol.reimItem.travelBeginDate = :travelBeginDate ");
            }

            if (oqp.getTravelEndDate() != null) {
                hql.append(" and ol.reimItem.travelEndDate = :travelEndDate ");
            }

            if (oqp.getBudgetItem() != null) {
                hql.append(" and ol.reimbursement.applyDept = :budgetItem ");
            }

            if (oqp.getRegionName() != null) {
                hql.append(" and ol.reimbursement.regionName = :regionName ");
            }

            if (oqp.getPayee() != null) {
                hql.append(" and ol.reimbursement.payee = :payee ");
            }
            if (oqp.getOption() != null) {
                if ("2".equals(oqp.getOption())) {
                    hql.append(" and ol.reimbursement.payee = :applicant ");
                } else if ("1".equals(oqp.getOption())) {
                    hql.append(" and ol.reimbursement.applicant = :applicant ");
                } else {
                    hql.append(" and (ol.reimbursement.applicant = :applicant or ol.reimbursement.payee = :applicant)");
                }
                oqp.removeParameter("searchOption");
            } else {
                if (oqp.getApplicant() != null) {
                    hql.append(" and ol.reimbursement.applicant = :applicant ");
                }
            }
            return new CommQuery<OutlayList>().queryListPage(oqp, oqp.appendOrders(hql, "ol"), getSession());

        } catch (RuntimeException re) {
            log.error("queryOutlayListDAO faild", re);
            return new ListPage<OutlayList>();
        }
    }

    @Override
    public ListPage<OutlayList> queryOutlayListDAO(OutlayListQueryParameters oqp, String financeId) {
        log.debug("queryOutlayListDAO with OutlayListQueryParameters and financeId: " + financeId);
        if (oqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }

        try {
            StringBuffer hql = new StringBuffer("select ol from OutlayList as ol where ol.reimbursement.passed=true ");

            if (oqp.getOutlayName() != null) {
                hql.append(" and ol.outlayName = :outlayName ");
            }
            if (oqp.getReimbursementId() != null) {
                hql.append(" and ol.reimbursement.id like :reimbursementId ");
                oqp.toArountParameter("reimbursementId");
            }
            if (oqp.getOutlayCategory() != null) {
                hql.append(" and ol.outlayCategory = :outlayCategory ");
            }

            if (oqp.getBeginApplyDate() != null) {
                hql.append(" and ol.reimbursement.applyDate >= :beginApplyDate ");
            }

            if (oqp.getEndApplyDate() != null) {
                hql.append(" and ol.reimbursement.applyDate < :endApplyDate ");
            }

            if (oqp.getBeginArchiveDate() != null) {
                hql.append(" and ol.reimbursement.archiveDate >= :beginArchiveDate ");
            }

            if (oqp.getEndArchiveDate() != null) {
                hql.append(" and ol.reimbursement.archiveDate < :endArchiveDate ");
            }

            if (oqp.getTravelBeginDate() != null) {
                hql.append(" and ol.reimItem.travelBeginDate = :travelBeginDate ");
            }

            if (oqp.getTravelEndDate() != null) {
                hql.append(" and ol.reimItem.travelEndDate = :travelEndDate ");
            }

            if (oqp.getBudgetId() != null) {
                hql.append(" and ol.reimbursement.budget.id = :budgetId ");
            }
            if (oqp.getYear() != null) {
                hql.append(" and ol.reimbursement.budget.year=:year");
            }

            if (oqp.getRegionName() != null) {
                hql.append(" and ol.reimbursement.regionName = :regionName ");
            }
            if (financeId != null && !"".equals(financeId)) {
                hql.append(" and ol.reimbursement.finance ='" + financeId + "'");
            }
            if (oqp.getPayee() != null) {
                hql.append(" and ol.reimbursement.payee = :payee ");
            }
            if (oqp.getOption() != null) {
                if ("2".equals(oqp.getOption())) {
                    hql.append(" and ol.reimbursement.payee = :applicant ");
                } else if ("1".equals(oqp.getOption())) {
                    hql.append(" and ol.reimbursement.applicant = :applicant ");
                } else {
                    hql.append(" and (ol.reimbursement.applicant = :applicant or ol.reimbursement.payee = :applicant)");
                }
                oqp.removeParameter("searchOption");
            } else {
                if (oqp.getApplicant() != null) {
                    hql.append(" and ol.reimbursement.applicant = :applicant ");
                }
            }
            hql.append(" order by ol.reimbursement.budget.name asc ");
            return new CommQuery<OutlayList>().queryListPage(oqp, oqp.appendOrders(hql, "ol"), getSession());

        } catch (RuntimeException re) {
            log.error("queryOutlayListDAO faild", re);
            return new ListPage<OutlayList>();
        }
    }

    @Override
    public double findStatOutlay(OutlayListQueryParameters oqp) {
        log.debug("findStatOutlay with OutlayListQueryParameters");
        if (oqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }
        try {
            StringBuffer hql = new StringBuffer();

            hql.append("select sum(ol.outlaySum) from OutlayList as ol where ol.reimbursement.passed=true ");

            if (oqp.getApplicant() != null) {
                hql.append(" and ol.reimbursement.applicant = :applicant ");
            }

            if (oqp.getOutlayName() != null) {
                hql.append(" and ol.outlayName = :outlayName ");
            }

            if (oqp.getOutlayCategory() != null) {
                hql.append(" and ol.outlayCategory = :outlayCategory ");
            }

            if (oqp.getBeginApplyDate() != null) {
                hql.append(" and ol.reimbursement.applyDate >= :beginApplyDate ");
            }

            if (oqp.getEndApplyDate() != null) {
                hql.append(" and ol.reimbursement.applyDate < :endApplyDate ");
            }
            if (oqp.getTravelBeginDate() != null) {
                hql.append(" and ol.reimItem.travelBeginDate = :travelBeginDate ");
            }

            if (oqp.getTravelEndDate() != null) {
                hql.append(" and ol.reimItem.travelEndDate = :travelEndDate ");
            }
            if (oqp.getBeginArchiveDate() != null) {
                hql.append(" and ol.reimbursement.archiveDate >= :beginArchiveDate ");
            }

            if (oqp.getEndArchiveDate() != null) {
                hql.append(" and ol.reimbursement.archiveDate < :endArchiveDate ");
            }

            if (oqp.getBudgetItem() != null) {
                hql.append(" and ol.reimbursement.budget.name = :budgetItem ");
            }

            if (oqp.getRegionName() != null) {
                hql.append(" and ol.reimbursement.regionName = :regionName ");
            }

            if (oqp.getPayee() != null) {
                hql.append(" and ol.reimbursement.payee = :payee ");
            }
            Query query = getSession().createQuery(hql.toString());
            query.setProperties(oqp.getParameters());
//            CommQuery.launchParamValues(query, oqp.getParameters());
            Double count = (Double) query.uniqueResult();
            if (count != null) {
                return count.doubleValue();
            }
        } catch (RuntimeException re) {
            log.error("findStatOutlay faild", re);
        }
        return 0;
    }

    @Override
    public double findStatOutlay(OutlayListQueryParameters oqp, String financeId) {
        log.debug("findStatOutlay with OutlayListQueryParameters and financeId: " + financeId);
        if (oqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }
        try {

            StringBuffer hql = new StringBuffer(
                    "select sum(ol.outlaySum) from OutlayList as ol where ol.reimbursement.passed=true ");

            if (oqp.getOutlayName() != null) {
                hql.append(" and ol.outlayName = :outlayName ");
            }
            if (oqp.getOutlayCategory() != null) {
                hql.append(" and ol.outlayCategory = :outlayCategory ");
            }

            if (oqp.getReimbursementId() != null) {
                hql.append(" and ol.reimbursement.id like :reimbursementId ");
                oqp.toArountParameter("reimbursementId");
            }

            if (oqp.getBeginApplyDate() != null) {
                hql.append(" and ol.reimbursement.applyDate >= :beginApplyDate ");
            }

            if (oqp.getEndApplyDate() != null) {
                hql.append(" and ol.reimbursement.applyDate < :endApplyDate ");
            }

            if (oqp.getBeginArchiveDate() != null) {
                hql.append(" and ol.reimbursement.archiveDate >= :beginArchiveDate ");
            }

            if (oqp.getEndArchiveDate() != null) {
                hql.append(" and ol.reimbursement.archiveDate < :endArchiveDate ");
            }

            if (oqp.getTravelBeginDate() != null) {
                hql.append(" and ol.reimItem.travelBeginDate = :travelBeginDate ");
            }

            if (oqp.getTravelEndDate() != null) {
                hql.append(" and ol.reimItem.travelEndDate = :travelEndDate ");
            }

            if (oqp.getBudgetId() != null) {
                hql.append(" and ol.reimbursement.budget.id = :budgetId ");
            }
            if (oqp.getYear() != null) {
                hql.append(" and ol.reimbursement.budget.year=:year");
            }

            if (oqp.getRegionName() != null) {
                hql.append(" and ol.reimbursement.regionName = :regionName ");
            }
            if (financeId != null && !"".equals(financeId)) {
                hql.append(" and ol.reimbursement.finance ='" + financeId + "'");
            }
            if (oqp.getPayee() != null) {
                hql.append(" and ol.reimbursement.payee = :payee ");
            }
            if (oqp.getOption() != null) {
                if ("2".equals(oqp.getOption())) {
                    hql.append(" and ol.reimbursement.payee = :applicant ");
                } else if ("1".equals(oqp.getOption())) {
                    hql.append(" and ol.reimbursement.applicant = :applicant ");
                } else {
                    hql.append(" and (ol.reimbursement.applicant = :applicant or ol.reimbursement.payee = :applicant)");
                }
                oqp.removeParameter("searchOption");
            } else {
                if (oqp.getApplicant() != null) {
                    hql.append(" and ol.reimbursement.applicant = :applicant ");
                }
            }
            Query query = getSession().createQuery(hql.toString());
            query.setProperties(oqp.getParameters());
//            CommQuery.launchParamValues(query, oqp.getParameters());
            Double count = (Double) query.uniqueResult();
            if (count != null) {
                return count.doubleValue();
            }
        } catch (RuntimeException re) {
            log.error("findStatOutlay faild", re);
        }
        return 0;
    }



    @SuppressWarnings("unchecked")
    public Map<String, Double> countReimNumOfProject(String custName, String projectCode) {
        try {
			String queryString = "select o.outlayName,sum(o.outlaySum) from OutlayList as o " +
					" where o.reimItem.csProjectKey =:projectCode and o.reimItem.customName =:custName" +
					" group by o.outlayName" ;
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("projectCode", projectCode);
            queryObject.setParameter("custName", custName);
            List<Object[]> listObject = queryObject.list();
            Map<String, Double> map = new HashMap<String, Double>();
            for (Object[] obj : listObject) {
                String category = (String) obj[0];
                Double num = (Double) obj[1];
                map.put(category, num);
            }
            return map;
        } catch (RuntimeException re) {
            log.error("countReimNumOfProject faild", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<OutlayList> findOutlayList(String name, String dictKey, String budgetID) {
        if (budgetID == null || name == null) {
            return null;
        }
        try {
            String queryString = "select ol from OutlayList as ol where ol.reimbursement.passed=true";
            // where m.budget.id=:budgetID
            if (name != null) {
                queryString += " and ol.outlayCategory=:outlayCategory";
            }
            if (budgetID != null) {
                queryString += " and ol.reimbursement.budget.id =:budgetId";
            }
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setString("budgetId", budgetID);
            if (name != null) {
                queryObject.setString("outlayCategory", name);
            }
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("findOutlayList faild", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<OutlayList> findOutlayListReim(String name, String budgetId) {
        if (budgetId == null || name == null) {
            return null;
        }
        try {
            String queryString = "select ol from OutlayList as ol where 1=1";
            if (name != null) {
                queryString += " and ol.outlayCategory=:outlayCategory";
            }
            if (budgetId != null) {
                queryString += " and ol.reimbursement.budget.id =:budgetId";
            }
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setString("budgetId", budgetId);
            if (name != null) {
                queryObject.setString("outlayCategory", name);
            }
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("findOutlayListReim faild", re);
            throw re;
        }
    }

}