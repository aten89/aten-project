package org.eapp.workflow.def.parse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eapp.workflow.WfmException;
import org.eapp.workflow.WfmServices;
import org.eapp.workflow.def.Action;
import org.eapp.workflow.def.Delegation;
import org.eapp.workflow.def.Event;
import org.eapp.workflow.def.FlowDefine;
import org.eapp.workflow.def.FlowElement;
import org.eapp.workflow.def.Node;
import org.eapp.workflow.def.ParallelTaskDefine;
import org.eapp.workflow.def.SerialTaskDefine;
import org.eapp.workflow.def.TaskDefine;
import org.eapp.workflow.def.Transition;
import org.eapp.workflow.def.VariableDeclare;
import org.eapp.workflow.def.node.DecisionCondition;
import org.eapp.workflow.def.node.DecisionNode;
import org.eapp.workflow.def.node.EndNode;
import org.eapp.workflow.def.node.ForkNode;
import org.eapp.workflow.def.node.JoinNode;
import org.eapp.workflow.def.node.StartNode;
import org.eapp.workflow.def.node.SubFlowNode;
import org.eapp.workflow.def.node.TaskNode;
import org.eapp.workflow.expression.ExpressionEvaluator;
import org.eapp.workflow.expression.IllegalExpressionException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 将流程定义文件（JSON）转化成对像
 * @author zhuoshiyao
 *
 */
public class WfdlJsonReader extends WfdlReader {
	private JSONObject jsonObj;
	private HashSet<String> nodeNames;//用来判断节点名称不能重复
	
	public WfdlJsonReader(String json) {
		super(json);
	}
	
	@Override
	protected void readFlowDefine(String json) throws ParserException {
		if (StringUtils.isBlank(json)) {
			throw new ParserException("JSON数据为空");
		}
		jsonObj = JSON.parseObject(json);
		flowDefine = FlowDefine.createNewFlowDefine();
		flowDefine.setTextDefine(json);
		
		nodeNames = new HashSet<String>();

		//读取流程基本属性
		parserFlowAttrs(jsonObj);
		//读取节点
		parserNodes(jsonObj.getJSONObject("nodes"));
		//读取连转向
		parserLines(jsonObj.getJSONObject("lines"));//
	}
	
	/**
	 * 解析流程基本属性
	 * @param jsonObj
	 * @throws ParserException
	 */
	private void parserFlowAttrs(JSONObject jsonObj) throws ParserException {
		if (jsonObj == null || jsonObj.isEmpty()) {
			throw new ParserException("JSON对象为空");
		}
		String flowName = jsonObj.getString("title");
		if (StringUtils.isBlank(flowName)) {
			throw new ParserException("流程名称为空");
		}
		flowDefine.setName(flowName);
		flowDefine.setCategory(jsonObj.getString("category"));
		flowDefine.setDescription(jsonObj.getString("description"));
		//读取流程变量
		//"metas":[{"name":"formId","displayName":"表单ID","type":"DATATYPE_STRING","notNull":true},
		// 		{"name":"groupName","displayName":"发起人部门","type":"DATATYPE_STRING","notNull":false}]
		JSONArray metasObj = jsonObj.getJSONArray("metas");
		if (metasObj != null && !metasObj.isEmpty()) {
			JSONObject metaObj = null;
			for (int i = 0; i < metasObj.size(); i++) {
				metaObj = metasObj.getJSONObject(i);
				String name = metaObj.getString("name");
				String type = metaObj.getString("type");
				if (name == null || type == null) {
					continue;
				}
				String displayName = metaObj.getString("displayName");
				Boolean notNull = metaObj.getBoolean("notNull");
				flowDefine.addVariableDeclare(new VariableDeclare(name, type, notNull, displayName));
			}
		}
		//读取流程事件
		parserEvents(jsonObj.getJSONObject("events"), flowDefine);
	}
	
	/**
	 * 解析事件
	 * "events":{"FLOW_START":"org.eapp.flow.handler.task.TaskCreateEvent",
	 * 		"FLOW_END":"org.eapp.flow.handler.task.TaskGiveUpEvent"}
	 * @param flowElement
	 * @param flowDefine
	 * @param jsonObj
	 * @throws ParserException
	 */
	private void parserEvents(JSONObject eventsObj,FlowElement flowElement) throws ParserException {
		if (eventsObj == null || eventsObj.isEmpty()) {
			return;
		}
		for (String eventType : eventsObj.keySet()) {
			Event event = flowElement.getEvent(eventType);
			if (event == null) {//节点中没有此event则添加
		    	event = new Event(); 
			    event.setEventType(eventType);
	    		flowElement.addEvent(event);
	    	}
			Action action = new Action();
			action.setName(flowElement.getName() + eventType + "事件");
			action.setActionHandler(eventsObj.getString(eventType));
			event.addAction(action);
			 //添加动作到流程定义中
		    flowDefine.addAction(action);
		}
	}
	
