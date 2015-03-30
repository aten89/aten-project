/**
 * 
 */
package org.eapp.client.hessian;

import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.util.SystemProperties;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.ISubSystemService;
import org.eapp.rpc.dto.SubSystemInfo;


/**
 * @author zsy
 *
 */
public class SubSystemService extends BaseEAPPService{
 	private static Log log = LogFactory.getLog(SubSystemService.class);
 	private static ISubSystemService service;
 	
	private ISubSystemService getService() throws MalformedURLException{
		if (service == null) {
			synchronized (SubSystemService.class) {
				service = (ISubSystemService) factory.create(ISubSystemService.class, getServiceUrl(SystemProperties.SERVICE_SUB_SYSTEM));
			}
		}
 		return service;
	}
	
	/**
	 * 取得EAPP系统配置信息
	 * @param subSystemID
	 * @return
	 * @throws RpcAuthorizationException
	 * @throws MalformedURLException
	 */
	public SubSystemInfo getEAPPSystemInfo() throws RpcAuthorizationException, MalformedURLException {
		try {
			return getService().getEAPPSystemInfo(getDefaultSessionID(false));
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				return getService().getEAPPSystemInfo(getDefaultSessionID(true));
			} else {
				throw e;
			}
		}
	}
	
	/**
	 * 取得子系统配置信息
	 * @param subSystemID
	 * @return
	 * @throws RpcAuthorizationException
	 * @throws MalformedURLException
	 */
	public SubSystemInfo getSubSystemInfo(String subSystemID) throws RpcAuthorizationException, MalformedURLException {
		try {
			return getService().getSubSystemInfo(getDefaultSessionID(false), subSystemID);
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				return getService().getSubSystemInfo(getDefaultSessionID(true), subSystemID);
			} else {
				throw e;
			}
		}
	}
}
