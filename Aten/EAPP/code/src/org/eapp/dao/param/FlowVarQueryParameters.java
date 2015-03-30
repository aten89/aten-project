/**
 * 
 */
package org.eapp.dao.param;

import org.eapp.util.hibernate.QueryParameters;

/**
 * @version 
 */
public class FlowVarQueryParameters extends QueryParameters {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5438627240302646554L;

	public void setDisplayName(String displayName) {
		this.addParameter("displayName", displayName);
	}
	
	public String getDisplayName() {
		return (String)this.getParameter("displayName");
	}
	
	public String getFlowClass() {
        return (String)this.getParameter("flowClass");
    }
    
    public void setFlowClass(String flowClass) {
    	this.addParameter("flowClass", flowClass);
    }
    
    public Boolean getGlobalFlag() {
        return (Boolean)this.getParameter("globalFlag");
    }
    
    public void setGlobalFlag(Boolean globalFlag) {
    	this.addParameter("globalFlag", globalFlag);
    }
}
