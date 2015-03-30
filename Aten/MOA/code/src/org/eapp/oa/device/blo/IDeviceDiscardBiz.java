package org.eapp.oa.device.blo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eapp.oa.device.dto.DevDiscardQueryParameters;
import org.eapp.oa.device.hbean.DevDiscardForm;
import org.eapp.oa.device.hbean.DiscardDevList;
import org.eapp.util.hibernate.ListPage;

import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.flow.hbean.Task;

/**
 * Description:设备报废业务逻辑层
 * 
 * @author sds
 * @version 2009-09-03
 */
public interface IDeviceDiscardBiz {
	/**
	 * 查询当前用户的设备报废单待办
	 * 
	 * @param userAccountId
	 * @return
	 */
	public List<DevDiscardForm> queryDealDiscardForm(String userAccountId);
	
	/**
	 * 查询当前用户的设备报废单归档
	 * 
	 * @param qp
	 * @param userAccountId
	 * @return
	 */
	public ListPage<DevDiscardForm> queryArchDiscardForm(DevDiscardQueryParameters qp, String userAccountId);
	
	/**
	 * 查询所有设备报废单归档
	 * 
	 * @param qp
	 * @return
	 */
	public ListPage<DevDiscardForm> queryAllArchedDiscardForm(DevDiscardQueryParameters qp);
	
	/**
	 * 根据ID获取设备报废单
	 * 
	 * @param id
	 * @param initList 是否初始化报废设备清单数据
	 * @return 
	 */
	public DevDiscardForm getDevDiscardFormById(String id, boolean initList);

	/**
	 * 删除设备报废单
	 * 
	 * @param id
	 * @throws OaException
	 */
	public void txDelDevDiscardForm(String id) throws OaException;

	/**
	 * 将设备报废单保存为草稿
	 * 
	 * @param form
	 * @param formType 
	 * @param isStartFlow
	 * @return
	 * @throws OaException
	 */
	public DevDiscardForm txSaveAsDraf(DevDiscardForm form, Integer formType, 
			boolean isStartFlow) throws OaException;
	
	/**
	 * 保存设备验收单
	 * 
	 * @param discardFormId
	 * @param devValidateFormId
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
	public void txSaveDiscardFormValidation(String discardFormId,String devValidateFormId, 
			String deviceID, int valiType, Date inDate, String accountID,
			Date valiDate, boolean isMoney, double deductMoney, String remark,
			String valiDetailStr);
	/**
	 * 获取已结束的任务列表
	 * 
	 * @param formId
	 * @return
	 */
	public List<Task> getEndedTasks(String formId) ;

	/**
	 * 处理审批任务
	 * 
	 * @param taskInstanceId
	 * @param comment
	 * @param transitionName
	 * @param formId
	 */
	public void txDealApproveTask(String taskInstanceId, String comment,
			String transitionName, String formId);


	/**
	 * 设备报废流程归档操作
	 * 
	 * @param formId
	 */
	public void txPublishDiscardForm(String formId);
	
	/**
	 * 设备报废单作废操作
	 * 
	 * @param formId
	 */
	public DevDiscardForm txCancellDiscardForm(String formId);
	
	
	/**
	 * 保存设备报废单
	 * @throws OaException
	 */
	public DevDiscardForm txSaveDevDiscardForm(DevDiscardForm devDiscardForm, Integer formType, String deviceId) throws OaException;

	/**
	 * 获取指定设备的已归档的设备报废列表
	 * @param deviceID
	 * @return
	 */
	public List<DiscardDevList> getArchDevScrapListByDeviceID(String deviceID, Boolean archiveDateOrder);
	
	/**
	 * 获取指定设备的已归档的设备报废单
	 * @param deviceID
	 * @return
	 */
	public List<DevDiscardForm> getArchDevDiscardFormDeviceID(String deviceID);
	
	/**
	 * 获取指定设备报废清单
	 * @param deviceID
	 * @return
	 */
	public DiscardDevList getDiscardDevListByID(String ID);
	
	/**
	 * 修改设备报废清单
	 * @param deviceID
	 * @return
	 * @throws OaException 
	 */
	public DevDiscardForm txUpdateDiscardDev(DevDiscardForm devDiscardForm, boolean addFlowVar) throws OaException;
	
	/**
	 * 修改设备报废清单
	 * @param deviceID
	 * @return
	 */
	public DevDiscardForm txUpdateDiscardByID(String id, Double workYear, Timestamp enterCompanyDate);
	
	/**
	 * 驳回到起草人修改
	 * @param id
	 * @param applyGroupName
	 * @param discardDevLists 新单的设备列表
	 * @return
	 * @throws OaException
	 */
	public DevDiscardForm txDraftmanAmend(String id, String applyGroupName, 
			Set<DiscardDevList> discardDevLists) throws OaException;
	
	/**
	 * 草稿修改
	 * @param id
	 * @param applyGroupName
	 * @param deviceTypeCode
	 * @param discardDevLists
	 * @param isStartFlow
	 * @return
	 */
	public DevDiscardForm txModifyDraftForm(String id, String applyGroupName, String deviceTypeCode, 
			Set<DiscardDevList> discardDevLists, boolean isStartFlow) throws OaException;
	/**
	 * 报废发送邮件
	 * @return
	 */
//	public void sendScrapEMail(DevDiscardForm discardForm) throws OaException;
	
	/**
	 * 离职处理发送邮件
	 * @param discardForm
	 * @param taskState 所在步骤 0 综合部复核；1 总裁审批.
	 * @throws OaException
	 */
//	public void sendLeaveEMail(DevDiscardForm discardForm,Integer taskState) throws OaException;
}
