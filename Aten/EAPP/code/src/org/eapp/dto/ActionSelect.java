/**
 * 
 */
package org.eapp.dto;

import java.util.Collection;

import org.eapp.hbean.Action;


/**
 * 生成动作列表
 * @author zsy
 * @version 
 */
public class ActionSelect extends HTMLSelect {
	
	private Collection<Action> actions;
	
	public ActionSelect(Collection<Action> actions) {
		this.actions = actions;
	}

	/**
	 * @return the actions
	 */
	public Collection<Action> getActions() {
		return actions;
	}

	/**
	 * @param actions the actions to set
	 */
	public void setActions(Collection<Action> actions) {
		this.actions = actions;
	}

	/**
	 * 格式：
	 * <div>8a283d001a52213f011a5255d2f60002**dsfdf</div>
	 * <div>zxt00001**datacenter</div>
	 * <div>004**matural</div>
	 */
	public String toString() {
		StringBuffer out = new StringBuffer();
		if (actions == null || actions.size() < 1) {
			return out.toString();
		}
		for (Action g : actions) {
			out.append(createOption(g.getActionKey(), g.getName()));
		}
		return out.toString();
	}
}
