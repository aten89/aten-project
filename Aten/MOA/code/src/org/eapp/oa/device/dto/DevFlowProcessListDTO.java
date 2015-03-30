package org.eapp.oa.device.dto;

import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;

import org.eapp.oa.system.config.SysConstants;

public class DevFlowProcessListDTO {
	private Collection<DevFlowProcessFormDTO> devFlowProcessForms;


	public Collection<DevFlowProcessFormDTO> getDevFlowProcessForms() {
		return devFlowProcessForms;
	}

	public void setDevFlowProcessForms(
			Collection<DevFlowProcessFormDTO> devFlowProcessForms) {
		this.devFlowProcessForms = devFlowProcessForms;
	}

	public DevFlowProcessListDTO(){}
	
	public DevFlowProcessListDTO(Collection<DevFlowProcessFormDTO> devFlowProcessForms) {
		super();
		this.devFlowProcessForms = devFlowProcessForms;
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
		if (devFlowProcessForms == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		Element proEle = null;
		for (DevFlowProcessFormDTO r : devFlowProcessForms) {
			proEle = contentEle.addElement("document");				
			proEle.addAttribute("id", r.getId());
			proEle.addElement("operator").setText(DataFormatUtil.noNullValue(r.getOperator()));		
			proEle.addElement("operator-display-name").setText(DataFormatUtil.noNullValue(r.getOperatorDisplayName()));	
			proEle.addElement("operator-group-name").setText(DataFormatUtil.noNullValue(r.getOperatorGroupName()));		
			proEle.addElement("opt-date").setText(DataFormatUtil.noNullValue(r.getOperateDate(),SysConstants.STANDARD_DATE_PATTERN));		
			proEle.addElement("opt-type").setText(DataFormatUtil.noNullValue(r.getOperateType()));
			proEle.addElement("opt-type-display-name").setText(DataFormatUtil.noNullValue(r.getOperateTypeDisplayName()));
			proEle.addElement("remark").setText(DataFormatUtil.noNullValue(r.getRemark()));		
			proEle.addElement("ref-form-id").setText(DataFormatUtil.noNullValue(r.getRefFormID()));
			proEle.addElement("form-no").setText(DataFormatUtil.noNullValue(r.getFormNO()));
			
		}
		return doc;
	}
	
}
