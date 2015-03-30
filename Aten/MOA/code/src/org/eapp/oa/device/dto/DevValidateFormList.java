package org.eapp.oa.device.dto;
import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.device.hbean.DevValidateForm;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;


public class DevValidateFormList {

	private Collection<DevValidateForm> deviceValidateList ;
	
	public DevValidateFormList( Collection<DevValidateForm> deviceValidateList){
		this.deviceValidateList =deviceValidateList;		
	}
	
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (deviceValidateList == null || deviceValidateList.size() < 1) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
	
		Element proEle = null;
		for (DevValidateForm validate : deviceValidateList) {
			proEle =contentEle.addElement("validate");
			proEle.addAttribute("id", validate.getId());
			proEle.addElement("account_id").setText(DataFormatUtil.noNullValue(validate.getAccountID()));
//			proEle.addElement("user-name").setText(validate.getValiManName());
//			proEle.addElement("group").setText(DataFormatUtil.noNullValue(validate.getGroupName()));
//			proEle.addElement("in_date").setText(DataFormatUtil.noNullValue(validate.getInDate()));
			proEle.addElement("remark").setText(DataFormatUtil.noNullValue(validate.getRemark()));
			proEle.addElement("vali_date").setText(DataFormatUtil.noNullValue(validate.getValiDate()));
//			proEle.addElement("deduct_money").setText(DataFormatUtil.noNullValue(validate.getDeductMoney()));
//			proEle.addElement("is_money").setText(DataFormatUtil.noNullValue(validate.getIsMoney()));
//			proEle.addElement("vali-type-name").setText(DataFormatUtil.noNullValue(validate.getValiTypeStr()));
			if (validate.getDevice() != null) {
				proEle.addElement("device_id").setText(DataFormatUtil.noNullValue(validate.getDevice().getId()));
			}
			//Element detailEle = null;
//			for(DeviceValiDetail detail : validate.getDeviceValiDetail()){
//				detailEle = proEle.addElement("validate-detail");				
//				detailEle.addAttribute("detail-id", detail.getId());
////				detailEle.addElement("detail-deviceValidateID").setText(DataFormatUtil.noNullValue(detail.getDeviceValidateID()));
//				detailEle.addElement("detail-item").setText(DataFormatUtil.noNullValue(detail.getItem()));
//				detailEle.addElement("detail-isEligibility").setText(detail.getIsEligibility() ? "是" : "否");
//				detailEle.addElement("detail-remark").setText(DataFormatUtil.noNullValue(detail.getRemark()));	
//			}
		}
		return doc;
	}
	
}
