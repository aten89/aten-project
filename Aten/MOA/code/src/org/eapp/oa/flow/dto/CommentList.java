package org.eapp.oa.flow.dto;

import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;


public class CommentList {
	
	public List<Task> tasks;

	
	public List<Task> getTasks() {
		return tasks;
	}


	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	
	public CommentList(List<Task> tasks) {
		super();
		this.tasks = tasks;
	}


	
	public Document toDocument() {
		
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		
		if (tasks == null || tasks.size()==0) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		
		root.addElement("message").addAttribute("code", "1");
		
		Element contentEle = root.addElement("content");
		
		Element proEle = null;
		for (Task m : tasks) {
			proEle = contentEle.addElement("task");		
			proEle.addElement("comment").setText(DataFormatUtil.noNullValue(m.getComment()));
			proEle.addElement("user").setText(DataFormatUtil.noNullValue(m.getTransactorDisplayName()));
			proEle.addElement("time").setText(DataFormatUtil.noNullValue(m.getEndTime()));
		}
		return doc;
	}
}
