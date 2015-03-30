/**
 * 
 */
package org.eapp.poss.dao;

import java.util.List;

import org.eapp.poss.dao.param.ProdFaqQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ProdFaq;
import org.eapp.util.hibernate.ListPage;

/**
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-21	lhg		新建
 * </pre>
 */
public interface IProdFaqDAO extends IBaseHibernateDAO {
    
    public ProdFaq findById(String id) throws PossException;
    
    public ListPage<ProdFaq> findProdFaqListPage(ProdFaqQueryParameters qp) throws PossException;
    
    public List<String> findProdFaqCreator() throws PossException;

}
