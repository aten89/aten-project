package org.eapp.oa.device.blo;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;


import org.eapp.oa.device.dto.DeviceQueryParameters;
import org.eapp.oa.device.hbean.Device;
import org.eapp.oa.device.hbean.DeviceProperty;
import org.eapp.oa.device.hbean.DeviceValiDetail;
import org.eapp.oa.system.exception.OaException;
import org.eapp.rpc.dto.DataDictInfo;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.WfmContext;

/**
 * Description: DeviceBiz 业务逻辑接口
 * 
 * @author 郑超
 */

public interface IDeviceBiz {
	
	/**
	 * 新增设备信息
	 * @param device
	 * @return
	 * @throws OaException
	 */
	public Device txAddDevice(Device device) throws OaException;
	
	/**
	 * 修改设备信息
	 * @param device
	 * @return
	 * @throws OaException
	 */
	public Device txModifyDevice(Device device) throws OaException;
			
	/**
	 * 删除设备信息
	 * 
	 * @param id
	 * @return
	 */
	public Device txDeleteDevice(String id)throws OaException;


	/**
	 * 通过ID查找设备
	 * 
	 * @param id
	 * @return
	 */
	public Device getDeviceById(String id);

	/**
	 * 获得设备类型(包含配件与非配件)
	 * 
	 * @param deviceClass
	 * @return
	 */
	public List<DataDictInfo> getDeviceTypeAll(String deviceClass);

	/**
	 * 获得设备类型
	 * @param deviceClass
	 * @param flag "0":配件,"1"非配件,null:所有
	 * @return
	 */
	public List<DataDictInfo> getDeviceType(String deviceClass,
			String flag);

	/**
	 * 获得设备属性
	 * @param id
	 * @param deviceClass
	 * @param deviceType
	 * @param merge 是否合并新配置的属性
	 * @return
	 */
	public Collection<DeviceProperty> getDeviceProperty(String id, String deviceClass,
			String deviceType, boolean merge);
	
	/**
	 * 获取设备验收单的条目
	 * @param id
	 * @param valiType
	 * @param dictKey
	 * @return
	 */
	public Collection<DeviceValiDetail> getDeviceValiDetail(String id, int valiType, String dictKey);
	
	/**
	 * 查询信息 带分页
	 */
	public ListPage<Device> getDeviceListPage(DeviceQueryParameters qp,List<String> deviceClass);


	/**
	 * 报废会议设备
	 * 
	 * @param id
	 */
	public void txDiscardMeetDevice(String id);
	
	/**
	 * 修改设备可用数量
	 * 
	 * @param id
	 * @param useNumber
	 */
	public void txUpdateDeviceUseNumber(String id, Integer useNumber);


	/**
	 * 获取设备类别
	 * 
	 * @return
	 */
	public Collection<DataDictInfo> getDeviceClass();
	
	public List<Device> queryDeviceBorrowInfo(String id, String deviceClass, Timestamp beginTime, Timestamp endTime);
	
	public List<Device> getUnbindITDevice(String deviceType,String deviceClass,String status,boolean isAccessory);
	
	public void txDiscardITDevice(String id) ;
	
	public void queryDeviceToExcel(ListPage<Device> listPage ,String fileName,File file)throws IOException, OaException;

	/**
	 * 获取指定人申请的设备
	 * @param applicantID 申请人
	 * @return
	 */
	public List<Device> queryDevicesByApplicantID(String applicantID);
	
	/**
	 * 搜索指定账户名下的设备.如果设备已删除、设备已报废、用户已离职则之前指定账户领用的设备不能算是其名下的设备.
	 * @param qp 查询参数
	 * @param applicantID 指定账户ID
	 * @return
	 */
	public ListPage<Device> queryDevicePageByApplicantID(DeviceQueryParameters qp, String applicantID);
	
	/**
	 * 获取设备流水号
	 * @param classID
	 * @param areaCode
	 * @return
	 */
	public int getDeviceSepNum(String classID,String areaCode);

	/**
	 * 查询设备列表分页数据
	 * @param qp
	 * @param ownerAcountID 设备归属账户ID
	 * @param assignDevClassFlag
	 * @param mergeFlag 是否追加我名下的设备和闲置设备的结果集。因为正常情况下两者是互斥的。如果该值为true，则追加
	 * @param approvingFlag 设备是否处于审批中，null所有；true处于审批中；false不在审批中的
	 * @param excludeScrapFlag 是否排除报废的
	 * @param deletedIDsAtReject 驳回修改删除的ID列表
	 * @param initDevCfgList 是否初始化设备配置项
	 * @return
	 */
	public ListPage<Device> queryDevices(DeviceQueryParameters qp, String ownerAccountID, boolean assignDevClassFlag, List<String> assignDeviceClassIDs, 
			boolean mergeFlag, Boolean approvingFlag, Boolean excludeScrapFlag, List<String> deletedIDsAtReject, boolean initDevCfgList);

	/**
	 * 根据ID查找设备数据
	 * @param deviceIDList
	 * @return
	 */
	public List<Device> queryDevicesByIDs(List<String> deviceIDList, boolean initDevCfgList);
	/**
	 * 根据设备类别查询设备信息
	 * @param deviceClass
	 * @return
	 */
	public List<Device> queryDeviceInfo(String deviceClass);
	
	/**
	 * 查询信息 带分页
	 */
	public ListPage<Device> getStatisticsDeviceListPage(DeviceQueryParameters qp,List<String> deviceClass,List<String> deviceAreaCodes,List<String> deviceTypes);
	
	public void csExportDevice(DeviceQueryParameters qp,List<String> deviceClass,List<String> deviceAreaCodes,List<String> deviceTypes,String expNameAndValue,String fileName,File dir) throws IOException, OaException;
	
	/**
	 * 初始化设备配置信息
	 * @return
	 */
	public void initConfigList(Device d);
	
	/**
	 * 检测设备是否属于同一资产类别
	 * @param ids
	 */
	public boolean validDeviceSameType(List<String> ids);
	
	/**
	 * 查询信息 带分页
	 */
	public ListPage<Device> getDeviceListPage(DeviceQueryParameters qp);
	
	public void endTask(String formID, WfmContext context);
	
	/**
	 * 根据设备id和设备状态结束对应的流程
	 * @param deviceId
	 * @param approveType
	 */
	public void txEndTaskByApproveType(String deviceId,Integer approveType);
	/**
	 * 结束审批流程
	 * @param deviceType
	 * @param areaCode
	 * @param deviceClass
	 * @param purpose
	 * @param userId
	 */
	public void txEndTaskByPurpose(String deviceType, String areaCode, String deviceClass, String purpose,String userId);
	
	/**
	 * 统计设备工作所在地所有台数
	 * @param deviceType
	 * @param areaCode
	 * @param deviceClass
	 * @param purpose
	 * @param userId
	 * @param manyTimesFlag
	 * @return
	 */
	public Map<String, Integer> getDeviceUseCount(String deviceType,String areaCode,String deviceClass, String purpose,String userId, boolean manyTimesFlag);
	
	/**
	 * 根据设备用途查询该设备用途是否存在或在对应的审批流程中，返回对应的提示信息 
	 * @param deviceType
	 * @param areaCode
	 * @param deviceClass
	 * @param deviceClassName
	 * @param purpose
	 * @param userId
	 * @param manyTimesFlag
	 * @param formType
	 * @param managerFlag
	 * @return
	 */
	public Map<String, String> getCheckDevicePurchase(String deviceType,String areaCode,String deviceClass, String deviceClassName, String purpose,String userId, Boolean manyTimesFlag,Integer formType,Boolean managerFlag);
}
