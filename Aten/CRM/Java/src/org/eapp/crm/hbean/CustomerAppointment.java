package org.eapp.crm.hbean;

import java.util.Date;

/**
 * CustomerAppointment entity. @author MyEclipse Persistence Tools
 */

public class CustomerAppointment implements java.io.Serializable {

	// Fields

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2177856388827254326L;
	private String id;
	private CustomerInfo customerInfo;
	private Date appointmentTime;
	private String appointmentType;
	private Integer warnOpportunity;
	private String remark;
	private String createor;
	
	private transient String appointmentTimeStr;
	private transient String appointmentTypeStr;
	private transient String createorName;

	// Constructors

	/** default constructor */
	public CustomerAppointment() {
	}
	
	// Property accessors

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the customerInfo
	 */
	public CustomerInfo getCustomerInfo() {
		return customerInfo;
	}

	/**
	 * @param customerInfo the customerInfo to set
	 */
	public void setCustomerInfo(CustomerInfo customerInfo) {
		this.customerInfo = customerInfo;
	}

	/**
	 * @return the appointmentTime
	 */
	public Date getAppointmentTime() {
		return appointmentTime;
	}

	/**
	 * @param appointmentTime the appointmentTime to set
	 */
	public void setAppointmentTime(Date appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	/**
	 * @return the appointmentType
	 */
	public String getAppointmentType() {
		return appointmentType;
	}

	/**
	 * @param appointmentType the appointmentType to set
	 */
	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	/**
	 * @return the warnOpportunity
	 */
	public Integer getWarnOpportunity() {
		return warnOpportunity;
	}

	/**
	 * @param warnOpportunity the warnOpportunity to set
	 */
	public void setWarnOpportunity(Integer warnOpportunity) {
		this.warnOpportunity = warnOpportunity;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the createor
	 */
	public String getCreateor() {
		return createor;
	}

	/**
	 * @param createor the createor to set
	 */
	public void setCreateor(String createor) {
		this.createor = createor;
	}

	/**
	 * @return the appointmentTimeStr
	 */
	public String getAppointmentTimeStr() {
		return appointmentTimeStr;
	}

	/**
	 * @param appointmentTimeStr the appointmentTimeStr to set
	 */
	public void setAppointmentTimeStr(String appointmentTimeStr) {
		this.appointmentTimeStr = appointmentTimeStr;
	}

	/**
	 * @return the appointmentTypeStr
	 */
	public String getAppointmentTypeStr() {
		return appointmentTypeStr;
	}

	/**
	 * @param appointmentTypeStr the appointmentTypeStr to set
	 */
	public void setAppointmentTypeStr(String appointmentTypeStr) {
		this.appointmentTypeStr = appointmentTypeStr;
	}

	public String getCreateorName() {
		return createorName;
	}

	public void setCreateorName(String createorName) {
		this.createorName = createorName;
	}
}