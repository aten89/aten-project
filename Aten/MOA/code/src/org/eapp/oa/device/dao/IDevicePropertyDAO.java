package org.eapp.oa.device.dao;

import java.util.List;

import org.eapp.oa.device.dto.DeviceOptionQueryParameters;
import org.eapp.oa.device.hbean.DeviceProperty;
import org.eapp.util.hibernate.ListPage;

import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IDevicePropertyDAO extends IBaseHibernateDAO{
	
	/**
	 * 分页查询设备属性
	 * @param qp
	 * @return
	 */
	public ListPage<DeviceProperty> findDevicePropertyPage(DeviceOptionQueryParameters qp);
	
	
	/**
	 * 根据设备属性id查询
	 * @param id 属性id
	 * @return
	 */
	public DeviceProperty findDeviceProperty(String id);
	
	/**
	 * 判断属性名称是否存在
	 * @param optionName
	 * @return
	 */
	public int checkOptionExist(String optionName);
	
	/**
	 * 查询设备属性
	 * @return
	 */
	public List<DeviceProperty> findDevicePropertyList();



}
