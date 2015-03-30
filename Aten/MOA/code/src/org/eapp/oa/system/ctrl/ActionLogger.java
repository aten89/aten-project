/**
 * 
 */
package org.eapp.oa.system.ctrl;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.LogService;
import org.eapp.oa.system.cas.filter.CASFilter;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.rpc.dto.LogSaveInfo;
import org.eapp.rpc.dto.SubSystemInfo;

import org.eapp.comobj.SessionAccount;

/**
 * @author zsy
 * @version 
 */
public class ActionLogger {
	private static final Log log = LogFactory.getLog(ActionLogger.class);
	
	public static final String RESULT_SUCCEED = "Y";
	public static final String RESULT_FAIL = "N";
	
	/**
	 * 记录系统日志
	 * @param request HttpServletRequest
	 * @param objectID 对象ID
	 * @param object 操作对象
	 * @param result 结果状态
	 */
	public static void log(HttpServletRequest request, String objectID, String object, boolean result) {
		try {
			LogSaveInfo log = new LogSaveInfo();
			SubSystemInfo sys = SysCodeDictLoader.getInstance().getCurrentSysConfig();
			log.setSystemID(sys.getSubSystemID());
			log.setModuleKey(new CASFilter().getModuleKey(request));
			log.setActionKey(request.getParameter("act"));
			SessionAccount user = (SessionAccount)request.getSession()
					.getAttribute(SysConstants.SESSION_USER_KEY);
			log.setAccountID(user.getAccountID());
			log.setObjectID(objectID);
			log.setObject(object);
			log.setIpAddress(request.getRemoteAddr());
			log.setResultStatus(result ? RESULT_SUCCEED : RESULT_FAIL);
			
			LogService logs = new LogService();
			logs.addActionLog(log);
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
	public static void log(HttpServletRequest request, String objectID, String object) {
		log(request, objectID, object, true);
	}
}
