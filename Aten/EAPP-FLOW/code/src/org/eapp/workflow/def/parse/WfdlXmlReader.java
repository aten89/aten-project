/**
 * 
 */
package org.eapp.workflow.def.parse;

import java.io.StringReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eapp.workflow.WfmException;
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
import org.eapp.workflow.def.action.ActionTypes;
import org.eapp.workflow.def.node.NodeTypes;
import org.eapp.workflow.def.node.StartNode;
import org.eapp.workflow.def.node.TaskNode;
import org.eapp.workflow.expression.ExpressionEvaluator;
import org.eapp.workflow.expression.IllegalExpressionException;
import org.xml.sax.InputSource;

/**
 * 将流程定义文件（XML）转化成对像
 * @author 卓诗垚
 * @version 1.0
 */
public class WfdlXmlReader extends WfdlReader {
	private String initialNodeName;//起始结点
	private Collection<Object[]> unresolvedTransitionDestinations;//等处理的转向列表
	
	private Document document;
	
	public WfdlXmlReader(String xml) {
		super(xml);
	}
	
	/**
	 * 实现父类的方法
	 * 从document中读取并封装成FlowDefine对像
	 */
	@Override
	protected void readFlowDefine(String xml) {
		StringReader stringReader = null;
		try {
			stringReader = new StringReader(xml);
			SAXReader saxReader = new SAXReader();//
			document = saxReader.read(new InputSource(stringReader));
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			if (stringReader != null) {
				stringReader.close();
			}
		}
		if (document == null) {
			throw new WfmException("define xml parse error");
		}
		//生成空的流程实例
		flowDefine = FlowDefine.createNewFlowDefine();
		//保存流程定义的XML描述
		flowDefine.setTextDefine(document.asXML());
		
		unresolvedTransitionDestinations = new ArrayList<Object[]>();
		Element root = document.getRootElement();
		
		
		//流程定义基本属性
		readFlowDefineAttributes(root);
		//流程定义的上下文变最申明
		readVariableDeclares(root);
		/*第一步解析*/
		readNodes(root);//读取节点
		readEvents(root, flowDefine);//读取流和事件
		/*第二步解析*/
		resolveTransitionDestinations();//节点的处理转向
	}
	
	/**
	 * 读取流程定义基本属性
	 * @param root
	 */
	private void readFlowDefineAttributes(Element root) {
	//	flowDefine.setId(getElementAttribute(root, "id"));
		flowDefine.setName(getElementAttribute(root, "name"));
		flowDefine.setFlowKey(getElementAttribute(root, "flow-key"));
		initialNodeName = getElementAttribute(root, "initial");
		flowDefine.setDescription(getElementText(root.element("description")));

		//分类
	    String category = getElementText(root.element("category"));
	    if (category == null) {
	    	category = getElementAttribute(root, "category");
	    }
		flowDefine.setCategory(category);
	}
	
	/**
	 * 读取流程定义中表达式用到的上下文变量申明
	 * @param root
	 */
	@SuppressWarnings("unchecked")
	private void readVariableDeclares(Element root) {
		Iterator<Element> iter = root.elementIterator("meta");
		while (iter.hasNext()) {
			Element ele = iter.next();
			String name = getElementAttribute(ele, "name");
			String type = getElementAttribute(ele, "type");
			if (name == null || type == null) {
				continue;
			}
			String displayName = getElementAttribute(ele, "display-name");
			String notNull = getElementAttribute(ele, "not-null");
			flowDefine.addVariableDeclare(new VariableDeclare(
					name, type, "true".equalsIgnoreCase(notNull), displayName));
		}
	}
	
	/**
	 * 遍历读取节点信息，并添加到流程定义中
	 * @param root
	 */
	private void readNodes(Element root) {
		Iterator<?> nodeElementIter = root.elementIterator();
		while (nodeElementIter.hasNext()) {
			Element nodeElement = (Element) nodeElementIter.next();
		   	String nodeName = nodeElement.getName();
			Class<?> nodeType = NodeTypes.getNodeType(nodeName);
			if (nodeType != null) {
				Node node = null;
		    	try {
		    		node = (Node)nodeType.newInstance();
		    	} catch(Exception e) {
		    		throw new ParserException("创建节点实例失败，结点名称：" + nodeName);
		    	}
		    	node.setFlowDefine(flowDefine);
		    	if (node instanceof StartNode && flowDefine.getStartNode() != null) {
		    		throw new ParserException("最多只允许一个开始节点");
		    	} else {
		    		readNode(nodeElement, node);
		    	}
			}
		}
	}
	
