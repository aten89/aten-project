package org.eapp.blo;

import java.util.List;
import java.util.Map;

import org.eapp.exception.EappException;
import org.eapp.hbean.SubSystem;

/**
 * 子系统业务逻辑接口
 * @version 1.0
 */
public interface ISubSystemBiz {

	/**
	 * 获取所有的子系统列表
	 * 
	 * @return List<SubSystem> 子系统类型链表
	 */
	public List<SubSystem> getAllSubSystems();

	/**
	 * 根据子系统的ID获得子系统的信息
	 * 
	 * @param systemId 子系统ID
	 * @return SubSystem 子系统的实例信息
	 */
	public SubSystem getSubSystem(String systemId);

	/**
	 * 根据子系统ID删除子系统，如果系统关联子模块则删除失败
	 * 
	 * @param systemId 子系统ID
	 * @return ResponseData 操作结果信息 .equals(ResponseData.SUCCESS_CODE) 则操作成功， 否则操作失败通过getMessage()获取失败信息
	 */
	public SubSystem deleteSubSystem(String systemId) throws EappException;

	/**
	 * 保存新建的子系统
	 * 
	 * @param name 子系统名
	 * @param logoURL 图标LOGO
	 * @param ipAddress IP地址
	 * @param serverName 服务器名
	 * @param domainName 域名
	 * @param port 端口
	 * @param description 备注
	 * @return 保存后的子系统实例
	 */
	public SubSystem addSubSystem(String name, String logoURL, String ipAddress, String serverName,
			String domainName, int port, boolean isValid, String description) throws EappException;

	/**
	 * 修改子系统信息
	 * 
	 * @param subSystemId 子系统ID
	 * @param name 子系统名称
	 * @param logoURL 图标链接
	 * @param ipAddress IP地址
	 * @param serverName 服务器名
	 * @param domainName 域名
	 * @param port 端口
	 * @param description 备注
	 * @return 修改后的子系统实例
	 */
	public SubSystem modifySubSystem(String subSystemId, String name, String logoURL, String ipAddress,
			String serverName, String domainName, int port, boolean isValid, String description) throws EappException;
	
	/**
	 * 通过角色列表取得了权限的子系统
	 * @param roleIDs 角色列表
	 * @return
	 */
	public List<SubSystem> getHasRightSubSystems(List<String> roleIDs);
	
	/**
	 * 排序
	 * @param idsort
	 */
	public void modifySubSystemSort(Map<String, Integer> idsort);

}