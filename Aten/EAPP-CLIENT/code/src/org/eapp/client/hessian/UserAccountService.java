/**
 * 
 */
package org.eapp.client.hessian;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.util.SystemProperties;
import org.eapp.exception.EappException;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.IUserAccountService;
import org.eapp.rpc.dto.GroupInfo;
import org.eapp.rpc.dto.PostInfo;
import org.eapp.rpc.dto.UserAccountInfo;


/**
 * @author jasonwong
 * 
 */
public class UserAccountService extends BaseEAPPService {

    private static Log log = LogFactory.getLog(UserAccountService.class);

    private static IUserAccountService service;

    private IUserAccountService getService() throws MalformedURLException {
        if (service == null) {
            synchronized (UserAccountService.class) {
                service = (IUserAccountService) factory.create(IUserAccountService.class, getServiceUrl(SystemProperties.SERVICE_USER_ACCOUNT));
            }
        }
        return service;
    }
    
    /**
     * 通过用户帐号查找
     * 
     * @param sessionID
     * @param accountID
     * @return
     * @throws RpcAuthorizationException
     * @throws MalformedURLException
     */
    public UserAccountInfo getUserAccount(String accountID) throws RpcAuthorizationException, MalformedURLException {
        try {
            return getService().getUserAccount(getDefaultSessionID(false), accountID);
        } catch (RpcAuthorizationException e) {
            if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
                log.warn("帐号登陆超时,重新登录");
                return getService().getUserAccount(getDefaultSessionID(true), accountID);
            } else {
                throw e;
            }
        }
    }
    
    /**
     * 根据部门Id查找该部门及子部门的用户，
     * 如果部门Id为空查找所有
     * 
     * @param groupName
     * @return
     * @throws RpcAuthorizationException
     * @throws MalformedURLException
     */
    public List<UserAccountInfo> getUserAccountsByGroup(String groupName) throws RpcAuthorizationException,
		    MalformedURLException {
		try {
		    return getService().getUserAccountsByGroup(getDefaultSessionID(false), groupName);
		} catch (RpcAuthorizationException e) {
		    if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
		        log.warn("帐号登陆超时,重新登录");
		        return getService().getUserAccountsByGroup(getDefaultSessionID(true), groupName);
		    } else {
		        throw e;
		    }
		}
	}
    
    /**
     * 获取指定的职位绑定的用户
     * 
     * @param groupName
     * @return
     * @throws RpcAuthorizationException
     * @throws MalformedURLException
     */
    public List<UserAccountInfo> getUserAccountsByPost(String postName) throws RpcAuthorizationException,
		    MalformedURLException {
		try {
		    return getService().getUserAccountsByPost(getDefaultSessionID(false), postName);
		} catch (RpcAuthorizationException e) {
		    if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
		        log.warn("帐号登陆超时,重新登录");
		        return getService().getUserAccountsByPost(getDefaultSessionID(true), postName);
		    } else {
		        throw e;
		    }
		}
	}
    
    /**
     * 获取指定的角色绑定的用户
     * 
     * @param roleName
     * @return
     * @throws RpcAuthorizationException
     * @throws MalformedURLException
     */
    public List<UserAccountInfo> getUserAccountsByRole(String roleName) throws RpcAuthorizationException,
		    MalformedURLException {
		try {
		    return getService().getUserAccountsByRole(getDefaultSessionID(false), roleName);
		} catch (RpcAuthorizationException e) {
		    if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
		        log.warn("帐号登陆超时,重新登录");
		        return getService().getUserAccountsByRole(getDefaultSessionID(true), roleName);
		    } else {
		        throw e;
		    }
		}
	}
 
    /**
	 * 查询用户，条件为空时查询所有
	 * @param sessionID
	 * @param groupName
	 * @param keyword
	 * @return
	 * @throws RpcAuthorizationException
	 */
	public List<UserAccountInfo> queryUserAccounts(String groupName, String keyword) throws RpcAuthorizationException,
			MalformedURLException {
		try {
		    return getService().queryUserAccounts(getDefaultSessionID(false), groupName, keyword);
		} catch (RpcAuthorizationException e) {
		    if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
		        log.warn("帐号登陆超时,重新登录");
		        return getService().queryUserAccounts(getDefaultSessionID(true), groupName, keyword);
		    } else {
		        throw e;
		    }
		}
	}

    /**
     * 新增用户
     * 
     * @param account 用户信息
     * @throws EappException
     * @throws MalformedURLException
     */
    public void addUserAccount(String accountID, String displayName, boolean isLock, 
    		Date invalidDate, String description) throws EappException, MalformedURLException {
        try {
            getService().addUserAccount(getDefaultSessionID(false), accountID, displayName, isLock, 
            		invalidDate, description);
        } catch (RpcAuthorizationException e) {
            if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
                log.warn("帐号登陆超时,重新登录");
                getService().addUserAccount(getDefaultSessionID(false), accountID, displayName, isLock, 
                		invalidDate, description);
            } else {
                throw e;
            }
        }
    }
    
    /**
     * 修改用户
     * 
     * @param account 用户信息
     * @throws EappException
     * @throws MalformedURLException
     */
    public void modifyUserAccount(String accountID, String displayName, boolean isLock, 
    		Date invalidDate, String description) throws EappException, MalformedURLException {
        try {
            getService().modifyUserAccount(getDefaultSessionID(false), accountID, displayName, isLock, 
            		invalidDate, description);
        } catch (RpcAuthorizationException e) {
            if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
                log.warn("帐号登陆超时,重新登录");
                getService().modifyUserAccount(getDefaultSessionID(true), accountID, displayName, isLock, 
                		invalidDate, description);
            } else {
                throw e;
            }
        }
    }

    /**
     * 删除用户
     * 
     * @param accountIDs 帐号ID
     * @throws EappException 删除超级管理员是抛出异常
     * @throws MalformedURLException
     */
    public void deleteUserAccounts(String[] accountIDs) throws EappException, MalformedURLException {
        try {
            getService().deleteUserAccounts(getDefaultSessionID(false), accountIDs);
        } catch (RpcAuthorizationException e) {
            if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
                log.warn("帐号登陆超时,重新登录");
                getService().deleteUserAccounts(getDefaultSessionID(true), accountIDs);
            } else {
                throw e;
            }
        }
    }
    
    /**
	 * 发送系统消息
	 * @param sessionID
	 * @param systemID 系统ID
	 * @param sender 发送者
	 * @param toAccountID 接收帐号ID
	 * @param msg 信息内容
	 * @throws RpcAuthorizationException
	 */
	public void sendSysMsg(String systemID, String sender, String toAccountID, String msg) 
			throws RpcAuthorizationException, MalformedURLException {
        try {
            getService().sendSysMsg(getDefaultSessionID(false), systemID, sender, toAccountID, msg);
        } catch (RpcAuthorizationException e) {
            if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
                log.warn("帐号登陆超时,重新登录");
                getService().sendSysMsg(getDefaultSessionID(false), systemID, sender, toAccountID, msg);
            } else {
                throw e;
            }
        }
    }

    /**
     * 根据帐号ID获取绑定的群组
     * 
     * @param sessionID
     * @param accountID
     * @return
     * @throws RpcAuthorizationException
     * @throws MalformedURLException
     */
    public List<GroupInfo> getBindedGroups(String accountID) throws RpcAuthorizationException,
            MalformedURLException {

        try {
            return getService().getBindedGroups(getDefaultSessionID(false), accountID);
        } catch (RpcAuthorizationException e) {
            if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
                log.warn("帐号登陆超时,重新登录");
                return getService().getBindedGroups(getDefaultSessionID(true), accountID);
            } else {
                throw e;
            }
        }
    }
    
    /**
     * 获取用户管理的部门和下属部门
     * 
     * @param accountID
     * @return
     * @throws RpcAuthorizationException
     * @throws MalformedURLException
     */
    public List<GroupInfo> getManageGroups(String accountID) throws RpcAuthorizationException,
            MalformedURLException {

        try {
            return getService().getManageGroups(getDefaultSessionID(false), accountID);
        } catch (RpcAuthorizationException e) {
            if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
                log.warn("帐号登陆超时,重新登录");
                return getService().getManageGroups(getDefaultSessionID(true), accountID);
            } else {
                throw e;
            }
        }

    }

    /**
     * 获取用户绑定的职位
     * 
     * @param sessionID
     * @param postID
     * @return
     * @throws MalformedURLException
     * @throws RpcAuthorizationException 
     */
    public List<PostInfo> getBindedPosts(String accountID) throws MalformedURLException, RpcAuthorizationException {
        try {
            return getService().getBindedPosts(getDefaultSessionID(false), accountID);
        } catch (RpcAuthorizationException e) {
            if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
                log.warn("帐号登陆超时,重新登录");
                return getService().getBindedPosts(getDefaultSessionID(true), accountID);
            } else {
                throw e;
            }
        }
    }

    /**
     * 获取用户绑定群组的管理者职位
     * 
     * @param sessionID
     * @param postID
     * @return
     * @throws MalformedURLException
     * @throws RpcAuthorizationException 
     */
    public List<PostInfo> getManagerPosts(String accountID) throws MalformedURLException, RpcAuthorizationException {
        try {
            return getService().getManagerPosts(getDefaultSessionID(false), accountID);
        } catch (RpcAuthorizationException e) {
            if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
                log.warn("帐号登陆超时,重新登录");
                return getService().getManagerPosts(getDefaultSessionID(true), accountID);
            } else {
                throw e;
            }
        }
    }
    
    /**
     * 获取用户绑定的角色
     * 
     * @param sessionID
     * @param postID
     * @return
     * @throws MalformedURLException
     * @throws RpcAuthorizationException 
     */
    public List<String> getBindedRoles(String accountID) throws MalformedURLException, RpcAuthorizationException {
        try {
            return getService().getBindedRoles(getDefaultSessionID(false), accountID);
        } catch (RpcAuthorizationException e) {
            if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
                log.warn("帐号登陆超时,重新登录");
                return getService().getBindedRoles(getDefaultSessionID(true), accountID);
            } else {
                throw e;
            }
        }
    }

}
