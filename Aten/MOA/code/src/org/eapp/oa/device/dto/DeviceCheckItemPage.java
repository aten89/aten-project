package org.eapp.oa.device.dto;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.device.hbean.DeviceClass;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;


public class DeviceCheckItemPage {
	private ListPage<DeviceClass> listPage;


	public DeviceCheckItemPage(){}
	
	public DeviceCheckItemPage(ListPage<DeviceClass> listPage) {
		this.listPage = listPage;
	}
	
	/**
	 *
	 * 返回格式
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1"/>
	 * 	<content total-count="1">
	 * 		<document id="信息单号">
	 * 			<device-class-id>标题</device-class-id>	
	 * 			<device-class-name>起草时间</device-class-name>		
	 * 			<device-check-item-str>起草人</device-check-item-str>	
	 * 		</document>
	 * 	</content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (listPage == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", Long.toString(listPage.getTotalCount()));
		contentEle.addAttribute("page-size", Long.toString(listPage.getCurrentPageSize()));
		contentEle.addAttribute("page-count", Long.toString(listPage.getTotalPageCount()));
		contentEle.addAttribute("current-page", Long.toString(listPage.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", Boolean.toString(listPage.hasPreviousPage()));
		contentEle.addAttribute("next-page", Boolean.toString(listPage.hasNextPage()));

		List<DeviceClass> data = new ArrayList<DeviceClass>();
		data = listPage.getDataList();
		if (data == null || data.size() < 1) {
			return doc;
		}
		Element eleDocument = null;
		for (DeviceClass dc : data) {
			eleDocument = contentEle.addElement("document");
			eleDocument.addAttribute("id", DataFormatUtil.noNullValue(dc.getId()));
			eleDocument.addElement("device-type-name").setText(DataFormatUtil.noNullValue(dc.getDeviceTypeName()));
			eleDocument.addElement("device-class-name").setText(DataFormatUtil.noNullValue(dc.getName()));
			eleDocument.addElement("device-check-item-str").setText(DataFormatUtil.noNullValue(dc.getDeviceCheckItemStr()));
			eleDocument.addElement("device-check-item-remark").setText(DataFormatUtil.noNullValue(dc.getDeviceCheckItemRemark()));
		}
		return doc;
	}
	
}
