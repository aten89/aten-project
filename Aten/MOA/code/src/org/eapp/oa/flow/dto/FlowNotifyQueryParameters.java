/**
 * 
 */
package org.eapp.oa.flow.dto;

import java.sql.Timestamp;

import org.eapp.util.hibernate.QueryParameters;


/**
 */
public class FlowNotifyQueryParameters extends QueryParameters {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1458190118301240692L;

	public void setSubject(String subject){
		this.addParameter("subject", subject);
	}
	public String getSubject(){
		return (String)this.getParameter("subject");
	}
	
	public void setCreator(String creator){
		this.addParameter("creator", creator);
	}
	
	public String getCreator(){
		return (String) this.getParameter("creator");
	}
	
	public void setFlowClass(String flowClass){
		this.addParameter("flowClass", flowClass);
	}
	
	public String getFlowClass(){
		return (String)this.getParameter("flowClass");
	}
	
	public void setFlowStatus(Integer flowStatus){
		this.addParameter("flowStatus", flowStatus);
	}
	
	public Integer getFlowStatus(){
		return (Integer)this.getParameter("flowStatus");
	}
	
	public void setBeginNotifyTime(Timestamp beginNotifyTime){
		this.addParameter("beginNotifyTime", beginNotifyTime);
	}

	public Timestamp getBeginNotifyTime(){
		return (Timestamp)this.getParameter("beginNotifyTime");
	}
	
	public void setEndNotifyTime(Timestamp endNotifyTime){
		this.addParameter("endNotifyTime", endNotifyTime);
	}

	public Timestamp getEndNotifyTime(){
		return (Timestamp)this.getParameter("endNotifyTime");
	}

}
