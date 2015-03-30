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
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;


/**
 * @author 林良益
 * 
 *
 */
public class ReimbursementPage {

	private ListPage<Reimbursement> reimbursementListPage;
	
	public ReimbursementPage(ListPage<Reimbursement> reimbursementListPage){
		this.reimbursementListPage = reimbursementListPage;
	}
	
	/**
	 *
	 * 返回格式
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1"/>
	 * 	<content total-count="1" page-size="0" page-count="1" current-page="1" previous-page="false" next-page="false">
	 * 		<reimbursement id="报销单号">
	 * 			<create-time>任务开始时间</create-time>	
	 * 			<end-time>任务结束时间</end-time>			
	 * 			<finance>财务隶属</finance>
	 * 			<payee>受款人</payee>
	 * 			<applicant>报销人</applicant>
	 * 			<causa>事由</causa>
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
		if (reimbursementListPage == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", String.valueOf(reimbursementListPage.getTotalCount()) );
		contentEle.addAttribute("page-size", String.valueOf(reimbursementListPage.getCurrentPageSize()) );
		contentEle.addAttribute("page-count", String.valueOf(reimbursementListPage.getTotalPageCount()) );
		contentEle.addAttribute("current-page", String.valueOf(reimbursementListPage.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", String.valueOf(reimbursementListPage.hasPreviousPage()));
		contentEle.addAttribute("next-page", String.valueOf(reimbursementListPage.hasNextPage()) );
		
		List<Reimbursement>  reimbursements = reimbursementListPage.getDataList();
		if(reimbursements!=null && reimbursements.size()>0){
			Element reimburEle = null;
			for (Reimbursement r : reimbursements) {
				reimburEle = contentEle.addElement("reimbursement");				
				reimburEle.addAttribute("id", r.getId());
				if (r.getTask() != null) {
					reimburEle.addElement("task-id").setText(DataFormatUtil.noNullValue(
							r.getTask().getId()));
					reimburEle.addElement("taskinstance-id").setText(DataFormatUtil.noNullValue(
							r.getTask().getTaskInstanceID()));
					reimburEle.addElement("create-time").setText(DataFormatUtil.noNullValue(
							r.getTask().getCreateTime(), SysConstants.STANDARD_TIME_PATTERN));
					reimburEle.addElement("end-time").setText(DataFormatUtil.noNullValue(
							r.getTask().getEndTime(), SysConstants.STANDARD_TIME_PATTERN));
					reimburEle.addElement("task-name").setText(DataFormatUtil.noNullValue(
							r.getTask().getTaskName()));
					reimburEle.addElement("node-name").setText(DataFormatUtil.noNullValue(
							r.getTask().getNodeName()));
					reimburEle.addElement("transactor").setText(DataFormatUtil.noNullValue(UsernameCache.getDisplayName( r.getTask().getTransactor())));
					reimburEle.addElement("view-flag").setText(DataFormatUtil.noNullValue(r.getTask().getViewFlag()));
				}
				reimburEle.addElement("finance").setText(DataFormatUtil.noNullValue(r.getFinance()));
				reimburEle.addElement("payee").setText(UsernameCache.getDisplayName(r.getPayee()));
				reimburEle.addElement("applicant").setText(UsernameCache.getDisplayName(r.getApplicant()));
				reimburEle.addElement("causa").setText(DataFormatUtil.noNullValue(r.getCausa()));
				reimburEle.addElement("apply-date").setText(DataFormatUtil.noNullValue(r.getApplyDate(),SysConstants.STANDARD_TIME_PATTERN));
				reimburEle.addElement("archive-date").setText(DataFormatUtil.noNullValue(r.getArchiveDate(),SysConstants.STANDARD_TIME_PATTERN));
				reimburEle.addElement("budget-item").setText(DataFormatUtil.noNullValue(r.getApplyDept()));
				reimburEle.addElement("reimbursement-sum").addCDATA(DataFormatUtil.noNullValue(r.getReimbursementSum()));
				if (r.getPassed() != null) {
					reimburEle.addElement("passed").setText(r.getPassed() ? "通过" : "作废");
				}
			}
		}
		return doc;
	}	
}
