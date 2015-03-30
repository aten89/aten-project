/**
 * 
 */
package org.eapp.oa.flow.blo;

import java.util.List;
import java.util.Map;

import org.eapp.oa.flow.dto.TaskQueryParameters;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.system.exception.OaException;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;


/**
 * @author zsy
 * @version Nov 28, 2008
 */
public interface ITaskBiz {
	
	/**
	 * 创建任务
	 * @param userId
	 * @param productId
	 * @param remark
	 * @throws OaException
	 */
	void addTask(Task task);

	/**
	 * 修改任务
	 * @param task
	 */
	void modifyTask(Task task);
	
	/**
	 * 通过任务实例ID查找
	 * @param taskInstanceId
	 * @return
	 */
	Task getByTaskInstanceId(String taskInstanceId);

	/**
	 * 取得任务的转向名称
	 * @param taskId
	 * @return
	 */
	List<String> getTaskTransitions(String taskInstanceId);
	
	/**
	 * 取得所有待办任务
	 * @param userAccountId
	 * @return
	 */
	ListPage<Task> getDealingTask(QueryParameters parms, String userAccountId);
	
	/**
	 * 取得任务实例
	 * 如果任务是创建状态则认领
	 * @param taskId
	 * @param userAccountId
	 * @return
	 */
	String txStartTask(String taskId, String userAccountId, List<String> userRoles) throws OaException;
	
	/**
	 * 处理审批任务
	 * @param taskId
	 * @param comment
	 * @param transitionName
	 */
	void txDealApproveTask(String taskInstanceId, String comment, String transitionName);
	
	/**
	 * 处理审批任务
	 * @param taskId
	 * @param comment
	 * @param transitionName
	 */
	void txDealApproveTask(String taskInstanceId, String comment, String transitionName, Map<String, String> contextVariables);
	
	/**
	 * 查询所有未完成的任务
	 * @param parms
	 * @return
	 */
	ListPage<Task> getUnFinishTask(TaskQueryParameters parms);
	
	/**
	 * 待办任务发送通知邮件
	 * @param userId 用户ID
	 * @param taskDesc 任务描述
	 */
	void csSendTaskNotice(Task task, String taskDesc);
	
	/**
	 * 流程实例管理：任务授权给单用户
	 * @param taskInstanceId
	 * @param userAccount
	 */
	void txTaskAssign(String taskInstanceId, String userAccount);
	
	/**
	 * 根据流程实例ID取得已结束的任务
	 * @param flowInstanceID 流程实例ID
	 * @return
	 */
	List<Task> getEndedTasks(String flowInstanceID);
}
