/**
 * 
 */
package org.eapp.comobj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 用户登录后的绑定在session中的相关信息
 * @author zsy
 * @version 
 */
public class SessionAccount implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9018910142438126919L;

	public static final long SYNCH_TERM = 15 * 60 * 1000;//同步权限时间为5分钟，单位豪秒
	
	private String userID;
	private String accountID;
	private String displayName;
	private String styleThemes;
	private boolean isLock;
	private long invalidDate;
	private String loginIpLimit;
	private String loginIpAddr;
	private long lastSynchTime;
	private List<Name> groups;
	private List<Name> depts;
	private List<Name> roles;
	private HashMap<String, String[]> moduleActions = new HashMap<String, String[]>();
	
	private List<Name> posts;
	
	/**
	 * @return the userID
	 */
	public String getUserID() {
		return userID;
	}
	/**
	 * @param userID the userID to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}
	/**
	 * @return the accountID
	 */
	public String getAccountID() {
		return accountID;
	}
	/**
	 * @param accountID the accountID to set
	 */
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getStyleThemes() {
		return styleThemes;
	}
	
	public void setStyleThemes(String styleThemes) {
		this.styleThemes = styleThemes;
	}
	/**
	 * @return the isLock
	 */
	public boolean isLock() {
		return isLock;
	}
	/**
	 * @param isLock the isLock to set
	 */
	public void setLock(boolean isLock) {
		this.isLock = isLock;
	}
	/**
	 * @return the invalidDate
	 */
	public long getInvalidDate() {
		return invalidDate;
	}
	/**
	 * @param invalidDate the invalidDate to set
	 */
	public void setInvalidDate(long invalidDate) {
		this.invalidDate = invalidDate;
	}
	
	public String getLoginIpLimit() {
		return loginIpLimit;
	}
	public void setLoginIpLimit(String loginIpLimit) {
		this.loginIpLimit = loginIpLimit;
	}
	public String getLoginIpAddr() {
		return loginIpAddr;
	}
	public void setLoginIpAddr(String loginIpAddr) {
		this.loginIpAddr = loginIpAddr;
	}
	/**
	 * @return the lastSynchTime
	 */
	public long getLastSynchTime() {
		return lastSynchTime;
	}
	/**
	 * @param lastSynchTime the lastSynchTime to set
	 */
	public void setLastSynchTime(long lastSynchTime) {
		this.lastSynchTime = lastSynchTime;
	}
	/**
	 * @return the roleIDs
	 */
	public List<Name> getRoles() {
		return roles;
	}
	
	/**
	 * 取得角色ID
	 * @return
	 */
	public List<String> getRoleIDs() {
		List<String> ids = new ArrayList<String>();
		if (roles == null || roles.size() < 1) {
			return ids;
		}
		for (Name n : roles) {
			ids.add(n.getId());
		}
		return ids;
	}
	/**
	 * @param roleIDs the roleIDs to set
	 */
	public void setRoles(List<Name> roles) {
		this.roles = roles;
	}
	/**
	 * @return the moduleActions
	 */
	public HashMap<String, String[]> getModuleActions() {
		return moduleActions;
	}
	/**
	 * @param moduleActions the moduleActions to set
	 */
	public void setModuleActions(HashMap<String, String[]> moduleActions) {
		this.moduleActions = moduleActions;
	}
	/**
	 * @return the groupNames
	 */
	public List<Name> getGroups() {
		return groups;
	}
	
	public String getGroupNames() {
		if (groups == null || groups.size() < 1) {
			return "";
		}
		StringBuffer gsb = new StringBuffer();
		for (Name n : groups) {
			gsb.append(n.getName()).append(",");
		}
		gsb.deleteCharAt(gsb.length()-1);
		return gsb.toString();
	}
	
	/**
	 * @param groupNames the groupNames to set
	 */
	public void setGroups(List<Name> groups) {
		this.groups = groups;
	}
	
	public List<Name> getPosts() {
		return posts;
	}
	
	public List<Name> getDepts() {
		return depts;
	}
	public void setDepts(List<Name> depts) {
		this.depts = depts;
	}
	
	/**
	 * 获取部门名称
	 * 
	 * @return
	 */
	public String getDeptNames() {
		if (depts == null || depts.size() < 1) {
			return "";
		}
		StringBuffer gsb = new StringBuffer();
		for (Name n : depts) {
			gsb.append(n.getName()).append(",");
		}
		gsb.deleteCharAt(gsb.length()-1);
		return gsb.toString();
	}
	
	/**
	 * 取得职位ID
	 * @return
	 */
	public List<String> getPostIDs() {
		List<String> ids = new ArrayList<String>();
		if (posts == null || posts.size() < 1) {
			return ids;
		}
		for (Name n : posts) {
			ids.add(n.getId());
		}
		return ids;
	}
	
	public void setPosts(List<Name> posts) {
		this.posts = posts;
	}
	/**
	 * 是否需要更新缓存
	 * @return
	 */
	public boolean isNeedSynch() {
		return System.currentTimeMillis() - lastSynchTime > SYNCH_TERM;
	}
	
	/**
	 * 用户帐号是否有效
	 * @return
	 */
	public boolean isVaild() {
		return !isLock //未锁定
				&& (invalidDate > System.currentTimeMillis() || invalidDate == 0) //未失效
				&& (loginIpLimit == null || loginIpLimit.indexOf(loginIpAddr) >= 0);//无IP限制
	}
	
	/**
	 * 判断动作在模块内是否有权限
	 * @param moduleID 模块ID
	 * @param action 动作
	 * @return
	 */
	public boolean hasRight(String moduleKey, String actionKey) {
		//以上判断动作在模块内是否需要权限，若不需要返回true
		if (moduleKey == null || moduleKey.trim().equals("") 
				|| actionKey == null || actionKey.trim().equals("")) {
			return true;
		}
		String[] actionList = moduleActions.get(moduleKey);
		if (actionList == null || actionList.length < 2) {
			return true;
		}
		String allActions = actionList[0];
		if (allActions == null) {
			return true;
		}
		if (!allActions.contains("," + actionKey + ",")) {
			return true;
		}
		
		//该action需要权限，以下为判断过程
		String myActions = actionList[1];
		if (myActions == null) {
			return false;
		}
		return myActions.contains("," + actionKey + ",");
	}
	
	/**
	 * 帐号失效时清空Bean中的信息，释放内存
	 */
	public void clear() {
		if (roles != null) {
			roles.clear();
		}
		if (moduleActions != null) {
			moduleActions.clear();
		}
	}
	
	/**
	 * 存储ID与名称
	 * @author zsy
	 * @version
	 */
	public class Name implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 5025821240059853240L;
		private String id;
		private String name;
		public Name(String id, String name) {
			this.id = id;
			this.name = name;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	}
}
