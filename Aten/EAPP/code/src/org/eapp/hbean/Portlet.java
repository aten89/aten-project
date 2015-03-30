package org.eapp.hbean;

import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;
import org.eapp.sys.config.SysConstants;

/**
 * Portlet entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Portlet implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -2429462326559819154L;
	//PORTLET_ID,VARCHAR2(36),不为空      --板块ID
	private String portletID;
	//PORLETNAME_,VARCHAR2(100),不为空    --板块名称
	private String portletName;
	//URL_,VARCHAR2(1024),不为空           --板块链接
	private String url;
	private String moreUrl;
	//
	private String style;
	//HIDDENABLE_,INTEGER                --是否隐藏
	private Boolean hiddenable;
	//MOVEDABLE_,INTEGER                 --是否可拖动
	private Boolean movedable;
	private Set<UserPortlet> userPorlets = new HashSet<UserPortlet>(0);
	private Set<DefaultPortlet> defaultPorlets = new HashSet<DefaultPortlet>(0);
	private Set<Role> roles = new HashSet<Role>(0);
	// SUBSYSTEM_ID,VARCHAR2(36)		 --所属子系统ID
	private SubSystem subSystem;

	@JSON(serialize=false)
	public Set<DefaultPortlet> getDefaultPorlets() {
		return defaultPorlets;
	}

	public void setDefaultPorlets(Set<DefaultPortlet> defaultPorlets) {
		this.defaultPorlets = defaultPorlets;
	}

	/** default constructor */
	public Portlet() {
	}

	/** minimal constructor */
	public Portlet(String portletName, String url) {
		this.portletName = portletName;
		this.url = url;
	}

	/** full constructor */
	public Portlet(String portletName, String url, Boolean hiddenable,
			Boolean movedable, Set<UserPortlet> userPorlets) {
		this.portletName = portletName;
		this.url = url;
		this.hiddenable = hiddenable;
		this.movedable = movedable;
		this.userPorlets = userPorlets;
	}

	// Property accessors

	public String getPortletID() {
		return this.portletID;
	}

	public void setPortletID(String portletID) {
		this.portletID = portletID;
	}

	public String getPortletName() {
		return this.portletName;
	}

	public void setPortletName(String portletName) {
		this.portletName = portletName;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getHiddenable() {
		return this.hiddenable;
	}

	public void setHiddenable(Boolean hiddenable) {
		this.hiddenable = hiddenable;
	}

	public Boolean getMovedable() {
		return this.movedable;
	}

	public void setMovedable(Boolean movedable) {
		this.movedable = movedable;
	}
	@JSON(serialize=false)
	public Set<UserPortlet> getUserPorlets() {
		return this.userPorlets;
	}

	public void setUserPorlets(Set<UserPortlet> userPorlets) {
		this.userPorlets = userPorlets;
	}

	/**
	 * @return the moreUrl
	 */
	public String getMoreUrl() {
		return moreUrl;
	}

	/**
	 * @param moreUrl the moreUrl to set
	 */
	public void setMoreUrl(String moreUrl) {
		this.moreUrl = moreUrl;
	}

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}
	
	@JSON(serialize=false)
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public SubSystem getSubSystem() {
		return this.subSystem;
	}

	public void setSubSystem(SubSystem subSystem) {
		this.subSystem = subSystem;
	}
	
	public String getUrlPath() {
		if (url != null && !url.startsWith("/") && !url.toLowerCase().startsWith("http://") 
				&& !url.toLowerCase().startsWith("https://")
				&& !subSystem.getSubSystemID().equals(SysConstants.EAPP_SUBSYSTEM_ID)) {
			return subSystem.getBasePath() + url;
		}
		return url;
	}
	public String getMoreUrlPath() {
		if (moreUrl != null && !moreUrl.startsWith("/") && !moreUrl.toLowerCase().startsWith("http://") 
				&& !moreUrl.toLowerCase().startsWith("https://")
				&& !subSystem.getSubSystemID().equals(SysConstants.EAPP_SUBSYSTEM_ID)) {
			return subSystem.getBasePath() + moreUrl;
		}
		return moreUrl;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((portletID == null) ? 0 : portletID.hashCode());
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
		final Portlet other = (Portlet) obj;
		if (portletID == null) {
			if (other.portletID != null)
				return false;
		} else if (!portletID.equals(other.portletID))
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
		sb.append("portletID=");
		sb.append(getPortletID());
		sb.append(",");
		sb.append("portletName=");
		sb.append(getPortletName());
		sb.append(",");
		sb.append("url=");
		sb.append(getUrl());
		sb.append(",");
		sb.append("hiddenable=");
		sb.append(getHiddenable());
		sb.append(",");
		sb.append("movedable=");
		sb.append(getMovedable());
		sb.append("]");
		return sb.toString();
	}

}