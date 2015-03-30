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
public class GroupQueryParameters extends QueryParameters {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2138562156293302431L;

	public void setGroupName(String groupName) {
		this.addParameter("groupName", groupName);
	}
	
	public String getGroupName() {
		return (String)this.getParameter("groupName");
	}
}
