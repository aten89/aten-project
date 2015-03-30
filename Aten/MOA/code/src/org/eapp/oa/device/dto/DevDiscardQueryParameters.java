package org.eapp.oa.device.dto;

import java.sql.Timestamp;

import org.eapp.util.hibernate.QueryParameters;


/**
 * 设备报废单查询参数
 * 
 * @author sds
 * @version 2009-09-08
 */
public class DevDiscardQueryParameters extends QueryParameters {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8146820452665642768L;
	public String getDeviceName() {
		return (String) this.getParameter("deviceName");
	}
	public void setDeviceName(String deviceName) {
		this.addParameter("deviceName", deviceName);
	}
	
//	public String getPublisher() {
//		return (String) this.getParameter("publisher");
//	}
//	public void setPublisher(String publisher) {
//		this.addParameter("publisher", publisher);
//	}
	
//	public void setStatus(Integer status){
//		this.addParameter("status", status);
//	}
//	
//	public Integer getStatus(){
//		return (Integer)this.getParameter("status");
//	}
	
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
}
