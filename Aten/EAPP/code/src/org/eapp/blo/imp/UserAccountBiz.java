/**
 * 
 */
package org.eapp.blo.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eapp.blo.IUserAccountBiz;
import org.eapp.dao.IGroupDAO;
import org.eapp.dao.IRoleDAO;
import org.eapp.dao.IShortCutMenuDAO;
import org.eapp.dao.IUserAccountDAO;
import org.eapp.dao.IUserPortletDAO;
import org.eapp.dao.param.UserAccountQueryParameters;
import org.eapp.dto.AccountSaveBean;
import org.eapp.exception.EappException;
import org.eapp.hbean.Post;
import org.eapp.hbean.Group;
import org.eapp.hbean.Role;
import org.eapp.hbean.ShortCutMenu;
import org.eapp.hbean.UserAccount;
import org.eapp.sys.config.SysConstants;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.security.DigestAlgorithm;


/**
 * @author zsy
 * @version 
 */
public class UserAccountBiz implements IUserAccountBiz {

	private IUserAccountDAO userAccountDAO;
	private IGroupDAO groupDAO;
	private IRoleDAO roleDAO;
	private IShortCutMenuDAO shortCutMenuDAO;
	private IUserPortletDAO userPortletDAO;
	private String defaultPassword;//默认密码

	public void setUserAccountDAO(IUserAccountDAO userAccountDAO) {
		this.userAccountDAO = userAccountDAO;
	}

	/**
	 * @param groupDAO the groupDAO to set
	 */
	public void setGroupDAO(IGroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}
	
	/**
	 * @param rebcRoleDAO the rebcRoleDAO to set
	 */
	public void setRoleDAO(IRoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}

	public void setShortCutMenuDAO(IShortCutMenuDAO shortCutMenuDAO) {
		this.shortCutMenuDAO = shortCutMenuDAO;
	}

	public void setUserPortletDAO(IUserPortletDAO userPortletDAO) {
		this.userPortletDAO = userPortletDAO;
	}

	public void setDefaultPassword(String defaultPassword) {
		this.defaultPassword = defaultPassword;
	}

	@Override
	public ListPage<UserAccount> queryUserAccount(UserAccountQueryParameters userQP) {
		if (userQP == null) {
			throw new IllegalArgumentException();
		}
		ListPage<UserAccount> page = userAccountDAO.queryUserAccount(userQP);
		
		//立即加载所属群组
		if (page != null && page.getDataList() != null) {
			List<UserAccount> users = page.getDataList();
			for (UserAccount user : users) {
				user.getGroups().size();//加载延迟内容
			}
		}
		return page;
	}


	@Override
	public List<UserAccount> deleteUsers(String[] accountIDs) throws EappException {
		List<UserAccount> users = new ArrayList<UserAccount>();
		if (accountIDs == null || accountIDs.length < 1) {
			return users;
		}
	//	userAccountDAO.deleteByAccountIDs(accountIDs);
		UserAccount user = null;
		for (String accountID : accountIDs) {
			if (StringUtils.isBlank(accountID)) {
				continue;
			}
			if (SysConstants.USER_ACCOUNT_ADMIN.equals(accountID.trim())) {
				throw new EappException("不能删除系统级管理员帐号");
			}
			user = userAccountDAO.findByAccountID(accountID);
			if (user == null) {
				continue;
			}
			user.setPosts(null);//删除职位绑定关系
			user.setRoles(null);//删除角色绑定关系
			user.setGroups(null);//删除群组绑定关系
			//删除用户快捷方式
			if (user.getShortCutMenus() != null) {
				for (ShortCutMenu sm : user.getShortCutMenus()) {
					shortCutMenuDAO.delete(sm);
				}
			}
			user.setShortCutMenus(null);
			//删除用户门户
			userPortletDAO.deleteByUser(user.getUserID());
			
			user.setIsLogicDelete(true);
			userAccountDAO.update(user);
//			userAccountLDAP.deleteUser(accountID);
			users.add(user);
		}
		return users;
	}
	@Override
	public UserAccount addUser(AccountSaveBean account) throws EappException {
		if (account == null) {
			throw new IllegalArgumentException();
		} else if (StringUtils.isBlank(account.getAccountID()) || StringUtils.isBlank(account.getDisplayName())) {
			throw new IllegalArgumentException("用户帐号与显示名称不能为空");
		}
		if (!userAccountDAO.testAccountID(account.getAccountID())) {
			throw new EappException("用户帐号“" + account.getAccountID() + "”已经存在");
		}
		UserAccount user = new UserAccount();
		user.setAccountID(account.getAccountID());
		user.setDisplayName(account.getDisplayName());
		user.setIsLock(account.getIsLock());
		user.setChangePasswordFlag(account.getChangePasswordFlag());
		user.setCreateDate(new Date());
		user.setInvalidDate(account.getInvalidDate());
		user.setDescription(account.getDescription());
		user.setLoginIpLimit(account.getLoginIpLimit());
		user.setLoginCount(0);
		user.setPassword(DigestAlgorithm.md5Digest(defaultPassword));
		Set<Role> roles = new HashSet<Role>();
		roles.add(new Role(SysConstants.ROLE_BASE));
		user.setRoles(roles);
		userAccountDAO.save(user);
//		userAccountLDAP.addUser(user.getUserID(), user.getAccountID(), user.getDisplayName(), 
//				user.getDescription(), user.getIsLock(), user.getInvalidDate());
//		userAccountLDAP.setUserPassword(user.getAccountID(), user.getPassword());
		return user;
	}


