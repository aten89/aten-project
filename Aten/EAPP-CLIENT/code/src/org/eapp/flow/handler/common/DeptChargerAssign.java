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
 * 授权给提交部门的分管领导
 * @author zsy
 */
public class DeptChargerAssign implements IAssignmentHandler {
	private static final Log log = LogFactory.getLog(DeptChargerAssign.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1979269892494340748L;

	@Override
	public void assign(IAssignable assignable, Execution exe) throws HandlerException {
		log.info("DeptChargerAssign:" + exe.getNode().getName());
		try {
			String groupName = HandlerHelper.getGroupName(exe);
			if (groupName == null) {
				log.warn("上下文变量为空：" + HandlerHelper.FLOW_VARNAME_GROUPNAME);
				return;
			}
			//取得部门的管理者
			List<UserAccountInfo> users = HandlerHelper.getGroupManagers(groupName, 1);
			//授权
			HandlerHelper.assign(assignable, users);
		} catch(Exception e) {
			throw new HandlerException(e);
		}
	}
}
