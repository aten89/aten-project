/**
 * 
 */
package org.eapp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IModuleActionBiz;
import org.eapp.blo.IModuleBiz;
import org.eapp.blo.ISubSystemBiz;
import org.eapp.dto.ModuleTree;
import org.eapp.exception.EappException;
import org.eapp.hbean.Action;
import org.eapp.hbean.Module;
import org.eapp.hbean.SubSystem;

/**
 * 模块处理
 * @author surefan
 * @version
 */
public class ModuleAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7541780596574753392L;
	private static final Log log = LogFactory.getLog(ModuleAction.class);
	
	private IModuleBiz moduleBiz;
	private IModuleActionBiz moduleActionBiz;
	private ISubSystemBiz subSystemBiz;

	private String moduleID;
	private String moduleKey;
	private String name;
	private String url;
	private String description;
	private String parentModuleID;
	private String quoteModuleID;
	private String subSystemID;
	private String orderIDs;
	private String actionStr;
	
	private Module module;
	private List<Module> modules;
	private List<Action> actions;
	private String htmlValue;//输出HTML内容
	
	public void setModuleBiz(IModuleBiz moduleBiz) {
		this.moduleBiz = moduleBiz;
	}

	public void setModuleActionBiz(IModuleActionBiz moduleActionBiz) {
		this.moduleActionBiz = moduleActionBiz;
	}

	public void setSubSystemBiz(ISubSystemBiz subSystemBiz) {
		this.subSystemBiz = subSystemBiz;
	}

	public void setModuleID(String moduleID) {
		this.moduleID = moduleID;
	}

	public void setModuleKey(String moduleKey) {
		this.moduleKey = moduleKey;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setParentModuleID(String parentModuleID) {
		this.parentModuleID = parentModuleID;
	}

	public void setQuoteModuleID(String quoteModuleID) {
		this.quoteModuleID = quoteModuleID;
	}

	public void setSubSystemID(String subSystemID) {
		this.subSystemID = subSystemID;
	}

	public void setOrderIDs(String orderIDs) {
		this.orderIDs = orderIDs;
	}

	public void setActionStr(String actionStr) {
		this.actionStr = actionStr;
	}

	public Module getModule() {
		return module;
	}

	public List<Module> getModules() {
		return modules;
	}

	public List<Action> getActions() {
		return actions;
	}

	public String getHtmlValue() {
		return htmlValue;
	}

	/**
	 * 初始化列表页面
	 */
	public String initQueryPage() {
		return success();
	}
	
	public String queryModule() {
		if (StringUtils.isBlank(subSystemID) || StringUtils.isBlank(name)) {
			return error("参数不能为空");
		}
		
		try {
			modules = moduleBiz.getModuleByName(subSystemID, name);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 初始化新增页面
	 */
	public String initAdd() {
		return success();
	}
	
	/**
	 * 新增操作
	 */
	public String addModule() {
		if (StringUtils.isBlank(subSystemID) || StringUtils.isBlank(name)) {
			return error("参数不能为空");
		}
		try {
			Module module = moduleBiz.addModule(parentModuleID, subSystemID, moduleKey, name, url, description, quoteModuleID);
			ActionLogger.log(getRequest(), module.getModuleID(), module.toString());
			return success(module.getModuleID());
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 初始化修改页面
	 */
	public String initModify() {
		if (StringUtils.isBlank(moduleID)) {
			return error("参数不能为空");
		}
		module = moduleBiz.getModule(moduleID);
		return success();
	}
	
	/**
	 * 修改操作
	 */
	public String modifyModule() {
		if (StringUtils.isBlank(moduleID) || StringUtils.isBlank(subSystemID) || StringUtils.isBlank(name)) {
			return error("参数不能为空");
		}
		
		try {
			Module module = moduleBiz.modifyModule(moduleID, parentModuleID, subSystemID, moduleKey, name, url,
					description, quoteModuleID);
			ActionLogger.log(getRequest(), module.getModuleID(), module.toString());
			return success(module.getModuleID());
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 初始化查看页面
	 */
	public String viewModule() {
		if (StringUtils.isBlank(moduleID)) {
			return error("参数不能为空");
		}
		module = moduleBiz.getModule(moduleID);
		return success();
	}
	
	/**
	 * 删除操作
	 */
	public String deleteModule() {
		if (StringUtils.isBlank(moduleID)) {
			return error("参数不能为空");
		}
		try {
			Module module = moduleBiz.deleteModule(moduleID);
			ActionLogger.log(getRequest(), module.getModuleID(), module.toString());
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initOrder() {
		if (StringUtils.isBlank(moduleID)) {
			if (StringUtils.isBlank(subSystemID)) {
				return error("参数不能为空");
			}
			modules = moduleBiz.getModulesBySysID(subSystemID, true, 0);
		} else {
			modules = moduleBiz.getChildModulesOrder(moduleID, 2);
		}
		return success();
	}
	
	/**
	 * 排序操作
	 */
	public String orderModule() {
		if (StringUtils.isBlank(orderIDs)) {
			return error("参数不能为空");
		}
		Map<String, Integer> idAndOrder = new HashMap<String, Integer>();
		String[] moduleArray = orderIDs.split(",");
		for (int i = 0; i < moduleArray.length; i++) {
			idAndOrder.put(moduleArray[i], i + 1);
		}
		try {
			moduleBiz.modifyModuleOrder(idAndOrder);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 初始化绑定页面,传入moduleID,传出系统信息、已有的动作、未选的动作
	 */
	public String initBindAction() {
		if (StringUtils.isBlank(moduleID)) {
			return error("参数不能为空");
		}
		actions = moduleActionBiz.getActionsByModuleID(moduleID);
		return success();
	}
	
	/**
	 * 绑定操作
	 */
	public String bindAction() {
		if (StringUtils.isBlank(moduleID)) {
			return error("参数不能为空");
		}
		String[] actions = null;
		if (StringUtils.isNotBlank(actionStr)) {
			actions = actionStr.split(",");
		}
		try {
			Module module = moduleBiz.txBandAction(moduleID, actions);
			if (module != null) {
				StringBuffer sbf = new StringBuffer();
				if (module.getModuleActions() != null) {
					for (org.eapp.hbean.ModuleAction ma : module.getModuleActions()) {
						sbf.append(ma.toString()).append("\n");
					}
					
				}
				ActionLogger.log(getRequest(), module.getModuleID(), module.toString() + "\n绑定对象：\n" + sbf.toString());
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String loadParentPath() {
		if (StringUtils.isBlank(moduleID)) {
			return error("参数不能为空");
		}

		try {
			List<Module> modules = moduleBiz.getParentsByModule(moduleID);
			StringBuffer parentIDs = new StringBuffer();
			for (Module module : modules) {
				parentIDs.append(module.getModuleID());
				parentIDs.append("/");
			}
			parentIDs.append(moduleID);
			return success(parentIDs.toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	

	/**
	 * 选择子系统操作
	 */
	public String loadSubSystemTree() {
		if (StringUtils.isBlank(subSystemID)) {
			return error("参数不能为空");
		}

		try {
			SubSystem subSystem = subSystemBiz.getSubSystem(subSystemID);
			List<Module> moduleList = moduleBiz.getModulesBySysID(subSystemID, false, 2);
			ModuleTree subSystemTree = new ModuleTree();
			subSystemTree.setModules(moduleList);
			subSystemTree.setSubSystem(subSystem);
//			subSystemTree.setPath(loadPath);
			htmlValue = subSystemTree.toString();
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	/**
	 * 展开模块操作
	 */
	public String loadModuleTree() {
		if (StringUtils.isBlank(moduleID)) {
			return error("参数不能为空");
		}
		try {
			List<Module> moduleList = moduleBiz.getChildModulesOrder(moduleID, 2);
			ModuleTree moduleTree = new ModuleTree();
			moduleTree.setModules(moduleList);
			htmlValue = moduleTree.toString();
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

}
