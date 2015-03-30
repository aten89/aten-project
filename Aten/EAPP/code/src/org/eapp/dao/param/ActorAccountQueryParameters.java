/**
 * 
 */
package org.eapp.dao.param;

import org.eapp.util.hibernate.QueryParameters;

/**
 * 接口用户查询条件
 * @author zsy
 * @version 
 */
public class ActorAccountQueryParameters extends QueryParameters {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6826317309048372853L;

	public void setAccountID(String accountID) {
		this.addParameter("accountID", accountID);
	}
	
	public String getAccountID() {
		return (String)this.getParameter("accountID");
	}
	
	public void setDisplayName(String displayName) {
		this.addParameter("displayName", displayName);
	}
	
	public String getDisplayName() {
		return (String)this.getParameter("displayName");
	}
	
	public void setIsLock(Boolean isLock) {
		this.addParameter("isLock", isLock);
	}
	
	public Boolean getIsLock() {
		return (Boolean)this.getParameter("isLock");
	}
	
	public void setIsValid(Boolean isValid) {
		this.addParameter("isValid", isValid);
	}
	
	public Boolean getIsValid() {
		return (Boolean)this.getParameter("isValid");
	}
	
	public void setKeyword(String keyword) {
		this.addParameter("keyword", keyword);
	}
	
	public String getKeyword() {
		return (String)this.getParameter("keyword");
	}
}
