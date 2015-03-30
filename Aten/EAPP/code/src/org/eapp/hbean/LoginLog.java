package org.eapp.hbean;

import java.sql.Timestamp;

import org.apache.struts2.json.annotations.JSON;

/**
 */

public class LoginLog implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1326356562368442047L;

	private String logID;
	private String sessionID;
	private String accountID;
	private String accountName;
	private String ipAddress;
	private boolean isSuccess;
	private String loginInfo;
	private Timestamp loginTime;
	private Timestamp logoutTime;
	//IsBackUp_ INTEGER  --是否备份过
	private Boolean isBackUp = new Boolean(false);
	

	public String getLogID() {
		return logID;
	}

	public void setLogID(String logID) {
		this.logID = logID;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(String loginInfo) {
		this.loginInfo = loginInfo;
	}
	@JSON(format="yyyy-MM-dd HH:mm")
	public Timestamp getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	public Timestamp getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Timestamp logoutTime) {
		this.logoutTime = logoutTime;
	}

	public Boolean getIsBackUp() {
		return isBackUp;
	}

	public void setIsBackUp(Boolean isBackUp) {
		this.isBackUp = isBackUp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((logID == null) ? 0 : logID.hashCode());
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
		final LoginLog other = (LoginLog) obj;
		if (logID == null) {
			if (other.logID != null)
				return false;
		} else if (!logID.equals(other.logID))
			return false;
		return true;
	}

}