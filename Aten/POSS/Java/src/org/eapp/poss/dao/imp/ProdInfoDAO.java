/**
 * 
 */
package org.eapp.poss.dao.imp;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.dao.IProdInfoDAO;
import org.eapp.poss.dao.param.ProdInfoQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;

/**
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-21	lhg		新建
 * </pre>
 */
public class ProdInfoDAO extends BaseHibernateDAO implements IProdInfoDAO {

    private static Log logger = LogFactory.getLog(ProdInfoDAO.class);
    
    /* (non-Javadoc)
     * @see org.eapp.poss.dao.IProdInfoDAO#findById(java.lang.String)
     */
    @Override
    public ProdInfo findById(String id) throws PossException {
        try {
            return (ProdInfo) getSession().get(ProdInfo.class, id);
        } catch (RuntimeException re) {
            logger.error("find ProdInfo instance by id failed: ", re);
            throw re;
        }
    }

    /* (non-Javadoc)
     * @see org.eapp.poss.dao.IProdInfoDAO#findProdInfoListPage(org.eapp.poss.dao.param.ProdInfoQueryParameters)
     */
    @Override
    public ListPage<ProdInfo> findProdInfoListPage(ProdInfoQueryParameters qp) throws PossException {
        StringBuffer hql = new StringBuffer();
        hql.append(" from ProdInfo as p where 1=1 ");
        if (qp.getSellDateBegin() != null) {
            hql.append(" and p.sellDate >= :sellDateBegin ");
        }
        if (qp.getSellDateEnd() != null) {
            hql.append(" and p.sellDate <= :sellDateEnd ");
        }
        if (StringUtils.isNotEmpty(qp.getSupplierId())) {
            hql.append(" and p.supplier.id = :supplierId ");
        }
        if (StringUtils.isNotEmpty(qp.getProdType())) {
            hql.append(" and p.prodType = :prodType ");
        }
        if (StringUtils.isNotEmpty(qp.getProdSecondaryClassify())) {
            hql.append(" and p.prodSecondaryClassify = :prodSecondaryClassify ");
        }
        if (StringUtils.isNotEmpty(qp.getProdStatus())) {
            hql.append(" and p.prodStatus = :prodStatus ");
        }
        if (StringUtils.isNotEmpty(qp.getExcProdStatus())) {
            hql.append(" and p.prodStatus <> :excProdStatus ");
        }
        if (StringUtils.isNotEmpty(qp.getProdCode())) {
            hql.append(" and p.prodCode = :prodCode ");
        }
        if (StringUtils.isNotEmpty(qp.getProdName())) {
            hql.append(" and p.prodName = :prodName ");
        }
        if (StringUtils.isNotEmpty(qp.getExcludeProdId())) {
            hql.append(" and p.id <> :excludeProdId ");
        }
        try {
            return new CommQuery<ProdInfo>().queryListPage(qp, qp.appendOrders(hql, "p"),
                    "select count(distinct p) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            logger.debug("query ProdInfo instance List by QueryParameters failed:", re);
            throw re;
        }
    }

}
