/**
 * 
 */
package org.eapp.blo.imp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eapp.blo.IPortletBiz;
import org.eapp.dao.IPortletDAO;
import org.eapp.dao.IRoleDAO;
import org.eapp.dao.ISubSystemDAO;
import org.eapp.dao.IUserAccountDAO;
import org.eapp.dao.param.PortletQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.DefaultPortlet;
import org.eapp.hbean.Portlet;
import org.eapp.hbean.Role;
import org.eapp.hbean.SubSystem;
import org.eapp.hbean.UserAccount;
import org.eapp.util.hibernate.ListPage;


/**
 * @version 1.0
 */
public class PortletBiz implements IPortletBiz {

	private IPortletDAO portletDAO;
	private IRoleDAO roleDAO;
	private IUserAccountDAO userAccountDAO;
	private ISubSystemDAO subSystemDAO;

	/**
	 * @param portletDAO the portletDAO to set
	 */
	public void setPortletDAO(IPortletDAO portletDAO) {
		this.portletDAO = portletDAO;
	}
	public void setRoleDAO(IRoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}
	public void setUserAccountDAO(IUserAccountDAO userAccountDAO) {
		this.userAccountDAO = userAccountDAO;
	}
	public void setSubSystemDAO(ISubSystemDAO subSystemDAO) {
		this.subSystemDAO = subSystemDAO;
	}

	@Override
	public ListPage<Portlet> queryPortlets(PortletQueryParameters queryParameters) {
		if(queryParameters == null){
			throw new IllegalArgumentException();
		}
		return portletDAO.queryPortlets(queryParameters);
	}
	
	@Override
	public Portlet getPortlet(String id) {
		return portletDAO.findById(id);
	}

	@Override
	public Portlet addPortlet(String subSystemId, String portletName, String url, String moreUrl, String style,
			Boolean hiddenable, Boolean movedable) throws EappException {
		if (StringUtils.isBlank(portletName)){
			throw new IllegalArgumentException("板块名称不能为空");
		}
		if(StringUtils.isBlank(url)){
			throw new IllegalArgumentException("板块URL不能为空");
		}
		SubSystem subSystem = null;
		if (StringUtils.isBlank(subSystemId)) {
			throw new IllegalArgumentException("子系统Id不能为空");
		} else {
			subSystem = subSystemDAO.findById(subSystemId);
			if(subSystem == null){
				throw new IllegalArgumentException("数据库中不存在该子系统ID"+subSystemId);
			}
		}
		List<Portlet> portlets = portletDAO.findByPortletName(portletName);
		if(portlets.size() > 0){
			throw new EappException("板块名称不能重复");
		}
		
		Portlet portlet = new Portlet();
		portlet.setSubSystem(subSystem);
		portlet.setPortletName(portletName);
		portlet.setUrl(url);
		portlet.setMoreUrl(moreUrl);
		portlet.setHiddenable(hiddenable);
		portlet.setMovedable(movedable);
		portlet.setStyle(style);
		portletDAO.save(portlet);
		return portlet;
	}

	@Override
	public void deletePortlet(String portletId) throws EappException {
		if(StringUtils.isBlank(portletId)){
			throw new IllegalArgumentException("板块ID不能为空");
		}
		Portlet p = portletDAO.findById(portletId);
		if (p != null) {
			portletDAO.delete(p);
		}
	}

	@Override
	public void modifyPortlet(String subSystemId, String portletId, String portletName, String url, 
			String moreUrl, String style, Boolean hiddenable, Boolean movedable)  throws EappException {
		if(StringUtils.isBlank(portletId)){
			throw new IllegalArgumentException("板块ID不能为空");
		}
		if(StringUtils.isBlank(portletName)){
			throw new IllegalArgumentException("板块名称不能为空");
		}
		if(StringUtils.isBlank(url)){
			throw new IllegalArgumentException("板块URL不能为空");
		}
		if (StringUtils.isBlank(subSystemId)) {
			throw new IllegalArgumentException("子系统Id不能为空");
		}
		SubSystem subSystem = subSystemDAO.findById(subSystemId);
		if(subSystem == null){
			throw new IllegalArgumentException("数据库中不存在该子系统ID"+subSystemId);
		}
		
		Portlet srcPortlet = portletDAO.findById(portletId);
		if (srcPortlet == null){
			throw new IllegalArgumentException("参数异常:数据库不存在该板块ID,"+portletId);
		}
		List<Portlet> portlets = portletDAO.findByPortletName(portletName);
		if(!srcPortlet.getPortletName().equals(portletName)&&portlets.size() > 0){
			throw new EappException("板块名称不能重复");
		}
		srcPortlet.setSubSystem(subSystem);
		srcPortlet.setPortletName(portletName);
		srcPortlet.setUrl(url);
		srcPortlet.setMoreUrl(moreUrl);
		srcPortlet.setHiddenable(hiddenable);
		srcPortlet.setMovedable(movedable);
		srcPortlet.setStyle(style);
		portletDAO.saveOrUpdate(srcPortlet);
	}

	@Override
	public List<Portlet> getPortletsForUserSelect(String userID) {
		return portletDAO.findUseablePortletsByUser(userID);
	}
	
	@Override
	public List<Portlet> getDefaultPortlets() {
		return portletDAO.findDefaultPortlets();
	}
	
	@Override
	public void txSetDefaultPortlets(List<DefaultPortlet> portlets) {
		
		//先删除掉原来的板块配置
		portletDAO.delAllDefaultPortlets();
		if(portlets == null){
			return;
		}
		Set<String> portletIds = new HashSet<String>();//判断重复
		//创建新的板块配置
		for(DefaultPortlet dp:portlets){
			if(dp == null || !portletIds.add(dp.getPortletId())){
				continue;
			}
			Portlet portlet = portletDAO.findById(dp.getPortletId());
			if (portlet == null) {
				continue;
			}
			dp.setPortlet(portlet);
			portletDAO.save(dp);
		}

	}

	@Override
	public List<Portlet> getPortlets(List<String> roleIds) {
		if (roleIds == null || roleIds.size() == 0) {
			return null;
		}
		return portletDAO.queryPortlets(roleIds);
		
	}
	
	@Override
	public List<Portlet> getPortlets(String userAccountID) {
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
		return portletDAO.queryPortlets(roleIds);
		
	}

	@Override
	public Set<Role> getBindedRoles(String portletID) {
		if (StringUtils.isBlank(portletID)) {
			return null;
		}
		Portlet portlet = portletDAO.findById(portletID);
		if (portlet == null) {
			return null;
		}
		portlet.getRoles().size();//加载延迟内容
		return portlet.getRoles();
	}

	@Override
	public Portlet txBindRole(String portletID, String[] roleIDs) {
		if (StringUtils.isBlank(portletID)) {
			throw new IllegalArgumentException();
		}
		
		Portlet portlet = portletDAO.findById(portletID);
		if (portlet == null)  {
			throw new IllegalArgumentException("对象不存在");
		}
		HashSet<Role> set = new HashSet<Role>();
		Role role = null;
		if (roleIDs != null && roleIDs.length > 0) {
			for (String gid : roleIDs) {
				if (StringUtils.isBlank(gid)) {
					continue;
				}
				role = roleDAO.findById(gid);
				if (role != null) {
					set.add(role);
				}
			}
		}
		portlet.setRoles(set);
		portletDAO.saveOrUpdate(portlet);
		return portlet;
	}
}
