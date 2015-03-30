/**
 * 
 */
package org.eapp.rpc.session;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.comobj.SessionAccount;
import org.eapp.comobj.SessionAccount.Name;
import org.eapp.dao.IActorAccountDAO;
import org.eapp.dao.IModuleActionDAO;
import org.eapp.dao.IUserAccountDAO;
import org.eapp.dto.ActionKey;
import org.eapp.exception.EappException;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.hbean.ActorAccount;
import org.eapp.hbean.Post;
import org.eapp.hbean.Group;
import org.eapp.hbean.Role;
import org.eapp.hbean.Service;
import org.eapp.hbean.UserAccount;
import org.eapp.sys.config.SysConstants;
import org.eapp.util.security.DigestAlgorithm;


/**
 * @author zsy
 * @version 
 */
public class SessionAccountInfoManage implements ISessionAccountInfoManage {
	private static final Log log = LogFactory.getLog(SessionAccountInfoManage.class);
	private IUserAccountDAO userAccountDAO;
	private IActorAccountDAO actorAccountDAO;
	private IModuleActionDAO moduleActionDAO;
	private ICredenceAlgorithm credenceAlgorithm;
	private String defaultStyleThemes;

	public void setUserAccountDAO(IUserAccountDAO userAccountDAO) {
		this.userAccountDAO = userAccountDAO;
	}

	public void setModuleActionDAO(IModuleActionDAO moduleActionDAO) {
		this.moduleActionDAO = moduleActionDAO;
	}

	public void setActorAccountDAO(IActorAccountDAO actorAccountDAO) {
		this.actorAccountDAO = actorAccountDAO;
	}

	public void setCredenceAlgorithm(ICredenceAlgorithm credenceAlgorithm) {
		this.credenceAlgorithm = credenceAlgorithm;
	}

	public void setDefaultStyleThemes(String defaultStyleThemes) {
		this.defaultStyleThemes = defaultStyleThemes;
	}

	@Override
	public SessionAccount csLoadSessionAccountInfo(String accountID, String ipAddr) {
		UserAccount user = userAccountDAO.findByAccountID(accountID);
		if (user == null) {
			return null;
		}

		//加载用户在session中的信息
		SessionAccount su = new SessionAccount();
		su.setUserID(user.getUserID());
		su.setAccountID(user.getAccountID());
		su.setDisplayName(user.getDisplayName());
		su.setStyleThemes(user.getStyleThemes() == null ? defaultStyleThemes : user.getStyleThemes());
		su.setLoginIpAddr(ipAddr);
		su.setLock(user.getIsLock());
		if (user.getInvalidDate() != null) {
			su.setInvalidDate(user.getInvalidDate().getTime());
		}
		if (StringUtils.isNotBlank(user.getLoginIpLimit())) {
			su.setLoginIpLimit(user.getLoginIpLimit());
		}
		if (!su.isVaild()) {
			return su;
		}
//		加载部门(包括部门和项目组)
		List<Name> groups = new ArrayList<Name>();
		if (user.getGroups() != null) {
			for (Group group : user.getGroups()) {
				groups.add(su.new Name(group.getGroupID(), group.getGroupName()));
			}
		}
		su.setGroups(groups);
		//加载部门
		List<Name> depts = new ArrayList<Name>();
		if (user.getGroups() != null) {
			for (Group group : user.getGroups()) {
				if(group.getType().equals(Group.GROUP_TYPE_DEPT))
					depts.add(su.new Name(group.getGroupID(), group.getGroupName()));
			}
		}
		su.setDepts(depts);
//		//加载项目组
//		List<Name> projects = new ArrayList<Name>();
//		if (user.getGroups() != null) {
//			for (Group group : user.getGroups()) {
//				if(group.getType().equals(Group.GROUP_TYPE_PROJECT))
//					projects.add(su.new Name(group.getGroupID(), group.getGroupName()));
//			}
//		}
//		su.setProjects(projects);
		//加载有效的角色
		List<Name> roles = new ArrayList<Name>();
		if (user.getValidRoles() != null) {
			for (Role role : user.getValidRoles()) {
				roles.add(su.new Name(role.getRoleID(), role.getRoleName()));
			}
		}
		su.setRoles(roles);
		//加载职位
		List<Name> posts = new ArrayList<Name>();
		if (user.getPosts() != null) {
			for (Post p : user.getPosts()) {
				posts.add(su.new Name(p.getPostID(), p.getPostName()));
			}
		}
		su.setPosts(posts);
		//设置最后同步时间
		su.setLastSynchTime(System.currentTimeMillis());
		
		return su;
	}

