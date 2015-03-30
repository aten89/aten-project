/**
 * 
 */
package org.eapp.blo;

import java.util.List;
import java.util.Set;

import org.eapp.dao.param.RoleQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.ModuleAction;
import org.eapp.hbean.Group;
import org.eapp.hbean.Role;
import org.eapp.hbean.UserAccount;
import org.eapp.util.hibernate.ListPage;


/**
 * 角色管理相关的业务方法
 * @author zsy
 * @version 
 */
public interface IRoleBiz {

	/**
	 * 根据查询条件查询角色
	 * @param qp 查询条件
	 * @return ListPage 对象
	 */
	public ListPage<Role> queryRole(RoleQueryParameters qp);
	
	/**
	 * 根据ID删除角色，
	 * 若角色已被用户或群组绑，则禁止删除
	 * @param roleIDs 角色ID
	 * @throws RelatedException 该角色已被用户或群组绑定时抛出
	 */
	public List<Role> deleteRoles(String[] roleIDs) throws EappException;
	
	/**
	 * 新增角色
	 * @param roleName 角色名称,不能为空
	 * @param isValid 是否有效
	 * @param description 描述
	 */
	public Role addRole(String roleName, boolean isValid, 
			String description) throws EappException;
	
	/**
	 * 修改角色信息，不会修改其关联集合部分
	 * @param roleID 角色ID
	 * @param roleName 角色名称,不能为空
	 * @param isValid 是否有效
	 * @param description 描述
	 */
	public Role modifyRole(String roleID, String roleName, boolean isValid, 
			String description) throws EappException;
	
	/**
	 * 通过角色ID取得实例
	 * @param roleID 角色ID
	 * @return
	 */
	public Role getRoleByID(String roleID);
	
	/**
	 * 通过Name取得角色
	 * @param roleName 角色
	 * @return
	 */
	public Role getRoleByName(String roleName);
	
	/**
	 * 绑定群组到角色
	 * 群组ID对应的群组记录必须存在否则抛出异常
	 * @param roleID 角色ID
	 * @param groupIDs 群组ID
	 */
	public Role txBindGroup(String roleID, String[] groupIDs);
	
	/**
	 * 绑定用户到角色
	 * 用户ID对应的用户记录必须存在否则抛出异常
	 * @param roleID 角色ID
	 * @param groupIDs 用户ID
	 */
	public Role txBindUser(String roleID, String[] userIDs) throws EappException;
	
	/**
	 * 绑定模块动作到角色
	 * @param roleID 角色ID
	 * @param moduleActionIDs 模块动作ID
	 */
	public Role txBindRight(String roleID, String[] moduleActionIDs) throws EappException;
	
	/**
	 * 取得已绑定到角色的群组
	 * @param roleID 角色ID
	 * @return
	 */
	public Set<Group> getBindedGroups(String roleID);
	
	/**
	 * 取得已绑定到角色的用户
	 * @param roleID 角色ID
	 * @return
	 */
	public Set<UserAccount> getBindedUsers(String roleID);
	
	/**
	 * 取得已绑定到角色的模块动作
	 * @param roleID 角色ID
	 * @return
	 */
	public Set<ModuleAction> getBindedRights(String roleID);
	
	
	/**
	 * 判断此角色是否被绑定到该用户管理的部门下
	 * @param roleID 角色ID
	 * @param postIDs 部门管理者职位
	 * @return
	 */
	public boolean getIsManageGroupRole(String roleID, List<String> postIDs);
}
