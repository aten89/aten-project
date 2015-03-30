/**
 * 
 */
package org.eapp.rpc.imp;

import org.eapp.blo.IActionLogBiz;
import org.eapp.dto.LogSaveBean;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.ILogService;
import org.eapp.rpc.dto.LogSaveInfo;
import org.eapp.rpc.session.RPCAuthorizationFilter;
import org.eapp.sys.config.SysConstants;

import com.caucho.hessian.server.HessianServlet;

/**
 * @author zsy
 * @version 
 */
public class LogService extends HessianServlet implements ILogService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2811942709583050518L;
	private static final String MODULE_KEY_ACTION = "action_log";
	private static final String MODULE_KEY_INTERFACE = "interface_log";
	private IActionLogBiz actionLogBiz;
	
	public void setActionLogBiz(IActionLogBiz actionLogBiz) {
		this.actionLogBiz = actionLogBiz;
	}

	@Override
	public void addActionLog(String sessionID, LogSaveInfo log) throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY_ACTION, SysConstants.ADD);
		RPCLogger.interfaceLog(sessionID, MODULE_KEY_ACTION, SysConstants.ADD, null,"方法:LogService.addActionLog 参数:log="+log);
		if (log == null) {
			throw new IllegalArgumentException("LogSaveBean 不能为空");
		}
		actionLogBiz.addLog(copy(log), false);
	}

	@Override
	public void addInterfaceLog(String sessionID, LogSaveInfo log) throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY_INTERFACE, SysConstants.ADD);
		RPCLogger.interfaceLog(sessionID, MODULE_KEY_INTERFACE, SysConstants.ADD, null,"方法:LogService.addInterfaceLog 参数:log="+log);
		if (log == null) {
			throw new IllegalArgumentException("LogSaveBean 不能为空");
		}
		actionLogBiz.addLog(copy(log), true);
		
	}

	/**
     * 复制本地pojo的属性到远程数据传输对象
     * 
     * @param org
     * @param dest
     */
    private LogSaveBean copy(LogSaveInfo from) {
    	LogSaveBean to = new LogSaveBean();
    	to.setSystemID(from.getSystemID());
    	to.setModuleKey(from.getModuleKey());
    	to.setActionKey(from.getActionKey());
    	to.setAccountID(from.getAccountID());
    	to.setObject(from.getObject());
    	to.setObjectID(from.getObjectID());
    	to.setIpAddress(from.getIpAddress());
    	to.setResultStatus(from.getResultStatus());
        return to;
    }
}