	/**
	 * 从XML中读取，并初始化节点的属性
	 * @param nodeElement
	 * @param node
	 */
	private void readNode(Element nodeElement, Node node) {
		//节点名称
		String name = getElementAttribute(nodeElement, "name");
		if (name == null) {
	    	throw new ParserException("节点名称不能为空");
	    }
	    if (flowDefine.getNode(name) != null) {
	    	throw new ParserException("节点名称不能重复");
	    }
	    node.setName(name);
	    //添加到流程定义
		flowDefine.addNode(node);
    	if ( initialNodeName != null && initialNodeName.equals(name)) {
    		//开始节点
    		flowDefine.setStartNode(node);
    	}
	    //节点ID
//	    node.setId(getElementAttribute(nodeElement, "id"));
	    //节点说明
	    node.setDescription(getElementText(nodeElement.element("description")));
	    //是否同步
	    String asyncText = getElementAttribute(nodeElement, "is-async");
	    if ("true".equalsIgnoreCase(asyncText)) {
	      node.setIsAsync(true);
	    }
	    //不同类型节点含有不同内容，调用各自的读取方法，如Node有动作，TaskNode有Task等
	    node.read(nodeElement, this);
	    
	    //节点事件
	    readEvents(nodeElement, node);
	    //保存transitions到集合中，到最后解析
	    addUnresolvedTransitionDestination(nodeElement, node);

	}
	
	/**
	 * 遍历读取节点中的事件，并添加到节点中
	 * @param parentElement
	 * @param flowElement 节点实例
	 */
	@SuppressWarnings("unchecked")
	private void readEvents(Element parentElement, FlowElement flowElement) {
		Iterator<Element> iter = parentElement.elementIterator("event");
	    while (iter.hasNext()) {
	    	Element eventElement = iter.next();
	    	String eventType = getElementAttribute(eventElement, "type");
	    	Event event = flowElement.getEvent(eventType);
	    	if (event == null) {//节点中没有此event则添加

		    	event = new Event(); 
			    event.setEventType(eventType);
//		    	String eventID = getElementAttribute(eventElement, "id");
//			    event.setId(eventID);
	    		flowElement.addEvent(event);
	    	}
	    	//添加动作到事件
	    	readActions(eventElement, event);
	    }
	}
	
	/**
	 * 遍历读取事件中的动作，并添加到事件中
	 * @param eventElement
	 * @param event
	 */
	public void readActions(Element eventElement, Event event) {
		Iterator<?> nodeElementIter = eventElement.elementIterator();
	    while (nodeElementIter.hasNext()) {
	    	Element actionElement = (Element) nodeElementIter.next();
	    	String actionName = actionElement.getName();
	    	if (ActionTypes.hasActionName(actionName)) {
	    		Action action = createAction(actionElement);
	    		if (event != null) {//为空时处理流程全局的动作
	    			event.addAction(action);
	    		}
	    	}
	    }
	}
	
	
	/**
	 * 根据动作结点名称，取得对应的动作类，创建动作实例
	 * @param actionElement 动作标签
	 * @return 动作
	 */
	public Action createAction(Element actionElement) {
	    // create a new instance of the action
	    Action action = null;
	    String actionName = actionElement.getName();
	    try {
	    	Class<?> actionType = ActionTypes.getActionType(actionName);
	    	action = (Action)actionType.newInstance();
	    } catch (Exception e) {
	    	throw new ParserException("创建动作实例失败，动作名称：" + actionName);
	    }

	    // read the common node parts of the action
	    readAction(actionElement, action);
	    
	    return action;
	}
	
	/**
	 * 从XML中读取，并初始化动作的属性，再添加到流程定义中
	 * @param element 动作标签
	 * @param action 动作实例
	 */
	private void readAction(Element element, Action action) {
	    // 动作名称
	    String actionName = getElementAttribute(element, "name");
	    if (actionName == null) {
	    	throw new ParserException("动作名称不能为空");
	    }
	    if (flowDefine.getAction(actionName) != null) {
	    	throw new ParserException("动作名称:" + actionName + "重复");
	    }
	    action.setName(actionName);
	    //添加动作到流程定义中
	    flowDefine.addAction(action);
	    //动作ID
//	    action.setId(getElementAttribute(element, "id"));
	    
	    //不同类型动作含有不同内容，调用各自的读取方法
	    action.read(element, this);
	}
	
/*处理转向 start*/	
	/**
	 * 暂存转向（Transition）信息
	 * @param nodeElement
	 * @param node
	 */
	private void addUnresolvedTransitionDestination(Element nodeElement, Node node) {
		unresolvedTransitionDestinations.add(new Object[]{nodeElement, node});
	}
	
