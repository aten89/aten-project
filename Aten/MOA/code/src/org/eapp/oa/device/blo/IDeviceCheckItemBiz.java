package org.eapp.oa.device.blo;

import java.util.List;

import org.eapp.oa.device.hbean.DeviceCheckItem;
import org.eapp.oa.device.hbean.DeviceClass;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;

import org.eapp.oa.system.exception.OaException;

/**
 * Description:设备检查项业务逻辑层
 * 
 * @author shenyinjie
 * @version 2011-03-16
 */
public interface IDeviceCheckItemBiz {

	/**
	 * 查询设备检查项配置
	 * @return
	 */
	public ListPage<DeviceClass> queryDeviceClassPage(QueryParameters qp);

	public DeviceCheckItem getById(String id);

	public void txDelByDeviceClassID(String id);

	public void txSaveDeviceCheckItem(String deviceClassID, String remark, List<String> itemNames) throws OaException;

	public void txModifyDeviceCheckItem(String deviceClassID, String remark,
			List<String> itemNames) throws OaException;
	
	public DeviceClass queryCheckItemDeviceClass(String deviceClassID);
	
	public List<DeviceCheckItem> queryCheckItemByDeviceClassId(String deviceClassID);
}
