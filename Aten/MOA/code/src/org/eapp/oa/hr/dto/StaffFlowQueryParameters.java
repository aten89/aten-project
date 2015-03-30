/**
 * 
 */
package org.eapp.oa.hr.dto;
import java.util.Date;
import java.util.List;

import org.eapp.util.hibernate.QueryParameters;

/**
 */
public class StaffFlowQueryParameters extends QueryParameters {


/**
	 * 
	 */
	private static final long serialVersionUID = -7339813991222179808L;

	public void setID(String id){
		this.addParameter("id", id);
	}
	
	public String getID(){
		return (String)this.getParameter("id");
	}
	
	public void setApplicant(String applicant){
		this.addParameter("applicant", applicant);
	}
	
	public String getApplicant(){
		return (String)this.getParameter("applicant");
	}
	
	public void setApplyType(int applyType){
		this.addParameter("applyType", applyType);
	}
	
	public Integer getApplyType(){
		return (Integer) this.getParameter("applyType");
	}

	public void setCompanyArea(String companyArea){
		this.addParameter("companyArea", companyArea);
	}
	
	public String getCompanyArea(){
		return (String)this.getParameter("companyArea");
	}
	
	public void setUserKeyword(String userKeyword){
		this.addParameter("userKeyword", userKeyword);
	}
	
	public String getUserKeyword(){
		return (String)this.getParameter("userKeyword");
	}
	
	public void setGroupName(String groupName){
		this.addParameter("groupName", groupName);
	}
	
	public String getGroupName(){
		return (String)this.getParameter("groupName");
	}
	
	public void setGroupNames(List<String> groupNames){
		this.addParameter("groupNames", groupNames);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getGroupNames(){
		return (List<String>)this.getParameter("groupNames");
	}
	
	public void setUserAccountID(String userAccountID){
		this.addParameter("userAccountID", userAccountID);
	}
	
	public String getUserAccountID(){
		return (String)this.getParameter("userAccountID");
	}
	
	
	public void setContractEndDate(Date contractEndDate){
		this.addParameter("contractEndDate", contractEndDate);
	}
	
	public Date getContractEndDate(){
		return (Date)this.getParameter("contractEndDate");
	}
	
	public void setFormalDate(Date formalDate){
		this.addParameter("formalDate", formalDate);
	}
	
	public Date getFormalDate(){
		return (Date)this.getParameter("formalDate");
	}
	
	public void setBirthdate(Date birthdate){
		this.addParameter("birthdate", birthdate);
	}
	
	public Date getBirthdate(){
		return (Date)this.getParameter("birthdate");
	}
	
	public void setStaffStatus(String staffStatus){
		this.addParameter("staffStatus", staffStatus);
	}
	
	public String getStaffStatus(){
		return (String)this.getParameter("staffStatus");
	}
}
