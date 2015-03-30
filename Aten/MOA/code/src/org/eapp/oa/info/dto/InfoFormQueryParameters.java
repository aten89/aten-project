package org.eapp.oa.info.dto;

import java.sql.Timestamp;

import org.eapp.util.hibernate.QueryParameters;


public class InfoFormQueryParameters extends QueryParameters{
	/**
	 * 
	 */
	private static final long serialVersionUID = 624864396196899451L;
	//标题
	public void setSubject(String title){
		this.addParameter("subject", title);
	}
	public String getSubject(){
		return (String)this.getParameter("subject");
	}
	//起草时间
	public void setBeginDraftDate(Timestamp beginDraftDate){
		this.addParameter("beginDraftDate", beginDraftDate);
	}
	
	public Timestamp getBeginDraftDate(){
		return (Timestamp)this.getParameter("beginDraftDate");
	}
	
	public void setEndDraftDate(Timestamp endDraftDate){
		this.addParameter("endDraftDate", endDraftDate);
	}
	
	public Timestamp getEndDraftDate(){
		return (Timestamp)this.getParameter("endDraftDate");
	}
	//归档时间
	public void setBeginArchDate(Timestamp beginDraftDate){
		this.addParameter("beginArchDate", beginDraftDate);
	}
	
	public Timestamp getBeginArchDate(){
		return (Timestamp)this.getParameter("beginArchDate");
	}
	
	public void setEndArchDate(Timestamp endArchDate){
		this.addParameter("endArchDate", endArchDate);
	}
	
	public Timestamp getEndArchDate(){
		return (Timestamp)this.getParameter("endArchDate");
	}
	//是否通过
	public void setPassed(Boolean passed){
		this.addParameter("passed", passed);
	}
	public Boolean getPassed(){
		return (Boolean)this.getParameter("passed");
	}
	
	//状态从Information中查
	public void setInfoStatus(int property){
		this.addParameter("infoStatus", property);
	}
	public Integer getInfoStatus(){
		return (Integer)this.getParameter("infoStatus");
	}
}
