/**
 * 
 */
package org.eapp.oa.flow.handler.hr;

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
 * 调出部门经理
 * @author zsy
 * @version Nov 30, 2008
 */
public class InDeptManagerAssign implements IAssignmentHandler {
	private static final Log log = LogFactory.getLog(InDeptManagerAssign.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1979269892494340748L;

	@Override
	public void assign(IAssignable assignable, Execution exe) throws Exception {
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_INGROUPNAME);
		String groupName = var.getValue();//
		PostInfo pDto = HandlerHelper.getGroupManagePost(groupName);
		if (pDto == null) {
			return;
		}
		//授权
		HandlerHelper.assign(assignable, pDto.getPostName());
		log.info("DeptManagerAssign:" + ((TaskInstance)assignable).getTaskName());
		
	}
}
