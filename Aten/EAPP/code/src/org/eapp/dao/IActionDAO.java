package org.eapp.dao;

import java.util.List;

import org.eapp.dao.param.ActionQueryParameters;
import org.eapp.hbean.Action;
import org.eapp.util.hibernate.ListPage;


/**
 * 动作数据访问接口
 * @version
 */
public interface IActionDAO extends IBaseHibernateDAO {
	
	/**
	 * 删除动作
	 * @param actionID
	 */
//	public void deleteById(String actionID);

	/**
	 * 根据ID加载动作实例
	 * @param id
	 * @return
	 */
	public Action findById(java.lang.String id);

	/**
	 * 根据actionKey查找
	 * @param actionKey
	 * @return
	 */
	public Action findByActionKey(String actionKey);
	
	/**
	 * 通过名称查找
	 * @param name
	 * @return
	 */
	public List<Action> findByName(String name);
	
	/**
	 * 列出所有的动作
	 * @return
	 */
	public List<Action> findAll();

	
	/**
	 * 获得模块没有绑定的动作
	 * @param moduleId 模块
	 * @return 动作列表
	 */
	public List<Action> findExcludeActions(String moduleId,String name);

	/**
	 * 根据模块ID取得动作
	 * @param moduleID 模块ID
	 * @return
	 */
	public List<Action> findByModuleID(String moduleID);

	/**
	 * 根据动作名模糊查找
	 * @param aqp
	 * @return
	 */
	public ListPage<Action> queryActions(ActionQueryParameters aqp);
}