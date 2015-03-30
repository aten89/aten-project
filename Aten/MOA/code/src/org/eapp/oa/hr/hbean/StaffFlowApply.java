package org.eapp.oa.hr.hbean;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eapp.client.util.UsernameCache;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.rpc.dto.DataDictInfo;

/**
 * 
 */

public class StaffFlowApply implements java.io.Serializable {

	private static final long serialVersionUID = -8588060832747658857L;
	
	public static final int STATUS_DRAFT = 0;//草稿
	public static final int STATUS_APPROVAL = 1;//审批中
	public static final int STATUS_ARCH = 2;//已发布
	public static final int STATUS_CANCELLATION = 3;//作废
	
	// Fields
	public static final int TYPE_ENTRY = 1;//入职
	public static final int TYPE_RESIGN = 2;//离职
	
	public static final String STAFF_STATUS_PROBATION = "试用";
	public static final String STAFF_STATUS_FORMAL = "正式";
	
	private String id;
	private String companyArea;
	private String groupName;
	private String userAccountID;
	private String post;
	private String employeeNumber;
	private String userName;
	private Integer sex;	
	private String idCard;
	private Date birthdate;
	private String description;
	private int applyType;
	private Date entryDate;
	private Date resignDate;
	private String applicant;
	private Date applyDate;
	private String flowInstanceId;
	private Boolean passed;
	private Integer applyStatus;
	private Date archiveDate;
	
	private String staffStatus;//员工状态 SMALLINT
	private String groupFullName;//所属部门组织全称 VARCHAR2(300)
	private String level;//级别 VARCHAR2(100)
	private Date formalDate;//转正时间 TIMESTAMP
	private Integer age;//年龄 NUMBER(3)
	private String education;//学历 VARCHAR2(20)
	private String finishSchool;//毕业学校 VARCHAR2(100)
	private String professional;//专业 VARCHAR2(100)
	private String degree;//学位 VARCHAR2(20)
	private String contractType;//合同类型 VARCHAR2(50)
	private Date contractStartDate;//合同开始时间 TIMESTAMP
	private Date contractEndDate;//合同终止日 TIMESTAMP
	private String mobile;//手机号码 VARCHAR2(20)
	private String officeTel;//办公电话 VARCHAR2(20)
	private String email;//电子邮箱 VARCHAR2(100)
	private String politicalStatus;//政治面貌 VARCHAR2(50)
	private String nation;//民族 VARCHAR2(50)
	private String nativePlace;//籍贯 VARCHAR2(300)
	private String homeAddr;//住址 VARCHAR2(300)
	private String zipCode;//邮编 VARCHAR2(20)
	private String domicilePlace;//户口所在地 VARCHAR2(300)
	private String domicileType;//户口性质 VARCHAR2(50)
	private String maritalStatus;//婚姻状况 SMALLINT
	private int hadKids;//是否生育 SMALLINT
	private int salesExperience;//是否有销售经验 SMALLINT
	private int financialExperience;//是否有金融从业经验 SMALLINT
	private int financialQualification;//是否有金融从业资格 SMALLINT
	private Date workStartDate;//开始工作日期 TIMESTAMP
	private String supervisor;//上级主管 VARCHAR2(36)
	private String recruitmentType;//招聘方式 SMALLINT
	private String recommended;//推荐人 VARCHAR2(50)
	private Integer seniority;//司龄 NUMBER(3)
	private String emergencyContact;//紧急联系人 VARCHAR2(50)
	private String emergencyContactTel;//紧急联系人电话 VARCHAR2(20)
	private String bankCardNO;//银行账号 VARCHAR2(50)
	private String bankType;//开户行 VARCHAR2(50)
	private String trainInfo;//培训情况 VARCHAR2(2000)
	private String skillsInfo;//专业技能信息 VARCHAR2(2000)
	private String resignType;//辞职类型 SMALLINT
	private String resignReason;//辞职原因 VARCHAR2(50)
	private String resignDesc;//辞职原因描述 VARCHAR2(2000)
	private String achievement;//入职累计业绩 VARCHAR2(50)
	private String project;//项目 VARCHAR2(50)
	private Date tranStartDate;//培训开始时间 TIMESTAMP
	private Date tranEndDate;//培训结束时间 TIMESTAMP
	private String tranCost;//培训费用 VARCHAR2(50)
	private String penalty;//违约金 VARCHAR2(50)
	private String staffClass;//员工分类 SMALLINT

	private Set<WorkExperience> workExperiences;
	
	private transient Task task;//不是hibernate属性
	private transient StaffFlowApply refStaffFlowApply;
	private transient int statDatys;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyArea() {
		return companyArea;
	}

