package org.eapp.oa.hr.hbean;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.eapp.client.util.UsernameCache;
import org.eapp.oa.flow.hbean.Task;

public class PositiveApply implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8280003605323084429L;
	public static final int STATUS_DRAFT = 0;// 草稿
	public static final int STATUS_APPROVAL = 1;// 审批中
	public static final int STATUS_ARCH = 2;// 已发布
	public static final int STATUS_CANCELLATION = 3;// 作废
	
	public static final String FORMAL_TYPE_EARLY = "EARLY";//提前转正
	public static final String FORMAL_TYPE_END = "END";//结束试用

	private String id;// 主键
	private String applicant;// 申请人
	private Date applyDate;// 申请时间
	private String positiveUser;// 转正员工
	private String dept;// 所属部门
	private Date entryDate;// 入职时间
	private Integer sex;// 性别
	private String post;// 职位
	private Integer probation;// 试用期（月）
	private Date formalDate;// 转正时间
	private String formalType;// 转正类别
	private String workResults;// 试用期工作成果
	private String cultureUnderstand;// 企业文化理解
	private String rulesCompliance;// 规章制度遵守情况
	private String workExperience;// 工作体会和收获
	private String workSummary;// 总结优势和不足
	private String workImprove;// 改进或提升计划
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
	public String getPositiveUser() {
		return positiveUser;
	}
	public void setPositiveUser(String positiveUser) {
		this.positiveUser = positiveUser;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public Integer getProbation() {
		return probation;
	}
	public void setProbation(Integer probation) {
		this.probation = probation;
	}
	public Date getFormalDate() {
		return formalDate;
	}
	public void setFormalDate(Date formalDate) {
		this.formalDate = formalDate;
	}
	public String getFormalType() {
		return formalType;
	}
	public void setFormalType(String formalType) {
		this.formalType = formalType;
	}
	public String getWorkResults() {
		return workResults;
	}
	public void setWorkResults(String workResults) {
		this.workResults = workResults;
	}
	public String getCultureUnderstand() {
		return cultureUnderstand;
	}
	public void setCultureUnderstand(String cultureUnderstand) {
		this.cultureUnderstand = cultureUnderstand;
	}
	public String getRulesCompliance() {
		return rulesCompliance;
	}
	public void setRulesCompliance(String rulesCompliance) {
		this.rulesCompliance = rulesCompliance;
	}
	public String getWorkExperience() {
		return workExperience;
	}
	public void setWorkExperience(String workExperience) {
		this.workExperience = workExperience;
	}
	public String getWorkSummary() {
		return workSummary;
	}
	public void setWorkSummary(String workSummary) {
		this.workSummary = workSummary;
	}
	public String getWorkImprove() {
		return workImprove;
	}
	public void setWorkImprove(String workImprove) {
		this.workImprove = workImprove;
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
	
	public String getPositiveUserName(){
		if (StringUtils.isBlank(positiveUser)) {
			return "";
		}
		return UsernameCache.getDisplayName(positiveUser);
	}
}
