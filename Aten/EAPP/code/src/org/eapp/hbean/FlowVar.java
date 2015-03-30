package org.eapp.hbean;

import java.util.Map;

import org.eapp.sys.config.SysCodeDictLoader;
import org.eapp.sys.config.SysConstants;

/**
 * 
 * @author Administrator
 * @version Nov 28, 2014
 */
public class FlowVar implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1408330630979928273L;
	
	//变量类型。STRING/BOOLEAN/INT/LONG/FLOAT/DOUBLE/DATE
	public static String TYPE_STRING = "DATATYPE_STRING";
	public static String TYPE_BOOLEAN = "DATATYPE_BOOLEAN";
	public static String TYPE_INT = "DATATYPE_INT";
	public static String TYPE_LONG = "DATATYPE_LONG";
	public static String TYPE_FLOAT = "DATATYPE_FLOAT";
	public static String TYPE_DOUBLE = "DATATYPE_DOUBLE";
	public static String DATE = "DATATYPE_DATE";
	
	private String varId;			//元数据标识
	private String flowClass;	//流程类别
	private String name;		//元数据名称
	private String displayName;	//显示名称
	private String type;			//类型
	private Boolean notNull;		//能否为空
	private Boolean globalFlag;	//是否全局公用
	

	// Constructors

	/** default constructor */
	public FlowVar() {
	}


	/** full constructor */
	public FlowVar(String varId, String flowClass, String name,
			String displayName, String type, Boolean notNull, Boolean globalFlag) {
		this.varId = varId;
		this.flowClass = flowClass;
		this.name = name;
		this.displayName = displayName;
		this.type = type;
		this.notNull = notNull;
		this.globalFlag = globalFlag;
	}


	public String getVarId() {
		return varId;
	}


	public void setVarId(String varId) {
		this.varId = varId;
	}


	public String getFlowClass() {
		return flowClass;
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

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Boolean getNotNull() {
		return notNull;
	}


	public void setNotNull(Boolean notNull) {
		this.notNull = notNull;
	}


	public Boolean getGlobalFlag() {
		return globalFlag;
	}


	public void setGlobalFlag(Boolean globalFlag) {
		this.globalFlag = globalFlag;
	}

	// Property accessors
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(this.getClass().getName());
		sb.append("[");
		sb.append("varId=");
		sb.append(getVarId());
		sb.append(",");
		sb.append("flowClass=");
		sb.append(getFlowClass());
		sb.append(",");
		sb.append("name=");
		sb.append(getName());
		sb.append(",");
		sb.append("displayName=");
		sb.append(getDisplayName());
		sb.append(",");
		sb.append("type=");
		sb.append(getType());
		sb.append(",");
		sb.append("notNull=");
		sb.append(getNotNull());
		sb.append("globalFlag=");
		sb.append(getGlobalFlag());
		sb.append("]");
		return sb.toString();
	}
}