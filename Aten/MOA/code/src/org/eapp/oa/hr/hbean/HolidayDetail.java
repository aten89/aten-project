package org.eapp.oa.hr.hbean;

import java.util.Date;



/**
 * HolidayApply entity.
 * Description:请假明细
 * @author MyEclipse Persistence Tools
 */

public class HolidayDetail implements java.io.Serializable {

	private static final long serialVersionUID = 2703642714025395714L;
	public static final String MORNING = "A";//上午
	public static final String AFTERNOON = "P";//下午
	
	
	//ID_,VARCHAR2(36),不能为空                               --主键ID
	private String id;
	//HolidayName_,VARCHAR2(128)		                 --假期名称
	private String holidayName;
	//StartDate_,TIMESTAMP                               --开始时间
	private Date startDate;
	private String startTime;	//A/P
	//EndDate_,TIMESTAMP                                 --结束时间
	private Date endDate;
	private String endTime;	//A/P
	//HolidayApplyID_,VARCHAR2(36)						 --请假单
	private HolidayApply holidayApply;   				                 
	//Days_,INTEGER                                      --请假天数
	private Double days;
	//Remark_,VARCHAR2(36)					             --请假说明
	private String remark;
	
	private Double cancelDays;//销假天数
	private String cancelRemark;//销假说明

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHolidayName() {
		return holidayName;
	}

	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getStartTimeStr() {
		return MORNING.equals(startTime) ? "上午" : "下午";
	}
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getEndTimeStr() {
		return MORNING.equals(endTime) ? "上午" : "下午";
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public HolidayApply getHolidayApply() {
		return holidayApply;
	}

	public void setHolidayApply(HolidayApply holidayApply) {
		this.holidayApply = holidayApply;
	}

	public Double getDays() {
		return days;
	}

	public void setDays(Double days) {
		this.days = days;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getCancelDays() {
		return cancelDays;
	}

	public void setCancelDays(Double cancelDays) {
		this.cancelDays = cancelDays;
	}

	public String getCancelRemark() {
		return cancelRemark;
	}

	public void setCancelRemark(String cancelRemark) {
		this.cancelRemark = cancelRemark;
	}

}