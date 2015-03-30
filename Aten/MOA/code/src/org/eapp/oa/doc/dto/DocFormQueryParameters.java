package org.eapp.oa.doc.dto;

import java.sql.Timestamp;

import org.eapp.util.hibernate.QueryParameters;


public class DocFormQueryParameters extends QueryParameters{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1666439545848566668L;
	//起草人
	public void setDraftsman(String draftsman){
		this.addParameter("draftsman", draftsman);
	}
	public String getDraftsman(){
		return (String)this.getParameter("draftsman");
	}
	//机构
	public void setGroupName(String groupName){
		this.addParameter("groupName", groupName);
	}
	public String getGroupName(){
		return (String)this.getParameter("groupName");
	}
	
	//文件类别
	public void setDocClassName(String docClassName){
		this.addParameter("docClassName", docClassName);
	}
	public String getDocClassName(){
		return (String)this.getParameter("docClassName");
	}
	
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
	public void setFileClass(int fileClass){
		this.addParameter("fileClass", fileClass);
	}
	public int getFileClass(){
		return (Integer)this.getParameter("fileClass");
	}
	//状态从Information中查
	public void setDocStatus(int property){
		this.addParameter("docStatus", property);
	}
	public Integer getDocStatus(){
		return (Integer)this.getParameter("docStatus");
	}
}
