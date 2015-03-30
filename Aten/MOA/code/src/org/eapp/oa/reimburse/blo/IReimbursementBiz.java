/**
 * 
 */
package org.eapp.oa.reimburse.blo;

import java.util.List;

import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.reimburse.dto.ReimbursementQueryParameters;
import org.eapp.oa.reimburse.hbean.Reimbursement;
import org.eapp.oa.system.exception.OaException;
import org.eapp.util.hibernate.ListPage;



/**
 * @author zsy
 * @version Nov 25, 2008
 */
public interface IReimbursementBiz {

	/**
	 * 取得用户的报销单草稿
	 * @param userAccountId
	 * @return
	 */
	List<Reimbursement> getReimbursements(String userAccountId, int formStatus);
	
	/**
	 * 通过ID取得报销单草稿
	 * @param id
	 * @return
	 */
	Reimbursement getReimbursementById(String id);
	
	/**
	 * 新增报销单草稿
	 * @param draft
	 * @return
	 */
	Reimbursement txAddOrModifyReimbursement(Reimbursement draft, String user);
	
	
	/**
	 * 删除流程草稿
	 * @param id
	 * @return
	 */
	public Reimbursement txDeleteReimbursement(String id);
	
	public Reimbursement txStartFlow(Reimbursement draft, String user) throws OaException;
	
	/**
     * 通过报销单草稿ID启动报销流程，启动后删除报销单草稿
     */

	public void txModifyReim(String reimId, boolean arch) throws OaException;
	
	/**
	 * 取得用户待办的报销单
	 * @param userAccountId
	 * @return
	 */
	public ListPage<Reimbursement> getDealingReimbursement(ReimbursementQueryParameters rqp, 
			String userAccountId);
	
	/**
	 * 根据条件查找用户未归档的报销单
	 * @param rqp
	 * @param userAccountId
	 * @return
	 */
	public ListPage<Reimbursement> getTrackReimbursement(ReimbursementQueryParameters rqp, 
			String userAccountId);
	
	/**
	 * 根据条件查找用户已归档档的报销单
	 * @param rqp
	 * @param userAccountId
	 * @return
	 */
	public ListPage<Reimbursement> getArchReimbursement(ReimbursementQueryParameters rqp, 
			String userAccountId);
	
	/**
	 * 根据表单视图取得已结束的任务
	 * @param formId
	 * @return
	 */
	public List<Task> getEndedTasks(String formId);
	
	/**
	 * 处理审批报销任务
	 * @param taskId 任务ID
	 * @param comment 处理意见
	 * @param transitionName 转向名称
	 * @param reim 报销单对像
	 * @param isCancel 是否作废
	 */
	void txDealApproveTask(String taskInstanceId, String comment, String transitionName, 
			Reimbursement reim) throws OaException;
	
	ListPage<Reimbursement> queryReimbursement(ReimbursementQueryParameters rqp);
//	//ListPage<Reimbursement> queryReimbursement(ReimbursementQueryParameters rqp,String financeId);
//	ListPage<Reimbursement> queryReimbursement(ReimbursementQueryParameters rqp,String financeId);
//	ListPage<Reimbursement> queryReimbursement(String accountID,List<String> groupNames, List<String> postNames,ReimbursementQueryParameters rqp);

//	
//	/**
//	 * 根据预算项目和费用名称，取得在途的报销金额
//	 * @param budgetItem
//	 * @param category
//	 * @param reimID	报销单号，用于排除当前报销单，否则会重复计算
//	 * @return
//	 */
//	public Double getNoPassedReiSum(String budgetItem, String category, String reimID);
//	
}
