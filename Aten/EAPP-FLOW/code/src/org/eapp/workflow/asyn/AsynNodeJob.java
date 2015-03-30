/**
 * 
 */
package org.eapp.workflow.asyn;

import org.eapp.workflow.WfmContext;
import org.eapp.workflow.def.Node;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.FlowToken;


/**
 * 异步结点任务
 * 当进入一个节点时，有可能产生一个异步的节点任务
 * 其功能是异步的调用node的execute
 * @author 林良益
 * @version 2.0
 */
public class AsynNodeJob extends AsynJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9121039575437727453L;
	//关联的节点对象
	Node node;
	
	public AsynNodeJob() {
		
	}
	
	public AsynNodeJob(FlowToken flowToken){
		super(flowToken);
	}

	@Override
	public boolean execute(WfmContext context) throws Exception {
	    flowToken.unlock(this.toString());
	    Execution execution = new Execution(flowToken);
	    node.execute(execution);
	    context.save(flowToken);
	    return true;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

}
