/**
 * 
 */
package org.eapp.poss.dao.imp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.dao.IProdFaqDAO;
import org.eapp.poss.dao.param.ProdFaqQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ProdFaq;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

/**
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-21	lhg		新建
 * </pre>
 */
public class ProdFaqDAO extends BaseHibernateDAO implements IProdFaqDAO {
    
    private static Log logger = LogFactory.getLog(ProdFaqDAO.class);

    /* (non-Javadoc)
     * @see org.eapp.poss.dao.IProdFaqDAO#findById(java.lang.String)
     */
    @Override
    public ProdFaq findById(String id) throws PossException {
        try {
            return (ProdFaq) getSession().get(ProdFaq.class, id);
        } catch (RuntimeException re) {
            logger.error("find ProdFaq instance by id failed: ", re);
            throw re;
        }
    }

    /* (non-Javadoc)
     * @see org.eapp.poss.dao.IProdFaqDAO#findProdFaqListPage(org.eapp.poss.dao.param.ProdFaqQueryParameters)
     */
    @Override
    public ListPage<ProdFaq> findProdFaqListPage(ProdFaqQueryParameters qp) throws PossException {
        StringBuffer hql = new StringBuffer();
        hql.append(" from ProdFaq as p where 1=1 ");
        if (qp.getCreateTimeBegin() != null) {
            hql.append(" and p.createTime >= :createTimeBegin ");
        }
        if (qp.getCreateTimeEnd() != null) {
            hql.append(" and p.createTime <= :createTimeEnd ");
        }
        if (StringUtils.isNotEmpty(qp.getProdInfoId())) {
            hql.append(" and p.prodInfo.id = :prodInfoId ");
        }
        if (StringUtils.isNotEmpty(qp.getCreator())) {
            hql.append(" and p.creator = :creator ");
        }
        if (qp.getHasAnswer() != null && qp.getHasAnswer()) {
            hql.append(" and p.childProdFaqs.size > 0 ");
        } else if (qp.getHasAnswer() != null && !qp.getHasAnswer()) {
            hql.append(" and p.childProdFaqs.size <= 0 ");
        }
        hql.append(" and p.parentProdFaq is null ");
        try {
            return new CommQuery<ProdFaq>().queryListPage(qp, "select distinct p " + qp.appendOrders(hql, "p"),
                    "select count(distinct p) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            logger.debug("query ProdFaq instance List by QueryParameters failed:", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> findProdFaqCreator() throws PossException {
        // hql 
        String hql = " select distinct p.creator from ProdFaq as p order by p.creator ";
        Query queryObject = getSession().createQuery(hql);
        return queryObject.list();
    }

}
