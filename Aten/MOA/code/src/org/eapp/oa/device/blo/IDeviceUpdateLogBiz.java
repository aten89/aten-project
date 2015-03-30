package org.eapp.oa.device.blo;

import java.util.List;

import org.eapp.oa.device.hbean.DeviceUpdateLog;


/**
 * Description:设备更新日志业务逻辑层
 * 
 * @author shenyinjie
 * @version 2011-04-16
 */
public interface IDeviceUpdateLogBiz {

	public DeviceUpdateLog getById(String id);
	
	public List<DeviceUpdateLog> queryByDeviceID(String deviceID, Boolean operateDateOrder);
	
}