	/**
	 * 处理转向（Transition）信息
	 */
	@SuppressWarnings("unchecked")
	private void resolveTransitionDestinations() {
		Iterator<Object[]> iter = unresolvedTransitionDestinations.iterator();
	    while (iter.hasNext()) {
	    	Object[] unresolvedTransition = (Object[]) iter.next();
	    	Element nodeElement = (Element) unresolvedTransition[0];
	    	Node node = (Node) unresolvedTransition[1];
	    	Iterator<Element> it = nodeElement.elements("transition").iterator();
	        while (it.hasNext()) {
	        	Element transitionElement = it.next();
	        	resolveTransitionDestination(transitionElement, node);
	        }
	    }
	}
	
	/**
	 * 
	 * @param transitionElement
	 * @param node
	 * @return
	 */
	private void resolveTransitionDestination(Element transitionElement, Node node) {
		Transition transition = new Transition();
		transition.setFlowDefine(flowDefine);
		//名称
		String name = getElementAttribute(transitionElement, "name");
		if (name == null) {
	    	throw new ParserException("转向名称不能为空");
	    }
	    if (node.getLeavingTransition(name) != null) {
	    	throw new ParserException("结点“" + name + "”的转向名称不能重复");
	    }
		transition.setName(name);
		//添加到Node的离开节点
	    node.addLeavingTransition(transition);
		//ID
//		transition.setId(getElementAttribute(transitionElement, "id"));
		
		//描述
	    transition.setDescription(getElementText(transitionElement.element("description")));
	    //转向条件
	    Element conditionElement = transitionElement.element("condition");
	    String condition = null;
	    if (conditionElement != null) {
	    	condition = getElementText(conditionElement);
	    	if (condition == null) {//再为空取子节点属性
          		condition = getElementAttribute(conditionElement, "expression");
          	}
	    } else {
	    	condition = getElementAttribute(transitionElement, "condition");
	    }
	    if (condition != null) {
		    //表达式验证
	    	try {
	    		ExpressionEvaluator.test(condition, flowDefine.getVariableDeclares(), Boolean.class);
			} catch (IllegalExpressionException e) {
				throw new ParserException("转向“" + name + "”中的表达式“" + condition + "”解析出错", e);
			}
		    transition.setConditions(condition);
	    }
	    //查找节点并添加到达路径
	    String toName = getElementAttribute(transitionElement, "to");
	    if (toName != null) {
	    	Node to = flowDefine.findNode(toName);
	    	if (to != null) {
	    		to.addArrivingTransition(transition);
	    	}
	    }
	    
	    //注意：transition下可以绑定多个action，这些action被添加到一个固定的TRANSITION事件中
	    //生成TRANSITION事件绑定到转向中
	    Event event = transition.getEvent(Event.EVENTTYPE_TRANSITION);
	    if (event == null) {
	    	event = new Event(Event.EVENTTYPE_TRANSITION);
	    	transition.addEvent(event);
    	}
	    //读取转向下的动作，并添加到事件中
	    readActions(transitionElement, event);
	    //readExceptionHandlers(transitionElement, transition);扩展读取节点中的异常Handel
	}
	
/*处理转向 end*/		

	
	/**
	 * 遍历读取任务节点中的任务
	 * @param element
	 * @param taskNode
	 */
	@SuppressWarnings("unchecked")
	public void readTasks(Element element, TaskNode taskNode) {
		Iterator<Element> iter = element.elementIterator("task");
		while (iter.hasNext()) {
	        Element taskElement = (Element) iter.next();
	        String  taskMultiType= getElementAttribute(taskElement, "multi-type");
	        TaskDefine task = null;
	        if(null == taskMultiType
	        		|| "single".equals(taskMultiType)){
		        task = new TaskDefine();
	        }else if("parallel".equals(taskMultiType)){
	        	task = new ParallelTaskDefine();
	        }else if("serial".equals(taskMultiType)){
	        	task = new SerialTaskDefine();
	        }else{
	        	throw new ParserException("任务节点“" + taskNode.getName() + "”中的任务并发类型错误");
	        }	        
	        
	        if (taskNode != null) {
	        	taskNode.addTask(task);
	        	task.setName(taskNode.getName());//任务名称默认跟节点名称一样
	        }
	        readTask(taskElement, task);
	 	}
	}
	
