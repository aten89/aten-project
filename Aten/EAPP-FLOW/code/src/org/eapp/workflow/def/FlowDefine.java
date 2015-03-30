package org.eapp.workflow.def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eapp.workflow.WfmException;
import org.eapp.workflow.def.node.StartNode;
import org.eapp.workflow.def.parse.WfdlJsonReader;
import org.eapp.workflow.def.parse.WfdlReader;
import org.eapp.workflow.def.parse.WfdlXmlReader;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.FlowInstance;



/**
 * 流程定义实体对象
 * 
 * @author 林良益
 * 2008-08-27
 * @version 2.0
 */
public class FlowDefine extends FlowElement implements java.io.Serializable {
	
	/*
	 * 流程状态常量
	 */
	//流程未启用,未启用的流程保存在FlowDraft表中
//	public static final String FLOW_STATE_NO_ENABLE = "NO_ENABLE";
	//流程启用
	public static final String FLOW_STATE_ENABLED = "ENABLED";
	//流程禁用
	public static final String FLOW_STATE_DISABLED = "DISABLED";
	
	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6962700227078365756L;
	//流程起始节点
	protected Node  startNode;
	//流程标识——用来识别流程的血统，同流程版本相关联，统一flowkey的流程，才谈的上version
	protected String flowKey;
	//流程版本
	protected long version;
	//流程状态——参考流程状态常量
	protected String state;
	//流程分类——可以用于对流程相关的业务的分类
	protected String category;
	//流程定义中的节点列表
	protected List<Node> nodes = new ArrayList<Node>(0);
	//节点名称映射表，瞬态变量，通过node list动态计算
	transient Map<String , Node> nodesMap = null;
	//流程定义全局Action
	protected Map<String, Action> actions = null;
	//流程定义的文本描述
	protected String textDefine;
	
	//流程定义上下文变量申明
	protected Map<String, VariableDeclare> variableDeclares = null;
	
	// Constructors

	/** default constructor */
	public FlowDefine() {
	}
	
	/**
	 * 创建空的流程定义对像
	 * @return
	 */
	public static FlowDefine createNewFlowDefine() {
		FlowDefine flowDefine = new FlowDefine();
		flowDefine.setState(FLOW_STATE_ENABLED);
		
		return flowDefine;
	}
	
	/**
	 * 解析流程定义JSON字符窜
	 * @param json
	 * @return
	 */
	public static FlowDefine parseJsonString(String json) {
		WfdlReader fdp = new WfdlJsonReader(json);
		return fdp.getFlowDefine();
	}
	
	/**
	 * 解析流程定义XML字符窜
	 * @param xml
	 * @return
	 */
	public static FlowDefine parseXmlString(String xml) {
		WfdlReader fdp = new WfdlXmlReader(xml);
		return fdp.getFlowDefine();
	}
	
	/**
	 * 创建流程实例
	 * @return
	 */
	public FlowInstance createFlowInstance() {
		return createFlowInstance(null);
	}
	
	
	/**
	 * 创建流程实例
	 * @param contextVariables
	 * @return
	 */
	public FlowInstance createFlowInstance(Map<String, ContextVariable> contextVariables) {
		//验证metaVariables是否为空 验证必填项
		if (variableDeclares != null && variableDeclares.size() > 0) {
			for (VariableDeclare vd : variableDeclares.values()) {
				if (contextVariables == null) {
					throw new WfmException("必须为流程定义中申明的上下文变量赋值");
				}
				String name = vd.getName();
				ContextVariable cv = contextVariables.get(name);
				if (cv == null) {
					throw new WfmException("必须为流程定义中申明的上下文变量“" + name + "”赋值");
				} else if (!vd.getType().equals(cv.getType())) {
					throw new WfmException("流程定义中申明的上下文变量“" + name + "”的类型不对");
				} else if (vd.getNotNull() && cv.getValue() == null) {
					throw new WfmException("流程定义中申明的上下文变量“" + name + "”的值不能为空");
				}
				
			}
		}
		
		FlowInstance fi = new FlowInstance(this ,contextVariables);
		return fi;
	}
	// Property accessors

	/**
	 * @return the startNode
	 */
	public Node getStartNode() {
		return startNode;
	}

