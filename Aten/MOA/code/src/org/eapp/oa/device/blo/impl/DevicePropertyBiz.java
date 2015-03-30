package org.eapp.oa.device.blo.impl;

import java.util.List;

import org.eapp.oa.device.blo.IDevicePropertyBiz;
import org.eapp.oa.device.dao.IDevicePropertyDAO;
import org.eapp.oa.device.dto.DeviceOptionQueryParameters;
import org.eapp.oa.device.hbean.DeviceProperty;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Hibernate;

import org.eapp.oa.system.exception.OaException;

public class DevicePropertyBiz implements IDevicePropertyBiz {
	private IDevicePropertyDAO devicePropertyDAO;
	
	public void setDevicePropertyDAO(IDevicePropertyDAO devicePropertyDAO) {
		this.devicePropertyDAO = devicePropertyDAO;
	}

	@Override
	public DeviceProperty queryDeviceProperty(String id) throws OaException {
		if(null == id) {
			throw new IllegalArgumentException();
		}
		DeviceProperty deviceProperty = devicePropertyDAO.findDeviceProperty(id);
		if(deviceProperty==null){
			throw new OaException("该设备属性不存在");
		}
		return deviceProperty;
	}

	@Override
	public ListPage<DeviceProperty> queryDevicePropertyPage(
			DeviceOptionQueryParameters qp) {
		
		return devicePropertyDAO.findDevicePropertyPage(qp);
	}

	@Override
	public DeviceProperty txAddDeviceProperty(String name, String remark) throws OaException {
		//判断属性名称是否已经存在
		int existNameNum = devicePropertyDAO.checkOptionExist(name);
		if(existNameNum>0){
			throw new OaException("设备属性名称【"+name+"】已经存在");
		}
		DeviceProperty deviceProperty = new DeviceProperty();
		deviceProperty.setPropertyName(name);
		deviceProperty.setRemark(remark);
		devicePropertyDAO.save(deviceProperty);
		return deviceProperty;
	}

	@Override
	public void txDeleteDeviceProperty(String id)throws OaException {
		if(id==null || id==""){
			throw new IllegalArgumentException("参数异常");
		}
		DeviceProperty deviceProperty = queryDeviceProperty(id);
		Hibernate.initialize(deviceProperty.getDeviceCfgItems());
		if(deviceProperty.getDeviceCfgItems()!=null && deviceProperty.getDeviceCfgItems().size()>0){
			throw new OaException("设备属性已经被设备类别使用，不能删除！");
		}
		devicePropertyDAO.delete(deviceProperty);
	}

	@Override
	public DeviceProperty txUpdateDeviceProperty(String id, String name,
			String remark) throws OaException {
		DeviceProperty deviceProperty = queryDeviceProperty(id);
		//判断属性名称是否已经存在
		if(!name.equals(deviceProperty.getPropertyName())){
			int existNameNum = devicePropertyDAO.checkOptionExist(name);
			if(existNameNum>0){
				throw new OaException("设备属性名称【"+name+"】已经存在");
			}
		}
		
		deviceProperty.setPropertyName(name);
		deviceProperty.setRemark(remark);
		devicePropertyDAO.saveOrUpdate(deviceProperty);
		return deviceProperty;
	}
	@Override
	public List<DeviceProperty> queryDevicePropertyList() {
		return devicePropertyDAO.findDevicePropertyList();
	}
	
}
