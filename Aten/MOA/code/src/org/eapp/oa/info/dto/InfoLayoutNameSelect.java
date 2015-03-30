package org.eapp.oa.info.dto;

import java.util.List;

import org.eapp.oa.system.dto.HTMLOptionsDTO;

public class InfoLayoutNameSelect extends HTMLOptionsDTO {
	
	private List<String> infoLayouts;
	
	public InfoLayoutNameSelect(List<String> infoLayouts){
		this.infoLayouts = infoLayouts;
	}

	/**
	 * 格式：
	 * <div>8a283d001a52213f011a5255d2f60002**dsfdf</div>
	 * <div>zxt00001**datacenter</div>
	 */
	public String toString() {
		
		StringBuffer out = new StringBuffer();
		if (infoLayouts == null || infoLayouts.size() < 1) {
			return out.toString();
		}
		for (String r : infoLayouts) {
			out.append(createOption(r,r));
		}
		return out.toString();
	}
}
