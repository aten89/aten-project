package org.eapp.oa.flow.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.flow.hbean.FlowConfig;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;


/**
 * @author zfc
 * @version May 8, 2009
 */
public class FlowSelect {
	public ListPage<FlowConfig> page;
	
	public ListPage<FlowConfig> getPage() {
		return page;
	}

	public void setPage(ListPage<FlowConfig> page) {
		this.page = page;
	}
	
	public FlowSelect(ListPage<FlowConfig> page) {
		super();
		this.page = page;
	}

	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		
		List<FlowConfig> fc =  page.getDataList();
		root.addElement("message").addAttribute("code", "1");
		if (fc == null || fc.size() < 1) {
			return doc;
		}
		Element contentEle = root.addElement("content");
		Element proEle = null;
		for (FlowConfig f : fc) {
			proEle = contentEle.addElement("flow-config");	
			if(f.getFlowName().split("-").length >1 ){
				proEle.addElement("name").setText(f.getFlowName().split("-")[0]);
				proEle.addElement("type").setText(f.getFlowName().split("-")[1]);
			} else {
				proEle.addElement("type").setText("");
				proEle.addElement("name").setText(DataFormatUtil.noNullValue(f.getFlowName()));
			}
			proEle.addElement("key").setText(DataFormatUtil.noNullValue(f.getFlowKey()));
			
		}
		return doc;
	}
}
