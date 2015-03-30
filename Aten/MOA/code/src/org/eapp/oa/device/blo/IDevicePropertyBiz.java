package org.eapp.oa.device.blo;

import java.util.List;

import org.eapp.oa.device.dto.DeviceOptionQueryParameters;
import org.eapp.oa.device.hbean.DeviceProperty;
import org.eapp.util.hibernate.ListPage;

import org.eapp.oa.system.exception.OaException;

/**
 * 设备属性
 * @author jiangxiongsheng
 *
 */
public interface IDevicePropertyBiz {
	/**
	 * 分页查询设备属性
	 * @param qp
	 * @return
	 */
	public ListPage<DeviceProperty> queryDevicePropertyPage(DeviceOptionQueryParameters qp);
	
	/**
	 * 新增设备属性信息
	 * @param name 属性名称
	 * @param remark 备注
	 * @return
	 */
	public DeviceProperty txAddDeviceProperty(String name, String remark)throws OaException;
	
	/**
	 * 修改设备属性信息
	 * @param id 属性id
	 * @param name 属性名称
	 * @param remark 备注
	 * @return
	 */
	public DeviceProperty txUpdateDeviceProperty(String id, String name, String remark)throws OaException;
	
	/**
	 * 删除设备信息
	 * @param id属性id
	 */
	public void txDeleteDeviceProperty(String id)throws OaException;
	
	/**
	 * 根据设备属性id查询
	 * @param id 属性id
	 * @return
	 */
	public DeviceProperty queryDeviceProperty(String id)throws OaException;
	
	/**
	 * 查询设备属性
	 * @return
	 */
	public List<DeviceProperty> queryDevicePropertyList();
}
