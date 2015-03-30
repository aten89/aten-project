/**
 * 
 */
package org.eapp.client.hessian;

import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.util.SystemProperties;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.ILogService;
import org.eapp.rpc.dto.LogSaveInfo;

/**
 * @author jasonwong
 *
 */
public class LogService extends BaseEAPPService{
	private static Log log = LogFactory.getLog(LogService.class);
	
	private static ILogService service;
	
	private ILogService getService() throws MalformedURLException{
		if (service == null) {
			synchronized(LogService.class) {
				service = (ILogService) factory.create(ILogService.class, getServiceUrl(SystemProperties.SERVICE_LOG));
			}
		}
		return service;
	}
	
	/**
	 * 写入接口日志
	 * @param log
	 * @throws RPCLoginException
	 * @throws MalformedURLException
	 * @throws RPCAuthorizationException
	 */
	public void addInterfaceLog(LogSaveInfo logg) throws MalformedURLException, RpcAuthorizationException {
		try{
			getService().addInterfaceLog(getDefaultSessionID(false), logg);
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				getService().addInterfaceLog(getDefaultSessionID(true), logg);
			} else {
				throw e;
			}
		}
	}

	/**
	 * 写入系统日志
	 * @param log
	 * @throws MalformedURLException
	 * @throws RpcAuthorizationException
	 * @throws RPCLoginException
	 */
	public void addActionLog(LogSaveInfo logg) throws MalformedURLException, RpcAuthorizationException {
		try {
			getService().addActionLog(getDefaultSessionID(false), logg);
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				getService().addActionLog(getDefaultSessionID(true), logg);
			} else {
				throw e;
			}
		}

	}

}
