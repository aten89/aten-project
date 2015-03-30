package org.eapp.oa.hr.hbean;

import java.util.Date;

/**
 * HolidayType entity.
 * Description:假期种类
 * @author MyEclipse Persistence Tools
 */

public class WorkExperience implements java.io.Serializable {

	private static final long serialVersionUID = -8588060832747658857L;
	//ID_,VARCHAR2(36),不能为空                                --主键ID
	private String id;
	private Date startDate;//起始日期
	private Date endDate;//结束日期
	private String companyName;//公司名称
	private String postName;//岗位名称
	private String postDuty;//岗位职责

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public String getPostDuty() {
		return postDuty;
	}
	public void setPostDuty(String postDuty) {
		this.postDuty = postDuty;
	}
	
}