	/**
	 * @param startNode the startNode to set
	 */
	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}
	
	public String getFlowKey() {
		return this.flowKey;
	}

	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}

	public long getVersion() {
		return this.version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public List<Node> getNodes() {
		return nodes;
	}
	/**
	 * 建立节点与节点名称映射
	 * @return
	 */
	public Map<String , Node> getNodesMap() {
		if (nodesMap==null) {
			nodesMap = new HashMap<String , Node>();
			if (nodes!=null) {
				Iterator<Node> iter = nodes.iterator();
				while (iter.hasNext()) {
					Node node = iter.next();
					nodesMap.put(node.getName(),node);
				}
			}
		}
		return nodesMap;
	}
	
	/**
	 * 根据节点名称，获取节点对象
	 * @param name
	 * @return
	 */
	public Node getNode(String name) {
		if (nodes==null){
			return null;
		}
		return getNodesMap().get(name);
	}	
	/**
	 * 向流程定义添加节点
	 * 约束：
	 * 1.不能向流程定义添加空(null)的节点
	 * @param node
	 * @return
	 */
	public Node addNode(Node node) {
		if (node == null){
			throw new IllegalArgumentException("不能向流程定义添加空(null)的节点");
		}
		if (nodes == null){
			nodes = new ArrayList<Node>();
		}
		nodes.add(node);
		//建立节点和流程定义的关联
		node.setFlowDefine(this);
		//清空节点映射表，强迫取时重新计算
		nodesMap = null;
		//设置起始节点    
		if( (node instanceof StartNode)&& (this.startNode==null)) {
			this.startNode = node;
		}
		return node;
	}
	
	/**
	 * 清空流程中指定的节点
	 * 约束：
	 * 1.不能从流程定义中移除空(null)的节点
	 * @param node
	 * @return
	 */
	public Node removeNode(Node node) {
		Node removedNode = null;
		if (node == null){
			throw new IllegalArgumentException("不能从流程定义中移除空(null)的节点");
		}
		if (nodes != null) {
			if (nodes.remove(node)) {
				removedNode = node;
				//断开流程定义同节点的关联
				removedNode.setFlowDefine(null);
				//清空节点映射表，强迫取时重新计算
				nodesMap = null;
			}
		}
		//清空起始节点    
		if (startNode==removedNode) {
			startNode = null;
		}
		return removedNode;
	}
	
	/**
	 * 通过名称查找节点
	 * 有子流程时需重载此方法
	 * @param name
	 * @return
	 */
	public Node findNode(String name) {
		//引入组合节点时时需重载
		return getNode(name);
	}
	
	/**
	 * 向流程定义添加动作
	 * 约束：
	 * 1.不能向流程定义添加空(null)的动作
	 * @param action
	 * @return
	 */
	public Action addAction(Action action) {
	    if (action == null) {
	    	throw new IllegalArgumentException("不能向流程定义添加空(null)的动作");
	    }
	    if (action.getName() == null) {
	    	throw new IllegalArgumentException("不能向流程定义添加名称为空(null)的动作");
	    }
	    if (getAction(action.getName()) != null) {
	    	throw new IllegalArgumentException("动作名称:" + action.getName() + "重复");
	    }
	    if (actions == null) {
	    	actions = new HashMap<String, Action>();
	    }
	    
	    actions.put(action.getName(), action);
	    //绑定流程和动作
	    action.setFlowDefine(this);
	    return action;
	}

	/**
	 * 清空流程中指定的动作
	 * 约束：
	 * 1.不能从流程定义中移除空(null)的动作
	 * @param action
	 */
	public void removeAction(Action action) {
		if (action == null) {
			throw new IllegalArgumentException("不能从流程定义中移除空(null)的动作");
		}
	    if (actions != null) {
	    	if (! actions.containsValue(action)) {
	    		throw new IllegalArgumentException("不能从流程定义中移除不存在的动作");
	    	}
	    	actions.remove(action.getName());
	    	//解除流程和动作绑定
	    	action.setFlowDefine(null);
		}
	}
	
	/**
	 * 通过动作名称取得流程定义中的对像
	 * @param name
	 * @return
	 */
	public Action getAction(String name) {
	    if (actions == null) {
	    	return null;
	    }
	    return actions.get(name);
	}
	
	/**
	 * 取得流程定义中的所有动作
	 * @return
	 */
	public Map<String, Action> getActions() {
	    return actions;
	}

	/**
	 * 流程定义中是否有动作
	 * @return
	 */
	public boolean hasActions() {
	    return ((actions!=null) && (actions.size()>0) );
	}
	
//	/**
//	 * 清空ID及级联保存的对像集合的ID
//	 */
//	public void cleanId() {
//		//清空自已ID
//		id = null;
//		//清空动作ID
//		if (actions != null) {
//			for (Action a : actions.values()) {
//				a.cleanId();
//			}
//		}
//		//清空节点ID
//		if (nodes != null) {
//			for (Node n : nodes) {
//				n.cleanId();
//			}
//		}
//		//清空事件ID
//		 if (events != null) {
//			 for (Event e : events.values()) {
//				 e.cleanId();
//			 }
//		 }
//		
//	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 重载FlowElement equals
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FlowDefine))
			return false;
		final FlowDefine other = (FlowDefine) obj;
		if (id == null || other.getId() == null) {
			return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	/**
	 * @param actions the actions to set
	 */
	public void setActions(Map<String, Action> actions) {
		this.actions = actions;
	}

	/**
	 * @return the xmlDefine
	 */
	public String getTextDefine() {
		return textDefine;
	}

	/**
	 * @param xmlDefine the xmlDefine to set
	 */
	public void setTextDefine(String textDefine) {
		this.textDefine = textDefine;
	}

	public Map<String, VariableDeclare> getVariableDeclares() {
		return variableDeclares;
	}

	public void setVariableDeclares(Map<String, VariableDeclare> variableDeclares) {
		this.variableDeclares = variableDeclares;
	}

	/**
	 * 添加上下文变量申明
	 * @param variableDeclare
	 * @return
	 */
	public VariableDeclare addVariableDeclare(VariableDeclare variableDeclare) {
	    if (variableDeclare == null) {
	    	throw new IllegalArgumentException("不能向流程定义添加空(null)的变量定义");
	    }
	    if (variableDeclare.getName() == null) {
	    	throw new IllegalArgumentException("不能向流程定义添加名称为空(null)的变量定义");
	    }
	    if (variableDeclares == null) {
	    	variableDeclares = new HashMap<String, VariableDeclare>();
	    }
	    variableDeclares.put(variableDeclare.getName(), variableDeclare);
	    return variableDeclare;
	}
	
	/**
	 * 通过变量名称取得流程定义中的变量申明
	 * @param name
	 * @return
	 */
	public VariableDeclare getVariableDeclare(String name) {
	    if (variableDeclares == null) {
	    	return null;
	    }
	    return variableDeclares.get(name);
	}
}