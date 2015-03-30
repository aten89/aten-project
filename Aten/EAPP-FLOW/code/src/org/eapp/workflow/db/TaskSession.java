/**
 * 
 */
package org.eapp.workflow.db;

import java.util.Date;
import java.util.List;

import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Query;


/**
 * 任务相关的持久化操作
 * @author 卓诗垚
 * @version 1.0
 */
public class TaskSession {
	protected HibernateSessionFactory dbService;
	
	public TaskSession(HibernateSessionFactory dbService) {
		this.dbService = dbService;
	}
	
	/**
	 * 根据时间段查询所有待办任务
	 * @param startCreateTime 查询开始时间（必需）
	 * @param endCreateTime 查询结束时间（必需）
	 * @param taskName 任务名称（可选）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TaskInstance> getOpeningTasks(Date startCreateTime, Date endCreateTime, String taskName) {
		if (startCreateTime == null || endCreateTime == null) {
			throw new IllegalArgumentException("开始与结束时间不能为空");
		}
		Query query = null;
		if (taskName != null) {
			query = dbService.getSession(true).getNamedQuery("TaskSession.getOpeningTasks-byName");
			query.setString("taskName", "%" + taskName + "%");
		} else {
			query = dbService.getSession(true).getNamedQuery("TaskSession.getOpeningTasks");
		}
		query.setDate("startCreateTime", startCreateTime);
		query.setDate("endCreateTime", endCreateTime);
		return query.list();
	}
	
	/**
	 * 通过用户ID取得必办任务列表
	 * @param actorId
	 * @return 任务实例列表
	 */
	@SuppressWarnings("unchecked")
	public List<TaskInstance> getCompulsoryTasks(String actorId) {
		if (actorId == null) {
			return null;
		}
		Query query = dbService.getSession(true).getNamedQuery("TaskSession.getCompulsoryTasks-ById");
		query.setString("actorId", actorId);
		return query.list();
	}
	
	/**
	 * 通过用户ID集合取得必办任务列表
	 * @param actorIds
	 * @return 任务实例列表
	 */
	@SuppressWarnings("unchecked")
	public List<TaskInstance> getCompulsoryTasks(List<String> actorIds) {
		if (actorIds == null || actorIds.size() == 0) {
			return null;
		}
		Query query = dbService.getSession(true).getNamedQuery("TaskSession.getCompulsoryTasks-ByIds");
		query.setParameterList("actorIds", actorIds);
		return query.list();
	}
	
	/**
	 * 通过用户ID取得待办任务列表
	 * @param actorId
	 * @return 任务实例列表
	 */
	@SuppressWarnings("unchecked")
	public List<TaskInstance> getActorTasks(String actorId) {
		if (actorId == null) {
			return null;
		}
		Query query = dbService.getSession(true).getNamedQuery("TaskSession.getActorTasks-ById");
		query.setString("actorId", actorId);
		return query.list();
	}
	
	/**
	 * 通过用户ID集合取得待办任务列表
	 * @param actorIds
	 * @return 任务实例列表
	 */
	@SuppressWarnings("unchecked")
	public List<TaskInstance> getActorTasks(List<String> actorIds) {
		if (actorIds == null || actorIds.size() == 0) {
			return null;
		}
		Query query = dbService.getSession(true).getNamedQuery("TaskSession.getActorTasks-ByIds");
		query.setParameterList("actorIds", actorIds);
		return query.list();
	}
	
	/**
	 * 通过角色ID取得待办任务列表
	 * @param roleId
	 * @return 任务实例列表
	 */
	@SuppressWarnings("unchecked")
	public List<TaskInstance> getRoleTasks(String roleId) {
		if (roleId == null) {
			return null;
		}
		Query query = dbService.getSession(true).getNamedQuery("TaskSession.getRoleTasks-ById");
		query.setString("roleId", roleId);
		return query.list();
	}
	
	/**
	 * 通过角色ID集取得待办任务列表
	 * @param roleIds
	 * @return 任务实例列表
	 */
	@SuppressWarnings("unchecked")
	public List<TaskInstance> getRoleTasks(List<String> roleIds) {
		if (roleIds == null || roleIds.size() == 0) {
			return null;
		}
		Query query = dbService.getSession(true).getNamedQuery("TaskSession.getRoleTasks-ByIds");
		query.setParameterList("roleIds", roleIds);
		return query.list();
	}
	
	/**
	 * 通过流程实例取得已结束的任务列表，按时间顺序排列
	 * @param flowInstanceId
	 * @return 任务实例列表
	 */
	@SuppressWarnings("unchecked")
	public List<TaskInstance> getEndedTasks(String flowInstanceId) {
		if (flowInstanceId == null) {
			return null;
		}
		Query query = dbService.getSession(true).getNamedQuery("TaskSession.getEndedTasks");
		query.setString("flowInstanceId", flowInstanceId);
		query.setString("processState", TaskInstance.PROCESS_STATE_END);
		return query.list();
	}
}
