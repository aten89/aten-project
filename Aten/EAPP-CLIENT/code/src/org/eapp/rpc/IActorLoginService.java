/**
 * 
 */
package org.eapp.rpc;

import org.eapp.exception.RpcAuthorizationException;

/**
 * @author 林良益 2008-06-12
 * @version 1.0
 */
public interface IActorLoginService {
	/**
	 * PRC用户登录
	 * 通过Actor帐号登录，同一ActorAccount只能登录一次
	 * @param actorAccountID 帐号ID
	 * @param credence 帐号凭证
	 * @return rpcSessionID 登录成功后分配一个RPCSession
	 * @throws RPCLoginException 登录异常
	 */
	public String login(String actorAccountID, String credence) throws RpcAuthorizationException;
	/**
	 * RPC用户登出
	 * 根据接入帐号的ID及分配的RPCSessionID注销登陆
	 * @param actorAccountID
	 * @param rpcSessionID	
	 */
	public void logout(String actorAccountID,String rpcSessionID);
	
	/**
	 * 验证RPC服务是否有权限，无异常抛出表示验证通过
	 * （子系统要开放服务接口时，可能通过此方法验证）
	 * @param sessionID
	 * @param subSystemID
	 * @param moduleKey
	 * @param actionKey
	 * @throws RpcAuthorizationException
	 */
	public void authorize(String sessionID , String subSystemID , String moduleKey , String actionKey)
			throws RpcAuthorizationException;
}
