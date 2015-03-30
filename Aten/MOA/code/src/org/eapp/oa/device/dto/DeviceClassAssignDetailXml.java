package org.eapp.oa.device.dto;

import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.device.hbean.DeviceClassAssignDetail;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;


/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public class DeviceClassAssignDetailXml {
	private Collection<DeviceClassAssignDetail> deviceClassAssigns;

	public DeviceClassAssignDetailXml(Collection<DeviceClassAssignDetail> deviceClassAssigns) {
		this.deviceClassAssigns = deviceClassAssigns;
	}
	
	/**
	 *生成职务的XML信息
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 *   <message code="1" /> 
	 *   <content>
	 * 	 	<device-class-assign id="">id</device-class-assign>
	 *      <device-class>设备类别</device-class>
	 *      <type>授权类型</type>
	 *      <assign-key>授权键值</assign-key>
	 *      <flag>类别标志</flag>
	 *   </content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		root.addElement("message").addAttribute("code", "1");
		if (deviceClassAssigns == null || deviceClassAssigns.size() < 1) {
			return doc;
		}
		Element contentEle = root.addElement("content");
		Element proEle = null;
		for (DeviceClassAssignDetail f : deviceClassAssigns) {
			proEle = contentEle.addElement("assign");
			proEle.addAttribute("type",DataFormatUtil.noNullValue(f.getType()));
			proEle.setText(DataFormatUtil.noNullValue(f.getAssignKey()));
		}
		return doc;
	}
}
