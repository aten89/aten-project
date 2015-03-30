package org.eapp.hbean;

import java.util.Map;

import org.eapp.sys.config.SysCodeDictLoader;
import org.eapp.sys.config.SysConstants;

/**
 * 
 * @author Administrator
 * @version Nov 28, 2014
 */
public class FlowHandler implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1408330630979928273L;
	
	//Hanlder类型
	public static String TYPE_ACTION = "ACTION";
	public static String TYPE_ACTION_TASKEVENT = "ACTION_TASKEVENT";//任务事件动作（ACTION的特殊）
	public static String TYPE_ASSIGN = "ASSIGN";//任务授权
	public static String TYPE_MUTIASSIGN = "MUTIASSIGN";//多人任务授权
	public static String TYPE_DECISION = "DECISION";//判断节点
	public static String TYPE_VIEW = "VIEW";//任务表单
	
	private String handId;
	private String flowClass;	//流程类别
	private String name;		//名称
	private String handlerClass;	//类名
	private String type;			//类型
	private Boolean globalFlag;	//是否全局公用

	// Constructors

	/** default constructor */
	public FlowHandler() {
	}

	/** full constructor */
	public FlowHandler(String handId, String flowClass, String name,
			String handlerClass, String type, Boolean globalFlag) {
		this.handId = handId;
		this.flowClass = flowClass;
		this.name = name;
		this.handlerClass = handlerClass;
		this.type = type;
		this.globalFlag = globalFlag;
	}

	public String getHandId() {
		return handId;
	}

	public void setHandId(String handId) {
		this.handId = handId;
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

	public String getHandlerClass() {
		return handlerClass;
	}

	public void setHandlerClass(String handlerClass) {
		this.handlerClass = handlerClass;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getGlobalFlag() {
		return globalFlag;
	}

	public void setGlobalFlag(Boolean globalFlag) {
		this.globalFlag = globalFlag;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(this.getClass().getName());
		sb.append("[");
		sb.append("handId=");
		sb.append(getHandId());
		sb.append(",");
		sb.append("flowClass=");
		sb.append(getFlowClass());
		sb.append(",");
		sb.append("name=");
		sb.append(getName());
		sb.append(",");
		sb.append("handlerClass=");
		sb.append(getHandlerClass());
		sb.append(",");
		sb.append("type=");
		sb.append(getType());
		sb.append(",");
		sb.append("globalFlag=");
		sb.append(getGlobalFlag());
		sb.append("]");
		return sb.toString();
	}
	
}