	private SessionAccount reloadSessionAccountInfo(SessionAccount user, String systemId,
			String moduleKey) {
		if (user == null) {
			throw new IllegalArgumentException();
		}
		synchronized(user) {
			if (user.isNeedSynch()) {//需要同步session中用户信息
				log.info("需要同步session中用户信息..");
				//更新基本信息
				UserAccount userAccount = userAccountDAO.findByAccountID(user.getAccountID());
				if (userAccount == null) {
					user.setLock(true);
					return user;
				}
				user.setDisplayName(userAccount.getDisplayName());
				user.setLock(userAccount.getIsLock());
				if (userAccount.getInvalidDate() != null) {
					user.setInvalidDate(userAccount.getInvalidDate().getTime());
				} else {
					user.setInvalidDate(0);
				}
				if (StringUtils.isNotBlank(user.getLoginIpLimit())) {
					user.setLoginIpLimit(user.getLoginIpLimit());
				} else {
					user.setLoginIpLimit(null);
				}
				if (!user.isVaild()) {
					user.clear();
					return user;
				}
				
				//更新有效的角色
				List<Name> roles = new ArrayList<Name>();
				if (userAccount.getValidRoles() != null) {
					for (Role role : userAccount.getValidRoles()) {
						roles.add(user.new Name(role.getRoleID(), role.getRoleName()));
					}
				}
				//更新职位
				List<Name> posts = new ArrayList<Name>();
				if (userAccount.getPosts() != null) {
					for (Post p : userAccount.getPosts()) {
						posts.add(user.new Name(p.getPostID(), p.getPostName()));
					}
				}
				
				user.setRoles(roles);
				user.setPosts(posts);
				//更新最后同步时间
				user.setLastSynchTime(System.currentTimeMillis());
				//清空模块动作缓存，
				user.getModuleActions().clear();
				if (StringUtils.isNotBlank(moduleKey)) {//加载当前模块动作
					log.info("同步..加载模块action：" + moduleKey);
					user.getModuleActions().put(moduleKey, getActions(systemId, moduleKey, user.getRoleIDs(), false));
				}
				
			} else {//不需要同步更新
				log.info("不需要同步session中用户信息..");
				if (StringUtils.isNotBlank(moduleKey)) {//
					String[] actionList = user.getModuleActions().get(moduleKey);
					if (actionList == null) {//当前模块动作不在缓存中则加载
						log.info("不同步..加载模块action：" + moduleKey);
						actionList = getActions(systemId, moduleKey, user.getRoleIDs(), false);
						user.getModuleActions().put(moduleKey, actionList);
						
					}
				}
			}
		}
		return user;
	}
	
	@Override
	public SessionAccount csReloadSessionAccountInfo(SessionAccount user,
			String moduleKey) {
		return reloadSessionAccountInfo(user, SysConstants.EAPP_SUBSYSTEM_ID, moduleKey);
	}
	
	@Override
	public SessionAccount csReloadSessionAccountInfo(SessionAccount user, String systemId,
			String moduleKey) {
		return reloadSessionAccountInfo(user, systemId, moduleKey);
	}
	
	
	@Override
	public RPCPrincipal getLoginRPCPrincipal(String accountID,String credence) throws RpcAuthorizationException {
		ActorAccount user = getActorAccount(accountID);
		if(!credenceAlgorithm.verifyCredence(user, credence)){//凭证验证失败
			throw new RpcAuthorizationException(RpcAuthorizationException.CODE_WRONGPASSWORD, "接口帐号认证失败：" + accountID);
		}
		//加载用户在session中的信息
		RPCPrincipal su = new RPCPrincipal();
		loadRPCPrincipal(user, su);
		log.info("接口帐号登录成功：" + accountID);
		return su;
	}
	
	/**
	 * 获取ActorAccount信息
	 * @param accountID 帐号ID
	 * @return ActorAccount
	 * @throws RpcAuthorizationException
	 */
	private ActorAccount getActorAccount(String accountID) throws RpcAuthorizationException {
		ActorAccount user = actorAccountDAO.findByAccountID(accountID);
		if (user == null) {
			throw new RpcAuthorizationException(RpcAuthorizationException.CODE_NOACCOUNT, "接口帐号不存在：" + accountID);
		} else if (user.getIsLock()) {
			throw new RpcAuthorizationException(RpcAuthorizationException.CODE_ACCOUNTINVALID, "接口帐号已锁定：" + accountID);
		}
		return user;
	}
	
	/**
	 * 加载RPCPrincipal信息
	 * @param user ActorAccount
	 * @param su RPCPrincipal
	 * @throws RpcAuthorizationException
	 */
	private void loadRPCPrincipal(ActorAccount user, RPCPrincipal su) throws RpcAuthorizationException {
		if (user.getInvalidDate() != null) {
			su.setInvalidDate(user.getInvalidDate().getTime());
		} else {
			su.setInvalidDate(0);
		}
		if (!su.isVaild()) {
			log.info(su.getAccountID() + "接口帐号失效时间：" + su.getInvalidDate() + "；当前系统时间：" + System.currentTimeMillis());
			su.clear();
			throw new RpcAuthorizationException(RpcAuthorizationException.CODE_ACCOUNTINVALID, "接口帐号已过期：" + user.getAccountID());
		}
		
		su.setActorID(user.getActorID());
		su.setAccountID(user.getAccountID());
		su.setDisplayName(user.getDisplayName());
//		su.setLock(user.getIsLock());//锁定时前面已经抛出异常，所以RPCPrincipal都是未锁定的
		
		//加载有效的服务（角色）
		List<String> services = new ArrayList<String>();
		if (user.getValidServices() != null) {
			for (Service service : user.getValidServices()) {
				services.add(service.getServiceID());
			}
		}
		su.setServiceIDs(services);
		//设置最后同步时间
		su.setLastRefreshTime(System.currentTimeMillis());
	}
	
