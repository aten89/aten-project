/**
 * 
 */
package org.eapp.blo.imp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eapp.blo.IModuleBiz;
import org.eapp.comobj.ModuleMenuTree;
import org.eapp.dao.IActionDAO;
import org.eapp.dao.IModuleActionDAO;
import org.eapp.dao.IModuleDAO;
import org.eapp.dao.ISubSystemDAO;
import org.eapp.exception.EappException;
import org.eapp.hbean.Action;
import org.eapp.hbean.Module;
import org.eapp.hbean.ModuleAction;
import org.eapp.hbean.SubSystem;
import org.eapp.sys.config.SysConstants;
import org.eapp.sys.util.ModuleTreeFacory;


/**
 * </p>
 * 
 * @author surefan
 * @version
 */
public class ModuleBiz implements IModuleBiz {

	private IModuleDAO moduleDAO;
	private IActionDAO actionDAO;
	private IModuleActionDAO moduleActionDAO;
	private ISubSystemDAO subSystemDAO;
	//固定一级菜单（切换系统时些菜单都会存在），些菜单及下级菜单都不要绑定任何权限
	private String fixedMenuKey;

	public void setSubSystemDAO(ISubSystemDAO subSystemDAO) {
		this.subSystemDAO = subSystemDAO;
	}
	public void setModuleDAO(IModuleDAO moduleDAO) {
		this.moduleDAO = moduleDAO;
	}

	/**
	 * @param moduleActionDAO
	 *            the moduleActionDAO to set
	 */
	public void setModuleActionDAO(IModuleActionDAO moduleActionDAO) {
		this.moduleActionDAO = moduleActionDAO;
	}

	/**
	 * @param actionDAO
	 *            the actionDAO to set
	 */
	public void setActionDAO(IActionDAO actionDAO) {
		this.actionDAO = actionDAO;
	}

	public void setFixedMenuKey(String fixedMenuKey) {
		this.fixedMenuKey = fixedMenuKey;
	}

	@Override
	public int getModulesCountByModule(String moduleId) {
		return moduleDAO.getChildModulesCountByModuleID(moduleId);
	}

	@Override
	public List<Module> getChildModulesOrder(String fatherid, int treeLevelDifferential) {
		if (treeLevelDifferential < 1) {
			throw new IllegalArgumentException("层次差不能大等于1");
		}
		Module parentModule = moduleDAO.findById(fatherid);
		if (parentModule == null) {
			throw new IllegalArgumentException("参数异常:数据库不存在该模块ID");
		}
		int level = parentModule.getTreeLevel();
		List<Module> modules = moduleDAO.getChildModulesByASC(fatherid);
		initLazy(modules, level + treeLevelDifferential);
		return modules;
	}
	
	@Override
	public Module getModule(String id) {
		if (StringUtils.isBlank(id)) {
			throw new IllegalArgumentException("模块ID不能为空");
		}
		Module module = moduleDAO.findById(id);
		if (module == null) {
			throw new IllegalArgumentException("参数异常:数据库不存在该模块ID");
		}
		Module qm = module.getQuoteModule();
		if (qm != null) {
			String path = getQuoteModulePath(qm);
			path = qm.getSubSystem().getName() + ">>" + path;
			module.setQuoteModulePath(path);
		}
		return module;
		
	}
	
	private String getQuoteModulePath(Module m) {
		if (m == null) {
			return null;
		}
		String tem  = getQuoteModulePath(m.getParentModule());
		String path = m.getName();
		if (tem != null) {
			path = tem + ">>" + path;
		}
		return path;
	}

