package org.eapp.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IPortletBiz;
import org.eapp.blo.IUserAccountBiz;
import org.eapp.blo.IUserPortalBiz;
import org.eapp.dao.param.RoleQueryParameters;
import org.eapp.dao.param.UserAccountQueryParameters;
import org.eapp.dto.AccountSaveBean;
import org.eapp.exception.EappException;
import org.eapp.hbean.Portlet;
import org.eapp.hbean.Group;
import org.eapp.hbean.Role;
import org.eapp.hbean.UserAccount;
import org.eapp.hbean.UserPortlet;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.util.hibernate.ListPage;

/**
 * 处理用户管理的请求
 * @author zsy
 * @version
 */
public class UserAccountAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 894932421380316916L;
	private static final Log log = LogFactory.getLog(UserAccountAction.class);

	private IUserAccountBiz userAccountBiz;
	private IUserPortalBiz userPortalBiz;
	private IPortletBiz portletBiz;
	
	private int pageNo;
	private int pageSize;
	private String accountID;
	private String displayName;
	private String isLock;
	private String changePasswordFlag;
	private String invalidDate;
	private String description;
	private String loginIpLimit;
	private String keyword;
	private String isValid;
	private String order;
	private String type;
	private String groupID;
	private String[] accountIDs;
	private String[] roleIDs;
	private String[] groupIDs;
	private String portletConfig;
	
	private ListPage<UserAccount> userAccountPage;
	private UserAccount userAccount;
	private Set<Role> roles;
	private Set<Group> groups;
	private List<Portlet> portlets;
	private List<UserAccount> userAccounts;

	public ListPage<UserAccount> getUserAccountPage() {
		return userAccountPage;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public List<Portlet> getPortlets() {
		return portlets;
	}

	public List<UserAccount> getUserAccounts() {
		return userAccounts;
	}

	public void setUserAccountBiz(IUserAccountBiz userAccountBiz) {
		this.userAccountBiz = userAccountBiz;
	}

	public void setUserPortalBiz(IUserPortalBiz userPortalBiz) {
		this.userPortalBiz = userPortalBiz;
	}

	public void setPortletBiz(IPortletBiz portletBiz) {
		this.portletBiz = portletBiz;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setIsLock(String isLock) {
		this.isLock = isLock;
	}

	public void setChangePasswordFlag(String changePasswordFlag) {
		this.changePasswordFlag = changePasswordFlag;
	}

	public void setInvalidDate(String invalidDate) {
		this.invalidDate = invalidDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setLoginIpLimit(String loginIpLimit) {
		this.loginIpLimit = loginIpLimit;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public void setAccountIDs(String[] accountIDs) {
		this.accountIDs = accountIDs;
	}

	public void setRoleIDs(String[] roleIDs) {
		this.roleIDs = roleIDs;
	}

	public void setGroupIDs(String[] groupIDs) {
		this.groupIDs = groupIDs;
	}

	public void setPortletConfig(String portletConfig) {
		this.portletConfig = portletConfig;
	}

	//初始化编辑页面
	public String initFrame() {
		return success();
	}

	public String initQuery() {
		return success();
	}
	
	
	public String queryUser() {
		UserAccountQueryParameters userQP = new UserAccountQueryParameters();
		userQP.setPageNo(pageNo);
		userQP.setPageSize(pageSize);
		if (StringUtils.isNotBlank(keyword)) {
			userQP.setKeyword(keyword);
		}
		if (StringUtils.isNotBlank(accountID)) {
			userQP.setAccountID(accountID);
		}
		if (StringUtils.isNotBlank(displayName)) {
			userQP.setDisplayName(displayName);
		}
		if (StringUtils.isNotBlank(isLock)) {
			userQP.setIsLock("Y".equals(isLock));
		}
		if (StringUtils.isNotBlank(isValid)) {
			userQP.setIsValid("Y".equals(isValid));
		}

		if (StringUtils.isNotBlank(order)) {
			userQP.addOrder(order, "A".equals(type));
		} else {
			userQP.addOrder("accountID", true);
		}
		if (StringUtils.isNotBlank(groupID)) {
			userQP.setGroupID(groupID);
		}

		try {
			userAccountPage = userAccountBiz.queryUserAccount(userQP);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initAdd() {
		return success();
	}
	
	
	
	
	public String addUser() {
		if (StringUtils.isBlank(accountID) || StringUtils.isBlank(displayName)) {
			return error("参数不能为空");
		}

		AccountSaveBean account = new AccountSaveBean();
		account.setAccountID(accountID);
		account.setDisplayName(displayName);
		account.setIsLock("Y".equals(isLock));
		if (StringUtils.isNotBlank(invalidDate)) {
			account.setInvalidDate(Timestamp.valueOf(DataFormatUtil.formatTime(invalidDate)));
		}
		
		account.setChangePasswordFlag(UserAccount.CHANGEPASSWORD_TRUE.equals(changePasswordFlag) ? 
				UserAccount.CHANGEPASSWORD_TRUE : UserAccount.CHANGEPASSWORD_FALSE);
		account.setDescription(description);
		account.setLoginIpLimit(loginIpLimit);
		try {
			UserAccount user = userAccountBiz.addUser(account);
			//写入日志
			if (user != null) {
				ActionLogger.log(getRequest(), user.getUserID(), user.toString());
			}
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initModify() {
		if (StringUtils.isBlank(accountID)) {
			return error("参数不能为空");
		}
		userAccount = userAccountBiz.getUserByAccountID(accountID);
		return success();
	}
	
	public String modifyUser() {
		if (StringUtils.isBlank(accountID) || StringUtils.isBlank(displayName)) {
			return error("参数不能为空");
		}

		AccountSaveBean account = new AccountSaveBean();
		account.setAccountID(accountID);
		account.setDisplayName(displayName);
		account.setIsLock("Y".equals(isLock));
		if (StringUtils.isNotBlank(invalidDate)) {
			account.setInvalidDate(Timestamp.valueOf(DataFormatUtil.formatTime(invalidDate)));
		}
		account.setChangePasswordFlag(UserAccount.CHANGEPASSWORD_TRUE.equals(changePasswordFlag) ? 
				UserAccount.CHANGEPASSWORD_TRUE : UserAccount.CHANGEPASSWORD_FALSE);
		account.setDescription(description);
		account.setLoginIpLimit(loginIpLimit);
		try {
			UserAccount user = userAccountBiz.modifyUser(account);
			//写入日志
			if (user != null) {
				ActionLogger.log(getRequest(), user.getUserID(), user.toString());
			}
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String viewUser() {
		if (StringUtils.isBlank(accountID)) {
			return error("参数不能为空");
		}
		userAccount = userAccountBiz.getUserByAccountID(accountID);
		return success();
	}
	
	public String deleteUsers() {
		if (accountIDs == null || accountIDs.length < 1) {
			return error("参数不能为空");
		}
		try {
			List<UserAccount> users = userAccountBiz.deleteUsers(accountIDs);
			//写入日志
			if (users != null) {
				for (UserAccount user : users) {
					ActionLogger.log(getRequest(), user.getUserID(), user.toString());
				}
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String resetPassword() {
		if (StringUtils.isBlank(accountID)) {
			return error("参数不能为空");
		}
		try {
			UserAccount user = userAccountBiz.txSetDefaultPassword(accountID);
			//写入日志
			if (user != null) {
				ActionLogger.log(getRequest(), user.getUserID(), user.toString());
			}
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initBindRole() {
		return success();
	}
	
	public String loadBindedRoles() {
		if (StringUtils.isBlank(accountID)) {
			return error("参数不能为空");
		}
		try {
			roles = userAccountBiz.getBindedRoles(accountID);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String saveBindRole() {
		if (StringUtils.isBlank(accountID)) {
			return error("参数不能为空");
		}
		try {
			UserAccount user = userAccountBiz.txBindRole(accountID, roleIDs);
			//写入日志
			if (user != null) {
				StringBuffer sbf = new StringBuffer();
				if (user.getRoles() != null) {
					for (Role r : user.getRoles()) {
						sbf.append(r.toString()).append("\n");
					}
					
				}
				ActionLogger.log(getRequest(), user.getUserID(), user.toString() + "\n绑定对象：\n" + sbf.toString());
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initBindGroup() {
		return success();
	}
	
	public String loadBindedGroups() {
		if (StringUtils.isBlank(accountID)) {
			return error("参数不能为空");
		}
		try {
			groups = userAccountBiz.getBindedGroups(accountID);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String saveBindGroup() {
		if (StringUtils.isBlank(accountID)) {
			return error("参数不能为空");
		}
		try {
			UserAccount user = userAccountBiz.txBindGroup(accountID, groupIDs);
			//写入日志
			if (user != null) {
				StringBuffer sbf = new StringBuffer();
				if (user.getGroups() != null) {
					for (Group g : user.getGroups()) {
						sbf.append(g.toString()).append("\n");
					}
					
				}
				ActionLogger.log(getRequest(), user.getUserID(), user.toString() + "\n绑定对象：\n" + sbf.toString());
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	
	
	public String initSetPortlet() {
		return success();
	}

	public String loadUserPortals() {
		if (StringUtils.isBlank(accountID)) {
			return error("参数不能为空");
		}
		try {
			portlets = userPortalBiz.getPortletConfigByUser(accountID);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	public String loadUserAllPortals() {
		if (StringUtils.isBlank(accountID)) {
			return error("参数不能为空");
		}
		try {
			portlets = portletBiz.getPortlets(accountID);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	public String saveUserPortal() {
		if (StringUtils.isBlank(accountID)) {
			return error("参数不能为空");
		}
		
		List<String[]> portletConfigList = new ArrayList<String[]>();
		if (StringUtils.isNotBlank(portletConfig)) {
			String[] configs = portletConfig.split("@");
			for (String config : configs) {
				String[] detail = config.split(",");
				if (detail.length != 3) {
					return error("配置信息格式不正确");
				} 
				portletConfigList.add(detail);
				
			}
		}
		try {
			List<UserPortlet> pus = userPortalBiz.modifyUserPortlets(accountID, portletConfigList);
			
			if (pus != null) {
				StringBuffer sbf = new StringBuffer();
				for (UserPortlet up : pus) {
					sbf.append(up.toString()).append("\n");
				}
				ActionLogger.log(getRequest(), null, "绑定对象：\n" + sbf.toString());
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
		
	}
	
	public String loadUsers() {
		UserAccountQueryParameters userQP = new UserAccountQueryParameters();
		userQP.setPageSize(RoleQueryParameters.ALL_PAGE_SIZE);
		if (StringUtils.isNotBlank(keyword)) {
			userQP.setKeyword(keyword);
		}
		if (StringUtils.isNotBlank(groupID)) {
			userQP.setGroupID(groupID);
		}
		
		userQP.setIsLock(false);
		userQP.setIsValid(true);
		userQP.addOrder("accountID", true);
		try {
			ListPage<UserAccount> page = userAccountBiz.queryUserAccount(userQP);
			if (page != null) {
				userAccounts = page.getDataList();
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
}
