package org.eapp.crm.dto;

import org.eapp.rpc.dto.UserAccountInfo;

public class ReportAssignInfo {
	private UserAccountInfo user;
	private String assignValue;

	public String getAssignValue() {
		return assignValue;
	}

	public void setAssignValue(String assignValue) {
		this.assignValue = assignValue;
	}

	public UserAccountInfo getUser() {
		return user;
	}

	public void setUser(UserAccountInfo user) {
		this.user = user;
	}

}
