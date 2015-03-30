/**
 * 
 */
package org.eapp.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IActionLogBiz;
import org.eapp.cas.filter.CASFilter;
import org.eapp.comobj.SessionAccount;
import org.eapp.dto.LogSaveBean;
import org.eapp.hbean.ActionLog;
import org.eapp.sys.config.SysConstants;
import org.eapp.util.spring.SpringHelper;


/**
 * @author zsy
 * @version 
 */
public class ActionLogger {
	private static final Log log = LogFactory.getLog(ActionLogger.class);
	private static IActionLogBiz actionLogBiz = (IActionLogBiz)SpringHelper.getBean("actionLogBiz");
	/**
	 * 记录系统日志
	 * @param request HttpServletRequest
	 * @param objectID 对象ID
	 * @param object 操作对象
	 * @param result 结果状态
	 */
	public static void log(HttpServletRequest request, String objectID, String object, boolean result) {
		try {
			LogSaveBean log = new LogSaveBean();
			log.setSystemID(SysConstants.EAPP_SUBSYSTEM_ID);
			CASFilter casf = new CASFilter();
			log.setModuleKey(casf.getModuleKey(request));
			log.setActionKey(casf.getActionKey(request));
			SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
			log.setAccountID(user.getAccountID());
			log.setObjectID(objectID);
			log.setObject(object);
			log.setIpAddress(request.getRemoteAddr());
			log.setResultStatus(result ? ActionLog.RESULT_SUCCEED : ActionLog.RESULT_FAIL);
			
			
			actionLogBiz.addLog(log, false);
		} catch(Exception e) {
			e.printStackTrace();
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
