package org.eapp.oa.device.dto;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import org.eapp.client.util.UsernameCache;
import org.eapp.oa.device.hbean.DeviceFlowView;
import org.eapp.oa.system.util.SerialNoCreater;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;

import org.eapp.oa.system.config.SysConstants;


public class DeviceFlowPage {
	ListPage<DeviceFlowView> listPage;
	
	public DeviceFlowPage(ListPage<DeviceFlowView> listPage){
		this.listPage=listPage;
	}
	
	/**
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 *   <message code="1" /> 
	 *   <content total-count="1" page-size="0" page-count="1" current-page="1" previous-page="false" next-page="false">
	 *     <device id="">
	 *       <device-type>设备类型</device-type> 
	 *       <device-NO>设备编号</device-NO>
	 *       <device-name>设备名称</device-name>
	 *       <device-model>设备型号</device-model>
	 *       <device-description>设备描述</device-description>
	 *       <device-nowState>当前状态</device-nowState>
	 *       <device-buyTime>购买时间</device-buyTime>
	 *       <device-buyType>购买方式</device-buyType>
	 *       <device-regTime>登记时间</device-regTime>
	 *       <device-remark>备注</device-remark>
	 *     </device-config>
	 *   </content>
	 * </root>
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
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
		
		List<DeviceFlowView> data = new ArrayList<DeviceFlowView>();
		data = listPage.getDataList();
	
		if (data == null || data.size() < 1) {
			return doc;
		}
		Element proEle = null;
		DeviceFlowView d = new DeviceFlowView();
		for (int i = 0 ; i < data.size() ; i++) {
			d = data.get(i);
			if (d != null) {
				proEle = contentEle.addElement("device-flow");
				proEle.addAttribute("id", d.getId());
				if (d.getTask() != null) {
					proEle.addElement("task-id").setText(DataFormatUtil.noNullValue(d.getTask().getId()));
					proEle.addElement("taskinstance-id").setText(DataFormatUtil.noNullValue(d.getTask().getTaskInstanceID()));
					proEle.addElement("create-time").setText(DataFormatUtil.noNullValue(
							d.getTask().getCreateTime(), SysConstants.STANDARD_TIME_PATTERN));
					proEle.addElement("end-time").setText(DataFormatUtil.noNullValue(
							d.getTask().getEndTime(), SysConstants.STANDARD_TIME_PATTERN));
					proEle.addElement("task-name").setText(DataFormatUtil.noNullValue(
							d.getTask().getTaskName()));
					proEle.addElement("node-name").setText(DataFormatUtil.noNullValue(
							d.getTask().getNodeName()));
					proEle.addElement("transactor").setText(DataFormatUtil.noNullValue(UsernameCache.getDisplayName( d.getTask().getTransactor())));
				}
				proEle.addElement("device-type").setText(DataFormatUtil.noNullValue(d.getDeviceType()));
				proEle.addElement("device-typeName").setText(DataFormatUtil.noNullValue(d.getDeviceTypeName()));
				proEle.addElement("apply-date").setText(DataFormatUtil.noNullValue(d.getApplyDate(),SysConstants.STANDARD_DATE_PATTERN));
				proEle.addElement("applicant-name").setText(DataFormatUtil.noNullValue(d.getApplicantDisplayName()));
				proEle.addElement("applicant-group").setText(DataFormatUtil.noNullValue(d.getApplyGroupName()));
				proEle.addElement("flowInstance-ID").setText(DataFormatUtil.noNullValue(d.getFlowInstanceID()));
				proEle.addElement("reg-name").setText(DataFormatUtil.noNullValue(d.getRegAccountDisplayName()));
				proEle.addElement("form-name").setText(DataFormatUtil.noNullValue(DeviceFlowView.formTypeMap.get(d.getFormType())));
				proEle.addElement("form-type").setText(DataFormatUtil.noNullValue(d.getFormType()));
				if(d.getApplyFormNO()!=null){
					proEle.addElement("form-code").setText(DataFormatUtil.noNullValue(d.getSequenceYear()+SerialNoCreater.getIsomuxByNum(d.getApplyFormNO().toString(),4)));
				}
				proEle.addElement("reg-time").setText(DataFormatUtil.noNullValue(d.getRegTime(),SysConstants.STANDARD_DATE_PATTERN));
				proEle.addElement("archive-date").setText(DataFormatUtil.noNullValue(d.getArchiveDate(),SysConstants.STANDARD_DATE_PATTERN));
				proEle.addElement("form-status").setText(DataFormatUtil.noNullValue(DeviceFlowView.statusMap.get(d.getFormStatus())));
			}
		}
		return doc;
		
	}
}
