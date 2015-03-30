package org.eapp.oa.flow.dto;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.flow.hbean.FlowConfig;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;


/**
 * <p>Title: </p>
 * <p>Description: 保存流程成功</p>
 * @version 1.0 
 */
public class CreateFlowDraftXml {

	private FlowConfig flowConfig;
	
	public FlowConfig getFlowConfig() {
		return flowConfig;
	}

	public void setFlowConfig(FlowConfig flowConfig) {
		this.flowConfig = flowConfig;
	}

	public CreateFlowDraftXml(FlowConfig flowConfig){
		this.flowConfig = flowConfig;
	}
	
	/**
	 * 生成创建工作流的XML信息
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 *   <message code="1" /> 
	 *   <content>
	 *     <flow-key>流程标识</flow-key>
	 *   </content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (flowConfig == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		
		Element contentEle = root.addElement("content");
		contentEle.addElement("flow-key").setText(DataFormatUtil.noNullValue(flowConfig.getFlowKey()));
		
		return doc;
	}
}
