package org.eapp.oa.hr.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.hr.hbean.HRFlowConf;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;


public class HRFlowConfList {

	
	private List<HRFlowConf> holidayFlow;
	

	
	public List<HRFlowConf> getHolidayFlow() {
		return holidayFlow;
	}

	public void setHolidayFlow(List<HRFlowConf> holidayFlow) {
		this.holidayFlow = holidayFlow;
	}


	public HRFlowConfList(List<HRFlowConf> holidayFlow) {
		this.holidayFlow = holidayFlow;
	}

	
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (holidayFlow == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		Element proEle = null;
		for (HRFlowConf r : holidayFlow) {
			proEle = contentEle.addElement("holiday-flow");				
			proEle.addAttribute("id", r.getId());
			proEle.addElement("group-name").setText(DataFormatUtil.noNullValue(r.getGroupName()));
			proEle.addElement("holi-flow-key", DataFormatUtil.noNullValue(r.getHolidayFlowKey()));
            proEle.addElement("holi-flow-name").setText(DataFormatUtil.noNullValue(r.getHolidayFlowName()));
            proEle.addElement("canholi-flow-key", DataFormatUtil.noNullValue(r.getCanHolidayFlowKey()));
            proEle.addElement("canholi-flow-name").setText(DataFormatUtil.noNullValue(r.getCanHolidayFlowName()));
            proEle.addElement("entry-flow-key", DataFormatUtil.noNullValue(r.getEntryFlowKey()));
            proEle.addElement("entry-flow-name").setText(DataFormatUtil.noNullValue(r.getEntryFlowName()));
            proEle.addElement("resign-flow-key", DataFormatUtil.noNullValue(r.getResignFlowKey()));
            proEle.addElement("resign-flow-name").setText(DataFormatUtil.noNullValue(r.getResignFlowName()));
            proEle.addElement("transfer-flow-key", DataFormatUtil.noNullValue(r.getTransferFlowKey()));
            proEle.addElement("transfer-flow-name").setText(DataFormatUtil.noNullValue(r.getTransferFlowName()));
            proEle.addElement("positive-flow-key", DataFormatUtil.noNullValue(r.getPositiveFlowKey()));
            proEle.addElement("positive-flow-name").setText(DataFormatUtil.noNullValue(r.getPositiveFlowName()));
			proEle.addElement("description").setText(DataFormatUtil.noNullValue(r.getDescription()));
		}
		return doc;
	}

}
