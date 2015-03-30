/**
 * 
 */
package org.eapp.oa.hr.dto;

import java.text.SimpleDateFormat;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.hr.hbean.HolidayApply;
import org.eapp.oa.hr.hbean.HolidayDetail;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;


/**
 * @author ZFC
 */
public class HolidayDetailPage {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private ListPage<HolidayDetail> holidayDetails;
	
	public HolidayDetailPage(ListPage<HolidayDetail> holidayDetails) {
		this.holidayDetails = holidayDetails;
	}

	/**
	 *生成系统日志
	 * 返回格式
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1"/>
	 * 	<content>
	 * 		<holidayApply id="单号">
	 * 			<apply-account>申请人</apply-account>
	 * 			<dept>申请部门</dept>
	 * 			<holidayType>假种类型</holidayType>
	 * 			<holiday-date>请假日期</holiday-date>
	 * 			<days>天数</days>
	 * 			<apply-date>申请时间</apply-date>
	 *   		<task-id>任务ID</task-id>
	 * 			<taskinstance-id>任务实例ID</taskinstance-id>
	 *  		<view-flag>是否以读</view-flag>
	 * 		</holidayApply>
	 * 	</content>
	 * </root>
	 * @return
	 */
	
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (holidayDetails == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", Long.toString(holidayDetails.getTotalCount()));
		contentEle.addAttribute("page-size", Long.toString(holidayDetails.getCurrentPageSize()));
		contentEle.addAttribute("page-count", Long.toString(holidayDetails.getTotalPageCount()));
		contentEle.addAttribute("current-page", Long.toString(holidayDetails.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", Boolean.toString(holidayDetails.hasPreviousPage()));
		contentEle.addAttribute("next-page", Boolean.toString(holidayDetails.hasNextPage()));
		Element proEle = null;
		if(holidayDetails!=null && holidayDetails.getDataList()!=null){
			for (HolidayDetail r : holidayDetails.getDataList()) {
				proEle = contentEle.addElement("holiday-detail");				
				proEle.addAttribute("hpply-id", r.getHolidayApply().getId());
				proEle.addElement("apply-account").setText(DataFormatUtil.noNullValue(r.getHolidayApply().getApplicantName()));
				proEle.addElement("dept").setText(DataFormatUtil.noNullValue(r.getHolidayApply().getApplyDept()));
				proEle.addElement("regional").setText(DataFormatUtil.noNullValue(r.getHolidayApply().getRegionalName()));
				proEle.addElement("holiday-name").setText(DataFormatUtil.noNullValue(r.getHolidayName()));

				String holidayTypeStr = "从 " + sdf.format(r.getStartDate()) + r.getStartTimeStr() + " 到 " + sdf.format(r.getEndDate()) + r.getEndTimeStr();
				proEle.addElement("holiday-date").setText(holidayTypeStr);
				proEle.addElement("days").setText(DataFormatUtil.noNullValue(r.getDays()));
				if (HolidayApply.STATUS_CANCELAPPROVAL != r.getHolidayApply().getApplyStatus() && r.getHolidayApply().getCancelFlag()) {
					proEle.addElement("cancel-days").setText(DataFormatUtil.noNullValue(r.getCancelDays()));
				}
				proEle.addElement("remark").setText(DataFormatUtil.noNullValue(r.getRemark()));
			}
		}
		return doc;
	}
}





