package org.eapp.oa.device.dto;

import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.device.hbean.DeviceClassAssign;
import org.eapp.oa.device.hbean.DeviceClassAssignDetail;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;


/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public class DeviceClassAssignXml {
	private Collection<DeviceClassAssign> deviceClassAssigns;

	public DeviceClassAssignXml(Collection<DeviceClassAssign> deviceClassAssigns) {
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
		for (DeviceClassAssign f : deviceClassAssigns) {
			proEle = contentEle.addElement("class-assign");				
			proEle.addAttribute("id", f.getId());
			proEle.addElement("areaCode").setText(DataFormatUtil.noNullValue(f.getAreaCode()));
			proEle.addElement("areaName").setText(DataFormatUtil.noNullValue(f.getAreaName()));
			proEle.addElement("deviceType").setText(DataFormatUtil.noNullValue(f.getDeviceType()));
			proEle.addElement("deviceTypeName").setText(DataFormatUtil.noNullValue(f.getDeviceTypeName()));
			proEle.addElement("class-names").setText(DataFormatUtil.noNullValue(f.getClassNames()));
			proEle.addElement("class-ids").setText(DataFormatUtil.noNullValue(f.getClassIds()));
			proEle.addElement("assign-date").setText(DataFormatUtil.noNullValue(f.getConfigTime(), "yyyy-MM-dd HH:mm"));
			proEle.addElement("remark").setText(DataFormatUtil.noNullValue(f.getRemark()));
			if(f.getAssignEdValue()!=null){
				String[] assigns = f.getAssignEdValue().split(",");
				for (String string : assigns) {
					if(string.equals(String.valueOf(DeviceClassAssignDetail.ASSIGN_MANAGER))){
						proEle.addElement("mamager-flag").setText(DataFormatUtil.noNullValue(DeviceClassAssignDetail.ASSIGN_MANAGER));
					}else{
						proEle.addElement("select-flag").setText(DataFormatUtil.noNullValue(DeviceClassAssignDetail.ASSIGN_SELECT));
					}
				}
			}
			
			
		}
		return doc;
	}
}
