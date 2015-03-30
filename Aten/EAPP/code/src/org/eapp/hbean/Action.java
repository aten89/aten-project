package org.eapp.hbean;

import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;


/**
 * Action entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Action implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6829643457633414442L;
	//ACTION_ID,VARCHAR2(36),不能为空              --动作ID
	private String actionID;
	//ACTIONKEY_,VARCHAR2(36)                   --动作KEY
	private String actionKey;
	//NAME_,VARCHAR2(100),不能为空                  --动作名字
	private String name;
	//LOGOURL_,VARCHAR2(1024)                   --图标链接
	private String logoURL;
	//TIPS_,VARCHAR2(100)                       --提示信息
	private String tips;
	//DESCRIPTION_,VARCHAR2(1024)               --备注
	private String description;
	private Set<ModuleAction> moduleActions = new HashSet<ModuleAction>(0);

	// Constructors

	/** default constructor */
	public Action() {
	}

	/** minimal constructor */
	public Action(String actionID, String name) {
		this.actionID = actionID;
		this.name = name;
	}

	/** full constructor */
	public Action(String actionID, String actionKey, String name,
			String logoURL, String tips, String description, Set<ModuleAction> moduleActions) {
		this.actionID = actionID;
		this.actionKey = actionKey;
		this.name = name;
		this.logoURL = logoURL;
		this.tips = tips;
		this.description = description;
		this.moduleActions = moduleActions;
	}

	// Property accessors

	public String getActionID() {
		return this.actionID;
	}

	public void setActionID(String actionID) {
		this.actionID = actionID;
	}

	public String getActionKey() {
		return this.actionKey;
	}

	public void setActionKey(String actionKey) {
		this.actionKey = actionKey;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogoURL() {
		return this.logoURL;
	}

	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}

	public String getTips() {
		return this.tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actionID == null) ? 0 : actionID.hashCode());
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
		final Action other = (Action) obj;
		if (actionID == null) {
			if (other.actionID != null)
				return false;
		} else if (!actionID.equals(other.actionID))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(this.getClass().getName());
		sb.append("[");
		sb.append("actionID=");
		sb.append(getActionID());
		sb.append(",");
		sb.append("actionKey=");
		sb.append(getActionKey());
		sb.append(",");
		sb.append("name=");
		sb.append(getName());
		sb.append(",");
		sb.append("logoURL=");
		sb.append(getLogoURL());
		sb.append(",");
		sb.append("tips=");
		sb.append(getTips());
		sb.append(",");
		sb.append("description=");
		sb.append(getDescription());
		sb.append("]");
		return sb.toString();
	}
}