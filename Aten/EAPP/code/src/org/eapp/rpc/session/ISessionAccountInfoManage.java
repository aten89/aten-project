package org.eapp.rpc.session;

import java.util.List;

import org.eapp.comobj.SessionAccount;
import org.eapp.exception.EappException;
import org.eapp.exception.RpcAuthorizationException;


/**
 * 管理登录用户在session中的用户信息
 * @author zsy
 * @version
 */
public interface ISessionAccountInfoManage {

	/**
	  * 通过用户ID取得绑定要session中的对象
	  * @param accountID 用户帐号
	  * @return
	  */
	public SessionAccount csLoadSessionAccountInfo(String accountID, String ipAddr);

	/**
	  * 更新session中的用户信息，如果用户需要更新，重新加载用户信息，及模块对应的动作Map
	  * @param user session中的用户信息
	  * @param moduleID 模块ID
	  */
	public SessionAccount csReloadSessionAccountInfo(
			SessionAccount user, String moduleKey);
	
	/**
	 * 更新session中的用户信息，如果用户需要更新，重新加载用户信息，及模块对应的动作Map
	 * @param user
	 * @param systemId
	 * @param moduleKey
	 * @return
	 */
	public SessionAccount csReloadSessionAccountInfo(
			SessionAccount user, String systemId, String moduleKey);
	
	/**
	  * 通过帐号ID取得绑定要RPCSsession中的对象
	  * @param accountID 用户帐号
	  * @return
	  */
	public RPCPrincipal getLoginRPCPrincipal(String accountID, String credence) throws RpcAuthorizationException;

	/**
	 * 更新RPCSsession中的用户信息，如果用户需要更新，重新加载用户信息，及模块对应的动作Map
	 * @param user
	 * @param systemId
	 * @param moduleKey
	 * @return
	 */
	public RPCPrincipal csReloadRPCPrincipal(RPCPrincipal user, String systemId, String moduleKey) throws RpcAuthorizationException;
	
	/**
	 * 加载用户session中的动作信息
	 * @param systemId 系统ID
	 * @param moduleKey 模块Key
	 * @param roleIDs 角色ID列表
	 * @return 数组0：该模块所有动作，数组1：给定角色在该模块有权限的动作;
	 * 		动作间用","分开
	 */
	public String[] getActions(String systemId, String moduleKey, List<String> roleIDs);

	/**
	 * 加载用户session中的动作信息
	 * @param moduleKey
	 * @param roleIDs
	 * @return
	 */
	public String[] getActions(String moduleKey, List<String> roleIDs);
	
	/**
	 * 验证用户名与密码
	 * @param username
	 * @param password
	 * @return
	 * @throws EappException
	 */
	public boolean csAuthenticateAccount(String username, String password) throws EappException;
}