/**
 * 
 */
package org.eapp.rpc;

import java.util.Date;
import java.util.List;

import org.eapp.exception.EappException;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.dto.GroupInfo;
import org.eapp.rpc.dto.PostInfo;
import org.eapp.rpc.dto.UserAccountInfo;


/**
 * @author zsy
 * @version
 */
public interface IUserAccountService {
	/**
     * 通过用户帐号查找
     * 
     * @param sessionID
     * @param accountID
     * @return
     * @throws RpcAuthorizationException
     */
    public UserAccountInfo getUserAccount(String sessionID, String accountID) throws RpcAuthorizationException;
    
    /**
     * 根据部门Id查找该部门及子部门的用户
     * 
     * @param sessionID
     * @param groupName
     * @return
     * @throws RpcAuthorizationException
     */
    public List<UserAccountInfo> getUserAccountsByGroup(String sessionID, String groupName) throws RpcAuthorizationException;


    /**
	 * 获取指定的职位绑定的用户
	 * @param sessionID
	 * @param postName
	 * @return
	 * @throws RpcAuthorizationException
	 */
	public List<UserAccountInfo> getUserAccountsByPost(String sessionID, String postName) throws RpcAuthorizationException;
	
	/**
	 * 获取指定的角色绑定的用户
	 * @param sessionID
	 * @param roleName
	 * @return
	 * @throws RpcAuthorizationException
	 */
	public List<UserAccountInfo> getUserAccountsByRole(String sessionID, String roleName) throws RpcAuthorizationException;
	
	/**
	 * 查询用户，条件为空时查询所有
	 * @param sessionID
	 * @param groupName
	 * @param keyword
	 * @return
	 * @throws RpcAuthorizationException
	 */
	public List<UserAccountInfo> queryUserAccounts(String sessionID, String groupName, String keyword) throws RpcAuthorizationException;
    
	/**
     * 新增用户
     * 
     * @param account 用户信息
     * @throws EappException
     */
    public void addUserAccount(String sessionID, String accountID, String displayName, boolean isLock, 
    		Date invalidDate, String description) throws EappException, RpcAuthorizationException;

    /**
     * 修改用户
     * 
     * @param account 用户信息
     * @throws EappException
     */
    public void modifyUserAccount(String sessionID, String accountID, String displayName, boolean isLock, 
    		Date invalidDate, String description) throws EappException, RpcAuthorizationException;

    /**
     * 删除用户
     * 
     * @param accountIDs 帐号ID
     * @throws EappException 删除超级管理员是抛出异常
     */
    public void deleteUserAccounts(String sessionID, String[] accountIDs) throws EappException, RpcAuthorizationException;

	/**
	 * 发送系统消息
	 * @param sessionID
	 * @param systemID 系统ID
	 * @param sender 发送者
	 * @param toAccountID 接收帐号ID
	 * @param msg 信息内容
	 * @throws RpcAuthorizationException
	 */
	public void sendSysMsg(String sessionID, String systemID, String sender, String toAccountID, String msg) 
			throws RpcAuthorizationException;
	
    /**
     * 获取用户绑定的群组
     * 
     * @param sessionID
     * @param accountID
     * @return
     * @throws EappException
     * @throws RpcAuthorizationException
     */
    public List<GroupInfo> getBindedGroups(String sessionID, String accountID) throws RpcAuthorizationException;

   
    /**
     * 获取用户管辖的部门，包括下属部门
     * 
     * @param sessionID
     * @param accountID
     * @return
     * @throws RpcAuthorizationException
     */
    public List<GroupInfo> getManageGroups(String sessionID, String accountID) throws RpcAuthorizationException;
    
    /**
     * 获取用户绑定的职位
     * 
     * @param sessionID
     * @param accountID
     * @return
     * @throws EappException
     * @throws RpcAuthorizationException
     */
    public List<PostInfo> getBindedPosts(String sessionID, String accountID) throws RpcAuthorizationException;

    /**
     * 获取用户绑定群组的管理者职位
     * 
     * @param sessionID
     * @param postID
     * @return
     * @throws RpcAuthorizationException
     */
    public List<PostInfo> getManagerPosts(String sessionID, String accountID) throws RpcAuthorizationException;
    
    /**
     * 获取用户绑定的角色
     * @param sessionID
     * @param accountID
     * @return
     * @throws RpcAuthorizationException
     */
    public List<String> getBindedRoles(String sessionID, String accountID) throws RpcAuthorizationException;

}
