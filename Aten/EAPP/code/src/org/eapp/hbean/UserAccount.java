package org.eapp.hbean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

/**
 * UserAccount entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class UserAccount implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -7898091424622930597L;
	
	public static final String CHANGEPASSWORD_FALSE = "N";//不强制修改密码
	public static final String CHANGEPASSWORD_TRUE = "Y";//强制修改密码
	
	//User_ID VARCHAR2(36) not null－－ID
	private String userID;
	// AccountID_ VARCHAR2(100) not null －－帐号
	private String accountID;
	//DisplayName_ VARCHAR2(100) not null－－显示名称
	private String displayName;
	//Password_ NCHAR(255) －－密码
	private String password;
	//IsLock_ SMALLINT not null －－是否锁定
	private Boolean isLock;
	//ChangePasswordFlag_ CHAR(1) not null －－是否强制修改密码
	private String changePasswordFlag;
	//CreateDate_ DATE not null －－创建时间
	private Date createDate;
	//InvalidDate_ DATE －－失效时间
	private Date invalidDate;
	//IsLogicDelete_ SMALLINT not null －－删除标志
	private Boolean isLogicDelete = Boolean.FALSE;
	//Description_ VARCHAR2(1024) －－描述
	private String description;
	
	private String styleThemes;//页面主题风格
	//LAST_LOGIN_TIME_     DATETIME,
	private Date lastLoginTime;
	//LOGIN_COUNT_         INT,
	private Integer loginCount;
	//LOGIN_IP_LIMIT_      VARCHAR(2048),
	private String loginIpLimit;
	
	private Set<ShortCutMenu> shortCutMenus;
	private Set<Group> groups;
	private Set<Role> roles;
	private Set<Role> validRoles;
	
	private Set<ShortCutMenu> disableShortCutMenus;
	private Set<ShortCutMenu> enableShortCutMenus;
	private Set<ShortCutMenu> favoriteShortCutMenus;
	
	private Set<Post> posts = new HashSet<Post>(0);//职位

	// Constructors

	/** default constructor */
	public UserAccount() {
	}

	public UserAccount(String userID) {
		this.userID = userID;
	}
	
	/** minimal constructor */
	public UserAccount(String accountID, String displayName, Boolean isLock,
			String changePasswordFlag, Date createDate, Boolean isLogicDelete) {
		this.accountID = accountID;
		this.displayName = displayName;
		this.isLock = isLock;
		this.changePasswordFlag = changePasswordFlag;
		this.createDate = createDate;
		this.isLogicDelete = isLogicDelete;
	}


	// Property accessors

	public String getUserID() {
		return this.userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getAccountID() {
		return this.accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsLock() {
		return this.isLock;
	}

	public void setIsLock(Boolean isLock) {
		this.isLock = isLock;
	}

	public String getChangePasswordFlag() {
		return this.changePasswordFlag;
	}

	public void setChangePasswordFlag(String changePasswordFlag) {
		this.changePasswordFlag = changePasswordFlag;
	}

	@JSON(format="yyyy-MM-dd HH:mm")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@JSON(format="yyyy-MM-dd")
	public Date getInvalidDate() {
		return invalidDate;
	}

	public void setInvalidDate(Date invalidDate) {
		this.invalidDate = invalidDate;
	}

	public Boolean getIsLogicDelete() {
		return this.isLogicDelete;
	}

	public void setIsLogicDelete(Boolean isLogicDelete) {
		this.isLogicDelete = isLogicDelete;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStyleThemes() {
		return styleThemes;
	}

	public void setStyleThemes(String styleThemes) {
		this.styleThemes = styleThemes;
	}
	@JSON(serialize=false)
	public Set<ShortCutMenu> getShortCutMenus() {
		return this.shortCutMenus;
	}

	public void setShortCutMenus(Set<ShortCutMenu> shortCutMenus) {
		this.shortCutMenus = shortCutMenus;
	}
	@JSON(serialize=false)
	public Set<Group> getGroups() {
		return this.groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}
	@JSON(serialize=false)
	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final UserAccount other = (UserAccount) obj;
		if (userID == null) {
			if (other.userID != null)
				return false;
		} else if (!userID.equals(other.userID))
			return false;
		return true;
	}

	/**
	 * @return the validRoles
	 */
	@JSON(serialize=false)
	public Set<Role> getValidRoles() {
		return validRoles;
	}

	/**
	 * @param validRoles the validRoles to set
	 */
	public void setValidRoles(Set<Role> validRoles) {
		this.validRoles = validRoles;
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer(this.getClass().getName());
		str.append("[")
				.append("userID=").append(getUserID())
				.append(",accountID=").append(getAccountID())
				.append(",displayName=").append(getDisplayName())
				.append(",password=").append(getPassword())
				.append(",isLock=").append(getIsLock())
				.append(",changePasswordFlag=").append(getChangePasswordFlag())
				.append(",createDate=").append(getCreateDate())
				.append(",invalidDate=").append(getInvalidDate())
				.append(",isLogicDelete=").append(getIsLogicDelete())
				.append(",description=").append(getDescription())
				.append("]");
		return str.toString();
	}

	/**
	 * @return the disableShortCutMenus
	 */
	@JSON(serialize=false)
	public Set<ShortCutMenu> getDisableShortCutMenus() {
		return disableShortCutMenus;
	}

	/**
	 * @param disableShortCutMenus the disableShortCutMenus to set
	 */
	public void setDisableShortCutMenus(Set<ShortCutMenu> disableShortCutMenus) {
		this.disableShortCutMenus = disableShortCutMenus;
	}

	/**
	 * @return the enableShortCutMenus
	 */
	@JSON(serialize=false)
	public Set<ShortCutMenu> getEnableShortCutMenus() {
		return enableShortCutMenus;
	}

	/**
	 * @param enableShortCutMenus the enableShortCutMenus to set
	 */
	public void setEnableShortCutMenus(Set<ShortCutMenu> enableShortCutMenus) {
		this.enableShortCutMenus = enableShortCutMenus;
	}
	
	@JSON(serialize=false)
	public Set<ShortCutMenu> getFavoriteShortCutMenus() {
		return favoriteShortCutMenus;
	}

	public void setFavoriteShortCutMenus(Set<ShortCutMenu> favoriteShortCutMenus) {
		this.favoriteShortCutMenus = favoriteShortCutMenus;
	}
	@JSON(serialize=false)
	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}
	
	public String getGroupNames() {
		if (groups == null || groups.isEmpty()) {
			return "";
		}
		StringBuffer gns = new StringBuffer();
		for(Group group : groups) {
			gns.append(group.getGroupName()).append(" ");
		}
		return gns.toString();
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Integer getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}

	public void addLoginCount() {
		if (loginCount == null) {
			loginCount = 1;
		} else {
			loginCount++;
		}
	}
	
	public String getLoginIpLimit() {
		return loginIpLimit;
	}

	public void setLoginIpLimit(String loginIpLimit) {
		this.loginIpLimit = loginIpLimit;
	}
}