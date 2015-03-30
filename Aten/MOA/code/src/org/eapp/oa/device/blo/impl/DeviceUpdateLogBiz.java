package org.eapp.oa.device.blo.impl;

import java.util.List;

import org.eapp.oa.device.blo.IDeviceUpdateLogBiz;
import org.eapp.oa.device.dao.IDeviceUpdateLogDAO;
import org.eapp.oa.device.hbean.DeviceUpdateLog;


public class DeviceUpdateLogBiz implements IDeviceUpdateLogBiz {

	private IDeviceUpdateLogDAO deviceUpdateLogDAO;
	
	@Override
	public DeviceUpdateLog getById(String id) {
		DeviceUpdateLog deviceUpdateLog = deviceUpdateLogDAO.findById(id);
		return deviceUpdateLog;
	}

	@Override
	public List<DeviceUpdateLog> queryByDeviceID(String deviceID, Boolean operateDateOrder) {
		return deviceUpdateLogDAO.findByDeviceID(deviceID, operateDateOrder);
	}
	

	public void setDeviceUpdateLogDAO(IDeviceUpdateLogDAO deviceUpdateLogDAO) {
		this.deviceUpdateLogDAO = deviceUpdateLogDAO;
	}
}
