/**
 * 
 */
package org.eapp.oa.reimburse.dto;

import java.sql.Timestamp;

import org.eapp.util.hibernate.QueryParameters;


/**
 * @author 林良益
 * 报销单查询
 */
public class ReimbursementQueryParameters extends QueryParameters {


/**
	 * 
	 */
	private static final long serialVersionUID = 6547068315668917296L;

	//	报销单号	ID_	VARCHAR2(36)	36		TRUE	FALSE	TRUE
//	财务隶属	Finance_	CHAR			FALSE	FALSE	FALSE
	public void setID(String id){
		this.addParameter("id", id);
	}
	
	public String getID(){
		return (String)this.getParameter("id");
	}

//	填单日期	ApplyDate_	TIMESTAMP			FALSE	FALSE	FALSE
	public void setBeginApplyDate(Timestamp beginApplyDate){
		this.addParameter("beginApplyDate", beginApplyDate);
	}
	
	public Timestamp getBeginApplyDate(){
		return (Timestamp)this.getParameter("beginApplyDate");
	}
	
	public void setEndApplyDate(Timestamp endApplyDate){
		this.addParameter("endApplyDate", endApplyDate);
	}
	
	public Timestamp getEndApplyDate(){
		return (Timestamp)this.getParameter("endApplyDate");
	}
	
//	归案时间
	public void setBeginArchiveDate(Timestamp beginArchiveDate){
		this.addParameter("beginArchiveDate", beginArchiveDate);
	}
	
	public Timestamp getBeginArchiveDate(){
		return (Timestamp)this.getParameter("beginArchiveDate");
	}
	
	public void setEndArchiveDate(Timestamp endArchiveDate){
		this.addParameter("endArchiveDate", endArchiveDate);
	}
	
	public Timestamp getEndArchiveDate(){
		return (Timestamp)this.getParameter("endArchiveDate");
	}
	

	//	受款人	Payee_	VARCHAR2(128)	128		FALSE	FALSE	FALSE
	public void setApplicant(String applicant){
		this.addParameter("applicant", applicant);
	}
	
	public String getApplicant(){
		return (String)this.getParameter("applicant");
	}
	
	public void setBudgetItem(String budgetItemName){
		this.addParameter("budgetItem", budgetItemName);
	}
	
	public String getBudgetItem(){
		return (String)this.getParameter("budgetItem");
	}
	
	public void setPassed(Boolean passed){
		this.addParameter("passed", passed);
	}
	
	public Boolean getPassed(){
		return (Boolean)this.getParameter("passed");
	}

	public void setFinance(String finance){
		this.addParameter("finance", finance);
	}
	
	public String getFinance(){
		return (String) this.getParameter("finance");
	}
//	public void setYear(Integer year){
//		this.addParameter("year", year);
//	}
//	
//	public Integer getYear(){
//		return (Integer)this.getParameter("year");
//	}
}
