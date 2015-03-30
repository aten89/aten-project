package org.eapp.oa.device.dao;

import java.util.List;

import org.eapp.oa.device.hbean.DevPurchaseForm;
import org.eapp.oa.device.hbean.DevPurchaseList;
import org.eapp.oa.device.hbean.PurchaseDevice;
import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IDevUseApplyFormDAO extends IBaseHibernateDAO {

	public DevPurchaseForm findById(java.lang.String id);

	public int findCurrentYearFormNO(int year);
	
	/**
	 * 根据设备ID查询已归档的设备领用记录列表(含调拨)
	 * @param deviceID
	 * @return
	 */
	public List<DevPurchaseList> queryArchDevUseListByDeviceID(String deviceID, Boolean archiveDateOrder);
	
	/**
	 * 根据设备ID查询已归档的设备申购记录
	 * @param deviceID
	 * @param archiveDateOrder
	 * @return
	 */
	public PurchaseDevice queryArchPurchaseDevByDeviceID(String deviceID, Boolean archiveDateOrder);
	
	/**
	 * 根据设备id查询申购设备
	 * @param id
	 * @return
	 */
	public PurchaseDevice findByPurchaseDeviceId(java.lang.String id);
	
	public List<DevPurchaseForm> queryDealDevPurchaseFormByDeviceID(String deviceID,Integer formType);
	
	public List<DevPurchaseList> getDeviceDealUseByPurpose(String deviceType, String areaCode, String deviceClass, String userId);
	
	public List<DevPurchaseForm> getDevPurchaseFormDealUseByPurpose(String deviceType, String areaCode, String deviceClass, String userId);
	
	/**
	 * 搜索由账号ID申请的正在办理中的申请单
	 * @param accountID 
	 * @param formType 申请单类型
	 * @return
	 */
	public List<DevPurchaseForm> findDealingFormByApplicantID(String accountID, Integer formType);
	
	public List<PurchaseDevice> getDeviceDealPurchaseByPurpose(String deviceType, String areaCode, String deviceClass, String userId);
	
	public List<DevPurchaseForm> getDevPurchaseFormDealPurchaseByPurpose(String deviceType, String areaCode, String deviceClass, String userId);
	
	


}