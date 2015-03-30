/**
 * 
 */
package org.eapp.blo.imp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eapp.blo.IModuleActionBiz;
import org.eapp.dao.IModuleActionDAO;
import org.eapp.dao.param.ModuleActionQueryParameters;
import org.eapp.hbean.Action;
import org.eapp.hbean.ModuleAction;
import org.eapp.util.hibernate.ListPage;


/**
 * @version 
 */
public class ModuleActionBiz implements IModuleActionBiz {
	private IModuleActionDAO moduleActionDAO;

	/**
	 * @return the moduleActionDAO
	 */
	public IModuleActionDAO getModuleActionDAO() {
		return moduleActionDAO;
	}

	/**
	 * @param moduleActionDAO the moduleActionDAO to set
	 */
	public void setModuleActionDAO(IModuleActionDAO moduleActionDAO) {
		this.moduleActionDAO = moduleActionDAO;
	}

	@Override
	public List<Action> getActionsByModuleID(String moduleID) {
		List<ModuleAction> moduleActions = moduleActionDAO.findByModuleID(moduleID);
		List<Action> actions = new ArrayList<Action>();
		for(ModuleAction moduleAction:moduleActions){
			Action action = moduleAction.getAction();
			actions.add(action);
		}
		return actions;
	}
	
	
	
	
	//add by zsy
	@Override
	public ListPage<ModuleAction> queryModuleAction(ModuleActionQueryParameters qp) {
		ListPage<ModuleAction> page = moduleActionDAO.queryModuleAction(qp);
		return page;
	}

	@Override
	public void addOptions(String moduleID, String[] validIDs, String[] rpcIDs, String[] httpIDs) {
		if (StringUtils.isBlank(moduleID)) {
			throw new IllegalArgumentException("动作ID不能为空或NULL");
		}
		moduleActionDAO.setValid(moduleID,validIDs);
		moduleActionDAO.setRPC(moduleID, rpcIDs);
		moduleActionDAO.setHTTP(moduleID, httpIDs);
	}
	
//	@Override
//	public List<ActionKey> csGetModuleActionByRoleIDs(List<String> roleIDs,String moduleID) {
//		if (roleIDs == null || roleIDs.size() <= 0 || moduleID == null) {
//			return null;
//		}
//		return moduleActionDAO.getModuleActionByRoleIDs(roleIDs, moduleID);
//	}
//	
//	@Override
//	public List<ActionKey> csGetModuleActionByServiceIDs(String subSystemID ,String moduleKey , List<String> serviceIDs) {
//		if (serviceIDs == null || serviceIDs.size() <= 0 || moduleKey == null) {
//			return null;
//		}
//		String moduleID = moduleActionDAO.getModuleID(subSystemID, moduleKey);
//		return moduleActionDAO.getModuleActionByServiceIDs(serviceIDs, moduleID);
//	}

	@Override
	public List<ModuleAction> getModuleActions(String[] moduleActionIDs) {
		if (moduleActionIDs == null || moduleActionIDs.length < 1) {
			return null;
		}
		List<ModuleAction> mas = new ArrayList<ModuleAction>(moduleActionIDs.length);
		ModuleAction ma = null;
		for (String maid : moduleActionIDs) {
			if (StringUtils.isBlank(maid)) {
				continue;
			}
			ma = moduleActionDAO.findById(maid);
			if (ma != null) {
				mas.add(ma);
			}
		}
		return mas;
	//	return moduleActionDAO.findByIDs(moduleActionIDs);
	}

}
