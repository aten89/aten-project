/**
 * 
 */
package org.eapp.oa.hr.dto;

import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.hr.hbean.StaffFlowApply;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;


/**
 * @author ZFC
 */
public class StaffFlowApplyList {
	
	private List<StaffFlowApply> staffFlowApplys;
	
	
	
	public StaffFlowApplyList(List<StaffFlowApply> staffFlowApplys) {
		super();
		this.staffFlowApplys = staffFlowApplys;
	}


	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (staffFlowApplys == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		Element proEle = null;
		for (StaffFlowApply r : staffFlowApplys) {
			proEle = contentEle.addElement("staff-flow");				
			proEle.addAttribute("id", r.getId());
			proEle.addElement("apply-account").setText(DataFormatUtil.noNullValue(r.getApplicantName()));
			proEle.addElement("apply-date").setText(DataFormatUtil.noNullValue(r.getApplyDate(),SysConstants.STANDARD_TIME_PATTERN));
			proEle.addElement("company-area").setText(DataFormatUtil.noNullValue(r.getCompanyArea()));
			proEle.addElement("company-area-name").setText(DataFormatUtil.noNullValue(r.getCompanyAreaName()));
			proEle.addElement("dept").setText(DataFormatUtil.noNullValue(r.getGroupName()));
			proEle.addElement("dept-full-name").setText(DataFormatUtil.noNullValue(r.getGroupFullName()));
			proEle.addElement("post").setText(DataFormatUtil.noNullValue(r.getPost()));
			proEle.addElement("user-name").setText(DataFormatUtil.noNullValue(r.getUserName()));
			proEle.addElement("user-account").setText(DataFormatUtil.noNullValue(r.getUserAccountID()));
			proEle.addElement("employee-number").setText(DataFormatUtil.noNullValue(r.getEmployeeNumber()));
			proEle.addElement("sex").setText(DataFormatUtil.noNullValue(r.getSex()));
			proEle.addElement("birthdate").setText(DataFormatUtil.noNullValue(r.getBirthdate(), SysConstants.STANDARD_DATE_PATTERN));
			proEle.addElement("entry-date").setText(DataFormatUtil.noNullValue(r.getEntryDate(), SysConstants.STANDARD_DATE_PATTERN));
			proEle.addElement("resign-date").setText(DataFormatUtil.noNullValue(r.getResignDate(), SysConstants.STANDARD_DATE_PATTERN));
			
			proEle.addElement("contract-start-date").setText(DataFormatUtil.noNullValue(r.getContractStartDate(), SysConstants.STANDARD_DATE_PATTERN));
			proEle.addElement("contract-end-date").setText(DataFormatUtil.noNullValue(r.getContractEndDate(), SysConstants.STANDARD_DATE_PATTERN));
			proEle.addElement("mobile").setText(DataFormatUtil.noNullValue(r.getMobile()));
			proEle.addElement("email").setText(DataFormatUtil.noNullValue(r.getEmail()));
			
			proEle.addElement("apply-type").setText(DataFormatUtil.noNullValue(r.getApplyType()));
			if (r.getTask() != null) {
				proEle.addElement("task-id").setText(DataFormatUtil.noNullValue(r.getTask().getId()));
				proEle.addElement("taskinstance-id").setText(DataFormatUtil.noNullValue(r.getTask().getTaskInstanceID()));
				proEle.addElement("task-start").setText(DataFormatUtil.noNullValue(r.getTask().getCreateTime(),SysConstants.STANDARD_TIME_PATTERN));
				proEle.addElement("flag").setText(DataFormatUtil.noNullValue(r.getTask().getViewFlag()));
			}
		}
		return doc;
	}
}





