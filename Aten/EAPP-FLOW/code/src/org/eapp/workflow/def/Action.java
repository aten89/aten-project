package org.eapp.workflow.def;

import org.dom4j.Element;
import org.eapp.workflow.def.parse.ParserException;
import org.eapp.workflow.def.parse.WfdlXmlReader;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.expression.ExpressionEvaluator;
import org.eapp.workflow.expression.IllegalExpressionException;
import org.eapp.workflow.handler.HandlerException;
import org.eapp.workflow.handler.IActionHandler;



/**
 * 流程中定义的动作对象
 * 
 * @author 林良益
 * 2008-06-27
 * @version 1.0
 */
public class Action implements java.io.Serializable , IActionHandler {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2676367575417703928L;
	// Fields
	//物理ID
	String id;
	//动作名称
	protected String name;
	//绑定的处理类全名称
	private String actionHandler;
	//绑定的处理表达式
	private String actionExpression;
	//动作关联的事件
	protected Event event;
	//动作关联的流程定义
	protected FlowDefine flowDefine;
	//动作是否异步运行
	protected Boolean isAsync = Boolean.FALSE;
	//是否接受传播事件
	protected Boolean acceptPropagationEvent = Boolean.TRUE;
	
	// Constructors

	/** default constructor */
	public Action() {
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

	public String getActionHandler() {
		return actionHandler;
	}

	public void setActionHandler(String actionHandler) {
		this.actionHandler = actionHandler;
	}

	public String getActionExpression() {
		return actionExpression;
	}

	public void setActionExpression(String actionExpression) {
		this.actionExpression = actionExpression;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public FlowDefine getFlowDefine() {
		return flowDefine;
	}

	public void setFlowDefine(FlowDefine flowDefine) {
		this.flowDefine = flowDefine;
	}

	public Boolean getIsAsync() {
		return isAsync;
	}

	public void setIsAsync(Boolean isAsync) {
		this.isAsync = isAsync;
	}
	
	/**
	 * 读取普通动作的内容,普通动作可以绑定多个事件
	 * 1.可设置是否接受传播事件
	 * 2.可设置是否异步运行
	 * 3.可设置Handel类或表达式
	 * @param actionElement
	 * @param wfdlXmlReader
	 */
	public void read(Element actionElement, WfdlXmlReader wfdlXmlReader) {
		//是否接受传播事件
	    String acceptPropagatedEvents = wfdlXmlReader.getElementAttribute(actionElement, "accept-propagated-events");
	    if ("false".equalsIgnoreCase(acceptPropagatedEvents)
	    		|| "no".equalsIgnoreCase(acceptPropagatedEvents) 
	    		|| "off".equalsIgnoreCase(acceptPropagatedEvents)) {
	    	acceptPropagationEvent = Boolean.FALSE;
	    }
	    //动作是否异步运行
	    String asyncText = wfdlXmlReader.getElementAttribute(actionElement, "is-async");
	    if ("true".equalsIgnoreCase(asyncText)) {
	    	isAsync =  Boolean.TRUE;
	    }
	    //动作的处理类或表达式
	    String handelClassName = wfdlXmlReader.getElementAttribute(actionElement, "class");
	    if (handelClassName != null) {
	    	actionHandler = handelClassName;
	    } else {
	    	String expression = wfdlXmlReader.getElementText(actionElement);
	    	if (expression == null) {
	    		expression = wfdlXmlReader.getElementAttribute(actionElement, "expression");
	    	}
	    	if (expression != null) {
	 		    //表达式验证
	 	    	try {
	 	    		ExpressionEvaluator.test(expression, flowDefine.getVariableDeclares(), Object.class);
	 			} catch (IllegalExpressionException e) {
	 				throw new ParserException("动作“" + name + "”中的表达式“" + expression + "”解析出错", e);
	 			}
	 			//表达式
		    	this.setActionExpression(expression);
	 	    }
	    }
	}
	
	/**
	 * 写入到XML文档
	 * 
	 */
//	public void write(Element nodeElement) {
//		
//	}

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
		if (!(obj instanceof Action))
			return false;
		final Action other = (Action) obj;
		if (id == null || other.getId() == null) {
			return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}
	
	/**
	 * 执行Action绑定的表达式或Handler接口实现
	 */
	public void execute(Execution execution) throws HandlerException {		
		if (actionHandler!=null) {
			try {
				Class<?> handlerClass = Class.forName(actionHandler);
				IActionHandler actionHandler = (IActionHandler)handlerClass.newInstance();
				actionHandler.execute(execution);
			} catch (ClassNotFoundException e) {
				throw new HandlerException(e);
			} catch (InstantiationException e) {
				throw new HandlerException(e);
			} catch (IllegalAccessException e) {
				throw new HandlerException(e);
			}
			
		} else if (actionExpression!=null) {
			//执行动作表达式
			//event中的行为，不影响流程走向，调用表达式函数
			ExpressionEvaluator.evaluate(actionExpression, execution, Object.class);
		}
	}

	public Boolean isAcceptPropagationEvent() {
		return acceptPropagationEvent;
	}

	public void setAcceptPropagationEvent(Boolean acceptPropagationEvent) {
		this.acceptPropagationEvent = acceptPropagationEvent;
	}
	
//	/**
//	 * 清空ID及级联保存的对像集合的ID
//	 */
//	public void cleanId() {
//		id = null;
//		//级联保存的对像集合
//	}
	
}