package org.eapp.oa.doc.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.doc.hbean.DocForm;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;

public class DocFormPage {
	private ListPage<DocForm> docFormListPage;

	/**
	 * @return the infoFormListPage
	 */
	public ListPage<DocForm> getDocFormListPage() {
		return docFormListPage;
	}

	/**
	 * @param infoFormListPage the infoFormListPage to set
	 */
	public void setDocFormListPage(ListPage<DocForm> docFormListPage) {
		this.docFormListPage = docFormListPage;
	}
	public DocFormPage(){}
	public DocFormPage(ListPage<DocForm> docFormListPage) {
		super();
		this.docFormListPage = docFormListPage;
	}
	
	/**
	 *
	 * 返回格式
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1"/>
	 * 	<content total-count="1" page-size="0" page-count="1" current-page="1" previous-page="false" next-page="false">
	 * 		<doc-form id="公文id">
	 * 			<subject>标题</subject>	
	 * 			<draft-date>起草时间</draft-date>		
	 * 			<drafts-man>起草人</drafts-man>
	 * 			<groupName>起草部门</groupName>
	 * 			<doc-class>公文分类</info-class>
	 * 			<arch-date>	归档时间 </arch-date>	
	 * 		</doc-form>
	 * 	</content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (docFormListPage == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", String.valueOf(docFormListPage.getTotalCount()) );
		contentEle.addAttribute("page-size", String.valueOf(docFormListPage.getCurrentPageSize()) );
		contentEle.addAttribute("page-count", String.valueOf(docFormListPage.getTotalPageCount()) );
		contentEle.addAttribute("current-page", String.valueOf(docFormListPage.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", String.valueOf(docFormListPage.hasPreviousPage()));
		contentEle.addAttribute("next-page", String.valueOf(docFormListPage.hasNextPage()) );
		
		List<DocForm>  docForms = docFormListPage.getDataList();
		if(docForms!=null && docForms.size()>0){
			Element reimburEle = null;
			for (DocForm r : docForms) {
				reimburEle = contentEle.addElement("doc-form");				
				reimburEle.addAttribute("id", r.getId());
				if (r.getTask() != null) {
					reimburEle.addElement("create-time").setText(DataFormatUtil.noNullValue(
							r.getTask().getCreateTime(), SysConstants.STANDARD_TIME_PATTERN));
					reimburEle.addElement("end-time").setText(DataFormatUtil.noNullValue(
							r.getTask().getEndTime(), SysConstants.STANDARD_TIME_PATTERN));
					reimburEle.addElement("task-name").setText(DataFormatUtil.noNullValue(
							r.getTask().getTaskName()));
					reimburEle.addElement("node-name").setText(DataFormatUtil.noNullValue(
							r.getTask().getNodeName()));
					//reimburEle.addElement("transactor").setText(DataFormatUtil.noNullValue(UserAccountTransform.getDisplayName( r.getTask().getTransactor())));
					reimburEle.addElement("transactor").setText(DataFormatUtil.noNullValue(r.getTask().getTransactor()));
				}
				if (r != null) {
					reimburEle.addElement("subject").setText(DataFormatUtil.noNullValue(r.getSubject()));
					reimburEle.addElement("draft-date").setText(DataFormatUtil.noNullValue(r.getDraftDate(),SysConstants.STANDARD_TIME_PATTERN));
					reimburEle.addElement("drafts-man").setText(DataFormatUtil.noNullValue(r.getDraftsmanName()));
					reimburEle.addElement("groupName").setText(DataFormatUtil.noNullValue(r.getGroupName()));
					reimburEle.addElement("doc-class").setText(DataFormatUtil.noNullValue(r.getDocClassName()));
					reimburEle.addElement("arch-date").setText(DataFormatUtil.noNullValue(r.getArchiveDate(),SysConstants.STANDARD_TIME_PATTERN));
					if (r.getPassed() != null) {
						reimburEle.addElement("passed").setText(r.getPassed() ? "通过" : "作废");
					}
				}
			}
		}
		return doc;
	}	
}
