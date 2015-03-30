package org.eapp.dao;

import java.util.List;
import java.util.Map;

import org.eapp.hbean.Module;


public interface IModuleDAO extends IBaseHibernateDAO {
	/**
	 * 根据模块ID取得按升序排列的子模块列表
	 * @param fatherId 模块ID
	 * @return 子模块列表
	 */
	public List<Module> getChildModulesByASC(String fatherId);

	/**
	 * 取得子系统下直属模块的数目
	 * @param subSystemId 子系统ID
	 * @return 直属模块的数目
	 */
	public int getModuleCountBySubSystemID(String subSystemId);
	
	public int getNextDisplayOrder(String subSystemID, String moduleID);
	
	public int getNextTreeLevel(String subSystemID, String moduleID);
	
	public void updateOrder(String parentModuleID, Integer theOrder);
	/**
	 * 取得模块的子模块数目
	 * @param moduleId 模块ID
	 * @return 子模块数目
	 */
	public int getChildModulesCountByModuleID(String moduleId);

	/**
	 * 将子模块中大于startIndex的子模块的displayOrder都减1
	 * @param moduleId 子模块ID
	 * @param startIndex 起始序列
	 */
//	public void resortChildModuleByModule(String systemid, String moduleId, int startIndex);

	/**
	 * 保存模块的显示顺序
	 * @param idSorts 模块顺序
	 */
	public void updateModuleSort(Map<String,Integer> idSorts);
	
	/**
	 * 加载模块实例
	 * @param id 模块ID
	 * @return 模块信息实例
	 */
	public Module findById(java.lang.String id);
	
	public List<Module> findByParentModuleID(String parentModuleID);
	
	public List<Module> findBySubSystemID(String subSystemID);

	/**
	 * @author zsy
	 * 通过系统ID取得该系统的一级模块列表
	 * @param systemID 系统ID
	 * @return
	 */
	public List<Module> findRootModulesBySysID(String systemID);
	
	/**
	 * 通过子系统ID查找所有模块
	 * @param systemID
	 * @return
	 */
	public List<Module> findModulesBySysID(String systemID);
	
	/**
	 * @author zsy
	 * 通过角色ID列表取得指定系统下可以进入的所有模块
	 * @param roleIDs 角色列表
	 * @param systemID 系统ID
	 * @return
	 */
	public List<Module> findModuleByRoleIDs(List<String> roleIDs, String systemID);
	
	/**
	 * 通过系统ID取处引用的模块
	 * @param systemID
	 * @return
	 */
	public List<Module> findQuoteModules(String systemID);
	
	/**
	 * 检测子系统下的KEY是否重复
	 * @param subSystemId
	 * @param key
	 * @return
	 */
	public boolean isKeyRepeat(String subSystemId,String key);
	
	public List<Module> queryModuleByName(String subSystemID,String name);

	public List<Module> findModulesBySystemIDModuleKey(String systemID, String moduleKey);

	/**
	 * 判断名称是否重复
	 * @param subSystemId 系统ID
	 * @param parentModuleId 父模块ID
	 * @param name 模块名称
	 * @return
	 */
	public boolean isNameRepeat(String subSystemId, String parentModuleId, String name);
	
	/**
	 * 通过模块KEY取得系统下的模块
	 * @param subSystemId
	 * @param moduleKey
	 * @return
	 */
	public Module findModuleByModuleKey(String subSystemId,String moduleKey);
}