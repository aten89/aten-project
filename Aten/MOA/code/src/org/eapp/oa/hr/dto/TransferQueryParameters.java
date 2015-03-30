/**
 * 
 */
package org.eapp.oa.hr.dto;
import org.eapp.util.hibernate.QueryParameters;

/**
 * @author zfc
 * 请假单查询
 */
public class TransferQueryParameters extends QueryParameters {


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

}
