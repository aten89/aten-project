package org.eapp.workflow.exe;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.def.Delegation;
import org.eapp.workflow.def.Event;
import org.eapp.workflow.def.IAssignable;
import org.eapp.workflow.def.Node;
import org.eapp.workflow.def.TaskDefine;
import org.eapp.workflow.def.Transition;
import org.eapp.workflow.def.node.TaskNode;
import org.eapp.workflow.expression.ExpressionEvaluator;
import org.eapp.workflow.handler.IViewHandler;


/**
 * 任务实例对象
 * TaskInstance entity.
 * 
 * @author 林良益
 * 2008-08-29
 * @version 1.0
 */
public class TaskInstance implements IAssignable , java.io.Serializable{

	//任务实例状态常量：
	
	//任务创建
	public static final String PROCESS_STATE_CREATE = "ps_create";
	//任务开始受理
	public static final String PROCESS_STATE_START = "ps_start";
	//任务结束
	public static final String PROCESS_STATE_END = "ps_end";
	//任务放弃
	public static final String PROCESS_STATE_CANCEL = "ps_cancel";
	
	//任务
	/**
	 * 
	 */
	private static final long serialVersionUID = 6203112728595321155L;
	// Fields

	String id;
	//版本戳（Hibernate 乐观锁）
	int version = 0;
	//绑定的流程实例
	protected FlowInstance flowInstance;
	//绑定的令牌
	protected FlowToken flowToken;
	//关联的任务定义
	protected TaskDefine taskDefine;
	//任务名称——来自任务定义
	protected String taskName;
	//任务实例生成时间
	protected Timestamp createTime;
	//任务实例开始（认领）时间
	protected Timestamp startTime;
	//任务实例完成时间
	protected Timestamp endTime;
	//任务实例完成期限
	protected Timestamp dueTime;
	//任务放弃
	protected boolean isCancelled = false;
	//任务挂起
	protected boolean isSuspended = false;
	//开放的（未关闭的）
	protected boolean isOpen = true;
	//任务是触发式的
	protected boolean isSignalling = true;
	//任务是阻断式的
	protected boolean isBlocking = false;
	//执行顺序
	protected Integer executeOrder;
	//任务优先级
	protected Integer priority;	
	//任务描述
	protected String description;
	//任务反馈（意见）
	protected String comment;
	//任务处理状态（）
	protected String processState;	
	//处理者ID
	protected String actorId;	
	//任务待处理角色
	protected Set<PooledRole> pooledRoles;
	//任务待处理人员
	protected Set<PooledActor> pooledActors;

	// Constructors

	/** default constructor */
	public TaskInstance() {
	}

	public TaskInstance(String taskName, String actorId) {
		this.taskName = taskName;
		this.actorId = actorId;
	}
	
	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public FlowToken getFlowToken() {
		return this.flowToken;
	}

	public void setFlowToken(FlowToken flowToken) {
		this.flowToken = flowToken;
	}


	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Timestamp getDueTime() {
		return dueTime;
	}

	public void setDueTime(Timestamp dueTime) {
		this.dueTime = dueTime;
	}

	public boolean getIsSignalling() {
		return isSignalling;
	}

	public void setIsSignalling(boolean isSignalling) {
		this.isSignalling = isSignalling;
	}

	public boolean getIsBlocking() {
		return isBlocking;
	}

	public void setIsBlocking(boolean isBlocking) {
		this.isBlocking = isBlocking;
	}

	public Integer getExecuteOrder() {
		return executeOrder;
	}