	@Override
	public UserAccount getUserByAccountID(String accountID) {
		if (StringUtils.isBlank(accountID)) {
			throw new IllegalArgumentException();
		}
		return userAccountDAO.findByAccountID(accountID);
	}
	
	@Override
	public UserAccount modifyUser(AccountSaveBean account) throws EappException {
		if (account == null) {
			throw new IllegalArgumentException();
		} else if (StringUtils.isBlank(account.getAccountID()) || StringUtils.isBlank(account.getDisplayName())) {
			throw new IllegalArgumentException("用户帐号与显示名称不能为空");
		}
		UserAccount user = userAccountDAO.findByAccountID(account.getAccountID());
		if (user == null)  {
			throw new EappException("用户不存在");
		}
		if (user.getIsLogicDelete() != null && user.getIsLogicDelete().booleanValue()) {
			throw new EappException("用户已被注销");
		}
		user.setDisplayName(account.getDisplayName());
		user.setIsLock(account.getIsLock());
		if (account.getChangePasswordFlag() != null) {//为空保持原值
			user.setChangePasswordFlag(account.getChangePasswordFlag());
		}
		user.setInvalidDate(account.getInvalidDate());
		user.setDescription(account.getDescription());
		user.setLoginIpLimit(account.getLoginIpLimit());
		userAccountDAO.update(user);
//		userAccountLDAP.modifyUser(user.getAccountID(), user.getDisplayName(),
//				user.getDescription(), user.getIsLock(), user.getInvalidDate());
		return user;
	}
	
	@Override
	public UserAccount txSetPassword(String accountID, String oldPassword, String newPassword) throws EappException {
		UserAccount user = getUserByAccountID(accountID);
		
		if(user == null){
			throw new IllegalArgumentException("用户不存在");
		}
		if(!user.getPassword().equals(DigestAlgorithm.md5Digest(oldPassword))){
			throw new EappException("帐号旧密码不正确");
		}
		//不强制修改密码
		user.setChangePasswordFlag(UserAccount.CHANGEPASSWORD_FALSE);
		setPassword(user, newPassword);
		return user;
	}
	
	
	private void setPassword(UserAccount user, String password) throws EappException {
		if (StringUtils.isBlank(password)) {
			throw new IllegalArgumentException("密码不能为空");
		}
		user.setPassword(DigestAlgorithm.md5Digest(password));
		userAccountDAO.update(user);
		//修改LDAP中密码
//		userAccountLDAP.setUserPassword(accountID, password);
	}
	
	@Override
	public UserAccount txSetDefaultPassword(String accountID) throws EappException {
		UserAccount user = userAccountDAO.findByAccountID(accountID);
		if (user == null) {
			throw new IllegalArgumentException("用户不存在");
		}
		//强制修改密码
		user.setChangePasswordFlag(UserAccount.CHANGEPASSWORD_TRUE);
		setPassword(user, defaultPassword);
		return user;
	}
	
	@Override
	public void txSetStyleThemes(String userAccound, String styleThemes) throws EappException {
		UserAccount user = userAccountDAO.findByAccountID(userAccound);
		if (user == null)  {
			throw new IllegalArgumentException("用户不存在");
		}
		if (user.getIsLogicDelete() != null && user.getIsLogicDelete().booleanValue()) {
			throw new EappException("用户已被注销");
		}
		user.setStyleThemes(styleThemes);
		userAccountDAO.update(user);
	}
	
