package org.eapp.oa.hr.hbean;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.eapp.client.util.UsernameCache;
import org.eapp.oa.flow.hbean.Task;

public class TransferApply implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4324895424744809344L;
	public static final int STATUS_DRAFT = 0;// 草稿
	public static final int STATUS_APPROVAL = 1;// 审批中
	public static final int STATUS_ARCH = 2;// 已发布
	public static final int STATUS_CANCELLATION = 3;// 作废

	private String id;// 主键
	private String applicant;// 申请人
	private Date applyDate;// 申请时间
	private String transferUser;// 异动员工
	private String contractBody;// 合同主体
	private Date contractStartDate;// 合同开始时间
	private Date contractEndDate;// 合同结束时间
	private Date transferDate;// 调动日期
	private Date entryDate;// 入司日期
	private String transferOutDept;// 调出部门
	private String transferOutPost;// 调出前岗位
	private String transferInDept;// 调入部门
	private String transferInPost;// 调入后岗位
	private Date reportDate;// 报到时间
	private String transferReason;// 调动原因
	private String transitionContent;// 工作交接内容
	private String finaceSupportUser;// 财务支持人员
	private String finaceSupportTel;// 财务支持电话
	private String finaceSupportEmail;// 财务支持邮箱
	private String itSupportUser;// IT支持人员
	private String itSupportTel;// IT支持电话
	private String itSupportEmail;// IT支持邮箱
	private String hrSupportUser;// 人力支持人员
	private String hrSupportTel;// 人力支持电话
	private String hrSupportEmail;// 人力支持邮箱
	private String changeOffice;// 变更后办公地
	private String weeklyReportTo;// 周汇报线
	private String monthlyReportTo;// 月汇报线
	private String flowInstanceID;// 流程实例ID
	private Date archiveDate;// 归档时间
	private Boolean passed;// 是否生效
	private Integer applyStatus;// 表单状态
	
	private transient Task task;//不是hibernate属性
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
	public String getTransferUser() {
		return transferUser;
	}
	public void setTransferUser(String transferUser) {
		this.transferUser = transferUser;
	}
	public String getContractBody() {
		return contractBody;
	}
	public void setContractBody(String contractBody) {
		this.contractBody = contractBody;
	}
	public Date getContractStartDate() {
		return contractStartDate;
	}
	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}
	public Date getContractEndDate() {
		return contractEndDate;
	}
	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}
	public Date getTransferDate() {
		return transferDate;
	}
	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	public String getTransferOutDept() {
		return transferOutDept;
	}
	public void setTransferOutDept(String transferOutDept) {
		this.transferOutDept = transferOutDept;
	}
	public String getTransferOutPost() {
		return transferOutPost;
	}
	public void setTransferOutPost(String transferOutPost) {
		this.transferOutPost = transferOutPost;
	}
	public String getTransferInDept() {
		return transferInDept;
	}
	public void setTransferInDept(String transferInDept) {
		this.transferInDept = transferInDept;
	}
	public String getTransferInPost() {
		return transferInPost;
	}
	public void setTransferInPost(String transferInPost) {
		this.transferInPost = transferInPost;
	}
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public String getTransferReason() {
		return transferReason;
	}
	public void setTransferReason(String transferReason) {
		this.transferReason = transferReason;
	}
	public String getTransitionContent() {
		return transitionContent;
	}
	public void setTransitionContent(String transitionContent) {
		this.transitionContent = transitionContent;
	}
	public String getFinaceSupportUser() {
		return finaceSupportUser;
	}
	public void setFinaceSupportUser(String finaceSupportUser) {
		this.finaceSupportUser = finaceSupportUser;
	}
	public String getFinaceSupportTel() {
		return finaceSupportTel;
	}
	public void setFinaceSupportTel(String finaceSupportTel) {
		this.finaceSupportTel = finaceSupportTel;
	}
	public String getFinaceSupportEmail() {
		return finaceSupportEmail;
	}
	public void setFinaceSupportEmail(String finaceSupportEmail) {
		this.finaceSupportEmail = finaceSupportEmail;
	}
	public String getItSupportUser() {
		return itSupportUser;
	}
	public void setItSupportUser(String supportUser) {
		itSupportUser = supportUser;
	}
	public String getItSupportTel() {
		return itSupportTel;
	}
	public void setItSupportTel(String supportTel) {
		itSupportTel = supportTel;
	}
	public String getItSupportEmail() {
		return itSupportEmail;
	}
	public void setItSupportEmail(String supportEmail) {
		itSupportEmail = supportEmail;
	}
	public String getHrSupportUser() {
		return hrSupportUser;
	}
	public void setHrSupportUser(String supportUser) {
		hrSupportUser = supportUser;
	}
	public String getHrSupportTel() {
		return hrSupportTel;
	}
	public void setHrSupportTel(String supportTel) {
		hrSupportTel = supportTel;
	}
	public String getHrSupportEmail() {
		return hrSupportEmail;
	}
	public void setHrSupportEmail(String supportEmail) {
		hrSupportEmail = supportEmail;
	}
	public String getChangeOffice() {
		return changeOffice;
	}
	public void setChangeOffice(String changeOffice) {
		this.changeOffice = changeOffice;
	}
	public String getWeeklyReportTo() {
		return weeklyReportTo;
	}
	public void setWeeklyReportTo(String weeklyReportTo) {
		this.weeklyReportTo = weeklyReportTo;
	}
	public String getMonthlyReportTo() {
		return monthlyReportTo;
	}
	public void setMonthlyReportTo(String monthlyReportTo) {
		this.monthlyReportTo = monthlyReportTo;
	}
	public String getFlowInstanceID() {
		return flowInstanceID;
	}
	public void setFlowInstanceID(String flowInstanceID) {
		this.flowInstanceID = flowInstanceID;
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
	public Integer getApplyStatus() {
		return applyStatus;
	}
	public void setApplyStatus(Integer applyStatus) {
		this.applyStatus = applyStatus;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public String getApplicantName(){
		if (StringUtils.isBlank(applicant)) {
			return "";
		}
		return UsernameCache.getDisplayName(applicant);
	}
	public String getTransferUserName(){
		if (StringUtils.isBlank(transferUser)) {
			return "";
		}
		return UsernameCache.getDisplayName(transferUser);
	}
	public String getFinaceSupportUserName(){
		if (StringUtils.isBlank(finaceSupportUser)) {
			return "";
		}
		return UsernameCache.getDisplayName(finaceSupportUser);
	}
	public String getItSupportUserName(){
		if (StringUtils.isBlank(itSupportUser)) {
			return "";
		}
		return UsernameCache.getDisplayName(itSupportUser);
	}
	public String getHrSupportUserName(){
		if (StringUtils.isBlank(hrSupportUser)) {
			return "";
		}
		return UsernameCache.getDisplayName(hrSupportUser);
	}
}