	public void setExecuteOrder(Integer executeOrder) {
		this.executeOrder = executeOrder;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public TaskDefine getTaskDefine() {
		return taskDefine;
	}
	/**
	 * 设置任务定义，并拷贝任务定义属性
	 * @param taskDefine
	 */
	public void setTaskDefine(TaskDefine taskDefine) {
		this.taskDefine = taskDefine;
		this.taskName = taskDefine.getName();
		this.description = taskDefine.getDescription();
		this.isBlocking = taskDefine.getIsBlocking();		
		this.priority = taskDefine.getPriority();
		
		if (taskDefine.getTaskNode()!=null) {
			//只要TaskDefine所在的节点的signalType被定义为SIGNAL_FIRST或者SIGNAL_LAST
			//则这个任务的实例就是isSignalling=true的；
			int signalType = taskDefine.getTaskNode().getSignalType();
			this.isSignalling = (signalType==TaskNode.SIGNAL_FIRST ) 
	                            || (signalType==TaskNode.SIGNAL_LAST )
	                            || taskDefine.getIsSignalling(); 
	                            //|| (signal==TaskNode.SIGNAL_FIRST_WAIT ) 
	                            //|| (signal==TaskNode.SIGNAL_LAST_WAIT ) 

	    }else{
	    	this.isSignalling = taskDefine.getIsSignalling(); 
	    }
	    // ***根据设定的办理时间计算实际催办时间，考虑到节假日
//	    if (task.getDueDate()!=null) {
//	      BusinessCalendar businessCalendar = new BusinessCalendar();
//	      this.dueDate = businessCalendar.add(Clock.getCurrentTime(), new Duration(task.getDueDate()));
//	    }
	}

	public FlowInstance getFlowInstance() {
		return flowInstance;
	}

	public void setFlowInstance(FlowInstance flowInstance) {
		this.flowInstance = flowInstance;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public String getProcessState() {
		return processState;
	}

	public void setProcessState(String processState) {
		this.processState = processState;
	}

	public String getActorId() {
		return actorId;
	}
	
	/**
	 * 该方法是bean的set方法
	 * 也是IAssignable的实现方法
	 * 同时触发任务分派事件
	 */
	public void setActorId(String actorId) {
		String oldActorId = this.actorId;
		this.actorId = actorId;
		if(oldActorId == null || !oldActorId.equals(actorId)){
			//(仅当第一次分派任务，或者任务分派变更时，触发分派事件)
			//触发EVENTTYPE_TASK_ASSIGN事件
			if((taskDefine!=null) && (flowToken!=null)) {
				//触发事件前先配置上下文
				Execution execution = new Execution(flowToken);
				execution.setTaskDefine(taskDefine);
				execution.setTaskInstance(this);
				//触发事件
				taskDefine.fireEvent(Event.EVENTTYPE_TASK_ASSIGN, execution);
		    }
		}
	}

	public Set<PooledRole> getPooledRoles() {
		return pooledRoles;
	}
	/**
	 * 该方法是Bean的属性设置方法
	 * @param pooledRoles
	 */
	public void setPooledRoles(Set<PooledRole> pooledRoles) {
		if(pooledRoles != null){
			this.pooledRoles = new HashSet<PooledRole>(pooledRoles);
			Iterator<PooledRole> iter = pooledRoles.iterator();
			while(iter.hasNext()){
				PooledRole pooledRole = iter.next();
				pooledRole.setTaskInstance(this);
			}
		} else {
			this.pooledRoles = null;
		}
	}
	
	/**
	 * 该方法是IAssignable接口的实现
	 */
	public void setPooledRoles(String[] pooledRoleIds) {
		this.pooledRoles = PooledRole.createPool(pooledRoleIds, this);
	}
	
	/**
	 * 重新授权角色
	 * @param pooledRoleIds
	 */
	public void reSetPooledRoles(String[] pooledRoleIds) {
		//删除原有角色
		if (pooledRoles != null) {
			for (PooledRole pr : pooledRoles) {
				WfmConfiguration.getInstance().getWfmContext().delete(pr);
			}
		}
		//得新设置角色
		setPooledRoles(pooledRoleIds);
		//重置为创建状态
		processState = TaskInstance.PROCESS_STATE_CREATE;
		startTime = null;
		
		//触发EVENTTYPE_TASK_NOTIFY事件
		if((taskDefine!=null) && (flowToken!=null)) {
			//触发事件前先配置上下文
			Execution execution = new Execution(flowToken);
			execution.setTaskDefine(taskDefine);
			execution.setTaskInstance(this);
			//触发事件(仅当第一次分派任务，或者任务分派变更时，触发分派事件)
			taskDefine.fireEvent(Event.EVENTTYPE_TASK_NOTIFY, execution);
	    }
	}

	public Set<PooledActor> getPooledActors() {
		return pooledActors;
	}
	/**
	 * 该方法是Bean的属性设置方法
	 * @param pooledActors
	 */
	public void setPooledActors(Set<PooledActor> pooledActors) {
		if (pooledActors!= null) {
			this.pooledActors = new HashSet<PooledActor>(pooledActors);
			Iterator<PooledActor> iter = pooledActors.iterator();
			while (iter.hasNext()) {
				PooledActor pooledActor = iter.next();
				pooledActor.setTaskInstance(this);
			}
		} else {
			this.pooledActors = null;
		}
	}

	/**
	 * 该方法是IAssignable接口的实现
	 */
	public void setPooledActors(String[] pooledActorIds) {
		this.pooledActors = PooledActor.createPool(pooledActorIds, this);
	}

	/**
	 * 重新授权用户
	 * @param pooledRoleIds
	 */
	public void reSetPooledActors(String[] pooledActorIds) {
		//删除原有用户
		if (pooledActors != null) {
			for (PooledActor pr : pooledActors) {
				WfmConfiguration.getInstance().getWfmContext().delete(pr);
			}
		}
		//得新设置角色
		setPooledActors(pooledActorIds);
		//重置为创建状态
		processState = TaskInstance.PROCESS_STATE_CREATE;
		startTime = null;
		
		//触发EVENTTYPE_TASK_NOTIFY事件
		if((taskDefine!=null) && (flowToken!=null)) {
			//触发事件前先配置上下文
			Execution execution = new Execution(flowToken);
			execution.setTaskDefine(taskDefine);
			execution.setTaskInstance(this);
			//触发事件(仅当第一次分派任务，或者任务分派变更时，触发分派事件)
			taskDefine.fireEvent(Event.EVENTTYPE_TASK_NOTIFY, execution);
	    }
	}
	
	
	/*
	 * 任务实例业务方法
	 */
	
	/**
	 * 创建任务实例
	 * 这里的创建是正对流程业务意义上的创建，不是new
	 */
	public void create(Execution execution) {
		if (createTime!=null) {
			throw new IllegalStateException("task instance '"+id+"' was already created");
		}
		//赋予任务实例生成时间，标示着任务实例被创建
		createTime = new Timestamp(System.currentTimeMillis());
		processState = PROCESS_STATE_CREATE;     
		//任务实例跟任务定义关联
		if ((taskDefine!=null) && (execution!=null)) {
			//关联执行上下文
			execution.setTaskInstance(this);
			execution.setTaskDefine(taskDefine);
			//触发任务实例生成事件
			taskDefine.fireEvent(Event.EVENTTYPE_TASK_CREATE, execution);
		}
	}	
	
	/**
	 * 对任务实例分配待执行人
	 * @param execution
	 */
	public void assign(Execution execution) {		
		TaskManagement tm = flowInstance.getTaskManagement();
		Delegation taskAssignment = taskDefine.getTaskAssignment();
		if(taskAssignment != null){
			String assignedHandler = taskAssignment.getClassName();
			String assignedExpression = taskAssignment.getExpression();
			tm.performAssignment(assignedHandler, 
					assignedExpression,
					this, 
					execution);
			//触发任务实例通知事件
			taskDefine.fireEvent(Event.EVENTTYPE_TASK_NOTIFY, execution);
		}
	}
	
	/**
	 * 认领任务实例，开始办理
	 * @param actorId
	 */
	public void start(String actorId){
		if (startTime!=null) {
			throw new IllegalStateException("任务'"+id+"'已经被认领，办理中");
		}
//		setActorId(actorId);
		//这里不要触发TASK_ASSIGN事件，所以不要调用setActorId
		this.actorId = actorId;
		start();
	}

	void start(){
		if (startTime!=null) {
			throw new IllegalStateException("任务'"+id+"'已经被认领，办理中");
		}
		
		startTime = new Timestamp(System.currentTimeMillis());		
		processState = PROCESS_STATE_START;
		
		if((taskDefine!=null) && (flowToken!=null)) {
			//设置上下文，为事件触发做准备
			Execution execution = new Execution(flowToken);
			execution.setTaskDefine(taskDefine);
			execution.setTaskInstance(this);
			//触发EVENTTYPE_TASK_START事件
			taskDefine.fireEvent(Event.EVENTTYPE_TASK_START, execution);
		}
	}
	
	/**
	 * 在没有Roles和Actors的情况下，任务不能撤销
	 * 放弃已经认领的任务实例
	 * @param actorId
	 */
	public void giveup(String actorId){
		if(startTime == null){
			throw new IllegalStateException("任务'"+id+"'尚未被认领");
		}		
		
		if(this.actorId != null && !this.actorId.equals(actorId)){
			throw new IllegalStateException("任务'"+id+"'已经被'" + this.actorId + "'认领,不能由'" + actorId + "'放弃");
		}
		//在没有Roles和Actors的情况下，任务不能撤销
		if((this.pooledActors == null || this.pooledActors.isEmpty())
				&& (this.pooledRoles == null || this.pooledRoles.isEmpty())){
			throw new IllegalStateException("任务'"+id+"'只有'" + this.actorId + "'一人认领,不能放弃");
		}

		//清空当前的认领人和认领时间
		if(this.actorId != null && this.actorId.equals(actorId)){
			this.startTime = null;
			this.actorId = null;
			processState = PROCESS_STATE_CREATE;
			//触发放弃认领的事件
			if((taskDefine!=null) && (flowToken!=null)) {
				//设置上下文，为事件触发做准备
				Execution execution = new Execution(flowToken);
				execution.setTaskDefine(taskDefine);
				execution.setTaskInstance(this);
				//触发EVENTTYPE_TASK_GIVEUP事件
				taskDefine.fireEvent(Event.EVENTTYPE_TASK_GIVEUP , execution);
			}		
		}
	}
	
	/**
	 * 结束当前任务，并使用默认的Transition离开当前节点
	 */
	public void end() {
		end((Transition)null);
	}
	
	/**
	 * 结束当前任务，并使用指定名称的Transition离开当前节点
	 * @param transitionName
	 */
	public void end(String transitionName) {
		Transition leavingTransition = null;
		if (taskDefine!=null) {
			Node node = taskDefine.getTaskNode();
			if (node!=null) {
				leavingTransition = node.getLeavingTransition(transitionName);
			}
		}
		if (leavingTransition==null) {
			throw new RuntimeException("任务没有指定的出口路径：'"+transitionName+"'");
		}
		end(leavingTransition);
	}
	
	/**
	 *  结束当前任务，并使用指定的Transition离开当前节点
	 * @param transition
	 */
	public void end(Transition transition) {
		if (this.endTime!=null){
			throw new IllegalStateException("任务实例'"+id+"' 已经结束");
		}
		
		if (this.isSuspended) {
			throw new RuntimeException("任务实例'"+id+"' 已经 挂起");
		}
		    
		//标记任务实例结束时间
		this.endTime = new Timestamp(System.currentTimeMillis());
		processState = PROCESS_STATE_END;
		this.isOpen = false;

		// 触发任务实例结束事件
		if ( (taskDefine!=null)&& (flowToken!=null)) {
			//初始化事件上下文
			Execution execution = new Execution(flowToken);
			execution.setTaskDefine(taskDefine);
			execution.setTaskInstance(this);
			//触发事件
			taskDefine.fireEvent(Event.EVENTTYPE_TASK_END, execution);
		}
		    
		TaskManagement tm = getFlowInstance().getTaskManagement();
		//验证任务是否触发流程节点向下流转
		if (isSignalling && !tm.hasBlockingTaskInstances(getFlowToken())) {//当前任务是一个“信号任务”			
			this.isSignalling = false;//标为false，表示信号已经发出	      
			if ( (taskDefine!=null)
		                && (flowToken!=null)
		                && (taskDefine.getTaskNode()!=null)
		                && (taskDefine.getTaskNode().completionTriggersSignal(this))) {
				//如果没有指定的流转路径，则按默认路径；否则按指定的路径流转
				if (transition==null) {
					flowToken.signal();
				} else {
					flowToken.signal(transition);
				}
			}
		}
	}	
	
	/**
	 * 取得任务视图
	 * @return
	 */
	public String getView() {
		Delegation view = this.getTaskDefine().getTaskView();
		if (view == null) {
			return null;
		}
		Execution execution = new Execution(this.getFlowToken());
		execution.setTaskInstance(this);
		if (view.getClassName() != null) {
 			try {
				Class<?> c = Class.forName(view.getClassName());
				IViewHandler vh = (IViewHandler)c.newInstance();
				return vh.calcViewURL(execution);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (view.getExpression() != null) {
			return ExpressionEvaluator.evaluate(view.getExpression(), execution, String.class);
		}
		return null;
	}
	
	
	/**
	 * 根据任务的endTime是否为空，判断任务实例是否已经结束
	 * @return
	 */  
	public boolean hasEnded(){
		return this.endTime != null;
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
	 * @
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TaskInstance))
			return false;
		final TaskInstance other = (TaskInstance) obj;
		if (id == null || other.getId() == null) {
			return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public boolean getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public boolean getIsSuspended() {
		return isSuspended;
	}

	public void setIsSuspended(boolean isSuspended) {
		this.isSuspended = isSuspended;
	}

	public boolean getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
	/**
	* 挂起当前任务实例
	*/
	public void suspend() {
		if (!isOpen) {
			throw new RuntimeException("任务处于非开启状态，无法挂起: "+toString());
		}
		isSuspended = true;
	}

	/**
	 * 挂起恢复
	 */
	public void resume() {
		if (!isOpen) {
			throw new RuntimeException("任务处于非开启状态，无法执行挂起恢复: "+toString());
		}
		isSuspended = false;
	}
	
	public String toString() {
		return "TaskInstance"+(taskName!=null ? "["+taskName+"]" : Integer.toHexString(System.identityHashCode(this)));
	}
	  
}