	/**
	 * 从XML中读取，并初始化任务的属性
	 * @param taskElement
	 * @param task
	 */
	public void readTask(Element taskElement, TaskDefine task) {
		
		task.setFlowDefine(flowDefine);
		//ID
//		task.setId(getElementAttribute(taskElement, "id"));
		//名称
		String name = getElementAttribute(taskElement, "name");
	    if (name != null) {
	    	task.setName(name);
	    }
	    //说明
	    task.setDescription(getElementText(taskElement.element("description")));
	    
	    //任务执行顺序
	    String temp = getElementText(taskElement.element("execute-order"));
	    if (temp == null) {
	    	temp = getElementAttribute(taskElement, "execute-order");
	    }
	    if (temp != null) {
	    	try {
	    		task.setExecuteOrder(new Integer(temp));
	    	} catch(NumberFormatException e) {
	    		e.printStackTrace();
	    	}
	    }
	    //任务优先级
	    temp = getElementText(taskElement.element("priority"));
	    if (temp == null) {
	    	temp = getElementAttribute(taskElement, "priority");
	    }
	    if (temp != null) {
	    	try {
	    		task.setPriority(new Integer(temp));
	    	} catch(NumberFormatException e) {
	    		e.printStackTrace();
	    	}
	    }
	    //到期日
	    temp = getElementText(taskElement.element("due-date"));
	    if (temp == null) {
	    	temp = getElementAttribute(taskElement, "due-date");
	    }
	    if (temp != null) {
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    	try {
				Date dueDate = sdf.parse(temp);
				task.setDueDate(new Timestamp(dueDate.getTime()));
			} catch (ParseException e) {
				throw new ParserException("任务“" + name + "”中的due-date“" + temp + "”时间格式不对", e);
			}
	    	
	    }
	    //任务分派
	    Element assignmentElement = taskElement.element("assignment");
	    if (assignmentElement != null) {
	    	String className = getElementAttribute(assignmentElement, "class");
	    	String expression = getElementText(assignmentElement);
	    	if (expression == null) {
	    		expression = getElementAttribute(assignmentElement, "expression");
	    	}
	    	
	    	if (expression != null) {
	 		    //表达式验证
	 	    	try {
	 	    		ExpressionEvaluator.test(expression, flowDefine.getVariableDeclares(), String.class);
	 			} catch (IllegalExpressionException e) {
	 				throw new ParserException("任务“" + name + "”中的表达式“" + expression + "”解析出错", e);
	 			}
	 	    }

	    	if (className != null || expression != null) {
	    		Delegation taskAssignment = new Delegation(expression, className);
		    	//ID
//		    	taskAssignment.setId(getElementAttribute(assignmentElement, "id"));
		    	task.setTaskAssignment(taskAssignment);
	    	}
		} 
	    //任务视图
	    Element viewElement = taskElement.element("view-binding");
	    if (viewElement != null) {
	    	String className = getElementAttribute(viewElement, "class");
	    	String viewUrl = getElementText(viewElement);
	    	
	    	if (viewUrl != null) {
	 		    //表达式验证
	 	    	try {
	 	    		ExpressionEvaluator.test(viewUrl, flowDefine.getVariableDeclares(), String.class);
	 			} catch (IllegalExpressionException e) {
	 				throw new ParserException("任务“" + name + "”中的表达式“" + viewUrl + "”解析出错", e);
	 			}
	 	    }
			
	    	if (className != null || viewUrl != null) {
	    		Delegation taskView = new Delegation(viewUrl, className);
	    		//ID
//	    		taskView.setId(getElementAttribute(viewElement, "id"));
		    	task.setTaskView(taskView);
	    	}
		} 
	    //授权完通知用户
	    
	    //任务事件
	    readEvents(taskElement, task);
	}

	
	/**
	 * 取得节点的文本性值，若为空字窜或null返回默认值
	 * @param e document节点
	 * @return 节点文本
	 */
	public String getElementText(Element e) {
		if (e != null) {
			String str = e.getTextTrim();
			if (StringUtils.isNotBlank(str)) {
				return str;
			}
		}
		
		return null;
	}
	
	/**
	 * 取得节点的属性值，若为空字窜或null返回默认值
	 * @param e document节点
	 * @param name 属性名
	 * @return 属性值
	 */
	public String getElementAttribute(Element e, String name) {
		if (e != null && name != null) {
			String str = e.attributeValue(name);
			if (StringUtils.isNotBlank(str)) {
				return str.trim();
			}
		}
		return null;
	}
}
