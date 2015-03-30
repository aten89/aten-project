package org.eapp.oa.travel.hbean;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eapp.client.util.UsernameCache;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.rpc.dto.DataDictInfo;

public class BusTripApply implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1707506228578502484L;

	public static final int STATUS_DRAFT = 0;//草稿
	public static final int STATUS_APPROVAL = 1;//审批中
	public static final int STATUS_ARCH = 2;//已发布
	public static final int STATUS_CANCELLATION = 3;//作废
	
	private String id;
	private String applicant;
	private Date applyDate;
	private String regional;
	private String applyDept;
	private String appointTo;
	private Double borrowSum;
	private String termType;
	private Double totalDays;
	private String tripAppId;//变更关联出差单
	private String changeReason;
	
	private String flowInstanceId;
	private Date archiveDate;
	private Boolean passed;
	private Integer applyStatus;
	private Set<BusTripApplyDetail> busTripApplyDetail = new HashSet<BusTripApplyDetail>(0);

	private transient Task task;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getRegional() {
		return regional;
	}

	public void setRegional(String regional) {
		this.regional = regional;
	}

	public String getRegionalName() {
		Map<String ,DataDictInfo> areaMap = SysCodeDictLoader.getInstance().getAreaMap();
		if(areaMap != null) {
			DataDictInfo dict = areaMap.get(this.regional);
			if(dict != null) {
				return dict.getDictName();
			}
		}
		return "";
	}
	
	public String getApplyDept() {
		return applyDept;
	}

	public void setApplyDept(String applyDept) {
		this.applyDept = applyDept;
	}

	public String getAppointTo() {
		return appointTo;
	}

	public void setAppointTo(String appointTo) {
		this.appointTo = appointTo;
	}

	public Double getBorrowSum() {
		return borrowSum;
	}

	public void setBorrowSum(Double borrowSum) {
		this.borrowSum = borrowSum;
	}

	public String getTermType() {
		return termType;
	}

	public void setTermType(String termType) {
		this.termType = termType;
	}

	public Double getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(Double totalDays) {
		this.totalDays = totalDays;
	}

	public String getTripAppId() {
		return tripAppId;
	}

	public void setTripAppId(String tripAppId) {
		this.tripAppId = tripAppId;
	}

	public Set<BusTripApplyDetail> getBusTripApplyDetail() {
		return busTripApplyDetail;
	}

	public void setBusTripApplyDetail(Set<BusTripApplyDetail> busTripApplyDetail) {
		this.busTripApplyDetail = busTripApplyDetail;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	public String getApplicantName(){
		return UsernameCache.getDisplayName(applicant);
	}

	public String getFlowInstanceId() {
		return flowInstanceId;
	}

	public void setFlowInstanceId(String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
	}

	public Date getArchiveDate() {
		return archiveDate;
	}

	public void setArchiveDate(Date archiveDate) {
		this.archiveDate = archiveDate;
	}

	public Boolean getPassed() {
		return passed;
	}

	public void setPassed(Boolean passed) {
		this.passed = passed;
	}

	public String getAppointToName(){
		if (StringUtils.isBlank(appointTo)) {
			return "";
		}
		return UsernameCache.getDisplayName(appointTo);
	}

	public Integer getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(Integer applyStatus) {
		this.applyStatus = applyStatus;
	}

	public String getChangeReason() {
		return changeReason;
	}

	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}
}
