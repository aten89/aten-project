/**
 * 
 */
package org.eapp.poss.dao;

import org.eapp.poss.dao.param.ProdInfoQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.util.hibernate.ListPage;

/**
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-21	lhg		新建
 * </pre>
 */
public interface IProdInfoDAO extends IBaseHibernateDAO {
    
    public ProdInfo findById(String id) throws PossException;
    
    public ListPage<ProdInfo> findProdInfoListPage(ProdInfoQueryParameters qp) throws PossException;

}