	/**
	 * 解析所有节点
	 * @param nodesObj
	 * @throws ParserException
	 */
	private void parserNodes(JSONObject nodesObj) throws ParserException {
		if (nodesObj == null || nodesObj.isEmpty()) {
			return;
		}
		for (String nodeId : nodesObj.keySet()) {
			JSONObject nodeObj = nodesObj.getJSONObject(nodeId);
			String nodeName = nodeObj.getString("name");
			if (StringUtils.isBlank(nodeName)) {
				throw new ParserException("节点名称不能为空");
			}
			if (nodeNames.contains(nodeName)) {
				throw new ParserException("节点名称不能复重复：" + nodeName);
			}
			nodeNames.add(nodeName);
			
			Node node = null;
			String nodeType = nodeObj.getString("type");
			if (nodeType.startsWith("start")) {
				node = parserStartNode(nodeObj);
			} else if(nodeType.startsWith("end")) {
				node = parserEndNode(nodeObj);
			} else if(nodeType.startsWith("fork")) {
				node = parserForkNode(nodeObj);
			} else if(nodeType.startsWith("join")) {
				node = parserJoinNode(nodeObj);
			} else if(nodeType.startsWith("complex")) {
				node = parserSubFlowNode(nodeObj);
			} else if(nodeType.startsWith("chat")) {
				node = parserDecisionNode(nodeObj);
			} else if(nodeType.startsWith("task")) {
				node = parserTaskNode(nodeObj);
			} else if(nodeType.startsWith("node")) {
				node = parserNode(nodeObj);
			} else {
				throw new ParserException("不存在JSON节点类型：" + nodeType);
			}
			//读取流程事件
			parserEvents(nodeObj.getJSONObject("events"), node);
			//关联ID
			node.setRefGraphKey(nodeId);
			
	    	flowDefine.addNode(node);
		}
	}
	
	/**
	 * 解析开始节点
	 * @param nodeObj
	 * @return
	 * @throws ParserException
	 */
	private Node parserStartNode(JSONObject nodeObj) throws ParserException {
		if (flowDefine.getStartNode() != null) {
    		throw new ParserException("最多只允许一个开始节点");
    	}
		StartNode node = new StartNode();
		node.setName(nodeObj.getString("name"));
		node.setDescription(nodeObj.getString("description"));
		return node;
	}
	
	/**
	 * 解析结束节点
	 * @param nodeObj
	 * @return
	 * @throws ParserException
	 */
	private Node parserEndNode(JSONObject nodeObj) throws ParserException {
		EndNode node = new EndNode();
		node.setName(nodeObj.getString("name"));
		node.setDescription(nodeObj.getString("description"));
		
		node.setEndFlow(nodeObj.getBooleanValue("endFlow"));//是否结束合流程
		return node;
	}
	
	/**
	 * 解析分支节点
	 * @param nodeObj
	 * @return
	 * @throws ParserException
	 */
	private Node parserForkNode(JSONObject nodeObj) throws ParserException {
		ForkNode node = new ForkNode();
		node.setName(nodeObj.getString("name"));
		node.setDescription(nodeObj.getString("description"));
		
		//读取条件表达式
		JSONObject condsObj = nodeObj.getJSONObject("conditions");
		JSONObject linesObj = jsonObj.getJSONObject("lines");//所有的线
		if (condsObj != null && !condsObj.isEmpty() 
				&& linesObj != null && !linesObj.isEmpty()) {
			List<DecisionCondition> condList = node.getDecisionConditions();
			if (condList == null) {
				condList = new ArrayList<DecisionCondition>();
				node.setDecisionConditions(condList);
			}
			for (String lineId : condsObj.keySet()) {
				String exp = condsObj.getString(lineId);//表达式值
				if (StringUtils.isBlank(exp)) {
					continue;
				}
				//查找线对应的名称
				JSONObject lineObj = linesObj.getJSONObject(lineId);
				if (lineObj == null) {
					continue;
				}
				//表达式验证
			 	try {
			 		ExpressionEvaluator.test(exp, flowDefine.getVariableDeclares(), Boolean.class);
			 	} catch (IllegalExpressionException e) {
					throw new ParserException("分支节点“" + node.getName() + "”中的分支线““" + lineObj.getString("name") + "””表达式“" + exp + "”解析出错", e);
				}
				condList.add(new DecisionCondition(lineObj.getString("name"), exp));
//				System.out.println(lineObj.getString("name") + "========="+exp);
			}
		}
		return node;
	}
	
