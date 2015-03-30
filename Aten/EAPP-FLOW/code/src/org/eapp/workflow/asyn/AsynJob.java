/**
 * 
 */
package org.eapp.workflow.asyn;

import java.io.Serializable;
import java.util.Date;

import org.eapp.workflow.WfmContext;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.FlowToken;


/**
 * 异步任务对象
 * @author 林良益
 * 2008-10-26
 * @version 2.0
 */
public abstract class AsynJob implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3495930492498663117L;
	//物理ID
	String id;
	//Hibernate数据库同步版本
	int version;
	//任务到期时间（当前时间>dueDate时，任务从数据库中取出执行）
	Date dueDate;
	//关联的流程实例
	FlowInstance flowInstance;
	//关联的令牌
	FlowToken flowToken;
	//任务挂起标志
	boolean isSuspended;
	//锁定当前任务的线程名称
	String lockOwner;
	//执行线程锁定当前任务的时间
	Date lockTime;
	//任务执行异常
	String exception;
	//重试次数，默认为1
	int retries = 1;	
	
	public AsynJob() {
		
	}
	
	public AsynJob(FlowToken flowToken){
		this.flowToken = flowToken;
		this.flowInstance = flowToken.getFlowInstance();
	}
	
	public abstract boolean execute(WfmContext context) throws Exception ;
	
	/**
	 * 
	 */
	public String toString() {
		return "AsynJob["+id+"]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public FlowInstance getFlowInstance() {
		return flowInstance;
	}

	public void setFlowInstance(FlowInstance flowInstance) {
		this.flowInstance = flowInstance;
	}

	public FlowToken getFlowToken() {
		return flowToken;
	}

	public void setFlowToken(FlowToken flowToken) {
		this.flowToken = flowToken;
	}

	public boolean getIsSuspended() {
		return isSuspended;
	}

	public void setIsSuspended(boolean isSuspended) {
		this.isSuspended = isSuspended;
	}

	public String getLockOwner() {
		return lockOwner;
	}

	public void setLockOwner(String lockOwner) {
		this.lockOwner = lockOwner;
	}

	public Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
