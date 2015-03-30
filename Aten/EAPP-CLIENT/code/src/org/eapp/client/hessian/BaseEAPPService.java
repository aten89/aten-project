/**
 * 
 */
package org.eapp.client.hessian;

import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.util.SystemProperties;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.IActorLoginService;

import com.caucho.hessian.client.HessianProxyFactory;

/**
 * @author zsy
 * 
 */
public class BaseEAPPService {
    private static String sessionID = null;
    private static Log log = LogFactory.getLog(BaseEAPPService.class);
    protected HessianProxyFactory factory = new HessianProxyFactory();
    
    private static IActorLoginService service;
    
    protected String getServiceUrl(String servicePath) {
        StringBuffer url = new StringBuffer();
        url.append(SystemProperties.SERVICE_URL);
        url.append(servicePath);
        return url.toString();
    }

    private IActorLoginService getService() throws MalformedURLException {
        if (service == null) {
            synchronized (BaseEAPPService.class) {
                service = (IActorLoginService) factory.create(IActorLoginService.class, getServiceUrl(SystemProperties.SERVICE_ACTOR_LOGIN));
            }
        }
        return service;
    }

    /**
     * 获得会话ID
     * 
     * @param newSession 是否新建Session，如果为是强制重新登录一下
     * @return
     * @throws MalformedURLException 
     * @throws RpcAuthorizationException 
     */
    public String getDefaultSessionID(boolean newSession) throws RpcAuthorizationException, MalformedURLException {
        if (!newSession && sessionID != null) {
            // 不新建Session且原SessionID不为空，直接返回
            return sessionID;
        }
        synchronized (BaseEAPPService.class) {
            if (newSession || sessionID == null) {
                sessionID = getService().login(SystemProperties.SERVICE_USERNAME, SystemProperties.SERVICE_CREDENCE);
                log.info("用户登陆获取SESSION:" + sessionID);
            }
        }
        return sessionID;
    }
    
    /**
     * 登入
     * @throws MalformedURLException 异常
     * @throws RPCAuthorizationException 异常

     */
    public void login() throws MalformedURLException, RpcAuthorizationException {
        sessionID = null;
        synchronized (BaseEAPPService.class) {
            sessionID = getService().login(SystemProperties.SERVICE_USERNAME, SystemProperties.SERVICE_CREDENCE);
        }
        log.info("用户登录获取SESSION:" + sessionID);
    }
    
    /**
     * 登出
     * @throws MalformedURLException
     */
    public void logout() throws MalformedURLException {
        getService().logout(SystemProperties.SERVICE_USERNAME, sessionID);
        log.info("用户登出销毁SESSION:" + sessionID);
    }
    
    /**
     * 验证RPC服务是否有权限，无异常抛出表示验证通过
	 * （子系统要开放服务接口时，可能通过此方法验证）
     * @param subSystemID
     * @param moduleKey
     * @param actionKey
     * @throws RpcAuthorizationException
     * @throws MalformedURLException
     */
    public void authorize(String subSystemID , String moduleKey , String actionKey)
			throws RpcAuthorizationException, MalformedURLException {
		try {
			getService().authorize(getDefaultSessionID(false), subSystemID, moduleKey, actionKey);
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				getService().authorize(getDefaultSessionID(true), subSystemID, moduleKey, actionKey);
			} else {
				throw e;
			}
		}
    }
}
