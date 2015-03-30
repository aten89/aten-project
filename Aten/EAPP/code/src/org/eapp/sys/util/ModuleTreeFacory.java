package org.eapp.sys.util;

import java.util.List;
import java.util.Stack;
import java.util.TreeMap;

import org.eapp.comobj.ModuleMenuTree;
import org.eapp.comobj.ModuleMenuTree.ModuleMenu;
import org.eapp.hbean.Module;
import org.eapp.sys.config.SysConstants;


/**
 * 模块的排序树模型
 * @author zsy
 * @version
 */
public class ModuleTreeFacory {

	public static ModuleMenuTree findSubModuleMenuTree(ModuleMenuTree tree, String moduleKey) {
		if (tree == null || moduleKey == null) {
			return null;
		}
		ModuleMenu m = tree.getModuleMenu();
		if (moduleKey.equals(m.getModuleKey())) {
			return tree;
		}
		TreeMap<ModuleMenu, ModuleMenuTree> subm = tree.getSubModuleMenu();
		for (ModuleMenuTree stree : subm.values()) {
			ModuleMenuTree t = findSubModuleMenuTree(stree, moduleKey);
			if (t != null) {
				return t;
			}
		}
		return null;
	}
	public static ModuleMenuTree createModueMenuTree(List<Module> modules) {
		return createModueMenuTree(modules, null);
	}
	
	public static ModuleMenuTree createModueMenuTree(List<Module> modules, String excludeModuleKey) {
		ModuleMenuTree tree = ModuleMenuTree.createRoot();
		if (modules != null) {
			for (Module m : modules) {
				addModule(m, tree, excludeModuleKey);
			}
		}
		return tree;
	}
	
	/**
	 * 添加模块，从当前结点往下查找，找到正确的结点再添加进去
	 * @param m
	 */
	private static void addToTree(Module m, ModuleMenuTree tree) {
		if (m == null) {
			return;
		}
		String parentModuleId = null;
		if(m.getParentModule() != null){
			parentModuleId = m.getParentModule().getModuleID();
		}
		if(parentModuleId == null || parentModuleId.equals(tree.getModuleMenu().getModuleID())) {
			//加入的模块是根结点或父模块ID等于当前结点的模块ID，直接把该模块加入下级Map中，
			String url = m.getUrl();
			if (url != null && !url.startsWith("/") && !url.toLowerCase().startsWith("http://") 
					&& !url.toLowerCase().startsWith("https://")
					&& !m.getSubSystem().getSubSystemID().equals(SysConstants.EAPP_SUBSYSTEM_ID)) {
				url = m.getSubSystem().getBasePath() + url;
			}
			ModuleMenu mm = new ModuleMenu(m.getModuleID(), m.getModuleKey(), m.getName(), 
					url, m.getDisplayOrder());
			if (!tree.getSubModuleMenu().keySet().contains(mm)) {
				tree.getSubModuleMenu().put(mm, new ModuleMenuTree(mm));
			}
		} else {
			//否则添加到下级进行判断
			for(ModuleMenuTree mm : tree.getSubModuleMenu().values()) {
				addToTree(m, mm);
			}
		}
	}
	
	private static void addModule(Module m, ModuleMenuTree tree, String excludeModuleKey) {
		if (m.getModuleKey().equals(excludeModuleKey)) {//排除模块ID
			return;
		}
		Stack<Module> s = new Stack<Module>();
		s.push(m);
		Module pm = m.getParentModule();
		while (pm != null) {
			if (pm.getModuleKey().equals(excludeModuleKey)) {//排除模块ID
				return;
			}
			s.push(pm);
			pm = pm.getParentModule();
		}

		while(!s.empty()) {
			pm = s.pop();
			addToTree(pm, tree);
		}
	}
	
	
	
	public static void addQuoteModules(ModuleMenuTree tree, List<Module> modules, Module quoteModule) {
		addQuoteModules(tree, modules, quoteModule, null);
	}
	
	public static void addQuoteModules(ModuleMenuTree tree, List<Module> modules, Module quoteModule, String excludeModuleKey) {
		if (modules != null) {
			for (Module m : modules) {
				addQuoteModule(m, tree, quoteModule, excludeModuleKey);
			}
		}
	}
	
	private static void addQuoteModule(Module m, ModuleMenuTree tree, Module qm, String excludeModuleKey) {
		if (m.getModuleKey().equals(excludeModuleKey)) {//排除模块ID
			return;
		}
		String quotedId = qm.getQuoteModule().getModuleID();//被引用模块ID
		boolean findQuote = m.getModuleID().equals(quotedId);//添加的模块是否为被引用模块
		Stack<Module> s = new Stack<Module>();
		s.push(m);
		Module pm = null;
		if (findQuote) {
			//添加的模块是被引用模块，将父级指向引用模块的父级
			m.setParentModule(qm.getParentModule());
			m.setDisplayOrder(qm.getDisplayOrder());//使用原模块的排序号
			m.setName(qm.getName());//使用原模块的名称
		}
		pm = m.getParentModule();
		
		while (pm != null) {
			if (pm.getModuleKey().equals(excludeModuleKey)) {//排除模块ID
				return;
			}
			s.push(pm);
			if (!findQuote) {//未找到第一个被引用模块
				findQuote = pm.getModuleID().equals(quotedId);//父模块是否为被引用模块
				if (findQuote) {
					//父模块是被引用模块，将父级指向引用模块的父级
					pm.setParentModule(qm.getParentModule());
					pm.setDisplayOrder(qm.getDisplayOrder());//使用原模块的排序号
					pm.setName(qm.getName());//使用原模块的名称
				}
			}
			pm = pm.getParentModule();
		}
		if (findQuote) {//找到被引用模块，将此链添加到树中
			while(!s.empty()) {
				pm = s.pop();
				addToTree(pm, tree);
			}
		}
	}
	
}
