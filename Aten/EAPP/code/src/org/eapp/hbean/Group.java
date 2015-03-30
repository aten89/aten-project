package org.eapp.hbean;

import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

/**
 * Group entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Group implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3683214355885012572L;
	public static final String GROUP_TYPE_DEPT = "D";
	public static final String GROUP_TYPE_PROJECT = "J";
	//Group_ID VARCHAR2(36) not null －－群组ID
	private String groupID;
	private Group parentGroup;
	private Post managerPost;
	//GroupName_ VARCHAR2(100) not null －－群组名称
	private String groupName;
	//DisplayOrder_ INTEGER not null －－排序
	private Integer displayOrder;
	//Type_ VARCHAR2(20) －－类型
	private String type;
	//TreeLevel_ INTEGER －－ 树层级
	private Integer treeLevel;
	//Description_ VARCHAR2(1024) －－描述
	private String description;
	private Set<Group> subGroups = new HashSet<Group>(0);
	private Set<Role> roles = new HashSet<Role>(0);
	private Set<UserAccount> userAccounts = new HashSet<UserAccount>(0);
	private Set<Post> posts = new HashSet<Post>(0);
	
	private boolean hasSubGroup;//是否有子群组



	// Constructors
	
	/** default constructor */
	public Group() {
	}
	public Group(String groupID) {
		this.groupID = groupID;
	}

	/** minimal constructor */
	public Group(String groupName, Integer displayOrder) {
		this.groupName = groupName;
		this.displayOrder = displayOrder;
	}

	/** full constructor */
	public Group(Group group, Post managerPost,
			String groupName, Integer displayOrder, String type,
			Integer treeLevel, String description, Set<Group> groups,
			Set<Role> roles, Set<UserAccount> userAccounts) {
		this.parentGroup = group;
		this.managerPost = managerPost;
		this.groupName = groupName;
		this.displayOrder = displayOrder;
		this.type = type;
		this.treeLevel = treeLevel;
		this.description = description;
		this.subGroups = groups;
		this.roles = roles;
		this.userAccounts = userAccounts;
	}

	// Property accessors

	public String getGroupID() {
		return this.groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	@JSON(serialize=false)
	public Group getParentGroup() {
		return this.parentGroup;
	}

	public void setParentGroup(Group group) {
		this.parentGroup = group;
	}
	@JSON(serialize=false)
	public Post getManagerPost() {
		return this.managerPost;
	}

	public void setManagerPost(Post managerPost) {
		this.managerPost = managerPost;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getTreeLevel() {
		return this.treeLevel;
	}

	public void setTreeLevel(Integer treeLevel) {
		this.treeLevel = treeLevel;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@JSON(serialize=false)
	public Set<Group> getSubGroups() {
		return this.subGroups;
	}

	public void setSubGroups(Set<Group> groups) {
		this.subGroups = groups;
	}
	@JSON(serialize=false)
	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	@JSON(serialize=false)
	public Set<UserAccount> getUserAccounts() {
		return this.userAccounts;
	}

	public void setUserAccounts(Set<UserAccount> userAccounts) {
		this.userAccounts = userAccounts;
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
		final Group other = (Group) obj;
		if (groupID == null) {
			if (other.groupID != null)
				return false;
		} else if (!groupID.equals(other.groupID))
			return false;
		return true;
	}
	/**
	 * @return the hasSubGroup
	 */
	public boolean isHasSubGroup() {
		return hasSubGroup;
	}
	/**
	 * @param hasSubGroup the hasSubGroup to set
	 */
	public void setHasSubGroup(boolean hasSubGroup) {
		this.hasSubGroup = hasSubGroup;
	}
	
	public String getNamePath() {
		StringBuffer path = new StringBuffer();
		initPath(path, this, 50);
		return path.toString();
	}
	
	private void initPath(StringBuffer path, Group group, int breakTimes) {
		if (group == null || breakTimes-- < 0) {
			return;
		}
		path.append(group.getGroupName()).append(",");
		initPath(path, group.getParentGroup(), breakTimes);
	}
	
	public String getIdPath() {
		StringBuffer path = new StringBuffer();
		initIdPath(path, this, 50);
		return path.toString();
	}
	
	private void initIdPath(StringBuffer path, Group g, int breakTime) {
		if (g == null || breakTime-- < 0) {
			return;
		}
		initIdPath(path, g.getParentGroup(), breakTime);
		path.append(g.getGroupID()).append("/");
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(this.getClass().getName());
		sb.append("[");
		sb.append("groupID=");
		sb.append(getGroupID());
		sb.append(",");
		sb.append("groupName=");
		sb.append(getGroupName());
		sb.append(",");
		sb.append("displayOrder=");
		sb.append(getDisplayOrder());
		sb.append(",");
		sb.append("type=");
		sb.append(getType());
		sb.append(",");
		sb.append("treeLevel=");
		sb.append(getTreeLevel());
		sb.append(",");
		sb.append("description=");
		sb.append(getDescription());
		sb.append("]");
		return sb.toString();
	}
	@JSON(serialize=false)
	public Set<Post> getPosts() {
		return posts;
	}
	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}
}