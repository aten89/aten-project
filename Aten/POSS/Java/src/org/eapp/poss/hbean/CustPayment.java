package org.eapp.poss.hbean;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;
import org.eapp.client.util.UsernameCache;


/**
 * 划款登记实体类 CustPayment
 * 
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-18	黄云耿	新建
 * </pre>
 */
public class CustPayment implements java.io.Serializable {

    
    /**
     * 
     */
    private static final long serialVersionUID = 5327749465474504992L;
    /**
     * 划款流程key
     */
    public static final String PAYMENT_FLOW_KEY = "HKLC";
    /**
     * 非标预约流程key
     */
    public static final String FBPAYMENT_FLOW_KEY = "FBYYLC";
    /**
     * 流程状态:处理
     */
    public static final String PROCESS_STATUS_DEAL = "1";
    /**
     * 流程状态:归档
     */
    public static final String PROCESS_STATUS_ARCH = "2";
    /**
     * 流程状态:作废
     */
    public static final String PROCESS_STATUS_INVALID = "3";
    /**
     * 新客户划款
     */
    public static final String NEW_CUST_PAYMENT = "0";
    /**
     * 老客户划款
     */
    public static final String OLD_CUST_PAYMENT = "1";
    
    public static final Map<String, String> PAY_TYPE_MAP = new HashMap<String, String>();

    static {
        PAY_TYPE_MAP.put(NEW_CUST_PAYMENT, "新客户划款");
        PAY_TYPE_MAP.put(OLD_CUST_PAYMENT, "老客户划款");
    }
    
    
    // 主键
    private String id;
    // 划款类型   0:新客户划款  1：老客户划款
    private String payType;
    // 产品ID
    private String prodId;
    // 客户经理ID
    private String saleManId;
    // 客户ID
    private String custId;
    // 客户名称
    private String custName;
    // 客户性质
    private String custProperty;
    // 预约金额
    private Double appointmentAmount;
    // 预约金额大写
    private String appointmentAmountCapital;
    // 有无订金
    private Boolean payDepositFlag;
    // 划款金额
    private Double transferAmount;
    // 划款金额大写
    private String transferAmountCapital;
    // 预计/划款时间
    private Date transferDate;
    // 备注
    private String remark;
    // 申请人
    private String proposer;
    // 所在部门
    private String proposerDept;
    // 申请时间
    private Date applyTime;
    // 流程实例ID
    private String flowInstanceId;
    // 归档时间
    private Date archiveDate;
    // 是否生效
    private Boolean passed;
    // 表单状态
    private String formStatus;
    // 非标预约标识
    private Boolean standardFlag;
    // 打款凭条
    private Attachment paymentReceipt;
    // 身份证号
    private String identityNum;
    
    // 累计退款金额
    private Double totalRefundAmount = 0.0;
    
    /**
     * 划款审批人
     */
    private transient String approver;
    /**
     * 划款审批人
     */
    private transient String approverName;
    /**
     * 关联任务
     */
    private transient Task task;
    /**
     * 产品名称
     */
    private transient String proName;
    private transient Double syed;
    private transient Double yhked;
    private transient String prodStatus;

    public String getCusManageName() {
        return UsernameCache.getDisplayName(saleManId);
    }

    public String getPayTypeName() {
        return PAY_TYPE_MAP.get(payType);
    }
    
    /** default constructor */
    public CustPayment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustProperty() {
        return custProperty;
    }

    public void setCustProperty(String custProperty) {
        this.custProperty = custProperty;
    }

    public Double getAppointmentAmount() {
        return appointmentAmount;
    }

    public void setAppointmentAmount(Double appointmentAmount) {
        this.appointmentAmount = appointmentAmount;
    }

    public Boolean getPayDepositFlag() {
        return payDepositFlag;
    }

    public void setPayDepositFlag(Boolean payDepositFlag) {
        this.payDepositFlag = payDepositFlag;
    }

    public Double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Double transferAmount) {
        this.transferAmount = transferAmount;
    }

    @JSON(format="yyyy-MM-dd")
    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProposer() {
        return proposer;
    }
    
    public String getProposerName() {
        return UsernameCache.getDisplayName(proposer);
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getProposerDept() {
        return proposerDept;
    }

    public void setProposerDept(String proposerDept) {
        this.proposerDept = proposerDept;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
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

    public String getFormStatus() {
        return formStatus;
    }

    public void setFormStatus(String formStatus) {
        this.formStatus = formStatus;
    }

    public Boolean getStandardFlag() {
        return standardFlag;
    }

    public void setStandardFlag(Boolean standardFlag) {
        this.standardFlag = standardFlag;
    }
    @JSON(serialize=false)
    public Attachment getPaymentReceipt() {
        return paymentReceipt;
    }

    public void setPaymentReceipt(Attachment paymentReceipt) {
        this.paymentReceipt = paymentReceipt;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getSaleManId() {
        return saleManId;
    }

    public void setSaleManId(String saleManId) {
        this.saleManId = saleManId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public Double getSyed() {
        return syed;
    }

    public void setSyed(Double syed) {
        this.syed = syed;
    }

    public Double getYhked() {
        return yhked;
    }

    public void setYhked(Double yhked) {
        this.yhked = yhked;
    }

    public String getProdStatus() {
		return prodStatus;
	}

	public void setProdStatus(String prodStatus) {
		this.prodStatus = prodStatus;
	}

	public String getAppointmentAmountCapital() {
        return appointmentAmountCapital;
    }

    public void setAppointmentAmountCapital(String appointmentAmountCapital) {
        this.appointmentAmountCapital = appointmentAmountCapital;
    }

    public String getTransferAmountCapital() {
        return transferAmountCapital;
    }

    public void setTransferAmountCapital(String transferAmountCapital) {
        this.transferAmountCapital = transferAmountCapital;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public String getIdentityNum() {
        return identityNum;
    }

    public void setIdentityNum(String identityNum) {
        this.identityNum = identityNum;
    }

	public Double getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(Double totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}
    
}