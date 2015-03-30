/**
 * 
 */
package org.eapp.sys.timer;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobBuilder;
import org.quartz.TriggerBuilder;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 定时器
 * 初始化时，自动启动系统定时任务，系统定时任务是不能移除
 * 初始化时，自动加载所有报表任务的定时作务
 * 
 * @author zsy
 *
 */
public final class QuartZTimer {
	/**
	 * 日志
	 */
	private static final Log log = LogFactory.getLog(QuartZTimer.class);
	/**
	 * 报表任务组
	 */
	private static final String SYSTEM_JOB_GROUP = "SYSTEM_JOB_GROUP";
	/**
	 * 单例对象
	 */
	private static QuartZTimer timer = new QuartZTimer();
	/**
	 * 定时器
	 */
	private Scheduler scheduler;
	
	/**
	 * 单例构造
	 * 不需要被外部实例化
	 */
	private QuartZTimer() {
		
	}
	
	/**
	 * 获取单例
	 * @return 单例
	 */
	public static QuartZTimer getInstance() {
		return timer;
	}
	
	/**
	 * 初始化定时器
	 * 自动启动临时文件清理任务
	 * 
	 * @param warnCron 通知Cron
	 */
	public void init() {
		try {
			//QuartZ的计划器
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			//每日任务
			JobDetail dailyJob = JobBuilder.newJob(DailyJob.class)
		            .withIdentity("DAILY_JOB", SYSTEM_JOB_GROUP)
		            .build();
			//每日任务触发器
			CronTrigger dailyJobTrigger = TriggerBuilder.newTrigger()
				    .withIdentity("DAILY_JOB_TIGGER")
				    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 2 * * ?")) // 每天2点清理一次
				    .build();
			scheduler.scheduleJob(dailyJob, dailyJobTrigger);
			
			//每年任务
			JobDetail yearlyJob = JobBuilder.newJob(YearlyJob.class)
		            .withIdentity("ANNUALLY_JOB", SYSTEM_JOB_GROUP)
		            .build();
			//每年触发器
			CronTrigger yearlyJobTrigger = TriggerBuilder.newTrigger()
				    .withIdentity("ANNUALLY_JOB_TIGGER")
				    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 2 1 1 ?")) // 每年1月1日执行
				    .build();
			scheduler.scheduleJob(yearlyJob, yearlyJobTrigger);
			
			//启动计划器
			scheduler.start(); 
			log.info("QuartZ 定时器已初始化");   
		} catch (SchedulerException e) {
			log.error("QuartZ 定时器初始化失败", e);
		} 
	}
	
	/**
	 * 关闭定时器
	 */
	public void destroy() {
		try {
			//销毁计划器
			scheduler.shutdown();
		} catch (SchedulerException e) {
			log.error("QuartZ 定时器关闭失败", e);
		}
	}
}
