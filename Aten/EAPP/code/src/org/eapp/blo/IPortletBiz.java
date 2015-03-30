/**
 * 
 */
package org.eapp.blo;

import java.util.List;
import java.util.Set;

import org.eapp.dao.param.PortletQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.DefaultPortlet;
import org.eapp.hbean.Portlet;
import org.eapp.hbean.Role;
import org.eapp.util.hibernate.ListPage;


/**
 * @version 1.0
 */
public interface IPortletBiz {
	/**
	 * 根据条件查询板块,板块名称模糊匹配
	 * @param queryParameters
	 * @return
	 */
	public ListPage<Portlet> queryPortlets(PortletQueryParameters queryParameters);

	/**
	 * 删除板块
	 * @param portletId
	 */
	public void deletePortlet(String portletId) throws EappException;

	/**
	 * 创建板块
	 * @param url
	 * @param style
	 * @param hiddenable
	 * @param movedable
	 * @param porletName
	 * @return
	 */
	public Portlet addPortlet(String subSystemId, String portletName, String url, 
			String moreUrl, String style, Boolean hiddenable, Boolean movedable) throws EappException;

	/**
	 * 查看板块信息
	 * @param id 板块ID
	 * @return
	 */
	public Portlet getPortlet(String id);

	/**
	 * 修改板块信息
	 * @param portlet
	 */
	public void modifyPortlet(String subSystemId, String portletID, String portletName, String url,
			String moreUrl, String style, Boolean hiddenable, Boolean movedable) throws EappException;
	
	public List<Portlet> getPortletsForUserSelect(String userID);
	
	/**
	 * 查找默认门户
	 * @return
	 */
	public List<Portlet> getDefaultPortlets();
	
	/**
	 * 保存默认门户
	 * @param portlets
	 */
	public void txSetDefaultPortlets(List<DefaultPortlet> portlets);
	
	/**
	 * 取得所有用户可定制的门户
	 * @param roleIds
	 * @return
	 */
	public List<Portlet> getPortlets(List<String> roleIds);
	
	/**
	 * 取得所有用户可定制的门户
	 * @param userAccountID
	 * @return
	 */
	public List<Portlet> getPortlets(String userAccountID);
	
	/**
	 * 取得已绑定的角色
	 * @param portletID
	 * @return
	 */
	public Set<Role> getBindedRoles(String portletID);
	
	public Portlet txBindRole(String portletID, String[] roleIDs);
}
