/**
 * 
 */
package org.eapp.rpc.session;

import org.eapp.exception.RpcAuthorizationException;
import org.eapp.sys.config.SysConstants;
import org.eapp.util.spring.SpringHelper;

/**
 * 
 * @author zsy
 * @version
 */
public class RPCAuthorizationFilter {
	/**
	 * 对RPC远程方法调用进行鉴权
	 * @param sessionID 由服务器端分配的，由远程客户端持有的会话ID
	 * @param subSystemID 子系统编号
	 * @param moduleKey 子系统内部模块名
	 * @param actionKey 模块的操作名
	 */
	public static void authorize(String sessionID , String subSystemID , String moduleKey , String actionKey)
				throws RpcAuthorizationException {
		if (sessionID == null || subSystemID == null || moduleKey == null || actionKey == null) {
			throw new IllegalArgumentException();
		}
		/*
		 * 1.根据sessionID获取账号登陆session
		 * 未登录、session过期，则要求重登录 —— session无效异常
		 */
		RPCSession rpcSession = RPCSessionContainer.singleton().getSession(sessionID);
		if (rpcSession == null ) {
			throw new RpcAuthorizationException(RpcAuthorizationException.CODE_OVERTIME, "RPCSession超时");
		}
		//2.从Session中取得帐号
		RPCPrincipal principal = (RPCPrincipal)rpcSession.getAttribute(SysConstants.RPCSESSION_USER_KEY);
		if (principal == null) {
			throw new RpcAuthorizationException(RpcAuthorizationException.CODE_OVERTIME,"RPCSession超时");
		}
		
//		if (principal.isVaild()) {//用户有效
		//失效时也要重载，这样帐号启用后可以自动生效
		ISessionAccountInfoManage sessionAccountInfoManage = 
			(ISessionAccountInfoManage) SpringHelper.getBean("sessionAccountInfoBiz");
		//重载用户缓存信息
		principal = sessionAccountInfoManage.csReloadRPCPrincipal(principal, subSystemID, moduleKey);
//		} 
//		//同步后如查用户失效
//		if(!principal.isVaild()) {
//			principal.clear();
//			throw new RPCAuthorizationException(RPCAuthorizationException.CODE_ACCOUNTINVALID,"接口帐号锁定或已过期：" 
//		+ principal.getAccountID());
//		}
		if(!principal.hasRight(subSystemID, moduleKey, actionKey)) {//没权限
			throw new RpcAuthorizationException(RpcAuthorizationException.CODE_NORIGHT, "接口帐号没有操作权限：" 
					+ principal.getAccountID());
		}
	}
}
