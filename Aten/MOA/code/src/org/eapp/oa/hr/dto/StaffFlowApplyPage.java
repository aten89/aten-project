/**
 * 
 */
package org.eapp.oa.hr.dto;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.client.util.UsernameCache;
import org.eapp.oa.hr.hbean.StaffFlowApply;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;

/**
 * @author ZFC
 */
public class StaffFlowApplyPage {
	
	private ListPage<StaffFlowApply> staffFlowApplys;
	
	public StaffFlowApplyPage(ListPage<StaffFlowApply> staffFlowApplys) {
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
		contentEle.addAttribute("total-count", Long.toString(staffFlowApplys.getTotalCount()));
		contentEle.addAttribute("page-size", Long.toString(staffFlowApplys.getCurrentPageSize()));
		contentEle.addAttribute("page-count", Long.toString(staffFlowApplys.getTotalPageCount()));
		contentEle.addAttribute("current-page", Long.toString(staffFlowApplys.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", Boolean.toString(staffFlowApplys.hasPreviousPage()));
		contentEle.addAttribute("next-page", Boolean.toString(staffFlowApplys.hasNextPage()));
		Element proEle = null;
		if(staffFlowApplys!=null && staffFlowApplys.getDataList()!=null){
			for (StaffFlowApply r : staffFlowApplys.getDataList()) {
//				String holidayTypeStr = "";
				proEle = contentEle.addElement("staff-flow");				
				proEle.addAttribute("id", r.getId());
				proEle.addElement("apply-account").setText(DataFormatUtil.noNullValue(r.getApplicantName()));
				proEle.addElement("apply-date").setText(DataFormatUtil.noNullValue(r.getApplyDate(),SysConstants.STANDARD_TIME_PATTERN));
				proEle.addElement("company-area").setText(DataFormatUtil.noNullValue(r.getCompanyArea()));
				proEle.addElement("company-area-name").setText(DataFormatUtil.noNullValue(r.getCompanyAreaName()));
				proEle.addElement("dept").setText(DataFormatUtil.noNullValue(r.getGroupName()));
				proEle.addElement("post").setText(DataFormatUtil.noNullValue(r.getPost()));
				proEle.addElement("user-name").setText(DataFormatUtil.noNullValue(r.getUserName()));
				proEle.addElement("employee-number").setText(DataFormatUtil.noNullValue(r.getEmployeeNumber()));
				proEle.addElement("user-account").setText(DataFormatUtil.noNullValue(r.getUserAccountID()));
				proEle.addElement("entry-date").setText(DataFormatUtil.noNullValue(r.getEntryDate(), SysConstants.STANDARD_DATE_PATTERN));
				proEle.addElement("resign-date").setText(DataFormatUtil.noNullValue(r.getResignDate(), SysConstants.STANDARD_DATE_PATTERN));
				proEle.addElement("apply-type").setText(DataFormatUtil.noNullValue(r.getApplyType()));
				if (r.getPassed() != null) {
					proEle.addElement("status").setText(r.getPassed()? "归档":"作废");
				}
				if(r.getTask()!=null){
					proEle.addElement("node-name").setText(DataFormatUtil.noNullValue(
							r.getTask().getNodeName()));
					proEle.addElement("create-time").setText(DataFormatUtil.noNullValue(
							r.getTask().getCreateTime(), SysConstants.STANDARD_TIME_PATTERN));
					proEle.addElement("deal-man").setText(DataFormatUtil.noNullValue(
							UsernameCache.getDisplayName(r.getTask().getTransactor())));
				}
			}
		}
		return doc;
	}
}





