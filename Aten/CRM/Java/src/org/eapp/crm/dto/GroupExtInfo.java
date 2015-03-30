package org.eapp.crm.dto;

import org.eapp.rpc.dto.GroupInfo;

public class GroupExtInfo extends GroupInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7873236312697947903L;
	private String businessType;

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	
}
