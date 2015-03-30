/**
 * 
 */
package org.eapp.oa.hr.dto;

import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.hr.hbean.HolidayApply;
import org.eapp.oa.hr.hbean.HolidayDetail;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;


/**
 * @author ZFC
 */
public class HolidayApplyArch {
	
	private ListPage<HolidayApply> holidayApplys;
	private String userAccountId;
	
	
	
	public HolidayApplyArch(ListPage<HolidayApply> holidayApplys, String userAccountId) {
		this.holidayApplys = holidayApplys;
		this.userAccountId = userAccountId;
	}

	

	public ListPage<HolidayApply> getHolidayApplys() {
		return holidayApplys;
	}



	public void setHolidayApplys(ListPage<HolidayApply> holidayApplys) {
		this.holidayApplys = holidayApplys;
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
		if (holidayApplys == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", Long.toString(holidayApplys.getTotalCount()));
		contentEle.addAttribute("page-size", Long.toString(holidayApplys.getCurrentPageSize()));
		contentEle.addAttribute("page-count", Long.toString(holidayApplys.getTotalPageCount()));
		contentEle.addAttribute("current-page", Long.toString(holidayApplys.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", Boolean.toString(holidayApplys.hasPreviousPage()));
		contentEle.addAttribute("next-page", Boolean.toString(holidayApplys.hasNextPage()));
		Element proEle = null;
		if(holidayApplys!=null && holidayApplys.getDataList()!=null){
			for (HolidayApply r : holidayApplys.getDataList()) {
//				String holidayTypeStr = "";
				proEle = contentEle.addElement("holidayApply");				
				proEle.addAttribute("id", r.getId());
				proEle.addElement("apply-account").setText(DataFormatUtil.noNullValue(r.getApplicantName()));
				proEle.addElement("dept").setText(DataFormatUtil.noNullValue(r.getApplyDept()));
				Set<HolidayDetail> holidayDetail = r.getHolidayDetail();
				double totalCancelDays = 0;
				if(holidayDetail != null){
					for (HolidayDetail h : holidayDetail){
//						if(!holidayTypeStr.equals("")){
//							holidayTypeStr += "</br>";
//						} 
//						holidayTypeStr += h.getHolidayName()+"："+" 从 " + sdf.format(h.getStartDate()) + h.getStartTimeStr() + " 到 " + sdf.format(h.getEndDate()) + h.getEndTimeStr();
						if (h.getCancelDays() != null) {
							totalCancelDays += h.getCancelDays();
						}
					}
				}
//				proEle.addElement("holiday-date").setText(holidayTypeStr);
				if (r.getCancelFlag() != null && r.getCancelFlag()) {
					proEle.addElement("cancel-flag").setText("已销假");
					proEle.addElement("days").setText(r.getTotalDays() + "天[销假"  + totalCancelDays + "天]" );
				} else {
					proEle.addElement("cancel-flag").setText("未销假");
					proEle.addElement("days").setText(r.getTotalDays() + "天");
				}
				String status = "";
				if (r.getApplyStatus() == HolidayApply.STATUS_ARCH) {
					status = "归档";
				} else if (r.getApplyStatus() == HolidayApply.STATUS_CANCELLATION) {
					status = "作废";
				} else if (r.getApplyStatus() == HolidayApply.STATUS_CANCELAPPROVAL) {
					status = "销假中";
				}
				
				proEle.addElement("status").setText(status);
				proEle.addElement("apply-date").setText(DataFormatUtil.noNullValue(r.getApplyDate(),SysConstants.STANDARD_TIME_PATTERN));
				if (userAccountId.equals(r.getApplicant()) && r.getPassed() && (r.getCancelFlag() == null || !r.getCancelFlag())) {
					proEle.addElement("cancel-opt").setText("1");
				} else {
					proEle.addElement("cancel-opt").setText("0");
				}
			}
		}
		return doc;
	}
}





