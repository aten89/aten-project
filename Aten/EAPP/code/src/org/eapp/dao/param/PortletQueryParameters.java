/**
 * 
 */
package org.eapp.dao.param;

import org.eapp.util.hibernate.QueryParameters;

/**
 * @version 1.0
 */
public class PortletQueryParameters extends QueryParameters {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5147469038764217422L;
	public void setPortletName(String portletName) {
		this.addParameter("portletName", portletName);
	}
	public String getPortletName() {
		return (String)this.getParameter("portletName");
	}
}
