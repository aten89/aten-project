/**
 * 
 */
package org.eapp.workflow.db;

import java.util.Collection;
import java.util.Date;

import org.eapp.workflow.WfmException;
import org.eapp.workflow.asyn.AsynJob;
import org.eapp.workflow.exe.FlowToken;
import org.hibernate.Query;


/**
 * 异步任务的数据库回话
 * @author 林良益
 * 2008-10-27
 * @version 2.0
 */
public class AsynJobSession {

	protected HibernateSessionFactory dbService;
	
	public AsynJobSession(HibernateSessionFactory dbService) {
		this.dbService = dbService;
	}
	
	/**
	 * 获取异步任务列表中的可认领的（已到期的）待办任务，按到期时间排序
	 * @param lockOwner
	 * @return
	 */
	public AsynJob getFirstAcquirableJob(String lockOwner) {
		AsynJob job = null;
		try {
			Query query = dbService.getSession().getNamedQuery("AsynJobSession.getFirstAcquirableJob");
			query.setString("lockOwner", lockOwner);
			query.setTimestamp("now", new Date());
			query.setMaxResults(1);
			job = (AsynJob) query.uniqueResult();

		} catch (Exception e) {
			//log.error(e);
			e.printStackTrace();
			throw new WfmException("无法取得待办的异步任务", e);
		}
		return job;
	}

	/**
	 * 获取到期时间最早的一个任务
	 * 针对未到期的任务
	 * 排除已经被其它线程认领的（已被监视的）
	 * @param lockOwner
	 * @param jobIdsToIgnore
	 * @return
	 */
	public AsynJob getFirstDueJob(String lockOwner, Collection<String> jobIdsToIgnore) {
		AsynJob job = null;
		try {
			Query query = null;
			if ( (jobIdsToIgnore==null) || (jobIdsToIgnore.isEmpty() )) {
		        query = dbService.getSession().getNamedQuery("AsynJobSession.getFirstDueJob");
		        query.setString("lockOwner", lockOwner);
		        
			} else {
				query = dbService.getSession().getNamedQuery("AsynJobSession.getFirstDueJobExlcMonitoredJobs");
				query.setString("lockOwner", lockOwner);
				query.setParameterList("jobIdsToIgnore", jobIdsToIgnore);
		        
			}
			query.setMaxResults(1);
			job = (AsynJob) query.uniqueResult();

		} catch (Exception e) {
			//log.error(e);
			e.printStackTrace();
			throw new WfmException("无法取得待办的异步任务", e);
		}
		return job;
	}
	
	/**
	 * 保存
	 * @param job
	 */
	public void saveJob(AsynJob job){
		dbService.getSession(true).saveOrUpdate(job);
	}
	
	/**
	 * 删除
	 * @param job
	 */
	public void deleteJob(AsynJob job){
		dbService.getSession(true).delete(job);
	}
	
	/**
	 * 加载实例
	 * @param jobID
	 * @return
	 */
	public AsynJob getJob(String jobID){
		return (AsynJob)dbService.getSession().get(AsynJob.class, jobID);
	}
	
	/**
	 * 挂起任务
	 * @param flowToken
	 */
	public void suspendJobs(FlowToken flowToken){
		try {
			Query query = dbService.getSession(true).getNamedQuery("AsynJobSession.suspendJobs");
			query.setParameter("token", flowToken);
			query.executeUpdate();

	      } catch (Exception e) {
	    	  e.printStackTrace();
	    	  throw new WfmException("无法挂起异步任务", e);
	      }
	}
	
	/**
	 * 恢复挂起的任务
	 * @param flowToken
	 */
	public void resumeJobs(FlowToken flowToken){
		try {
			Query query = dbService.getSession(true).getNamedQuery("AsynJobSession.resumeJobs");
			query.setParameter("token", flowToken);
			query.executeUpdate();

	      } catch (Exception e) {
	    	  e.printStackTrace();
	    	  throw new WfmException("无法恢复挂起的异步任务", e);
	      }
	}
	
	
}
