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
import org.eapp.oa.flow.handler.HandlerHelper;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.rpc.dto.GroupInfo;
import org.eapp.rpc.dto.PostInfo;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.workflow.def.IAssignable;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.handler.IAssignmentHandler;


/**
 * 报销审批
 * 查找报销用户的部门经理
 * @author zsy
 * @version Nov 30, 2008
 */
public class UserDeptManagerAssign implements IAssignmentHandler {
	private static final Log log = LogFactory.getLog(UserDeptManagerAssign.class);
	
	public static final String DEPT_TYPE = "D";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1979269892494340748L;

	@Override
	public void assign(IAssignable assignable, Execution exe) throws Exception {
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_USERACCOUNTID);
		String userAccountId = var.getValue();//填单人
		if (userAccountId == null) {
			log.warn("上下文变量为空：" + SysConstants.FLOW_VARNAME_USERACCOUNTID);
			return;
		}
		//取得用户绑定的群组
		UserAccountService uas = new UserAccountService();
		List<GroupInfo> groups = uas.getBindedGroups(userAccountId);
		if (groups == null || groups.size() ==0) {
			return;
		}
		//取得部门的管理者
		List<UserAccountInfo> users = new ArrayList<UserAccountInfo>();
		PostService ps = new PostService();
		for(GroupInfo g : groups) {
			if (DEPT_TYPE.equals(g.getType())) {//如果是部门
				PostInfo pDto = ps.getPostByID(g.getManagerPostID());//取得部门管理都职位
				List<UserAccountInfo> us = uas.getUserAccountsByPost(pDto.getPostName());//取得职位的用户
				if (us != null) {
					users.addAll(us);
				}
			}
		}
		
		//授权
		HandlerHelper.assign(assignable, users);
		
		log.info("UserDeptManagerAssign:" + ((TaskInstance)assignable).getTaskName());
		
	}
}
