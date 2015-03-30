/**
 * 
 */
package org.eapp.workflow.def.node;

import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;
import org.eapp.workflow.WfmException;
import org.eapp.workflow.WfmServices;
import org.eapp.workflow.def.Event;
import org.eapp.workflow.def.FlowDefine;
import org.eapp.workflow.def.Node;
import org.eapp.workflow.def.Transition;
import org.eapp.workflow.def.parse.ParserException;
import org.eapp.workflow.def.parse.WfdlXmlReader;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.FlowToken;

/**
 * 子流程节点
 * @author 林良益
 * 2008-10-17
 * @version 2.0
 */
public class SubFlowNode extends Node{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6263841118362103420L;
	//子流程名称
	String subFlowName;
	//关联子流程定义
	FlowDefine subFlowDefine;
	
	
	/**
	 * 读取SubFlowNode的内容
	 * @param nodeElement
	 * @param wfdlXmlReader
	 */
	public void read(Element nodeElement, WfdlXmlReader wfdlXmlReader) {
		//解析SubFlowNode标签
		Element subFlowElement = nodeElement.element("sub-flow");
	    
	    if (subFlowElement!=null) {
	    	//取得子流程名称
	    	subFlowName = wfdlXmlReader.getElementAttribute(subFlowElement , "name");
	    	if(subFlowName == null){
	    		//抛异常
	    		throw new ParserException("子流程节点“" + name + "解析出错：子流程名未定义");
	    	}
	    	//取得初始化状态设定
	    	String initial = wfdlXmlReader.getElementAttribute(subFlowElement ,"initial");
	    	
	    	if ("runtime".equalsIgnoreCase(initial)) {
	    		//运行时初始化子流程定义，这里啥都不做
	    	} else {
	    		String versionStr =  wfdlXmlReader.getElementAttribute(subFlowElement ,"version");
	    		if (versionStr == null) {
	    			throw new ParserException("子流程节点“" + subFlowName + "解析出错：子流程版本不能为空");
	    		}
		      	try {
		      		subFlowDefine = WfmServices.getSubFlowService().findSubFlowDefine(subFlowName, Long.parseLong(versionStr));
		        } catch (WfmException e) {
		        	e.printStackTrace();
		        }

//	    		// 子流程与主流程是同一流程定义（递归流程哦） ————死循环？
//	    		if (subFlowDefine == null 
//	    				&& subFlowName.equals(flowDefine.getName())) {
//	    			subFlowDefine = flowDefine;
//	    		}
		        if (subFlowDefine == null) {
		        	throw new ParserException("子流程节点“" + subFlowName + "解析出错：子流程不存在");
		        }
	    	}
	    }
	}	

	public String getSubFlowName() {
		return subFlowName;
	}



	public void setSubFlowName(String subFlowName) {
		this.subFlowName = subFlowName;
	}



	public FlowDefine getSubFlowDefine() {
		return subFlowDefine;
	}



	public void setSubFlowDefine(FlowDefine subFlowDefine) {
		this.subFlowDefine = subFlowDefine;
	}
	
	
	@Override
	public void execute(Execution execution) {
		//获取主流程Token
		FlowToken superProcessToken = execution.getFlowToken();
		//获取子流程定义    
		FlowDefine usedSubFlowDefine = subFlowDefine;
		//处理延迟绑定的流程定义
		//根据表达式计算动态获取子流程定义名称
		if ( (subFlowDefine==null) && (subFlowName!=null)) {
			//根据流程名称，获取并实例化流程定义
//			String subFlowNameResolved = (String) ExpressionEvaluator.evaluate(subFlowName, execution);
			usedSubFlowDefine = WfmServices.getSubFlowService().findSubFlowDefine(subFlowName, null);
		}
		
		if (usedSubFlowDefine == null) {
			throw new WfmException("找不到子流程");
		}
		//生成子流程实例
		FlowInstance subFlowInstance = superProcessToken.createSubFlowInstance(usedSubFlowDefine);
		
		//触发子流程创建事件
		fireEvent(Event.EVENTTYPE_SUBFLOW_CREATE , execution);

		//传递父流程变量
		//这里我们只简单复制主流程的上下文变量,起到参数传递作用
		Map<String ,ContextVariable> parentVariables = superProcessToken.getFlowInstance().getContextVariables();
		if(parentVariables != null){
			Iterator<String> keys = parentVariables.keySet().iterator();
			String key = null;
			ContextVariable cVarInSuper = null;
			ContextVariable cVarInSub = null;
			for(;keys.hasNext();){
				key = keys.next();
				cVarInSuper = parentVariables.get(key);
				//为子流程建立变量副本，不能共用主流程的
				cVarInSub = new ContextVariable(key , cVarInSuper.getType() ,cVarInSuper.getValue());
				cVarInSub.setDisplayName(cVarInSuper.getDisplayName());
				cVarInSub.setDisplayOrder(cVarInSuper.getDisplayOrder());
				subFlowInstance.addContextVariable(cVarInSub);
			}
		}
		//触发子流程
		subFlowInstance.signal();
	}
	

	@Override
	public void leaveNode(Execution execution , Transition transition) {
		//从上下文获取子流程实例，这个实例是子流程结束时绑定到上下文的
		FlowInstance subFlowInstance = execution.getSubFlowInstance();
		//获取主流程Token
		FlowToken superProcessToken = subFlowInstance.getSuperFlowToken();
		//将子流程变量传回主流程
		Map<String ,ContextVariable> subVariables = subFlowInstance.getContextVariables();
		Map<String ,ContextVariable> superVariables = execution.getContextVaribales();
		if(subVariables != null && superVariables != null){
			Iterator<String> keys = subVariables.keySet().iterator();
			String key = null;
			ContextVariable cVarInSub = null;
			ContextVariable cVarInSuper = null;
			for(;keys.hasNext();){
				key = keys.next();
				cVarInSub = subVariables.get(key);						
				cVarInSuper = superVariables.get(key);
				if(cVarInSuper != null){
					//修改主流程中变量值		
					cVarInSuper.setValue(cVarInSub.getValue());
					cVarInSuper.setDisplayOrder(cVarInSub.getDisplayOrder());
				}else{
					//如果是子流程新增的变量则建立主流程的副本
					cVarInSuper = new ContextVariable(key , cVarInSub.getType() ,cVarInSub.getValue());
					cVarInSuper.setDisplayName(cVarInSub.getDisplayName());
					cVarInSuper.setDisplayOrder(cVarInSub.getDisplayOrder());
					superVariables.put(key, cVarInSuper);
				}
			}
		}		
		
	    //触发子流程结束事件 fire the subprocess ended event
	    fireEvent(Event.EVENTTYPE_SUBFLOW_END , execution);
	    //移除主流程Token关联的子流程
	    superProcessToken.setSubFlowInstance(null);
	    //还应该移除上下文的子流程关联
	    execution.setSubFlowInstance(null);		  

	    //离开SubFlowNode
	    super.leaveNode(execution , getDefaultLeavingTransition());
	}
	

}
