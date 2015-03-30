/**
 * 
 */
package org.eapp.blo;

import java.util.List;

import org.eapp.hbean.Portlet;
import org.eapp.hbean.UserPortlet;


/**
 * 门户展示业务逻辑接口
 * @version 
 */
public interface IUserPortalBiz {
	/**
	 * 列出用户所有使用的板块的信息 
	 * @param userID 用户ID:不能为空,且ID必须存在数据库中
	 * @return
	 */
	public List<Portlet> getUserPortlets(String userID, List<String> roleIds, String systemID);
	
	/**
	 * 列出用户所有使用的板块的信息
	 * @param userAccountId
	 * @return
	 */
	public List<Portlet> getPortletConfigByUser(String userAccountId);
	
	/**
	 * 更新用户的板块信息,先删除用户的所有板块信息,然后将新的板块信息新增
	 * @param userID 用户ID:不能为空,且ID必须存在数据库中
	 * @param userPortlets 用户板块列表:不能为空,不能有空元素
	 * @return 
	 */
	public List<UserPortlet> modifyUserPortlets(String accountID, List<String[]> portletConfs);
}
