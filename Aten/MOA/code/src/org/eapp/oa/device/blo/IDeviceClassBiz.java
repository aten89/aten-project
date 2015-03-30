package org.eapp.oa.device.blo;

import java.util.List;

import org.eapp.oa.device.hbean.DeviceCfgItem;
import org.eapp.oa.device.hbean.DeviceClass;
import org.eapp.oa.device.hbean.DeviceClassAssign;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;

import org.eapp.oa.system.exception.OaException;

/**
 * 设备类别业务逻辑层
 * 
 * @author sds
 * @version Sep 1, 2009
 */
public interface IDeviceClassBiz {

	/**
	 * 新增设备类别
	 * @param name
	 * @param remark
	 * @param cfgItemIds
	 * @return
	 * @throws OaException
	 */
	public DeviceClass txAddDeviceClass(String classType, String name, String remark, String[] cfgItemIds) throws OaException;

	/**
	 * 删除设备类别
	 * 
	 * @param id
	 * @return
	 */
	public DeviceClass txDelDeviceClass(String id)throws OaException;
	
	/**
	 * 更改设备类别状态
	 * 
	 * @param id
	 * @return
	 */
	public DeviceClass txUpdateDeviceClassStatus(String id);

	/**
	 * 修改设备类别
	 * @param id
	 * @param name
	 * @param remark
	 * @param cfgItemIds
	 * @return
	 * @throws OaException
	 */
	public DeviceClass txModifyDeviceClass(String id, String classType, String name,String remark, String[] cfgItemIds)
			throws OaException;

	/**
	 * 取得用户有权限的设备类别
	 * 
	 * @param userAccountId
	 * @param groupNames
	 * @param postNames
	 * @return
	 */
	public List<DeviceClass> getAssignClass(String deviceType, String areaCode, Integer assgintype,String userAccountId,
			List<String> groupNames, List<String> postNames);

	/**
	 * 分页查询所有的设备类别
	 * 
	 * @return
	 */
	public ListPage<DeviceClass> queryDeviceClassPage(QueryParameters qp);
	/**
	 * 获得指定资产类别的所有设备类别信息，如果资产类别不指定， 查询正常类别不包括删除状态
	 * @param deviceTypeCode 指定的资产类别
	 * @param areaCode 所属地区
	 * @return
	 */
	public List<DeviceClass> getAllDeviceClass(String deviceTypeCode,String areaCode);

	/**
	 * 通过id查找
	 * 
	 * @param id
	 * @return
	 */
	public DeviceClass getDeviceClassById(String id);
	
	//改造使用方法
	/**
	 * 根据设备类别id查询设备属性
	 * @param Id
	 * @return
	 */
	public List<DeviceCfgItem> queryDeviceCfgItem(String Id);
	
	/**
	 * 获得指定资产类别的所有设备类别信息，如果资产类别不指定，则查询所有设备类别信息 
	 * @param deviceTypeCode 指定的资产类别
	 * @return
	 */
	public List<DeviceClass> findAll(String deviceTypeCode);
	
	/**
	 * 取得用户有权限的设备类别
	 * @param deviceType
	 * @param areaCode
	 * @param assgintype
	 * @param userAccountId
	 * @param groupNames
	 * @param postNames
	 * @return
	 */
	public List<DeviceClass> getAssignClassSelect(List<String> deviceType, List<String> areaCode, Integer assgintype,String userAccountId,
			List<String> groupNames, List<String> postNames);
	
	/**
	 * 取得用户有权限的设备类别
	 * @param deviceType
	 * @param areaCode
	 * @param assgintype
	 * @param userAccountId
	 * @param groupNames
	 * @param postNames
	 * @return
	 */
	public List<DeviceClassAssign> getAssignClass(List<String> deviceType, List<String> areaCode, Integer assgintype,String userAccountId,
			List<String> groupNames, List<String> postNames);
	
	

}
