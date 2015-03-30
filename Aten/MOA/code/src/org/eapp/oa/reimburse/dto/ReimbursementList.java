/**
 * 
 */
package org.eapp.oa.reimburse.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import org.eapp.client.util.UsernameCache;
import org.eapp.oa.reimburse.hbean.Reimbursement;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;


/**
 * @author zsy
 * @version Nov 26, 2008
 */
public class ReimbursementList {
	private List<Reimbursement> reimbursements;
	
	public ReimbursementList(List<Reimbursement> reimbursements) {
		this.reimbursements = reimbursements;
	}

	public List<Reimbursement> getReimbursements() {
		return reimbursements;
	}

	public void setReimbursements(List<Reimbursement> reimbursements) {
		this.reimbursements = reimbursements;
	}
	
	/**
	 *
	 * 返回格式
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1"/>
	 * 	<content>
	 * 		<reimbursement id="报销单号">
	 * 			<create-time>任务创建时间</create-time>	
	 * 			<end-time>任务结束时间</end-time>		
	 * 			<task-name>任务名称</task-name>		
	 * 			<finance>财务隶属</finance>
	 * 			<payee>受款人</payee>
	 * 			<applicant>报销人</applicant>
	 * 			<apply-date>填单日期</apply-date>
	 * 			<budget-item>费用归属项目</budget-item>
	 * 			<reimbursement-sum>报销总额</reimbursement-sum>
	 * 		</reimbursement>
	 * 	</content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (reimbursements == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", Integer.toString(reimbursements.size()));
		Element proEle = null;
		for (Reimbursement r : reimbursements) {
			proEle = contentEle.addElement("reimbursement");				
			proEle.addAttribute("id", r.getId());
			if (r.getTask() != null) {
				proEle.addElement("task-id").setText(DataFormatUtil.noNullValue(
						r.getTask().getId()));
				proEle.addElement("taskinstance-id").setText(DataFormatUtil.noNullValue(
						r.getTask().getTaskInstanceID()));
				proEle.addElement("view-flag").setText(DataFormatUtil.noNullValue(
						r.getTask().getViewFlag()));
				proEle.addElement("create-time").setText(DataFormatUtil.noNullValue(
						r.getTask().getCreateTime(), SysConstants.STANDARD_TIME_PATTERN));
				proEle.addElement("end-time").setText(DataFormatUtil.noNullValue(
						r.getTask().getEndTime(), SysConstants.STANDARD_TIME_PATTERN));
				proEle.addElement("task-name").setText(DataFormatUtil.noNullValue(
						r.getTask().getTaskName()));
				proEle.addElement("node-name").setText(DataFormatUtil.noNullValue(
						r.getTask().getNodeName()));
			}
			proEle.addElement("payee").setText(UsernameCache.getDisplayName(r.getPayee()));
			proEle.addElement("applicant").setText(UsernameCache.getDisplayName(r.getApplicant()));
			proEle.addElement("apply-date").setText(DataFormatUtil.noNullValue(r.getApplyDate()));
			proEle.addElement("reimbursement-sum").addCDATA(DataFormatUtil.noNullValue(r.getReimbursementSum()));
			proEle.addElement("causa").setText(DataFormatUtil.noNullValue(r.getCausa()));
		}
		return doc;
	}
}
