/**
 * 
 */
package org.eapp.oa.flow.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.client.util.UsernameCache;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;


/**
 * 
 * @author zsy
 * @version Dec 10, 2008
 */
public class TaskPage {

	private ListPage<Task> taskPage;
	
	public TaskPage(ListPage<Task> taskPage){
		this.taskPage = taskPage;
	}
	
	/**
	 *
	 * 返回格式
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1"/>
	 * 	<content total-count="1" page-size="0" page-count="1" current-page="1" previous-page="false" next-page="false">
	 * 		<task id="报销单号">
	 * 			<flow-class>流程分类</flow-class>		
	 * 			<flow-class-name>流程分类名称</flow-class-name>
	 * 			<flow-name>流程名称</flow-name>		
	 * 			<create-time>任务开始时间</create-time>	
	 * 			<form-id>表单ID</form-id>
	 * 			<task-name>任务名称</task-name>
	 * 			<taskinstance-id>任务实例ID</taskinstance-id>
	 * 		</task>
	 * 	</content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (taskPage == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", String.valueOf(taskPage.getTotalCount()) );
		contentEle.addAttribute("page-size", String.valueOf(taskPage.getCurrentPageSize()) );
		contentEle.addAttribute("page-count", String.valueOf(taskPage.getTotalPageCount()) );
		contentEle.addAttribute("current-page", String.valueOf(taskPage.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", String.valueOf(taskPage.hasPreviousPage()));
		contentEle.addAttribute("next-page", String.valueOf(taskPage.hasNextPage()) );
		
		List<Task>  tasks = taskPage.getDataList();
		if(tasks !=null && tasks.size()>0){
			Element reimburEle = null;
			for (Task t : tasks) {
				reimburEle = contentEle.addElement("task");
				reimburEle.addAttribute("id", t.getId());
				reimburEle.addElement("task-id").setText(DataFormatUtil.noNullValue(t.getId()));
				reimburEle.addElement("taskinstance-id").setText(DataFormatUtil.noNullValue(t.getTaskInstanceID()));
//				if (t.getFlowConfig() != null) {
//					String flowClass = t.getFlowConfig().getFlowClass();
//					if (flowClass != null) {
//						reimburEle.addElement("flow-class").setText(flowClass);		
//						//通过系统字典获取流程类别的显示名称
//						DataDictionaryDTO flowClassDTO =  SysCodeDictLoader.getInstance().getFlowClassByKey(flowClass);
//						if(flowClassDTO != null){
//							reimburEle.addElement("flow-class-name").setText(DataFormatUtil.noNullValue(
//									flowClassDTO.getDictKey()));
//						}
//					}
//					
//					reimburEle.addElement("flow-name").setText(DataFormatUtil.noNullValue(
//							t.getFlowName()));
//				}
				reimburEle.addElement("flow-class-name").setText(DataFormatUtil.noNullValue(
						t.getFlowName()));
				reimburEle.addElement("create-time").setText(DataFormatUtil.noNullValue(
						t.getCreateTime(), SysConstants.STANDARD_TIME_PATTERN));
				reimburEle.addElement("form-id").setText(DataFormatUtil.noNullValue(t.getFormID()));
				reimburEle.addElement("task-name").setText(DataFormatUtil.noNullValue(t.getTaskName()));
				reimburEle.addElement("node-name").setText(DataFormatUtil.noNullValue(t.getNodeName()));
				reimburEle.addElement("view-flag").setText(DataFormatUtil.noNullValue(t.getViewFlag()));
				reimburEle.addElement("description").setText(DataFormatUtil.noNullValue(t.getDescription()));
				reimburEle.addElement("transactor").setText(UsernameCache.getDisplayName(DataFormatUtil.noNullValue(t.getTransactor())));
			}
		}
		return doc;
	}	
}
