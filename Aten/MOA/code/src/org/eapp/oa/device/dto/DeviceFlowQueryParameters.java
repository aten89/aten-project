package org.eapp.oa.device.dto;

import java.sql.Timestamp;

import org.eapp.util.hibernate.QueryParameters;


/**
 * 设备检查项配置查询参数
 * 
 * @author shenyinjie
 * @version 2009-09-08
 */
public class DeviceFlowQueryParameters extends QueryParameters {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5913475260892312327L;

	public Integer getFormType() {
		return (Integer) this.getParameter("formType");
	}

	public void setFormType(Integer formType) {
		this.addParameter("formType", formType);
	}
	
	public String getApplyCode() {
		return (String) this.getParameter("applyCode");
	}

	public void setApplyCode(String applyCode) {
		this.addParameter("applyCode", applyCode);
	}

	public String getDeviceType() {
		return (String) this.getParameter("deviceType");
	}

	public void setDeviceType(String deviceType) {
		this.addParameter("deviceType", deviceType);
	}

	public String getDeviceName() {
		return (String) this.getParameter("deviceName");
	}

	public String getApplicantID() {
		return (String) this.getParameter("applicantID");
	}

	public void setApplicantID(String applicantID) {
		this.addParameter("applicantID", applicantID);
	}
	
	public Timestamp getBeginApplyTime() {
		return (Timestamp) this.getParameter("beginApplyTime");
	}
	public void setBeginApplyTime(Timestamp beginApplyTime) {
		this.addParameter("beginApplyTime", beginApplyTime);
	}
	
	public Timestamp getEndApplyTime() {
		return (Timestamp) this.getParameter("endApplyTime");
	}

	public void setEndApplyTime(Timestamp endApplyTime) {
		this.addParameter("endApplyTime", endApplyTime);
	}
	public Timestamp getBeginArchTime() {
		return (Timestamp) this.getParameter("beginArchTime");
	}
	public void setBeginArchTime(Timestamp beginArchTime) {
		this.addParameter("beginArchTime", beginArchTime);
	}
	
	public Timestamp getEndArchTime() {
		return (Timestamp) this.getParameter("endArchTime");
	}

	public void setEndArchTime(Timestamp endArchTime) {
		this.addParameter("endArchTime", endArchTime);
	}
}
