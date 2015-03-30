package org.eapp.hbean;

import java.sql.Timestamp;

import org.apache.struts2.json.annotations.JSON;

/**
 * ActionLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ActionLog implements java.io.Serializable {

	// Fields
	/**
	 * 
	 */
	private static final long serialVersionUID = -6996875378691671084L;
	
	public static final String RESULT_SUCCEED = "Y";
	public static final String RESULT_FAIL = "N";
	
	//ActionLog_ID VARCHAR2(36) not null -- 日志ID
	private String logID;
	//SystemID_  VARCHAR2(36) not null --系统ID
	private String systemID;
	//SystemName_  VARCHAR2(100) --系统名称
	private String systemName;
	//ModuleKey_   VARCHAR2(20)  --模块KEY
	private String moduleKey;
	//ModuleName_ VARCHAR2(100) --模块名称
	private String moduleName;
	//ActionKey_ VARCHAR2(100) not null --操作KEY
	private String actionKey;
	//ActionName_ VARCHAR2(100) not null --操作名称
	private String actionName;
	//userAccountID_ VARCHAR2(100) not null --帐号ID
	private String accountID;
	//userDisplayName_  VARCHAR2(100) not null  --帐号名称
	private String accountName;
	//ObjectID_ VARCHAR2(36)  --对象ID
	private String objectID;
	//Object_ CLOB --对象
	private String object;
	//IPAddress_ VARCHAR2(40) --IP地址
	private String ipAddress;
	//ResultStatus_ VARCHAR2(100) --操作结果
	private String resultStatus;
	//OperateTime_ TIMESTAMP not null  --操作时间
	private Timestamp operateTime;
	//IsServiceLog_ INTEGER  --是服务日志
	private Boolean isServiceLog = new Boolean(false);
	//IsBackUp_ INTEGER  --是否备份过
	private Boolean isBackUp = new Boolean(false);
	
	// Constructors

	/** default constructor */
	public ActionLog() {
	}


	public String getLogID() {
		return logID;
	}


	public void setLogID(String logID) {
		this.logID = logID;
	}


	public String getSystemID() {
		return systemID;
	}


	public void setSystemID(String systemID) {
		this.systemID = systemID;
	}


	public String getSystemName() {
		return systemName;
	}


	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}


	public String getModuleKey() {
		return moduleKey;
	}


	public void setModuleKey(String moduleKey) {
		this.moduleKey = moduleKey;
	}


	public String getModuleName() {
		return moduleName;
	}


	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}


	public String getActionKey() {
		return actionKey;
	}


	public void setActionKey(String actionKey) {
		this.actionKey = actionKey;
	}


	public String getActionName() {
		return actionName;
	}


	public void setActionName(String actionName) {
		this.actionName = actionName;
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

	@JSON(format="yyyy-MM-dd HH:mm")
	public Timestamp getOperateTime() {
		return operateTime;
	}


	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}


	public Boolean getIsBackUp() {
		return isBackUp;
	}


	public void setIsBackUp(Boolean isBackUp) {
		this.isBackUp = isBackUp;
	}


	public Boolean getIsServiceLog() {
		return isServiceLog;
	}


	public void setIsServiceLog(Boolean isServiceLog) {
		this.isServiceLog = isServiceLog;
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
		final ActionLog other = (ActionLog) obj;
		if (logID == null) {
			if (other.logID != null)
				return false;
		} else if (!logID.equals(other.logID))
			return false;
		return true;
	}

}