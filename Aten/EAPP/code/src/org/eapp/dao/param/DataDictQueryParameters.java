/**
 * 
 */
package org.eapp.dao.param;

import org.eapp.util.hibernate.QueryParameters;

/**
 * @author wangshengwei
 * @version 
 */
public class DataDictQueryParameters extends QueryParameters {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6034079762815224547L;
	public void setSubSystemID(String subSystemID){
		this.addParameter("subSystemID", subSystemID);
	}
	public String getSubSystemID(){
		return (String)this.getParameter("subSystemID");
	}
	public void setDictName(String dictName){
		this.addParameter("dictName", dictName);
	}
	public String getDictName(){
		return (String)this.getParameter("dictName");
	}
}
