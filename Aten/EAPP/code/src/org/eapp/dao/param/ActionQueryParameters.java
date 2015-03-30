/**
 * 
 */
package org.eapp.dao.param;

import org.eapp.util.hibernate.QueryParameters;

/**
 * @version 
 */
public class ActionQueryParameters extends QueryParameters {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5438627240302646554L;

	public void setActionName(String name) {
		this.addParameter("name", name);
	}
	
	public String getActionName() {
		return (String)this.getParameter("name");
	}
	
	public void setActionKey(String actionKey) {
		this.addParameter("actionKey", actionKey);
	}
	
	public String getActionKey() {
		return (String)this.getParameter("actionKey");
	}
}
