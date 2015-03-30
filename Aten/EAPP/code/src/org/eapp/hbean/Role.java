package org.eapp.hbean;

import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

/**
 * Role entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Role implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 5885741724893176519L;
	//Role_ID VARCHAR2(36) not null --角色ID
	private String roleID;
	//RoleName_ VARCHAR2(100) not null --角色名称
	private String roleName;
	//IsValid_ SMALLINT not null --是否有效
	private Boolean isValid;
	//Description_ VARCHAR2(1024) --描述
	private String description;
	private Set<Group> groups = new HashSet<Group>(0);
	private Set<UserAccount> userAccounts = new HashSet<UserAccount>(0);
	private Set<ModuleAction> moduleActions = new HashSet<ModuleAction>(0);

	private Set<Portlet> portlets = new HashSet<Portlet>(0);
	// Constructors

	public Role(String roleID) {
		this.roleID = roleID;
	}
	
	/** default constructor */
	public Role() {
	}

	/** minimal constructor */
	public Role(String roleName, Boolean isValid) {
		this.roleName = roleName;
		this.isValid = isValid;
	}

	/** full constructor */
	public Role(String roleName, Boolean isValid, String description,
			Set<Group> groups, Set<UserAccount> userAccounts, Set<ModuleAction> moduleActions) {
		this.roleName = roleName;
		this.isValid = isValid;
		this.description = description;
		this.groups = groups;
		this.userAccounts = userAccounts;
		this.moduleActions = moduleActions;
	}

	// Property accessors

	public String getRoleID() {
		return this.roleID;
	}

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Boolean getIsValid() {
		return this.isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@JSON(serialize=false)
	public Set<Group> getGroups() {
		return this.groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}
	@JSON(serialize=false)
	public Set<UserAccount> getUserAccounts() {
		return this.userAccounts;
	}

	public void setUserAccounts(Set<UserAccount> userAccounts) {
		this.userAccounts = userAccounts;
	}
	@JSON(serialize=false)
	public Set<ModuleAction> getModuleActions() {
		return this.moduleActions;
	}

	public void setModuleActions(Set<ModuleAction> moduleActions) {
		this.moduleActions = moduleActions;
	}
	
	
	@JSON(serialize=false)
	public Set<Portlet> getPortlets() {
		return portlets;
	}

	public void setPortlets(Set<Portlet> portlets) {
		this.portlets = portlets;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((roleID == null) ? 0 : roleID.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Role other = (Role) obj;
		if (roleID == null) {
			if (other.roleID != null)
				return false;
		} else if (!roleID.equals(other.roleID))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(this.getClass().getName());
		sb.append("[");
		sb.append("roleID=");
		sb.append(getRoleID());
		sb.append(",");
		sb.append("roleName=");
		sb.append(getRoleName());
		sb.append(",");
		sb.append("isValid=");
		sb.append(getIsValid());
		sb.append(",");
		sb.append("description=");
		sb.append(getDescription());
		sb.append("]");
		return sb.toString();

	}

}