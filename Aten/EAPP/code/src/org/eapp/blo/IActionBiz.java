/**
 * 
 */
package org.eapp.blo;

import java.util.List;

import org.eapp.dao.param.ActionQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.Action;
import org.eapp.util.hibernate.ListPage;


/**
 * @version
 */
public interface IActionBiz {

	/**
	 * 列出所有的动作
	 * @return 动作列表
	 */
	public List<Action> getAllActions();

	/**
	 * 删除动作
	 * @param actionId 动作ID
	 * @throws EappException 该动作仍绑定模块,不能删除
	 */
	public Action deleteAction(String actionId) throws EappException;

	/**
	 * 新增动作
	 * @param actionKey 动作KEY
	 * @param name 动作名
	 * @param logoURL 图标链接
	 * @param tips 说明
	 * @param description 备注
	 * @return 保存后的动作实例(数据库生成的ID)
	 * @throws EappException KEY重复
	 */
	public Action addAction(String actionKey, String name, String logoURL, String tips, String description) throws EappException;

	/**
	 * 查看动作详细信息
	 * @param id 动作ID
	 * @return 动作信息实例
	 */
	public Action getAction(String id);

	/**
	 * 修改动作实例
	 * @param action 动作实例
	 * @throws EappException 动作KEY重复
	 */
	public Action modifyAction(String actionID, String name, String logoURL, String tips, String description) throws EappException ;

	/**
	 * 列出所有模块没有绑定的动作
	 * @param moduleId
	 * @return
	 */
	public List<Action> getExcludeActions(String moduleId,String name);
	
	/**
	 * 通过模块取得动作
	 * @param moduleID 模块ID
	 * @return
	 */
	public List<Action> getActionsByModuleID(String moduleID);

	public ListPage<Action> queryActions(ActionQueryParameters aqp);
	

}
