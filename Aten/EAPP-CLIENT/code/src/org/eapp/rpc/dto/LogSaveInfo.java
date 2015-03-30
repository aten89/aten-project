package org.eapp.rpc.dto;

import java.io.Serializable;


/**
 * InterfaceLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class LogSaveInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7717471707881754285L;
	private String systemID;
	private String moduleKey;
	private String actionKey;
	private String accountID;
	private String objectID;
	private String object;
	private String ipAddress;
	private String resultStatus;
	public String getSystemID() {
		return systemID;
	}
	public void setSystemID(String systemID) {
		this.systemID = systemID;
	}
	public String getModuleKey() {
		return moduleKey;
	}
	public void setModuleKey(String moduleKey) {
		this.moduleKey = moduleKey;
	}
	public String getActionKey() {
		return actionKey;
	}
	public void setActionKey(String actionKey) {
		this.actionKey = actionKey;
	}
	public String getAccountID() {
		return accountID;
	}
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	public String getObjectID() {
		return objectID;
	}
	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getResultStatus() {
		return resultStatus;
	}
	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

}