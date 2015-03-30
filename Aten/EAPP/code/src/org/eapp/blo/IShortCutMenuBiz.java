/**
 * 
 */
package org.eapp.blo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eapp.exception.EappException;
import org.eapp.hbean.ShortCutMenu;


/**
 * 快捷方式业务逻辑接口
 * @version 1.0
 */
public interface IShortCutMenuBiz {

	/**
	 * 列出用户的所有快捷方式
	 * @param userid
	 * @return
	 */
	public List<ShortCutMenu> getAllShortCutMenusByUser(String userid);

	/**
	 * 删除快捷方式
	 * @param id
	 */
	public void deleteShortCutMenu(String id, boolean custom);

	/**
	 * 发布快捷方式
	 * @param id
	 * @throws EappException
	 */
	public void txEnableShortCutMenu(String id) throws EappException;

	/**
	 * 撤销快捷方式
	 * @param shortCutMenuId
	 * @throws EappException
	 */
	public void txDisableShortCutMenu(String shortCutMenuId) throws EappException;

	/**
	 * 查看快捷方式
	 * @param id
	 * @return
	 */
	public ShortCutMenu getShortCutMenu(String id);

	/**
	 * 新增快捷方式
	 * @param userid
	 * @param menuTitle
	 * @param url
	 * @param windowTarget
	 * @param logoUrl
	 * @param type
	 * @return
	 */
	public ShortCutMenu addShortCutMenu(String userid, String menuTitle, String moduleTitle, String menuLink, String windowTarget,
			String menuIcon, boolean isValid, String type) throws EappException;

	/**
	 * 修改快捷方式
	 * @param shortCutMenu
	 */
	public void modifyShortCutMenu(String shortCutID, String menuTitle, String menuLink, String windowTarget, String menuIcon, boolean isValid, String type) throws EappException;

	/**
	 * 列出所有发布的快捷方式
	 * @param userid
	 * @return
	 */
	public Set<ShortCutMenu> getEnableShortCutMenu(String userid);
	
	/**
	 * 列出所有收藏的快捷菜单
	 * @param userid
	 * @return
	 */
	public Set<ShortCutMenu> getFavoriteShortCutMenu(String userid);
	
	/**
	 * 列出所有未发布的快捷方式
	 * 08.06.12 ADD FOR 快捷方式定制
	 * @param userid
	 */
	public Set<ShortCutMenu> getDisableShortCutMenu(String userid);

	/**
	 * 排序快捷方式
	 * @param idsort
	 */
	public void modifyShortCutMenuSort(Map<String, Integer> idsort);
	
	/**
	 * 整理快捷方式
	 * 08.06.12 ADD FOR 快捷方式定制
	 * @param idsort
	 */
	public void txManageShortCutMenus(Map<String, Integer> idsortPublish,Map<String, Integer> idsortCancel);

	/**
	 * 列出所有的系统级快捷方式
	 * 08.06.16 ADD
	 * @return
	 */
	public List<ShortCutMenu> getSystemShortCutMenus();
	
//	public ListPage<ShortCutMenu> queryShortCutMenus(QueryParameters qp);
	
	/**
	 * 列出所有的可用系统级快捷方式
	 * 08.06.16 ADD
	 * @return
	 */
	public List<ShortCutMenu> getEnableSystemShortCutMenus();
}
