package org.eapp.oa.info.dto;

import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.info.hbean.Information;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;

public class InformationPage {
	private ListPage<Information> listpage;

	/**
	 * @return the listpage
	 */
	public ListPage<Information> getListpage() {
		return listpage;
	}

	/**
	 * @param listpage the listpage to set
	 */
	public void setListpage(ListPage<Information> listpage) {
		this.listpage = listpage;
	}
	public InformationPage(){}
	public InformationPage(ListPage<Information> listpage) {
		super();
		this.listpage = listpage;
	}
	
	/**
	 *
	 * 返回格式
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1"/>
	 * 	<content total-count="1" page-size="0" page-count="1" current-page="1" previous-page="false" next-page="false">
	 * 		<information id="">
	 *			<subject>标题</subject>
	 * 			<info-layout>板块</info-layout>
	 * 			<info-class>信息分类</info-class>
	 * 			<public-date>发布时间</public-date>
	 * 			<drafts-man>起草人</drafts-man>
	 *   		<info-property>状态</info-property>
	 * 		</information>
	 * 	</content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (listpage == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", String.valueOf(listpage.getTotalCount()) );
		contentEle.addAttribute("page-size", String.valueOf(listpage.getCurrentPageSize()) );
		contentEle.addAttribute("page-count", String.valueOf(listpage.getTotalPageCount()) );
		contentEle.addAttribute("current-page", String.valueOf(listpage.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", String.valueOf(listpage.hasPreviousPage()));
		contentEle.addAttribute("next-page", String.valueOf(listpage.hasNextPage()) );		
		List<Information>  informationLists = listpage.getDataList();
		if(informationLists != null && informationLists.size() > 0){
			Element informationEle = null;
			Date now = new Date(System.currentTimeMillis() - 2*24*3600000);
			for (Information ol : informationLists) {
				informationEle = contentEle.addElement("information");				
				informationEle.addAttribute("id", ol.getId());
				informationEle.addElement("subject").setText(DataFormatUtil.noNullValue(ol.getSubject()));
				informationEle.addElement("info-layout").setText(DataFormatUtil.noNullValue(ol.getInfoLayout()));
				informationEle.addElement("info-class").setText(DataFormatUtil.noNullValue(ol.getInfoClass()));
				informationEle.addElement("public-date").setText(DataFormatUtil.noNullValue(ol.getPublicDate(),SysConstants.STANDARD_TIME_PATTERN));
				informationEle.addElement("short-public-date").setText(DataFormatUtil.noNullValue(ol.getPublicDate(),SysConstants.STANDARD_DATE_PATTERN));
				informationEle.addElement("drafts-man").setText(ol.getDraftsManName());
				informationEle.addElement("group-name").setText(DataFormatUtil.noNullValue(ol.getGroupName()));
				informationEle.addElement("info-property").setText(DataFormatUtil.noNullValue(ol.getInfoProperty()));
				informationEle.addElement("info-property-name").setText(DataFormatUtil.noNullValue(ol.getInfoPropertyName()));
				informationEle.addElement("is-new").setText(DataFormatUtil.noNullValue(now.before(ol.getPublicDate())));
			}
		}
		return doc;
	}		
	
}
