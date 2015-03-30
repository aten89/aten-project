package org.eapp.oa.device.dto;

import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.device.blo.IDeviceBiz;
import org.eapp.oa.device.hbean.Device;
import org.eapp.oa.device.hbean.PurchaseDevice;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;


public class PurchaseDeviceListDTO {
	private Collection<PurchaseDevice> purchaseDevices;
	public Collection<PurchaseDevice> getPurchaseDevices() {
		return purchaseDevices;
	}
	public void setPurchaseDevices(Collection<PurchaseDevice> purchaseDevices) {
		this.purchaseDevices = purchaseDevices;
	}
	public PurchaseDeviceListDTO(){}
	
	public PurchaseDeviceListDTO(Collection<PurchaseDevice> purchaseDevices) {
		super();
		this.purchaseDevices = purchaseDevices;
	}
	
	/**
	 *
	 * 返回格式
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1"/>
	 * 	<content total-count="1">
	 * 		<document id="信息单号">
	 * 			<subject>标题</subject>	
	 * 			<draft-date>起草时间</draft-date>		
	 * 			<drafts-man>起草人</drafts-man>	
	 * 			<groupName>起草部门</groupName>	
	 * 			<doc-class>类别</info-class>
	 * 		</document>
	 * 	</content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (purchaseDevices == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		Element proEle = null;
		for (PurchaseDevice r : purchaseDevices) {
			proEle = contentEle.addElement("document");				
			proEle.addAttribute("id", r.getId());
			IDeviceBiz deviceBiz = (IDeviceBiz)SpringHelper.getSpringContext().getBean("deviceBiz");
			Device device = deviceBiz.getDeviceById(r.getDeviceID());
			proEle.addElement("device-no").setText(DataFormatUtil.noNullValue(device.getDeviceNO()));//设备编号
			proEle.addElement("device-name").setText(DataFormatUtil.noNullValue(device.getDeviceName()));//设备名称
		}
		return doc;
	}
	
}
