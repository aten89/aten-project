package org.eapp.oa.reimburse.hbean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ReimItem implements java.io.Serializable{
	
	private static final long serialVersionUID = -8115890834220449807L;
	//ID_,VARCHAR2(36),不能为空                             --主键ID
	private String id;
	//ReimbursementID_,VARCHAR2(36)                    --报销单
	private Reimbursement reimbursement;
	private String regionName;
	//CustomName_,VARCHAR2(128)                        --客户名称
	private String customName;
	//TravelDate_,TIMESTAMP                            --出差开始日期
	private Date travelBeginDate;
	//TravelDate_,TIMESTAMP                            --出差结束日期
	private Date travelEndDate;
	//TravelPlace_,VARCHAR2(512)                       --出差地点
	private String travelPlace;
	//CoterielList_,VARCHAR2(1024)                     --同行名单
	private String coterielList;
	//DISPLAYORDER                                      --排序
	private Integer displayOrder;
	
	private Set<OutlayList> outlayLists  = new HashSet<OutlayList>(0);
	
	public Set<OutlayList> getOutlayLists() {
		return outlayLists;
	}
	public void setOutlayLists(Set<OutlayList> outlayLists) {
		this.outlayLists = outlayLists;
	}
	public Integer getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Reimbursement getReimbursement() {
		return reimbursement;
	}
	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}
	
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getCustomName() {
		return customName;
	}
	public void setCustomName(String customName) {
		this.customName = customName;
	}
	public String getTravelPlace() {
		return travelPlace;
	}
	public void setTravelPlace(String travelPlace) {
		this.travelPlace = travelPlace;
	}
	public String getCoterielList() {
		return coterielList;
	}
	public void setCoterielList(String coterielList) {
		this.coterielList = coterielList;
	}
	
	public Date getTravelBeginDate() {
		return travelBeginDate;
	}
	public void setTravelBeginDate(Date travelBeginDate) {
		this.travelBeginDate = travelBeginDate;
	}
	public Date getTravelEndDate() {
		return travelEndDate;
	}
	public void setTravelEndDate(Date travelEndDate) {
		this.travelEndDate = travelEndDate;
	}
	
}
