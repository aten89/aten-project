package org.eapp.oa.device.dto;

import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.device.hbean.DevPurchaseList;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;

import org.eapp.oa.system.config.SysConstants;

public class DevPurchaseListDTO {
	private Collection<DevPurchaseList> devPurchaseList;
	public Collection<DevPurchaseList> getDevPurchaseList() {
		return devPurchaseList;
	}
	public void setDevPurchaseList(Collection<DevPurchaseList> devPurchaseList) {
		this.devPurchaseList = devPurchaseList;
	}
	public DevPurchaseListDTO(){}
	public DevPurchaseListDTO(Collection<DevPurchaseList> devPurchaseList) {
		super();
		this.devPurchaseList = devPurchaseList;
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
		if (devPurchaseList == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", Integer.toString(devPurchaseList.size()));
		Element proEle = null;
		for (DevPurchaseList r : devPurchaseList) {
			proEle = contentEle.addElement("document");				
			proEle.addAttribute("id", r.getId());
			proEle.addElement("applicant-display-name").setText(DataFormatUtil.noNullValue(r.getDevPurchaseForm().getApplicantDisplayName()));//申请人
			proEle.addElement("apply-group-name").setText(r.getDevPurchaseForm().getApplyGroupName());//所在部门
			proEle.addElement("use-date").setText(DataFormatUtil.noNullValue(r.getDevPurchaseForm().getArchiveDate(),SysConstants.STANDARD_DATE_PATTERN));//领用日期
			proEle.addElement("back-date").setText(DataFormatUtil.noNullValue(r.getReturnBackDate(),SysConstants.STANDARD_DATE_PATTERN));// 归还日期
//			proEle.addElement("purpose").setText(DataFormatUtil.noNullValue(r.getPurposeDisplayName()));//用途
			proEle.addElement("ref-form-id").setText(DataFormatUtil.noNullValue(r.getDevPurchaseForm().getId()));//关联的领用单
		}
		return doc;
	}
	
}
