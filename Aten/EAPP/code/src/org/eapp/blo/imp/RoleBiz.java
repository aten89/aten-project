/**
 * 
 */
package org.eapp.blo.imp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eapp.blo.IRoleBiz;
import org.eapp.dao.IModuleActionDAO;
import org.eapp.dao.IGroupDAO;
import org.eapp.dao.IRoleDAO;
import org.eapp.dao.IUserAccountDAO;
import org.eapp.dao.param.RoleQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.ModuleAction;
import org.eapp.hbean.Group;
import org.eapp.hbean.Role;
import org.eapp.hbean.UserAccount;
import org.eapp.sys.config.SysConstants;
import org.eapp.util.hibernate.ListPage;


/**
 * @author zsy
 * @version 
 */
public class RoleBiz implements IRoleBiz {

	private IRoleDAO roleDAO;
	private IGroupDAO groupDAO;
	private IModuleActionDAO moduleActionDAO;
	private IUserAccountDAO userAccountDAO;

	public void setRoleDAO(IRoleDAO roleDAO) {
		this.roleDAO = roleDAO;
		
	}
	
	/**
	 * @param groupDAO the groupDAO to set
	 */
	public void setGroupDAO(IGroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	/**
	 * @param userAccountDAO the userAccountDAO to set
	 */
	public void setUserAccountDAO(IUserAccountDAO userAccountDAO) {
		this.userAccountDAO = userAccountDAO;
	}

	/**
	 * @param moduleActionDAO the moduleActionDAO to set
	 */
	public void setModuleActionDAO(IModuleActionDAO moduleActionDAO) {
		this.moduleActionDAO = moduleActionDAO;
	}
	
	@Override
	public ListPage<Role> queryRole(RoleQueryParameters qp) {
		if (qp == null) {
			throw new IllegalArgumentException();
		}
		return roleDAO.queryRole(qp);
	}

	@Override
	public List<Role> deleteRoles(String[] roleIDs) throws EappException {
		 List<Role> roles = new ArrayList<Role>();
		if (roleIDs == null || roleIDs.length < 1) {
			return roles;
		}
		Role role = null;
		for (String id : roleIDs) {
			if (StringUtils.isBlank(id)) {
				continue;
			}
			id = id.trim();
			if (SysConstants.ROLE_ADMIN.equals(id)) {
				throw new EappException("不能删除系统级管理员角色");
			}
			if (SysConstants.ROLE_BASE.equals(id)) {
				throw new EappException("不能删除系统级用户基础角色");
			}
			role = roleDAO.findById(id);
			if (role == null) {
				continue;
			}
			if (roleDAO.existGroup(role)) {
				throw new EappException("角色”" + role.getRoleName() + "“已经被机构关联");
			}
			if (roleDAO.existUserAccount(role)) {
				throw new EappException("角色”" + role.getRoleName() + "“已经被用户关联");
			}
			roleDAO.delete(role);
			roles.add(role);
		}
		return roles;
	}

	@Override
	public Role addRole(String roleName, boolean isValid, 
			String description) throws EappException{
		if (StringUtils.isBlank(roleName)) {
			throw new IllegalArgumentException();
		}
		roleName = roleName.trim();
		if (roleDAO.checkRepetition(roleName)) {
			throw new EappException("角色名称已存在");
		}
		Role role = new Role();
		role.setRoleName(roleName);
		role.setIsValid(isValid);
		role.setDescription(description);
		roleDAO.save(role);
		return role;
	}
	
	@Override
	public Role modifyRole(String roleID, String roleName, boolean isValid, 
			String description)  throws EappException{
		if (StringUtils.isBlank(roleID) || StringUtils.isBlank(roleName)) {
			throw new IllegalArgumentException();
		}
		Role role = roleDAO.findById(roleID);
		if (role == null)  {
			throw new IllegalArgumentException("roleID的对象不存在");
		}
		roleName = roleName.trim();
		if (!roleName.equals(role.getRoleName())) {
			if (roleDAO.checkRepetition(roleName)) {
				throw new EappException("角色名称已存在");
			}
		}
		role.setRoleName(roleName);
		role.setIsValid(isValid);
		role.setDescription(description);
		roleDAO.update(role);
		return role;
	}
	
	@Override
	public Role getRoleByID(String roleID) {
		if (StringUtils.isBlank(roleID)) {
			throw new IllegalArgumentException();
		}
		return roleDAO.findById(roleID);
	}
	
	@Override
	public Role getRoleByName(String roleName) {
		if (StringUtils.isBlank(roleName)) {
			throw new IllegalArgumentException();
		}
		List<Role> roles = roleDAO.findByRoleName(roleName);
		if(roles == null || roles.isEmpty()) {
			return null;
		}
		Role role = roles.get(0);
		role.getUserAccounts().size();//加载延迟内容
		return role;
	}

	@Override
	public Role txBindGroup(String roleID, String[] groupIDs) {
		if (StringUtils.isBlank(roleID)) {
			throw new IllegalArgumentException();
		}
		Role role = roleDAO.findById(roleID);
		if (role == null)  {
			throw new IllegalArgumentException("roleID的对象不存在");
		}
		HashSet<Group> set = new HashSet<Group>();
		Group group = null;
		if (groupIDs != null && groupIDs.length > 0) {
			for (String gid : groupIDs) {
				if (StringUtils.isBlank(gid)) {
					continue;
				}
				group = groupDAO.findById(gid);
				if (group != null) {
					set.add(group);
				}
			}
		}
		role.setGroups(set);
		roleDAO.update(role);
		return role;
	}
	
	@Override
	public Role txBindUser(String roleID, String[] userIDs) throws EappException {
		if (StringUtils.isBlank(roleID)) {
			throw new IllegalArgumentException();
		}
		Role role = roleDAO.findById(roleID);
		if (role == null)  {
			throw new IllegalArgumentException("roleID的对象不存在");
		}
		boolean isAdminRole = SysConstants.ROLE_ADMIN.equals(roleID.trim());
		HashSet<UserAccount> set = new HashSet<UserAccount>();
		UserAccount ua = null;
		if (userIDs != null && userIDs.length > 0) {
			boolean finded = false;
			for (String uid : userIDs) {
				if (StringUtils.isBlank(uid)) {
					continue;
				}
				ua = userAccountDAO.findById(uid);
				if (ua != null) {
					if (isAdminRole) {
						if (SysConstants.USER_ACCOUNT_ADMIN.equals(ua.getAccountID())) {
							finded = true;
						}
					}
					set.add(ua);
				}
				if (isAdminRole && !finded) {
					throw new EappException("超级管理员角色不能删除管理员帐号");
				}
			//	set.add(new UserAccount(uid));
			}
		}
		role.setUserAccounts(set);
		roleDAO.update(role);
		return role;
	}

	@Override
	public Role txBindRight(String roleID, String[] moduleActionIDs) throws EappException {
		if (StringUtils.isBlank(roleID)) {
			throw new IllegalArgumentException();
		}
		if (SysConstants.ROLE_ADMIN.equals(roleID.trim())) {
			throw new EappException("不能修改系统级管理员角色绑定的动作");
		}
		Role role = roleDAO.findById(roleID);
		if (role == null)  {
			throw new IllegalArgumentException("roleID的对象不存在");
		}
		HashSet<ModuleAction> set = new HashSet<ModuleAction>();
		ModuleAction ma = null;
		if (moduleActionIDs != null && moduleActionIDs.length > 0) {
			for (String uid : moduleActionIDs) {
				if (StringUtils.isBlank(uid)) {
					continue;
				}
				ma = moduleActionDAO.findById(uid);
				if (ma != null) {
					set.add(ma);
				}
			//	set.add(new ModuleAction(uid));
			}
		}
		role.setModuleActions(set);
		roleDAO.update(role);
		return role;
	}

	@Override
	public Set<Group> getBindedGroups(String roleID) {
		if (StringUtils.isBlank(roleID)) {
			return null;
		}
		Role role = roleDAO.findById(roleID);
		if (role == null) {
			return null;
		}
		Set<Group> gs = role.getGroups();
		gs.size();//加载延迟
//		for (Group g : gs) {
//			initLazy(g, 50);
//		}
		return gs;
	}

//	private void initLazy(Group g, int breakTimes) {
//		if (g == null || breakTimes-- < 0) {
//			return;
//		}
//		Hibernate.initialize(g.getParentGroup());
//		initLazy(g.getParentGroup(), breakTimes);
//	}
	
	@Override
	public Set<ModuleAction> getBindedRights(String roleID) {
		if (StringUtils.isBlank(roleID)) {
			return null;
		}
		Role role = roleDAO.findById(roleID);
		if (role == null) {
			return null;
		}
		Set<ModuleAction> rights = role.getModuleActions();
		rights.size();//加载延迟
		return rights;
	}

	@Override
	public Set<UserAccount> getBindedUsers(String roleID) {
		if (StringUtils.isBlank(roleID)) {
			return null;
		}
		Role role = roleDAO.findById(roleID);
		if (role == null) {
			return null;
		}
		for (UserAccount user : role.getUserAccounts()) {
			user.getGroups().size();//加载延迟内容
		}
		return role.getUserAccounts();
	}

	@Override
	public boolean getIsManageGroupRole(String roleID, List<String> postIDs) {
		if (StringUtils.isBlank(roleID) || postIDs == null) {
			return false;
		}
		//通过职位取得有管理权限的部门
		List<Group> groups = groupDAO.getGroupsByManagerPost(postIDs);
		if (groups == null || groups.size() < 1) {
			return false;
		}
		List<String> groupIDs = new ArrayList<String>();
		for (Group g : groups) {
			groupIDs.add(g.getGroupID());
		}
		return roleDAO.isBindToGroups(roleID, groupIDs);
	}
}
