package org.eapp.oa.device.blo;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eapp.oa.device.dto.DevAllocateQueryParameters;
import org.eapp.oa.device.hbean.DevAllocateForm;
import org.eapp.oa.device.hbean.DevAllocateList;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.util.hibernate.ListPage;

import org.eapp.oa.system.exception.OaException;
/**
 * @author tim
 *	调拨单
 */
public interface IDeviceAllocateBiz {
	
	/**
	 * 查询调拨单列表
	 * @param userAccountId
	 * @param formStatus
	 * @return
	 */
	public List<DevAllocateForm> queryAllocateForm(String userAccountId, int formStatus);
	
	/**
	 * 通过Id查询调拨单
	 * @param id
	 * @param initList
	 * @param initDevCfgList 是否初始化设备配置信息
	 * @return
	 */
	public DevAllocateForm getDevAllocateFormById(String id, boolean initList, boolean initDevCfgList);
	
	/**
	 * 删除调拨单
	 * @param id
	 * @param accountId
	 * @throws OaException
	 */
	public void txDelDevAllocateForm(String id) throws OaException;
	
	/**
	 * 待办列表
	 * @param userId
	 * @return
	 */
	public List<DevAllocateForm> queryDealingAllocateFrom(String userId);
	
	/**
	 * 获取任务
	 * @param formId
	 * @return
	 */
	public List<Task> getEndedTasks(String formId) ;
	
	/**
	 * 处理提交的流程任务
	 * @param taskInstanceId
	 * @param comment
	 * @param transitionName
	 * @param formId
	 */
	public void txDealApproveTask(String taskInstanceId, String comment, String transitionName, String formId);
	
	/**
	 * 归档列表
	 * @param qp
	 * @param userAccountId
	 * @return
	 */
	public ListPage<DevAllocateForm> getArchAllocateForm(DevAllocateQueryParameters qp,
			String userAccountId) ;
	
	/**
	 * 验收单保存
	 * @param allocateId
	 * @param valiId
	 * @param deviceID
	 * @param valiType
	 * @param inDate
	 * @param accountID
	 * @param valiDate
	 * @param isMoney
	 * @param deductMoney
	 * @param remark
	 * @param valiDetailStr
	 */
	public void txSaveValidateForm(String allocateId, String valiId, String deviceID,
			int valiType, Date inDate, String accountID,
			Date valiDate, boolean isMoney, double deductMoney, String remark,
			String valiDetailStr);
	
	/**
	 * 启动流程
	 * @param devAlcForm
	 * @param flowKey
	 * @throws OaException
	 */
	public void txStartFlow(DevAllocateForm devAlcForm, String flowKey) throws OaException ;
	
	/**
	 * 流程归档操作
	 * @param id
	 * @param inUser
	 * @param outUserId
	 * @param status
	 * @return
	 * @throws OaException
	 */
	public DevAllocateForm txPublishAllocateForm(String id) throws OaException;
	
	/**
	 * 作废操作
	 * @param formId
	 * @throws OaException
	 */
	public void txCancellAllocateForm(String formId)throws OaException ;
	
	/**
	 * 查询列表
	 * @param qp
	 * @return
	 */
	public ListPage<DevAllocateForm> getQueryAllocateForm(DevAllocateQueryParameters qp);
	
	/**
	 * 管理员保存调拨单
	 * @param allocateForm
	 * @param deviceId 设备id
	 * @param purpose 设备用途
	 * @param validMainDevFlag 是否验证主设备
	 * @return
	 * @throws OaException
	 */
	public DevAllocateForm txSaveDevAllocateForm(DevAllocateForm allocateForm,String deviceId, boolean validMainDevFlag) throws OaException;
	
	/**
	 * 获取指定设备的已归档调拨记录列表
	 * @param deviceID
	 * @return
	 */
	public List<DevAllocateList> getArchDevAllotListByDeviceID(String deviceID, Boolean archiveDateOrder);
	
	/**
	 * 起草调拨
	 * @param deviceTypeCode
	 * @param allotType
	 * @param moveDate
	 * @param applicantID
	 * @param userGroupName
	 * @param inAccountID
	 * @param inGroupName
	 * @param reason
	 * @param devAllocateList
	 * @param isStartFlow
	 * @param validMainDevFlag 是否验证主设备
	 * @return
	 * @throws OaException
	 */
	public DevAllocateForm txSaveAsDraf(String deviceTypeCode, String allotType, Date moveDate, 
			String applicantID, String userGroupName, String inAccountID, String inGroupName, String reason,  
			Set<DevAllocateList> devAllocateList, boolean isStartFlow, boolean validMainDevFlag) throws OaException;
	
	/**
	 * 草稿修改
	 * @param id
	 * @param deviceTypeCode
	 * @param allotType
	 * @param moveDate
	 * @param inAccountID
	 * @param inGroupName
	 * @param reason
	 * @param devAllocateList
	 * @param isStartFlow
	 * @param validMainDevFlag 是否验证主设备
	 * @return
	 * @throws OaException
	 */
	public DevAllocateForm txModifyDraftForm(String id, String deviceTypeCode, String allotType, Date moveDate, 
			String inAccountID, String inGroupName, String reason,  
			Set<DevAllocateList> devAllocateList, boolean isStartFlow, boolean validMainDevFlag) throws OaException;
	
	/**
	 * 驳回到起草人修改
	 * @param id
	 * @param allotType
	 * @param moveDate
	 * @param inAccountID
	 * @param inGroupName
	 * @param reason
	 * @param devAllocateList
	 * @param validMainDevFlag 是否验证主设备
	 * @return
	 * @throws OaException
	 */
	public DevAllocateForm txDraftmanAmend(String id, String allotType, Date moveDate, 
			String inAccountID, String inGroupName, String reason,  
			Set<DevAllocateList> devAllocateList, boolean validMainDevFlag) throws OaException;
	
	/**
	 * 设备调入 保存设备工作用途和地区
	 * @param map
	 * @throws OaException 
	 */
	public void txSaveOrUpdateDevAllocateForm(String id,Map<String,DevAllocateList> map,Boolean valiDevFlag) throws OaException;

}
