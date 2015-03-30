/**
 * 
 */
package org.eapp.rpc.imp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eapp.blo.IGroupBiz;
import org.eapp.blo.IPostBiz;
import org.eapp.blo.IRoleBiz;
import org.eapp.blo.ISysMsgBiz;
import org.eapp.blo.IUserAccountBiz;
import org.eapp.dao.param.UserAccountQueryParameters;
import org.eapp.dto.AccountSaveBean;
import org.eapp.exception.EappException;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.hbean.Post;
import org.eapp.hbean.Group;
import org.eapp.hbean.Role;
import org.eapp.hbean.UserAccount;
import org.eapp.rpc.IUserAccountService;
import org.eapp.rpc.dto.GroupInfo;
import org.eapp.rpc.dto.PostInfo;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.rpc.session.RPCAuthorizationFilter;
import org.eapp.sys.config.SysConstants;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;

import com.caucho.hessian.server.HessianServlet;

/**
 * @author zsy
 * @version
 */
public class UserAccountService extends HessianServlet implements IUserAccountService {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private static final String MODULE_KEY = "user_account";

    private IUserAccountBiz userAccountBiz;
    private IGroupBiz groupBiz;
    private IPostBiz postBiz;
    private IRoleBiz roleBiz;
    private ISysMsgBiz sysMsgBiz;
    

    public void setUserAccountBiz(IUserAccountBiz userAccountBiz) {
		this.userAccountBiz = userAccountBiz;
	}

	public void setPostBiz(IPostBiz postBiz) {
		this.postBiz = postBiz;
	}
	
	public void setGroupBiz(IGroupBiz groupBiz) {
		this.groupBiz = groupBiz;
	}

	public void setRoleBiz(IRoleBiz roleBiz) {
		this.roleBiz = roleBiz;
	}

	public void setSysMsgBiz(ISysMsgBiz sysMsgBiz) {
		this.sysMsgBiz = sysMsgBiz;
	}

