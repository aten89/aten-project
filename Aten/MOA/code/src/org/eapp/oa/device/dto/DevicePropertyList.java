package org.eapp.oa.device.dto;

import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.device.hbean.DeviceProperty;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;


public class DevicePropertyList {

	private Collection<DeviceProperty> list;
	private StringBuffer sb;
	
	public DevicePropertyList(Collection<DeviceProperty> list,StringBuffer sb) {
		this.list = list;
		this.sb = sb ;
	}
	/**
	 * 构造DOM
	 *  Date    : Jul 7, 2009 8:53:35 PM
	 * @return
	 */
	public Document toDocument()
	{
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (list == null || list.size() < 1) {
			root.addElement("message").addAttribute("code", "0");
			if(sb !=null){
				Element deviceNo = root.addElement("deviceNo");
				deviceNo.addElement("no").addText(sb.toString());
			}
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		Element proEle = null;
		for (DeviceProperty d : list) {
			proEle = contentEle.addElement("device-property");
			proEle.addElement("name").addCDATA(DataFormatUtil.noNullValue(d.getPropertyName()));
//			proEle.addElement("value").addCDATA(DataFormatUtil.noNullValue(d.getPropertyValue()));
//			proEle.addElement("type").addText(DataFormatUtil.noNullValue(d.getPropertyType()));
//			proEle.addElement("description").addCDATA(DataFormatUtil.noNullValue(d.getDescription()));
		}
		if(sb !=null){
			Element deviceNo = root.addElement("deviceNo");
			deviceNo.addElement("no").addText(sb.toString());
		}
		
		return doc;
	}

}
