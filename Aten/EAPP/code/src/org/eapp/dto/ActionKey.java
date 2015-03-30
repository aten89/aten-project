package org.eapp.dto;

/**
 * 
 * @author zsy
 * @version
 */
public class ActionKey implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5691528857027431983L;
	private String actionID;
	private String actionKey;
	
	public ActionKey() {
		
	}
	
	public ActionKey(String actionID, String actionKey) {
		this.actionID = actionID;
		this.actionKey = actionKey;
	}
	/**
	 * @return the actionID
	 */
	public String getActionID() {
		return actionID;
	}
	/**
	 * @param actionID the actionID to set
	 */
	public void setActionID(String actionID) {
		this.actionID = actionID;
	}
	/**
	 * @return the actionKey
	 */
	public String getActionKey() {
		return actionKey;
	}
	/**
	 * @param actionKey the actionKey to set
	 */
	public void setActionKey(String actionKey) {
		this.actionKey = actionKey;
	}
}
