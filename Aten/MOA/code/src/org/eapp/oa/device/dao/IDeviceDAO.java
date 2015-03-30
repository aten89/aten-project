
package org.eapp.oa.device.dao;

import java.sql.Timestamp;
import java.util.List;

import org.eapp.oa.device.dto.DeviceQueryParameters;
import org.eapp.oa.device.hbean.Device;
import org.eapp.oa.system.dao.IBaseHibernateDAO;
import org.eapp.util.hibernate.ListPage;


/**
 * Description: 设备DAO接口
 * @author 郑超
 */

public interface IDeviceDAO extends IBaseHibernateDAO {

	
	/**
	 * 通过主键查找 返回Device对象
	 */
	public Device findById(java.lang.String id);

	/**
	 * 
	 */
	public List<Device> findByExample(Device instance);

	/**
	 * 通过字段查找 返回List<Device>对象
	 */
	public List<Device> findByProperty(String propertyName, Object value);

	/**
	 * 查询所有数据信息 返回List<Device>
	 */
	public List<Device> findAll();

	/**
	 * 根据字段查询匹配信息 返回List<Device>
	 */
	public List<Device> findByDeviceNO(Object DeviceNO);
		
	public List<Device> findByDeviceClass(Object DeviceClass);
		
	public List<Device> findByDeviceType(Object DeviceType);
		
	public List<Device> findByIsAccessory(Object isAccessory);
		
	public List<Device> findByDeviceName(Object DeviceName);
		
	public List<Device> findByDeviceModel(Object DeviceModel);
		
	public List<Device> findByNumber(Object number);
		
	public List<Device> findByUseNumber(Object useNumber);
		
	public List<Device> findByDescription(Object description);
		
	public List<Device> findByStatus(Object status);
		
	public List<Device> findByIsUsing(Object isUsing);
		
	public List<Device> findByRegTime(Object regTime);
		
	public List<Device> findByBuyTime(Object buyTime);
		
	public List<Device> findByBuyType(Object buyType);
		
	public ListPage<Device> queryDeviceListPage(DeviceQueryParameters qp,List<String> deviceClass);
	
	public List<Device> queryDeviceBorrowInfo(String id, String deviceClass, Timestamp beginTime, Timestamp endTime);
	
	public int getDeviceNumByNo(String deviceNO,String areaCode,String classId);
	
	public int getDeviceNumByName(String areaCode,String classId,String deviceName);
	
	public List<Device> findUnbindITDevice(String deviceType,String deviceClass,String status,boolean isAccessory);
	
	/**
	 * 查找由applicantID指定的申请人所申请的设备
	 * @param applicantID 申请人ID
	 * @return
	 */
	public List<Device> findDevicesByApplicantID(String applicantID);
	
	/**
	 * 查找由applicantID指定的申请人所领用的设备，如果设备已报废，则不计入
	 * @param applicantID
	 * @return
	 */
	public List<Device> findUseDevicesByApplicantID(String applicantID);

	
	/**
	 * 搜索指定账户名下的设备.如果设备已删除、设备已报废、用户已离职则之前指定账户领用的设备不能算是其名下的设备.
	 * @param qp
	 * @param applicantID 账户ID
	 * @return
	 */
	public ListPage<Device> findDevicePageByApplicantID(DeviceQueryParameters qp, String applicantID);
	/**
	 * 获取设备流水号
	 * @param classID
	 * @param areaCode
	 * @return
	 */
	public int getDeviceSepNum(String classID,String areaCode);
	
	/**
	 * 搜索设备分页数据
	 * @param qp
	 * @param ownerAccountID 设备归属账户ID
	 * @param assignDevClassFlag 
	 * @param approvingFlag 设备是否处于审批中，null所有；true处于审批中；false不在审批中的
	 * @param mergeFlag 是否追加我名下的设备和闲置设备的结果集。因为正常情况下两者是互斥的。如果该值为true，则追加
	 * @param excludeScrapFlag 是否排除报废的
	 * @param deletedIDsAtReject 驳回修改时删除的ID列表
	 * @return 
	 */
	public ListPage<Device> findDevices(DeviceQueryParameters qp, String ownerAccountID, boolean assignDevClassFlag, List<String> assignDeviceClassIDs, 
			boolean mergeFlag, Boolean approvingFlag, Boolean excludeScrapFlag, List<String> deletedIDsAtReject);
	
	/**
	 * 根据设备id查找设备数据
	 * @param deviceIDs
	 * @return
	 */
	public List<Device> findDevicesByIDs(List<String> deviceIDs);
	
	public List<Device> findDeviceInfo(String deviceClass);
	
	public List<Device> findDevicesByApplicantIDAndClassID(String applicantID, String deviceClassID);
	
	public ListPage<Device> getStatisticsDeviceListPage(DeviceQueryParameters qp,List<String> deviceClass,List<String> deviceTypes,List<String> deviceAreaCodes);
	
	public ListPage<Device> getDeviceListPage(DeviceQueryParameters qp);
	
	public List<Device> getDeviceListByPuerpose(String deviceType,String areaCode,String deviceClass,String puerpose,String userId);
}

