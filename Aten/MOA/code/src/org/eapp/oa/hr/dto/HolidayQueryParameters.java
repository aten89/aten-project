/**
 * 
 */
package org.eapp.oa.hr.dto;
import java.util.Date;

import org.eapp.util.hibernate.QueryParameters;

/**
 * @author zfc
 * 请假单查询
 */
public class HolidayQueryParameters extends QueryParameters {


/**
	 * 
	 */
	private static final long serialVersionUID = -7339813991222179808L;

	//	请假单号	ID_	VARCHAR2(36)	36		TRUE	FALSE	TRUE
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
	
	public void setFormType(int formType){
		this.addParameter("formType", formType);
	}
	
	public Integer getFormType(){
		return (Integer) this.getParameter("formType");
	}
	
	public void setBgnQueryDate(Date bgnQueryDate){
		this.addParameter("bgnQueryDate", bgnQueryDate);
	}
	
	public Date getBgnQueryDate(){
		return (Date) this.getParameter("bgnQueryDate");
	}
	
	public void setEndQueryDate(Date endQueryDate){
		this.addParameter("endQueryDate", endQueryDate);
	}
	
	public Date getEndQueryDate(){
		return (Date) this.getParameter("endQueryDate");
	}
}
