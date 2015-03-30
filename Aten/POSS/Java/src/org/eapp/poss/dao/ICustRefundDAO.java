/**
 * 
 */
package org.eapp.poss.dao;

import java.util.List;

import org.eapp.poss.dao.param.CustRefundQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.CustRefund;
import org.eapp.util.hibernate.ListPage;

/**
 * 
 * 退款表单DAO接口
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-20	钟华杰	新建
 * </pre>
 */
public interface ICustRefundDAO extends IBaseHibernateDAO {
    
	
	/**
	 * 查询草稿列表
	 * @param qp
	 * @return
	 * @throws PossException
	 */
	public ListPage<CustRefund> queryCustRefundDraftList(CustRefundQueryParameters qp) throws PossException;
	
    /**
     * 查询退款表单列表
     * @param qp 查询参数
     * @return 退款表单列表
     * @throws PossException
     *
     * <pre>
     * 修改日期     	修改人 	修改原因
     * 2014-5-20        钟华杰 	新建
     * </pre>
     */
    public ListPage<CustRefund> queryCustRefundList(CustRefundQueryParameters qp, List<String> userRoles) throws PossException;

    /**
     * 通过ID获取划款
     * @param id 划款ID
     * @return 划款
     * @throws PossException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-23	钟华杰	新建
     * </pre>
     */
    public CustRefund findById(String id) throws PossException;

    /**
     * 查询划款归档列表
     * @param iqp 查询参数
     * @return 划款归档列表
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	钟华杰	新建
     * </pre>
     */
    ListPage<CustRefund> queryArchiveCustRefundList(CustRefundQueryParameters iqp) throws PossException;

    /**
     * 查询划款跟踪列表
     * @param qp 查询参数
     * @return 款跟踪列表
     * 
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	钟华杰	新建
     * </pre>
     */
    public ListPage<CustRefund> queryTrackCustRefundList(CustRefundQueryParameters qp);

   

}
