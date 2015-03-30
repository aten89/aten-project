package org.eapp.oa.flow.hbean;

import java.util.Date;

/**
 * FlowNotify entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class FlowNotify implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1038502364927187840L;
	
	public static final int NOTIFYTYPE_NOW =  1;//立即知会
	public static final int NOTIFYTYPE_END =  3;//流程结束后知会
	
	public static final int STATUS_CREATE =  0;//登记
	public static final int STATUS_NOTIFY =  1;//知会
	public static final int STATUS_READED =  2;//已读
	
	public static final int FLOW_STATUS_DEAL =  2;//审批中
	public static final int FLOW_STATUS_PASS =  1;//通过
	public static final int FLOW_STATUS_CANCEL =  0;//作废
	
	private String id;			//
	private String subject;	//知会标题
	private String notifyUser;		//知会人
	private int notifyType;		//知会环环节
	private String creator;	//发起知会人
	private String groupFullName;	//发起人部门
	private Date createTime;	//发起时间
	private int status;		//状态
	private String flowClass;	//流程类别
	private Date notifyTime;	//知会时间
	private String refFormID;	//关联表单ID
	private String viewDetailURL;	//查看地址
	private int flowStatus;		//流程状态
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getNotifyUser() {
		return notifyUser;
	}
	public void setNotifyUser(String notifyUser) {
		this.notifyUser = notifyUser;
	}
	public int getNotifyType() {
		return notifyType;
	}
	public void setNotifyType(int notifyType) {
		this.notifyType = notifyType;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getGroupFullName() {
		return groupFullName;
	}
	public void setGroupFullName(String groupFullName) {
		this.groupFullName = groupFullName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getFlowClass() {
		return flowClass;
	}
	public void setFlowClass(String flowClass) {
		this.flowClass = flowClass;
	}
	public Date getNotifyTime() {
		return notifyTime;
	}
	public void setNotifyTime(Date notifyTime) {
		this.notifyTime = notifyTime;
	}
	public String getRefFormID() {
		return refFormID;
	}
	public void setRefFormID(String refFormID) {
		this.refFormID = refFormID;
	}
	public String getViewDetailURL() {
		return viewDetailURL;
	}
	public void setViewDetailURL(String viewDetailURL) {
		this.viewDetailURL = viewDetailURL;
	}
	public int getFlowStatus() {
		return flowStatus;
	}
	public void setFlowStatus(int flowStatus) {
		this.flowStatus = flowStatus;
	}
}