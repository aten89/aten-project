package org.eapp.oa.device.dto;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.json.JSONUtil;
import org.eapp.oa.device.hbean.DevValidateForm;
import org.eapp.oa.device.hbean.Device;
import org.eapp.oa.device.hbean.DeviceValiDetail;


/**
 * 设备验证单json
 * 
 *
 * <pre>
 * 修改日期      修改人      修改原因
 * 2012-7-23      方文伟      修改注释
 * </pre>
 */
public class DevValidataFormJSON {
    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(DevValidataFormJSON.class);
	/**
	 * 将json转为对象
	 * @param json json字符串
	 * @param device 设备
	 * @return DevValidateForm 设备验证单
	 * @throws OaException oa异常
	 * @throws Exception 异常
	 *
	 * <pre>
	 * 修改日期      修改人      修改原因
	 * 2012-7-23      方文伟      修改注释
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public static DevValidateForm parseJSON(String json, Device device) throws Exception{
		DevValidateForm devValidateForm = null;
		if (json == null || json.trim().equals("")) {
			return null;
		}
		
		try {
			
			List<Map<String,Object>> obj = (List<Map<String,Object>>)JSONUtil.deserialize(json);
			if (obj == null || obj.isEmpty()) {
				return null;
			}
			for (Map<String,Object> map : obj) {
				if (map.size() == 0) {
					continue;
				}
				devValidateForm = new DevValidateForm();
				String applicant=(String)map.get("applicant");//
				String valiDateStr=(String)map.get("valiDate");
				String remark=(String)map.get("remark");
				String valiType=(String)map.get("valiType");
				devValidateForm.setAccountID(applicant);
				SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");
				if(valiDateStr!=null){
					devValidateForm.setValiDate(dateFormat.parse(valiDateStr));
				}
				if(valiType!=null){
					devValidateForm.setValiType(Integer.valueOf(valiType));
				}
				devValidateForm.setRemark(remark);
				devValidateForm.setDevice(device);
				List<Map<String,Object>> obj_new = (List<Map<String,Object>>)map.get("valDetail");
				if (obj_new == null || obj_new.isEmpty()) {
					return null;
				}
				Set<DeviceValiDetail> deviceValiDetails = new HashSet<DeviceValiDetail>();
				for (Map<String, Object> mapSub : obj_new) {
					if (mapSub.size() == 0) {
						continue;
					}
					if(mapSub!=null && mapSub.size()>0){
						String itemName=(String)mapSub.get("itemName");//
						String itemRemark=(String)mapSub.get("remark");
						String isEligibilityStr=(String)mapSub.get("isEligibility");
						boolean isEligibility = false;
						if(isEligibilityStr.equals("1")||isEligibilityStr.equals("true")){
							isEligibility = true;
						}
						DeviceValiDetail deviceValiDetail= new DeviceValiDetail();
						deviceValiDetail.setDevValidateForm(devValidateForm);
						deviceValiDetail.setIsEligibility(isEligibility);
						deviceValiDetail.setItem(itemName);
						deviceValiDetail.setRemark(itemRemark);
						deviceValiDetails.add(deviceValiDetail);
					}
				}
				devValidateForm.setDeviceValiDetails(deviceValiDetails);
				return devValidateForm;
			}
		} catch(Exception e) {
		    log.error("parseJSON", e);
			throw e;
		}
		return null;
	}
}