	@Override
	public Module addModule(String parentModuleId, String subSystemId, String moduleKey, String name, String url,
			String description, String quoteModuleId) throws EappException {
		if (StringUtils.isBlank(subSystemId) || StringUtils.isBlank(name) || StringUtils.isBlank(moduleKey)) {
			throw new IllegalArgumentException();
		}
		if (moduleDAO.isKeyRepeat(subSystemId, moduleKey)) {
			throw new EappException("模块代码不能重复");
		}
		if (isNameRepeat(subSystemId, parentModuleId, name)) {
			throw new EappException("模块名称不能重复");
		}
		
		Module module = new Module();
		SubSystem subSystem = subSystemDAO.findById(subSystemId);
		if(subSystem == null){
			throw new IllegalArgumentException("数据库中不存在该子系统ID"+subSystemId);
		}
		module.setSubSystem(subSystem);
		//父模块
		if(StringUtils.isNotBlank(parentModuleId)){
			Module parentModule = moduleDAO.findById(parentModuleId);
			if(parentModule == null){
				throw new IllegalArgumentException("数据库中不存在该父模块ID"+parentModuleId);
			}
			module.setParentModule(parentModule);
		}
		
		//引用模块
		if(StringUtils.isNotBlank(quoteModuleId)){
			Module quoteModule = moduleDAO.findById(quoteModuleId);
			if(quoteModule == null){
				throw new IllegalArgumentException("数据库中不存在该模块ID"+quoteModuleId);
			}
			module.setQuoteModule(quoteModule);
		}

		module.setModuleKey(moduleKey);
		module.setName(name);
		module.setUrl(url);
		module.setDescription(description);
		module.setDisplayOrder(new Integer(moduleDAO.getNextDisplayOrder(subSystemId, parentModuleId)));
		module.setTreeLevel(new Integer(moduleDAO.getNextTreeLevel(subSystemId, parentModuleId)));
		moduleDAO.save(module);
		return module;
	}

	@Override
	public Module deleteModule(String moduleId) throws EappException {
		if (StringUtils.isBlank(moduleId)) {
			throw new IllegalArgumentException("id不能为空");
		}
		Module module = moduleDAO.findById(moduleId);
		if (module == null) {
			throw new IllegalArgumentException("参数异常:数据库不存在该模块ID," + moduleId);
		}
		// 模块不能绑定子模块
		if (module.getChildModules().size() > 0) {
			throw new EappException("该模块绑定了子模块，请先删除子模块");
		}
		
		//删除已绑定的模块动作
		if (module.getModuleActions().size() > 0) {
			for (ModuleAction ma : module.getModuleActions()) {
				moduleActionDAO.delete(ma);
			}
			module.getModuleActions().clear();
		}
		if (module.getQuotedModules().size() > 0) {
			for (Module m : module.getQuotedModules()) {
				m.setQuoteModule(null);
			}
			module.getQuotedModules().clear();
		}
		
//		String fatherId = module.getParentModule() == null ? null : module.getParentModule().getModuleID();
//		String subsystemID = module.getSubSystem().getSubSystemID();
//		Integer startIndex = module.getDisplayOrder();
		moduleDAO.delete(module);
	//	moduleDAO.resortChildModuleByModule(subsystemID,fatherId, startIndex);
		return module;
	}

	@Override
	public Module modifyModule(String moduleId, String parentModuleId, String subSystemId, String moduleKey,
			String name, String url, String description, String quoteModuleId) throws EappException {
		if (StringUtils.isBlank(moduleId) || StringUtils.isBlank(subSystemId) || StringUtils.isBlank(name) || StringUtils.isBlank(moduleKey)) {
			throw new IllegalArgumentException();
		}
		if (StringUtils.isBlank(parentModuleId)) {
			parentModuleId = null;
		}
		Module module = moduleDAO.findById(moduleId);
		if (module == null) {
			throw new IllegalArgumentException("参数异常:数据库不存在该模块ID," + moduleId);
		}
		
		if (!module.getModuleKey().equals(moduleKey) && moduleDAO.isKeyRepeat(subSystemId, moduleKey)) {
			throw new EappException("模块代码不能重复");
		}
		if(!module.getName().equals(name) && isNameRepeat(subSystemId, parentModuleId, name)){
			throw new EappException("模块名称不能重复");
		}
		
		SubSystem subSystem = subSystemDAO.findById(subSystemId);
		if(subSystem == null){
			throw new IllegalArgumentException("数据库中不存在该子系统ID"+subSystemId);
		}
		module.setSubSystem(subSystem);
		
		//引用模块
		if(StringUtils.isNotBlank(quoteModuleId)){
			Module quoteModule = moduleDAO.findById(quoteModuleId);
			if(quoteModule == null){
				throw new IllegalArgumentException("数据库中不存在该模块ID"+quoteModuleId);
			}
			//引用模块不能为父模块或子模块
			checkIsParent(module, quoteModule.getModuleID());
			checkIsChild(module.getChildModules(), quoteModule.getModuleID());
			module.setQuoteModule(quoteModule);
		} else {
			module.setQuoteModule(null);
		}
		
		//父ID有改动时
		String oldParentModuleID = module.getParentModule() == null ? null : module.getParentModule().getModuleID();
		if ((parentModuleId != null && !parentModuleId.equals(oldParentModuleID)) || 
				(parentModuleId == null && oldParentModuleID != null)) {
			//父结点不能为本身结点
			if (module.getModuleID().equals(parentModuleId)) {
				throw new EappException("父模块不能为本身模块");
			}
			
			Module pm = null;
			if (StringUtils.isNotBlank(parentModuleId)) {
				pm = moduleDAO.findById(parentModuleId);
				if (pm == null) {
					throw new EappException("模块不存在");
				}
			}
			module.setParentModule(pm);
			
			//如果父结点是本身的子结点，刚抛异常
			checkIsChild(module.getChildModules(), parentModuleId);
			//修改原父级下的的DisplayOrder
			moduleDAO.updateOrder(oldParentModuleID, module.getDisplayOrder());
			module.setDisplayOrder(new Integer(moduleDAO.getNextDisplayOrder(subSystemId, parentModuleId)));
			//修改setTreeLevel
			int level = moduleDAO.getNextTreeLevel(subSystemId, parentModuleId);
			module.setTreeLevel(new Integer(level));
			updateSubGroupTreeLevel(module.getChildModules(), level, 50);
			
			
		}
		module.setModuleKey(moduleKey);
		module.setName(name);
		module.setUrl(url);
		module.setDescription(description);
		moduleDAO.saveOrUpdate(module);
		return module;
	}
	
