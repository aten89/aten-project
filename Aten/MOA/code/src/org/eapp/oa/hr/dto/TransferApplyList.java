/**
 * 
 */
package org.eapp.oa.hr.dto;

import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.hr.hbean.TransferApply;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;


/**
 * @author ZFC
 */
public class TransferApplyList {
	
	private List<TransferApply> holidayApplys;
	
	
	
	public TransferApplyList(List<TransferApply> holidayApplys) {
		super();
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
		Element proEle = null;
		for (TransferApply r : holidayApplys) {
			proEle = contentEle.addElement("TransferApply");				
			proEle.addAttribute("id", r.getId());
			proEle.addElement("apply-account").setText(DataFormatUtil.noNullValue(r.getApplicantName()));
			proEle.addElement("transfer-user-name").setText(DataFormatUtil.noNullValue(r.getTransferUserName()));
			proEle.addElement("transfer-in-dept").setText(DataFormatUtil.noNullValue(r.getTransferInDept()));
			proEle.addElement("transfer-out-dept").setText(DataFormatUtil.noNullValue(r.getTransferOutDept()));
			proEle.addElement("transfer-date").setText(DataFormatUtil.noNullValue(r.getTransferDate(),SysConstants.STANDARD_DATE_PATTERN));
			proEle.addElement("apply-date").setText(DataFormatUtil.noNullValue(r.getApplyDate(),SysConstants.STANDARD_TIME_PATTERN));
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