	/**
	 * 解析联合节点
	 * @param nodeObj
	 * @return
	 * @throws ParserException
	 */
	private Node parserJoinNode(JSONObject nodeObj) throws ParserException {
		JoinNode node = new JoinNode();
		node.setName(nodeObj.getString("name"));
		node.setDescription(nodeObj.getString("description"));
		return node;
	}

	/**
	 * 解析子流程节点
	 * @param nodeObj
	 * @return
	 * @throws ParserException
	 */
	private Node parserSubFlowNode(JSONObject nodeObj) throws ParserException {
		SubFlowNode node = new SubFlowNode();
		node.setName(nodeObj.getString("name"));
		node.setDescription(nodeObj.getString("description"));
		
		String subFlowName = nodeObj.getString("subFlowName");
		if(StringUtils.isBlank(subFlowName)){
    		throw new ParserException("子流程节点“" + subFlowName + "解析出错：子流程名未定义");
    	}
		node.setSubFlowName(subFlowName);//是否结束合流程
		
		String initial = nodeObj.getString("subFlowInitial");
		if ("immediate".equalsIgnoreCase(initial)) {
			String version = nodeObj.getString("subFlowVersion");
			if (StringUtils.isBlank(version)) {
				throw new ParserException("子流程节点“" + subFlowName + "解析出错：子流程版本不能为空");
			}
			//查找子流程 
			try {
				FlowDefine subFlowDefine = WfmServices.getSubFlowService().findSubFlowDefine(subFlowName, Long.parseLong(version));
				if (subFlowDefine == null) {
					throw new ParserException("子流程节点“" + subFlowName + "解析出错：子流程不存在");
				}
				node.setSubFlowDefine(subFlowDefine);
			} catch (WfmException e) {
	        	e.printStackTrace();
	        }
	        
			
//    		// 子流程与主流程是同一流程定义（递归流程）  死循环？
//    		if (subFlowDefine == null 
//    				&& subFlowName.equals(flowDefine.getName())) {
//    			subFlowDefine = flowDefine;
//    		}
		}
		return node;
	}
	
	/**
	 * 解析机器节点
	 * @param nodeObj
	 * @return
	 * @throws ParserException
	 */
	private Node parserNode(JSONObject nodeObj) throws ParserException {
		Node node = new Node();
		node.setName(nodeObj.getString("name"));
		node.setDescription(nodeObj.getString("description"));
		
		Action action = new Action();
		action.setName(node.getName() + "执行程序");
		action.setActionHandler(nodeObj.getString("action"));
		
		node.setAction(action);
		//添加动作到流程定义中
	    flowDefine.addAction(action);
		return node;
	}
	
	/**
	 * 解析判断节点
	 * @param nodeObj
	 * @return
	 * @throws ParserException
	 */
	private Node parserDecisionNode(JSONObject nodeObj) throws ParserException {
		DecisionNode node = new DecisionNode();
		node.setName(nodeObj.getString("name"));
		node.setDescription(nodeObj.getString("description"));
		
		String expression = nodeObj.getString("expression");
		String className = nodeObj.getString("action");
		if (StringUtils.isNotBlank(expression)) {
			//表达式验证
		 	try {
		 		ExpressionEvaluator.test(expression, flowDefine.getVariableDeclares(), String.class);
		 	} catch (IllegalExpressionException e) {
				throw new ParserException("判断节点“" + node.getName() + "”中的表达式“" + expression + "”解析出错", e);
			}
		 	className = null;
		} else if (StringUtils.isBlank(className)) {
			throw new ParserException("判断节点“" + node.getName() + "”中的表达式与执行程序不能同时为空");
		}
		
		node.setDecisionDelegation(new Delegation(expression, className));
		return node;
	}
	
