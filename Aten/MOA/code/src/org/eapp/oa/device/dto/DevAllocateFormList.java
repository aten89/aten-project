package org.eapp.oa.device.dto;

import java.util.Collection;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.device.hbean.DevAllocateForm;
import org.eapp.oa.system.util.web.XMLResponse;


public class DevAllocateFormList {
	private Collection<DevAllocateForm> devAllocateForms;

	public Collection<DevAllocateForm> getDevAllocateForms() {
		return devAllocateForms;
	}
	public void setDevAllocateForms(Collection<DevAllocateForm> devAllocateForms) {
		this.devAllocateForms = devAllocateForms;
	}
	public DevAllocateFormList(){}
	public DevAllocateFormList(Collection<DevAllocateForm> devAllocateForms) {
		super();
		this.devAllocateForms = devAllocateForms;
	}
	
	/**
	 *
	 * 返回格式
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1"/>
	 * 	<content total-count="1">
	 * 		<document id="信息单号">
	 * 			<subject>标题</subject>	
	 * 			<draft-date>起草时间</draft-date>		
	 * 			<drafts-man>起草人</drafts-man>	
	 * 			<groupName>起草部门</groupName>	
	 * 			<doc-class>类别</info-class>
	 * 		</document>
	 * 	</content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (devAllocateForms == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", Integer.toString(devAllocateForms.size()));
		//Element proEle = null;
//		for (DevAllocateForm r : devAllocateForms) {
//			proEle = contentEle.addElement("allocation");				
//			proEle.addAttribute("id", r.getId());
//			if (r.getTask() != null) {
//				proEle.addElement("task-id").setText(DataFormatUtil.noNullValue(
//						r.getTask().getId()));
//				proEle.addElement("taskinstance-id").setText(DataFormatUtil.noNullValue(
//						r.getTask().getTaskInstanceID()));
//				proEle.addElement("view-flag").setText(DataFormatUtil.noNullValue(
//						r.getTask().getViewFlag()));
//				proEle.addElement("create-time").setText(DataFormatUtil.noNullValue(
//						r.getTask().getCreateTime(), SysConstants.STANDARD_TIME_PATTERN));
//				proEle.addElement("end-time").setText(DataFormatUtil.noNullValue(
//						r.getTask().getEndTime(), SysConstants.STANDARD_TIME_PATTERN));
//				proEle.addElement("task-name").setText(DataFormatUtil.noNullValue(
//						r.getTask().getTaskName()));
//				proEle.addElement("node-name").setText(DataFormatUtil.noNullValue(
//						r.getTask().getNodeName()));
//			}
//			proEle.addElement("draft_date").setText(DataFormatUtil.noNullValue(r.getDraftDate(),SysConstants.STANDARD_TIME_PATTERN));
//			proEle.addElement("drafts_man").setText(r.getDraftsmanName());
//			proEle.addElement("device").setText(DataFormatUtil.noNullValue(r.getDevice().getDeviceName()));
//			proEle.addElement("move_type").setText(DataFormatUtil.noNullValue(r.getMoveType()==0 ?"调拨给其他人":"调拨入库"));		
//			proEle.addElement("in_account").setText(DataFormatUtil.noNullValue(r.getInAccountId()));	
//			proEle.addElement("in_user_name").setText(DataFormatUtil.noNullValue(r.getInUserName()));
//			proEle.addElement("in_group").setText(DataFormatUtil.noNullValue(r.getInGroupName()));		
//			proEle.addElement("out_account").setText(DataFormatUtil.noNullValue(r.getOutAccountId()));	
//			proEle.addElement("out_user_name").setText(DataFormatUtil.noNullValue(r.getOutUserName()));
//			proEle.addElement("out_group").setText(DataFormatUtil.noNullValue(r.getOutGroupName()));
//			proEle.addElement("move_date").setText(DataFormatUtil.noNullValue(r.getMoveDate(),SysConstants.STANDARD_TIME_PATTERN));		
//			proEle.addElement("passed").setText(DataFormatUtil.noNullValue(r.getPassed()));		
//			proEle.addElement("reason").addCDATA(DataFormatUtil.noNullValue(r.getReason()));
//			proEle.addElement("form_status").setText(DataFormatUtil.noNullValue(r.getFormStatus()));		
//			
//		}
		return doc;
	}
	
}
