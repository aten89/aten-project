package org.eapp.workflow.exe;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.db.AsynJobSession;
import org.eapp.workflow.def.FlowDefine;
import org.eapp.workflow.def.Node;
import org.eapp.workflow.def.Transition;


/**
 * 流程执行令牌对象
 * 
 * @author 林良益
 * 2008-06-27
 * @version 1.0
 */
public class FlowToken implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7971999693241633609L;
	
	// Fields
	String id;
	//令牌锁
	String locked = null;
	//版本锁
	int version = 0;
	//日志索引
    protected int nextLogIndex = 0; 
	//令牌名
	protected String name;
	//令牌所处的当前节点
	protected Node node;
	//令牌的流程实例
	protected FlowInstance flowInstance;
	//令牌的子流程实例
	protected FlowInstance subFlowInstance;	
	//令牌的父令牌
	protected FlowToken parent;
	//令牌的子令牌
	protected Map<String , FlowToken> children;
	//令牌的生成时间
	protected Timestamp start;
	//令牌的结束时间
	protected Timestamp end;
	//当Token为子令牌的时候，这个标识表示子令牌是否能激活父令牌
	protected boolean isAbleToReactivateParent = true;
	//挂起标识
	protected boolean isSuspended = false;
	//驳回标识
	protected boolean isRejected = false;
	

	// Constructors

	/** default constructor */
	public FlowToken() {
	}
	
	/**
	 * 构造一个根令牌（rootToken）
	 * @param flowInstance
	 */
	public FlowToken(FlowInstance flowInstance){
		this.flowInstance = flowInstance;
		this.node = flowInstance.getFlowDefine().getStartNode();
		this.start = new Timestamp(System.currentTimeMillis());
	}
	/**
	 * 根据根令牌构造子令牌
	 * @param parent
	 * @param name
	 */	
	public FlowToken(FlowToken parent, String name) {
		this.start = new Timestamp(System.currentTimeMillis());
		this.flowInstance = parent.getFlowInstance();
		this.name = name;
		this.node = parent.getNode();
		this.parent = parent;
		parent.addChild(this);		    
		//保存子令牌，并赋予ID
		WfmConfiguration.getInstance().getWfmContext().save(this);
	}	
	
	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}
	
	public FlowInstance getFlowInstance() {
		return flowInstance;
	}

	public void setFlowInstance(FlowInstance flowInstance) {
		this.flowInstance = flowInstance;
	}
	
	public FlowInstance getSubFlowInstance() {
		return subFlowInstance;
	}

	public void setSubFlowInstance(FlowInstance subFlowInstance) {
		this.subFlowInstance = subFlowInstance;
	}
	

	public FlowToken getParent() {
		return parent;
	}

	public void setParent(FlowToken parent) {
		this.parent = parent;
	}

	public Map<String , FlowToken> getChildren() {
		return children;
	}	
	
	/* 
	 * ******************
	 * 流程运行相关方法
	 * ******************
	 */
	/**
	 * 根据默认出口路径，触发流转
	 */
	public void signal() {
		if (node==null) {
			throw new RuntimeException("令牌没有绑定节点，无法发起流转");
		}
		if (node.getDefaultLeavingTransition() == null) {
			throw new RuntimeException("令牌所在的节点没有默认的流转路径，无法发起流转");
		}
		signal(node.getDefaultLeavingTransition(), new Execution(this));
	}
	
	/**
	 * 指定出口路径名称，触发流转
	 * @param transitionName
	 */
	public void signal(String transitionName) {
		if (node==null) {
			throw new RuntimeException("令牌没有绑定节点，无法发起流转");
		}
		Transition leavingTransition = node.getLeavingTransition(transitionName);
		if (leavingTransition==null) {
			throw new RuntimeException("指定的流转路径不存在，无法发起流转");
		}		
    	signal(leavingTransition, new Execution(this));
	}
	
	/**
	 * 指定出口路径对象，触发流转
	 * @param transition
	 */	
	public void signal(Transition transition) {
		signal(transition, new Execution(this));
	}
	
	/**
	 * 
	 * @param execution
	 */
	void signal(Execution execution){
		signal(node.getDefaultLeavingTransition(), execution);
	}
	
	/**
	 * 
	 * @param transition
	 * @param execution
	 */
	void signal(Transition transition, Execution execution) {
		if (transition == null) {
			throw new RuntimeException("流转路径为空(transition == null)，无法发起流转");
		}
		if (execution == null) {
			throw new RuntimeException("执行上下文为空(execution == null)，无法发起流转");
		}
//		令牌被挂起
//		if (isSuspended) {
//			throw new RuntimeException("can't signal token '"+name+"' ("+id+"): it is suspended");
//		}
		//令牌被锁定
		if (hasLocked()) {
			throw new RuntimeException("令牌被锁定，锁定者："+locked);
		}
		//按指定的出口路径，离开当前令牌所在的节点，并计算下一个节点的内容
		node.leaveNode(execution , transition);
	}	
	
	/**
	 * 使用指定的ID锁定当前令牌
	 * @param lockOwnerId
	 */
	public void lock(String lockOwnerId) {
		if (lockOwnerId == null) {
			throw new RuntimeException("无法使用空的ID锁定当前令牌");
		}
		if ((locked!=null) && (!locked.equals(lockOwnerId))) {
			throw new RuntimeException("无法使用ID=" + lockOwnerId + "锁定当前令牌；令牌已经被ID=" + locked + "锁定");
		}
		locked = lockOwnerId;
	}
	
	/**
	 * 使用指定的ID解锁当前令牌
	 * @param lockOwnerId
	 */
	public void unlock(String lockOwnerId) {
		if (locked == null) {

		} else if (!locked.equals(lockOwnerId)) {
			throw new RuntimeException("无法使用ID="+lockOwnerId+"解锁当前令牌；令牌已经被ID=" + locked + "锁定");
		}
		locked = null;
	}
	
	/**
	 * 判断当前令牌是否被锁
	 * @return
	 */
	public boolean hasLocked() {
		return locked!=null;
	}		

	public Timestamp getStart() {
		return start;
	}

	public void setStart(Timestamp start) {
		this.start = start;
	}

	public Timestamp getEnd() {
		return end;
	}

	public void setEnd(Timestamp end) {
		this.end = end;
	}	
	
	public void end(){
		end(true);
	}
	
	/**
	 * 结束当前Token
	 * @param verifyParentTermination
	 */
	public void end(boolean verifyParentTermination) {
		// 判断Token是否已经结束
		if (end == null) {
			//设置Token的结束标志时间
			this.end = new Timestamp(System.currentTimeMillis());
			 //如果Token是子令牌，则设置为不再激活父令牌
		    this.isAbleToReactivateParent = false; 
		    
			//结束当前令牌的子令牌——在有分支流程的情况下
			if (children != null) {
				Iterator<FlowToken> iter = children.values().iterator();
				while (iter.hasNext()) {
					FlowToken child = iter.next();
					if (!child.hasEnded()) {
						child.end();
					}
				}
			}
			// 结束子流程  
			if (subFlowInstance!=null) {
				subFlowInstance.end();
			}

			//将当前Token分支上的所有任务实例的信号状态设置为false
			TaskManagement tm = (flowInstance!=null ? flowInstance.getTaskManagement() : null);
			if (tm!=null) {
				tm.removeSignalling(this);
			}

			//如果Token为根Token，则结束当前的主流程
			//如果为分支Token，并且是最后一个结束的分支Token，要结束主Token（Join节点的除外，Join的时候不会结束主Token）
			if (verifyParentTermination) {
				//结束流程实例
				if (isRoot()) {
					flowInstance.end();
				} else {		      
					if (!parent.hasActiveChildren()) {
						parent.end();
					}
				}
			}

		}
	}
	
	/**
	 * 判断当前的Token是否个根令牌
	 */
	public boolean isRoot() {
		return (parent == null);
	}
	
	/**
	 * 生成子流程实例
	 * @param subFlowDefine
	 * @return
	 */
	public FlowInstance createSubFlowInstance(FlowDefine subFlowDefine) {
		// 创建子流程实例
		subFlowInstance = new FlowInstance(subFlowDefine);
		//绑定Token和子流程实例
		setSubFlowInstance(subFlowInstance);
		subFlowInstance.setSuperFlowToken(this);
		//绑定子流程和主流程，在存储主流程是，手动级联存储子流程
		flowInstance.addCascadeFlowInstance(subFlowInstance);
		//保存流程实例，并赋予ID
		//jBPM在绑定子流程时，要求手动调用主流程的save方法，但这样做会重复存储主流程实例，这里增加了子流程生成时的自动存储，有待测试
		WfmConfiguration.getInstance().getWfmContext().save(this);
		return subFlowInstance;
	}
	
	/**
	 * 挂起流程上的一个令牌分支
	*/
	public void suspend() {
		isSuspended = true;
	    //挂起关联的异步任务
		suspendJobs();
		//挂起关联的任务实例
		suspendTaskInstances();
		//挂起关联的子令牌
		if (children!=null) {
			Iterator<FlowToken> iter = children.values().iterator();
			while (iter.hasNext()) {
				FlowToken child = (FlowToken) iter.next();
				child.suspend(); 
			}
		}
	}
	
	/**
	 * 挂起令牌关联的异步任务
	 */
	void suspendJobs() {
		WfmContext wfmContext = WfmConfiguration.getInstance().getWfmContext();
		AsynJobSession jobSession = wfmContext.getAsynJobSession();
		if (jobSession!=null) {
			jobSession.suspendJobs(this);
		}
	}
	
	/**
	 * 挂起令牌关联的任务实例
	 */
	void suspendTaskInstances() {
		TaskManagement taskMgmt = (flowInstance!=null ? flowInstance.getTaskManagement() : null);
		if (taskMgmt!=null) {
			taskMgmt.suspend(this);
		}
	}
	
	/**
	* 恢复挂起
	*/
	public void resume() {
		isSuspended = false;
		//恢复挂起的异步任务
		resumeJobs();
		//恢复挂起的任务实例
		resumeTaskInstances();	
		//恢复关联的子令牌
		if (children!=null) {
			Iterator<FlowToken> iter = children.values().iterator();
			while (iter.hasNext()) {
				FlowToken child = (FlowToken) iter.next();
				child.resume(); 
			}
		}
	}

	/**
	 * 恢复挂起的异步任务
	 */
	void resumeJobs() {
		WfmContext wfmContext = WfmConfiguration.getInstance().getWfmContext();
		AsynJobSession jobSession = wfmContext.getAsynJobSession();
		if (jobSession!=null) {
			jobSession.resumeJobs(this);
		}
	}

	/**
	 * 恢复挂起的任务实例
	 */
	void resumeTaskInstances() {
		TaskManagement taskMgmt = (flowInstance!=null ? flowInstance.getTaskManagement() : null);
		if (taskMgmt!=null) {
			taskMgmt.resume(this);
		}
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
		if (!(obj instanceof FlowToken))
			return false;
		final FlowToken other = (FlowToken) obj;
		if (id == null || other.getId() == null) {
			return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(Map<String, FlowToken> children) {
		this.children = children;
	}
	/**
	 * 绑定新的子令牌对象，并建立名称映射表
	 * @param flowToken
	 */
	void addChild(FlowToken flowToken) {
		if (children==null) {
			children = new HashMap<String , FlowToken>();
		}
		children.put(flowToken.getName(), flowToken);
	}
	/**
	 * 根据令牌名称，获取子令牌
	 * @param name
	 * @return
	 */
	public FlowToken getChild(String name) {
		FlowToken child = null;
		if (children != null) {
			child = children.get(name);
		}
		return child;
	}		
	/**
	 * ]判断是否包含指定名称的子令牌
	 * @param name
	 * @return
	 */
	public boolean hasChild(String name) {
		return (children != null ? children.containsKey(name) : false);
	}

	public boolean getIsAbleToReactivateParent() {
		return isAbleToReactivateParent;
	}

	public void setIsAbleToReactivateParent(boolean isAbleToReactivateParent) {
		this.isAbleToReactivateParent = isAbleToReactivateParent;
	}
	
	public boolean hasEnded() {
		return (end != null);
	}
	
	  /**
	   * 判断是否存在没有结束的子Token
	   */
	public boolean hasActiveChildren() {
		boolean foundActiveChildToken = false;
	    // try and find at least one child token that is
	    // still active (= not ended)
		if (children!=null) {
			Iterator<FlowToken> iter = children.values().iterator();
			while ((iter.hasNext()) && (!foundActiveChildToken)) {
				FlowToken child = (FlowToken) iter.next();
				if (!child.hasEnded()) {
					foundActiveChildToken = true;
				}
			}
	    }
	    return foundActiveChildToken;
	}
	
	
	public int nextLogIndex() {
		return nextLogIndex++;
	}

	public int getNextLogIndex() {
		return nextLogIndex;
	}

	public void setNextLogIndex(int nextLogIndex) {
		this.nextLogIndex = nextLogIndex;
	}

	public boolean getIsSuspended() {
		return isSuspended;
	}

	public void setIsSuspended(boolean isSuspended) {
		this.isSuspended = isSuspended;
	}

	public boolean getIsRejected() {
		return isRejected;
	}

	public void setIsRejected(boolean isRejected) {
		this.isRejected = isRejected;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}
}