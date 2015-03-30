package org.eapp.oa.flow.dao;

import java.util.List;

import org.eapp.oa.flow.dto.TaskQueryParameters;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.system.dao.IBaseHibernateDAO;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;


public interface ITaskDAO extends IBaseHibernateDAO {

	public Task findById(String id);

	/**
	 * 取得所有待办任务
	 * @param userAccountId
	 * @return
	 */
	public ListPage<Task> queryDealingTask(QueryParameters parms, String userAccountId);
	
	/**
	 * 结束指定的任务
	 */
	
	public ListPage<Task> queryUnFinishTask(TaskQueryParameters parms);
	
	
	/**
	 * 根据实例Id查找待办任务
	 * @param taskInstanceID
	 * @return
	 */
	public List<Task> findByTaskInstanceID(String taskInstanceID);
	
	/**
	 * 根据表单视图取得已结束的任务
	 * @param formId
	 * @return
	 */
	public List<Task> findEndedTasks(String formId);
	
	/**
	 * 强制结束TASK表中的任务
	 * @param flowInstanceID
	 * @param comment
	 */
	public void endTasksByFlowInstanceID(String flowInstanceID, String comment);
	
	/**
	 * 根据流程实例ID取得处理中的任务
	 * @param flowInstanceId
	 * @return
	 */
	public List<Task> findDealingTaskList(String flowInstanceId);
}