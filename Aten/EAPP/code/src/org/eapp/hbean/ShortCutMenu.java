package org.eapp.hbean;

import org.apache.struts2.json.annotations.JSON;

/**
 * ShortCutMenu entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ShortCutMenu implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1404408606282407548L;
	
	public static final String CUSTOM_TYPE = "CUSTOM";
	public static final String SYSTEM_TYPE = "SYSTEM";
	public static final String FAVORITE_TYPE = "FAVORITE";
	//SHORTCUTMENU_ID,VARCHAR2(36),不为空   --快捷方式ID
	private String shortCutMenuID;
	//USER_ID,VARCHAR2(36)                 --用户ID
	private UserAccount userAccount;
	//MENUTITLE_,VARCHAR2(100),不为空        --快捷方式标题    
	private String menuTitle;
	//URL_,VARCHAR2(1024),不为空              --快捷方式链接
	private String url;
	//WINDOWTARGET_,VARCHAR2(20),不为空      --弹出方式
	private String windowTarget;
	//LOGOURL_,VARCHAR2(1024)              --图标链接
	private String logoURL;
	//DISPLAYORDER_,INTEGER,不为空            --显示顺序
	private Integer displayOrder;
	//TYPE_,VARCHAR2(20)                   --类型
	private String type;
	//ISVALID_,INTEGER                     --是否可用
	private Boolean isValid = Boolean.TRUE;

	private String moduleTitle;//收藏夹收藏模块标题
	// Constructors

	/** default constructor */
	public ShortCutMenu() {
	}

	/** minimal constructor */
	public ShortCutMenu(String menuTitle, String url, String windowTarget,
			Integer displayOrder) {
		this.menuTitle = menuTitle;
		this.url = url;
		this.windowTarget = windowTarget;
		this.displayOrder = displayOrder;
	}

	/** full constructor */
	public ShortCutMenu(UserAccount userAccount, String menuTitle, String url,
			String windowTarget, String logoURL, Integer displayOrder,
			String type, Boolean isValid) {
		this.userAccount = userAccount;
		this.menuTitle = menuTitle;
		this.url = url;
		this.windowTarget = windowTarget;
		this.logoURL = logoURL;
		this.displayOrder = displayOrder;
		this.type = type;
		this.isValid = isValid;
	}

	// Property accessors

	public String getShortCutMenuID() {
		return this.shortCutMenuID;
	}

	public void setShortCutMenuID(String shortCutMenuID) {
		this.shortCutMenuID = shortCutMenuID;
	}

	@JSON(serialize=false)
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public String getMenuTitle() {
		return this.menuTitle;
	}

	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWindowTarget() {
		return this.windowTarget;
	}

	public void setWindowTarget(String windowTarget) {
		this.windowTarget = windowTarget;
	}

	public String getLogoURL() {
		return this.logoURL;
	}

	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
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

	public Boolean getIsValid() {
		return this.isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	public String getModuleTitle() {
		return moduleTitle;
	}

	public void setModuleTitle(String moduleTitle) {
		this.moduleTitle = moduleTitle;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((shortCutMenuID == null) ? 0 : shortCutMenuID.hashCode());
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
		final ShortCutMenu other = (ShortCutMenu) obj;
		if (shortCutMenuID == null) {
			if (other.shortCutMenuID != null)
				return false;
		} else if (!shortCutMenuID.equals(other.shortCutMenuID))
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
		sb.append("shortCutMenuID=");
		sb.append(getShortCutMenuID());
		sb.append(",");
		sb.append("menuTitle=");
		sb.append(getMenuTitle());
		sb.append(",");
		sb.append("url=");
		sb.append(getUrl());
		sb.append(",");
		sb.append("windowTarget=");
		sb.append(getWindowTarget());
		sb.append(",");
		sb.append("logoURL=");
		sb.append(getLogoURL());
		sb.append(",");
		sb.append("displayOrder=");
		sb.append(getDisplayOrder());
		sb.append(",");
		sb.append("type=");
		sb.append(getType());
		sb.append(",");
		sb.append("isValid=");
		sb.append(getIsValid());
		sb.append("]");
		return sb.toString();
	}

}