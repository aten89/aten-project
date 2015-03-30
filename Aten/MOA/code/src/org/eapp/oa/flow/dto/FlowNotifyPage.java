/**
 * 
 */
package org.eapp.oa.flow.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.client.util.UsernameCache;
import org.eapp.oa.flow.hbean.FlowNotify;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;


/**
 * 
 * @author zsy
 * @version Dec 10, 2008
 */
public class FlowNotifyPage {

	private ListPage<FlowNotify> flowNotifyPage;
	
	public FlowNotifyPage(ListPage<FlowNotify> flowNotifyPage){
		this.flowNotifyPage = flowNotifyPage;
	}
	
	/**
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (flowNotifyPage == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", String.valueOf(flowNotifyPage.getTotalCount()) );
		contentEle.addAttribute("page-size", String.valueOf(flowNotifyPage.getCurrentPageSize()) );
		contentEle.addAttribute("page-count", String.valueOf(flowNotifyPage.getTotalPageCount()) );
		contentEle.addAttribute("current-page", String.valueOf(flowNotifyPage.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", String.valueOf(flowNotifyPage.hasPreviousPage()));
		contentEle.addAttribute("next-page", String.valueOf(flowNotifyPage.hasNextPage()) );
		
		List<FlowNotify>  flowNotifys = flowNotifyPage.getDataList();
		if(flowNotifys != null && flowNotifys.size() > 0) {
			Element reimburEle = null;
			for (FlowNotify t : flowNotifys) {
				reimburEle = contentEle.addElement("flow-notify");
				reimburEle.addAttribute("id", t.getId());
				reimburEle.addElement("subject").setText(DataFormatUtil.noNullValue(t.getSubject()));
				reimburEle.addElement("creator").setText(DataFormatUtil.noNullValue(t.getCreator()));
				reimburEle.addElement("creator-name").setText(UsernameCache.getDisplayName(t.getCreator()));
				reimburEle.addElement("group-full-name").setText(DataFormatUtil.noNullValue(t.getGroupFullName()));
				reimburEle.addElement("notify-time").setText(DataFormatUtil.noNullValue(
						t.getNotifyTime(), SysConstants.STANDARD_TIME_PATTERN));
				reimburEle.addElement("status").setText(DataFormatUtil.noNullValue(t.getStatus()));
				reimburEle.addElement("flow-class").setText(DataFormatUtil.noNullValue(t.getFlowClass()));
				reimburEle.addElement("flow-status").setText(DataFormatUtil.noNullValue(t.getFlowStatus()));
				reimburEle.addElement("ref-form-id").setText(DataFormatUtil.noNullValue(t.getRefFormID()));
				reimburEle.addElement("view-detail-url").setText(DataFormatUtil.noNullValue(t.getViewDetailURL()));
			}
		}
		return doc;
	}	
}
