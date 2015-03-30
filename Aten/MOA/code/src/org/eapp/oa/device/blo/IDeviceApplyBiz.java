package org.eapp.oa.device.blo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eapp.oa.device.hbean.DevPurchaseForm;
import org.eapp.oa.device.hbean.DevPurchaseList;
import org.eapp.oa.device.hbean.PurchaseDevPurpose;
import org.eapp.oa.device.hbean.PurchaseDevice;

import org.eapp.oa.system.exception.OaException;
/**
 * @author tim
 *	领用单
 */
public interface IDeviceApplyBiz {
	
	
	/**
	 * 通过ID获取领用单
	 * @param id
	 * @param initList 是否初始化列表
	 * @return
	 */
	public DevPurchaseForm getDevUseApplyFormById(String id, boolean initList,boolean purpose);

	/**
	 * 删除领用单
	 * @param id
	 * @throws OaException
	 */
	public void txDelDevApplyForm(String id) throws OaException;
	
	/**
	 * 发布领用单
	 * @param id
	 * @param status
	 * @return
	 */
	public void txPublishApplyForm(String id);
	
	/**
	 * 设备申购完成发布设备信息
	 * @param applyFormID
	 * @throws OaException
	 */
	public void txPublishDeviceByApplyForm(String applyFormID) throws OaException;
	/**
	 * 报废领用单
	 * @param formId
	 */
	public void txCancellDevPurchaseForm(String formId);

	/**
	 * 新增领用。直接发布
	 * @param applyForm
	 * @param validMainDevFlag
	 * @return
	 * @throws OaException
	 */
	public DevPurchaseForm txAddDeviceApplyForm(DevPurchaseForm applyForm, boolean validMainDevFlag) throws OaException;
	
	/**
	 * 保存申购领用单
	 * @param deviceTypeCode
	 * @param userAccountID
	 * @param applyGroupName
	 * @param remark
	 * @param buyType
	 * @param budgetMoney
	 * @param formType
	 * @param devPurchaseList 领用设备列表
	 * @param purchaseDevices 申购设备列表
	 * @param isStartFlow
	 * @param validMainDevFlag 是否验证主设备
	 * @return
	 * @throws OaException
	 */
	public DevPurchaseForm txSaveAsDraf(String deviceTypeCode, String deviceClass, String userAccountID, String applyGroupName, 
			String remark, String buyType, Double budgetMoney, int formType, String areaCode,String workAreaCode,
			Set<DevPurchaseList> devPurchaseList, Set<PurchaseDevice> purchaseDevices,Set<PurchaseDevPurpose> purchaseDevPurposes,
			boolean isStartFlow, boolean validMainDevFlag) throws OaException;
	
	/**
	 * 获取指定设备的已归档领用记录列表
	 * @param deviceID
	 * @return
	 */
	public List<DevPurchaseList> getArchDevUseListByDeviceID(String deviceID, Boolean archiveDateOrder);
	
	/**
	 * 获取指定设备的已归档申购记录
	 * @param deviceID
	 * @return
	 */
	public PurchaseDevice getArchPurchaseDevByDeviceID(String deviceID, Boolean archiveDateOrder);
	
	/**
	 * 驳回到起草人修改
	 * @param id
	 * @param applyGroupName
	 * @param remark
	 * @param buyType
	 * @param budgetMoney
	 * @param devPurchaseLists 领用设备列表
	 * @param purchaseDevices 申购设备列表
	 * @param validMainDevFlag 是否验证主设备
	 * @return
	 * @throws OaException
	 */
	public DevPurchaseForm txDraftmanAmend(String id, String applyGroupName, 
			String remark, String buyType, Double budgetMoney,String workAreaCode,
			Set<DevPurchaseList> devPurchaseLists, Set<PurchaseDevice> purchaseDevices,Set<PurchaseDevPurpose> purchaseDevPurposes,
			boolean validMainDevFlag) throws OaException;
	
	/**
	 * 草稿修改
	 * @param id
	 * @param applyGroupName
	 * @param deviceTypeCode
	 * @param remark
	 * @param buyType
	 * @param budgetMoney
	 * @param formType
	 * @param devPurchaseLists 领用设备列表
	 * @param purchaseDevices 申购设备列表
	 * @param isStartFlow
	 * @param boolean validMainDevFlag 是否验证主设备
	 * @return
	 * @throws OaException
	 */
	public DevPurchaseForm txModifyDraftForm(String id, String applyGroupName, String deviceTypeCode, String deviceClass,
			String remark, String buyType, Double budgetMoney, int formType, String areaCode,String workAreaCode,
			Set<DevPurchaseList> devPurchaseLists, Set<PurchaseDevice> purchaseDevices,Set<PurchaseDevPurpose> purchaseDevPurposes, 
			boolean isStartFlow, boolean validMainDevFlag) throws OaException;
	/**
	 * 修改设备领用申购单 添加申购设备信息
	 * @param id
	 * @param purchaseDevices
	 */
	public void txUpdatePurchaseForm(String id,Set<PurchaseDevice> purchaseDevices)throws OaException ;
	
	/**
	 * 修改或保存申购设备验收单
	 * @param devValidateForms
	 */
	public void txSaveOrUpdateDevValidateForms(String id, Set<PurchaseDevice> purchaseDevices,Boolean bntFlag)throws OaException;
	
	/**
	 * 设备领用确认 保存设备工作用途和地区
	 * @param map
	 * @throws Exception 
	 * @throws OaException 
	 */
	public void txSaveOrUpdateDeviceForms(String id,Map<String,PurchaseDevice> map) throws OaException, Exception;
	
	public List<PurchaseDevice> getDeviceDealPurchaseByPurpose(String deviceType, String areaCode, String deviceClass, String userId);
	
	public List<DevPurchaseList> getDeviceDealUseByPurpose(String deviceType, String areaCode, String deviceClass, String userId);
	
	/**
	 * 修改设备领用申购单 添加申购设备是否扣款
	 * @param purchaseDevices
	 */
	public void txUpdatePurchaseDevices(Set<PurchaseDevice> purchaseDevices);
	/**
	 * 判断设备领用次数
	 * @param deviceTypeCode
	 * @param areaCode
	 * @param deviceClassId
	 * @param deviceClassName
	 * @param userId
	 * @param formType
	 * @param purchaseDevPurposes
	 * @param managerFlag
	 * @throws OaException
	 */
	public void checkDeviceMsg(String deviceTypeCode, String areaCode, String deviceClassId, String deviceClassName, String userId, Integer formType, Set<PurchaseDevPurpose> purchaseDevPurposes,Boolean managerFlag, boolean userFlag) throws OaException;
	
	/**
	 * 申购：判断用户是否已领用了不同种类的主设备
	 * @param userFlag true设备申购人
	 * @return
	 * @throws OaException
	 */
	public void getPurchaseMainDevCount(Set<PurchaseDevice> devPurchaseLists, String accountID, int formType, Set<String> deleteDeviceIDs,boolean userFlag) throws OaException;
	
	/**
	 * 根据申购设备清单ID获取申购清单实例
	 * @param purchaseDeviceID 申购设备清单ID
	 * @return
	 */
	public PurchaseDevice getByPurchaseDeviceId(String purchaseDeviceID);
}
