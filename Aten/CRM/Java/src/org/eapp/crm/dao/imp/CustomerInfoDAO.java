/**
 * 
 */
package org.eapp.crm.dao.imp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.crm.dao.ICustomerInfoDAO;
import org.eapp.crm.dao.param.CustomerInfoQueryParameters;
import org.eapp.crm.dto.AutoCompleteData;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.CustomerAppointment;
import org.eapp.crm.hbean.CustomerConsult;
import org.eapp.crm.hbean.CustomerInfo;
import org.eapp.crm.hbean.ReturnVist;
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
public class CustomerInfoDAO extends BaseHibernateDAO implements ICustomerInfoDAO {

    /**
     * 日志
     */
    private static Log logger = LogFactory.getLog(CustomerInfoDAO.class);

    @Override
    public CustomerInfo findById(String id) {
    	logger.debug("execute findById  with id: " + id);
    	CustomerInfo instance = (CustomerInfo) getSession().get(CustomerInfo.class, id);
        return instance;
    }
    
    @Override
    public CustomerConsult findByConsultId(String id) throws CrmException {
        logger.debug("execute findByConsultId  with id: " + id);
        CustomerConsult instance = (CustomerConsult) getSession().get(CustomerConsult.class, id);
        return instance;
    }
    
    @Override
    public ReturnVist findByReturnVistId(String id) throws CrmException {
        logger.debug("execute findByReturnVistId  with id: " + id);
        ReturnVist instance = (ReturnVist) getSession().get(ReturnVist.class, id);
        return instance;
    }
    
    @Override
    public ListPage<CustomerInfo> queryCustomerInfoList(CustomerInfoQueryParameters qp) {

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
        if (StringUtils.isNotEmpty(qp.getDataSource())) {
            hql.append(" and c.dataSource = :dataSource ");
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
        if (qp.getIds() != null && !qp.getIds().isEmpty()) {
            hql.append(" and c.id in (:ids) ");
        }
        
        if (qp.getBgnSubmitTime() != null) {
            hql.append(" and c.submitTime >= :bgnSubmitTime ");
        }
        if (qp.getEndSubmitTime() != null) {
            hql.append(" and c.submitTime < :endSubmitTime ");
        }
        
        return new CommQuery<CustomerInfo>().queryListPage(qp, qp.appendOrders(hql, "c"),
                "select count(distinct c) " + hql.toString(), getSession());
    }
    
    @Override
    public ListPage<CustomerConsult> queryConsultRecordList(CustomerInfoQueryParameters qp) throws CrmException {

        StringBuffer hql = new StringBuffer(" from CustomerConsult as cc");
        hql.append(" left join cc.customerInfo as c");
        hql.append(" where 1=1");
        if (StringUtils.isNotEmpty(qp.getId())) {
            hql.append(" and c.id =:id ");
        }
        hql.append(" order by cc.consultTime desc");
       
        return new CommQuery<CustomerConsult>().queryListPage(qp, "select distinct cc " + qp.appendOrders(hql, "cc"),
                "select count(distinct cc) " + hql.toString(), getSession());
    }
    
    @Override
    public ListPage<ReturnVist> queryVisitRecordList(CustomerInfoQueryParameters qp) throws CrmException {

        StringBuffer hql = new StringBuffer(" from ReturnVist as v");
        hql.append(" left join v.customerInfo as c");
        hql.append(" where 1=1");
        if (StringUtils.isNotEmpty(qp.getId())) {
            hql.append(" and c.id =:id ");
        }
        hql.append(" order by v.returnVistTime desc");
       
        return new CommQuery<ReturnVist>().queryListPage(qp, "select distinct v " + qp.appendOrders(hql, "v"),
                "select count(distinct v) " + hql.toString(), getSession());
    }
    
    @Override
    public ListPage<CustomerAppointment> queryAppointmentRecordList(
    		CustomerInfoQueryParameters qp) throws CrmException {
    	StringBuffer hql = new StringBuffer(" from CustomerAppointment as ca");
        hql.append(" left join ca.customerInfo as c");
        hql.append(" where 1=1");
        if (StringUtils.isNotEmpty(qp.getId())) {
            hql.append(" and c.id =:id ");
        }
        if (null != qp.getParameter("createor")) {
        	hql.append(" and ca.createor =:createor ");
        }
        hql.append(" order by ca.appointmentTime desc");
       
        return new CommQuery<CustomerAppointment>().queryListPage(qp, "select distinct ca " + qp.appendOrders(hql, "ca"),
                "select count(distinct ca) " + hql.toString(), getSession());
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<AutoCompleteData> queryAutoCompleteData(String saleMan, String[] status, int pageSize) {
		String sql = "select new org.eapp.crm.dto.AutoCompleteData(c.id, c.custName) from CustomerInfo as c where c.saleMan=:saleMan";
		if (status != null) {
			sql += " and c.status in (:status)";
		}
		sql += " order by c.custName";
		Query query = getSession().createQuery(sql);
		query.setString("saleMan", saleMan);
		if (status != null) {
			query.setParameterList("status", status);
		}
		query.setMaxResults(pageSize);
		return query.list();
	}

	@Override
	public boolean checkTel(String tel) {
		Query query = getSession().createQuery("select count(c) from CustomerInfo c where c.tel=:tel");
		query.setString("tel", tel);
		Number num = (Number)query.uniqueResult();
		if (num != null && num.intValue() > 0) {
			return false;
		}
		return true;
	}
}
