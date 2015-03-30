package org.eapp.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IGroupBiz;
import org.eapp.blo.IRoleBiz;
import org.eapp.comobj.SessionAccount;
import org.eapp.dao.param.RoleQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.ModuleAction;
import org.eapp.hbean.Group;
import org.eapp.hbean.Role;
import org.eapp.hbean.UserAccount;
import org.eapp.sys.config.SysConstants;
import org.eapp.util.hibernate.ListPage;

/**
 * 处理角色管理的请求
 * @author zsy
 * @version
 */
public class RoleAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 894932421380316916L;
	private static final Log log = LogFactory.getLog(RoleAction.class);
	
	private IRoleBiz roleBiz;
	private IGroupBiz groupBiz;
	
	private int pageNo;
	private int pageSize;
	private String roleID;
	private String roleName;
	private String isValid;
	private String description;
	private String[] roleIDs;
	private String[] groupIDs;
	private String[] userIDs;
	private String[] moduleActionIDs;
	
	private List<Role> roles;
	private ListPage<Role> rolePage;
	private Role role;
	private Set<Group> groups;
	private Set<UserAccount> userAccounts;
	private Set<ModuleAction> moduleActions;
	
	public ListPage<Role> getRolePage() {
		return rolePage;
	}

	public Role getRole() {
		return role;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public Set<UserAccount> getUserAccounts() {
		return userAccounts;
	}

	public Set<ModuleAction> getModuleActions() {
		return moduleActions;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setRoleIDs(String[] roleIDs) {
		this.roleIDs = roleIDs;
	}

	public void setGroupIDs(String[] groupIDs) {
		this.groupIDs = groupIDs;
	}

	public void setUserIDs(String[] userIDs) {
		this.userIDs = userIDs;
	}

	public void setModuleActionIDs(String[] moduleActionIDs) {
		this.moduleActionIDs = moduleActionIDs;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoleBiz(IRoleBiz roleBiz) {
		this.roleBiz = roleBiz;
	}

	public void setGroupBiz(IGroupBiz groupBiz) {
		this.groupBiz = groupBiz;
	}

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	//初始化编辑页面
	public String initFrame() {
		return success();
	}
	
	public String initQuery() {
		return success();
	}
	
	public String queryRole() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登录");
		}
		RoleQueryParameters roleQP = new RoleQueryParameters();
		roleQP.setPageNo(pageNo);
		roleQP.setPageSize(pageSize);
		
		if (StringUtils.isNotBlank(roleName)) {
			roleQP.setRoleName(roleName);
		}
		if (StringUtils.isNotBlank(isValid)) {
			roleQP.SetIsValid(new Boolean("Y".equals(isValid)));
		}
		
		if (!user.getRoleIDs().contains(SysConstants.ROLE_ADMIN)) {
			//如果不是超管，只列出他管理的部门的角色
			List<Group> groups = groupBiz.getGroupsByManagerPost(user.getPostIDs());
			if (groups != null) {
				List<String> groupIDs = new ArrayList<String>();
				for (Group g : groups) {
					groupIDs.add(g.getGroupID());
				}
				roleQP.setGroupIDs(groupIDs);
			}
		}
		roleQP.addOrder("roleName", true);
		try {
			rolePage = roleBiz.queryRole(roleQP);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	public String deleteRoles() {
		if (roleIDs == null || roleIDs.length < 1) {
			return error("参数不能为空");
		}
		try {
			List<Role> roles = roleBiz.deleteRoles(roleIDs);
			//写入日志
			if (roles != null) {
				for (Role r : roles) {
					ActionLogger.log(getRequest(), r.getRoleID(), r.toString());
				}
			}
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initAdd() {
		return success();
	}
	
	public String addRole() {
		if (StringUtils.isBlank(roleName)) {
			return error("参数不能为空");
		}
		try {
			Role role = roleBiz.addRole(roleName, "Y".equals(isValid), description);
			//写入日志
			if (role != null) {
				ActionLogger.log(getRequest(),role.getRoleID(), role.toString());
			}
			return success(role.getRoleID());
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initModify() {
		if (StringUtils.isBlank(roleID)) {
			return error("参数不能为空");
		}
		role = roleBiz.getRoleByID(roleID);
		return success();
	}
	
	public String modifyRole() {
		if (StringUtils.isBlank(roleID) || StringUtils.isBlank(roleName)) {
			return error("参数不能为空");
		}
		
		try {
			Role role = roleBiz.modifyRole(roleID, roleName, "Y".equals(isValid), description);
			//写入日志
			if (role != null) {
				ActionLogger.log(getRequest(), role.getRoleID(), role.toString());
			}
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String viewRole() {
		if (StringUtils.isBlank(roleID)) {
			return error("参数不能为空");
		}
		role = roleBiz.getRoleByID(roleID);
		return success();
	}
	
	public String initBindGroup() {
		return success();
	}
	
	
	public String loadBindedGroups() {
		if (StringUtils.isBlank(roleID)) {
			return error("参数不能为空");
		}
		try {
			groups = roleBiz.getBindedGroups(roleID);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String saveBindGroup() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登录");
		}
		if (StringUtils.isBlank(roleID)) {
			return error("参数不能为空");
		}
		//如果不是系统管理员，只能操作管理的部门的角色
		if (!user.getRoleIDs().contains(SysConstants.ROLE_ADMIN)) {
			if (!roleBiz.getIsManageGroupRole(roleID, user.getPostIDs())) {
				return error("无权限修改此角色");
			}
		}
		try {
			Role role = roleBiz.txBindGroup(roleID, groupIDs);
			//写入日志
			if (role != null) {
				StringBuffer sbf = new StringBuffer(role.toString());
				if (role.getGroups() != null) {
					sbf.append("\n绑定对象：");
					for (Group s : role.getGroups()) {
						sbf.append("\n").append(s.toString());
					}
					
				}
				ActionLogger.log(getRequest(), role.getRoleID(), sbf.toString());
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initBindUser() {
		return success();
	}
	
	public String loadBindedUsers() {
		if (StringUtils.isBlank(roleID)) {
			return error("参数不能为空");
		}
		try {
			userAccounts = roleBiz.getBindedUsers(roleID);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String saveBindUser() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登录");
		}
		if (StringUtils.isBlank(roleID)) {
			return error("参数不能为空");
		}
		
		//如果不是系统管理员，只能操作管理的部门的角色
		if (!user.getRoleIDs().contains(SysConstants.ROLE_ADMIN)) {
			if (!roleBiz.getIsManageGroupRole(roleID, user.getPostIDs())) {
				return error("无权限修改此角色");
			}
		}
		try {
			Role role = roleBiz.txBindUser(roleID, userIDs);
			//写入日志
			if (role != null) {
				StringBuffer sbf = new StringBuffer(role.toString());
				if (role.getUserAccounts() != null) {
					sbf.append("\n绑定对象：");
					for (UserAccount s : role.getUserAccounts()) {
						sbf.append("\n").append(s.toString());
					}
					
				}
				ActionLogger.log(getRequest(), role.getRoleID(), sbf.toString());
			}
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initBindRight() {
		return success();
	}
	
	public String loadBindedRights() {
		if (StringUtils.isBlank(roleID)) {
			return error("参数不能为空");
		}
		try {
			moduleActions = roleBiz.getBindedRights(roleID);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String saveBindRight() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登录");
		}
		if (StringUtils.isBlank(roleID)) {
			return error("参数不能为空");
		}
		//如果不是系统管理员，只能操作管理的部门的角色
		if (!user.getRoleIDs().contains(SysConstants.ROLE_ADMIN)) {
			if (!roleBiz.getIsManageGroupRole(roleID, user.getPostIDs())) {
				return error("无权限修改此角色");
			}
		}
		
		try {
			Role role = roleBiz.txBindRight(roleID, moduleActionIDs);
			//写入日志
			if (role != null) {
				StringBuffer sbf = new StringBuffer(role.toString());
				if (role.getModuleActions() != null) {
					sbf.append("\n绑定对象：");
					for (ModuleAction s : role.getModuleActions()) {
						sbf.append("\n").append(s.toString());
					}
					
				}
				ActionLogger.log(getRequest(), role.getRoleID(), sbf.toString());
			}
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String loadUserRoles() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登录");
		}
		RoleQueryParameters roleQP = new RoleQueryParameters();
		roleQP.setPageSize(RoleQueryParameters.ALL_PAGE_SIZE);
		if (StringUtils.isNotBlank(roleName)) {
			roleQP.setRoleName(roleName);
		}
//		roleQP.setRoleID(roleID);
		roleQP.SetIsValid(Boolean.TRUE);
		roleQP.addOrder("roleName", true);
		if (!user.getRoleIDs().contains(SysConstants.ROLE_ADMIN)) {
			//如果不是超管，只列出他管理的部门的角色
			List<Group> groups = groupBiz.getGroupsByManagerPost(user.getPostIDs());
			if (groups != null) {
				List<String> groupIDs = new ArrayList<String>();
				for (Group g : groups) {
					groupIDs.add(g.getGroupID());
				}
				roleQP.setGroupIDs(groupIDs);
			}
		}
		try {
			ListPage<Role> page = roleBiz.queryRole(roleQP);
			if (page != null) {
				roles = page.getDataList();
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
}
