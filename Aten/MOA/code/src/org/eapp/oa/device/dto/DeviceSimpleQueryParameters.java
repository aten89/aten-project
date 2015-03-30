package org.eapp.oa.device.dto;

import java.util.Date;

import org.eapp.util.hibernate.QueryParameters;


/**
 * Description: 简单查询条件
 * 封装：开始时间、结束时间、人员ID、设备名称、设备类别
 * 
 * @author sds
 * @version Aug 5, 2009
 */
public class DeviceSimpleQueryParameters extends QueryParameters {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5087395775448249095L;

	public String getDeviceName() {
		return (String) this.getParameter("deviceName");
	}

	public void setDeviceName(String deviceName) {
		this.addParameter("deviceName", deviceName);
	}

	public String getAccountId() {
		return (String) this.getParameter("accountId");
	}

	public void setAccountId(String accountId) {
		this.addParameter("accountId", accountId);
	}

	public String getDeviceClass() {
		return (String) this.getParameter("deviceClass");
	}

	public void setDeviceClass(String deviceClass) {
		this.addParameter("deviceClass", deviceClass);
	}
	
	public Date getBeginTime() {
		return (Date) this.getParameter("beginTime");
	}

	public void setBeginTime(Date beginTime) {
		this.addParameter("beginTime", beginTime);
	}
	
	public Date getEndTime() {
		return (Date) this.getParameter("endTime");
	}

	public void setEndTime(Date endTime) {
		this.addParameter("endTime", endTime);
	}
	
	public Integer getStatus() {
		return (Integer) this.getParameter("status");
	}

	public void setStatus(Integer status) {
		this.addParameter("status", status);
	}
}
