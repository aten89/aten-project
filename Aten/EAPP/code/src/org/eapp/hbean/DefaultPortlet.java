package org.eapp.hbean;

/**
 * UserPortlet entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DefaultPortlet implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6843001037128779633L;
	private String defaultPortletID;
	//PAGECONTAINERID_,VARCHAR2(20),不为空---容器ID
	private String pageContainerID;
	//POSITIONINDEX_,INTEGER,不为空---位置索引
	private Integer positionIndex;
	private Portlet portlet;
	// Constructors
	
	transient private String portletId;

	public String getPortletId() {
		return portletId;
	}

	public void setPortletId(String portletId) {
		this.portletId = portletId;
	}

	/** default constructor */
	public DefaultPortlet() {
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
		result = prime * result + ((defaultPortletID == null) ? 0 : defaultPortletID.hashCode());
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
		final DefaultPortlet other = (DefaultPortlet) obj;
		if (defaultPortletID == null) {
			if (other.defaultPortletID != null)
				return false;
		} else if (!defaultPortletID.equals(other.defaultPortletID))
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
		sb.append("pageContainerID=");
		sb.append(getPageContainerID());
		sb.append(",");
		sb.append("positionIndex=");
		sb.append(getPositionIndex());
		sb.append("]");
		return sb.toString();
	}

	/**
	 * @return the defaultPortletID
	 */
	public String getDefaultPortletID() {
		return defaultPortletID;
	}

	/**
	 * @param defaultPortletID the defaultPortletID to set
	 */
	public void setDefaultPortletID(String defaultPortletID) {
		this.defaultPortletID = defaultPortletID;
	}

	/**
	 * @return the portlet
	 */
	public Portlet getPortlet() {
		return portlet;
	}

	/**
	 * @param portlet the portlet to set
	 */
	public void setPortlet(Portlet portlet) {
		this.portlet = portlet;
	}
}