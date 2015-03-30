/**
 * 
 */
package org.eapp.dao.param;

import org.eapp.util.hibernate.QueryParameters;

/**
 * 群组查询条件
 * @author zsy
 * @version 
 */
public class PostQueryParameters extends QueryParameters {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7744732529204879514L;

	public void setPostName(String postName) {
		this.addParameter("postName", postName);
	}
	
	public String getPostName() {
		return (String)this.getParameter("postName");
	}
	
	public void setBinded(Boolean isBinded) {
		this.addParameter("isBinded", isBinded);
	}
	
	public Boolean isBinded() {
		return (Boolean)this.getParameter("isBinded");
	}
}
