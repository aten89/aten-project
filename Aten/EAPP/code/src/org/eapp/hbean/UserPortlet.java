package org.eapp.hbean;

/**
 * UserPortlet entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class UserPortlet implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6843001037128779633L;
	private UserPortletId id;
	//PAGECONTAINERID_,VARCHAR2(20),不为空---容器ID
	private String pageContainerID;
	//POSITIONINDEX_,INTEGER,不为空---位置索引
	private Integer positionIndex;
	
	// Constructors

	/** default constructor */
	public UserPortlet() {
	}

	/** full constructor */
	public UserPortlet(UserPortletId id, String pageContainerID,
			Integer positionIndex) {
		this.id = id;
		this.pageContainerID = pageContainerID;
		this.positionIndex = positionIndex;
	}

	// Property accessors

	public UserPortletId getId() {
		return this.id;
	}

	public void setId(UserPortletId id) {
		this.id = id;
	}

	public String getPageContainerID() {
		return this.pageContainerID;
	}

	public void setPageContainerID(String pageContainerID) {
		this.pageContainerID = pageContainerID;
	}

	public Integer getPositionIndex() {
		return this.positionIndex;
	}

	public void setPositionIndex(Integer positionIndex) {
		this.positionIndex = positionIndex;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		final UserPortlet other = (UserPortlet) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		if (getId() != null) {
			sb.append("userID=");
			sb.append(getId().getUserAccount().getUserID());
			sb.append(",");
			sb.append("portletID=");
			sb.append(getId().getPortlet().getPortletID());
			sb.append(",");
		}
		sb.append("pageContainerID=");
		sb.append(getPageContainerID());
		sb.append(",");
		sb.append("positionIndex=");
		sb.append(getPositionIndex());
		sb.append("]");
		return sb.toString();
	}

}