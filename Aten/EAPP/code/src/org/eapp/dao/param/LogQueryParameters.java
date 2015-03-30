/**
 * 
 */
package org.eapp.dao.param;

import java.sql.Timestamp;

import org.eapp.util.hibernate.QueryParameters;

/**
 * @author zsy
 * @version 
 */
public class LogQueryParameters extends QueryParameters {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8979468688613027236L;

	public void setSystemID(String systemID) {
		this.addParameter("systemID", systemID);
	}
	
	public String getSystemID() {
		return (String)this.getParameter("systemID");
	}
	
	public void setModuleKey(String moduleKey) {
		this.addParameter("moduleKey", moduleKey);
	}
	
	public String getModuleKey() {
		return (String)this.getParameter("moduleKey");
	}
	
	public void setActionKey(String actionKey) {
		this.addParameter("actionKey", actionKey);
	}
	
	public String getActionKey() {
		return (String)this.getParameter("actionKey");
	}
	
	public void setAccountName(String accountName) {
		this.addParameter("accountName", accountName);
	}
	
	public String getAccountName() {
		return (String)this.getParameter("accountName");
	}
	
	public void setAccountID(String accountID) {
		this.addParameter("accountID", accountID);
	}
	
	public String getAccountID() {
		return (String)this.getParameter("accountID");
	}
	
	public void setBeginTime(Timestamp beginTime) {
		this.addParameter("beginTime", beginTime);
	}
	
	public Timestamp getBeginTime() {
		return (Timestamp)this.getParameter("beginTime");
	}
	
	public void setEndTime(Timestamp endTime) {
		this.addParameter("endTime", endTime);
	}
	
	public Timestamp getEndeTime() {
		return (Timestamp)this.getParameter("endTime");
	}
	
	public void setIsServiceLog(Boolean isServiceLog) {
		this.addParameter("isServiceLog", isServiceLog);
	}
	
	public Boolean getIsServiceLog() {
		return (Boolean)this.getParameter("isServiceLog");
	}
}
