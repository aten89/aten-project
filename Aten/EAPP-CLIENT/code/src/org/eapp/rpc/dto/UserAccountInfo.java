/**
 * 
 */
package org.eapp.rpc.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zsy
 * @version 
 */
public class UserAccountInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2345431873206016054L;
	private String accountID;
	//显示名称
	private String displayName;
	//是否锁定
	private Boolean isLock;
	//创建时间
	private Date createDate;
	//失效时间
	private Date invalidDate;
	//描述
	private String description;
	//最后登录时间
	private Date lastLoginTime;
	//登录次数
	private Integer loginCount;
	//登录IP限制
	private String loginIpLimit;

	public String getAccountID() {
		return accountID;
	}
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Boolean getIsLock() {
		return isLock;
	}
	public void setIsLock(Boolean isLock) {
		this.isLock = isLock;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getInvalidDate() {
		return invalidDate;
	}
	public void setInvalidDate(Date invalidDate) {
		this.invalidDate = invalidDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public String getLoginIpLimit() {
		return loginIpLimit;
	}
	public void setLoginIpLimit(String loginIpLimit) {
		this.loginIpLimit = loginIpLimit;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountID == null) ? 0 : accountID.hashCode());
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
		final UserAccountInfo other = (UserAccountInfo) obj;
		if (accountID == null) {
			if (other.accountID != null)
				return false;
		} else if (!accountID.equals(other.accountID))
			return false;
		return true;
	}
}
