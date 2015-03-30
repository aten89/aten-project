package org.eapp.oa.device.dao;

import java.util.List;

import org.eapp.oa.device.hbean.DevValidateForm;

import org.eapp.oa.system.dao.IBaseHibernateDAO;


/**
 * Description: DevValidateFormDAO 数据访问对象接口
 * @author 郑超
 */

public interface IDevValidateFormDAO extends IBaseHibernateDAO {

	
	/**
	 * 通过主键查找 返回DevValidateForm对象
	 */
	public DevValidateForm findById(java.lang.String id);

	/**
	 * 
	 */
	public List<DevValidateForm> findByExample(DevValidateForm instance);

	/**
	 * 查询所有数据信息 返回List<DevValidateForm>
	 */
	public List<DevValidateForm> findAll();

	/**
	 * 根据字段查询匹配信息 返回List<DevValidateForm>
	 */
	
	public List<DevValidateForm> findByDeviceIdAndValiType(Integer valiType, String deviceID);

	/**
	 * 查找validType类型的验收单中处于当前年份的最大验收单编号
	 * @param validType　验收单类型
	 * @param year 编号年份
	 */
	public int findCurrentYearValidFormNO(int validType, int year);

	/**
	 * 根据申购设备清单ID获取验收单
	 * @param purchaseDevID 申购设备清单ID，注意：不是申购设备的ID
	 * @return
	 */
	public DevValidateForm findByPurchaseDevId(String purchaseDevID);
	
}