	@Override
    public UserAccountInfo getUserAccount(String sessionID, String accountID) throws RpcAuthorizationException {
        RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.VIEW);
        UserAccount u = userAccountBiz.getUserByAccountID(accountID);
        if (u == null) {
            return null;
        }
        return copy(u);
    }
	
	@Override
	public List<UserAccountInfo> getUserAccountsByGroup(String sessionID, String groupName)
			throws RpcAuthorizationException {
        RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.QUERY);
        Group g = groupBiz.getGroupByName(groupName);
        if (g == null) {
        	return null;
        }
        List<UserAccount> uas = userAccountBiz.getCascadeAccountsByGroupId(g.getGroupID());
        if (uas == null) {
            return null;
        }
        List<UserAccountInfo> usDto = new ArrayList<UserAccountInfo>();
        for (UserAccount u : uas) {
            usDto.add(copy(u));
        }
        return usDto;
    }
	
	@Override
	public List<UserAccountInfo> queryUserAccounts(String sessionID, String groupName, String keyword)
			throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.QUERY);
		
        UserAccountQueryParameters uqp = new UserAccountQueryParameters();
        uqp.setPageNo(1);
        uqp.setPageSize(QueryParameters.ALL_PAGE_SIZE);
        if (StringUtils.isNotBlank(keyword)) {
        	uqp.setKeyword(keyword);
        }
        List<UserAccountInfo> usDto = new ArrayList<UserAccountInfo>();
        if (StringUtils.isNotBlank(groupName)) {
	        Group g = groupBiz.getGroupByName(groupName);
	        if (g != null) {
	        	uqp.setGroupID(g.getGroupID());
	        } else {
	        	return usDto;
	        }
        }
        ListPage<UserAccount> users = userAccountBiz.queryUserAccount(uqp);
        if (users != null && users.getDataList() != null) {
        	for (UserAccount u : users.getDataList()) {
        		usDto.add(copy(u));
        	}
        }
        return usDto;
	}
	
	@Override
	public List<UserAccountInfo> getUserAccountsByPost(String sessionID, String postName)
			throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID,MODULE_KEY, SysConstants.QUERY);
		if (postName == null) {
			return null;
		}
		//根据名称获取职位
		Post post = postBiz.getPostByName(postName);
		if (post == null) {
			return null;
		}
		//获取职位绑定的用户对像
		Set<UserAccount> userAccounts = post.getUserAccounts();
		if (userAccounts == null) {
			return null;
		}
		
		List<UserAccountInfo> userDTOs = new ArrayList<UserAccountInfo>();
		for(UserAccount u : userAccounts){
			userDTOs.add(copy(u));
		}
		return userDTOs;
	}
	
	@Override
	public List<UserAccountInfo> getUserAccountsByRole(String sessionID, String roleName) 
			throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID,MODULE_KEY, SysConstants.QUERY);
		if (roleName == null) {
			return null;
		}
		//根据名称获取职位
		Role role = roleBiz.getRoleByName(roleName);
		if (role == null) {
			return null;
		}
		//获取职位绑定的用户对像
		Set<UserAccount> userAccounts = role.getUserAccounts();
		if (userAccounts == null) {
			return null;
		}
		
		List<UserAccountInfo> userDTOs = new ArrayList<UserAccountInfo>();
		for(UserAccount u : userAccounts){
			userDTOs.add(copy(u));
		}
		return userDTOs;
	}
	
	@Override
	public void addUserAccount(String sessionID, String accountID, String displayName, boolean isLock, 
			Date invalidDate, String description) throws EappException, RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.ADD);
		RPCLogger.interfaceLog(sessionID, MODULE_KEY, SysConstants.ADD, null,
        		"方法:UserAccountService.addUserAccount 参数:sessionID="+sessionID+",account="+accountID);
		
        AccountSaveBean account = new AccountSaveBean();
        account.setAccountID(accountID);
        account.setDisplayName(displayName);
        account.setIsLock(isLock);
        account.setChangePasswordFlag(UserAccount.CHANGEPASSWORD_TRUE);
        account.setInvalidDate(invalidDate);
        account.setDescription(description);
        
        UserAccount user = userAccountBiz.addUser(account);
        if (user != null) {
            RPCLogger.actionLog(sessionID, MODULE_KEY, SysConstants.ADD, user.getUserID(), user.toString());
        }
    }

	@Override
    public void modifyUserAccount(String sessionID, String accountID, String displayName, boolean isLock, 
			Date invalidDate, String description) throws EappException, RpcAuthorizationException {
        RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.MODIFY);
        RPCLogger.interfaceLog(sessionID, MODULE_KEY, SysConstants.MODIFY, null,
        		"方法:UserAccountService.modifyUserAccount 参数:sessionID="+sessionID+",account="+accountID);
        
        AccountSaveBean account = new AccountSaveBean();
        account.setAccountID(accountID);
        account.setDisplayName(displayName);
        account.setIsLock(isLock);
        account.setInvalidDate(invalidDate);
        account.setDescription(description);
        
        UserAccount user = userAccountBiz.modifyUser(account);
        if (user != null) {
            RPCLogger.actionLog(sessionID, MODULE_KEY, SysConstants.MODIFY, user.getUserID(), user.toString());
        }
    }
	
	@Override
    public void deleteUserAccounts(String sessionID, String[] accountIDs) throws EappException,
            RpcAuthorizationException {
        if (accountIDs == null) {
            return;
        }
        RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.DELETE);
        RPCLogger.interfaceLog(sessionID, MODULE_KEY, SysConstants.DELETE, null,"方法:UserAccountService.deleteUserAccounts 参数:sessionID="
        		+ sessionID+",accountIDs="+Arrays.toString(accountIDs));
        List<UserAccount> users = userAccountBiz.deleteUsers(accountIDs);
        if (users != null) {
            for (UserAccount user : users) {
                RPCLogger.actionLog(sessionID, MODULE_KEY, SysConstants.DELETE, user.getUserID(), user.toString());
            }
        }
    }
	
	@Override
	public void sendSysMsg(String sessionID, String systemID, String sender, String toAccountID, String msg) throws
			RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.SEND);
        RPCLogger.interfaceLog(sessionID, MODULE_KEY, SysConstants.SEND, null,"方法:UserAccountService.sendSysMsg 参数:sessionID="
        		+ sessionID+",systemID="+systemID+",sender="+sender+",toAccountID="+toAccountID+",msg="+msg);
		sysMsgBiz.addSysMsg(systemID, sender, toAccountID, msg);
	}

	@Override
    public List<GroupInfo> getBindedGroups(String sessionID, String accountID) throws RpcAuthorizationException {
		if (accountID == null) {
            throw new IllegalArgumentException();
        }
        RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.QUERY);
        
        Set<Group> groups = userAccountBiz.getBindedGroups(accountID);
        if (groups == null || groups.isEmpty()) {
        	return null;
        }
        List<GroupInfo> bandingGroups = new ArrayList<GroupInfo>(groups.size());
        for (Group g : groups) {
            bandingGroups.add(GroupService.copy(g));
        }
        return bandingGroups;
    }
	
	@Override
    public List<GroupInfo> getManageGroups(String sessionID, String accountID) throws RpcAuthorizationException {
        if (accountID == null) {
            throw new IllegalArgumentException();
        }
        RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.QUERY);
        
        // 获取帐号绑定的职位列表
        Set<Post> posts = userAccountBiz.getBindedPosts(accountID);
        if (posts == null || posts.isEmpty()) {
        	return null;
        }
        // 使用set排除重复的组织机构
        Set<GroupInfo> tempSet = new HashSet<GroupInfo>();
        for (Post post : posts) {
            // 获取职位管辖的所有部门
            List<Group> groups = postBiz.getManagedGroups(post.getPostID());
            if (groups == null || groups.isEmpty()) {
            	continue;
            }
            for (Group g : groups) {
                tempSet.add(GroupService.copy(g));
            }
        }
        return new ArrayList<GroupInfo>(tempSet);
    }
	

	@Override
    public List<PostInfo> getBindedPosts(String sessionID, String accountID) throws RpcAuthorizationException {
		if (accountID == null) {
            throw new IllegalArgumentException();
        }
        RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.QUERY);
        
        Set<Post> posts = userAccountBiz.getBindedPosts(accountID);
        if (posts == null || posts.isEmpty()) {
        	return null;
        }
        List<PostInfo> bandingPosts = new ArrayList<PostInfo>(posts.size());
        for (Post p : posts) {
            bandingPosts.add(PostService.copy(p));
        }
        return bandingPosts;
    }

	@Override
    public List<PostInfo> getManagerPosts(String sessionID, String accountID) throws RpcAuthorizationException {
		if (accountID == null) {
            throw new IllegalArgumentException();
        }
        RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.QUERY);
        
        Set<Group> groups = userAccountBiz.getBindedGroups(accountID);
        if (groups == null || groups.isEmpty()) {
        	return null;
        }
    	
        List<PostInfo> managerPosts = new ArrayList<PostInfo>(groups.size());
        for (Group g : groups) {
            Post p = g.getManagerPost();
            if (p == null) {
                continue;
            }
            // 添加到结果列表
            managerPosts.add(PostService.copy(p));
        }
        return managerPosts;
    }
	
	@Override
	public List<String> getBindedRoles(String sessionID, String accountID) throws RpcAuthorizationException {
		if (accountID == null) {
            throw new IllegalArgumentException();
        }
        RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.QUERY);
        
        Set<Role> roles = userAccountBiz.getBindedValidRoles(accountID);
        if (roles == null || roles.isEmpty()) {
        	return null;
        }
        List<String> bandingRoles = new ArrayList<String>(roles.size());
        for (Role r : roles) {
        	bandingRoles.add(r.getRoleName());
        }
        return bandingRoles;
	}

	/**
	 * 复制
	 * @param u
	 * @return
	 */
    private UserAccountInfo copy(UserAccount u) {
        UserAccountInfo as = new UserAccountInfo();
        as.setAccountID(u.getAccountID());
        as.setDisplayName(u.getDisplayName());
        as.setIsLock(u.getIsLock());
        as.setCreateDate(u.getCreateDate());
        as.setInvalidDate(u.getInvalidDate());
        as.setDescription(u.getDescription());
        as.setLastLoginTime(u.getLastLoginTime());
        as.setLoginCount(u.getLoginCount());
        as.setLoginIpLimit(u.getLoginIpLimit());
        return as;
    }
}
