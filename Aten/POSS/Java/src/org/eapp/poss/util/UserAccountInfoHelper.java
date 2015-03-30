package org.eapp.poss.util;

import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eapp.client.hessian.GroupService;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.dto.GroupInfo;
import org.springframework.util.CollectionUtils;

public class UserAccountInfoHelper {
	private static UserAccountService uas = new UserAccountService();
	private static GroupService gs = new GroupService();
	
	public static String getOrgName(String accountId) {
		try {
			List<GroupInfo> groups = uas.getBindedGroups(accountId);
	        if (!CollectionUtils.isEmpty(groups)) {
	        	return getParentDept(groups.get(0));
	        }
		} catch(Exception e) {
			e.printStackTrace();
		}
        return null;
	}
	
	private static String getParentDept(GroupInfo g) throws RpcAuthorizationException, MalformedURLException{
		if (StringUtils.isNotEmpty(g.getParentGroupID())) {
			GroupInfo group = gs.getGroupByID(g.getParentGroupID());
			getParentDept(group);
		}
		return g.getGroupName();
		
	}
}
