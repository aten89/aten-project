/**
 * 
 */
package org.eapp.sys.timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IActionLogBiz;
import org.eapp.blo.ILoginLogBiz;
import org.eapp.sys.config.SysCodeDictLoader;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.upload.FileUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 每日任务
 * 
 * @author zsy
 */
public class DailyJob implements Job {
	/**
	 * 日志
	 */
	private static final Log log = LogFactory.getLog(DailyJob.class);
	
	private IActionLogBiz actionLogBiz = (IActionLogBiz)SpringHelper.getBean("actionLogBiz");
	private ILoginLogBiz loginLogBiz = (ILoginLogBiz)SpringHelper.getBean("loginLogBiz");
	
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
		
		//同步数据字典
		log.info("清除缓存数据");
		SysCodeDictLoader.getInstance().initAllCodeDict();
		
		//备份前一天的日志
		log.info("备份日志:");
		actionLogBiz.txBackUpLastDayLog();
		loginLogBiz.txBackUpLastDayLog();
	}

}
