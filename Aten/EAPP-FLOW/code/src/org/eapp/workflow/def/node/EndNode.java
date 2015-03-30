package org.eapp.workflow.def.node;

import org.dom4j.Element;
import org.eapp.workflow.def.Node;
import org.eapp.workflow.def.Transition;
import org.eapp.workflow.def.parse.WfdlXmlReader;
import org.eapp.workflow.exe.Execution;


/**
 * 流程终止节点
 * @author 林良益
 * 2008-08-28
 * @version 1.0
 */
public class EndNode extends Node{
	//是否结束全流程的标志
	protected boolean endFlow = false; 
	/**
	 * 
	 */
	private static final long serialVersionUID = 4670000123982776824L;

	@Override
	public void read(Element nodeElement, WfdlXmlReader wfdlXmlReader) {
		String endCompleteProcess = nodeElement.attributeValue("end-complete-process");
		if("true".equalsIgnoreCase(endCompleteProcess)){
			endFlow = true;
		}
	}
	
//	@Override
//	public void write(Element nodeElement) {
//		
//	}
	
	@Override
	public void execute(Execution execution) {
		//结束流程处理
		if (endFlow) {
			execution.getFlowInstance().end();
		} else {
			execution.getFlowToken().end();
		}		
		
	}
		
	@Override
	public Transition addLeavingTransition(Transition t) {
		//覆盖父类方法，禁止调用
		throw new UnsupportedOperationException("非法操作 : 终止节点不能添加离开路径");
	}

	public boolean isEndFlow() {
		return endFlow;
	}

	public void setEndFlow(boolean endFlow) {
		this.endFlow = endFlow;
	}
	
}
