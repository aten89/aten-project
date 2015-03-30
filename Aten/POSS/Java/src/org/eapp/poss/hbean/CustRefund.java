package org.eapp.poss.hbean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;
import org.eapp.client.util.UsernameCache;

/**
 * CustRefund entity. @author MyEclipse Persistence Tools
 */

public class CustRefund implements java.io.Serializable {

	// Fields

	/**
     * 
     */
    private static final long serialVersionUID = 2274364876850868725L;
    
    /**
     * 退款流程key
     */
    public static final String REFUND_FLOW_KEY = "TKLC";
    /**
     * 流程状态:草稿
     */
    public static final String PROCESS_STATUS_DRAFT = "0";
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
    
    private String id;
	private ProdInfo prodInfo;
	private String supplier;
	private String custId;
	private String flowTitle;
	private String proposer;
	private String proposerDept;
	private Date applyTime;
	private String paymentId;
	private Boolean fullRefundFlag;
	private Double refundAmount;
	private String refundReason;
	private String flowInstanceId;
	private Date archiveDate;
	private Boolean passed;
	private String formStatus;
	private Set<Attachment> custRefundAttach = new HashSet<Attachment>(0);
	
	// 客户经理ID
    private transient String saleManId;
    
    private transient String custName;
    
    private transient String applyTimeStr;
    
    private transient String prodName;
	/**
     * 划款审批人
     */
    private transient String approver;
    
    private transient String approverName;
	/**
     * 关联任务
     */
    private transient Task task;

	// Constructors

	/** default constructor */
	public CustRefund() {
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProdInfo getPossProdInfo() {
		return this.prodInfo;
	}

	public void setPossProdInfo(ProdInfo possProdInfo) {
		this.prodInfo = possProdInfo;
	}

	public String getSupplier() {
		return this.supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getCustId() {
		return this.custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getFlowTitle() {
		return this.flowTitle;
	}

	public void setFlowTitle(String flowTitle) {
		this.flowTitle = flowTitle;
	}

	public String getProposer() {
		return this.proposer;
	}
	 public String getProposerName() {
	        return UsernameCache.getDisplayName(proposer);
	    }
	public void setProposer(String proposer) {
		this.proposer = proposer;
	}

	public String getProposerDept() {
		return this.proposerDept;
	}

	public void setProposerDept(String proposerDept) {
		this.proposerDept = proposerDept;
	}

	@JSON(format = "yyyy-MM-dd")
	public Date getApplyTime() {
		return this.applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getPaymentId() {
		return this.paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public Boolean getFullRefundFlag() {
		return this.fullRefundFlag;
	}

	public void setFullRefundFlag(Boolean fullRefundFlag) {
		this.fullRefundFlag = fullRefundFlag;
	}

	public Double getRefundAmount() {
		return this.refundAmount;
	}

	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getRefundReason() {
		return this.refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public String getFlowInstanceId() {
		return this.flowInstanceId;
	}

	public void setFlowInstanceId(String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
	}

	public Date getArchiveDate() {
		return this.archiveDate;
	}

	public void setArchiveDate(Date archiveDate) {
		this.archiveDate = archiveDate;
	}

	public Boolean getPassed() {
		return this.passed;
	}

	public void setPassed(Boolean passed) {
		this.passed = passed;
	}

	public String getFormStatus() {
		return this.formStatus;
	}

	public void setFormStatus(String formStatus) {
		this.formStatus = formStatus;
	}

    /**
     * get the custRefundAttach
     * @return the custRefundAttach
     */
	@JSON(serialize = false)
    public Set<Attachment> getCustRefundAttach() {
        return custRefundAttach;
    }

    /**
     * set the custRefundAttach to set
     * @param custRefundAttach the custRefundAttach to set
     */
    public void setCustRefundAttach(Set<Attachment> custRefundAttach) {
        this.custRefundAttach = custRefundAttach;
    }

	public ProdInfo getProdInfo() {
		return prodInfo;
	}

	public void setProdInfo(ProdInfo prodInfo) {
		this.prodInfo = prodInfo;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getSaleManId() {
		return saleManId;
	}

	public void setSaleManId(String saleManId) {
		this.saleManId = saleManId;
	}


	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getApplyTimeStr() {
		return applyTimeStr;
	}

	public void setApplyTimeStr(String applyTimeStr) {
		this.applyTimeStr = applyTimeStr;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}
}