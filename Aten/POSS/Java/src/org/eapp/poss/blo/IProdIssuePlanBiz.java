/**
 * 
 */
package org.eapp.poss.blo;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ProdIssuePlan;

/**
 */
public interface IProdIssuePlanBiz {
    public ProdIssuePlan getProdIssuePlanById(String id) throws PossException;
    public ProdIssuePlan getProdIssuePlanByProdId(String id) throws PossException;
    public ProdIssuePlan addOrModifyProdIssuePlan(ProdIssuePlan transProdFaq) throws PossException;
}
