/**
 * 
 */
package org.eapp.poss.dao;

import org.eapp.poss.dao.param.RefuncNoticeQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.RefuncNotice;
import org.eapp.util.hibernate.ListPage;

/**
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-21	钟华杰		新建
 * </pre>
 */
public interface IRefuncNoticeDAO extends IBaseHibernateDAO {
    
    public RefuncNotice findById(String id) throws PossException;
    
    public ListPage<RefuncNotice> findRefuncNoticeListPage(RefuncNoticeQueryParameters qp) throws PossException;

}
