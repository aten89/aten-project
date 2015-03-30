package org.eapp.oa.device.dto;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.device.hbean.DevPurchaseForm;
import org.eapp.oa.device.hbean.Device;
import org.eapp.oa.device.hbean.DeviceCurStatusInfo;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;

import org.eapp.oa.system.config.SysConstants;


public class DevicePage {
	ListPage<Device> listPage;
	
	public DevicePage(ListPage<Device> listPage){
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
		DecimalFormat format = new DecimalFormat("0.00");
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
		
		List<Device> data = new ArrayList<Device>();
		data = listPage.getDataList();
	
		if (data == null || data.size() < 1) {
			return doc;
		}
		Element proEle = null;
		Device d = new Device();
		for (int i = 0 ; i < data.size() ; i++) {
			d = data.get(i);
			if (d != null) {
				proEle = contentEle.addElement("device");
				proEle.addAttribute("id", d.getId());
				if(d.getDeviceClass()==null){
					proEle.addElement("device-class-str").setText(DataFormatUtil.noNullValue(d.getDeviceClassDisplayName()));
				}else{
					proEle.addElement("device-class-str").setText(DataFormatUtil.noNullValue(d.getDeviceClass().getName()));
				}
				proEle.addElement("device-class").setText(DataFormatUtil.noNullValue(d.getDeviceClass() == null ? "" : d.getDeviceClass().getId()));
				DeviceCurStatusInfo statusInfo = d.getDeviceCurStatusInfo();
				proEle.addElement("device-type").setText(DataFormatUtil.noNullValue(d.getDeviceType()));
				proEle.addElement("device-no").setText(DataFormatUtil.noNullValue(d.getDeviceNO()));
				proEle.addElement("device-type-str").setText(DataFormatUtil.noNullValue(d.getDeviceTypeName()));
				proEle.addElement("device-name").setText(DataFormatUtil.noNullValue(d.getDeviceName()));
				proEle.addElement("device-model").setText(DataFormatUtil.noNullValue(d.getDeviceModel()));
				proEle.addElement("config-list").setText(DataFormatUtil.noNullValue(d.getConfigList()));
				proEle.addElement("status").setText(DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getDeviceCurStatus()));
				proEle.addElement("status-str").setText(DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getDeviceCurStatusStr()));
				proEle.addElement("work-areaName").setText(DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getAreaName()));
				proEle.addElement("work-areaCode").setText(DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getAreaCode()));
				proEle.addElement("work-purpose").setText(DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getPurpose()));
//				proEle.addElement("work-purposeName").setText(DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getPurposeName()));
				proEle.addElement("work-groupName").setText(DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getGroupName()));
				proEle.addElement("work-userId").setText(DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getOwner()));
				proEle.addElement("work-userName").setText(DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getOwnerName()));
				if(statusInfo != null){
					proEle.addElement("apply-date").setText(DataFormatUtil.noNullValue((statusInfo.getStatusUptDate()),SysConstants.STANDARD_DATE_PATTERN));
				}
				
				
				proEle.addElement("description").setText(DataFormatUtil.noNullValue(d.getDescription()));
				//proEle.addElement("is-using").setText(DataFormatUtil.noNullValue(d.getIsUsing()));
				proEle.addElement("reg-account-id").setText(DataFormatUtil.noNullValue(d.getRegAccountID()));
				proEle.addElement("reg-time").setText(DataFormatUtil.noNullValue(d.getRegTime(),SysConstants.STANDARD_DATE_PATTERN));
				proEle.addElement("buy-time").setText(DataFormatUtil.noNullValue(d.getBuyTime(),SysConstants.STANDARD_DATE_PATTERN));
				proEle.addElement("buy-type-str").setText(DataFormatUtil.noNullValue(DevPurchaseForm.getBuyTypeDisplayName(d.getBuyType())));
				proEle.addElement("buy-type").setText(DataFormatUtil.noNullValue(d.getBuyType()));
//				proEle.addElement("deduct-flag").setText(DataFormatUtil.noNullValue(d.getDeductFlag()));
//				proEle.addElement("deduct-money").setText(DataFormatUtil.noNullValue(d.getDeductMoney()));
//				proEle.addElement("in-date").setText(DataFormatUtil.noNullValue(d.getInDate()));
				proEle.addElement("area-code").setText(DataFormatUtil.noNullValue(d.getAreaCode()));
				proEle.addElement("area-name").setText(DataFormatUtil.noNullValue(d.getAreaName()));
				proEle.addElement("sequence").setText(DataFormatUtil.noNullValue(d.getSequence()));
				proEle.addElement("price").setText(DataFormatUtil.noNullValue(format.format(d.getPrice() == null ? 0.00 : d.getPrice())));
				proEle.addElement("finance-original-val").setText(DataFormatUtil.noNullValue(format.format(d.getFinanceOriginalVal()==null?0.00:d.getFinanceOriginalVal())));
				proEle.addElement("remaining").setText(DataFormatUtil.noNullValue(format.format(d.getRemaining()==null?0.00:d.getRemaining())));
				proEle.addElement("buyPrice").setText(DataFormatUtil.noNullValue(format.format(d.getBuyPrice()==null?0.00:d.getBuyPrice())));
				if(DeviceCurStatusInfo.STATUS_NORMAL==statusInfo.getDeviceCurStatus()||DeviceCurStatusInfo.STATUS_LEAVE_TAKE==statusInfo.getDeviceCurStatus()){
					proEle.addElement("device-user").setText(DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getOwnerName()));
				}else{
					proEle.addElement("device-user").setText(DataFormatUtil.noNullValue(statusInfo == null ? "" : DeviceCurStatusInfo.dealMap.get(statusInfo.getDeviceCurStatus())));
				}
				if("BUY-TYPE-SUB".equals(d.getBuyType())){
					if(d.getDeductFlag()!=null && d.getDeductFlag()){
						proEle.addElement("deduct-flag").setText(DataFormatUtil.noNullValue("是"));
						if(d.getDeductMoney()!=null){
							proEle.addElement("deduct-money").setText(DataFormatUtil.noNullValue(format.format(d.getDeductMoney()==null?0.00:d.getDeductMoney())));
						}
						if(d.getInDate()!=null){
							proEle.addElement("in-date").setText(DataFormatUtil.noNullValue(d.getInDate()));
						}
					}else{
						proEle.addElement("deduct-flag").setText(DataFormatUtil.noNullValue("否"));
					}
				}
				
				proEle.addElement("approve-type").setText(DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getApproveType()));
				proEle.addElement("approve-type-str").setText(DataFormatUtil.noNullValue(statusInfo == null || statusInfo.getApproveType() == null ? "" : DeviceCurStatusInfo.approveTypeMap.get(statusInfo.getApproveType())));
			}
		}
		return doc;
		
	}
}
