package org.eapp.oa.flow.dto;

import org.eapp.util.hibernate.QueryParameters;


/**
 * <p>Title: </p>
 * <p>Description: 流程配置查询条件</p>
 * @version 1.0 
 */
public class FlowConfigQueryParameters extends QueryParameters {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2883482604885788371L;

	public String getId() {
        return (String)this.getParameter("id");
    }
    
    public void setId(String id) {
        this.addParameter("id", id);
    }

    public String getFlowClass() {
        return (String)this.getParameter("flowClass");
    }
    
    public void setFlowClass(String flowClass) {
    	this.addParameter("flowClass", flowClass);
    }

    public String getFlowKey() {
    	return (String)this.getParameter("flowKey");
    }
    
    public void setFlowKey(String flowKey) {
    	this.addParameter("flowKey", flowKey);
    }

    public String getFlowName() {
    	return (String)this.getParameter("flowName");
    }
    
    public void setFlowName(String flowName) {
    	this.addParameter("flowName", flowName);
    }

    public Long getFlowVersion() {
    	return (Long)this.getParameter("flowVersion");
    }
    
    public void setFlowVersion(Long flowVersion) {
    	this.addParameter("flowVersion", flowVersion);
    }

    public Integer getDraftFlag() {
    	return (Integer)this.getParameter("draftFlag");
    }
    
    public void setDraftFlag(Integer draftFlag) {
    	this.addParameter("draftFlag", draftFlag);
    }
    
}
