/**
 * 
 */
package org.eapp.oa.system.util;

import java.util.Calendar;
import java.util.TimerTask;

import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.util.web.upload.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * 周期性的执行任务，此定时器的执行周期必须设为1小时，定时任务才能正常执行
 * @author zsy
 * @version 
 */
public class PeriodTask extends TimerTask {
	 /**
     * 日志
     */
	private static Logger log = LoggerFactory.getLogger(PeriodTask.class);
	/**
	 * 启动时间
	 */
	public static final int START_HOUR = 2;
	
	public PeriodTask() {
	}
	
	/**
	 * 任务
	 */
	public void run() {
		try {
			
			//从ERMP中同步数据字典
			SysCodeDictLoader.getInstance().initAllCodeDict();
			
			//清除通讯录缓存ERMP的数据
			UserAccountCache.clearCache();
			
			Calendar c = Calendar.getInstance();
			
			//每天固定时间执行
			if (c.get(Calendar.HOUR_OF_DAY) == START_HOUR) {
				//清理临时图片文件夹
				log.info("清理临时图片文件夹:");
				FileUtil.cleanTempFile(2);
			}
			
//			//每年1月1日0点执行
//			if (c.get(Calendar.MONTH) == 0 && 
//					c.get(Calendar.DAY_OF_MONTH) == 1 && 
//					c.get(Calendar.HOUR_OF_DAY) == 0) {
//				
//				log.info("作废所有报销单，且删除所有草稿，并发送邮件");
//				IReimbursementBiz reimbursementBiz = (IReimbursementBiz) SpringHelper.getSpringContext().getBean(
//					"reimbursementBiz");
//				
//				reimbursementBiz.txCleanReimbursement();
//				log.info("初始化当年节假日信息");
//				IHolidaysInfoBiz holidaysInfoBiz = (IHolidaysInfoBiz) SpringHelper.getSpringContext().getBean(
//					"holidaysInfoBiz");
//				holidaysInfoBiz.txInitHolidays();
//				
//				log.info("年休假结转...");
//				IHolidayInfoBiz holidayInfoBiz = (IHolidayInfoBiz) SpringHelper.getSpringContext().getBean(
//					"holidayInfoBiz");
//				holidayInfoBiz.txCarryForwardHoliday();
//			}
			
		} catch(Exception e) {
			log.error("定时器运行出错", e);
		}
	}
}
