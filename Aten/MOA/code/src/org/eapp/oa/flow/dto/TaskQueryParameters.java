/**
 * 
 */
package org.eapp.oa.flow.dto;

import java.sql.Timestamp;

import org.eapp.util.hibernate.QueryParameters;


/**
 * @author 林良益
 * 预算申请单查询参数
 */
public class TaskQueryParameters extends QueryParameters {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1458190118301240692L;

	public void setFormId(String formId){
		this.addParameter("formId", formId);
	}
	public String getFormId(){
		return (String)this.getParameter("formId");
	}
	
	public void setDesc(String desc){
		this.addParameter("desc", desc);
	}
	
	public String getDesc(){
		return (String) this.getParameter("desc");
	}
	
	public void setBeginCreateTime(Timestamp beginCreateTime){
		this.addParameter("beginCreateTime", beginCreateTime);
	}

	public Timestamp getBeginCreateTime(){
		return (Timestamp)this.getParameter("beginCreateTime");
	}
	
	public void setEndCreateTime(Timestamp endCreateTime){
		this.addParameter("endCreateTime", endCreateTime);
	}

	public Timestamp getEndCreateTime(){
		return (Timestamp)this.getParameter("endCreateTime");
	}

	public void setTransactor(String transactor){
		this.addParameter("transactor", transactor);
	}
	
	public String getTransactor(){
		return (String)this.getParameter("transactor");
	}
	
	public void setStates(String[] state){
		this.addParameter("states", state);
	}
	
	public String[] getState(){
		return (String[])this.getParameter("states");
	}
}
