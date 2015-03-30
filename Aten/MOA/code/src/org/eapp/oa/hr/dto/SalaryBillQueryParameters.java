/**
 * 
 */
package org.eapp.oa.hr.dto;
import org.eapp.util.hibernate.QueryParameters;

/**
 */
public class SalaryBillQueryParameters extends QueryParameters {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7339813991222179808L;

	public void setMonth(Integer month){
		this.addParameter("month", month);
	}
	
	public Integer getMonth(){
		return (Integer) this.getParameter("month");
	}
	
	public void setUserAccountID(String userAccountID){
		this.addParameter("userAccountID", userAccountID);
	}
	
	public String getUserAccountID(){
		return (String)this.getParameter("userAccountID");
	}
	
	public void setUserKeyword(String userKeyword){
		this.addParameter("userKeyword", userKeyword);
	}
	
	public String getUserKeyword(){
		return (String)this.getParameter("userKeyword");
	}
	
	public void setEmployeeNumber(String employeeNumber){
		this.addParameter("employeeNumber", employeeNumber);
	}
	
	public String getEmployeeNumber(){
		return (String)this.getParameter("employeeNumber");
	}
}
