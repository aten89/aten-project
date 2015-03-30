package org.eapp.crm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eapp.rpc.dto.UserAccountInfo;


public class UserAccountCache {
	
	private static final int MAX_CACHE_SIZE = 20;
	private static Map<String, List<String>> userAccountCacheMap = new HashMap<String, List<String>>();
	
	private UserAccountCache() {
		
	}
	/**
	 * 取得缓存数据
	 * @param groupID
	 * @return
	 */
	public static List<String> getUserCaches(String groupID) {
		if (groupID == null) {
			groupID = "All";
		}
		return userAccountCacheMap.get(groupID);
	}
	
	public static void addUserCaches(String groupID, List<UserAccountInfo> us) {
		if (userAccountCacheMap.size() > MAX_CACHE_SIZE) {
			clearCache();
		}
		if (groupID == null) {
			groupID = "All";
		}
		List<String> uc = new ArrayList<String>();
		if (us != null && !us.isEmpty()) {
			for (UserAccountInfo u : us) {
				uc.add(u.getAccountID());
			}
		}
		userAccountCacheMap.put(groupID, uc);
	}
	
	/**
	 * 清除缓存,由定时器调用
	 */
	public static void clearCache() {
		userAccountCacheMap.clear();
	}
}
