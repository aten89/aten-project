/**
 * 
 */
package org.eapp.poss.blo;

import java.io.File;
import java.util.List;

import org.eapp.comobj.SessionAccount;
import org.eapp.poss.dao.param.CustPaymentQueryParameters;
import org.eapp.poss.dto.PaymentDisposalProcDTO;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Attachment;
import org.eapp.poss.hbean.CustPayment;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.poss.rmi.hessian.PaymentRecord;
import org.eapp.poss.rmi.hessian.TransactionRecord;
import org.eapp.util.hibernate.ListPage;

/**
 * 客服管理业务逻辑层接口
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-12	黄云耿	新建
 * </pre>
 */
public interface ICustPaymentBiz {

    /**
     * 查询客户划款列表
     * @param qp 查询参数
     * @return 客户划款列表
     * @throws PossException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-20	黄云耿	新建
     * </pre>
     */
    public ListPage<CustPayment> queryCustPaymentList(CustPaymentQueryParameters qp, List<String> userRoles) throws PossException;

    /**
     * 新增客户划款
     * @param paymentJson 客户划款JSON
     * @param creator 创建者
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-22	黄云耿	新建
     * </pre>
     */
    public void addCustPayment(String paymentJson, SessionAccount user) throws PossException;

    /**
     * 通过ID获取划款
     * @param id 划款ID
     * @return 划款
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-23	黄云耿	新建
     * </pre>
     * @throws PossException 
     * @throws Exception 
     */
    public CustPayment getCustPaymentById(String id, String flag) throws PossException, Exception;

    /**
     * 确认到款审批
     * @param id 划款ID
     * @param tiid 任务实例ID
     * @param transition 流程步骤名称
     * @param taskCommentId 任务审批意见ID
     * @param comment 审批意见
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	黄云耿	新建
     * </pre>
     */
    public void txApprovePayment(CustPayment custPayment, String tiid, String transition, String taskCommentId, String comment) throws PossException;

    /**
     * 划款处理过程
     * @param id 划款ID
     * @return 划款处理过程
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	黄云耿	新建
     * </pre>
     */
    public List<PaymentDisposalProcDTO> csGetDisposalProc(String id) throws PossException;

    /**
     * 根据ID归档划款流程
     * @param formId 划款ID
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	黄云耿	新建
     * </pre>
     * @throws CrmException 
     */
    public void txArchiveCustPayment(String formId) throws PossException;

    /**
     * 根据ID作废划款流程
     * @param formId 划款ID
     * @throws PossException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	黄云耿	新建
     * </pre>
     * @throws CrmException 
     */
    public void txInvalidCustPayment(String formId) throws PossException;

    /**
     * 查询划款归档列表
     * @param qp 查询参数
     * @return 划款归档列表
     * @throws PossException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	黄云耿	新建
     * </pre>
     */
    ListPage<CustPayment> queryArchiveCustPaymentList(CustPaymentQueryParameters qp) throws PossException;
    
    /**
     * 查询归档列表，不包含task
     * @param qp
     * @return
     * @throws PossException
     */
    ListPage<CustPayment> queryArchiveCustPaymentListWithoutTask(CustPaymentQueryParameters qp) throws PossException;
    
    /**
     * 查询出对应客户的已经划过款的产品
     * @param qp
     * @return
     * @throws PossException
     */
    List<ProdInfo> queryProdInfoList(CustPaymentQueryParameters qp) throws PossException;

    /**
     * 查询跟踪列表
     * @param qp 查询参数
     * @return 划款跟踪列表
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	黄云耿	新建
     * </pre>
     */
    public ListPage<CustPayment> queryTrackCustPaymentList(CustPaymentQueryParameters qp) throws PossException;

    /**
     * 处理流程任务
     * @param id
     * @param tiid
     * @param taskCommentId
     * @param transition
     * @param comment
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-25	黄云耿	新建
     * </pre>
     * @param flag 
     */
    public void txDealTask(String id, String tiid, String taskCommentId, String transition, String comment, String flag) throws PossException;

    /**
     * 重新提交
     * @param custPayment
     * @param tiid
     * @param taskCommentId
     * @param transition
     * @param comment
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-26	黄云耿	新建
     * </pre>
     */
    public void modifyPaymentAndEndTransition(CustPayment custPayment, String tiid, String taskCommentId,
            String transition, String comment) throws PossException;

    /**
     * 审批划款
     * @param tiid
     * @param transition
     * @param taskCommentId
     * @param comment
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-27	黄云耿	新建
     * </pre>
     * @throws CrmException 
     */
    public void txApprovePaymentLeader(String id, String tiid, String transition, String taskCommentId, String comment) throws PossException;

    public void txModifyDealTask(CustPayment custPayment, String tiid, String taskCommentId, String transition, String comment) throws PossException;

    public Attachment saveAttachmentToTempDir(File attachment, String extName, String displayName) throws PossException;

    public void txApproveRejectEvent(String formId) throws PossException;
    
    public ListPage<TransactionRecord> queryTransactionRecordPage(CustPaymentQueryParameters qp);
    
    public ListPage<PaymentRecord> queryPaymentRecordPage(CustPaymentQueryParameters qp);

    public ListPage<CustPayment> queryAllCustPaymentList(CustPaymentQueryParameters iqp) throws PossException;
}
