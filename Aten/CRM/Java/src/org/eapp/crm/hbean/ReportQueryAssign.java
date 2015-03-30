package org.eapp.crm.hbean;

/**
 */

public class ReportQueryAssign implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3123148554247752512L;
	private String id;
	private String rptID;
	private String userAccountID;//
	private String groupName;//
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getRptID() {
		return rptID;
	}
	public void setRptID(String rptID) {
		this.rptID = rptID;
	}
	public String getUserAccountID() {
		return userAccountID;
	}
	public void setUserAccountID(String userAccountID) {
		this.userAccountID = userAccountID;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}