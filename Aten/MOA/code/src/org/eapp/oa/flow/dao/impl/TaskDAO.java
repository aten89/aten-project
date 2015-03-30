package org.eapp.oa.flow.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.eapp.oa.flow.dao.ITaskDAO;
import org.eapp.oa.flow.dto.TaskQueryParameters;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.flow.hbean.TaskAssign;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * A data access object (DAO) providing persistence and search support for Task
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see .Task
 * @author MyEclipse Persistence Tools
 */

public class TaskDAO extends BaseHibernateDAO implements ITaskDAO {
	/**
     * 日志
     */
	private static Logger log = LoggerFactory.getLogger(TaskDAO.class);
	
	@Override
	public Task findById(String id) {
		log.debug("getting Task instance with id: " + id);
		Task instance = (Task) getSession().get(Task.class, id);
		return instance;
	}

	@Override
	public ListPage<Task> queryDealingTask(QueryParameters parms, String userAccountId) {
		if (parms == null)  {
			throw new IllegalArgumentException("非法参数:查询参数对象为null");
		}
		String hql = "from Task as t left join t.taskAssigns as p where (t.taskState=:createState or t.taskState=:startState)" +
				" and (" +
				"(t.transactor=:userId ) or (p.assignKey=:userId and t.transactor is null and p.type=:type)" +
				") order by t.createTime";
		try {
			parms.addParameter("userId", userAccountId);
			parms.addParameter("createState", TaskInstance.PROCESS_STATE_CREATE);
			parms.addParameter("startState", TaskInstance.PROCESS_STATE_START);
			parms.addParameter("type", TaskAssign.TYPE_USER);
			
			ListPage<Task> page = new CommQuery<Task>().queryListPage(parms, "select distinct(t) " + hql,
					"select count(*) " + hql, getSession());
			
////			ListPage page = CommQuery.queryPage(parms, "select distinct(t) " + hql, 
////					"select count(*) " + hql, getSession());
//			List<Task> list = page.getDataList();
//			List<Task> tasks = new ArrayList<Task>();
////			if (list != null) {
////				for (Object[] o : list) {
////					Task t = (Task)o[0];
////					t.setFlowConfig((FlowConfig)o[1]);
////					tasks.add(t);
////				}
////			}
//			if (list != null) {
//				for (Task t : list) {
//					tasks.add(t);
//				}
//			}
//			page.setDataList(tasks);
			return page;
		} catch (RuntimeException re) {
			log.error("query listPage error",re);
			return new ListPage<Task>();
		}
	}

	@Override
	public ListPage<Task> queryUnFinishTask(TaskQueryParameters parms) {
		
		if (parms == null)  {
			throw new IllegalArgumentException("非法参数:查询参数对象为null");
		}
		
		StringBuffer hql = new StringBuffer("from Task as t where 1=1");
		
		if(parms.getFormId() !=null){
			hql.append(" and t.formID = :formId");
		}
		if(parms.getDesc() != null){
			hql.append(" and t.description like :desc");
			parms.toArountParameter("desc");
		}
		
		if(parms.getEndCreateTime() !=null){
			hql.append(" and t.createTime <= :endCreateTime");
		}
		
		if(parms.getBeginCreateTime() !=null){
			hql.append(" and t.createTime >= :beginCreateTime");
		}
		
		if(parms.getTransactor() !=null){
			hql.append(" and (" +
					"(t.transactor=:transactor ) or (ta.assignKey=:transactor and t.transactor is null and ta.type=:type)" +
					")");
		}
		if(parms.getState() !=null && parms.getState().length > 0){
			hql.append(" and t.taskState in (:states)");
		}
		
		try {
			return new CommQuery<Task>().queryListPage(parms,
					"select distinct(t) " + parms.appendOrders(hql, "t"),
					"select count(distinct t) " + hql.toString(), getSession());
		} catch(RuntimeException re) {
			re.printStackTrace();
			return new ListPage<Task>();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Task> findByTaskInstanceID(String taskInstanceID){
		log.debug("finding Task instance By taskInstanceID: " + taskInstanceID);

		String queryString = "from Task as model where model.taskInstanceID= ?";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setParameter(0, taskInstanceID);
		return queryObject.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Task> findEndedTasks(String flowInstanceID) {
		if (flowInstanceID == null) {
			return null;
		}
		Query query = getSession().createQuery("from Task as t where t.flowInstanceID=:flowInstanceID and " +
				"t.endTime is not null order by t.endTime");
		query.setString("flowInstanceID", flowInstanceID);
		return query.list();
	}
	
	/**
	 * 通过流程ID，把相关未结束的任务强行结束掉
	 */
	@Override
	public void endTasksByFlowInstanceID(String flowInstanceID, String comment) {
		try {
			Query query = getSession().createQuery(
					"update Task as t set t.taskState=:endState,t.comment=:comment,t.endTime=:endTime " +
					"where t.flowInstanceID=:flowInstanceID and t.taskState!=:endState");
			query.setTimestamp("endTime", new Timestamp(System.currentTimeMillis()));
			query.setString("endState", TaskInstance.PROCESS_STATE_END);
			query.setString("comment", comment);
			query.setString("flowInstanceID", flowInstanceID);
			query.executeUpdate();
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Task> findDealingTaskList(String flowInstanceId) {
		if (flowInstanceId == null) {
			return null;
		}
		try {
			Query query = getSession().createQuery("from Task as t where t.flowInstanceID=:flowInstanceID and " +
					"(t.taskState=:createState or t.taskState=:startState) order by t.createTime desc");
			query.setString("flowInstanceID", flowInstanceId);
			query.setString("createState", TaskInstance.PROCESS_STATE_CREATE);
			query.setString("startState", TaskInstance.PROCESS_STATE_START);
			
			return query.list();
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}