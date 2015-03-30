/**
 * 
 */
package org.eapp.rpc;


import java.util.List;

import org.eapp.comobj.ModuleMenuTree;
import org.eapp.comobj.SessionAccount;
import org.eapp.exception.RpcAuthorizationException;


/**
 * @author zsy
 * @version 
 */
public interface ISessionAccountService {

	/**
	  * 通过用户ID取得绑定要session中的对象
	  * @param accountID 用户帐号
	  * @return
	  */
	public SessionAccount getSessionAccount(String sessionID, String accountID, String ipAddr) throws RpcAuthorizationException;

	/**
	 * 更新session中的用户信息，如果用户需要更新，重新加载用户信息，及模块对应的动作Map
	 * @param account session中的用户信息
	 * @param systemID 系统ID
	 * @param moduleKey 模块ID
	 * @return
	 */
	public SessionAccount reloadSessionAccount(
			String sessionID, SessionAccount account, String systemID, String moduleKey) throws RpcAuthorizationException;
	
	/**
	 * 加载用户session中的动作信息
	 * 返回 	数组0：该模块所有动作，
	 * 		数组1：给定角色在该模块有权限的动作;
	 * 		动作间用","分开
	 * 格式	{",add,modify,delete,query,", ",add,query,"}
	 * @param systemID 系统ID
	 * @param moduleKey 模块Key
	 * @param roleIDs
	 * @return 
	 */
	public String[] getActionRightStr(String sessionID, String systemID, String moduleKey, List<String> roleIDs) throws RpcAuthorizationException;
	
	/**
	 * 返回用户有权限的菜单
	 * @param systemID
	 * @param roleIDs
	 * @return
	 */
	public ModuleMenuTree getModuleMenuTree(String sessionID, String systemID, List<String> roleIDs) throws RpcAuthorizationException;
	
}
