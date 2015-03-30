/**
 * 
 */
package org.eapp.workflow.def.node;

import java.util.Iterator;

import org.dom4j.Element;
import org.eapp.workflow.def.Node;
import org.eapp.workflow.def.parse.WfdlXmlReader;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.FlowToken;


/**
 * 分支归并节点
 * @author 林良益
 * 2008-09-11
 * @version 2.0 
 */
public class JoinNode extends Node {

	/**
	 * 
	 */
	private static final long serialVersionUID = 202965139883524520L;
	
	@Override
	public void read(Element nodeElement, WfdlXmlReader wfdlXmlReader) {
		
	}
	
	@Override
	public void execute(Execution execution){
		FlowToken token = execution.getFlowToken();
		//默认情况下，每个子令牌都能激活其父令牌
	    boolean isAbleToReactivateParent = token.getIsAbleToReactivateParent();
	    //结束当前子令牌
	    if (!token.hasEnded()) {
	    	token.end(false);
	    }
	    
	    if(isAbleToReactivateParent){
	    	// 设置到达归并节点的子令牌的isAbleToReactivateParent标志为false
	    	//每个子令牌只能激活父令牌一次
	    	//所有到达归并节点的子令牌的isAbleToReactivateParent标志都被设置为false
	    	token.setIsAbleToReactivateParent(false);
	    	// 获取子令牌的父令牌
	    	FlowToken parent = token.getParent();
	    	//如果父令牌不为空，则要根据节点规则，启动父令牌
	    	if(parent != null){
	    		//默认的，如果父令牌上所有指定的子令牌都到达归并节点，则父令牌要被激活
	    		boolean reactivateParent = hasAllChildrenTokenArrived(parent, 
	    															parent.getChildren().keySet().iterator());
	    		if(reactivateParent){
	    			// 构造父令牌的执行上下文，并离开join节点
	    			Execution parentContext = new Execution(parent);
	    			leaveNode(parentContext);
	    		}
	    	}
	    }		
	}
	/**
	 * 判定父令牌下属的子令牌是否全部到达归并节点
	 * @param parent
	 * @param childrenTokenNames
	 * @return
	 */
	private boolean hasAllChildrenTokenArrived(FlowToken parent , Iterator<String> childrenTokenNames ){
		boolean reactivateParent = true;
		while((childrenTokenNames.hasNext()) && (reactivateParent) ){
			String concurrentTokenName = childrenTokenNames.next();
			FlowToken concurrentToken = parent.getChild(concurrentTokenName);
			//如果有子令牌的isAbleToReactivateParent=true，说明还有子令牌没有到达归并节点
			if(concurrentToken.getIsAbleToReactivateParent()) {
				reactivateParent = false;
			}
		}
		return reactivateParent;
	}
}
