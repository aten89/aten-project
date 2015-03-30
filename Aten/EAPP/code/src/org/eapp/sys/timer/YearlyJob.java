/**
 * 
 */
package org.eapp.sys.timer;

import java.util.Calendar;

import org.eapp.blo.IActionLogBiz;
import org.eapp.blo.ILoginLogBiz;
import org.eapp.util.spring.SpringHelper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 每年任务
 * 
 * @author zsy
 */
public class YearlyJob implements Job {
	
	private IActionLogBiz actionLogBiz = (IActionLogBiz)SpringHelper.getBean("actionLogBiz");
	private ILoginLogBiz loginLogBiz = (ILoginLogBiz)SpringHelper.getBean("loginLogBiz");
	/**
	 * JOB执行内容
	 * @param context JobExecutionContext
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Calendar c = Calendar.getInstance();
		//创建今年的备份
		int year = c.get(Calendar.YEAR);
		actionLogBiz.addLogTable(year);
		loginLogBiz.addLogTable(year);
		//删除当前表中去年已备份的数据
		actionLogBiz.deleteLastYearLogs();
		loginLogBiz.deleteLastYearLogs();
	}

}
