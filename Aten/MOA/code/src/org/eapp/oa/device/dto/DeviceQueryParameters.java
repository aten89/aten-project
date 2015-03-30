package org.eapp.oa.device.dto;

import java.sql.Timestamp;

import org.eapp.util.hibernate.QueryParameters;


/**
 * Description: Device查询条件 
 * @author 郑超
 */

public class DeviceQueryParameters extends QueryParameters {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4506345439675423362L;

	public Integer getStatus() {
		return (Integer) this.getParameter("status");
	}

	public void setStatus(Integer status) {
		this.addParameter("status", status);
	}
	public Integer getIsUsered() {
		return (Integer) this.getParameter("isUsered");
	}

	public void setIsUsered(Integer isUsered) {
		this.addParameter("isUsered", isUsered);
	}
	
	public String getDeviceNO() {
		return (String)this.getParameter("deviceNO");
	}

	public void setDeviceNO(String deviceNO) {
		this.addParameter("deviceNO", deviceNO);
	}
	public String getAreaCode() {
		return (String) this.getParameter("areaCode");
	}

	public void setAreaCode(String areaCode) {
		this.addParameter("areaCode", areaCode);
	}
	public String getDeviceClass() {
		return (String) this.getParameter("deviceClass");
	}

	public void setDeviceClass(String deviceClass) {
		this.addParameter("deviceClass", deviceClass);
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

	public void setDeviceName(String deviceName) {
		this.addParameter("deviceName", deviceName);
	}

	public Integer getIsUsing() {
		return (Integer) this.getParameter("isUsing");
	}

	public void setIsUsing(Integer isUsing) {
		this.addParameter("isUsing", isUsing);
	}

	public Timestamp getBeginBuyTime() {
		return (Timestamp) this.getParameter("beginBuyTime");
	}

	public void setBeginBuyTime(Timestamp beginbuyTime) {
		this.addParameter("beginBuyTime", beginbuyTime);
	}
	
	public Timestamp getEndBuyTime() {
		return (Timestamp) this.getParameter("endBuyTime");
	}

	public void setEndBuyTime(Timestamp endbuyTime) {
		this.addParameter("endBuyTime", endbuyTime);
	}

	public String getApplicantID() {
		return (String) this.getParameter("applicantID");
	}

	public void setApplicantID(String applicantID) {
		this.addParameter("applicantID", applicantID);
	}
	public String getGroupName() {
		return (String) this.getParameter("groupName");
	}

	public void setGroupName(String groupName) {
		this.addParameter("groupName", groupName);
	}
	
	public void setIDs(String[] ids) {
		this.addParameter("ids", ids);
	}
	
	public String[] getIDs() {
		return (String[])this.getParameter("ids");
	}
	
	public String[] getBuyTypes() {
		return (String[]) this.getParameter("buyTypes");
	}

	public void setBuyTypes(String[] buyTypes) {
		this.addParameter("buyTypes", buyTypes);
	}
	
	public void setDeviceTypeIDs(String[] deviceTypes) {
		this.addParameter("deviceTypes", deviceTypes);
	}
	
	public String[] getDeviceTypeIDs() {
		return (String[])this.getParameter("deviceTypes");
	}
	
	public void setDeviceClassIDs(String[] deviceClassIDs) {
		this.addParameter("deviceClassIDs", deviceClassIDs);
	}
	
	public String[] getDeviceClassIDs() {
		return (String[])this.getParameter("deviceClassIDs");
	}
	
	public void setStatuses(Integer[] statuses) {
		this.addParameter("statuses", statuses);
	}
	
	public Integer[] getStatuses() {
		return (Integer[])this.getParameter("statuses");
	}
	
	public void setAreaCodes(String[] areaCodes) {
		this.addParameter("areaCodes", areaCodes);
	}
	
	public String[] getAreaCodes() {
		return (String[])this.getParameter("areaCodes");
	}
	public Timestamp getBeginReTime() {
		return (Timestamp) this.getParameter("beginReTime");
	}
	public void setBeginReTime(Timestamp beginReTime) {
		this.addParameter("beginReTime", beginReTime);
	}
	
	public Timestamp getEndReTime() {
		return (Timestamp) this.getParameter("endReTime");
	}

	public void setEndReTime(Timestamp endReTime) {
		this.addParameter("endReTime", endReTime);
	}
	
	public boolean getAssignDevClassFlag() {
		return (Boolean)this.getParameter("assignDevClassFlag");
	}

	public void setAssignDevClassFlag(boolean assignDevClassFlag) {
		this.addParameter("assignDevClassFlag", assignDevClassFlag);
	}
	
	public Boolean getExcludeSelfBuyFlag() {
		return (Boolean)this.getParameter("excludeSelfBuyFlag");
	}

	public void setExcludeSelfBuyFlag(Boolean excludeSelfBuyFlag) {
		this.addParameter("excludeSelfBuyFlag", excludeSelfBuyFlag);
	}
	
	/**
	 * 个人购买
	 * @return
	 */
	public Boolean getExcludeSubBuyFlag() {
		return (Boolean)this.getParameter("excludeSubBuyFlag");
	}

	public void setExcludeSubBuyFlag(Boolean excludeSubBuyFlag) {
		this.addParameter("excludeSubBuyFlag", excludeSubBuyFlag);
	}
	
	public String getDeviceModel(){
		return (String) this.getParameter("deviceModel");
	}
	
	public void setDeviceModel(String deviceModel){
		this.addParameter("deviceModel", deviceModel);
	}
}
