package org.eapp.dao.param;

import org.eapp.util.hibernate.QueryParameters;

/**
 * 流程配置查询条件
 */
public class FlowConfigQueryParameters extends QueryParameters {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2883482604885788371L;

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

    public Integer getDraftFlag() {
    	return (Integer)this.getParameter("draftFlag");
    }
    
    public void setDraftFlag(Integer draftFlag) {
    	this.addParameter("draftFlag", draftFlag);
    }
}
