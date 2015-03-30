/**
 * 
 */
package org.eapp.poss.blo;

import java.util.List;

import org.eapp.poss.dao.param.ProdFaqQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ProdFaq;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.util.hibernate.ListPage;

/**
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-21	lhg		新建
 * </pre>
 */
public interface IProdFaqBiz {
    
    public ProdFaq queryProdFaqById(String id) throws PossException;
    
    public void txAddProdFaq(ProdFaq transProdFaq) throws PossException;
    
    public void txUpdateProdFaq(ProdFaq transProdFaq) throws PossException;
    
    public void txDelProdFaq(String id) throws PossException;
    
    public ListPage<ProdFaq> queryProdFaqListPage(ProdFaqQueryParameters qp) throws PossException;
    
    public List<UserAccountInfo> queryCreatorList() throws PossException;

}
