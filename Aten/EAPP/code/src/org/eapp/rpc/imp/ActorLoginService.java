/**
 * 
 */
package org.eapp.rpc.imp;

import java.util.Hashtable;

import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.IActorLoginService;
import org.eapp.rpc.session.ISessionAccountInfoManage;
import org.eapp.rpc.session.RPCAuthorizationFilter;
import org.eapp.rpc.session.RPCPrincipal;
import org.eapp.rpc.session.RPCSession;
import org.eapp.rpc.session.RPCSessionContainer;
import org.eapp.sys.config.SysConstants;

import com.caucho.hessian.server.HessianServlet;

/**
 * @author 林良益 2008-06-21
 * @modify zsy 2009-5-12
 * @version 1.0
 */
public class ActorLoginService extends HessianServlet implements IActorLoginService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5759006350951234907L;
	
	/*
	 * 接入帐号登记注册表 actorAccountID -> rpcSessionID
	 */
	private static Hashtable<String , String> loginRegistMap = new Hashtable<String , String>();
	/*
	 * ActorAcount 业务逻辑接口
	 */
	private ISessionAccountInfoManage sessionAccountInfoBiz;

	public void setSessionAccountInfoBiz(
			ISessionAccountInfoManage sessionAccountInfoBiz) {
		this.sessionAccountInfoBiz = sessionAccountInfoBiz;
	}

	@Override
	public String login(String actorAccountID, String credence) throws RpcAuthorizationException {
		if(actorAccountID == null){
			throw new IllegalArgumentException("actorAccountID 参数为空");
		}
		if(credence == null){
			throw new IllegalArgumentException("credence 参数为空");
		}
		synchronized(loginRegistMap) {
			String rpcSessionID = loginRegistMap.get(actorAccountID);
			if(rpcSessionID != null){
				RPCSession rpcSession = RPCSessionContainer.singleton().getSession(rpcSessionID);
				if (rpcSession != null) {
					//session不为空，说明未失效
					RPCPrincipal principal = (RPCPrincipal)rpcSession.getAttribute(SysConstants.RPCSESSION_USER_KEY);
					if (principal != null) {//绑定session的对像不为空
						return rpcSession.getID();
				//		throw new RPCAuthorizationException(RPCAuthorizationException.CODE_REPEATLOGIN, "接口帐号不能重复登录：" + actorAccountID);
					}
					//使session失效，从容器中自动移除Session
					rpcSession.invalidate();
				}
				//移除注册表中的就的rpcSessionID
				loginRegistMap.remove(actorAccountID);
			}
	
			RPCPrincipal principal = sessionAccountInfoBiz.getLoginRPCPrincipal(actorAccountID, credence);

			//注册RPCPrincipal到RPCSession
			RPCSession rpcSession = RPCSessionContainer.singleton().createSession();
			loginRegistMap.put(actorAccountID, rpcSession.getID());
			rpcSession.setAttribute(SysConstants.RPCSESSION_USER_KEY, principal);
			return rpcSession.getID();
		}
	}
	
	@Override
	public void logout(String actorAccountID, String rpcSessionID) {
		if(actorAccountID == null){
			throw new IllegalArgumentException("actorAccountID 参数为空");
		}
		if(rpcSessionID == null){
			throw new IllegalArgumentException("rpcSessionID 参数为空");
		}
		/*
		 * 如果帐号没有登录，直接返回
		 */
		String regitedRPCSessionID = loginRegistMap.get(actorAccountID);
		if(regitedRPCSessionID == null){
			return;
		}
		if(rpcSessionID.equals(regitedRPCSessionID)) {
			//移除注册表中的接入帐号ID
			loginRegistMap.remove(actorAccountID);
			RPCSession rpcSession = RPCSessionContainer.singleton().getSession(rpcSessionID);
			if (rpcSession != null) {
				rpcSession.invalidate();//让RPCSession失效
			}
		}
	}
	
	@Override
	public void authorize(String sessionID , String subSystemID , String moduleKey , String actionKey)
			throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID, subSystemID, moduleKey, actionKey);
	}
}
