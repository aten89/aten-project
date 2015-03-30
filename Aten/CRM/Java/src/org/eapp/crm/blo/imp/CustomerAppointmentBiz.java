/**
 * 
 */
package org.eapp.crm.blo.imp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.client.util.SystemProperties;
import org.eapp.client.util.UsernameCache;
import org.eapp.crm.blo.ICustomerAppointmentBiz;
import org.eapp.crm.dao.ICustomerAppointmentDAO;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.CustomerAppointment;
import org.eapp.oa.rmi.hessian.Contact;
import org.eapp.oa.rmi.hessian.IAddressListPoint;
import org.eapp.util.mail.JMailProxy;
import org.eapp.util.mail.MailMessage;
import org.springframework.util.CollectionUtils;

/**
 * 预约记录业务逻辑层实现
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-10	钟华杰	新建
 * </pre>
 */
public class CustomerAppointmentBiz implements ICustomerAppointmentBiz {

	private static final Log log = LogFactory.getLog(CustomerAppointmentBiz.class);
	
    /**
     * 预约记录DAO接口
     */
    private ICustomerAppointmentDAO customerAppointmentDAO;
    
    /**
     * 联系方式接口
     */
    private IAddressListPoint addressListPoint;

    @Override
    public void txNoticeCustomerAppointment() throws CrmException, Exception {
    	//获取符合条件的预约记录
    	Date noticeTimeEnd = new Date();
    	Calendar ca = Calendar.getInstance();
    	ca.setTime(noticeTimeEnd);
    	ca.add(Calendar.MINUTE, -1);
    	Date noticeTimeBegin = ca.getTime();
    	List<CustomerAppointment> customerAppointmentList = customerAppointmentDAO.queryAllCustomerAppointment(noticeTimeBegin, noticeTimeEnd);
    	if(!CollectionUtils.isEmpty(customerAppointmentList)) {
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    		for(CustomerAppointment customerAppointment : customerAppointmentList) {
                if(customerAppointment.getAppointmentTime() != null){
                	customerAppointment.setAppointmentTimeStr(df.format(customerAppointment.getAppointmentTime()));
                }
    			String typeStr = customerAppointment.getAppointmentType();
    			if(!StringUtils.isEmpty(typeStr)) {
    				String content = "您在" + customerAppointment.getAppointmentTimeStr() + "预约了客户“" + customerAppointment.getCustomerInfo().getCustName() + "”，请提前做好准备。【" + customerAppointment.getRemark() + "】";
    				if(-1 != typeStr.indexOf("SM_MESSAGE")) {
    					//短信通知 TODO
    				}
    				
    				if(-1 != typeStr.indexOf("MAIL_MESSAGE")) {
    					//邮件通知
    					Contact contact = addressListPoint.getUserContact(customerAppointment.getCreateor());
    					if(null != contact && null != contact.getEmail()) {
    						MailMessage mailMessage = new MailMessage(contact.getEmail(), "预约提醒", content, "text/html;charset=gbk");
    						// 开始发送
    			            try {
    			                JMailProxy.daemonSend(mailMessage);
    			            } catch (Exception e) {
    			                log.error("邮件发送失败!", e);
    			            }
    					}
    						
    				}
    				
    				if(-1 != typeStr.indexOf("SYS_MESSAGE")) {
    					//系统消息框
    					UserAccountService userAccountService = new UserAccountService();
    					userAccountService.sendSysMsg(SystemProperties.SYSTEM_ID, "系统", customerAppointment.getCreateor(), content);
    				}
    			}
    		}
    	}
    }
    
    @Override
    public CustomerAppointment findCustomerAppointmentById(String id)
    		throws CrmException {
    	CustomerAppointment appointmentRecord = customerAppointmentDAO.findById(id);
    	if(null != appointmentRecord) {
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    		//设置时间格式
            if(appointmentRecord.getAppointmentTime() != null){
            	appointmentRecord.setAppointmentTimeStr(df.format(appointmentRecord.getAppointmentTime()));
            }
            
            //设置提交人名称
            appointmentRecord.setCreateorName(UsernameCache.getDisplayName(appointmentRecord.getCreateor()));
    	}
    	return appointmentRecord;
    }

	public void setCustomerAppointmentDAO(
			ICustomerAppointmentDAO customerAppointmentDAO) {
		this.customerAppointmentDAO = customerAppointmentDAO;
	}

	public void setAddressListPoint(IAddressListPoint addressListPoint) {
		this.addressListPoint = addressListPoint;
	}
}
