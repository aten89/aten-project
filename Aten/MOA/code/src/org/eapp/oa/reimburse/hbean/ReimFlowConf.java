package org.eapp.oa.reimburse.hbean;

// default package

/**
 * OutlayList entity.
 * Description:费用明细
 * @author MyEclipse Persistence Tools
 */

public class ReimFlowConf implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5220865752240853001L;
	private String id;
	private String groupName;
//	private String flowClass;
	private String flowKey;
	private String description;
	
	private transient String flowName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
//	public String getFlowClass() {
//		return flowClass;
//	}
//	public void setFlowClass(String flowClass) {
//		this.flowClass = flowClass;
//	}
	public String getFlowKey() {
		return flowKey;
	}
	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

}