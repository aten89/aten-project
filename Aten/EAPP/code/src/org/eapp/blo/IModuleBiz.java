/**
 * 
 */
package org.eapp.blo;

import java.util.List;
import java.util.Map;

import org.eapp.comobj.ModuleMenuTree;
import org.eapp.exception.EappException;
import org.eapp.hbean.Module;


/**
 * @version
 */
public interface IModuleBiz {

	/**
	 * 获取模块下的子模块的数量
	 * @param moduleId 模块
	 * @return int 模块数目
	 */
	public int getModulesCountByModule(String moduleId );

	/**
	 * 根据模块ID获取模块信息
	 * @param id 模块ID
	 * @return Module 模块信息实例
	 */
	public Module getModule(String id);

	/**
	 * 删除模块，如果有子模块或绑定动作则无法删除，删除后所有在相同一父模块下子模块的显示顺序大于该模块的模块
	 * 的显示顺序都减去1（即该模块后的所有模块向前挪一位）
	 * @param moduleId 模块Id
	 * @return 
	 * @throws EappException 拥有子模块或绑定动作
	 */
	public Module deleteModule(String moduleId) throws EappException;

	/**
	 * 新增模块,对树层次及序列进行计算,树层次为父模块层次加1（如果无父模块则层次为1）,序列为父模块下子模块数加1
	 * @param parentModuleId 父模块Id
	 * @param subSystemId 子系统Id
	 * @param moduleKey 模块KEY
	 * @param name 模块名
	 * @param url 链接
	 * @param description 备注
	 * @return Module 保存后的Module实例,主要是获取生成的ID
	 * @throws EappException KEY值重复 
	 */
	public Module addModule(String parentModuleId, String subSystemId, String moduleKey, String name, String url,
			String description, String quoteModuleId) throws EappException;

	/**
	 * 修改模块信息,本身及所有子模块的树层次减该模块的层次差,
	 * @param moduleId 模块Id
	 * @param parentModuleId 子模块Id
	 * @param subSystemId 所属子系统Id
	 * @param moduleKey 模块KEY
	 * @param name 模块名
	 * @param url 链接
	 * @param description 备注
	 * @return 
	 * @return ResponseData 修改结果信息
	 * @throws EappException 模块的父模块为该模块的子模块,KEY值重复
	 */
	public Module modifyModule(String moduleId, String parentModuleId, String subSystemId, String moduleKey, String name,
			String url, String description, String quoteModuleId) throws EappException;

	/**
	 * 按升序顺序列出父模块下N级的子模块
	 * @param fatherid 父模块Id
	 * @param treeLevelDifferential n级别 
	 * @return List<Module> 子模块列表
	 */
	public List<Module> getChildModulesOrder(String fatherid ,int treeLevelDifferential);

	/**
	 * 保存模块顺序
	 * @param idAndOrder 模块ID相对应的顺序
	 */
	public void modifyModuleOrder(Map<String, Integer> idAndOrder);


	/**
	 * 
	 * 通过系统ID取得该系统的模块列表
	 * @param systemID 系统ID
	 * @param lazy 是否延迟加载所有子模块
	 * @param level 需要立即加载多少级的子模块
	 * @return
	 */
	public List<Module> getModulesBySysID(String systemID, boolean lazy,int level);
	
	/**
	 * @author zsy
	 * 通过角色ID列表取得指定系统下可以进入的所有模块
	 * 
	 * @param roleIDs 角色列表
	 * @param systemID 系统ID
	 * @return
	 */
	public List<Module> getHasRightModules(List<String> roleIDs, String systemID);
	
	/**
	 * 通过角色ID列表取得指定系统下有权限的模块权，包含引用模块
	 * @param roleIDs
	 * @param systemID
	 * @return
	 */
	public ModuleMenuTree getHasRightModuleTree(List<String> roleIDs, String systemID);
	
	/**
	 * 取得所有下级模块
	 * @param moduleKey
	 * @param systemID
	 * @return
	 */
	public ModuleMenuTree getFixedModuleTree(List<String> roleIDs);
	
	/** add
	 * 根据模块名称模糊匹配查询
	 * @param name
	 * @return
	 */
	public List<Module> getModuleByName(String subSystemID,String name);
	
	/**add
	 * 列出模块的父模块直到一级父模块
	 * @param moduleID
	 * @return
	 */
	public List<Module> getParentsByModule(String moduleID);

	public List<Module> getChildModules(String systemID, String moduleKey);

	public List<Module> getModuleTree(String systemID);

	public Module getParentModule(String systemID, String moduleKey);
	
	/**
	 * 获取子系统的模块列表
	 * @param systemID 子系统ID
	 * @return
	 */
	public List<Module> getModulesBySubSystem(String systemID);
	
	/**
	 * 将选择的动作绑定在模块上
	 * @param moduleID 模块ID
	 * @param actionIDs 选择的动作ID
	 */
	public Module txBandAction(String moduleID, String[] actionIDs);
}
