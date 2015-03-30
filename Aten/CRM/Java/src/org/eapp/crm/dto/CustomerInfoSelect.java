/**
 * 
 */
package org.eapp.crm.dto;

import java.util.List;

/**
 * @author zsy
 * @version Nov 26, 2008
 */
public class CustomerInfoSelect extends HTMLSelect {

	private List<AutoCompleteData> customerInfos;
	
	public CustomerInfoSelect(List<AutoCompleteData> customerInfos) {
		this.customerInfos = customerInfos;
	}
	
	/**
	 * 有默认值的情况
	 * 格式：
	 * <div isselected="true">8a283d001a52213f011a5255d2f60002**dsfdf</div>
	 * <div>zxt00001**datacenter</div>
	 */
	public String toString() {
		StringBuffer out = new StringBuffer();
		if (customerInfos == null || customerInfos.size() < 1) {
			return out.toString();
		}
		for (AutoCompleteData c : customerInfos) {
			out.append(createOption(c.getCode(), c.getName()));
		}
		return out.toString();
	}
		
}
