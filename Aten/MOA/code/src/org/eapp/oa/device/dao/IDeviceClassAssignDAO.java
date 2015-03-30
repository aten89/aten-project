package org.eapp.oa.device.dao;

import java.util.List;

import org.eapp.oa.device.hbean.DeviceClassAssign;
import org.eapp.oa.device.hbean.DeviceClassAssignDetail;

import org.eapp.oa.system.dao.IBaseHibernateDAO;
public interface IDeviceClassAssignDAO extends IBaseHibernateDAO {
	
	public DeviceClassAssign findDeviceClassAssignById(java.lang.String id);
	
	public List<DeviceClassAssign> findDeviceClassAssignList();

	public List<DeviceClassAssignDetail> findBindById(String id , Integer key, Integer flag);
	
	public void delBindById(String id , Integer key, Integer flag);
	
	public List<DeviceClassAssignDetail> findDeviceClassAssignDetail(String assginId,Integer flag);
	

}
