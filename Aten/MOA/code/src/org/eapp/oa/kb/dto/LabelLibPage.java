package org.eapp.oa.kb.dto;

import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.kb.hbean.LabelLib;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;


public class LabelLibPage {
	
	
	ListPage<LabelLib> listPage;
	public LabelLibPage(ListPage<LabelLib> listPage){
		this.listPage=listPage;
	}
	public ListPage<LabelLib> getListPage() {
		return listPage;
	}
	public void setListPage(ListPage<LabelLib> listPage) {
		this.listPage = listPage;
	}

	public Document toDocument() {
		
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (listPage == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", Long.toString(listPage.getTotalCount()));
		contentEle.addAttribute("page-size", Long.toString(listPage.getCurrentPageSize()));
		contentEle.addAttribute("page-count", Long.toString(listPage.getTotalPageCount()));
		contentEle.addAttribute("current-page", Long.toString(listPage.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", Boolean.toString(listPage.hasPreviousPage()));
		contentEle.addAttribute("next-page", Boolean.toString(listPage.hasNextPage()));
		
		List<LabelLib> data = listPage.getDataList();
		if (data == null || data.size() < 1) {
			return doc;
		}
		Element proEle = null;
		for (LabelLib m : data) {
			proEle = contentEle.addElement("labelLib");		
			proEle.addAttribute("id", m.getId());
			proEle.addElement("name").setText(DataFormatUtil.noNullValue(m.getName()));
			proEle.addElement("count").setText(DataFormatUtil.noNullValue(m.getCount()));
			proEle.addElement("property").setText(DataFormatUtil.noNullValue(m.getPropertyName()));
			proEle.addElement("date").setText(DataFormatUtil.noNullValue(m.getCreateDate(), SysConstants.STANDARD_TIME_PATTERN));
		}
		return doc;
	}
}
