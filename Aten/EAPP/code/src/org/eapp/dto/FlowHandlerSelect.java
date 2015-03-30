/**
 * 
 */
package org.eapp.dto;

import java.util.List;

import org.eapp.hbean.FlowHandler;


/**
 * 流程HANDLER列表
 * @author zsy
 * @version 
 */
public class FlowHandlerSelect extends HTMLSelect {
	
	private List<FlowHandler> flowHandlers;
	
	public FlowHandlerSelect(List<FlowHandler> flowHandlers) {
		this.flowHandlers = flowHandlers;
	}

	/**
	 * 格式：
	 * <div>8a283d001a52213f011a5255d2f60002**dsfdf</div>
	 * <div>zxt00001**datacenter</div>
	 * <div>004**matural</div>
	 */
	public String toString() {
		StringBuffer out = new StringBuffer();
		if (flowHandlers == null || flowHandlers.isEmpty()) {
			return out.toString();
		}
		for (FlowHandler h : flowHandlers) {
			out.append(createOption(h.getHandlerClass(), h.getName()));
		}
		return out.toString();
	}
}
