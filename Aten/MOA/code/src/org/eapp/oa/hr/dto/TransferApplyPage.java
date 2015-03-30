/**
 * 
 */
package org.eapp.oa.hr.dto;


import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.client.util.UsernameCache;
import org.eapp.oa.hr.hbean.TransferApply;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;

/**
 * @author ZFC
 */
public class TransferApplyPage {
	
	private ListPage<TransferApply> transferApplys;
	
	
	
	public TransferApplyPage(ListPage<TransferApply> transferApplys) {
		super();
		this.transferApplys = transferApplys;
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
		if (transferApplys == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", Long.toString(transferApplys.getTotalCount()));
		contentEle.addAttribute("page-size", Long.toString(transferApplys.getCurrentPageSize()));
		contentEle.addAttribute("page-count", Long.toString(transferApplys.getTotalPageCount()));
		contentEle.addAttribute("current-page", Long.toString(transferApplys.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", Boolean.toString(transferApplys.hasPreviousPage()));
		contentEle.addAttribute("next-page", Boolean.toString(transferApplys.hasNextPage()));
		Element proEle = null;
		if(transferApplys!=null && transferApplys.getDataList()!=null){
			for (TransferApply r : transferApplys.getDataList()) {
				proEle = contentEle.addElement("transfer-apply");				
				proEle.addAttribute("id", r.getId());
				proEle.addElement("apply-account").setText(DataFormatUtil.noNullValue(r.getApplicantName()));
				proEle.addElement("apply-date").setText(DataFormatUtil.noNullValue(r.getApplyDate(),SysConstants.STANDARD_TIME_PATTERN));
				proEle.addElement("transfer-user-name").setText(DataFormatUtil.noNullValue(r.getTransferUserName()));
				proEle.addElement("transfer-in-dept").setText(DataFormatUtil.noNullValue(r.getTransferInDept()));
				proEle.addElement("transfer-out-dept").setText(DataFormatUtil.noNullValue(r.getTransferOutDept()));
				proEle.addElement("transfer-date").setText(DataFormatUtil.noNullValue(r.getTransferDate(),SysConstants.STANDARD_DATE_PATTERN));
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





