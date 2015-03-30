/**
 * 
 */
package org.eapp.blo.imp;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eapp.blo.IActionBiz;
import org.eapp.dao.IActionDAO;
import org.eapp.dao.param.ActionQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.Action;
import org.eapp.hbean.ModuleAction;
import org.eapp.util.hibernate.ListPage;


/**
 * 动作业务逻辑层
 * @version 
 */
public class ActionBiz implements IActionBiz {
	
	private IActionDAO actionDAO;

	public void setActionDAO(IActionDAO actionDAO) {
		this.actionDAO = actionDAO;
	}
	
	public IActionDAO getActionDAO() {
		return actionDAO;
	}
	
	
	public Action addAction(String actionKey, String name, String logoURL, String tips,
			String description) throws EappException {
		
		if(StringUtils.isBlank(name)){
			throw new IllegalArgumentException("动作名不能为空");
		}
		if(isKeyRepeat(actionKey)){
			throw new EappException("KEY不能重复");
		}
		if(isNameRepeat(name)){
			throw new EappException("名称不能重复");
		}
		Action action = new Action();
		action.setName(name);
		action.setActionKey(actionKey);
		action.setLogoURL(logoURL);
		action.setTips(tips);
		action.setDescription(description);
		actionDAO.save(action);
		return action;
	}

	
	public Action deleteAction(String actionId) throws EappException {
		Action action = actionDAO.findById(actionId);
		if(action == null){
			throw new IllegalArgumentException("非法参数:动作ID在数据库中不存在");
		}
		Set<ModuleAction> moduleActions = action.getModuleActions();
		if(moduleActions.size() > 0){
			StringBuffer msg = new StringBuffer("该动作仍绑定模块,不能删除:");
			throw new EappException(msg.toString());
		}
		actionDAO.delete(action);
		return action;
	}

	
	public List<Action> getAllActions() {
		return actionDAO.findAll();
	}

	/* (non-Javadoc)
	 * @deprecated 考虑到数据库查询的性能问题，不推荐使用
	 */
	public List<Action> getExcludeActions(String moduleId,String name) {
		return actionDAO.findExcludeActions(moduleId,name);
	}

	public Action modifyAction(String actionID, String name, String logoURL, String tips, String description) throws EappException {
		if(StringUtils.isBlank(actionID) || StringUtils.isBlank(name)){
			throw new IllegalArgumentException();
		}
		
		Action actionSrc = actionDAO.findById(actionID);
		if(!actionSrc.getName().equals(name)&&isNameRepeat(name)){
			throw new EappException("动作名称不能重复");
		}
		actionSrc.setDescription(description);
		actionSrc.setLogoURL(logoURL);
		actionSrc.setName(name);
		actionSrc.setTips(tips);
		actionDAO.saveOrUpdate(actionSrc);
		return actionSrc;
	}

	public Action getAction(String id) {
		return actionDAO.findById(id);
	}

	private boolean isKeyRepeat(String key) {
		Action action= actionDAO.findByActionKey(key);
		if (action == null) {
			return false;
		} else {
			return true;
		}
	}
	private boolean isNameRepeat(String name) {
		List<Action> actions= actionDAO.findByName(name);
		if (actions == null || actions.size() == 0 ){
			return false;
		} else {
			return true;
		}
	}
	
	public List<Action> getActionsByModuleID(String moduleID) {
		if(StringUtils.isBlank(moduleID)){
			throw new IllegalArgumentException();
		}
		return actionDAO.findByModuleID(moduleID);
	}

	public ListPage<Action> queryActions(ActionQueryParameters aqp) {
		return actionDAO.queryActions(aqp);
	}
}
