package org.eapp.dao;

import java.util.List;

import org.eapp.hbean.UserPortlet;


/**
 * 
 * @version
 */
public interface IUserPortletDAO extends IBaseHibernateDAO {
	/**
	 * 获得使用板块的用户数
	 * @param portletID
	 * @return
	 */
	public int getUserCountByPortletID(String portletID);

	/**
	 * 删除用户的所有板块
	 * @param userID
	 */
	public void deleteByUser(String userID);
	
	/**
	 * @author zsy
	 * 查找用户定制的门户
	 * @param userID
	 * @return
	 */
	public List<UserPortlet> findByUserID(String userID, List<String> roleIds, String systemID);

}