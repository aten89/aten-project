/**
 * 
 */
package org.eapp.client.hessian;

import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.util.SystemProperties;
import org.eapp.comobj.ModuleMenuTree;
import org.eapp.comobj.SessionAccount;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.ISessionAccountService;

/**
 * @author zsy
 *
 */
public class SessionAccountService extends BaseEAPPService {
	
	private static Log log = LogFactory.getLog(SessionAccountService.class);
	private static ISessionAccountService service;
	
	private ISessionAccountService getService() throws MalformedURLException{
		if (service == null) {
			synchronized (SessionAccountService.class) {
				service = (ISessionAccountService) factory.create(ISessionAccountService.class, getServiceUrl(SystemProperties.SERVICE_SESSION_ACCOUNT));
			}
		}
		return service;
	}

	/**
	 * 通过用户ID取得绑定要session中的对象
	 * @param accountID
	 * @return
	 * @throws MalformedURLException
	 * @throws RpcAuthorizationException 
	 */
	public SessionAccount getSessionAccount(String accountID, String ipAddr) throws MalformedURLException, RpcAuthorizationException {
		try {
			return getService().getSessionAccount(getDefaultSessionID(false), accountID, ipAddr);
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				return getService().getSessionAccount(getDefaultSessionID(true), accountID, ipAddr);
			} else {
				throw e;
			}
		}
			
	}

	/**
	 * 更新session中的用户信息，如果用户需要更新，重新加载用户信息，及模块对应的动作Map
	 * @param user
	 * @param systemId
	 * @param moduleKey
	 * @return
	 * @throws MalformedURLException
	 * @throws RpcAuthorizationException 
	 */
	public SessionAccount reloadSessionAccount(SessionAccount user, 
			String systemID, String moduleKey) throws MalformedURLException, RpcAuthorizationException {
		try {
			return getService().reloadSessionAccount(getDefaultSessionID(false), user, systemID, moduleKey);
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				return getService().reloadSessionAccount(getDefaultSessionID(true), user, systemID, moduleKey);
			} else {
				throw e;
			}
		}
		
	}
	
	/**
	 * 加载用户session中的动作信息
	 * @param systemId
	 * @param moduleKey
	 * @param roleIDs
	 * @return
	 * @throws MalformedURLException 
	 * @throws RpcAuthorizationException 
	 */
	public String[] getActionRightStr(String systemID, String moduleKey, List<String> roleIDs) throws MalformedURLException, RpcAuthorizationException {
		try {
			return getService().getActionRightStr(getDefaultSessionID(false), systemID, moduleKey, roleIDs);
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				return getService().getActionRightStr(getDefaultSessionID(true), systemID, moduleKey, roleIDs);
			} else {
				throw e;
			}
		}
		
	}
	
	/**
	 * 返回用户有权限的菜单
	 * @param accountID
	 * @param systemId
	 * @return
	 * @throws MalformedURLException 
	 * @throws RpcAuthorizationException 
	 */
	public ModuleMenuTree getModuleMenuTree(String systemID, List<String> roleIDs) throws MalformedURLException, RpcAuthorizationException {
		try{
			return getService().getModuleMenuTree(getDefaultSessionID(false), systemID, roleIDs);
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				return getService().getModuleMenuTree(getDefaultSessionID(true), systemID, roleIDs);
			} else {
				throw e;
			}
		}

	}
}