	public void setCompanyArea(String companyArea) {
		this.companyArea = companyArea;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getUserAccountID() {
		return userAccountID;
	}

	public void setUserAccountID(String userAccountID) {
		this.userAccountID = userAccountID;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getApplyType() {
		return applyType;
	}

	public void setApplyType(int applyType) {
		this.applyType = applyType;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Date getResignDate() {
		return resignDate;
	}

	public void setResignDate(Date resignDate) {
		this.resignDate = resignDate;
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
	
	public String getApplicantName(){
		if (StringUtils.isBlank(applicant)) {
			return "";
		}
		return UsernameCache.getDisplayName(applicant);
	}
	
	public String getCompanyAreaName() {
		Map<String ,DataDictInfo> areaMap = SysCodeDictLoader.getInstance().getAreaMap();
		if(areaMap != null && this.companyArea != null) {
			DataDictInfo dict = areaMap.get(this.companyArea);
			if(dict != null) {
				return dict.getDictName();
			}
		}
		return "";
	}
	
	
	
	
	
	
	public String getStaffStatus() {
		return staffStatus;
	}

	public void setStaffStatus(String staffStatus) {
		this.staffStatus = staffStatus;
	}

	public String getGroupFullName() {
		return groupFullName;
	}

	public void setGroupFullName(String groupFullName) {
		this.groupFullName = groupFullName;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Date getFormalDate() {
		return formalDate;
	}

	public void setFormalDate(Date formalDate) {
		this.formalDate = formalDate;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getFinishSchool() {
		return finishSchool;
	}

	public void setFinishSchool(String finishSchool) {
		this.finishSchool = finishSchool;
	}

	public String getProfessional() {
		return professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOfficeTel() {
		return officeTel;
	}

	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPoliticalStatus() {
		return politicalStatus;
	}

	public void setPoliticalStatus(String politicalStatus) {
		this.politicalStatus = politicalStatus;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public String getHomeAddr() {
		return homeAddr;
	}

	public void setHomeAddr(String homeAddr) {
		this.homeAddr = homeAddr;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getDomicilePlace() {
		return domicilePlace;
	}

	public void setDomicilePlace(String domicilePlace) {
		this.domicilePlace = domicilePlace;
	}

	public String getDomicileType() {
		return domicileType;
	}

	public void setDomicileType(String domicileType) {
		this.domicileType = domicileType;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public int getHadKids() {
		return hadKids;
	}

	public void setHadKids(int hadKids) {
		this.hadKids = hadKids;
	}

	public int getSalesExperience() {
		return salesExperience;
	}

	public void setSalesExperience(int salesExperience) {
		this.salesExperience = salesExperience;
	}

	public int getFinancialExperience() {
		return financialExperience;
	}

	public void setFinancialExperience(int financialExperience) {
		this.financialExperience = financialExperience;
	}

	public int getFinancialQualification() {
		return financialQualification;
	}

	public void setFinancialQualification(int financialQualification) {
		this.financialQualification = financialQualification;
	}

	public Date getWorkStartDate() {
		return workStartDate;
	}

	public void setWorkStartDate(Date workStartDate) {
		this.workStartDate = workStartDate;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public String getRecruitmentType() {
		return recruitmentType;
	}

	public void setRecruitmentType(String recruitmentType) {
		this.recruitmentType = recruitmentType;
	}

	public String getRecommended() {
		return recommended;
	}

	public void setRecommended(String recommended) {
		this.recommended = recommended;
	}

	public Integer getSeniority() {
		return seniority;
	}

	public void setSeniority(Integer seniority) {
		this.seniority = seniority;
	}
	
	public String getCountSeniority() {
		if (entryDate == null) {
			return "";
		}
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
    	cal.setTime(entryDate);
    	int entryYear = cal.get(Calendar.YEAR);
    	
    	return Integer.toString(year - entryYear);
	}

	public String getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

	public String getEmergencyContactTel() {
		return emergencyContactTel;
	}

	public void setEmergencyContactTel(String emergencyContactTel) {
		this.emergencyContactTel = emergencyContactTel;
	}

	public String getBankCardNO() {
		return bankCardNO;
	}

	public void setBankCardNO(String bankCardNO) {
		this.bankCardNO = bankCardNO;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getTrainInfo() {
		return trainInfo;
	}

	public void setTrainInfo(String trainInfo) {
		this.trainInfo = trainInfo;
	}

	public String getSkillsInfo() {
		return skillsInfo;
	}

	public void setSkillsInfo(String skillsInfo) {
		this.skillsInfo = skillsInfo;
	}

	public String getResignType() {
		return resignType;
	}

	public void setResignType(String resignType) {
		this.resignType = resignType;
	}

	public String getResignReason() {
		return resignReason;
	}

	public void setResignReason(String resignReason) {
		this.resignReason = resignReason;
	}

	public String getResignDesc() {
		return resignDesc;
	}

	public void setResignDesc(String resignDesc) {
		this.resignDesc = resignDesc;
	}

	public String getAchievement() {
		return achievement;
	}

	public void setAchievement(String achievement) {
		this.achievement = achievement;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public Date getTranStartDate() {
		return tranStartDate;
	}

	public void setTranStartDate(Date tranStartDate) {
		this.tranStartDate = tranStartDate;
	}

	public Date getTranEndDate() {
		return tranEndDate;
	}

	public void setTranEndDate(Date tranEndDate) {
		this.tranEndDate = tranEndDate;
	}

	public String getTranCost() {
		return tranCost;
	}

	public void setTranCost(String tranCost) {
		this.tranCost = tranCost;
	}

	public String getPenalty() {
		return penalty;
	}

	public void setPenalty(String penalty) {
		this.penalty = penalty;
	}

	public String getStaffClass() {
		return staffClass;
	}

	public void setStaffClass(String staffClass) {
		this.staffClass = staffClass;
	}

	public Set<WorkExperience> getWorkExperiences() {
		return workExperiences;
	}

	public void setWorkExperiences(Set<WorkExperience> workExperiences) {
		this.workExperiences = workExperiences;
	}

	public StaffFlowApply getRefStaffFlowApply() {
		return refStaffFlowApply;
	}

	public void setRefStaffFlowApply(StaffFlowApply refStaffFlowApply) {
		this.refStaffFlowApply = refStaffFlowApply;
	}

	public int getStatDatys() {
		return statDatys;
	}

	public void setStatDatys(int statDatys) {
		this.statDatys = statDatys;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final StaffFlowApply other = (StaffFlowApply) obj;
		if (userAccountID == null) {
			if (other.userAccountID != null)
				return false;
		} else if (!userAccountID.equals(other.userAccountID))
			return false;
		return true;
	}
}