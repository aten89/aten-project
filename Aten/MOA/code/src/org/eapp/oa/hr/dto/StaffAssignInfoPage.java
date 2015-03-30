/**
 * 
 */
package org.eapp.oa.hr.dto;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;

/**
 * @author ZFC
 */
public class StaffAssignInfoPage {
	
	private ListPage<StaffAssignInfo> staffAssignInfos;
	
	public StaffAssignInfoPage(ListPage<StaffAssignInfo> staffAssignInfos) {
		this.staffAssignInfos = staffAssignInfos;
	}

	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (staffAssignInfos == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", Long.toString(staffAssignInfos.getTotalCount()));
		contentEle.addAttribute("page-size", Long.toString(staffAssignInfos.getCurrentPageSize()));
		contentEle.addAttribute("page-count", Long.toString(staffAssignInfos.getTotalPageCount()));
		contentEle.addAttribute("current-page", Long.toString(staffAssignInfos.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", Boolean.toString(staffAssignInfos.hasPreviousPage()));
		contentEle.addAttribute("next-page", Boolean.toString(staffAssignInfos.hasNextPage()));
		Element proEle = null;
		if(staffAssignInfos != null && staffAssignInfos.getDataList() != null){
			for (StaffAssignInfo s : staffAssignInfos.getDataList()) {
				
				UserAccountInfo r = s.getUser();
//				String holidayTypeStr = "";
				proEle = contentEle.addElement("user-account-info");				
				proEle.addAttribute("id", r.getAccountID());
				proEle.addElement("display-name").setText(DataFormatUtil.noNullValue(r.getDisplayName()));
				proEle.addElement("create-date").setText(DataFormatUtil.noNullValue(r.getCreateDate(),SysConstants.STANDARD_DATE_PATTERN));
				proEle.addElement("invalid-date").setText(DataFormatUtil.noNullValue(r.getInvalidDate(),SysConstants.STANDARD_DATE_PATTERN));
				proEle.addElement("is-lock").setText(DataFormatUtil.noNullValue(r.getIsLock()));
				proEle.addElement("description").setText(DataFormatUtil.noNullValue(r.getDescription()));
				
				proEle.addElement("assign-value").setText(DataFormatUtil.noNullValue(s.getAssignValue()));
			}
		}
		return doc;
	}
}

