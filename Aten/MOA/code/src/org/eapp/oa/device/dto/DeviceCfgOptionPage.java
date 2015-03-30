/**
 * 
 */
package org.eapp.oa.device.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.device.hbean.DeviceProperty;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;


/**
 * 
 * @author zsy
 * @version Feb 25, 2009
 */
public class DeviceCfgOptionPage {
	
	private ListPage<DeviceProperty> devicePropertyPage;	
	
	public DeviceCfgOptionPage(ListPage<DeviceProperty> devicePropertyPage){
		this.devicePropertyPage = devicePropertyPage;
	}

	/**
	 *
	 * 返回格式
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1"/>
	 * 	<content total-count="1" page-size="0" page-count="1" current-page="1" previous-page="false" next-page="false">
	 * 		<propertyList id="">
	 * 			<deviceProperty-id>属性 编号</reimbursement-id>
	 *			<property-name>属性名称</property-name>
	 * 			<property-remark>备注</property-remark>
	 * 		</propertyList>
	 * 	</content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (devicePropertyPage == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", String.valueOf(devicePropertyPage.getTotalCount()) );
		contentEle.addAttribute("page-size", String.valueOf(devicePropertyPage.getCurrentPageSize()) );
		contentEle.addAttribute("page-count", String.valueOf(devicePropertyPage.getTotalPageCount()) );
		contentEle.addAttribute("current-page", String.valueOf(devicePropertyPage.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", String.valueOf(devicePropertyPage.hasPreviousPage()));
		contentEle.addAttribute("next-page", String.valueOf(devicePropertyPage.hasNextPage()) );		
		List<DeviceProperty>  propertyLists = devicePropertyPage.getDataList();
		if(propertyLists != null && propertyLists.size() > 0){
			Element propertyListEle = null;
			for (DeviceProperty ol : propertyLists) {
				propertyListEle = contentEle.addElement("device_property");				
				propertyListEle.addAttribute("id", ol.getId());
				propertyListEle.addElement("property-name").setText(DataFormatUtil.noNullValue(ol.getPropertyName()));
				propertyListEle.addElement("property-remark").setText(DataFormatUtil.noNullValue(ol.getRemark()));
				
			}
		}
		return doc;
	}		
	
}
