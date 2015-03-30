/**
 * 
 */
package org.eapp.rpc.imp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IActionLogBiz;
import org.eapp.dto.LogSaveBean;
import org.eapp.hbean.ActionLog;
import org.eapp.rpc.session.RPCPrincipal;
import org.eapp.rpc.session.RPCSession;
import org.eapp.rpc.session.RPCSessionContainer;
import org.eapp.sys.config.SysConstants;
import org.eapp.util.spring.SpringHelper;


/**
 * @author zsy
 * @version 
 */
public class RPCLogger {
	private static final Log log = LogFactory.getLog(RPCLogger.class);
	private static IActionLogBiz actionLogBiz = (IActionLogBiz) SpringHelper.getBean("actionLogBiz");
	
	/**
	 * 初始化LogSaveBean
	 * @param sessionID
	 * @param moduleKey
	 * @param action
	 * @param objectID
	 * @param object
	 * @param result
	 * @return
	 */
	private static LogSaveBean getLogSaveBean(String sessionID, String moduleKey, String action, String objectID, String object, boolean result) {
		LogSaveBean log = new LogSaveBean();
		log.setSystemID(SysConstants.EAPP_SUBSYSTEM_ID);
		log.setModuleKey(moduleKey);
		log.setActionKey(action);
		//log.setIpAddress();
		RPCSession rpcSession = RPCSessionContainer.singleton().getSession(sessionID);
		RPCPrincipal principal = (RPCPrincipal)rpcSession.getAttribute(SysConstants.RPCSESSION_USER_KEY);
		log.setAccountID(principal.getAccountID());
		log.setObjectID(objectID);
		log.setObject(object);
		log.setResultStatus(result ? ActionLog.RESULT_SUCCEED : ActionLog.RESULT_FAIL);
		return log;
	}
	
	/**
	 * 记录接口日志
	 * @param request HttpServletRequest
	 * @param objectID 对象ID
	 * @param object 操作对象
	 * @param result 结果状态
	 */
	public static void interfaceLog(String sessionID, String moduleKey, String action, String objectID, String object, boolean result) {
		try {
			actionLogBiz.addLog(getLogSaveBean(sessionID, moduleKey, action, objectID, object, result), true);
		} catch(Exception e) {
			log.error("写入日志失败", e);
		}
	}
	
	/**
	 * 记录接口日志
	 * @param request
	 * @param objectID
	 * @param object
	 */
	public static void interfaceLog(String sessionID, String moduleKey, String action, String objectID, String object) {
		interfaceLog(sessionID, moduleKey, action, objectID, object, true);
	}
	
	/**
	 * 记录系统日志
	 * @param request HttpServletRequest
	 * @param objectID 对象ID
	 * @param object 操作对象
	 * @param result 结果状态
	 */
	public static void actionLog(String sessionID, String moduleKey, String action, String objectID, String object, boolean result) {
		try {
			LogSaveBean log = getLogSaveBean(sessionID, moduleKey, action, objectID, object, result);
			log.setAccountID(log.getAccountID() + "（接口账号）");
			actionLogBiz.addLog(log, false);
		} catch(Exception e) {
			log.error("写入日志失败", e);
		}
	}
	
	/**
	 * 记录系统日志
	 * @param request
	 * @param objectID
	 * @param object
	 */
	public static void actionLog(String sessionID, String moduleKey, String action, String objectID, String object) {
		actionLog(sessionID, moduleKey, action, objectID, object, true);
	}
}
