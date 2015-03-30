package org.eapp.dao;

import java.util.List;
import java.util.Map;

import org.eapp.hbean.ShortCutMenu;

/**
 * @author surefan
 * @version 1.0
 */
public interface IShortCutMenuDAO extends IBaseHibernateDAO {
	
	public ShortCutMenu findById(String id);

	public List<ShortCutMenu> findByUserID(String userID);
	
	public List<ShortCutMenu> findByType(String type);
	
	public List<ShortCutMenu> findEnableByType(String type);
	
//	public ListPage<ShortCutMenu> queryShortCutMenus(QueryParameters qp);
	/**
	 * 根据ID和序列排序快捷方式
	 * @param idsort
	 */
	public void sortShortCutMenus(Map<String, Integer> idsort);
	
	public void sortShortCutMenus(Map<String, Integer> idsort,Boolean isValid);
	
	/**
	 * 获取用户拥有的快捷方式的最大序列
	 * @param userID
	 * @return
	 */
	public int getMaxDisplayOrderByUserID(String userID);
	
	public boolean isShortCutMenuRepeat(String userid, String type, String menuTitle);

}