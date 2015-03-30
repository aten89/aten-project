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
public class SysMsgQueryParameters extends QueryParameters {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4293811683437977219L;

	public void setToAccountID(String toAccountID) {
		this.addParameter("toAccountID", toAccountID);
	}
	
	public String getToAccountID() {
		return (String)this.getParameter("toAccountID");
	}
	
	public void setMsgSender(String msgSender) {
		this.addParameter("msgSender", msgSender);
	}
	
	public String getMsgSender() {
		return (String)this.getParameter("msgSender");
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
	
	public void setViewFlag(Boolean viewFlag) {
		this.addParameter("viewFlag", viewFlag);
	}
	
	public Boolean getViewFlag() {
		return (Boolean)this.getParameter("viewFlag");
	}
}
