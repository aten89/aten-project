package org.eapp.oa.flow.hbean;

// default package

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.eapp.client.util.UsernameCache;


/**
 * Task entity.
 * Description:任务
 * @author MyEclipse Persistence Tools
 */

public class Task implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4237974902084123284L;
	//ID_,VARCHAR2(36),不能为空                             --主键ID
	private String id;
	//TaskInstanceID_,VARCHAR2(36)                     --任务实例ID
	private String taskInstanceID;
	//FlowInstanceID_,VARCHAR2(36)                     --流程实例ID
	private String flowInstanceID;
	//FlowKey_,VARCHAR2(36)                            --流程标识
	private String flowKey;
	//FlowDefineID_,VARCHAR2(36)                       --流程定义ID
	private String flowDefineID;
	//TaskState_,VARCHAR2(20)                          --任务状态
	private String taskState;
	//FormID_,VARCHAR2(36)                             --表单ID
	private String formID;
	//Transactor_,VARCHAR2(36)                         --待办人
	private String transactor;
	//CreateTime_,TIMESTAMP							   --创建时间
	private Date createTime;
	//StartTime_,TIMESTAMP							   --开始时间
	private Date startTime;
	//ENDTIME_,TIMESTAMP 								--结束时间
	private Date endTime;
	//   COMMENT_,VARCHAR2(4000) 						--审批意见
	private String comment;
	//   NODENAME_,VARCHAR2(400) 						--节点名称
	private String nodeName;
	//  TASKNAME_,VARCHAR2(400) 						--任务名称
	private String taskName;
	//  FLOWNAME_,VARCHAR2(400) 						--流程名称
	private String flowName;
	
	private Boolean viewFlag;
	//Description_,VARCHAR2(400)                        --描述
	private String description;
	   
	private Set<TaskAssign> taskAssigns = new HashSet<TaskAssign>(0);
	
	private transient FlowConfig flowConfig;
	
	// Constructors

	/** default constructor */
	public Task() {
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskInstanceID() {
		return this.taskInstanceID;
	}

	public void setTaskInstanceID(String taskInstanceID) {
		this.taskInstanceID = taskInstanceID;
	}

	public String getFlowInstanceID() {
		return this.flowInstanceID;
	}

	public void setFlowInstanceID(String flowInstanceID) {
		this.flowInstanceID = flowInstanceID;
	}

	public String getFlowKey() {
		return this.flowKey;
	}

	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}

	public String getFlowDefineID() {
		return this.flowDefineID;
	}

	public void setFlowDefineID(String flowDefineID) {
		this.flowDefineID = flowDefineID;
	}

	public String getTaskState() {
		return this.taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	public String getFormID() {
		return this.formID;
	}

	public void setFormID(String formID) {
		this.formID = formID;
	}

	public String getTransactor() {
		return this.transactor;
	}

	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}

	public Set<TaskAssign> getTaskAssigns() {
		return this.taskAssigns;
	}

	public void setTaskAssigns(Set<TaskAssign> taskAssigns) {
		this.taskAssigns = taskAssigns;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Task other = (Task) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public FlowConfig getFlowConfig() {
		return flowConfig;
	}

	public void setFlowConfig(FlowConfig flowConfig) {
		this.flowConfig = flowConfig;
	}

	public String getTransactorDisplayName() {
		String transactorName ="";
		if(transactor == null && taskAssigns!=null){
			for(TaskAssign assign:taskAssigns){
				if(!"".equals(transactorName)){
					transactorName += ",";
				}
				transactorName += assign.getAssignKey();
			}
		}else{
			transactorName = UsernameCache.getDisplayName(transactor);
		}
		return transactorName;
	}

	public Boolean getViewFlag() {
		return viewFlag;
	}

	public void setViewFlag(Boolean viewFlag) {
		this.viewFlag = viewFlag;
	}
}