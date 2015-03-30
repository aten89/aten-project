/**
 * 
 */
package org.eapp.poss.dao.imp;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.dao.IRefuncNoticeDAO;
import org.eapp.poss.dao.param.RefuncNoticeQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.RefuncNotice;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;

/**
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-21	钟华杰		新建
 * </pre>
 */
public class RefuncNoticeDAO extends BaseHibernateDAO implements IRefuncNoticeDAO {

	private static Log logger = LogFactory.getLog(RefuncNoticeDAO.class);
	
    /* (non-Javadoc)
     * @see org.eapp.poss.dao.IRefuncNoticeDAO#findById(java.lang.String)
     */
    @Override
    public RefuncNotice findById(String id) throws PossException {
    	try {
            return (RefuncNotice) getSession().get(RefuncNotice.class, id);
        } catch (RuntimeException re) {
            logger.error("find RefuncNotice instance by id failed: ", re);
            throw re;
        }
    }

    /* (non-Javadoc)
     * @see org.eapp.poss.dao.IRefuncNoticeDAO#findRefuncNoticeListPage(org.eapp.poss.dao.param.RefuncNoticeQueryParameters)
     */
    @Override
    public ListPage<RefuncNotice> findRefuncNoticeListPage(RefuncNoticeQueryParameters qp) throws PossException {
    	StringBuffer hql = new StringBuffer();
        hql.append(" from RefuncNotice as r where 1=1 ");
        if (qp.getCreateTimeBegin() != null) {
            hql.append(" and r.createTime >= :createTimeBegin ");
        }
        if (qp.getCreateTimeEnd() != null) {
            hql.append(" and r.createTime <= :createTimeEnd ");
        }
        if (StringUtils.isNotEmpty(qp.getTrustCompany())) {
            hql.append(" and r.trustCompany = :trustCompany ");
        }
        try {
            return new CommQuery<RefuncNotice>().queryListPage(qp, "select distinct r " + qp.appendOrders(hql, "r"),
                    "select count(distinct r) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            logger.debug("query RefuncNotice instance List by QueryParameters failed:", re);
            throw re;
        }
    }

}