	@Override
	public Set<Group> getBindedGroups(String accountID) {
		if (StringUtils.isBlank(accountID)) {
			return null;
		}
		UserAccount user = userAccountDAO.findByAccountID(accountID);
		if (user == null) {
			return null;
		}
		Set<Group> gs = user.getGroups();
		gs.size();//加载
//		for (Group g : gs) {
//			initParentGroup(g);
//		}
		return gs;
	}
	
//	private void initParentGroup(Group g) {
//		if (g == null) {
//			return;
//		}
//		Hibernate.initialize(g.getManagerPost());
//		Hibernate.initialize(g.getParentGroup());
//		initParentGroup(g.getParentGroup());
//	}

	@Override
	public Set<Role> getBindedRoles(String accountID) {
		if (StringUtils.isBlank(accountID)) {
			return null;
		}
		UserAccount user = userAccountDAO.findByAccountID(accountID);
		if (user == null) {
			return null;
		}
		user.getRoles().size();//加载延迟内容
		return user.getRoles();
	}
	
	@Override
	public Set<Role> getBindedValidRoles(String accountID) {
		if (StringUtils.isBlank(accountID)) {
			return null;
		}
		UserAccount user = userAccountDAO.findByAccountID(accountID);
		if (user == null) {
			return null;
		}
		user.getValidRoles().size();//加载延迟内容
		return user.getValidRoles();
	}
	
	@Override
	public Set<Post> getBindedPosts(String accountID) {
		if (StringUtils.isBlank(accountID)) {
			return null;
		}
		UserAccount user = userAccountDAO.findByAccountID(accountID);
		if (user == null) {
			return null;
		}
		user.getPosts().size();//加载延迟内容
		return user.getPosts();
	}
	

	@Override
	public UserAccount txBindGroup(String accountID, String[] groupIDs) {
		if (StringUtils.isBlank(accountID)) {
			throw new IllegalArgumentException();
		}
		UserAccount user = userAccountDAO.findByAccountID(accountID);
		if (user == null)  {
			throw new IllegalArgumentException("accountID的对象不存在");
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
		user.setGroups(set);
		userAccountDAO.update(user);
		return user;
	}

	@Override
	public UserAccount txBindRole(String accountID, String[] roleIDs) throws EappException {
		if (StringUtils.isBlank(accountID)) {
			throw new IllegalArgumentException();
		}
		
		if (SysConstants.USER_ACCOUNT_ADMIN.equals(accountID.trim())) {
			
			if (roleIDs != null && roleIDs.length > 0) {
				boolean finded = false;
				for (String gid : roleIDs) {
					if (SysConstants.ROLE_ADMIN.equals(gid)) {
						finded = true;
						break;
					}
				}
				if (!finded) {
					throw new EappException("管理员帐号不能删除超级管理员角色");
				}
			}
			
		}
		UserAccount user = userAccountDAO.findByAccountID(accountID);
		if (user == null)  {
			throw new IllegalArgumentException("accountID的对象不存在");
		}
		HashSet<Role> set = new HashSet<Role>();
		Role role = null;
		if (roleIDs != null && roleIDs.length > 0) {
			for (String gid : roleIDs) {
				if (StringUtils.isBlank(gid)) {
					continue;
				}
				role = roleDAO.findById(gid);
				if (role != null) {
					set.add(role);
				}
			}
		}
		user.setRoles(set);
		userAccountDAO.update(user);
		return user;
	}
	

	@Override
	public List<UserAccount> getCascadeAccountsByGroupId(String groupID) {
		if (StringUtils.isBlank(groupID)) {
			return userAccountDAO.findAll();
		}
		List<String> groupIDList = new ArrayList<String>();
		Group group = groupDAO.findById(groupID);
		if (group == null) {
			return null;
		}
		loadAllChildGroupIDs(group, groupIDList);
		
		return userAccountDAO.findByGroupIDs(groupIDList);
	}
	
	/**
	 * 加载所有下级部门的ID
	 * @param groupID
	 * @param groupIDList
	 */
	private void loadAllChildGroupIDs(Group group, List<String> groupIDList) {
		if (group == null) {
			return;
		}
		groupIDList.add(group.getGroupID());
		if (group.getSubGroups() != null) {
			for (Group g : group.getSubGroups()) {
				loadAllChildGroupIDs(g, groupIDList);
			}
		}
	}

	@Override
	public boolean getForceChangePassword(String accountID) {
		UserAccount user = userAccountDAO.findByAccountID(accountID);
		if (user == null) {
			return true;
		}
		return UserAccount.CHANGEPASSWORD_TRUE.equals(user.getChangePasswordFlag());
	}
}