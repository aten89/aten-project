package org.eapp.crm.dao;

import java.util.Date;
import java.util.List;

import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.CustomerAppointment;

/**
 * 预约记录DAO接口
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-10	钟华杰	新建
 * </pre>
 */
public interface ICustomerAppointmentDAO extends IBaseHibernateDAO {

	/**
	 * 根据时间获取符合条件的预约记录
	 * @param runTime
	 * @param offset
	 * @return
	 * @throws CrmException
	 */
	public List<CustomerAppointment> queryAllCustomerAppointment(Date appointmentTimeBegin, Date appointmentTimeEnd) throws CrmException;
	
	/**
	 * 根据ID获取预约记录
	 * @param customerAppointmentId
	 * @return
	 * @throws CrmException
	 */
	public CustomerAppointment findById(String customerAppointmentId) throws CrmException;
}
