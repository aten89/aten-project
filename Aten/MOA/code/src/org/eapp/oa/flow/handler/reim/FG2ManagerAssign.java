/**
 * 
 */
package org.eapp.oa.flow.handler.reim;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.flow.handler.HandlerHelper;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.rpc.dto.PostInfo;
import org.eapp.workflow.def.IAssignable;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.handler.IAssignmentHandler;

/**
 * 报销审批
 * 查找费用归属项目的分领导
 * @author zsy
 * @version Nov 30, 2008
 */
public class FG2ManagerAssign implements IAssignmentHandler {
	private static final Log log = LogFactory.getLog(FG2ManagerAssign.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1979269892494340748L;

	@Override
	public void assign(IAssignable assignable, Execution exe) throws Exception {
		log.info("FGManagerAssign:" + exe.getNode().getName());
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_GROUPNAME);
		String groupName = var.getValue();//费用归属项目编号
		PostInfo ppDto = HandlerHelper.getGroupHighLevel2Post(groupName);
		if (ppDto == null) {
			return;
		}
		
		//授权
		HandlerHelper.assign(assignable, ppDto.getPostName());
		
		log.info("DeptManagerAssign:" + ((TaskInstance)assignable).getTaskName());
		
	}
}
