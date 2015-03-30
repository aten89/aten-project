/**
 * 
 */
package org.eapp.oa.flow.handler.reim;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.PostService;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.rpc.dto.GroupInfo;
import org.eapp.rpc.dto.PostInfo;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.handler.IDecisionHandler;


/**
 * 报销审批是否要经过报销用户的部门领导
 * @author zsy
 * @version Nov 30, 2008
 */
public class UserDeptManagerDecision implements IDecisionHandler {
	public static final String TRANSITION_YES = "是";
	public static final String TRANSITION_NO = "否";
	
	private static final Log log = LogFactory.getLog(UserDeptManagerDecision.class);
	
	@Override
	public String decide(Execution exe) throws Exception {
		log.info("UserDeptManagerDecision:" + exe.getNode().getName());
		
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_USERACCOUNTID);
		String userAccountId = var.getValue();//报销人
		if (userAccountId == null) {
			log.warn("上下文变量为空：" + SysConstants.FLOW_VARNAME_USERACCOUNTID);
			return TRANSITION_YES;
		}
		//取得用户绑定的群组
		UserAccountService uas = new UserAccountService();
		List<GroupInfo> groups = uas.getBindedGroups(userAccountId);
		if (groups == null || groups.size() ==0) {
			return TRANSITION_NO;
		}
		//取得部门的管理者
		List<UserAccountInfo> users = new ArrayList<UserAccountInfo>();
		PostService ps = new PostService();
		for(GroupInfo g : groups) {
			if (UserDeptManagerAssign.DEPT_TYPE.equals(g.getType())) {//如果是部门
				PostInfo pDto = ps.getPostByID(g.getManagerPostID());//取得部门管理都职位
				List<UserAccountInfo> us = uas.getUserAccountsByPost(pDto.getPostName());//取得职位的用户
				if (us != null) {
					users.addAll(us);
				}
			}
		}

		if (users != null) {//部门经理
			for (UserAccountInfo u : users) {
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
