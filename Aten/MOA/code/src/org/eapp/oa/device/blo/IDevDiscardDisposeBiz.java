package org.eapp.oa.device.blo;

import java.util.List;

import org.eapp.oa.device.hbean.DevDiscardDealForm;

import org.eapp.oa.system.exception.OaException;

/**
 * Description: IDevDiscardDisposeBiz 业务逻辑接口
 * 
 * @author shenyinjie
 */

public interface IDevDiscardDisposeBiz {
	
	/**
	 * 查找设备报废处理单
	 * @return
	 */
	public DevDiscardDealForm getById(String id);
	
	/**
	 * 新增报废设备处理
	 * @param form
	 * @return
	 * @throws OaException
	 */
	public DevDiscardDealForm txSaveForm(DevDiscardDealForm form) throws OaException;
	
	/**
	 * 根据设备id查询报废处理单
	 * @param deviceID
	 * @return
	 * @throws OaException
	 */
	public List<DevDiscardDealForm> queryDevDiscardDealFormByDeviceID(String deviceID);
	
}
