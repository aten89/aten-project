/**
 * 
 */
package org.eapp.flow.handler.common;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.workflow.def.IAssignable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.handler.HandlerException;
import org.eapp.workflow.handler.IAssignmentHandler;

/**
 * 授权给提交部门的领导
 * @author zsy
 */
public class DeptLeaderAssign implements IAssignmentHandler {
	private static final Log log = LogFactory.getLog(DeptLeaderAssign.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1979269892494340748L;

	@Override
	public void assign(IAssignable assignable, Execution exe) throws HandlerException {
		log.info("DeptLeaderAssign:" + exe.getNode().getName());
		try {
			String groupName = HandlerHelper.getGroupName(exe);
			if (groupName == null) {
				log.warn("上下文变量为空：" + HandlerHelper.FLOW_VARNAME_GROUPNAME);
				return;
			}
			//取得部门的管理者
			List<UserAccountInfo> users = HandlerHelper.getGroupManagers(groupName, 0);
			//授权
			HandlerHelper.assign(assignable, users);
		} catch(Exception e) {
			throw new HandlerException(e);
		}
	}
}
