package org.eapp.hbean;

import java.util.Map;

import org.eapp.sys.config.SysCodeDictLoader;
import org.eapp.sys.config.SysConstants;

/**
 * 
 * @author Administrator
 * @version Nov 28, 2014
 */
public class FlowConfig  implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3720716896866557124L;
	
	public static final int FLOW_FLAG_DRAFT = 1;//草稿
	public static final int FLOW_FLAG_PUBLISHED = 2;//已发布
	public static final int FLOW_FLAG_DISABLE = 3;//禁用

    // Fields    
	 private String confId;		//id
     private String flowClass;	//流程类别
     private String flowKey;	//流程key
     private String flowName;	//流程名称
     private Long flowVersion;	//流程版本
     private Integer draftFlag;	//草稿标识
     
    // Constructors

    /** default constructor */
    public FlowConfig() {
    }

    /** full constructor */
    public FlowConfig(String confId, String flowClass, String flowKey, String flowName, Long flowVersion, Integer draftFlag) {
        this.confId = confId;
        this.flowClass = flowClass;
        this.flowKey = flowKey;
        this.flowName = flowName;
        this.flowVersion = flowVersion;
        this.draftFlag = draftFlag;
    }

   
    // Property accessors

    public String getConfId() {
        return this.confId;
    }
    
    public void setConfId(String confId) {
        this.confId = confId;
    }

    public String getFlowClass() {
        return this.flowClass;
    }
    
    public void setFlowClass(String flowClass) {
        this.flowClass = flowClass;
    }
    
    public String getFlowClassName() {
        if (this.flowClass != null) {
	        Map<String, DataDictionary> typeMap = SysCodeDictLoader.getInstance().getDataDictsByType(SysConstants.DICT_KEY_FLOW_CLASS);
	        if (typeMap != null) {
	        	DataDictionary dict = typeMap.get(flowClass);
	        	if (dict != null) {
	        		return dict.getDictName();
	        	}
	        }
        }
        return "";
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
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(this.getClass().getName());
		sb.append("[");
		sb.append("confId=");
		sb.append(getConfId());
		sb.append(",");
		sb.append("flowClass=");
		sb.append(getFlowClass());
		sb.append(",");
		sb.append("flowKey=");
		sb.append(getFlowKey());
		sb.append(",");
		sb.append("flowName=");
		sb.append(getFlowName());
		sb.append(",");
		sb.append("flowVersion=");
		sb.append(getFlowVersion());
		sb.append(",");
		sb.append("draftFlag=");
		sb.append(getDraftFlag());
		sb.append("]");
		return sb.toString();
	}
}