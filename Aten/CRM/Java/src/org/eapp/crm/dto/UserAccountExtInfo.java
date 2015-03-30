package org.eapp.crm.dto;

import org.eapp.client.util.UsernameCache;
import org.eapp.rpc.dto.UserAccountInfo;

public class UserAccountExtInfo extends UserAccountInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5776891995713870869L;
	private String serviceAccountId;

	public String getServiceAccountId() {
		return serviceAccountId;
	}

	public void setServiceAccountId(String serviceAccountId) {
		this.serviceAccountId = serviceAccountId;
	}
	
	public String getServiceAccountName() {
		return UsernameCache.getDisplayName(serviceAccountId);
	}
	
}
