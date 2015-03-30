package org.eapp.oa.flow.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.flow.hbean.FlowConfig;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.rpc.dto.DataDictInfo;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;

/**
 * <p>Title: </p>
 * <p>Description: 流程分页对象</p>
 * @version 1.0 
 */
public class FlowConfigPage {
	ListPage<FlowConfig> listPage;

	public FlowConfigPage(ListPage<FlowConfig> listPage) {
		this.listPage = listPage;
	}
	
	/**
	 * @return the listPage
	 */
	public ListPage<FlowConfig> getListPage() {
		return listPage;
	}

	/**
	 * @param listPage the listPage to set
	 */
	public void setListPage(ListPage<FlowConfig> listPage) {
		this.listPage = listPage;
	}
	
	/**
	 *生成用户的XML信息
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 *   <message code="1" /> 
	 *   <content total-count="1" page-size="0" page-count="1" current-page="1" previous-page="false" next-page="false">
	 *     <flow-config id="id" flow-key="流程标识">
	 *       <flow-class>流程类别</flow-class> 
	 *       <flow-name>流程名称</flow-name> 
	 *       <flow-version>版本号</flow-version> 
	 *       <flow-description>流程描述</flow-description> 
	 *     </flow-config>
	 *   </content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (listPage == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", Long.toString(listPage.getTotalCount()));
		contentEle.addAttribute("page-size", Long.toString(listPage.getCurrentPageSize()));
		contentEle.addAttribute("page-count", Long.toString(listPage.getTotalPageCount()));
		contentEle.addAttribute("current-page", Long.toString(listPage.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", Boolean.toString(listPage.hasPreviousPage()));
		contentEle.addAttribute("next-page", Boolean.toString(listPage.hasNextPage()));
		
		List<FlowConfig> data = listPage.getDataList();
		if (data == null || data.size() < 1) {
			return doc;
		}
		Element proEle = null;
		for (FlowConfig m : data) {
			//if(m.getFlowDefine() == null){continue;}
			
			proEle = contentEle.addElement("flow-config");		
			proEle.addAttribute("id", m.getId());			
			proEle.addAttribute("flow-key", DataFormatUtil.noNullValue(m.getFlowKey()));
			String flowClass = null;			
			//通过系统字典获取流程类别的显示名称
			if(m.getFlowClass() != null || !"".equalsIgnoreCase(m.getFlowClass())){
				DataDictInfo flowClassDTO =  SysCodeDictLoader.getInstance().getFlowClassByKey(m.getFlowClass());
				if(flowClassDTO != null){
					flowClass = flowClassDTO.getDictName();
				}
			}
			proEle.addElement("flow-class").setText(DataFormatUtil.noNullValue(flowClass));
			
			
//			proEle.addAttribute("flow-key", DataFormatUtil.noNullValue(m.getFlowDefine().getFlowKey()));
//			proEle.addElement("flow-class").setText(DataFormatUtil.noNullValue(m.getFlowDefine().getCategory()));
//			proEle.addElement("flow-name").setText(DataFormatUtil.noNullValue(m.getFlowDefine().getName()));
			proEle.addElement("flow-name").setText(DataFormatUtil.noNullValue(m.getFlowName()));
			proEle.addElement("flow-version").setText(DataFormatUtil.noNullValue(m.getFlowDefine() != null?m.getFlowDefine().getVersion():""));
			proEle.addElement("flow-description").setText(DataFormatUtil.noNullValue(m.getFlowDefine() != null?m.getFlowDefine().getDescription():""));
			
		}
		return doc;
	}
}
