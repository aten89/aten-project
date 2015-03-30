package org.eapp.oa.device.dao;

import java.util.List;

import org.eapp.oa.device.hbean.DeviceUpdateLog;

import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IDeviceUpdateLogDAO extends IBaseHibernateDAO {

	/**
	 * 根据主键查找记录
	 * @param id
	 * @return
	 */
	public DeviceUpdateLog findById(String id);

	/**
	 * 查找指定设备的更新日志
	 * @param deviceID
	 * @return
	 */
	public List<DeviceUpdateLog> findByDeviceID(String deviceID, Boolean operateDateOrder);
}
