/**
 * 
 */
package org.eapp.poss.dao.imp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.dao.IProdIssuePlanDAO;
import org.eapp.poss.hbean.ProdIssuePlan;
import org.hibernate.Query;

/**
 */
public class ProdIssuePlanDAO extends BaseHibernateDAO implements IProdIssuePlanDAO {
    
    private static Log logger = LogFactory.getLog(ProdIssuePlanDAO.class);

    @Override
    public ProdIssuePlan findById(String id) {
        try {
            return (ProdIssuePlan) getSession().get(ProdIssuePlan.class, id);
        } catch (RuntimeException re) {
            logger.error("find ProdIssuePlan instance by id failed: ", re);
            throw re;
        }
    }
    
    @Override
    public ProdIssuePlan findByProdId(String prodId) {
    	Query query = getSession().createQuery("from ProdIssuePlan as pip where pip.prodInfo.id=:prodId");
		query.setString("prodId", prodId);
		query.setMaxResults(1);
		return (ProdIssuePlan)query.uniqueResult();
    }

	@Override
	public void deleteByProdId(String prodId) {
		Query query = getSession().createQuery("delete from ProdIssuePlan as pip where pip.prodInfo.id=:prodId");
		query.setString("prodId", prodId);
		query.executeUpdate();
	}
}
