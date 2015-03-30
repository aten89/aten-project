package org.eapp.oa.hr.hbean;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eapp.client.util.UsernameCache;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.rpc.dto.DataDictInfo;


/**
 * HolidayApply entity.
 * Description:假期申请单
 * @author MyEclipse Persistence Tools
 */

public class HolidayApply implements java.io.Serializable {

	private static final long serialVersionUID = -8588060832747658857L;
	
	public static final int STATUS_DRAFT = 0;//草稿
	public static final int STATUS_APPROVAL = 1;//审批中
	public static final int STATUS_ARCH = 2;//已发布
	public static final int STATUS_CANCELLATION = 3;//作废
	public static final int STATUS_CANCELAPPROVAL = 4;//销假审批中
	
	// Fields
	public static final String DEFAULT_FINACE = "0";//总公司
	
	//ID_,VARCHAR2(36),不能为空                               --主键ID
	private String id;
	//Applicant_,VARCHAR2(128)		                     --申请人
	private String applicant;
	//ApplyDate_,TIMESTAMP                               --申请时间
	private Date applyDate;
	//ApplyDept_,VARCHAR2(128)                           --申请部门
	private String applyDept;
	//AppointTo_,VARCHAR2(36)							 --指定审批人
	private String appointTo;
	//IsSpecial_,SMALLINT                                --是否特批
	private Boolean isSpecial;	
	//specialReason_,VARCHAR2(36)					     --特批理由
	private String specialReason;
	//TotalDays_,INTEGER                                 --请假总天数
	private Double totalDays;
	//Regional,VARCHAR2(36)								--所属区域
	private String regional;
//	//FormType_											--表单类别
//	private Integer formType = FORM_TYPE_APPLY;
	//HolidayAppID_										--销假单所关联的请假单
//	private String holidayAppId;
	//CancelFlag_										--是否已经销假
	private Boolean cancelFlag;
	//Remark_,VARCHAR2(36)					             --请假说明
	private String remark;
	
	//FlowInstanceID_,VARCHAR2(36)                       --流程实例
	private String flowInstanceId;
	//Passed,SMALLINT                                    --是否生效
	private Boolean passed;
	//Formstatus_				   						 --假单状态
	private Integer applyStatus;
	// ArchiveDate_,TIMESTAMP      						 --归档时间
	private Date archiveDate;
	
	
	private transient Task task;//不是hibernate属性
	
	private Set<HolidayDetail> holidayDetail = new HashSet<HolidayDetail>(0);
	
	
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
	public Boolean getIsSpecial() {
		return isSpecial;
	}
	public void setIsSpecial(Boolean isSpecial) {
		this.isSpecial = isSpecial;
	}
	public String getSpecialReason() {
		return specialReason;
	}
	public void setSpecialReason(String specialReason) {
		this.specialReason = specialReason;
	}
	public Double getTotalDays() {
		return totalDays;
	}
	public void setTotalDays(Double totalDays) {
		this.totalDays = totalDays;
	}
	public Set<HolidayDetail> getHolidayDetail() {
		return holidayDetail;
	}
	public void setHolidayDetail(Set<HolidayDetail> holidayDetail) {
		this.holidayDetail = holidayDetail;
	}
	public String getFlowInstanceId() {
		return flowInstanceId;
	}
	public void setFlowInstanceId(String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
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
	public Date getArchiveDate() {
		return archiveDate;
	}
	public void setArchiveDate(Date archiveDate) {
		this.archiveDate = archiveDate;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public String getRegional() {
		return regional;
	}
	public void setRegional(String regional) {
		this.regional = regional;
	}
	public String getAppointToName(){
		if (StringUtils.isBlank(appointTo)) {
			return "";
		}
    	return UsernameCache.getDisplayName(appointTo);
	}
	public String getApplicantName(){
		if (StringUtils.isBlank(applicant)) {
			return "";
		}
		return UsernameCache.getDisplayName(applicant);
	}
//	public Integer getFormType() {
//		return formType;
//	}
//	public void setFormType(Integer formType) {
//		this.formType = formType;
//	}
	public Boolean getCancelFlag() {
		return cancelFlag;
	}
	public void setCancelFlag(Boolean cancelFlag) {
		this.cancelFlag = cancelFlag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String toString() {
		StringBuffer str = new StringBuffer(this.getClass().getName());
		str.append("[")
				.append("id=").append(getId())
				.append(",regional=").append(getRegional())
				.append(",applicant=").append(getApplicant())
				.append(",applyDate=").append(getApplyDate())
				.append(",applyDept=").append(getApplyDept())
				.append(",totalDays=").append(getTotalDays())
				.append("]");
		return str.toString();
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
}