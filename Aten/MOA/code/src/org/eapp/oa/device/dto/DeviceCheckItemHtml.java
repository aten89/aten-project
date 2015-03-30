package org.eapp.oa.device.dto;

import java.util.List;

import org.eapp.oa.device.hbean.DeviceCheckItem;
import org.eapp.oa.system.dto.HTMLOptionsDTO;

/**
 * 
 * @author jxs
 */
public class DeviceCheckItemHtml extends HTMLOptionsDTO {
	private List<DeviceCheckItem> deviceCheckItems;
	
	
	public DeviceCheckItemHtml(List<DeviceCheckItem> deviceCheckItems) {
		this.deviceCheckItems = deviceCheckItems;
	}
	
	
	
	public String toString() {
		StringBuffer out = new StringBuffer();
		if (deviceCheckItems == null || deviceCheckItems.size() < 1) {
			return out.toString();
		}
		for (DeviceCheckItem d : deviceCheckItems) {
			out.append(createOption(d.getId(), d.getItemName()));
		}
		return out.toString();
	}
	
	
}
