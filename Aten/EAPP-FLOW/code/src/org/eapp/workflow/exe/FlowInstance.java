package org.eapp.workflow.exe;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.def.Event;
import org.eapp.workflow.def.FlowDefine;
import org.eapp.workflow.def.Node;
import org.eapp.workflow.def.Transition;


/**
 * 流程实例对象
 * FlowInstance entity.
 * 
 * @author 林良益
 * 2008-08-29
 * @version 1.0
 */
public class FlowInstance implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9047917737692883029L;
	// Fields
	
	//流程实例ID
	String id;
	//流程版本
	protected long version;
	//流程标识——用来识别流程的血统，同流程版本相关联，统一flowkey的流程，才谈的上version
	protected String flowKey;
	//关联的流程定义
	protected FlowDefine flowDefine;	
	//关联的令牌
	protected FlowToken rootToken;
	//关联的父流程令牌
	protected FlowToken superFlowToken;
	//流程名称
	protected String flowName;
	//流程实例创建时间
	protected Timestamp createTime;
	//流程实例结束时间
	protected Timestamp endTime;
	//流程实例上下文变量,使用变量字段名做key的映射（这里的变量主要来自业务系统)
	protected Map<String , ContextVariable> contextVariables;
	//流程实例上下文绑定的任务实例
	protected Set<TaskInstance> taskInstances;
	//任务实例管理类	
	protected TaskManagement tm;
	//流程实例挂起
	protected boolean isSuspended = false;
	
	//子流程实例集合
	protected List<FlowInstance> cascadeFlowInstances = null;	

	// Constructors
	/** default constructor */
	public FlowInstance(){
		//创建任务管理器
		if(tm == null){
			tm = new TaskManagement(this);
		}				
	}
	
	public FlowInstance(FlowDefine flowDefine){
		this(flowDefine , null);
	}
	
	public FlowInstance(FlowDefine flowDefine, Map<String, ContextVariable> contextVariables) {
		this();
		if(flowDefine == null){
			throw new RuntimeException("流程定义参数为空，无法生产流程实例");
		}
		this.flowDefine = flowDefine;
		//复制流程定义属性
		this.flowName = flowDefine.getName();
		this.version = flowDefine.getVersion();
		this.flowKey = flowDefine.getFlowKey();
		//标识流程实例创建
		this.createTime = new Timestamp(System.currentTimeMillis());
		//创建流程实例根令牌
		this.rootToken = new FlowToken(this);
		
		//上下文变量
		this.setContextVariables(contextVariables);
		//保存流程实例，并赋予ID
		WfmConfiguration.getInstance().getWfmContext().save(this);
		
		//进入并启动流程的初始节点
		Node initialNode = rootToken.getNode();
		//触发流程开始事件
		if (initialNode!=null) {
			Execution execution = new Execution(rootToken);
			flowDefine.fireEvent(Event.EVENTTYPE_FLOW_START , execution);
			// 执行起始节点
			initialNode.execute(execution);
		}		
	}


	/**
	 * 
	 * @return
	 */
	public TaskManagement getTaskManagement() {
		return tm;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FlowToken getRootToken() {
		return this.rootToken;
	}

	public void setRootToken(FlowToken token) {
		this.rootToken = token;
	}
	
	public FlowToken getSuperFlowToken() {
		return this.superFlowToken;
	}

	public void setSuperFlowToken(FlowToken token) {
		this.superFlowToken = token;
	}	
	

	public FlowDefine getFlowDefine() {
		return this.flowDefine;
	}

	public void setFlowDefine(FlowDefine flowDefine) {
		this.flowDefine = flowDefine;
	}

	public String getFlowName() {
		return this.flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * 如果endTime标志被赋值过，则说明流程实例已经结束
	 * @return
	 */
	public boolean hasEnded() {
		return ( endTime != null );
	}	
	
	/*
	 * ***********************
	 * 流程实例上下文任务实例访问
	 * ***********************
	 */
	/**
	 * 获取流程实例绑定的任务实例
	 */
	public Set<TaskInstance> getTaskInstances() {
		return this.taskInstances;
	}
	/**
	 * 绑定新的任务实例
	 * @param taskInstance
	 * @return
	 */
	public TaskInstance addTaskInstance(TaskInstance taskInstance){
		if(taskInstance == null){
			throw new IllegalArgumentException("不能向流程实例添加空(NULL)的任务实例");
		}
		if(taskInstances == null){
			taskInstances = new HashSet<TaskInstance>();
		}
		taskInstances.add(taskInstance);
		taskInstance.setFlowInstance(this);
		return taskInstance;
	}
	
	/**
	 * 移除指定的任务实例，与流程实例解绑定
	 * @param taskInstance
	 * @return
	 */
	public TaskInstance removeTaskInstance(TaskInstance taskInstance){
		TaskInstance removedTaskInstance = null;
		if(taskInstance == null){
			throw new IllegalArgumentException("不能移除空(NULL)的任务实例");
		}
		if(taskInstances != null){
			if(taskInstances.remove(taskInstance)){
				removedTaskInstance = taskInstance;
				removedTaskInstance.setFlowInstance(null);
			}
		}
		return removedTaskInstance;
	}
	/*
	 * ***********************
	 * 流程实例上下文变量访问
	 * ***********************
	 */
	
	/**
	 * 获取实例上下文变量映射表
	 */
	public Map<String , ContextVariable> getContextVariables() {
		return this.contextVariables;
	}
	
	/**
	 * 根据变量的字段名获取变量对象
	 * @param variableName
	 * @return
	 */
	public ContextVariable getVariable(String variableName){
		ContextVariable cv = null;
		if(contextVariables != null){
			cv = contextVariables.get(variableName);
		}
		return cv;
	}

	/**
	 * 向流程实例添加新变量
	 * @param contextVariable
	 * @return
	 */
	public ContextVariable addContextVariable(ContextVariable contextVariable){
		if(contextVariable == null){
			throw new IllegalArgumentException("不能向流程实例添加空(NULL)的上下文变量");
		}
		if(contextVariable.getName() == null){
			throw new IllegalArgumentException("不能向流程实例添加字段名为空(NULL)的上下文变量");
		}
		if(contextVariables == null){
			contextVariables = new HashMap<String , ContextVariable>();
		}
		contextVariables.put(contextVariable.getName(), contextVariable);
		contextVariable.setFlowInstance(this);
		return contextVariable;
	}
	
	/**
	 * 从流程实例中移除指定的上下文变量
	 * @param contextVariable
	 * @return
	 */
	public ContextVariable removeContextVariable(ContextVariable contextVariable){
		ContextVariable removedContextVariable = null;
		if(contextVariable == null){
			throw new IllegalArgumentException("不能移除空(NULL)的上下文变量");
		}
		if(contextVariable.getName() == null){
			throw new IllegalArgumentException("不能移除字段名为空(NULL)的上下文变量");
		}
		if(contextVariables != null){
			removedContextVariable = contextVariables.remove(contextVariable.getName());
			if(removedContextVariable != null){
				removedContextVariable.setFlowInstance(null);
			}
		}		
		return removedContextVariable;
	}
	
	/**
	 * 触发一个新的流程实例
	 */
	public void signal() {
		if ( hasEnded() ) {
			throw new IllegalStateException("当前流程实例已经结束,不能发起一个已结束的流程");
		}
		rootToken.signal();
	}

	/**
	 * 根据指定的路径名，触发一个新的流程实例
	 * @param transitionName
	 */
	public void signal(String transitionName) {
		if ( hasEnded() ) {
			throw new IllegalStateException("当前流程实例已经结束,不能发起一个已结束的流程");
		}
		rootToken.signal(transitionName);
	}

	/**
	 * 根据指定的路径，触发一个新的流程实例
	 * @param transition
	 */
	public void signal( Transition transition ) {
		if ( hasEnded() ) {
		      throw new IllegalStateException("当前流程实例已经结束,不能发起一个已结束的流程");
		}
		rootToken.signal(transition);
	}
		  
	/**
	 * 结束当前流程实例
	 */
	public void end() {
		//结束跟令牌，如果令牌已经结束，则立刻从方法中返回
		rootToken.end();
	    
		if (endTime == null) {
			// 标志流程实例的结束时间
			endTime = new Timestamp(System.currentTimeMillis());
	      
			// 触发流程实例结束事件
			Execution execution = new Execution(rootToken);
			flowDefine.fireEvent(Event.EVENTTYPE_FLOW_END , execution);
	      
			//如果当前流程实例是作为其它流程的子流程，则父流程要发出信号
			if (superFlowToken!=null) {
				// 将父流程实例加到当前流程自己的级联流程实例中（搞不明白jbpm为什么要这一步）
				addCascadeFlowInstance(superFlowToken.getFlowInstance());
				//结束子流程后，恢复上级流程的流传s
				Execution superExecution = new Execution(superFlowToken);
				superExecution.setSubFlowInstance(this);
				superFlowToken.signal(superExecution);
			}
		}
	}
	

	/**
	 * 挂起当前流程
	 */
	public void suspend() {
	    isSuspended = true;
	    rootToken.suspend();
	}

	/**
	 * 恢复当前挂起的流程
	 */
	public void resume() {
		isSuspended = false;
		rootToken.resume();
	}	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FlowInstance))
			return false;
		final FlowInstance other = (FlowInstance) obj;
		if (id == null || other.getId() == null) {
			return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}


	/**
	 * @return the version
	 */
	public long getVersion() {
		return version;
	}


	/**
	 * @param version the version to set
	 */
	public void setVersion(long version) {
		this.version = version;
	}


	/**
	 * @return the flowKey
	 */
	public String getFlowKey() {
		return flowKey;
	}


	/**
	 * @param flowKey the flowKey to set
	 */
	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}

	/**
	 * @param taskInstances the taskInstances to set
	 */
	public void setTaskInstances(Set<TaskInstance> taskInstances) {
		this.taskInstances = taskInstances;
	}


	/**
	 * @param contextVariables the contextVariables to set
	 */
	public void setContextVariables(Map<String, ContextVariable> contextVariables) {
		this.contextVariables = contextVariables;
	}

	/**
	 * 关联子流程实例
	 * @param cascadeFlowInstance
	 */
	void addCascadeFlowInstance(FlowInstance cascadeFlowInstance) {
		if (cascadeFlowInstances==null) {
			cascadeFlowInstances = new ArrayList<FlowInstance>();
		}
		cascadeFlowInstances.add(cascadeFlowInstance);
	}
	
	/**
	 * 移除所有子流程实例关联
	 * @return
	 */
	public List<FlowInstance> removeCascadeFlowInstances() {
		List<FlowInstance> removed = cascadeFlowInstances;
		cascadeFlowInstances = null;
		return removed;
	}

	public boolean getIsSuspended() {
		return isSuspended;
	}

	public void setIsSuspended(boolean isSuspended) {
		this.isSuspended = isSuspended;
	}	
	

	/**
	 * 获取流程实例绑定的当前任务实例
	 */
	public List<TaskInstance> getCurrentTaskInstances() {
		List<TaskInstance> list = new ArrayList<TaskInstance>();
		if(taskInstances!=null){
			for(TaskInstance ti : taskInstances){
				if(TaskInstance.PROCESS_STATE_CREATE.equals(ti.getProcessState()) || TaskInstance.PROCESS_STATE_START.equals(ti.getProcessState())){
					list.add(ti);
				}
			}
		}
		return list;
	}

}