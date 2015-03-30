package org.eapp.oa.device.dao;

import java.util.List;

import org.eapp.oa.device.hbean.DeviceCfgItem;
import org.eapp.oa.device.hbean.DeviceClass;
import org.eapp.oa.device.hbean.DeviceClassAssign;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;

import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IDeviceClassDAO extends IBaseHibernateDAO {

	public DeviceClass findById(java.lang.String id);

	public List<DeviceClass> findByProperty(String propertyName, Object value);

	public List<DeviceClass> findByPurchaseFlowKey(Object purchaseFlowKey);

	public List<DeviceClass> findByUseApplyFlowKey(Object useApplyFlowKey);

	public List<DeviceClass> findByAllocateFlowKey(Object allocateFlowKey);

	public List<DeviceClass> findByDiscardFlowKey(Object discardFlowKey);

	/**
	 * 查找属于指定设备类型代码的设备类别，如果设备类型代码不指定，则默认查所有的设备类别
	 * @param deviceTypeCode 设备类型代码
	 * @param areaCode 所属地区
	 * @return
	 */
	public List<DeviceClass> findByDeviceTypeCode(String deviceTypeCode,String areaCode);

	public List<DeviceClass> findByClassKey(Object classKey);
	
	public List<DeviceClass> findAll(String deviceTypeCode);

	public DeviceClass merge(DeviceClass detachedInstance);
	
	/**
	 * 分页查询所有的设备类别
	 * 
	 * @return
	 */
	public ListPage<DeviceClass> findDeviceClassPage(QueryParameters qp);
	
	//自定义抽象方法
	
	public int getDisplayOrder();
	
	public List<DeviceClass> findAssignClass(String userAccountId,
			List<String> groupNames, List<String> postNames, Integer flag, String name) ;
	
	//改造使用方法
	
	/**
	 * 根据设备类别id查询设备属性
	 * @param Id
	 * @return
	 */
	public List<DeviceCfgItem> findDeviceCfgItem(String Id);
	
	public ListPage<DeviceClass> findCheckItemDeviceClass(QueryParameters qp);
	/**
	 * 感觉资产类别和类别名称查询
	 * @param deviceType
	 * @param name
	 * @return
	 */
	public List<DeviceClass> findByNameAndType(String deviceType,String name);
	
	/**
	 * 
	 * @param deviceType
	 * @param assgintype
	 * @param userAccountId
	 * @param groupNames
	 * @param postNames
	 * @return
	 */
	public List<DeviceClass> findAssignDeciveClass(String deviceType,String areaCode,Integer assgintype,String userAccountId,
			List<String> groupNames, List<String> postNames);
	
	public List<DeviceClass> findAssignClass(List<String> deviceType,
			List<String> areaCode, Integer assgintype, String userAccountId,
			List<String> groupNames, List<String> postNames);
	
	public List<DeviceClassAssign> findSelectAssignClass(List<String> deviceType,
			List<String> areaCode, Integer assgintype, String userAccountId,
			List<String> groupNames, List<String> postNames);
	
}