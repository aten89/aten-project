package org.eapp.oa.travel.dto;

import org.eapp.util.hibernate.QueryParameters;

public class BusTripQueryParameters extends QueryParameters {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4872195268618765768L;

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
	
	public void setApplyDept(String applyDept){
		this.addParameter("applyDept", applyDept);
	}
	
	public String getApplyDept(){
		return (String) this.getParameter("applyDept");
	}
}
