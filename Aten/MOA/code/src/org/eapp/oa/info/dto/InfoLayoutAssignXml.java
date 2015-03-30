package org.eapp.oa.info.dto;

import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.info.hbean.InfoLayoutAssign;
import org.eapp.oa.system.util.web.XMLResponse;

public class InfoLayoutAssignXml {
	private Collection<InfoLayoutAssign> infoLayoutAssigns;

	public InfoLayoutAssignXml(Collection<InfoLayoutAssign> infoLayoutAssigns) {
		this.infoLayoutAssigns = infoLayoutAssigns;
	}
	
	/**
	 *生成职务的XML信息
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 *   <message code="1" /> 
	 *   <content>
	 * 	 	<assign type="0">部门经理</assign>
	 *   </content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		root.addElement("message").addAttribute("code", "1");
		if (infoLayoutAssigns == null || infoLayoutAssigns.size() < 1) {
			return doc;
		}
		Element contentEle = root.addElement("content");
	
		Element proEle = null;
		for (InfoLayoutAssign ila : infoLayoutAssigns) {
			proEle = contentEle.addElement("assign");
			proEle.addAttribute("type", Integer.toString(ila.getType()));
			proEle.setText(ila.getAssignKey());
		}
		return doc;
	}
}
