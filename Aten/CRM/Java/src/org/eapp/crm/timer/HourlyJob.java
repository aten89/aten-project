/**
 * 
 */
package org.eapp.crm.timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.util.UsernameCache;
import org.eapp.crm.config.SysCodeDictLoader;
import org.eapp.crm.util.UserAccountCache;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 临时图片文件夹清理任务
 * 自动清理创建时间为1个小时之前的所有临时文件及文件夹
 * 
 * @author zsy
 */
public class HourlyJob implements Job {
	/**
	 * 日志
	 */
	private static final Log log = LogFactory.getLog(HourlyJob.class);
	
	/**
	 * JOB执行内容
	 * @param context JobExecutionContext
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("从ERMP中同步数据字典");
		//从ERMP中同步数据字典
		SysCodeDictLoader.getInstance().initAllCodeDict();
		log.info("清除缓存数据");
		//清除通讯录缓存ERMP的数据
		UserAccountCache.clearCache();
		//清除用户账号与用户名转换缓存
		UsernameCache.clearUsernames();
	}

}