	@Override
	public RPCPrincipal csReloadRPCPrincipal(RPCPrincipal user, String systemId, String moduleKey) throws RpcAuthorizationException {
		if (user == null || systemId == null || moduleKey == null) {
			throw new IllegalArgumentException();
		}
		synchronized(user) {
			if (user.isNeedSynch()) {//需要同步session中用户信息
				log.info("需要同步RPCsession中用户信息...." + user.getAccountID());
				//更新基本信息
				ActorAccount userAccount = getActorAccount(user.getAccountID());
				loadRPCPrincipal(userAccount, user);
				
				//清空模块动作缓存，
				user.getModuleActions().clear();
				//加载当前模块动作
				log.info("RPC同步..加载模块action：" + moduleKey);
				user.addModuleAction(systemId, moduleKey, getActions(systemId, moduleKey, user.getServiceIDs(), true));
			} else {//不需要同步更新
				log.info("不需要同步RPCsession中用户信息..");
				String[] actionList = user.getModuleAction(systemId, moduleKey);
				if (actionList == null) {//当前模块动作不在缓存中则加载
					log.info("RPC不同步..加载模块action：" + moduleKey);
					actionList = getActions(systemId, moduleKey, user.getServiceIDs(), true);
					user.addModuleAction(systemId, moduleKey, actionList);
				}
			}
		}
		return user;
	}

	/**
	 * 加载用户session中的动作信息
	 * @param systemId 系统ID
	 * @param moduleKey 模块Key
	 * @param roleIDs 角色ID列表
	 * @return 数组0：该模块所有动作，数组1：给定角色在该模块有权限的动作;
	 * 		动作间用","分开
	 */
	private String[] getActions(String systemId, String moduleKey, List<String> roleIDs, boolean isRPC) {
		if (systemId == null) {
			throw new IllegalArgumentException();
		}
		String[] actionArray = new String[2];
		String moduleID = moduleActionDAO.getModuleID(systemId, moduleKey);
		log.info("模块ID：" + moduleID);
		if (moduleID == null) {
			return actionArray;
		}
		
		//加载当前模块的所有动作
		List<ActionKey> list = moduleActionDAO.getModuleActionByModuleID(moduleID);
		StringBuffer asb = new StringBuffer();
		if (list != null && list.size()> 0) {
			asb.append(",");
			for (ActionKey ak : list) {
				asb.append(ak.getActionKey()).append(",");
			}
			actionArray[0] = asb.toString();
		}
		log.info("所有动作：" + actionArray[0]);
		if (roleIDs == null || roleIDs.size() == 0) {
			return actionArray;
		}
		//加载用户在当前模块有权限的动作
		if (isRPC) {
			list = moduleActionDAO.getModuleActionByServiceIDs(roleIDs, moduleID);
		} else {
			list = moduleActionDAO.getModuleActionByRoleIDs(roleIDs, moduleID);
		}
		asb = new StringBuffer();
		if (list != null && list.size()> 0) {
			asb.append(",");
			for (ActionKey ak : list) {
				asb.append(ak.getActionKey()).append(",");
			}
			actionArray[1] = asb.toString();
		}
		log.info("有权限动作：" + actionArray[1]);
		return actionArray;
	}
	
	@Override
	public String[] getActions(String moduleKey, List<String> roleIDs) {
		return getActions(SysConstants.EAPP_SUBSYSTEM_ID, moduleKey, roleIDs, false);
	}
	
	@Override
	public String[] getActions(String systemId, String moduleKey, List<String> roleIDs) {
		return getActions(systemId, moduleKey, roleIDs, false);
	}

	@Override
	public boolean csAuthenticateAccount(String accountID,
			String password) throws EappException {
		if (accountID == null || password == null) {
			return false;
		}
		UserAccount user = userAccountDAO.findByAccountID(accountID);
		if (user == null) {
			throw new EappException("用户不存在");
		} else if (user.getIsLock() != null && user.getIsLock()) {
			throw new EappException("用户已锁定");
		} else if (user.getInvalidDate() != null && 
				user.getInvalidDate().getTime() < System.currentTimeMillis()) {
			throw new EappException("用户已失效");
		}
		return user.getPassword().equals(DigestAlgorithm.md5Digest(password));
	}
}
