/**
 * 
 */
package org.eapp.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zsy
 * @version 
 */
public class AccountSaveBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2345431873206016054L;
	private String accountID;
	private String displayName;
	private Boolean isLock;
	private String changePasswordFlag;
	private Date invalidDate;
	private String description;
	private String loginIpLimit;
	
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
	/**
	 * @return the isLock
	 */
	public Boolean getIsLock() {
		return isLock;
	}
	/**
	 * @param isLock the isLock to set
	 */
	public void setIsLock(Boolean isLock) {
		this.isLock = isLock;
	}
	/**
	 * @return the changePasswordFlag
	 */
	public String getChangePasswordFlag() {
		return changePasswordFlag;
	}
	/**
	 * @param changePasswordFlag the changePasswordFlag to set
	 */
	public void setChangePasswordFlag(String changePasswordFlag) {
		this.changePasswordFlag = changePasswordFlag;
	}
	/**
	 * @return the invalidDate
	 */
	public Date getInvalidDate() {
		return invalidDate;
	}
	/**
	 * @param invalidDate the invalidDate to set
	 */
	public void setInvalidDate(Date invalidDate) {
		this.invalidDate = invalidDate;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLoginIpLimit() {
		return loginIpLimit;
	}
	public void setLoginIpLimit(String loginIpLimit) {
		this.loginIpLimit = loginIpLimit;
	}
	
}
