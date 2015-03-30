package org.eapp.oa.flow.hbean;

// default package

/**
 * TaskAssigns entity.
 * Description:任务授权
 * @author MyEclipse Persistence Tools
 */

public class TaskAssign implements java.io.Serializable {

	// Fields
	public static final String TYPE_USER = "0";//用户授权
	public static final String TYPE_ROLE = "1";//角色授权
	/**
	 * 
	 */
	private static final long serialVersionUID = 5465465931152856101L;
	//ID_,VARCHAR2(36),不能为空                             --主键ID
	private String id;
	//TaskID_,VARCHAR2(36)                             --任务ID
	private Task task;
	//Type_,CHAR                                       --授权类型 	
	private String type;
	//AssignKey_,VARCHAR2(128)                         --授权键值
	private String assignKey;

	// Constructors

	/** default constructor */
	public TaskAssign() {
	}

	/** full constructor */
	public TaskAssign(Task oaTasks, String type, String assignKey) {
		this.task = oaTasks;
		this.type = type;
		this.assignKey = assignKey;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Task getTask() {
		return this.task;
	}

	public void setTask(Task oaTasks) {
		this.task = oaTasks;
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
		final TaskAssign other = (TaskAssign) obj;
		if (id == null) {
//			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}