package org.eapp.hbean;

import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

/**
 * Group entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Post implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6146304057500024877L;
	// Fields

	//Post_ID VARCHAR2(36) not null －－职位ID
	private String postID;
	//PostName_ VARCHAR2(100) not null －－职位名称
	private String postName;
	//DisplayOrder_ INTEGER not null －－排序
	private Integer displayOrder;
	//TreeLevel_ INTEGER －－ 树层级
	private Integer treeLevel;
	//Description_ VARCHAR2(1024) －－描述
	private String description;
	private Group group;
	private Post parentPost;
	private Set<Post> subPosts = new HashSet<Post>(0);
	private Set<UserAccount> userAccounts = new HashSet<UserAccount>(0);

	private boolean hasSubPost;//是否有下级职位
	// Constructors
	
	public boolean isHasSubPost() {
		return hasSubPost;
	}
	public void setHasSubPost(boolean hasSubPost) {
		this.hasSubPost = hasSubPost;
	}
	/** default constructor */
	public Post() {
	}
	public Post(String postID) {
		this.postID = postID;
	}

	/** minimal constructor */
	public Post(String postName, Integer displayOrder) {
		this.postName = postName;
		this.displayOrder = displayOrder;
	}

	public String getPostID() {
		return postID;
	}
	public void setPostID(String postID) {
		this.postID = postID;
	}
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public Integer getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	public Integer getTreeLevel() {
		return treeLevel;
	}
	public void setTreeLevel(Integer treeLevel) {
		this.treeLevel = treeLevel;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@JSON(serialize=false)
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	@JSON(serialize=false)
	public Post getParentPost() {
		return parentPost;
	}
	public void setParentPost(Post parentPost) {
		this.parentPost = parentPost;
	}
	@JSON(serialize=false)
	public Set<Post> getSubPosts() {
		return subPosts;
	}
	public void setSubPosts(Set<Post> subPosts) {
		this.subPosts = subPosts;
	}
	@JSON(serialize=false)
	public Set<UserAccount> getUserAccounts() {
		return userAccounts;
	}
	public void setUserAccounts(Set<UserAccount> userAccounts) {
		this.userAccounts = userAccounts;
	}
	
	public String getIdPath() {
		StringBuffer path = new StringBuffer();
		initIdPath(path, this, 50);
		return path.toString();
	}
	
	private void initIdPath(StringBuffer path, Post g, int breakTime) {
		if (g == null || breakTime-- < 0) {
			return;
		}
		initIdPath(path, g.getParentPost(), breakTime);
		path.append(g.getPostID()).append("/");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((postID == null) ? 0 : postID.hashCode());
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
		final Post other = (Post) obj;
		if (postID == null) {
			if (other.postID != null)
				return false;
		} else if (!postID.equals(other.postID))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(this.getClass().getName());
		sb.append("[");
		sb.append("groupID=");
		sb.append(getPostID());
		sb.append(",");
		sb.append("groupName=");
		sb.append(getPostName());
		sb.append(",");
		sb.append("displayOrder=");
		sb.append(getDisplayOrder());
		sb.append(",");
		sb.append("treeLevel=");
		sb.append(getTreeLevel());
		sb.append(",");
		sb.append("description=");
		sb.append(getDescription());
		sb.append("]");
		return sb.toString();
	}
}