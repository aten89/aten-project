package org.eapp.oa.device.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.json.JSONUtil;
import org.eapp.oa.device.hbean.Device;
import org.eapp.oa.device.hbean.DevicePropertyDetail;


/**
 * 设备选项list
 * 
 *
 * <pre>
 * 修改日期      修改人      修改原因
 * 2012-7-23      方文伟      修改注释
 * </pre>
 */
public class DeviceOptionLists {
    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(DeviceOptionLists.class);
	/**
	 * 将json转为对象
	 * @param json json字符串
	 * @param device 设备
	 * @return Set<DevicePropertyDetail>
	 * @throws OaException oa异常
	 * @throws Exception 异常
	 *
	 * <pre>
	 * 修改日期      修改人      修改原因
	 * 2012-7-23      方文伟      修改注释
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public static Set<DevicePropertyDetail> parseJSON(String json, Device device) throws Exception{
		Set<DevicePropertyDetail> devicePropertys = new HashSet<DevicePropertyDetail>();
		DevicePropertyDetail devicePropertyDetail = null;
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
				devicePropertyDetail = new DevicePropertyDetail();
				
				String itemName=(String)map.get("itemName");
				String itemRemark=(String)map.get("remark");
				devicePropertyDetail.setPropertyName(itemName);
				devicePropertyDetail.setPropertyValue(itemRemark);
				devicePropertyDetail.setDevice(device);
				devicePropertys.add(devicePropertyDetail);
			}
		} catch(Exception e) {
		    log.error("parseJSON", e);
			throw e;
		}
		return devicePropertys;
	}
}
