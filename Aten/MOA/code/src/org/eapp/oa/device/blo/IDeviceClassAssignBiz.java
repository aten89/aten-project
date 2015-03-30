package org.eapp.oa.device.blo;

import java.util.List;

import org.eapp.oa.device.hbean.DeviceClassAssign;
import org.eapp.oa.device.hbean.DeviceClassAssignDetail;

import org.eapp.oa.system.exception.OaException;

/**
 * 授权，授权分为管理授权：0，查询统计授权：1，
 * 授权账户为用户：0，群组：1，职位：2
 * 设备类型授权
 * @author jiangxiongsheng
 *
 */
public interface IDeviceClassAssignBiz {
	
	/**
	 * 保存设备类别授权
	 * @param deviceClassAssign
	 */
	public DeviceClassAssign txSaveDeviceClassAssign(DeviceClassAssign deviceClassAssign);
	
	/**
	 * 保存设备类别授权
	 * @param deviceClassAssign
	 */
	public DeviceClassAssign txUpdateDeviceClassAssign(DeviceClassAssign deviceClassAssign)throws OaException ;
	
	/**
	 * 查询设备类别授权
	 * @return
	 */
	public List<DeviceClassAssign> queryDeviceClassAssignList();
	
	/**
	 * 删除设备类别授权
	 */
	public void txDeleteDeviceClassAssign(String id)throws OaException ;
	
	
	/**
	 * 取得绑定的用户
	 * @param id
	 * @return
	 */
	public List<DeviceClassAssignDetail> getBindingUsers(String id, Integer flag);
	/**
	 * 取得绑定的机构
	 * @param id
	 * @return
	 */
	public List<DeviceClassAssignDetail> getBindingGroups(String id, Integer flag);
	/**
	 * 取得绑定的职位
	 * @param id
	 * @return
	 */
	public List<DeviceClassAssignDetail> getBindingPosts(String id, Integer flag);
	/**
	 * 绑定用户
	 * @param id
	 * @param userIDs
	 */
	public void txBindingUsers(String id,String[] userIDs, Integer flag)throws OaException;
	/**
	 * 绑定机构
	 * @param id
	 * @param userIDs
	 */
	public void txBindingGroups(String id,String[] groupIDs, Integer flag)throws OaException;
	/**
	 * 绑定职位
	 * @param id
	 * @param userIDs
	 */
	public void txBindingPosts(String id,String[] postIDs, Integer flag)throws OaException;
	
	/**
	 * 批量删除
	 * @param id
	 */
	public void txBatchDeltete(String[] ids)throws OaException;
	
	
}
