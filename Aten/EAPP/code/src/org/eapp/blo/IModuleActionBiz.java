/**
 * 
 */
package org.eapp.blo;

import java.util.List;

import org.eapp.dao.param.ModuleActionQueryParameters;
import org.eapp.hbean.Action;
import org.eapp.hbean.ModuleAction;
import org.eapp.util.hibernate.ListPage;


/**
 * 定义模块动作相关的业务方法
 * @author zsy
 * @version 
 */
public interface IModuleActionBiz {
	
	/**
	 * @author linliangyi
	 * 取得模块的所有动作
	 * @param moduleID 模块Key
	 * @return 动作
	 */
	public List<Action> getActionsByModuleID(String moduleID);	
	
	
	//add by zsy
	/**
	 * 通过条件取得所有的模块动作，并加载关联的Action对象
	 * @param qp 查询条件
	 * @return
	 */
	public ListPage<ModuleAction> queryModuleAction(ModuleActionQueryParameters qp);
	
	/**
	 * 设置模块的模块权限的有效性与是否为服务
	 * 模块ID为moduleID，模块动作ID在validIDs内的设为有效，否则设为无效
	 * 模块ID为moduleID，模块动作ID在rpcIDs内的设为服务，否则设非服务
	 * @param moduleID 模块ID
	 * @param validIDs 设置为有效的ID列表
	 * @param rpcIDs 设置为rpc服务的ID列表
	 * @param httpIDs 设置为http服务的ID列表
	 */
	public void addOptions(String moduleID, String[] validIDs, String[] rpcIDs, String[] httpIDs);
	
//	/**
//	 * 获取指定角色在指定模块中的权限,模块权限点必须是有效的
//	 * @param roleIDs 角色ID集合
//	 * @param moduleID 模块ID
//	 * @return Hessian调用时会抛出延迟加载异常，所以封装成DTO
//	 */
//	public List<ActionKey> csGetModuleActionByRoleIDs(List<String> roleIDs,String moduleID);
//	
//	/**
//	 * 获取指定服务在指定模块中的权限,模块权限点必须是有效的,而且是已开放服务
//	 * @param subSystemID 子系统ID
//	 * @param moduleKey 模块Key
//	 * @param roleIDs 服务ID集合
//	 * @return Hessian调用时会抛出延迟加载异常，所以封装成DTO
//	 */
//	public List<ActionKey> csGetModuleActionByServiceIDs(String subSystemID ,String moduleKey , List<String> serviceIDs);
//	
	/**
	 * 通过ID列表取得模块动作
	 * @param moduleActionIDs
	 * @return
	 */
	public List<ModuleAction> getModuleActions(String[] moduleActionIDs);
}
