/**
 * 
 */
package org.eapp.dao.param;

import org.eapp.util.hibernate.QueryParameters;

/**
 * @version 
 */
public class FlowHandlerQueryParameters extends QueryParameters {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5438627240302646554L;

	public void setName(String name) {
		this.addParameter("name", name);
	}
	
	public String getName() {
		return (String)this.getParameter("name");
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
