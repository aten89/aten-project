package org.eapp.oa.kb.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.kb.hbean.KnowledgeLog;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;


public class KnowledgeLogPage {
	
	private ListPage<KnowledgeLog> knowledgeLogPage;
	
	public KnowledgeLogPage(ListPage<KnowledgeLog> knowledgeLogPage){
		this.knowledgeLogPage = knowledgeLogPage;
	}
	
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (knowledgeLogPage == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", String.valueOf(knowledgeLogPage.getTotalCount()) );
		contentEle.addAttribute("page-size", String.valueOf(knowledgeLogPage.getCurrentPageSize()) );
		contentEle.addAttribute("page-count", String.valueOf(knowledgeLogPage.getTotalPageCount()) );
		contentEle.addAttribute("current-page", String.valueOf(knowledgeLogPage.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", String.valueOf(knowledgeLogPage.hasPreviousPage()));
		contentEle.addAttribute("next-page", String.valueOf(knowledgeLogPage.hasNextPage()) );
		
		List<KnowledgeLog>  list = knowledgeLogPage.getDataList();
		if(list!= null && list.size()>0){
			Element ele = null;
			for (KnowledgeLog obj : list) {
				ele = contentEle.addElement("knowledgeLog");				
				ele.addAttribute("id", obj.getId());
				ele.addElement("userid").setText(DataFormatUtil.noNullValue(obj.getUserid()));
				ele.addElement("type").setText(DataFormatUtil.noNullValue(obj.getType()));
				ele.addElement("knowledgeid").setText(DataFormatUtil.noNullValue(obj.getKnowledgeid()));
				ele.addElement("knowledgetitle").setText(DataFormatUtil.noNullValue(obj.getKnowledgetitle()));
				ele.addElement("operatetime").setText(DataFormatUtil.noNullValue(obj.getOperatetime(), SysConstants.FULL_TIME_PATTERN));
				ele.addElement("userName").setText(DataFormatUtil.noNullValue(obj.getUserName()));
			}
		}
		return doc;
	}		
}
