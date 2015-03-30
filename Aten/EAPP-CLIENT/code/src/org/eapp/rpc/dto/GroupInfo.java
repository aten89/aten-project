/**
 * 
 */
package org.eapp.rpc.dto;

import java.io.Serializable;

/**
 * @version 
 */
public class GroupInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5454707132206746555L;
	//群组ID
	private String groupID;
	//父ID
	private String parentGroupID;
	//管理员ID
	private String managerRostID;
	//群组名称
	private String groupName;
	//排序
	private Integer displayOrder;
	//类型
	private String type;
	//树层级
	private Integer treeLevel;
	//描述
	private String description;

	/**
	 * @return the groupID
	 */
	public String getGroupID() {
		return groupID;
	}
	/**
	 * @param groupID the groupID to set
	 */
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	/**
	 * @return the parentGroupID
	 */
	public String getParentGroupID() {
		return parentGroupID;
	}
	/**
	 * @param parentGroupID the parentGroupID to set
	 */
	public void setParentGroupID(String parentGroupID) {
		this.parentGroupID = parentGroupID;
	}
	/**
	 * @return the managerRostID
	 */
	public String getManagerPostID() {
		return managerRostID;
	}
	/**
	 * @param managerRostID the managerRostID to set
	 */
	public void setManagerPostID(String managerRostID) {
		this.managerRostID = managerRostID;
	}
	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}
	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * @return the displayOrder
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}
	/**
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the treeLevel
	 */
	public Integer getTreeLevel() {
		return treeLevel;
	}
	/**
	 * @param treeLevel the treeLevel to set
	 */
	public void setTreeLevel(Integer treeLevel) {
		this.treeLevel = treeLevel;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
    /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupID == null) ? 0 : groupID.hashCode());
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
		final GroupInfo other = (GroupInfo) obj;
		if (groupID == null) {
			if (other.groupID != null)
				return false;
		} else if (!groupID.equals(other.groupID))
			return false;
		return true;
	}
	
	
}
