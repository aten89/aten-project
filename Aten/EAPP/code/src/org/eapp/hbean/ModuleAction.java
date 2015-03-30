package org.eapp.hbean;

import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

/**
 * ModuleAction entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ModuleAction implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8641359062971603162L;
	// ModuleAction_ID VARCHAR2(36) not null －－动作模块ID
	private String moduleActionID;
	// Module_ID VARCHAR2(36) not null －－模块ID 
	private Module module;
	// Action_ID VARCHAR2(36) not null －－动作ID
	private Action action;
	// ModuleKey_ VARCHAR2(36) －－模块Key
	private String moduleKey;
	// ActionKey_ VARCHAR2(36) －－动作Key
	private String actionKey;
	// IsValid_ SMALLINT not null －－是否启用
	private Boolean isValid = new Boolean(true);
	// IsRPC_  SMALLINT not null －－是否服务
	private Boolean isRPC = new Boolean(false);
	// IsHTTP_  SMALLINT not null －－是否本地http服务
	private Boolean isHTTP = new Boolean(false);
	private Set<Role> roles = new HashSet<Role>(0);
	private Set<Service> services = new HashSet<Service>(0);

	// Constructors

	/** default constructor */
	public ModuleAction() {
	}

	public ModuleAction(String moduleActionID) {
		this.moduleActionID = moduleActionID;
	}
	
	/** minimal constructor */
	public ModuleAction(Module module, Action action, Boolean isValid) {
		this.module = module;
		this.action = action;
		this.isValid = isValid;
	}

	/** full constructor */
	public ModuleAction(Module module, Action action, String moduleKey,
			String actionKey, Boolean isValid, Set<Role> roles, Set<Service> services) {
		this.module = module;
		this.action = action;
		this.moduleKey = moduleKey;
		this.actionKey = actionKey;
		this.isValid = isValid;
		this.roles = roles;
		this.services = services;
	}

	// Property accessors

	public String getModuleActionID() {
		return this.moduleActionID;
	}

	public void setModuleActionID(String moduleActionID) {
		this.moduleActionID = moduleActionID;
	}
	
	public Module getModule() {
		return this.module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public Action getAction() {
		return this.action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public String getModuleKey() {
		return this.moduleKey;
	}

	public void setModuleKey(String moduleKey) {
		this.moduleKey = moduleKey;
	}

	public String getActionKey() {
		return this.actionKey;
	}

	public void setActionKey(String actionKey) {
		this.actionKey = actionKey;
	}

	public Boolean getIsValid() {
		return this.isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}
	@JSON(serialize=false)
	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	@JSON(serialize=false)
	public Set<Service> getServices() {
		return this.services;
	}

	public void setServices(Set<Service> services) {
		this.services = services;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((moduleActionID == null) ? 0 : moduleActionID.hashCode());
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
		final ModuleAction other = (ModuleAction) obj;
		if (moduleActionID == null) {
			if (other.moduleActionID != null)
				return false;
		} else if (!moduleActionID.equals(other.moduleActionID))
			return false;
		return true;
	}

	/**
	 * @return the isRPC
	 */
	public Boolean getIsRPC() {
		return isRPC;
	}

	/**
	 * @param isRPC the isRPC to set
	 */
	public void setIsRPC(Boolean isRPC) {
		this.isRPC = isRPC;
	}

	public Boolean getIsHTTP() {
		return isHTTP;
	}

	public void setIsHTTP(Boolean isHTTP) {
		this.isHTTP = isHTTP;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(this.getClass().getName());
		sb.append("[");
		sb.append("moduleActionID=");
		sb.append(getModuleActionID());
		sb.append(",");
		sb.append("moduleKey=");
		sb.append(getModuleKey());
		sb.append(",");
		sb.append("actionKey=");
		sb.append(getActionKey());
		sb.append(",");
		sb.append("isValid=");
		sb.append(getIsValid());
		sb.append(",");
		sb.append("isRPC=");
		sb.append(getIsRPC());
		sb.append("]");
		return sb.toString();
	}
}