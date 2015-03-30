package org.eapp.oa.device.dto;

import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.device.hbean.DevAllocateList;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;


public class DevAllocateListDTO {
	private Collection<DevAllocateList> devAllocateLists;

	public Collection<DevAllocateList> getDevAllocateLists() {
		return devAllocateLists;
	}
	public void setDevAllocateLists(Collection<DevAllocateList> devAllocateLists) {
		this.devAllocateLists = devAllocateLists;
	}
	public DevAllocateListDTO(){}
	public DevAllocateListDTO(Collection<DevAllocateList> devAllocateLists) {
		super();
		this.devAllocateLists = devAllocateLists;
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
		if (devAllocateLists == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		Element proEle = null;
		for (DevAllocateList r : devAllocateLists) {
			proEle = contentEle.addElement("document");				
			proEle.addAttribute("id", r.getId());
			proEle.addElement("move-type").setText(DataFormatUtil.noNullValue(r.getDevAllocateForm().getMoveType()));		
			proEle.addElement("move-type-display-name").setText(DataFormatUtil.noNullValue(r.getDevAllocateForm().getMoveTypeDisplayName()));		
			proEle.addElement("move-date").setText(DataFormatUtil.noNullValue(r.getDevAllocateForm().getMoveDate(),SysConstants.STANDARD_DATE_PATTERN));		
			proEle.addElement("in-user-name").setText(DataFormatUtil.noNullValue(r.getDevAllocateForm().getInApplicantDisplayName()));
			proEle.addElement("in-group").setText(DataFormatUtil.noNullValue(r.getDevAllocateForm().getInGroupName()));		
			proEle.addElement("out-user-name").setText(DataFormatUtil.noNullValue(r.getDevAllocateForm().getApplicantDisplayName()));
			proEle.addElement("out-group").setText(DataFormatUtil.noNullValue(r.getDevAllocateForm().getApplyGroupName()));
			proEle.addElement("area-name").setText(DataFormatUtil.noNullValue(r.getAreaName()));		
			proEle.addElement("area-name-bef").addCDATA(DataFormatUtil.noNullValue(r.getAreaNameBef()));
			proEle.addElement("ref-form-id").setText(DataFormatUtil.noNullValue(r.getDevAllocateForm() == null ? null : r.getDevAllocateForm().getId()));//关联的调拨单
		}
		return doc;
	}
	
}
