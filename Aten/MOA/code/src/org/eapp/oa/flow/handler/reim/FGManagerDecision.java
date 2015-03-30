/**
 * 
 */
package org.eapp.oa.flow.handler.reim;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.PostService;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.oa.flow.handler.HandlerHelper;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.rpc.dto.PostInfo;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.handler.IDecisionHandler;

/**
 * 申购是否要经过分管领导
 * @author zsy
 * @version Nov 30, 2008
 */
public class FGManagerDecision implements IDecisionHandler {
	public static final String TRANSITION_YES = "是";
	public static final String TRANSITION_NO = "否";
	
	private static final Log log = LogFactory.getLog(FGManagerDecision.class);
	
	@Override
	public String decide(Execution exe) throws Exception {
		log.info("FGManagerDecision:" + exe.getNode().getName());
		
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_GROUPNAME);
		String groupName = var.getValue();//费用归属项目编号
		PostInfo pDto = HandlerHelper.getGroupManagePost(groupName);
		if (pDto == null) {
			return TRANSITION_NO;
		}
		
		PostService ps = new PostService();
		
		//部门上级职位
		PostInfo ppDto = ps.getPostByID(pDto.getParentPostID());
		if (ppDto == null) {
			return TRANSITION_NO;
		}
//		if (CEO_POST.equals(ppDto.getPostName())) {//上级分管是总经理
//			return TRANSITION_NO;
//		}
		
		UserAccountService ups = new UserAccountService();
		//部门经理
		List<UserAccountInfo> pms = ups.getUserAccountsByPost(pDto.getPostName());
			
		//分管领导
		List<UserAccountInfo> fgms = ups.getUserAccountsByPost(ppDto.getPostName());
		
		if (fgms == null || fgms.containsAll(pms)) {//上级分管是同时又是部门经理
			log.info(TRANSITION_NO);
			return TRANSITION_NO;
		}
		log.info(TRANSITION_YES);
		return TRANSITION_YES;
	}

}
