/**
 * 
 */
package org.eapp.client.util;


import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.rpc.dto.UserAccountInfo;

/**
 * @author zsy
 * @version Dec 13, 2008
 */
public class UsernameCache {
	private static Log log = LogFactory.getLog(UsernameCache.class);
	private static HashMap<String, Username> usernames = new HashMap<String, Username>();
	
	
	private static final long CACHE_TIME = 864000000;//最大缓存时间
	/**
	 * 用户帐号转化为显示名称
	 * @param accountID
	 * @return
	 */
	public static String getDisplayName(String accountID) {
		if (accountID == null) {
			return "";
		}
		Username u = usernames.get(accountID);

		if (u == null || u.isOvertime()) {
			UserAccountService uas = new UserAccountService();
			try {
				UserAccountInfo ua = uas.getUserAccount(accountID);
				if (ua != null) {
					u = new Username(ua);
					usernames.put(accountID, u);
				}
				
			} catch (Exception e) {
				log.error("获取用户信息出错", e);
			}
		}
		return u == null ? accountID : u.displayName;
		
	}
	
	/**
	 * 缓存内容
	 * @author zsy
	 * @version Dec 13, 2008
	 */
	private static class Username {
		private String displayName;
		private long cacheTime;
		
		public Username(UserAccountInfo u) {
			displayName = u.getDisplayName();
			cacheTime = System.currentTimeMillis();
		}
		//缓存是否超时
		public boolean isOvertime() {
			return System.currentTimeMillis() - cacheTime > CACHE_TIME;
		}
	}

	/**
	 * 清空缓存
	 */
    public static void clearUsernames() {
        if (usernames != null && !usernames.isEmpty()) {
            usernames.clear();
        }
    }
    
}
