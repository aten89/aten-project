package org.eapp.hbean;

import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

/**
 * Service entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Service implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4179345381905478573L;
	//Service_ID VARCHAR2(36) not null --服务ID
	private String serviceID;
	//ServiceName_ VARCHAR2(100) not null --服务名称
	private String serviceName;
	//IsValid_ SMALLINT not null --是否有效
	private Boolean isValid = Boolean.FALSE;
	//Description_ VARCHAR2(1024) --描述
	private String description;
	private Set<ModuleAction> moduleActions = new HashSet<ModuleAction>(0);
	private Set<ActorAccount> actorAccounts = new HashSet<ActorAccount>(0);

	// Constructors

	/** default constructor */
	public Service() {
	}
	
	public Service(String serviceID) {
		this.serviceID = serviceID;
	}

	/** minimal constructor */
	public Service(String serviceName, Boolean isValid) {
		this.serviceName = serviceName;
		this.isValid = isValid;
	}

	/** full constructor */
	public Service(String serviceName, Boolean isValid, String description,
			Set<ModuleAction> moduleActions, Set<ActorAccount> actorAccounts) {
		this.serviceName = serviceName;
		this.isValid = isValid;
		this.description = description;
		this.moduleActions = moduleActions;
		this.actorAccounts = actorAccounts;
	}

	// Property accessors

	public String getServiceID() {
		return this.serviceID;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
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
	public Set<ModuleAction> getModuleActions() {
		return this.moduleActions;
	}

	public void setModuleActions(Set<ModuleAction> moduleActions) {
		this.moduleActions = moduleActions;
	}
	@JSON(serialize=false)
	public Set<ActorAccount> getActorAccounts() {
		return this.actorAccounts;
	}

	public void setActorAccounts(Set<ActorAccount> actorAccounts) {
		this.actorAccounts = actorAccounts;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((serviceID == null) ? 0 : serviceID.hashCode());
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
		final Service other = (Service) obj;
		if (serviceID == null) {
			if (other.serviceID != null)
				return false;
		} else if (!serviceID.equals(other.serviceID))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(this.getClass().getName());
		sb.append("[");
		sb.append("serviceID=");
		sb.append(getServiceID());
		sb.append(",");
		sb.append("serviceName=");
		sb.append(getServiceName());
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