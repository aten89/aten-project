/**
 * 
 */
package org.eapp.poss.dao;

import org.eapp.poss.hbean.ProdIssuePlan;

/**
 */
public interface IProdIssuePlanDAO extends IBaseHibernateDAO {
    
    public ProdIssuePlan findById(String id);
    public ProdIssuePlan findByProdId(String prodId);
    public void deleteByProdId(String prodId);
}
