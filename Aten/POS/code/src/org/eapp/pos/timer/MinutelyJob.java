/**
 * 
 */
package org.eapp.pos.timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 每分钟任务
 * @author zsy
 */
public class MinutelyJob implements Job {
	/**
	 * 日志
	 */
	private static final Log log = LogFactory.getLog(MinutelyJob.class);
	
	/**
	 * JOB执行内容
	 * @param context JobExecutionContext
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//TODO 
		log.info("");
	}

}
