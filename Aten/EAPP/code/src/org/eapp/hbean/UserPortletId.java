package org.eapp.hbean;

/**
 * UserPortletId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class UserPortletId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -7324439398723902690L;
	private UserAccount userAccount;
	private Portlet portlet;
	public UserPortletId(){
		
	}
	public UserPortletId(UserAccount userAccount,Portlet portlet){
		this.userAccount = userAccount;
		this.portlet = portlet;
	}
	public UserAccount getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}
	public Portlet getPortlet() {
		return portlet;
	}
	public void setPortlet(Portlet portlet) {
		this.portlet = portlet;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((portlet == null) ? 0 : portlet.hashCode());
		result = prime * result
				+ ((userAccount == null) ? 0 : userAccount.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final UserPortletId other = (UserPortletId) obj;
		if (portlet == null) {
			if (other.portlet != null)
				return false;
		} else if (!portlet.equals(other.portlet))
			return false;
		if (userAccount == null) {
			if (other.userAccount != null)
				return false;
		} else if (!userAccount.equals(other.userAccount))
			return false;
		return true;
	}
	

	// Constructors

	
}