	/**
	 * 递归更新子群组的层级
	 * @param groups 群组集合
	 * @param parentLevel 父层级
	 */
	private void updateSubGroupTreeLevel(Set<Module> modules, int parentLevel, int breakTime) {
		if (modules == null || modules.isEmpty() || breakTime-- < 0) {
			return;
		}
		int level = parentLevel + 1;
		for (Module g : modules) {
			g.setTreeLevel(new Integer(level));
			updateSubGroupTreeLevel(g.getChildModules(),level, breakTime);
		}
	}
	
	private void checkIsParent(Module module, String quoteModuleId) throws EappException {
		if (module == null) {
			return;
		}
		if (module.getModuleID().equals(quoteModuleId)) {
			throw new EappException("引用模块不能为该模块的父模块");
		}
		checkIsParent(module.getParentModule(), quoteModuleId);
	}
	
	private void checkIsChild(Set<Module> modules, String quoteModuleId) throws EappException {
		if (modules == null || modules.isEmpty()) {
			return;
		}
		for (Module m : modules) {
			if (m.getModuleID().equals(quoteModuleId)) {
				throw new EappException("所选模块不能为该模块的子模块");
			}
			checkIsChild(m.getChildModules(), quoteModuleId);
		}
	}

	@Override
	public void modifyModuleOrder(Map<String, Integer> idAndOrder) {
		if (idAndOrder == null) {
			throw new IllegalArgumentException("参数不能为空");
		}
		moduleDAO.updateModuleSort(idAndOrder);

	}

	/**
	 * add by zhuoshiyao
	 */
	@Override
	public List<Module> getModulesBySysID(String systemID, boolean lazy, int level) {
		if (StringUtils.isBlank(systemID)) {
			return null;
		}

		List<Module> modules = moduleDAO.findRootModulesBySysID(systemID);
		if (!lazy) {
			initLazy(modules, level);
		}
		return modules;
	}
	
	private boolean isNameRepeat(String subSystemId, String parentModuleId, String name) {
		if (StringUtils.isBlank(subSystemId) || StringUtils.isBlank(name)) {
			return true;
		}
		return moduleDAO.isNameRepeat(subSystemId, parentModuleId, name);
	}

	/**
	 * 
	 * 立即加载多级模块的子模块
	 * 
	 * @param cols
	 *            模块集合
	 */
	private void initLazy(Collection<Module> cols, int level) {
		if (cols == null || cols.isEmpty()) {
			return;
		}
		for (Module m : cols) {
			if (level == 0 || m.getTreeLevel() < level) {
				m.getChildModules().size();//加载延迟内容
				initLazy(m.getChildModules(), level);
			}
		}
	}

