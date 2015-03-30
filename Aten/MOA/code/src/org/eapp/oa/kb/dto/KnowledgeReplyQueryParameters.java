package org.eapp.oa.kb.dto;

import java.sql.Timestamp;

import org.eapp.util.hibernate.QueryParameters;


public class KnowledgeReplyQueryParameters  extends QueryParameters{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3912974465864477489L;
	public String getKnowledgeClass() {
		return (String) this.getParameter("knowledge");
	}
	public void setKnowledgeClass(String knowledge) {
		this.addParameter("knowledge", knowledge);
	}
	
	public Timestamp getReplyDate(){
		return (Timestamp) this.getParameter("replyDate");
	}
	public void setReplyDate(Timestamp replyDate) {
		this.addParameter("replyDate", replyDate);
	}
}
