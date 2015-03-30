package org.eapp.dao;

import java.util.List;

import org.eapp.dao.param.PortletQueryParameters;
import org.eapp.hbean.Portlet;
import org.eapp.util.hibernate.ListPage;

/**
 * 
 * @author wangshengwei
 * @version 1.0
 */
public interface IPortletDAO extends IBaseHibernateDAO {

	public Portlet findById(String id);

	public List<Portlet> findByPortletName(String portletName);
	
	/**
	 * 查询板块列表
	 * @param queryParameters
	 * @return
	 */
	public ListPage<Portlet> queryPortlets(PortletQueryParameters queryParameters);

	/**
	 * 列出用户可用的门户列表
	 * @param userID 用户ID
	 * @return
	 */
	public List<Portlet> findUseablePortletsByUser(String userID);
	
	/**
	 * 查找默认门户
	 * @return
	 */
	public List<Portlet> findDefaultPortlets();
	
	/**
	 * 查找默认门户
	 * @param roleIds
	 * @return
	 */
	public List<Portlet> findDefaultPortlets(List<String> roleIds, String subSystemID);
	
	/**
	 * 删除所有默认门户
	 */
	public void delAllDefaultPortlets();
	
	/**
	 * 取得所有用户可定制的门户
	 * @param roleIds
	 * @return
	 */
	public List<Portlet> queryPortlets(List<String> roleIds);

}