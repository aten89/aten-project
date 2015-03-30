/**
 * 
 */
package org.eapp.oa.hr.dto;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.hr.hbean.SalaryBill;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;

/**
 */
public class SalaryBillPage {
	
	private ListPage<SalaryBill> salaryBillpage;
	
	public SalaryBillPage(ListPage<SalaryBill> salaryBillpage) {
		this.salaryBillpage = salaryBillpage;
	}

	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (salaryBillpage == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", Long.toString(salaryBillpage.getTotalCount()));
		contentEle.addAttribute("page-size", Long.toString(salaryBillpage.getCurrentPageSize()));
		contentEle.addAttribute("page-count", Long.toString(salaryBillpage.getTotalPageCount()));
		contentEle.addAttribute("current-page", Long.toString(salaryBillpage.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", Boolean.toString(salaryBillpage.hasPreviousPage()));
		contentEle.addAttribute("next-page", Boolean.toString(salaryBillpage.hasNextPage()));
		Element proEle = null;
		if(salaryBillpage!=null && salaryBillpage.getDataList()!=null){
			for (SalaryBill r : salaryBillpage.getDataList()) {
				proEle = contentEle.addElement("salary-bill");				
				proEle.addAttribute("id", r.getId());
				proEle.addElement("month").setText(DataFormatUtil.noNullValue(r.getMonth()));
				proEle.addElement("user-account").setText(DataFormatUtil.noNullValue(r.getUserAccountID()));
				proEle.addElement("user-name").setText(DataFormatUtil.noNullValue(r.getUserName()));
				proEle.addElement("employee-number").setText(DataFormatUtil.noNullValue(r.getEmployeeNumber()));
				
				proEle.addElement("dept").setText(DataFormatUtil.noNullValue(r.getDept()));
				proEle.addElement("post").setText(DataFormatUtil.noNullValue(r.getPost()));
				proEle.addElement("entry-date").setText(DataFormatUtil.noNullValue(r.getEntryDate(),SysConstants.STANDARD_DATE_PATTERN));
				proEle.addElement("wage-basic").setText(DataFormatUtil.noNullValue(r.getWageBasic()));
				proEle.addElement("wage-performance-real").setText(DataFormatUtil.noNullValue(r.getWagePerformanceReal()));
				proEle.addElement("commission").setText(DataFormatUtil.noNullValue(r.getCommission()));
				proEle.addElement("deduct-else").setText(DataFormatUtil.noNullValue(r.getDeductElse()));
				proEle.addElement("cost-five-insurance").setText(DataFormatUtil.noNullValue(r.getCostFiveInsurance()));
				proEle.addElement("wage-real").setText(DataFormatUtil.noNullValue(r.getWageReal()));
				proEle.addElement("wage-allowance").setText(DataFormatUtil.noNullValue(r.getWageAllowance()));
				
				proEle.addElement("deduct-less-month").setText(DataFormatUtil.noNullValue(r.getDeductLessMonth()));
				proEle.addElement("deduct-compassionate").setText(DataFormatUtil.noNullValue(r.getDeductCompassionate()));
				proEle.addElement("deduct-sick").setText(DataFormatUtil.noNullValue(r.getDeductSick()));
				proEle.addElement("deduct-late").setText(DataFormatUtil.noNullValue(r.getDeductLate()));
				proEle.addElement("attendance").setText(DataFormatUtil.noNullValue(-r.getDeductLate() - r.getDeductSick() - r.getDeductCompassionate() - r.getDeductLessMonth()));
			}
		} 
		return doc;
	}
}





