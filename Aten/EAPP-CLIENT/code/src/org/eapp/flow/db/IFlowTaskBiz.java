package org.eapp.flow.db;

import java.util.List;
import java.util.Map;

import org.eapp.flow.db.FlowTask;

public interface IFlowTaskBiz {
	/**
	 * 办理任务
	 * @param taskInstanceId
	 * @param comment
	 * @param transitionName
	 */
	public void dealTask(String taskInstanceId, String comment, String transitionName);
	
	/**
	 * 办理任务
	 * @param taskInstanceId
	 * @param comment
	 * @param transitionName
	 * @param contextVariables
	 */
	public void dealTask(String taskInstanceId, String comment, String transitionName, Map<String, Object> contextVariables);
	
	/**
	 * 任务重新授权
	 * @param taskInstanceId
	 * @param assignVal多个用逗号分开
	 */
	public void reAssignTask(String taskInstanceId, String assign);
	
	/**
	 * 强制结束整个流程
	 * @param taskInstanceId
	 */
	public void forceEndFlow(String taskInstanceId);
	
	/**
	 * 查询任务到达下一步的转向
	 * @param taskInstanceId
	 * @return
	 */
	public List<String> getTaskLeavingTransitions(String taskInstanceId);
	
	/**
	 * 开始任务
	 * @param taskInstanceId 任务实例ID
	 * @param userAccountId 用户账号
	 * @param userRoles 用户角色
	 * @return 任务视图
	 */
	public String txStartTask(String taskInstanceId, String userAccountId, List<String> userRoles);
	
	/**
	 * 新增任务
	 * @param task
	 */
	public void addTask(FlowTask task);

	/**
	 * 修改任务
	 * @param task
	 */
	public void modifyTask(FlowTask task);
	
	/**
	 * 通过任务实例ID查找
	 * @param taskInstanceId 任务实例ID
	 * @return
	 */
	public FlowTask getByTaskInstanceId(String taskInstanceId);
	
}
