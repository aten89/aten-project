package org.eapp.oa.device.dto;

import java.util.Date;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.device.hbean.Device;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;

import org.eapp.oa.system.config.SysConstants;


public class DeviceXml {
	Device device;
	
	public DeviceXml(Device device){
		this.device=device;
	}
	
	/**
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 *   <message code="1" /> 
	 *   <content total-count="1" page-size="0" page-count="1" current-page="1" previous-page="false" next-page="false">
	 *     <device-config dID="ID">
	 *       <device-type>设备类型</device-type> 
	 *       <device-NO>设备编号</device-NO>
	 *       <device-name>设备名称</device-name>
	 *       <device-model>设备型号</device-model>
	 *       <device-description>设备描述</device-description>
	 *       <device-nowState>当前状态</device-nowState>
	 *       <device-buyTime>购买时间</device-buyTime>
	 *       <device-buyType>购买方式</device-buyType>
	 *       <device-regTime>登记时间</device-regTime>
	 *       <device-remark>备注</device-remark>
	 *     </device-config>
	 *   </content>
	 * </root>
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (device == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");		
		Element contentEle = root.addElement("content");
		Element proEle = null;
		proEle = contentEle.addElement("device");
		proEle.addAttribute("id", device.getId());
		//DataDictionaryDTO classMap =SysCodeDictLoader.getInstance().getDeviceClass().get(device.getDeviceClass());
		//proEle.addElement("device-class").setText(DataFormatUtil.noNullValue(classMap==null ? "" : DataFormatUtil.noNullValue(classMap.getDictKey())));
//		proEle.addElement("device-no").setText(DataFormatUtil.noNullValue(device.getDeviceNO()));
		proEle.addElement("device-type").setText(DataFormatUtil.noNullValue(device.getDeviceType()));
		proEle.addElement("device-name").setText(DataFormatUtil.noNullValue(device.getDeviceName()));
		proEle.addElement("device-model").setText(DataFormatUtil.noNullValue(device.getDeviceModel()));
		proEle.addElement("reg-time").setText(DataFormatUtil.noNullValue(new Date(),SysConstants.STANDARD_DATE_PATTERN));
	//		proEle.addElement("device-buyType").setText(DataFormatUtil.noNullValue(d.getBuyType()));
		return doc;
	}
}
