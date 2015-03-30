/**
 * 
 */
package org.eapp.workflow.trace;

import java.util.Date;

/**
 * 流程轨迹点
 */
public class TracePoint implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5521767670242602661L;
	
	public static final String ELEMENTTYPE_NODE = "NODE";//节点
	public static final String ELEMENTTYPE_TRANSITION = "TRANSITION";//转向
	
	public static final String ACTIONTYPE_ENTER = "ENTER";//进入
	public static final String ACTIONTYPE_LEAVE = "LEAVE";//离开
	
	private String id;
	private String flowDefineId;//流程定义ID
	private String flowInstanceId;//流程实例ID
	private String elementId;//记录的元素ID
	private String elementType;//记录的元素类型
	private String actionType;//动作类型
	private Date snapTime;//记录时间
	private String refGraphKey;//对应图形的ID
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFlowDefineId() {
		return flowDefineId;
	}
	public void setFlowDefineId(String flowDefineId) {
		this.flowDefineId = flowDefineId;
	}
	public String getFlowInstanceId() {
		return flowInstanceId;
	}
	public void setFlowInstanceId(String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
	}
	public String getElementId() {
		return elementId;
	}
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	public String getElementType() {
		return elementType;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public Date getSnapTime() {
		return snapTime;
	}
	public void setSnapTime(Date snapTime) {
		this.snapTime = snapTime;
	}
	public String getRefGraphKey() {
		return refGraphKey;
	}
	public void setRefGraphKey(String refGraphKey) {
		this.refGraphKey = refGraphKey;
	}

}
