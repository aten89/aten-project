package org.eapp.oa.flow.hbean;

import org.eapp.workflow.def.FlowDefine;



/**
 * FlowConfig entity. @author MyEclipse Persistence Tools
 */

public class FlowConfig  implements java.io.Serializable {

	/**
	 * 流程标志：草稿
	 */
	public static final int FLOW_FLAG_DRAFT = 1;
	
	/**
	 * 流程标志：已发布
	 */
	public static final int FLOW_FLAG_PUBLISHED = 2;
	
	/**
	 * 流程标志：已禁用
	 */
	public static final int FLOW_FLAG_DISABLE = 3;

    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 3720716896866557124L;
	 private String id;			//id
     private String flowClass;	//流程类别
     private String flowKey;	//流程key
     private String flowName;	//流程名称
     private Long flowVersion;	//流程版本
     private Integer draftFlag;	//草稿标识

     private FlowDefine flowDefine;
     
    // Constructors

    /** default constructor */
    public FlowConfig() {
    }

	/** minimal constructor */
    public FlowConfig(String id) {
        this.id = id;
    }
    
    /** full constructor */
    public FlowConfig(String id, String flowClass, String flowKey, String flowName, Long flowVersion, Integer draftFlag) {
        this.id = id;
        this.flowClass = flowClass;
        this.flowKey = flowKey;
        this.flowName = flowName;
        this.flowVersion = flowVersion;
        this.draftFlag = draftFlag;
    }

   
    // Property accessors

    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getFlowClass() {
        return this.flowClass;
    }
    
    public void setFlowClass(String flowClass) {
        this.flowClass = flowClass;
    }

    public String getFlowKey() {
        return this.flowKey;
    }
    
    public void setFlowKey(String flowKey) {
        this.flowKey = flowKey;
    }

    public String getFlowName() {
        return this.flowName;
    }
    
    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public Long getFlowVersion() {
        return this.flowVersion;
    }
    
    public void setFlowVersion(Long flowVersion) {
        this.flowVersion = flowVersion;
    }

	public Integer getDraftFlag() {
		return draftFlag;
	}

	public void setDraftFlag(Integer draftFlag) {
		this.draftFlag = draftFlag;
	}

	public FlowDefine getFlowDefine() {
		return flowDefine;
	}

	public void setFlowDefine(FlowDefine flowDefine) {
		this.flowDefine = flowDefine;
	}
   
}