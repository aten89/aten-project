/**
 * 
 */
package org.eapp.poss.dao;

import java.util.List;

import org.eapp.poss.dao.param.CustPaymentQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.CustPayment;
import org.eapp.poss.rmi.hessian.PaymentRecord;
import org.eapp.poss.rmi.hessian.TransactionRecord;
import org.eapp.util.hibernate.ListPage;

/**
 * 
 * 客户划款DAO接口
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-20	黄云耿	新建
 * </pre>
 */
public interface ICustPaymentDAO extends IBaseHibernateDAO {
    
    /**
     * 查询客户划款列表
     * @param qp 查询参数
     * @return 客户划款列表
     * @throws PossException
     *
     * <pre>
     * 修改日期     修改人 修改原因
     * 2014-5-20    黄云耿 新建
     * </pre>
     */
    public ListPage<CustPayment> queryCustPaymentList(CustPaymentQueryParameters qp, List<String> userRoles) throws PossException;

    /**
     * 通过ID获取划款
     * @param id 划款ID
     * @return 划款
     * @throws PossException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-23	黄云耿	新建
     * </pre>
     */
    public CustPayment findById(String id) throws PossException;

    /**
     * 查询划款归档列表
     * @param iqp 查询参数
     * @return 划款归档列表
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	黄云耿	新建
     * </pre>
     */
    ListPage<CustPayment> queryArchiveCustPaymentList(CustPaymentQueryParameters iqp) throws PossException;
    
    /**
     * 查询还款记录列表（不关联TASK）
     * @param iqp
     * @return
     * @throws PossException
     */
    ListPage<CustPayment> queryCustPaymentListWithoutTask(CustPaymentQueryParameters iqp) throws PossException;

    /**
     * 查询划款跟踪列表
     * @param qp 查询参数
     * @return 款跟踪列表
     * 
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	黄云耿	新建
     * </pre>
     */
    public ListPage<CustPayment> queryTrackCustPaymentList(CustPaymentQueryParameters qp);

   
    public ListPage<TransactionRecord> queryTransactionRecordPage(CustPaymentQueryParameters qp);
    
    public ListPage<PaymentRecord> queryPaymentRecordPage(CustPaymentQueryParameters qp);
    
    public ListPage<CustPayment> queryAllCustPaymentList(CustPaymentQueryParameters iqp);
    
    /**
     * 检查客户是否还有划款
     * @param custId
     * @param excludeCustPaymentId 排除的划款单
     * @return
     */
    public boolean checkCustPayment(String custId, String excludeCustPaymentId);

}
