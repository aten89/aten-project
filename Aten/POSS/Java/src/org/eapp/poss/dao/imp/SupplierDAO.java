/**
 * 
 */
package org.eapp.poss.dao.imp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.dao.ISupplierDAO;
import org.eapp.poss.dao.param.SupplierQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Supplier;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;

public class SupplierDAO extends BaseHibernateDAO implements ISupplierDAO {

    private static Log logger = LogFactory.getLog(SupplierDAO.class);
    
	/* (non-Javadoc)
	 * @see org.eapp.poss.dao.ISupplierDAO#findById(java.lang.String)
	 */
	@Override
	public Supplier findById(String id) throws PossException {
		try {
            return (Supplier) getSession().get(Supplier.class, id);
        } catch (RuntimeException re) {
            logger.error("find Supplier instance by id failed: ", re);
            throw re;
        }
	}

	/* (non-Javadoc)
	 * @see org.eapp.poss.dao.ISupplierDAO#querySupplierListPage(org.eapp.poss.dao.param.SupplierQueryParameters)
	 */
	@Override
	public ListPage<Supplier> querySupplierListPage(SupplierQueryParameters qp)
			throws PossException {
		StringBuffer hql = new StringBuffer();
        hql.append(" from Supplier as s where 1=1 ");
        try {
            return new CommQuery<Supplier>().queryListPage(qp, "select distinct s " + qp.appendOrders(hql, "s"),
                    "select count(distinct s) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            logger.debug("query querySupplierListPage failed:", re);
            throw re;
        }
	}
    
}
