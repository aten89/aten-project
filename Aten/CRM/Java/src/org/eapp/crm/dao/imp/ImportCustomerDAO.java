/**
 * 
 */
package org.eapp.crm.dao.imp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.crm.dao.IImportCustomerDAO;
import org.eapp.crm.dao.param.ImportCustomerQueryParameters;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.ImportCustomer;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

/**
 * 导入客户DAO实现层
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-4-27	lhg		新建
 * </pre>
 */
public class ImportCustomerDAO extends BaseHibernateDAO implements IImportCustomerDAO {

    /**
     * 日志
     */
    private static Log logger = LogFactory.getLog(ImportCustomerDAO.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.eapp.crm.dao.IImportCustomerDAO#findById(java.lang.String)
     */
    @Override
    public ImportCustomer findById(String id) throws CrmException {
        logger.debug("find ImportCustomer instance by id :" + id);
        try {
            return (ImportCustomer) getSession().get(ImportCustomer.class, id);
        } catch (RuntimeException re) {
            logger.error("find ImportCustomer instance by id failed: ", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eapp.crm.dao.IImportCustomerDAO#findByIds(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ImportCustomer> findByIds(List<String> ids, Boolean allocateFlag) throws CrmException {
        logger.debug("find ImportCustomer instance by ids");
        StringBuffer queryStr = new StringBuffer();
        queryStr.append(" from ImportCustomer as ic where 1=1 ");
        if (ids != null && !ids.isEmpty()) {
            queryStr.append(" and ic.id in (:ids) ");
        }
        if (allocateFlag != null) {
            queryStr.append(" and ic.allocateFlag = :allocateFlag ");
        }
        try {
            Query query = getSession().createQuery(queryStr.toString());
            if (ids != null && !ids.isEmpty()) {
                query.setParameterList("ids", ids);
            }
            if (allocateFlag != null) {
                query.setParameter("allocateFlag", allocateFlag);
            }
            return (List<ImportCustomer>) query.list();
        } catch (RuntimeException re) {
            logger.error("find ImportCustomer instance by ids failed: ", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eapp.crm.dao.IImportCustomerDAO#queryImportCustomers(org.eapp.crm.dao.param.ImportCustomerQueryParameters)
     */
    @Override
    public ListPage<ImportCustomer> queryImportCustomers(ImportCustomerQueryParameters qp) throws CrmException {
        StringBuffer hql = new StringBuffer(" from ImportCustomer as ic");
        hql.append(" where 1=1");
        if(null != qp.getAllocateFlag()) {
        	hql.append(" and ic.allocateFlag =:allocateFlag");
        }
        if (StringUtils.isNotEmpty(qp.getBatchNumber())) {
            hql.append(" and ic.batchNumber like :batchNumber ");
            qp.toArountParameter("batchNumber");
        }
        if (StringUtils.isNotEmpty(qp.getCustomerName())) {
            hql.append(" and ic.custName like :customerName ");
            qp.toArountParameter("customerName");
        }
        if (StringUtils.isNotEmpty(qp.getTel())) {
            hql.append(" and ic.tel like :tel ");
            qp.toArountParameter("tel");
        }
        if (StringUtils.isNotEmpty(qp.getEmail())) {
            hql.append(" and ic.email like :email ");
            qp.toArountParameter("email");
        }
        if (null != qp.getAllocateTimeBegin()) {
        	hql.append(" and ic.allocateTime >= :allocateTimeBegin ");
        }
        if (null != qp.getAllocateTimeEnd()) {
        	hql.append(" and ic.allocateTime <= :allocateTimeEnd ");
        }
        
        return new CommQuery<ImportCustomer>().queryListPage(qp, qp.appendOrders(hql, "ic"),
              "select count(distinct ic) " + hql.toString(), getSession());
    }

}
