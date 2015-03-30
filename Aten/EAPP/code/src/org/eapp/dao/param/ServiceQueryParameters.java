/**
 * 
 */
package org.eapp.dao.param;

import org.eapp.util.hibernate.QueryParameters;

/**
 * 角色查询条件
 * @author zsy
 * @version 
 */
public class ServiceQueryParameters extends QueryParameters {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1283117578815349587L;

	public void setServiceName(String serviceName) {
		this.addParameter("serviceName", serviceName);
	}
	
	public String getServiceName() {
		return (String)this.getParameter("serviceName");
	}
	
	public void SetIsValid(Boolean isValid) {
		this.addParameter("isValid", isValid);
	}
	
	public Boolean getIsValid() {
		return (Boolean)this.getParameter("isValid");
	}
}
