/**
 * 
 */
package org.eapp.dao.param;

import org.eapp.util.hibernate.QueryParameters;

/**
 * 用户查询条件
 * @author zsy
 * @version 
 */
public class ModuleActionQueryParameters extends QueryParameters {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3848318010680842420L;

	public void setModuleID(String moduleID) {
		this.addParameter("moduleID", moduleID);
	}
	
	public String getModuleID() {
		return (String)this.getParameter("moduleID");
	}
	public void setIsRPC(Boolean isRPC) {
		this.addParameter("isRPC", isRPC);
	}
	
	public Boolean getIsRPC() {
		return (Boolean)this.getParameter("isRPC");
	}
	
	public void setIsHTTP(Boolean isHTTP) {
		this.addParameter("isHTTP", isHTTP);
	}
	
	public Boolean getIsHTTP() {
		return (Boolean)this.getParameter("isHTTP");
	}
	
	public void setIsValid(Boolean isValid) {
		this.addParameter("isValid", isValid);
	}
	
	public Boolean getIsValid() {
		return (Boolean)this.getParameter("isValid");
	}
}
