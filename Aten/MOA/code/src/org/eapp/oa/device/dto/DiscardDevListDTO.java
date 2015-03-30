package org.eapp.oa.device.dto;

import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.device.hbean.DevDiscardForm;
import org.eapp.oa.device.hbean.DiscardDevList;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;

import org.eapp.oa.system.config.SysConstants;


public class DiscardDevListDTO {
	private Collection<DiscardDevList> discardDevListDTOs;
	
	public DiscardDevListDTO(Collection<DiscardDevList> discardDevListDTOs){
		this.discardDevListDTOs = discardDevListDTOs;
	}
	
	/**
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 *   <message code="1" /> 
	 *   <content>
	 *     <document id="ID">
	 *       <device-id>设备id</device-id> 
	 *       <repairForm-no>维修单号</repairForm-no>
	 *       <account-id></account-id>
	 *       <account-name>设备名称</account-name>
	 *       <group-name>设备型号</group-name>
	 *       <status>设备描述</status>
	 *       <create-time>当前状态</create-time>
	 *       <budget-money>购买时间</budget-money>
	 *       <reason>购买方式</reason>
	 *       <remark>购买方式</remark>
	 *     </repairForm-config>
	 *   </content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (discardDevListDTOs == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		Element proEle = null;
		for (DiscardDevList discardDevList : discardDevListDTOs) {
			proEle = contentEle.addElement("document");		
			DevDiscardForm form = discardDevList.getDevDiscardForm();
			proEle.addAttribute("id", discardDevList.getId());
			proEle.addElement("applicant-display-name").setText(DataFormatUtil.noNullValue(form == null ? null : form.getApplicantDisplayName()));		
			proEle.addElement("apply-group-name").setText(DataFormatUtil.noNullValue(form == null ? null : form.getApplyGroupName()));
			proEle.addElement("apply-date").setText(DataFormatUtil.noNullValue(form == null ? null : form.getApplyDate(),SysConstants.STANDARD_DATE_PATTERN));		
			proEle.addElement("discard-type-display-name").setText(DataFormatUtil.noNullValue(discardDevList == null ? null : discardDevList.getDiscardTypeDisplayName()));
			proEle.addElement("deal-type-display-name").setText(DataFormatUtil.noNullValue(discardDevList == null ? null : discardDevList.getDealTypeDisplayName()));	
			proEle.addElement("ref-form-id").setText(DataFormatUtil.noNullValue(form == null ? null : form.getId()));//关联的报废单
		}
		return doc;
	}
}