	@Override
	public List<Module> getParentsByModule(String moduleID) {
		if (StringUtils.isBlank(moduleID)) {
			throw new IllegalArgumentException();
		}
		List<Module> parentModuleList = new ArrayList<Module>();
		Module module = moduleDAO.findById(moduleID);
		if (module == null) {
			throw new IllegalArgumentException("参数异常:数据库不存在该模块ID," + moduleID);
		}
		Module parentModule = module.getParentModule();
		while (parentModule != null) {
			parentModuleList.add(parentModule);
			parentModule = parentModule.getParentModule();
		}
		Collections.reverse(parentModuleList);
		return parentModuleList;
	}

	@Override
	public List<Module> getModuleByName(String subSystemID, String name) {
		return moduleDAO.queryModuleByName(subSystemID, name);
	}

	@Override
	public List<Module> getChildModules(String systemID, String moduleKey) {
		if (StringUtils.isBlank(systemID) || StringUtils.isBlank(moduleKey)) {
			throw new IllegalArgumentException();
		}
		return moduleDAO.findModulesBySystemIDModuleKey(systemID, moduleKey);
	}

	@Override
	public List<Module> getModuleTree(String systemID) {
		if (StringUtils.isBlank(systemID)) {
			throw new IllegalArgumentException("系统ID不能为空");
		}
		return moduleDAO.findRootModulesBySysID(systemID);
	}

	@Override
	public Module getParentModule(String systemID, String moduleKey) {
		if (StringUtils.isBlank(systemID) || StringUtils.isBlank(moduleKey)) {
			throw new IllegalArgumentException();
		}
		Module module = moduleDAO.findModuleByModuleKey(systemID, moduleKey);
		if (module == null) {
			throw new IllegalArgumentException("找不到相应的模块");
		}
		return module;
	}

	@Override
	public List<Module> getHasRightModules(List<String> roleIDs, String systemID) {
		if (roleIDs == null || roleIDs.size() == 0 || systemID == null) {
			return null;
		}
		return moduleDAO.findModuleByRoleIDs(roleIDs, systemID);
	}
	
	public ModuleMenuTree getHasRightModuleTree(List<String> roleIDs, String systemID) {
		return getHasRightModuleTree(roleIDs, systemID, fixedMenuKey);
	}
	
	private ModuleMenuTree getHasRightModuleTree(List<String> roleIDs, String systemID, String excludeModuleKey) {
		if (roleIDs == null || roleIDs.isEmpty() || StringUtils.isBlank(systemID)) {
			return null;
		}
		List<Module> rightModules = moduleDAO.findModuleByRoleIDs(roleIDs, systemID);
		ModuleMenuTree mmt = ModuleTreeFacory.createModueMenuTree(rightModules, excludeModuleKey);
		
		List<Module> quoteModules = moduleDAO.findQuoteModules(systemID);
		if (quoteModules != null && !quoteModules.isEmpty()) {
			Map<String, List<Module>> smMap = new HashMap<String, List<Module>>();
			smMap.put(systemID, rightModules);
			for (Module qm : quoteModules) {
				String sysId = qm.getQuoteModule().getSubSystem().getSubSystemID();//被引用系统ID
				rightModules = smMap.get(sysId);//缓存，引用同一系统多个模块时不要重复查
				if (rightModules == null) {
					rightModules = moduleDAO.findModuleByRoleIDs(roleIDs, sysId);//被引用系统有权限的模块
					smMap.put(sysId, rightModules);
				}
				ModuleTreeFacory.addQuoteModules(mmt, rightModules, qm, excludeModuleKey);//被引用系统有权限的模块合并到源树中
			}
			smMap.clear();
		}
		return mmt;
	}
	
