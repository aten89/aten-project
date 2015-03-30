/**
 * 
 */
package org.eapp.oa.flow.handler.reim;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.oa.flow.handler.HandlerHelper;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.rpc.dto.PostInfo;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.handler.IDecisionHandler;

/**
 * 报销审批是否要经过部门领导
 * @author zsy
 * @version Nov 30, 2008
 */
public class DeptManagerDecision implements IDecisionHandler {
	public static final String TRANSITION_YES = "是";
	public static final String TRANSITION_NO = "否";
	
	private static final Log log = LogFactory.getLog(DeptManagerDecision.class);
	
	@Override
	public String decide(Execution exe) throws Exception {
		log.info("FGManagerDecision:" + exe.getNode().getName());
		
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_USERACCOUNTID);
		String userAccountId = var.getValue();//报销人
		if (userAccountId == null) {
			log.warn("上下文变量为空：" + SysConstants.FLOW_VARNAME_USERACCOUNTID);
			return TRANSITION_YES;
		}
		
		var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_GROUPNAME);
		String groupName = var.getValue();//费用归属项目编号
		PostInfo pDto = HandlerHelper.getGroupManagePost(groupName);
		if (pDto == null) {
			return TRANSITION_NO;
		}
		
		UserAccountService ps = new UserAccountService();
		//部门经理
		List<UserAccountInfo> pms = ps.getUserAccountsByPost(pDto.getPostName());
		if (pms != null) {//部门经理
			for (UserAccountInfo u : pms) {
				if (u.getAccountID().equals(userAccountId)) {
					log.info(TRANSITION_YES);
					return TRANSITION_YES;
				}
			}
			
		}
		log.info(TRANSITION_NO);
		return TRANSITION_NO;
	}

}
