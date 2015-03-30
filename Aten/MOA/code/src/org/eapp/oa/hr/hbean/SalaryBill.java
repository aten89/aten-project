package org.eapp.oa.hr.hbean;
import java.util.Date;


public class SalaryBill implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7076277189437083082L;
	
	 private String id;//主键
	 private String userAccountID;//用户账号
	 private Integer month;//月份
	 private Date importDate;//导入时间
	 private String dept;//部门
	 private String post;//职务
	 private String employeeNumber;//工号
	 private String userName;//姓名
	 private Date entryDate;//入职日期
	 private Integer peopleNums;//人数
	 private String status;//状态
	 private Float wageTotal;//工资总额
	 private Float wageBasic;//基本工资
	 private Float wagePerformance;//绩效工资
	 private Integer scorePerformance;//绩效得分
	 private Float wagePerformanceReal;//实际绩效工资
	 private Float allowance;//补助
	 private Float commission;//提成
	 private Integer lessMonthDays;//不足月天数
	 private Float deductLessMonth;//不足月扣款
	 private Integer leaveCompassionate;//事假天数
	 private Float deductCompassionate;//事假扣款
	 private Integer leaveSick;//病假天数
	 private Float deductSick;//病假扣款
	 private Float deductLate;//迟到扣款
	 private Float deductElse;//其它补/扣款
	 private Float wagePayable;//应发工资总额
	 private Float pension;//养老个人
	 private Float lostJob;//失业个人
	 private Float medical;//医疗个人
	 private Float insurancePayment;//社保、公积金补缴
	 private Float costSocialSecurity;//本月社保扣款
	 private Float costAccumulationFund;//本月公积金扣款
	 private Float costFiveInsurance;//五险一金扣款
	 private Float taxWage;//应税工资
	 private Float wagePreTax;//税前工资
	 private Float taxPersonal;//个税
	 private Float wageReal;//工资实发
	 private Float wageAllowance;//补发工资


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserAccountID() {
		return userAccountID;
	}
	public void setUserAccountID(String userAccountID) {
		this.userAccountID = userAccountID;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Date getImportDate() {
		return importDate;
	}
	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
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
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	public Integer getPeopleNums() {
		return peopleNums;
	}
	public void setPeopleNums(Integer peopleNums) {
		this.peopleNums = peopleNums;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Float getWageTotal() {
		return wageTotal;
	}
	public void setWageTotal(Float wageTotal) {
		this.wageTotal = wageTotal;
	}
	public Float getWageBasic() {
		return wageBasic;
	}
	public void setWageBasic(Float wageBasic) {
		this.wageBasic = wageBasic;
	}
	public Float getWagePerformance() {
		return wagePerformance;
	}
	public void setWagePerformance(Float wagePerformance) {
		this.wagePerformance = wagePerformance;
	}
	public Integer getScorePerformance() {
		return scorePerformance;
	}
	public void setScorePerformance(Integer scorePerformance) {
		this.scorePerformance = scorePerformance;
	}
	public Float getWagePerformanceReal() {
		return wagePerformanceReal;
	}
	public void setWagePerformanceReal(Float wagePerformanceReal) {
		this.wagePerformanceReal = wagePerformanceReal;
	}
	public Float getAllowance() {
		return allowance;
	}
	public void setAllowance(Float allowance) {
		this.allowance = allowance;
	}
	public Float getCommission() {
		return commission;
	}
	public void setCommission(Float commission) {
		this.commission = commission;
	}
	public Integer getLessMonthDays() {
		return lessMonthDays;
	}
	public void setLessMonthDays(Integer lessMonthDays) {
		this.lessMonthDays = lessMonthDays;
	}
	public Float getDeductLessMonth() {
		return deductLessMonth;
	}
	public void setDeductLessMonth(Float deductLessMonth) {
		this.deductLessMonth = deductLessMonth;
	}
	public Integer getLeaveCompassionate() {
		return leaveCompassionate;
	}
	public void setLeaveCompassionate(Integer leaveCompassionate) {
		this.leaveCompassionate = leaveCompassionate;
	}
	public Float getDeductCompassionate() {
		return deductCompassionate;
	}
	public void setDeductCompassionate(Float deductCompassionate) {
		this.deductCompassionate = deductCompassionate;
	}
	public Integer getLeaveSick() {
		return leaveSick;
	}
	public void setLeaveSick(Integer leaveSick) {
		this.leaveSick = leaveSick;
	}
	public Float getDeductSick() {
		return deductSick;
	}
	public void setDeductSick(Float deductSick) {
		this.deductSick = deductSick;
	}
	public Float getDeductLate() {
		return deductLate;
	}
	public void setDeductLate(Float deductLate) {
		this.deductLate = deductLate;
	}
	public Float getDeductElse() {
		return deductElse;
	}
	public void setDeductElse(Float deductElse) {
		this.deductElse = deductElse;
	}
	public Float getWagePayable() {
		return wagePayable;
	}
	public void setWagePayable(Float wagePayable) {
		this.wagePayable = wagePayable;
	}
	public Float getPension() {
		return pension;
	}
	public void setPension(Float pension) {
		this.pension = pension;
	}
	public Float getLostJob() {
		return lostJob;
	}
	public void setLostJob(Float lostJob) {
		this.lostJob = lostJob;
	}
	public Float getMedical() {
		return medical;
	}
	public void setMedical(Float medical) {
		this.medical = medical;
	}
	public Float getInsurancePayment() {
		return insurancePayment;
	}
	public void setInsurancePayment(Float insurancePayment) {
		this.insurancePayment = insurancePayment;
	}
	public Float getCostSocialSecurity() {
		return costSocialSecurity;
	}
	public void setCostSocialSecurity(Float costSocialSecurity) {
		this.costSocialSecurity = costSocialSecurity;
	}
	public Float getCostAccumulationFund() {
		return costAccumulationFund;
	}
	public void setCostAccumulationFund(Float costAccumulationFund) {
		this.costAccumulationFund = costAccumulationFund;
	}
	public Float getCostFiveInsurance() {
		return costFiveInsurance;
	}
	public void setCostFiveInsurance(Float costFiveInsurance) {
		this.costFiveInsurance = costFiveInsurance;
	}
	public Float getTaxWage() {
		return taxWage;
	}
	public void setTaxWage(Float taxWage) {
		this.taxWage = taxWage;
	}
	public Float getWagePreTax() {
		return wagePreTax;
	}
	public void setWagePreTax(Float wagePreTax) {
		this.wagePreTax = wagePreTax;
	}
	public Float getTaxPersonal() {
		return taxPersonal;
	}
	public void setTaxPersonal(Float taxPersonal) {
		this.taxPersonal = taxPersonal;
	}
	public Float getWageReal() {
		return wageReal;
	}
	public void setWageReal(Float wageReal) {
		this.wageReal = wageReal;
	}
	public Float getWageAllowance() {
		return wageAllowance;
	}
	public void setWageAllowance(Float wageAllowance) {
		this.wageAllowance = wageAllowance;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SalaryBill other = (SalaryBill) obj;
		if (id == null) {
//			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
