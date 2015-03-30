package org.eapp.flow.db;

import java.util.Date;
import java.util.Set;
import org.eapp.client.util.UsernameCache;


/**
 * 流程对照的任务实体，对应表在子系统中创建
 * @author Administrator
 * @version Dec 4, 2014
 */
public class FlowTask implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4237974902084123284L;
	
	private String taskId;//主键
	private String flowClass;//任务类别
	private String flowKey;//流程标识
	private String taskInstanceId;//任务实例ID
	private String flowInstanceId;//流程实例ID
	private String flowDefineId;//流程定义ID
	private String taskState;//任务状态
	private String formId;//关联任务表单ID
	private String transactor;//待办人
	private Date createTime;//创建时间
	private Date startTime;//开始时间
	private Date endTime;//结束时间
	private String comment;//审批意见
	private String nodeName;//节点名称
	private String taskName;//任务名称
	private String flowName;//流程名称
	private Boolean viewFlag;//查看标志
	private String description;//任务描述
	private Set<FlowTaskAssign> taskAssigns;//任务授权
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getFlowClass() {
		return flowClass;
	}

	public void setFlowClass(String flowClass) {
		this.flowClass = flowClass;
	}

	public String getFlowKey() {
		return flowKey;
	}

	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}

	public String getTaskInstanceId() {
		return taskInstanceId;
	}

	public void setTaskInstanceId(String taskInstanceId) {
		this.taskInstanceId = taskInstanceId;
	}

	public String getFlowInstanceId() {
		return flowInstanceId;
	}

	public void setFlowInstanceId(String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
	}

	public String getFlowDefineId() {
		return flowDefineId;
	}

	public void setFlowDefineId(String flowDefineId) {
		this.flowDefineId = flowDefineId;
	}

	public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getTransactor() {
		return transactor;
	}

	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
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

	public Boolean getViewFlag() {
		return viewFlag;
	}

	public void setViewFlag(Boolean viewFlag) {
		this.viewFlag = viewFlag;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<FlowTaskAssign> getTaskAssigns() {
		return taskAssigns;
	}

	public void setTaskAssigns(Set<FlowTaskAssign> taskAssigns) {
		this.taskAssigns = taskAssigns;
	}

	public String getTransactorNames() {
		if (transactor != null && !transactor.trim().equals("")) {
			return UsernameCache.getDisplayName(transactor);
		}
		StringBuffer transactorNames = new StringBuffer();
		if (taskAssigns != null && !taskAssigns.isEmpty()) {
			for(FlowTaskAssign assign : taskAssigns){
				if (transactorNames.length() > 0) {
					transactorNames.append(',');
				}
				if (FlowTaskAssign.TYPE_USER.equals(assign.getType())) {
					transactorNames.append(UsernameCache.getDisplayName(assign.getAssignKey()));
				} else {
					//非用户类型直接拼接
					transactorNames.append(assign.getAssignKey());
				}
			}
		}
		return transactorNames.toString();
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
		result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
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
		final FlowTask other = (FlowTask) obj;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		return true;
	}
}