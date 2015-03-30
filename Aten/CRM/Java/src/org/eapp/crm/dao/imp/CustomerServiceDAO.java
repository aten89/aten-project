/**
 * 
 */
package org.eapp.crm.dao.imp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.crm.dao.ICustomerServiceDAO;
import org.eapp.crm.dao.param.CustomerInfoQueryParameters;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.CustomerInfo;
import org.eapp.crm.hbean.ReturnVist;
import org.eapp.crm.hbean.UserAccountExt;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;


/**
 * 客户信息DAO实现
 * 
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-4	黄云耿	新建
 * </pre>
 */
public class CustomerServiceDAO extends BaseHibernateDAO implements ICustomerServiceDAO {

    /**
     * 日志
     */
    private static Log logger = LogFactory.getLog(CustomerServiceDAO.class);

    @Override
    public ListPage<CustomerInfo> queryCustomerInfoList(CustomerInfoQueryParameters qp) throws CrmException {

        StringBuffer hql = new StringBuffer(" from CustomerInfo as c");
        hql.append(" where 1=1");
        if (StringUtils.isNotEmpty(qp.getSaleMan())) {
            hql.append(" and c.saleMan =:saleMan ");
        }
        if (StringUtils.isNotEmpty(qp.getStatus())) {
            hql.append(" and c.status =:status ");
        }
        if (qp.getMultipleStatus() != null) {
            hql.append(" and c.status in (:multipleStatus) ");
        }
        if (qp.getIds() != null && !qp.getIds().isEmpty()) {
            hql.append(" and c.id in (:ids) ");
        }
        if (qp.getSaleMans() != null && !qp.getSaleMans().isEmpty()) {
            hql.append(" and c.saleMan in (:saleMans) ");
        }
        if (StringUtils.isNotEmpty(qp.getRecommendProduct())) {
            hql.append(" and c.recommendProduct like :recommendProduct ");
            qp.toArountParameter("recommendProduct");
        }
        if (StringUtils.isNotEmpty(qp.getTel())) {
            hql.append(" and c.tel like :tel ");
            qp.toArountParameter("tel");
        }
        if (StringUtils.isNotEmpty(qp.getEmail())) {
            hql.append(" and c.email like :email ");
            qp.toArountParameter("email");
        }
        if (StringUtils.isNotEmpty(qp.getCustName())) {
            hql.append(" and c.custName like :custName ");
            qp.toArountParameter("custName");
        }
        if (qp.getBgnSubmitTime() != null) {
            hql.append(" and c.submitTime >= :bgnSubmitTime ");
        }
        if (qp.getEndSubmitTime() != null) {
            hql.append(" and c.submitTime < :endSubmitTime ");
        }
        if (qp.getBgnVistTime() != null || qp.getEndVistTime() != null) {
        	hql.append(" and c.id in (select v1.customerInfo.id from ReturnVist as v1 group by v1.customerInfo.id having 1=1");
        	 if (qp.getBgnVistTime() != null) {
        		 hql.append(" and max(v1.returnVistTime) >=:bgnVistTime ");
        	 }
        	 if (qp.getEndVistTime() != null) {
                 hql.append(" and max(v1.returnVistTime) < :endVistTime ");
             }
        	hql.append(")");
        }
        
        return new CommQuery<CustomerInfo>().queryListPage(qp, qp.appendOrders(hql, "c"),
                "select count(distinct c) " + hql.toString(), getSession());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<UserAccountExt> queryUserAccountExtList(String serviceAccountId) throws CrmException {
        logger.debug("find queryUserAccountExtList instance by serviceAccountId");
        StringBuffer hql = new StringBuffer(" from UserAccountExt as u where 1=1");
        
        if (serviceAccountId != null) {
            hql.append(" and u.serviceAccountId = :serviceAccountId ");
        }
        try {
            Query query = getSession().createQuery(hql.toString());
            if (serviceAccountId != null) {
                query.setParameter("serviceAccountId", serviceAccountId);
            }
            return (List<UserAccountExt>) query.list();
        } catch (RuntimeException re) {
            logger.error("find queryUserAccountExtList instance by serviceAccountId failed: ", re);
            throw re;
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ReturnVist> queryVisitRecordList(String custId, String returnVistUser) {
        logger.debug("queryVisitRecordList");
        StringBuffer queryString = new StringBuffer(" from ReturnVist as v where 1=1");
        
        if (StringUtils.isNotEmpty(custId)) {
            queryString.append(" and v.customerInfo.id = :custId");
        }
        if (StringUtils.isNotEmpty(returnVistUser)) {
            queryString.append(" and v.returnVistUser = :returnVistUser");
        }
        queryString.append(" order by v.returnVistTime desc");
        try {
            Query query = getSession().createQuery(queryString.toString());
            
            if (StringUtils.isNotEmpty(custId)) {
                query.setParameter("custId", custId);
            }
            if (StringUtils.isNotEmpty(returnVistUser)) {
                query.setParameter("returnVistUser", returnVistUser);
            }
            return query.list();
        } catch (RuntimeException re) {
            logger.error("queryVisitRecordList error", re);
            throw re;
        }
    }
  
    
}