	public ModuleMenuTree getFixedModuleTree(List<String> roleIDs) {
		//构建系统完整树
		ModuleMenuTree tree = getHasRightModuleTree(roleIDs, SysConstants.EAPP_SUBSYSTEM_ID, null);
		//查找子节点
		ModuleMenuTree fixedMenuNode = ModuleTreeFacory.findSubModuleMenuTree(tree, fixedMenuKey);
		if (fixedMenuNode == null) {
			return null;
		}
		//创建空的根节点
		ModuleMenuTree fixedMenuTree = ModuleMenuTree.createRoot();
		//将查到的子节点放入根节点
		fixedMenuTree.getSubModuleMenu().put(fixedMenuNode.getModuleMenu(), fixedMenuNode);
		return fixedMenuTree;
//		return ModuleTreeFacory.findSubModuleMenuTree(tree, fixedMenuKey);
//		
//		if (StringUtils.isBlank(fixedMenuKey)) {
//			return null;
//		}
//		Module module = moduleDAO.findModuleByModuleKey(SysConstants.EAPP_SUBSYSTEM_ID, fixedMenuKey);
//		if (module == null) {
//			return null;
//		}
//		List<Module> modules = new ArrayList<Module>();
//		List<Module> quoteModules = new ArrayList<Module>();
//		getChildModules(module, modules, quoteModules);
//		ModuleMenuTree mmt = ModuleTreeFacory.createModueMenuTree(modules);
//		if (!quoteModules.isEmpty()) {
//			Map<String, List<Module>> smMap = new HashMap<String, List<Module>>();
//			for (Module qm : quoteModules) {
//				String sysId = qm.getQuoteModule().getSubSystem().getSubSystemID();//被引用系统ID
//				List<Module> rightModules = smMap.get(sysId);//缓存，引用同一系统多个模块时不要重复查
//				if (rightModules == null) {
//					rightModules = moduleDAO.findModuleByRoleIDs(roleIDs, sysId);//被引用系统有权限的模块
//					smMap.put(sysId, rightModules);
//				}
//				ModuleTreeFacory.addQuoteModules(mmt, rightModules, qm);//被引用系统有权限的模块合并到源树中
//			}
//			smMap.clear();
//		}
//		return mmt;
	}
	
//	/**
//	 * 装载所有子模块
//	 * @param m
//	 * @param modules
//	 */
//	private void getChildModules(Module m, List<Module> modules, List<Module> quoteModules) {
//		if (m == null || m.getChildModules().isEmpty()) {
//			return;
//		}
//		for (Module sm : m.getChildModules()) {
//			if (sm.getQuoteModule() != null) {
//				quoteModules.add(sm);
//			} else {
//				modules.add(sm);
//			}
//			getChildModules(sm, modules, quoteModules);
//		}
//	}

	@Override
	public List<Module> getModulesBySubSystem(String systemID) {
		if (StringUtils.isBlank(systemID)) {
			throw new IllegalArgumentException("子系统Id不能为空");
		}
		return moduleDAO.findBySubSystemID(systemID);
	}
	
	@Override
	public Module txBandAction(String moduleID, String[] actionIDs) {
		if (StringUtils.isBlank(moduleID)) {
			throw new IllegalArgumentException("模块ID不能为空或NULL");
		}
		
		List<String> actionList = null;
		if (actionIDs != null && actionIDs.length > 0) {
			actionList = new ArrayList<String>(Arrays.asList(actionIDs));
		} else {
			actionList = new ArrayList<String>();
		}
		Module module = moduleDAO.findById(moduleID);
		if(module == null){
			throw new IllegalArgumentException("参数异常:在数据库中无法找到给模块ID"+moduleID);
		}
		// 删除模块下的删除的动作
		Set<ModuleAction> moduleActions = module.getModuleActions();
		String actionID = null;
		for (ModuleAction ma : moduleActions) {
			if (ma.getAction() == null) {
				continue;
			}
			actionID = ma.getAction().getActionID();
			if (!actionList.contains(actionID)) {
				moduleActionDAO.delete(ma);
			} else {
				actionList.remove(actionID);
			}
		}
		//保存
		for (String actionId : actionList) {
			if (StringUtils.isBlank(actionId)) {
				continue;
			}
			ModuleAction moduleAction = new ModuleAction();
			Action action_ = actionDAO.findById(actionId);
			Module module_ = moduleDAO.findById(moduleID);
			moduleAction.setAction(action_);
			moduleAction.setModule(module_);
			moduleAction.setModuleKey(module.getModuleKey());
			moduleAction.setActionKey(action_.getActionKey());
			moduleAction.setIsValid(Boolean.TRUE);
			moduleAction.setIsRPC(Boolean.FALSE);
			moduleAction.setIsHTTP(Boolean.TRUE);//默认开启HTTP
			moduleActionDAO.save(moduleAction);
		}
		return module;
	}
}
