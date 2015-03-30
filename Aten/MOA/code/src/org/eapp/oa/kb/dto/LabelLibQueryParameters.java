package org.eapp.oa.kb.dto;

import org.eapp.util.hibernate.QueryParameters;

public class LabelLibQueryParameters extends QueryParameters {
	/**
	 * 
	 */
	private static final long serialVersionUID = -129675238052679472L;
	public String getName() {
		return (String) this.getParameter("name");
	}
	public void setName(String name) {
		this.addParameter("name", name);
	}
	
	public Long getStartCount() {
		return (Long) this.getParameter("stratCount");
	}
	public void setStartCount(Long count) {
		this.addParameter("stratCount", count);
	}
	
	public Long getEndCount() {
		return (Long) this.getParameter("endCount");
	}
	public void setEndCount(Long count) {
		this.addParameter("endCount", count);
	}
}
