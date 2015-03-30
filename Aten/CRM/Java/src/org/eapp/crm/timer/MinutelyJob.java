/**
 * 
 */
package org.eapp.crm.timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.crm.blo.ICustomerAppointmentBiz;
import org.eapp.crm.exception.CrmException;
import org.eapp.util.spring.SpringHelper;
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
		log.info("通知预约记录内容");
		ICustomerAppointmentBiz customerAppointmentBiz = (ICustomerAppointmentBiz) SpringHelper.getBean("customerAppointmentBiz");
		if(null != customerAppointmentBiz) {
			try {
				customerAppointmentBiz.txNoticeCustomerAppointment();
			} catch (CrmException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
