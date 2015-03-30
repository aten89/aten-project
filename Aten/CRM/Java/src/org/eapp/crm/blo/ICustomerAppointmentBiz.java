package org.eapp.crm.blo;

import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.CustomerAppointment;

/**
 * 预约记录业务逻辑层接口
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-10	钟华杰	新建
 * </pre>
 */
public interface ICustomerAppointmentBiz {
    
	/**
	 * 通知预约记录
	 * @throws CrmException
	 */
	public void txNoticeCustomerAppointment() throws CrmException, Exception;
	
	/**
	 * 根据ID获取预约记录
	 * @param id
	 * @return
	 * @throws CrmException
	 */
	public CustomerAppointment findCustomerAppointmentById(String id) throws CrmException;

}
