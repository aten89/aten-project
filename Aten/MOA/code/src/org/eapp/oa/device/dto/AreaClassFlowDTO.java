package org.eapp.oa.device.dto;

/**
 * 设备区域、类别、流程类别 DTO
 * @author aGrainOfSand
 *
 */
public class AreaClassFlowDTO implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1144308966121367628L;

	private String areaCode;
	
	private String deviceClassID;

	private Integer flowType;

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getDeviceClassID() {
		return deviceClassID;
	}

	public void setDeviceClassID(String deviceClassID) {
		this.deviceClassID = deviceClassID;
	}

	public Integer getFlowType() {
		return flowType;
	}

	public void setFlowType(Integer flowType) {
		this.flowType = flowType;
	}
	
}
