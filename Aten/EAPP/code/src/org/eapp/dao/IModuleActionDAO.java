package org.eapp.dao;

import java.util.List;

import org.eapp.dao.param.ModuleActionQueryParameters;
import org.eapp.dto.ActionKey;
import org.eapp.hbean.ModuleAction;
import org.eapp.util.hibernate.ListPage;

/**
 * 
 * @author zsy
 * @version
 */
public interface IModuleActionDAO extends IBaseHibernateDAO {

	public ModuleAction findById(java.lang.String id);

	public List<ModuleAction> findByModuleID(String moduleID);
	
	public void deleteByIds(String moduleID,List<String> ids);
	
	
	/**
	 * 设置在给定模块内的指定模块动作为有效，其余的设为无效
	 * 若moduleActionIDs为空，所有设为无效
	 * @param moduleID 模块ID
	 * @param moduleActionIDs 模块动作ID
	 */
	public void setValid(String moduleID, String[] moduleActionIDs);
	
	/**
	 * 设置在给定模块内的指定模块动作为RPC服务，其余的设为非RPC服务
	 * 若moduleActionIDs为空，所有设为非RPC服务
	 * @param moduleID 模块ID
	 * @param moduleActionIDs 模块动作ID
	 */
	public void setRPC(String moduleID, String[] moduleActionIDs);
	
	/**
	 * 设置在给定模块内的指定模块动作为HTTP服务，其余的设为非HTTP服务
	 * 若moduleActionIDs为空，所有设为非HTTP服务
	 * @param moduleID 模块ID
	 * @param moduleActionIDs 模块动作ID
	 */
	public void setHTTP(String moduleID, String[] moduleActionIDs);
	
	/**
	 * 获取指定角色在指定模块中的权限,模块权限点必须是有效的
	 * @param roleIDs 角色ID集合
	 * @param moduleID 模块ID
	 * @return Hessian调用时会抛出延迟加载异常，所以封装成DTO
	 */
	public List<ActionKey> getModuleActionByRoleIDs(List<String> roleIDs,String moduleID);
	
	/**
	 * 获取指定服务在指定模块中的权限,模块权限点必须是有效的,而且是已开放服务
	 * @param roleIDs 服务ID集合
	 * @param moduleID 模块ID
	 * @return Hessian调用时会抛出延迟加载异常，所以封装成DTO
	 */
	public List<ActionKey> getModuleActionByServiceIDs(List<String> serviceIDs, String moduleID);
	
	/**
	 * 通过条件取得所有的模块动作，并加载关联的Action对象
	 * @param qp 查询条件
	 * @return
	 */
	public ListPage<ModuleAction> queryModuleAction(ModuleActionQueryParameters qp);
	
	/**
	 * 获取指定模块中的所有权限
	 * @param moduleID 模块ID
	 * @return
	 */
	public List<ActionKey> getModuleActionByModuleID(String moduleID);
	
	/**
	 * 通过子系统ID与模块Key取得模块ID
	 * @param moduleKey 模块Key
	 * @param systemID 子系统ID
	 * @return
	 */
	public String getModuleID(String systemID, String moduleKey);
}