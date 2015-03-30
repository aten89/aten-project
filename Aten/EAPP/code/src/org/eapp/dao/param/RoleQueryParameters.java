/**
 * 
 */
package org.eapp.dao.param;

import java.util.List;

import org.eapp.util.hibernate.QueryParameters;

/**
 * 角色查询条件
 * @author zsy
 * @version 
 */
public class RoleQueryParameters extends QueryParameters {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7787640581915441320L;

	public void setRoleID(String roleID) {
		this.addParameter("roleID", roleID);
	}
	
	public String getRoleID() {
		return (String)this.getParameter("roleID");
	}
	
	public void setRoleName(String roleName) {
		this.addParameter("roleName", roleName);
	}
	
	public String getRoleName() {
		return (String)this.getParameter("roleName");
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getGroupIDs() {
		return (List<String>)this.getParameter("groupIDs");
	}
	
	public void setGroupIDs(List<String> groupIDs) {
		this.addParameter("groupIDs", groupIDs);
	}

	public void SetIsValid(Boolean isValid) {
		this.addParameter("isValid", isValid);
	}
	
	public Boolean getIsValid() {
		return (Boolean)this.getParameter("isValid");
	}
}
