package org.eapp.oa.travel.hbean;

import java.util.Date;

public class BusTripApplyDetail implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6653677732366734240L;

	private String id;
	private String fromPlace;
	private String toPlace;
	private Date startDate;
	private Date endDate;
	private Double days;
	private String causa;
	private BusTripApply busTripApply;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromPlace() {
		return fromPlace;
	}

	public void setFromPlace(String fromPlace) {
		this.fromPlace = fromPlace;
	}

	public String getToPlace() {
		return toPlace;
	}

	public void setToPlace(String toPlace) {
		this.toPlace = toPlace;
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

	public Double getDays() {
		return days;
	}

	public void setDays(Double days) {
		this.days = days;
	}

	public String getCausa() {
		return causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
	}

	public BusTripApply getBusTripApply() {
		return busTripApply;
	}

	public void setBusTripApply(BusTripApply busTripApply) {
		this.busTripApply = busTripApply;
	}
		
}
