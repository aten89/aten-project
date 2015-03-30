/**
 * 
 */
package org.eapp.workflow.svr;

import org.eapp.workflow.WfmContext;
import org.eapp.workflow.asyn.AsynJob;
import org.eapp.workflow.asyn.exec.AsynJobRunner;
import org.eapp.workflow.db.AsynJobSession;


/**
 * 基于数据库的消息服务
 * @author 林良益
 * 2008-10-27
 * @version 2.0
 */
public class DBMessageServiceImpl implements MessageService {
	private AsynJobSession jobSession;
	
	boolean hasJobsInQueue = false;
	
	public DBMessageServiceImpl(WfmContext context){
		this.jobSession = context.getAsynJobSession();
	}

	public void close() {
		AsynJobRunner jobRunner = AsynJobRunner.getInstance();
		//如果数据库中有任务加入，切jobRunner已经启动，则激活可能在wait状态的处理线程
		if(hasJobsInQueue){
			synchronized(jobRunner){
				jobRunner.notify();
			}
		}
	}

	public void send(AsynJob job) {
		
		//向数据库写入异步任务
		jobSession.saveJob(job);
		hasJobsInQueue = true;
	}

}
