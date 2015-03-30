package org.eapp.oa.info.dto;

import java.util.Collection;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.info.hbean.InfoForm;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;

public class InfoFormList {
	private Collection<InfoForm> infoForms;

	/**
	 * @return the infor
	 */
	public Collection<InfoForm> getInfor() {
		return infoForms;
	}

	/**
	 * @param infor the infor to set
	 */
	public void setInfor(Collection<InfoForm> infor) {
		this.infoForms = infor;
	}
	public InfoFormList(){}
	public InfoFormList(Collection<InfoForm> infor) {
		super();
		this.infoForms = infor;
	}
	
	/**
	 *
	 * 返回格式
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1"/>
	 * 	<content total-count="1">
	 * 		<information id="信息单号">
	 * 			<subject>标题</subject>	
	 * 			<draft-date>起草时间</draft-date>		
	 * 			<drafts-man>起草人</drafts-man>		
	 * 			<info-layout>板块</info-layout>
	 * 			<info-class>类别</info-class>
	 * 		</information>
	 * 	</content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (infoForms == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", Integer.toString(infoForms.size()));
		Element proEle = null;
		for (InfoForm r : infoForms) {
			proEle = contentEle.addElement("information");				
			proEle.addAttribute("id", r.getId());
			if (r.getTask() != null) {
				proEle.addElement("task-id").setText(DataFormatUtil.noNullValue(
						r.getTask().getId()));
				proEle.addElement("taskinstance-id").setText(DataFormatUtil.noNullValue(
						r.getTask().getTaskInstanceID()));
				proEle.addElement("view-flag").setText(DataFormatUtil.noNullValue(
						r.getTask().getViewFlag()));
				proEle.addElement("create-time").setText(DataFormatUtil.noNullValue(
						r.getTask().getCreateTime(), SysConstants.STANDARD_TIME_PATTERN));
				proEle.addElement("end-time").setText(DataFormatUtil.noNullValue(
						r.getTask().getEndTime(), SysConstants.STANDARD_TIME_PATTERN));
				proEle.addElement("task-name").setText(DataFormatUtil.noNullValue(
						r.getTask().getTaskName()));
				proEle.addElement("node-name").setText(DataFormatUtil.noNullValue(
						r.getTask().getNodeName()));
			}
			if (r.getInformation() != null) {
				proEle.addElement("subject").setText(DataFormatUtil.noNullValue(r.getInformation().getSubject()));
				proEle.addElement("draft-date").setText(DataFormatUtil.noNullValue(r.getInformation().getDraftDate(),SysConstants.STANDARD_TIME_PATTERN));
				proEle.addElement("drafts-man").setText(r.getInformation().getDraftsManName());
				proEle.addElement("info-layout").setText(DataFormatUtil.noNullValue(r.getInformation().getInfoLayout()));
				proEle.addElement("info-class").addCDATA(DataFormatUtil.noNullValue(r.getInformation().getInfoClass()));
			}
		}
		return doc;
	}
	
}
