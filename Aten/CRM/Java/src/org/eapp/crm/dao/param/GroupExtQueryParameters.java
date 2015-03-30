package org.eapp.crm.dao.param;

import org.eapp.util.hibernate.QueryParameters;

public class GroupExtQueryParameters extends QueryParameters {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1576936061585305162L;

	public void setBusinessType(String businessType) {
	    addParameter("businessType", businessType);
	  }

	public String getBusinessType() {
	    return (String)getParameter("businessType");
	}
	
	public void setGroupId(String groupId) {
		addParameter("groupId",groupId);
	}
	
	public String getGroupId() {
		return (String)getParameter("groupId");
	}
}
