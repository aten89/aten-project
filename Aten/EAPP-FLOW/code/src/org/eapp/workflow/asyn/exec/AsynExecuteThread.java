/**
 * 
 */
package org.eapp.workflow.asyn.exec;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Set;

import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.asyn.AsynJob;
import org.eapp.workflow.db.AsynJobSession;


/**
 * 异步任务执行线程
 * @author 林良益
 * 2008-10-27
 * @version 2.0
 */
public class AsynExecuteThread extends Thread {
	//异步工作执行器
	AsynJobRunner jobRunner;
	//线程空闲间隔时间
	int idleInterval;
	
	//线程对像当前实际的时间间隔
	int currentIdleInterval;
	//线程是否是激活状态的（默认为true），false表示线程即将结束或已经结束
	boolean isActive = true;
	
	public AsynExecuteThread(String threadName , AsynJobRunner runner , int idleInterval){
		super(threadName);
		this.jobRunner = runner;
		this.idleInterval = idleInterval;
	}

	public int getIdleInterval() {
		return idleInterval;
	}

	public void setIdleInterval(int idleInterval) {
		this.idleInterval = idleInterval;
	}

	public AsynJobRunner getRunner() {
		return jobRunner;
	}

	public int getCurrentIdleInterval() {
		return currentIdleInterval;
	}

	public void setCurrentIdleInterval(int currentIdleInterval) {
		this.currentIdleInterval = currentIdleInterval;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	/**
	 * 异步任务线程运行体
	 * 目前的设计为一个线程在一个时间内执行一个异步任务
	 */
	@Override
	public void run(){
		try {
			//初始化线程休眠间隔
			currentIdleInterval = idleInterval;
			//判断线程是否被stop，如果isActive=false，表名线程被停职
			while (isActive) {
				WfmContext context = WfmConfiguration.getInstance().getWfmContext();//从当前线程取对象
				try {
					//获取当前线程要执行的异步任务列表（目前一次只获取一条）
					if (execute(context)) {
						context.close();//有执行任务要提交一次
					} else if (isActive) {
						//没有取到已经到期，需要执行的任务
						//根据队列中的下一任务判断当前线程的休眠时间
						long waitPeriod = getWaitPeriod(context);
						if (waitPeriod>0) {
							//线程休眠
							synchronized(jobRunner) {
								jobRunner.wait(waitPeriod);
							}
						}
					}	            
					// 完成一次任务执行，重置间隔时间
					currentIdleInterval = idleInterval;
				} catch (InterruptedException e) {
					//休眠中断
					e.printStackTrace();
				} catch (Exception e) {
					//遇到其它异常（数据库链接异常）
					e.printStackTrace();
					try {
						synchronized(jobRunner) {
							jobRunner.wait(currentIdleInterval);
						}
					} catch (InterruptedException e2) {
						e2.printStackTrace();
					}
					// after an exception, the current idle interval is doubled to prevent 
					// continuous exception generation when e.g. the db is unreachable
					//出现异常，将间隔时间延长一倍
					currentIdleInterval = currentIdleInterval*2;
				} finally {
					context.close();
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	/**
	 * 从队列中获取异步任务
	 * 线程同步
	 * @return
	 */
	protected boolean execute(WfmContext context) {
		//将runner作为同步锁，所有的线程只能依次获取异步任务
		AsynJobSession jobSession = context.getAsynJobSession();
		AsynJob job = null;
		synchronized (jobRunner) {
			//获取待执行的异步任务
			job = jobSession.getFirstAcquirableJob(getName());
			if (job == null) {
				return false;
			}
        	job.setLockOwner(getName());
        	job.setLockTime(new Date());
		}
		

		try {
			if (job.execute(context)) {
				jobSession.deleteJob(job);
			}
		} catch (Exception e) {
			//执行异步任务异常
	        //log.debug("exception while executing '"+job+"'", e);
			//记录异常信息
	        StringWriter sw = new StringWriter();
	        e.printStackTrace(new PrintWriter(sw));
	        job.setException(sw.toString());
	        // 重试次数减一
	        job.setRetries(job.getRetries()-1);
		}
		return true;
	}
	
	
	/**
	 * 获取当前线程的等待时间间隔
	 * 默认是currentIdleInterval，
	 * 如果下一任务的时间期限早于currentTimeMillis + currentIdleInterval,则按任务期限缩短间隔时间	 * 
	 * @return
	 */
	protected long getWaitPeriod(WfmContext context) {
		long interval = currentIdleInterval;
		Date nextDueDate = getNextDueDate(context);
		if (nextDueDate!=null) {
			long currentTimeMillis = System.currentTimeMillis();
			long nextDueDateTime = nextDueDate.getTime();
			if (nextDueDateTime < currentTimeMillis + currentIdleInterval) {
				interval = nextDueDateTime - currentTimeMillis;
			}
		}
		if (interval<0) {
			interval = 0;
		}
		return interval;
	}	

	
	/**
	 * 获取下一个到期的任务时间
	 * 不包括已经受监管的任务
	 * @return
	 */  
	protected Date getNextDueDate(WfmContext context) {
		Date nextDueDate = null;
		AsynJobSession jobSession = context.getAsynJobSession();
	
		Set<String> jobIdsToIgnore = jobRunner.getMonitoredJobIds();
		//这里应该同步，避免两线程同时取到相同新任务
		synchronized(jobRunner){
			AsynJob job = jobSession.getFirstDueJob(getName(), jobIdsToIgnore);
			if (job!=null) {
				nextDueDate = job.getDueDate();
				jobRunner.addMonitoredJobId(getName(), job.getId());
			}
		}

		return nextDueDate;
	}

}
