package org.eapp.oa.device.blo;


import java.util.List;

import org.eapp.oa.device.hbean.AreaDeviceCfg;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;

import org.eapp.oa.system.exception.OaException;

/**
 * 
 * @author jxs
 * @version 2011-3-16
 */
public interface IDeviceAreaConfigBiz {

	/**
	 * 新增设备编号以及流程配置
	 * @param areaDeviceCfg
	 * @return
	 * @throws OaException
	 */
	public AreaDeviceCfg txAddAreaDeviceCfg(AreaDeviceCfg areaDeviceCfg)
			throws OaException;

	/**
	 * 删除设备区域配置
	 * 
	 * @param id
	 * @return
	 * @throws OaException
	 */
	public AreaDeviceCfg txDelAreaDeviceCfg(String id) throws OaException;

	/**
	 * 修改设备区域配置
	 * 
	 * @param areaDeviceCfg
	 * @throws OaException
	 */
	public AreaDeviceCfg txModifyAreaDeviceCfg (AreaDeviceCfg areaDeviceCfg) throws OaException;

	/**
	 * 查询所有公文编号
	 * 
	 * @return
	 */
	public ListPage<AreaDeviceCfg> queryAllAreaDeviceCfgPage(QueryParameters qp);
	
	/**
	 * 查询区域设备配置
	 * @param Id
	 * @return
	 * @throws OaException
	 */
	public AreaDeviceCfg getAreaDeviceCfg(String Id)
			throws OaException;
	
	/**
	 * 根据设备类别查询区域设备配置
	 * @param deviceClass
	 * @return
	 * @throws OaException
	 */
	public AreaDeviceCfg getAreaDeviceCfgByClassId(String areaCode,String classID)
			throws OaException;
	/**
	 * 获得指定地区的所有设备类别区域信息，如果地区类别不指定，则查询所有设备类别区域配置 
	 * @param areaCode 指定的区域
	 * @param deviceType 指定的资产类别
	 * @return
	 */
	public List<AreaDeviceCfg> queryAreaDeviceByAreaCode(String areaCode,String deviceType);
	
	
	public List<AreaDeviceCfg> queryAllAreaDevices();
	
	/**
	 * 获取所有是主设备的区域类别配置
	 * @return
	 */
	public List<AreaDeviceCfg> getMainDevAreaDeviceCfgs();
	
	/**
	 * 根据设备类别查询区域设备配置
	 * @param deviceClass
	 * @return
	 * @throws OaException
	 */
//	public List<DeviceAcptCountCfg> getDeviceAcptCountCfgByClassId(String areaCode,String classID,String purpose)
//			throws OaException;
	/**
	 * 根据设备工作用途查询是否多次领用
	 * @param areaCode
	 * @param classID
	 * @param purpose
	 * @return
	 * @throws OaException
	 */
//	public boolean getManyTimeFlagByPurpose(String areaCode,String classID,String purpose) throws OaException;
}
