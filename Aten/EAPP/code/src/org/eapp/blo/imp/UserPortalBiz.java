/**
 * 
 */
package org.eapp.blo.imp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eapp.blo.IUserPortalBiz;
import org.eapp.dao.IPortletDAO;
import org.eapp.dao.IUserAccountDAO;
import org.eapp.dao.IUserPortletDAO;
import org.eapp.hbean.Portlet;
import org.eapp.hbean.Role;
import org.eapp.hbean.UserAccount;
import org.eapp.hbean.UserPortlet;
import org.eapp.hbean.UserPortletId;


/**
 * @version 
 */
public class UserPortalBiz implements IUserPortalBiz {
	
	private IUserPortletDAO userPortletDAO;
	private IUserAccountDAO userAccountDAO;
	private IPortletDAO portletDAO;
	
	public void setUserAccountDAO(IUserAccountDAO userAccountDAO) {
		this.userAccountDAO = userAccountDAO;
	}

	public void setPortletDAO(IPortletDAO portletDAO) {
		this.portletDAO = portletDAO;
	}

	/**
	 * @param userPortletDAO the userPortletDAO to set
	 */
	public void setUserPortletDAO(IUserPortletDAO userPortletDAO) {
		this.userPortletDAO = userPortletDAO;
	}

	@Override
	public List<Portlet> getUserPortlets(String userID, List<String> roleIds, String systemID) {
		if (StringUtils.isBlank(userID) || roleIds == null || roleIds.isEmpty()) {
			return null;
		}
		List<UserPortlet> ups = userPortletDAO.findByUserID(userID, roleIds, systemID);
		if (ups != null && !ups.isEmpty()) {
			List<Portlet> ps = new ArrayList<Portlet>();
			for (UserPortlet up : ups) {
				ps.add(up.getId().getPortlet());
			}
			return ps;
		}
		//如果为空，显示默认门户配置
		return portletDAO.findDefaultPortlets(roleIds, systemID);
//			if (dps != null && !dps.isEmpty()) {
//				for (DefaultPortlet dp : dps) {
//					ps.add(dp.getPortlet());
//				}
//			}
//		}
	}
	
	@Override
	public List<Portlet> getPortletConfigByUser(String userAccountID) {
		if (StringUtils.isBlank(userAccountID)) {
			return null;
		}
		UserAccount user = userAccountDAO.findByAccountID(userAccountID);
		if (user == null) {
			return null;
		}
		List<String> roleIds = new ArrayList<String>();
		if (user.getValidRoles() == null) {
			return null;
		}
		for (Role r : user.getValidRoles()) {
			roleIds.add(r.getRoleID());
		}
		List<UserPortlet> ups = userPortletDAO.findByUserID(user.getUserID(), roleIds, null);
		if (ups != null && !ups.isEmpty()) {
			List<Portlet> ps = new ArrayList<Portlet>();
			for (UserPortlet up : ups) {
				ps.add(up.getId().getPortlet());
			}
			return ps;
		}
		return null;
	}

	@Override
	public List<UserPortlet> modifyUserPortlets(String accountID, List<String[]> portletConfs) {
		UserAccount user = userAccountDAO.findByAccountID(accountID);
		if (user == null) {
			throw new IllegalArgumentException("用户不存在");
		}
		//先删除掉原来的板块配置
		userPortletDAO.deleteByUser(user.getUserID());
		
		if(portletConfs == null){
			return null;
		}
		
		List<UserPortlet> ups = new ArrayList<UserPortlet>();
		
		//创建新的板块配置
		Set<String> portletIds = new HashSet<String>();//判断重复
		for (String[] portletConf : portletConfs) {
			String portletID = portletConf[0];
			if(portletConf == null || !portletIds.add(portletID)){
				continue;
			}
			Portlet portlet = portletDAO.findById(portletID);
			if (portlet == null) {
				continue;
			}
			
			UserPortlet userPortlet = new UserPortlet();
			userPortlet.setPageContainerID(portletConf[1]);
			userPortlet.setPositionIndex(Integer.valueOf(portletConf[2]));
			userPortlet.setId(new UserPortletId(user, portlet));
			userPortletDAO.saveOrUpdate(userPortlet);
			ups.add(userPortlet);
		}
		
		portletIds.clear();
		return ups;
	}
	
}
