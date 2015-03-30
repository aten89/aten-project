package org.eapp.oa.device.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.device.hbean.DeviceClass;
import org.eapp.oa.system.dto.HTMLOptionsDTO;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;


/**
 * 
 * @author sds
 * @version Sep 1, 2009
 */
public class DeviceClassList extends HTMLOptionsDTO {
	private List<DeviceClass> deviceClasses;
	private ListPage<DeviceClass> listPage;
	public DeviceClassList(List<DeviceClass> deviceClasses) {
		this.deviceClasses = deviceClasses;
	}
	
	public DeviceClassList(ListPage<DeviceClass> listPage) {
		this.listPage = listPage;
	}
	
	/**
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 *   <message code="1" /> 
	 *   <content>
	 *     <device-class id="2">
	 *       <class-name>电脑</class-name>
	 *       <class-item-name>领用流程</class-item-name>
	 *       <class-remark>调拨流程</class-remark>
	 *     </device-class>
	 *   </content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		root.addElement("message").addAttribute("code", "1");
		if (deviceClasses == null || deviceClasses.size() < 1) {
			return doc;
		}
		Element contentEle = root.addElement("content");
	
		Element proEle = null;
		for (DeviceClass f : deviceClasses) {
			
			proEle = contentEle.addElement("device-class");				
			proEle.addAttribute("id", f.getId());
			proEle.addElement("class-item-name").setText( DataFormatUtil.noNullValue(f.getClassItemName()));
			proEle.addElement("class-name").setText(DataFormatUtil.noNullValue(f.getName()));
			proEle.addElement("class-remark").setText(DataFormatUtil.noNullValue(f.getRemark()));
			proEle.addElement("type-name").setText(DataFormatUtil.noNullValue(f.getDeviceTypeName()));
		}
		return doc;
	}
	
	public String toString() {
		StringBuffer out = new StringBuffer();
		if (deviceClasses == null || deviceClasses.size() < 1) {
			return out.toString();
		}
		for (DeviceClass d : deviceClasses) {
			out.append(createOption(d.getId(), d.getName()+"<input type='hidden' id='main_"+d.getId()+"' value='"+d.getMainDevFlag()+"'>"));
		}
		return out.toString();
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
	public Document toPageDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (listPage == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", String.valueOf(listPage.getTotalCount()) );
		contentEle.addAttribute("page-size", String.valueOf(listPage.getCurrentPageSize()) );
		contentEle.addAttribute("page-count", String.valueOf(listPage.getTotalPageCount()) );
		contentEle.addAttribute("current-page", String.valueOf(listPage.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", String.valueOf(listPage.hasPreviousPage()));
		contentEle.addAttribute("next-page", String.valueOf(listPage.hasNextPage()) );		
		List<DeviceClass>  deviceClassList = listPage.getDataList();
		if(deviceClassList != null && deviceClassList.size() > 0){
			Element proEle = null;
			for (DeviceClass d : deviceClassList) {
				proEle = contentEle.addElement("device-class");				
				proEle.addAttribute("id", d.getId());
				proEle.addElement("class-item-name").setText( DataFormatUtil.noNullValue(d.getClassItemName()));
				proEle.addElement("class-name").setText(DataFormatUtil.noNullValue(d.getName()));
				proEle.addElement("class-remark").setText(DataFormatUtil.noNullValue(d.getRemark()));
				proEle.addElement("type-name").setText(DataFormatUtil.noNullValue(d.getDeviceTypeName()));
				proEle.addElement("class-status").setText(DataFormatUtil.noNullValue(d.getStatus()));
				
			}
		}
		return doc;
	}		
}
