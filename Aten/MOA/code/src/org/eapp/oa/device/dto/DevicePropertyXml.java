package org.eapp.oa.device.dto;

import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.device.hbean.DeviceProperty;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;


/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public class DevicePropertyXml {
	private Collection<DeviceProperty> devicPropertys;

	public DevicePropertyXml(Collection<DeviceProperty> devicPropertys) {
		this.devicPropertys = devicPropertys;
	}
	
	/**
	 *生成设备属性的XML信息
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 *   <message code="1" /> 
	 *   <content>
	 * 	 	<device-property id="">id</device-property>
	 *      <property-name>设备属性名称</property-name>
	 *   </content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		root.addElement("message").addAttribute("code", "1");
		if (devicPropertys == null || devicPropertys.size() < 1) {
			return doc;
		}
		Element contentEle = root.addElement("content");
	
		Element proEle = null;
		for (DeviceProperty f : devicPropertys) {
			proEle = contentEle.addElement("device-property");				
			proEle.addAttribute("id", f.getId());
			proEle.addElement("property-name").setText(DataFormatUtil.noNullValue(f.getPropertyName()));
		}
		return doc;
	}
}
