/**
 * 
 */
package org.eapp.poss.timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.util.web.upload.FileUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 临时图片文件夹清理任务
 * 自动清理创建时间为1个小时之前的所有临时文件及文件夹
 * 
 * @author zsy
 */
public class DailyJob implements Job {
	/**
	 * 日志
	 */
	private static final Log log = LogFactory.getLog(DailyJob.class);
	
	/**
	 * JOB执行内容
	 * @param context JobExecutionContext
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//清理临时图片文件夹
		log.info("清理临时文件夹");
		//清理生成时间在一小时之前的文件
		FileUtil.cleanTempFile(1); 
	}

}
