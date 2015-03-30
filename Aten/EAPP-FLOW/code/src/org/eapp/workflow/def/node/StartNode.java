package org.eapp.workflow.def.node;

import org.dom4j.Element;
import org.eapp.workflow.def.Node;
import org.eapp.workflow.def.Transition;
import org.eapp.workflow.def.parse.WfdlXmlReader;
import org.eapp.workflow.exe.Execution;



/**
 * 流程起始节点
 * @author 林良益
 * 2008-08-28
 * @version 1.0
 */
public class StartNode extends Node{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1771201859942309315L;

	@Override
	public void read(Element nodeElement, WfdlXmlReader wfdlXmlReader) {
		//去掉开始节点的任务
	}
	
//	@Override
//	public void write(Element nodeElement) {
//		
//	}
	
	@Override
	public void leaveNode(Execution execution, Transition transition) {
		//调用父类方法
		super.leaveNode(execution, transition);
	}
	@Override	  
	public void execute(Execution execution) {
		//覆盖父类方法，空实现
	}
	@Override	  
	public Transition addArrivingTransition(Transition t) {
		//覆盖父类方法，禁止调用
		throw new UnsupportedOperationException( "非法操作 : 起始节点不能添加到达路径" );
	}
}
