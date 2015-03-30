package org.eapp.oa.device.dto;

import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.device.hbean.DeviceCfgItem;
import org.eapp.oa.system.dto.HTMLOptionsDTO;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public class DeviceCfgItemXml extends HTMLOptionsDTO{
	private Collection<DeviceCfgItem> deviceCfgItems;

	public DeviceCfgItemXml(Collection<DeviceCfgItem> deviceCfgItems) {
		this.deviceCfgItems = deviceCfgItems;
	}
	
	/**
	 *生成设备属性的XML信息
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 *   <message code="1" /> 
	 *   <content>
	 * 	 	<device-property id="">id</device-property>
	 *      <item-name>设备属性名称</item-name>
	 *   </content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		root.addElement("message").addAttribute("code", "1");
		if (deviceCfgItems == null || deviceCfgItems.size() < 1) {
			return doc;
		}
		Element contentEle = root.addElement("content");
	
		Element proEle = null;
		for (DeviceCfgItem f : deviceCfgItems) {
			proEle = contentEle.addElement("config-items");				
			proEle.addAttribute("id", f.getId());
			if(f.getDeviceProperty()!=null){
				proEle.addElement("item-name").setText(DataFormatUtil.noNullValue(f.getDeviceProperty().getPropertyName()));
				proEle.addElement("item-id").setText(DataFormatUtil.noNullValue(f.getDeviceProperty().getId()));
				
			}
			proEle.addElement("config-id").setText(DataFormatUtil.noNullValue(f.getId()));
		}
		return doc;
	}
	
	public String toString() {
		StringBuffer out = new StringBuffer();
		if (deviceCfgItems == null || deviceCfgItems.size() < 1) {
			return out.toString();
		}
		for (DeviceCfgItem d : deviceCfgItems) {
			if(d.getDeviceProperty()!=null){
				out.append(createOption(d.getId(), d.getDeviceProperty().getPropertyName()));
			}
			
		}
		return out.toString();
	}
}