	/**
	 * 解析机器节点
	 * @param nodeObj
	 * @return
	 * @throws ParserException
	 */
	private Node parserTaskNode(JSONObject nodeObj) throws ParserException {
		TaskNode node = new TaskNode();
		node.setName(nodeObj.getString("name"));
		node.setDescription(nodeObj.getString("description"));
		
		String  taskMultiType= nodeObj.getString("multiType");
        TaskDefine task = null;
        if(taskMultiType == null
        		|| "single".equals(taskMultiType)){
	        task = new TaskDefine();
        }else if("parallel".equals(taskMultiType)){
        	task = new ParallelTaskDefine();
        }else if("serial".equals(taskMultiType)){
        	task = new SerialTaskDefine();
        }else{
        	throw new ParserException("任务节点“" + node.getName() + "”中的任务并发类型错误：" + taskMultiType);
        }	
        
        node.addTask(task);
        task.setName(node.getName());//任务名称默认跟节点名称一样
        task.setDescription(node.getDescription());//默认跟节点一样
    	task.setFlowDefine(flowDefine);
		
    	String expression = nodeObj.getString("assignExpression");
    	String className = nodeObj.getString("assignAction");
    	if (StringUtils.isNotBlank(expression)) {
			//表达式验证
		 	try {
		 		ExpressionEvaluator.test(expression, flowDefine.getVariableDeclares(), String.class);
		 	} catch (IllegalExpressionException e) {
				throw new ParserException("任务节点“" + node.getName() + "”中任务授权的表达式“" + expression + "”解析出错", e);
			}
		 	className = null;
		} else if (StringUtils.isBlank(className)) {
			throw new ParserException("任务节点“" + node.getName() + "”中任务授权的表达式与执行程序不能同时为空");
		}
    	task.setTaskAssignment(new Delegation(expression, className));
    	
    	expression = nodeObj.getString("viewExpression");
    	className = nodeObj.getString("viewAction");
    	if (StringUtils.isNotBlank(expression)) {
			//表达式验证
		 	try {
		 		ExpressionEvaluator.test(expression, flowDefine.getVariableDeclares(), String.class);
		 	} catch (IllegalExpressionException e) {
				throw new ParserException("任务节点“" + node.getName() + "”中的任务表单表达式“" + expression + "”解析出错", e);
			}
		 	className = null;
		} else if (StringUtils.isBlank(className)) {
			throw new ParserException("任务节点“" + node.getName() + "”中任务表单的表达式与执行程序不能同时为空");
		}
    	task.setTaskView(new Delegation(expression, className));
    	
		return node;
	}
	
	
	/**
	 * 解析所有线
	 * @param linesObj
	 * @throws ParserException
	 */
	private void parserLines(JSONObject linesObj) throws ParserException {
		if (linesObj == null || linesObj.isEmpty()) {
			return;
		}
		JSONObject nodesObj = jsonObj.getJSONObject("nodes");//所有的节点
		if (nodesObj == null || nodesObj.isEmpty()) {
			return;
		}
		for (String lineId : linesObj.keySet()) {
			JSONObject lineObj = linesObj.getJSONObject(lineId);
			String name = lineObj.getString("name");
			if (name == null) {
		    	throw new ParserException("转向名称不能为空");
		    }
			//通过from查找node的JSON对象
			JSONObject nodeObj =  nodesObj.getJSONObject(lineObj.getString("from"));
			if (nodeObj == null) {
				throw new ParserException("转向“" + name + "”的from节点JSON对象不存在");
			}
			//通过node名称查打Node对象
			Node node = flowDefine.findNode(nodeObj.getString("name"));
			if (node == null) {
				throw new ParserException("转向“" + name + "”的from节点不存在");
			}
		    if (node.getLeavingTransition(name) != null) {
		    	throw new ParserException("结点“" + name + "”的转向名称不能重复");
		    }
		    Transition transition = new Transition();
		    //添加到Node的离开节点
		    node.addLeavingTransition(transition);
			transition.setFlowDefine(flowDefine);
			transition.setName(name);
		    transition.setDescription(lineObj.getString("description"));
		    //关联ID
		    transition.setRefGraphKey(lineId);
		    
		    //查找节点并添加到达路径
		    nodeObj =  nodesObj.getJSONObject(lineObj.getString("to"));
			if (nodeObj == null) {
				throw new ParserException("转向“" + name + "”的to节点JSON对象不存在");
			}
			//通过node名称查打Node对象
			node = flowDefine.findNode(nodeObj.getString("name"));
			if (node == null) {
				throw new ParserException("转向“" + name + "”的to节点不存在");
			}
		    node.addArrivingTransition(transition);

		    
		    String condition = lineObj.getString("condition");
		    if (StringUtils.isNotBlank(condition)) {
				//表达式验证
			 	try {
			 		ExpressionEvaluator.test(condition, flowDefine.getVariableDeclares(), Boolean.class);
			 	} catch (IllegalExpressionException e) {
					throw new ParserException("转向“" + name + "”中的通过条件表达式“" + condition + "”解析出错", e);
				}
			 	transition.setConditions(condition);
			}
		    
		    //注意：transition下可以绑定多个action，这些action被添加到一个固定的TRANSITION事件中
		    //生成TRANSITION事件绑定到转向中
		    String className = lineObj.getString("action");
		    if (StringUtils.isNotBlank(className)) {
		    	Event event = transition.getEvent(Event.EVENTTYPE_TRANSITION);
		 	    if (event == null) {
		 	    	event = new Event(Event.EVENTTYPE_TRANSITION);
		 	    	transition.addEvent(event);
		     	}
			    
			    Action action = new Action();
				action.setName(name + Event.EVENTTYPE_TRANSITION + "事件");
				action.setActionHandler(className);
				event.addAction(action);
				 //添加动作到流程定义中
			    flowDefine.addAction(action);
		    }
		}
	}
}
