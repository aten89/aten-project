package org.eapp.flow.db;

// default package

/**
 * TaskAssigns entity.
 * Description:任务授权
 * @author MyEclipse Persistence Tools
 */

public class FlowTaskAssign implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5465465931152856101L;
	
	public static final String TYPE_USER = "0";//用户授权
	public static final String TYPE_ROLE = "1";//角色授权
	
	private String assignId;//主键ID
	private FlowTask flowTask;//授权类型
	private String type;//授权键值
	private String assignKey;
	
	public FlowTaskAssign() {
		
	}
	
	public FlowTaskAssign(String type, String assignKey) {
		this.type = type;
		this.assignKey = assignKey;
	}

	public String getAssignId() {
		return this.assignId;
	}

	public void setAssignId(String assignId) {
		this.assignId = assignId;
	}

	public FlowTask getFlowTask() {
		return this.flowTask;
	}

	public void setFlowTask(FlowTask flowTask) {
		this.flowTask = flowTask;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAssignKey() {
		return this.assignKey;
	}

	public void setAssignKey(String assignKey) {
		this.assignKey = assignKey;
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
		result = prime * result + ((assignId == null) ? 0 : assignId.hashCode());
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
		final FlowTaskAssign other = (FlowTaskAssign) obj;
		if (assignId == null) {
//			if (other.id != null)
				return false;
		} else if (!assignId.equals(other.assignId))
			return false;
		return true;
	}

}