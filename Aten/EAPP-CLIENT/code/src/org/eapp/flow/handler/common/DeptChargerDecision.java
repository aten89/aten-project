/**
 * 
 */
package org.eapp.flow.handler.common;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.handler.HandlerException;
import org.eapp.workflow.handler.IDecisionHandler;

/**
 * 填单人是否为提交部门的分管领导
 * @author zsy
 */
public class DeptChargerDecision implements IDecisionHandler {

	private static final Log log = LogFactory.getLog(DeptChargerDecision.class);
	
	@Override
	public String decide(Execution exe) throws HandlerException {
		log.info("DeptChargerDecision:" + exe.getNode().getName());
		try {
			String userAccountId = HandlerHelper.getUserAccountId(exe);
			if (userAccountId == null) {
				log.warn("上下文变量为空：" + HandlerHelper.FLOW_VARNAME_USERACCOUNTID);
				return HandlerHelper.TRANSITION_NO;
			}
			String groupName = HandlerHelper.getGroupName(exe);
			if (groupName == null) {
				log.warn("上下文变量为空：" + HandlerHelper.FLOW_VARNAME_GROUPNAME);
				return HandlerHelper.TRANSITION_NO;
			}
			//取得部门的管理者
			List<UserAccountInfo> users = HandlerHelper.getGroupManagers(groupName, 1);
			
			if (users != null && !users.isEmpty()) {
				for (UserAccountInfo u : users) {
					if (u.getAccountID().equals(userAccountId)) {
						//是填单人
						return HandlerHelper.TRANSITION_YES;
					}
				}
			}
		} catch(Exception e) {
			throw new HandlerException(e);
		}
		return HandlerHelper.TRANSITION_NO;
	}

}
