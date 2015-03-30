/**
 * 
 */
package org.eapp.workflow.asyn;

import org.eapp.workflow.WfmContext;
import org.eapp.workflow.def.Action;
import org.eapp.workflow.def.Node;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.FlowToken;


/**
 * 异步的动作任务
 * 
 * @author 林良益
 *	2008-10-26
 * @version 2.0
 */
public class AsynActionJob extends AsynJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7001259681934371465L;
	//关联的Action
	Action action;
	
	public AsynActionJob() {
		
	}
	
	public AsynActionJob(FlowToken flowToken){
		super(flowToken);
	}

	@Override
	public boolean execute(WfmContext context)  throws Exception {
	    
	    Execution execution = new Execution(flowToken);
	    execution.setCurrentAction(action);
	    execution.setCurrentEvent(action.getEvent());
	    
	    Node node = (flowToken!=null ? flowToken.getNode() : null);
	    if (node!=null) {
	      node.executeAction(action, execution);
	    } else {
	      action.execute(execution);
	    }

	    context.save(flowToken);
	    return true;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

}
