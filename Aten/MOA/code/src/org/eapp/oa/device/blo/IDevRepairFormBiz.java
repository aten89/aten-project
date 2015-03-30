package org.eapp.oa.device.blo;

import java.util.List;

import org.eapp.oa.device.dto.DeviceSimpleQueryParameters;
import org.eapp.oa.device.hbean.DevRepairForm;
import org.eapp.util.hibernate.ListPage;

import org.eapp.oa.system.exception.OaException;

public interface IDevRepairFormBiz {
	/**
	 * 获取指定设备的维修记录列表
	 * @param deviceID
	 * @return
	 */
	public List<DevRepairForm> findByDeviceID(String deviceID, Boolean createTimeOrder);
	
	/**
	 * 根据ID获取维修单
	 * 
	 * @param id
	 * @return
	 */
	public DevRepairForm getDevRepairFormById(String id);

	/**
	 * 新增设备维修单
	 */
	public DevRepairForm txAddDevRepairForm(DevRepairForm devRepairForm) throws OaException;

	/**
	 * 修改设备维修单
	 */
	public void txModifyDevRepairForm(DevRepairForm devRepairForm) throws OaException;
	/**
	 * 删除维修单
	 * 
	 * @param id
	 * @param accountId
	 */
	void txDelDevRepairForm(String id);

	/**
	 * 填写维修说明
	 * 
	 * @param id
	 * @param remark
	 * @param operator 操作人
	 */
	public void txWriteRemark(String id, String remark);
	/**
	 * 分页模糊条件查询 默认查询所有数据
	 * 
	 * @param qp
	 * @return
	 */
	public ListPage<DevRepairForm> queryDevRepairForm(
			DeviceSimpleQueryParameters qp);

}
