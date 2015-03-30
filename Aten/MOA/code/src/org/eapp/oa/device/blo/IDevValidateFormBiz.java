package org.eapp.oa.device.blo;

import java.util.Date;
import java.util.List;

import org.eapp.oa.device.hbean.DevValidateForm;


/**
 * 设备验收单 业务逻辑接口
 * 
 * @author sds
 * @version Aug 7, 2009
 */
public interface IDevValidateFormBiz {

	/**
	 * 新增设备验收单
	 * @param deviceID
	 * @param valiType
	 * @param inDate
	 * @param accountID
	 * @param valiDate
	 * @param isMoney
	 * @param deductMoney
	 * @param remark
	 * @param valiDetailStr
	 * @return
	 */
	public DevValidateForm txSaveDevValidateForm(String id, String deviceID,
			int valiType, Date inDate, String accountID,
			Date valiDate, boolean isMoney, double deductMoney, String remark,
			String valiDetailStr);

	/**
	 * 删除设备验收单
	 * 
	 * @param devValidateForm
	 * @return
	 */
	public DevValidateForm txDeleteDevValidateForm(String id);

	/**
	 * 通过ID查找
	 * 
	 * @param id
	 * @return
	 */
	public DevValidateForm getDevValidateFormById(String id);

	/**
	 * 根据申购设备清单ID获取验收单
	 * 
	 * @param purchaseDevID 申购设备清单ID 注意：不是申购设备ID
	 * @return
	 */
	public DevValidateForm getDevValidateFormByPurchaseDevId(String purchaseDevID);
	/**
	 * 取得设备的验证单
	 * @param deviceID
	 * @param valiType
	 * @return
	 */
	public List<DevValidateForm> getDevValidateForms(String deviceID, Integer valiType);
	
	/**
	 * 获取属于validType类型的验收单中指定年份的最大流水号
	 * @param validType
	 * @param year
	 * @return
	 */
	public int getCurrentYearValidFormNO(int validType, int year);

}
