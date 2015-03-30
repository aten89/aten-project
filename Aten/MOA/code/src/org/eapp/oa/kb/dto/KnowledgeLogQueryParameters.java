package org.eapp.oa.kb.dto;

import java.sql.Timestamp;

import org.eapp.util.hibernate.QueryParameters;


public class KnowledgeLogQueryParameters extends QueryParameters {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2427283209603822886L;

	public void setBegingOperatetime(Timestamp operatetime) {
		if (operatetime != null) {
			super.addParameter("beginOperatetime",operatetime);
		}
	}
	
	public Timestamp getBeginOperatetime(){
		return (Timestamp)super.getParameter("beginOperatetime");
	}
	
	public void setEndOperatetime(Timestamp operatetime) {
		if (operatetime != null) {
			super.addParameter("endOperatetime",operatetime);
		}
	}
	
	public Timestamp getEndOperatetime(){
		return (Timestamp)super.getParameter("endOperatetime");
	}	
	
	public void setUserid(String userid) {
		if (userid != null) {
			super.addParameter("userid", userid);
		}
	}
	
	public String getUserid() {
		return (String)super.getParameter("userid");
	}
	
	public void setKnowledgeid(String knowledgeid) {
		if (knowledgeid != null) {
			super.addParameter("knowledgeid", knowledgeid);
		}
	}
	
	public String getKnowledgeid() {
		return (String)super.getParameter("knowledgeid");
	}
	
	public void setKnowledgetitle(String knowledgetitle) {
		if (knowledgetitle != null) {
			super.addParameter("knowledgetitle", knowledgetitle);
		}
	}
	
	public String getKnowledgetitle() {
		return (String)super.getParameter("knowledgetitle");
	}
}
