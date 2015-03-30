package org.eapp.oa.doc.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.doc.hbean.DocClass;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;


/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public class DocClassList {
	private List<DocClass> docClasses;
	public DocClassList(List<DocClass> docClasses) {
		this.docClasses = docClasses;
	}
	
	/**
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 *   <message code="1" /> 
	 *   <content>
	 *     <info-layout id="2">
	 *       <flow-class>2</flow-class> 
	 *       <file-class>2</file-class> 
	 *       <name>22</name>
	 *     </info-layout>
	 *   </content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		root.addElement("message").addAttribute("code", "1");
		if (docClasses == null || docClasses.size() < 1) {
			return doc;
		}
		Element contentEle = root.addElement("content");
	
		Element proEle = null;
		for (DocClass f : docClasses) {
			proEle = contentEle.addElement("doc-class");				
			proEle.addAttribute("id", f.getId());
			proEle.addElement("flow-class", DataFormatUtil.noNullValue(f.getFlowClass()));
			proEle.addElement("flow-class-name").setText(DataFormatUtil.noNullValue(f.getFlowClassName()));
			proEle.addElement("file-class").setText(DataFormatUtil.noNullValue(f.getFileClass() == 1 ? "普通文件" : "公文" ));
//			if(f.getAssignType() == 0){
//				proEle.addElement("isAssign").setText("无需指定审批");
//			}
//			if(f.getAssignType() == 1){
//				proEle.addElement("isAssign").setText("必选指定审批");
//			}
//			if(f.getAssignType() == 2){
//				proEle.addElement("isAssign").setText("可选指定审批");
//			}
			proEle.addElement("name").setText(DataFormatUtil.noNullValue(f.getName()));
			proEle.addElement("description").setText(DataFormatUtil.noNullValue(f.getDescription()));
			proEle.addElement("has-template").setText(f.getBodyTemplate() == null ? "无" : "有");
		}
		return doc;
	}
}
