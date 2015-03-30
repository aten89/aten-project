/**
 * 
 */
package org.eapp.oa.flow.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.workflow.def.FlowDefine;


/**
 * @author zsy
 * @version Nov 25, 2008
 */
public class FlowList {
	private List<FlowDefine> flows;
	public FlowList(List<FlowDefine> flows) {
		this.flows = flows;
	}
	public List<FlowDefine> getFlows() {
		return flows;
	}
	public void setFlows(List<FlowDefine> flows) {
		this.flows = flows;
	}
	
	/**
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 *   <message code="1" /> 
	 *   <content>
	 *     <flow flow-key="2">
	 *       <version>2</version> 
	 *       <name>22</name> 
	 *       <description>22</description>
	 *     </flow>
	 *   </content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		root.addElement("message").addAttribute("code", "1");
		if (flows == null || flows.size() < 1) {
			return doc;
		}
		Element contentEle = root.addElement("content");
	
		Element proEle = null;
		for (FlowDefine f : flows) {
			proEle = contentEle.addElement("flow");				
			proEle.addAttribute("flow-key", f.getFlowKey());
			proEle.addElement("version", Long.toString(f.getVersion()));
			proEle.addElement("name").setText(DataFormatUtil.noNullValue(f.getName()));
			proEle.addElement("description").setText(DataFormatUtil.noNullValue(f.getDescription()));
		}
		return doc;
	}
}
