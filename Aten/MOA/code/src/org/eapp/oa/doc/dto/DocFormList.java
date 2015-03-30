package org.eapp.oa.doc.dto;

import java.util.Collection;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.doc.hbean.DocForm;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;

public class DocFormList {
	private Collection<DocForm> docForms;



	public Collection<DocForm> getDocForms() {
		return docForms;
	}
	public void setDocForms(Collection<DocForm> docForms) {
		this.docForms = docForms;
	}
	public DocFormList(){}
	public DocFormList(Collection<DocForm> docForms) {
		super();
		this.docForms = docForms;
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
		if (docForms == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", Integer.toString(docForms.size()));
		Element proEle = null;
		for (DocForm r : docForms) {
			proEle = contentEle.addElement("document");				
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
		
			proEle.addElement("subject").setText(DataFormatUtil.noNullValue(r.getSubject()));
			proEle.addElement("draft-date").setText(DataFormatUtil.noNullValue(r.getDraftDate(),SysConstants.STANDARD_TIME_PATTERN));
			proEle.addElement("drafts-man").setText(r.getDraftsmanName());
			proEle.addElement("groupname").setText(DataFormatUtil.noNullValue(r.getGroupName()));		
			proEle.addElement("doc-class").addCDATA(DataFormatUtil.noNullValue(r.getDocClassName()));
			
		}
		return doc;
	}
	
}
