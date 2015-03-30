/**
 * 
 */
package org.eapp.rpc.session;

import java.util.HashMap;
import java.util.List;


/**
 * @author linliangyi 2008-06-21
 * @modify zsy 2009-5-12
 * @version 1.0
 */
public class RPCPrincipal {
	
	/*
	 * PRCPrincipal超时时长,单位：秒
	 */
	public static int rpcPrincipalRefreshInterval = 15 * 60;//同步权限时间为5分钟

	private String actorID;//Actor 帐号的数据库主键
	private String accountID;//Actor 的帐号ID
	private String displayName;//Actor的显示名称
	private long invalidDate;//Actor 的失效时间
//	private boolean isLock;//是否锁定
	private long lastRefreshTime;//当前PRCPrincipal实例的最后一次更新时间
	private List<String> serviceIDs;//Actor享有的服务（角色）的ID集合
	
	//KEY为系统ID+模块ID
	private HashMap<String, String[]> moduleActions = new HashMap<String, String[]>();
	
	/**
	 * 设置超时时长
	 * @param rpcPrincipalRefreshInterval
	 */
	public static void setRpcPrincipalRefreshInterval(int rpcPrincipalRefreshInterval) {
		RPCPrincipal.rpcPrincipalRefreshInterval = rpcPrincipalRefreshInterval;
	}
	
	public String getActorID() {
		return actorID;
	}

	public void setActorID(String actorID) {
		this.actorID = actorID;
	}

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public long getInvalidDate() {
		return invalidDate;
	}

	public void setInvalidDate(long invalidDate) {
		this.invalidDate = invalidDate;
	}

//	public boolean isLock() {
//		return isLock;
//	}
//	
//	public void setLock(boolean isLock) {
//		this.isLock = isLock;
//	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	public List<String> getServiceIDs() {
		return serviceIDs;
	}
	
	public void setServiceIDs(List<String> serviceIDs) {
		this.serviceIDs = serviceIDs;
	}

	public HashMap<String, String[]> getModuleActions() {
		return moduleActions;
	}

	public void setModuleActions(HashMap<String, String[]> moduleActions) {
		this.moduleActions = moduleActions;
	}

	/**
	 * 添加有权限的模块动作缓存
	 * @param systemId
	 * @param moduleKey
	 * @param actions
	 */
	public void addModuleAction(String systemID, String moduleKey, String[] actions) {
		moduleActions.put(getModuleActionMapKey(systemID, moduleKey), actions);
	}
	
	/**
	 * 取得 有权限的模块动作缓存
	 * @param systemID
	 * @param moduleKey
	 * @return
	 */
	public String[] getModuleAction(String systemID, String moduleKey) {
		return moduleActions.get(getModuleActionMapKey(systemID, moduleKey));
	}
	
	/**
	 * 是否需要更新
	 * @return
	 */
	public boolean isNeedSynch() {
		return System.currentTimeMillis() - lastRefreshTime > rpcPrincipalRefreshInterval * 1000;
	}
	
	/**
	 * 判断接口帐号已经过期
	 * @return
	 */
	public boolean isVaild() {
//		return !isLock && (invalidDate > System.currentTimeMillis() || invalidDate == 0);
		return invalidDate == 0 || invalidDate > System.currentTimeMillis();
	}
	
	/**
	 * 判断动作在模块内是否有权限
	 * @param moduleID 模块ID
	 * @param action 动作
	 * @return
	 */
	public boolean hasRight(String systemID, String moduleKey, String actionKey) {
		//以上判断动作在模块内是否需要权限，若不需要返回true
		if (systemID == null || moduleKey == null || actionKey == null) {
			return true;
		}
		String[] actionList = moduleActions.get(getModuleActionMapKey(systemID, moduleKey));
		if (actionList == null || actionList.length < 2) {
			return true;
		}
		String allActions = actionList[0];
		if (allActions == null) {
			return true;
		}
		//要验证的动作不在所有动作列表里，说明不需要权限验证，直接通过
		if (!allActions.contains("," + actionKey + ",")) {
			return true;
		}
		
		//该action需要权限，以下为判断过程
		String myActions = actionList[1];
		if (myActions == null) {
			//该模块下没有任何权限，直接不通过
			return false;
		}
		//判断要验证的动作是否有权限的动作列表里
		return myActions.contains("," + actionKey + ",");
	}
	
	/**
	 * 帐号失效时清空Bean中的信息，释放内存
	 */
	public void clear() {
		if (serviceIDs != null) {
			serviceIDs.clear();
		}
		if (moduleActions != null) {
			moduleActions.clear();
		}
	}
	
	private String getModuleActionMapKey(String systemID, String moduleKey) {
		return systemID + "-" +moduleKey;
	}
}
