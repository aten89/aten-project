/**
 * 
 */
package org.eapp.oa.reimburse.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import org.eapp.client.util.UsernameCache;
import org.eapp.oa.reimburse.hbean.OutlayList;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;


/**
 * @author 林良益
 *
 */
public class OutlayListPage {
	
	private ListPage<OutlayList> outlayListPage;	
	
	public OutlayListPage(ListPage<OutlayList> outlayListPage){
		this.outlayListPage = outlayListPage;
	}

	/**
	 *
	 * 返回格式
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1"/>
	 * 	<content total-count="1" page-size="0" page-count="1" current-page="1" previous-page="false" next-page="false">
	 * 		<outlayList id="">
	 * 			<reimbursement-id>报销单号</reimbursement-id>
	 *			<payee>受款人</payee>
	 * 			<apply-date>填单日期</apply-date>
	 * 			<budget-item>费用归属项目</budget-item>
	 * 			<outlayName>费用名称</outlayName>			
	 * 			<outlayCategory>费用类别</outlayCategory>
	 * 			<outlaySum>费用金额</outlaySum>
	 * 		</outlayList>
	 * 	</content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (outlayListPage == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", String.valueOf(outlayListPage.getTotalCount()) );
		contentEle.addAttribute("page-size", String.valueOf(outlayListPage.getCurrentPageSize()) );
		contentEle.addAttribute("page-count", String.valueOf(outlayListPage.getTotalPageCount()) );
		contentEle.addAttribute("current-page", String.valueOf(outlayListPage.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", String.valueOf(outlayListPage.hasPreviousPage()));
		contentEle.addAttribute("next-page", String.valueOf(outlayListPage.hasNextPage()) );		
		List<OutlayList>  outlayLists = outlayListPage.getDataList();
		if(outlayLists != null && outlayLists.size() > 0){
			Element outlayListEle = null;
			for (OutlayList ol : outlayLists) {
				outlayListEle = contentEle.addElement("outlayList");				
				outlayListEle.addAttribute("id", ol.getId());
				outlayListEle.addElement("reimbursement-id").setText(DataFormatUtil.noNullValue(ol.getReimbursement().getId()));
				outlayListEle.addElement("applicant").setText(UsernameCache.getDisplayName(ol.getReimbursement().getApplicant()));
				outlayListEle.addElement("archive-date").setText(DataFormatUtil.noNullValue(ol.getReimbursement().getArchiveDate()));
				outlayListEle.addElement("budget-item").setText(DataFormatUtil.noNullValue(ol.getReimbursement().getApplyDept()));
				outlayListEle.addElement("outlay-category").setText(DataFormatUtil.noNullValue(ol.getOutlayCategory()));
				outlayListEle.addElement("outlay-name").setText(DataFormatUtil.noNullValue(ol.getOutlayName()));
				outlayListEle.addElement("outlay-sum").setText(DataFormatUtil.noNullValue(ol.getOutlaySum()));
				outlayListEle.addElement("description").setText(DataFormatUtil.noNullValue(ol.getDescription()));
			}
		}
		return doc;
	}		
	
}
