package org.eapp.oa.doc.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.doc.hbean.DocNumber;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;


/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public class DocNumberList {

	private List<DocNumber> list;

	/**
	 * @return the list
	 */
	public List<DocNumber> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<DocNumber> list) {
		this.list = list;
	}
	
	public DocNumberList(List<DocNumber> list) {
		this.list = list;
	}
	
	/**
	 *生成费用的XML信息，格式如下：
	 *<?xml version="1.0" encoding="utf-8" ?> 
	 *<root>
	 *	<message code="1"/>
	 *	<content>
	 *     <doc-no id="id">
	 *       <docWord>文件字</docWord> 
	 *       <yearPrefix>年份前缀</yearPrefix> 
	 *       <currentYear>当年年份</currentYear>
	 *       <yearPostfix>年份后缀</yearPostfix>
	 *       <orderPrefix>流水号前缀</orderPrefix>
	 *       <orderNumber>流水号</orderNumber>
	 *       <orderPostfix>流水号后缀</orderPostfix>
	 *       <description>描述</description>
	 *    </doc-no>
	 *	</content>
	 *</root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		root.addElement("message").addAttribute("code", "1");
		if (list == null || list.size() < 1) {
			return doc;
		}
		Element contentEle = root.addElement("content");
		Element proEle = null;
		for (DocNumber dn : list){
			proEle = contentEle.addElement("doc-no");
			proEle.addAttribute("id", dn.getId());
			proEle.addElement("docWord").setText(DataFormatUtil.noNullValue(dn.getDocWord()));
			proEle.addElement("yearPrefix").addCDATA(DataFormatUtil.noNullValue(dn.getYearPrefix()));
			proEle.addElement("currentYear").setText(DataFormatUtil.noNullValue(dn.getCurrentYear()));
			proEle.addElement("yearPostfix").addCDATA(DataFormatUtil.noNullValue(dn.getYearPostfix()));
			proEle.addElement("orderPrefix").addCDATA(DataFormatUtil.noNullValue(dn.getOrderPrefix()));
			proEle.addElement("orderNumber").setText(DataFormatUtil.noNullValue(dn.getOrderNumber()));
			proEle.addElement("orderPostfix").addCDATA(DataFormatUtil.noNullValue(dn.getOrderPostfix()));
			proEle.addElement("description").addCDATA(DataFormatUtil.noNullValue(dn.getDescription()));
			proEle.addElement("has-template").setText(dn.getHeadTemplate() == null ? "无" : "有");
			proEle.addElement("docTmpUrl").addCDATA(DataFormatUtil.noNullValue(dn.getHeadTemplateUrl()));
		} 
		
		return doc;
	}
}
