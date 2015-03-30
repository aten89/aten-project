package org.eapp.oa.info.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import org.eapp.client.util.UsernameCache;
import org.eapp.oa.info.hbean.InfoForm;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;

public class InfoFormPage {
	private ListPage<InfoForm> infoFormListPage;

	/**
	 * @return the infoFormListPage
	 */
	public ListPage<InfoForm> getInfoFormListPage() {
		return infoFormListPage;
	}

	/**
	 * @param infoFormListPage the infoFormListPage to set
	 */
	public void setInfoFormListPage(ListPage<InfoForm> infoFormListPage) {
		this.infoFormListPage = infoFormListPage;
	}
	public InfoFormPage(){}
	public InfoFormPage(ListPage<InfoForm> infoFormListPage) {
		super();
		this.infoFormListPage = infoFormListPage;
	}
	
	/**
	 *
	 * 返回格式
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1"/>
	 * 	<content total-count="1" page-size="0" page-count="1" current-page="1" previous-page="false" next-page="false">
	 * 		<info-form id="信息审批单号">
	 * 			<subject>标题</subject>	
	 * 			<draft-date>起草时间</draft-date>		
	 * 			<drafts-man>起草人</drafts-man>
	 * 			<info-layout>板块</info-layout>
	 * 			<info-class>信息分类</info-class>
	 * 			<arch-date>	归档时间 </arch-date>	
	 * 		</info-form>
	 * 	</content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (infoFormListPage == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", String.valueOf(infoFormListPage.getTotalCount()) );
		contentEle.addAttribute("page-size", String.valueOf(infoFormListPage.getCurrentPageSize()) );
		contentEle.addAttribute("page-count", String.valueOf(infoFormListPage.getTotalPageCount()) );
		contentEle.addAttribute("current-page", String.valueOf(infoFormListPage.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", String.valueOf(infoFormListPage.hasPreviousPage()));
		contentEle.addAttribute("next-page", String.valueOf(infoFormListPage.hasNextPage()) );
		
		List<InfoForm>  infoForms = infoFormListPage.getDataList();
		if(infoForms!=null && infoForms.size()>0){
			Element reimburEle = null;
			for (InfoForm r : infoForms) {
				reimburEle = contentEle.addElement("info-form");				
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
					reimburEle.addElement("transactor").setText(DataFormatUtil.noNullValue(UsernameCache.getDisplayName( r.getTask().getTransactor())));
				}
				if (r.getInformation() != null) {
					reimburEle.addElement("subject").setText(DataFormatUtil.noNullValue(r.getInformation().getSubject()));
					reimburEle.addElement("draft-date").setText(DataFormatUtil.noNullValue(r.getInformation().getDraftDate(),SysConstants.STANDARD_TIME_PATTERN));
					reimburEle.addElement("drafts-man").setText(DataFormatUtil.noNullValue(r.getInformation().getDraftsManName()));
					reimburEle.addElement("info-layout").setText(DataFormatUtil.noNullValue(r.getInformation().getInfoLayout()));
					reimburEle.addElement("info-class").setText(DataFormatUtil.noNullValue(r.getInformation().getInfoClass()));
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
