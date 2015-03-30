/**
 * 
 */
package org.eapp.poss.blo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eapp.comobj.SessionAccount;
import org.eapp.poss.dao.param.CustRefundQueryParameters;
import org.eapp.poss.dto.CustRefundDisposalProcDTO;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Attachment;
import org.eapp.poss.hbean.CustRefund;
import org.eapp.util.hibernate.ListPage;

/**
 * 退款表单业务逻辑层接口
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-12	钟华杰	新建
 * </pre>
 */
public interface ICustRefundBiz {

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
     * 修改日期		修改人	修改原因
     * 2014-5-20	钟华杰	新建
     * </pre>
     */
    public ListPage<CustRefund> queryCustRefundList(CustRefundQueryParameters qp, List<String> userRoles) throws PossException;

    /**
     * 新增退款表单
     * @param custRefundJson 退款表单JSON
     * @param creator 创建者
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-22	钟华杰	新建
     * </pre>
     */
    public CustRefund addCustRefund(String custRefundJson, SessionAccount user, Boolean flowFlag) throws PossException;
    
    /**
     * 修改退款表单
     * @param custRefundJson
     * @param user
     * @param flowFlag
     * @throws PossException
     */
    public CustRefund modifyCustRefund(String custRefundJson, SessionAccount user, Boolean flowFlag) throws PossException;

    /**
     * 通过ID获取划款
     * @param id 划款ID
     * @return 划款
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-23	钟华杰	新建
     * </pre>
     * @throws PossException 
     */
    public CustRefund getCustRefundById(String id) throws PossException;

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
     * 2014-5-24	钟华杰	新建
     * </pre>
     */
    public void txRejectCustRefund(String id, String tiid, String transition, String taskCommentId, String comment) throws PossException;
    
    /**
     * 审批通过
     * @param id
     * @param tiid
     * @param transition
     * @param taskCommentId
     * @param comment
     * @throws PossException
     */
    public void txApproveCustRefund(String id, String tiid, String transition, String taskCommentId, String comment) throws PossException;
    
    /**
     * 获取流程变量MAP
     * @param tiid
     * @return
     * @throws PossException
     */
    public Map<String, String> queryFlowVarialbleMap(String tiid) throws PossException;
    
    /**
     * 重新提交
     * @param custPayment
     * @param tiid
     * @param taskCommentId
     * @param transition
     * @param comment
     * @throws PossException
     */
    public void modifyCustRefundAndEndTransition(CustRefund custRefund, String tiid, String taskCommentId,
            String transition, String comment) throws PossException;
    
    /**
     * 重新提交（最新）
     * @param custRefund
     * @param tiid
     * @param taskCommentId
     * @param transition
     * @param comment
     * @throws PossException
     */
    public CustRefund txAgainCommitCustRefund(CustRefund custRefund, String tiid, String taskCommentId,
    		String transition, String comment) throws PossException;

    /**
     * 划款处理过程
     * @param id 划款ID
     * @return 划款处理过程
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	钟华杰	新建
     * </pre>
     */
    public List<CustRefundDisposalProcDTO> csGetDisposalProc(String id) throws PossException;

    /**
     * 根据ID归档划款流程
     * @param formId 划款ID
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	钟华杰	新建
     * </pre>
     */
    public void txArchiveCustRefund(String formId) throws PossException;

    /**
     * 根据ID作废划款流程
     * @param formId 划款ID
     * @throws PossException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	钟华杰	新建
     * </pre>
     */
    public void txInvalidCustRefund(String formId) throws PossException;

    /**
     * 查询划款归档列表
     * @param qp 查询参数
     * @return 划款归档列表
     * @throws PossException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	钟华杰	新建
     * </pre>
     */
    ListPage<CustRefund> queryArchiveCustRefundList(CustRefundQueryParameters qp) throws PossException;

    /**
     * 查询跟踪列表
     * @param qp 查询参数
     * @return 划款跟踪列表
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	钟华杰	新建
     * </pre>
     */
    public ListPage<CustRefund> queryTrackCustRefundList(CustRefundQueryParameters qp) throws PossException;

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
     * 2014-5-25	钟华杰	新建
     * </pre>
     */
    public void txDealTask(String id, String tiid, String taskCommentId, String transition, String comment) throws PossException;
    /**
     * 上传退款申请附件
     * @param id
     * @param deletedFiles
     * @param refuncNoticeAttachments
     * @throws PossException
     */
    public void txBatchUploadCustRefundAttachment(final String id, String[] deletedFiles, final Collection<Attachment> custRefundAttachments) throws PossException;
    
}
