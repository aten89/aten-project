package org.eapp.oa.doc.dto;

import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.doc.hbean.DocClassAssign;
import org.eapp.oa.system.util.web.XMLResponse;


/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public class DocClassAssignXml {
	private Collection<DocClassAssign> docClassAssigns;

	public DocClassAssignXml(Collection<DocClassAssign> docClassAssigns) {
		this.docClassAssigns = docClassAssigns;
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
		if (docClassAssigns == null || docClassAssigns.size() < 1) {
			return doc;
		}
		Element contentEle = root.addElement("content");
	
		Element proEle = null;
		for (DocClassAssign ila : docClassAssigns) {
			proEle = contentEle.addElement("assign");
			proEle.addAttribute("type", Integer.toString(ila.getType()));
			proEle.setText(ila.getAssignKey());
		}
		return doc;
	}
}
