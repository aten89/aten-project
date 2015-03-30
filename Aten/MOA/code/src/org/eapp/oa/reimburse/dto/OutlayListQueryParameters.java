/**
 * 
 */
package org.eapp.oa.reimburse.dto;

import java.sql.Timestamp;
import java.util.List;

import org.eapp.util.hibernate.QueryParameters;


/**
 * @author linliangyi
 * 报销明细查询
 *
 */
public class OutlayListQueryParameters extends QueryParameters {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6202011090941483021L;

	//报销单
	public void setReimbursementId(String reimbursementId){
		this.addParameter("reimbursementId", reimbursementId);
	}
	
	public String getReimbursementId(){
		return (String)this.getParameter("reimbursementId");
	}
	public void setOption(String option){
		this.addParameter("searchOption", option);
	}
	
	public String getOption(){
		return (String)this.getParameter("searchOption");
	}
	public void setBudgetId(String budgetId){
		this.addParameter("budgetId", budgetId);
	}
	
	public String getBudgetId(){
		return (String)this.getParameter("budgetId");
	}
	
	//	受款人	Payee_	VARCHAR2(128)	128		FALSE	FALSE	FALSE
	public void setApplicant(String applicant){
		this.addParameter("applicant", applicant);
	}
	
	public String getApplicant(){
		return (String)this.getParameter("applicant");
	}
	
	//费用归属项目（与Reimbursement的联合查询）
	public void setBudgetItem(String budgetItemName){
		this.addParameter("budgetItem", budgetItemName);
	}

	public String getBudgetItem(){
		return (String)this.getParameter("budgetItem");
	}
	
//	填单日期（与Reimbursement的联合查询）	ApplyDate_	TIMESTAMP			FALSE	FALSE	FALSE
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
	

//	受款人（与Reimbursement的联合查询）	Payee_	VARCHAR2(128)	128		FALSE	FALSE	FALSE
	public void setPayee(String payee){
		this.addParameter("payee", payee);
	}
	
	public String getPayee(){
		return (String)this.getParameter("payee");
	}
	
//	费用类别	OutlayCategory_	VARCHAR2(128)	128		FALSE	FALSE	FALSE
	public void setOutlayCategory(String outlayCategory){
		this.addParameter("outlayCategory", outlayCategory);
	}
	
	public String getOutlayCategory(){
		return (String)this.getParameter("outlayCategory");
	}
	
	
//	费用名称	OutlayName_	VARCHAR2(128)	128		FALSE	FALSE	FALSE
	public void setOutlayName(String outlayName){
		this.addParameter("outlayName", outlayName);
	}
	
	public String getOutlayName(){
		return (String)this.getParameter("outlayName");
	}
	
//	public void setCsProject(String csProject){
//		this.addParameter("csProject", csProject);
//	}
//	
//	public String getCsProject(){
//		return (String)this.getParameter("csProject");
//	}
	
	/**
	 * 2013-3-5 设置多个项目组查询条件
	 * @param csProjects 多个项目组
	 *
	 * <pre>
	 * 修改日期		修改人	修改原因
	 * 2013-3-5	           李海根	新建
	 * </pre>
	 */
	public void setCsProjects(List<String> csProjects){
        this.addParameter("csProjects", csProjects);
    }
    
	/**
	 * 2013-3-5 获取多个项目组查询条件
	 * @return 多个项目组
	 *
	 * <pre>
	 * 修改日期		修改人	修改原因
	 * 2013-3-5	           李海根	新建
	 * </pre>
	 */
    @SuppressWarnings("unchecked")
    public List<String> getCsProjects(){
        return (List<String>)this.getParameter("csProjects");
    }
    
	public void setRegionName(String regionName){
		this.addParameter("regionName", regionName);
	}
	public String getRegionName(){
		return (String)this.getParameter("regionName");
	}
	
	public void setTravelBeginDate(Timestamp travelBeginDate){
		this.addParameter("travelBeginDate", travelBeginDate);
	}
	
	public Timestamp getTravelBeginDate(){
		return (Timestamp)this.getParameter("travelBeginDate");
	}
	
	public void setTravelEndDate(Timestamp travelEndDate){
		this.addParameter("travelEndDate", travelEndDate);
	}
	
	public Timestamp getTravelEndDate(){
		return (Timestamp)this.getParameter("travelEndDate");
	}
	public void setYear(Integer year){
		this.addParameter("year", year);
	}
	
	public Integer getYear(){
		return (Integer)this.getParameter("year");
	}
	public Integer getBYear(){
		return (Integer)this.getParameter("byear");
	}
	public void setBYear(Integer year){
		this.addParameter("byear", year);
	}
	public void setMonth(Integer month){
		this.addParameter("month", month);
	}
	
	public Integer getMonth(){
		return (Integer)this.getParameter("month");
	}
	
	public void setType(Integer type){
		this.addParameter("type", type);
	}
	
	public Integer getType(){
		return (Integer)this.getParameter("type");
	}
}
