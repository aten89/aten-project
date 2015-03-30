package org.eapp.dao;

import java.util.List;

import org.eapp.dao.param.RoleQueryParameters;
import org.eapp.hbean.Role;
import org.eapp.util.hibernate.ListPage;

/**
 * 角色管理DAO
 * @author zsy
 * @version
 */
public interface IRoleDAO extends IBaseHibernateDAO {

	public Role findById(java.lang.String id);

	public List<Role> findByRoleName(String roleName);
	
	/**
	 * 根据查询条件查询角色
	 * @param qp 查询条件
	 * @return ListPage -> dataList<Role> 对象
	 */
	public ListPage<Role> queryRole(RoleQueryParameters qp);
	
	/**
	 * 
	 * @param role
	 * @return
	 */
	public boolean existGroup(Role role);
	
	/**
	 * 
	 * @param role
	 * @return
	 */
	public boolean existUserAccount(Role role);
	
	/**
	 * 检查名称是否重复，若为空认为是重复
	 * @param name 服务名称
	 * @return
	 */
	public boolean checkRepetition(String name);
	
	/**
	 * 判断角色是否被绑定到其中一个群组
	 * @param roleID
	 * @param groupIDs
	 * @return
	 */
	public boolean isBindToGroups(String roleID, List<String> groupIDs);
}