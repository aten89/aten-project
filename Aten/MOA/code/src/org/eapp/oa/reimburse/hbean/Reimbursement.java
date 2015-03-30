package org.eapp.oa.reimburse.hbean;

// default package

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
 * Reimbursement entity.
 * Description:报销单
 * @author MyEclipse Persistence Tools
 */

public class Reimbursement implements java.io.Serializable {

	// Fields
	public static final String DEFAULT_FINACE = "0";//总公司
	
	public static final int STATUS_DRAFT = 0;//草稿
	public static final int STATUS_APPROVAL = 1;//审批中
	public static final int STATUS_ARCH = 2;//已发布
	public static final int STATUS_CANCELLATION = 3;//作废
	/**
	 * 
	 */
	private static final long serialVersionUID = 2849616642523300162L;
	
	//ID_,VARCHAR2(36),不能为空                             --主键ID
	private String id;
	//Finance_,VARCHAR2(10)                            --财务隶属
	private String finance;
	//Applicant_,VARCHAR2(128)                         --报销人
	private String applicant;
	//ApplyDate_,TIMESTAMP                             --填单日期
	private Date applyDate;
	//ApplyDept_,VARCHAR2(128)                         --所属部门
	private String applyDept;
	//Payee_,VARCHAR2(128)                             --受款人
	private String payee;
	//Causa_,VARCHAR2(4000)                            --报销事由
	private String causa;
	//LoanSum_,NUMBER(10,2)                            --预借款额
	private Double loanSum;
	//ReimbursementSum_,NUMBER(10,2)                   --报销总额
	private Double reimbursementSum;
	//Passed_,SMALLINT                                 --是否生效
	private Boolean passed;
	//ArchiveDate_,TIMESTAMP                           --归档时间
	private Date archiveDate;
	//flowInstanceId_,TIMESTAMP                        --流程实例
	private String flowInstanceId;
	private Integer formStatus;
	//指定审批人
	private String appointTo;
	
	private Set<ReimItem> reimItems  = new HashSet<ReimItem>(0);

	private transient Task task;//不是hibernate属性
	// Constructors

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	/** default constructor */
	public Reimbursement() {
	}


	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFinance() {
		return this.finance;
	}

	public void setFinance(String finance) {
		this.finance = finance;
	}

	public String getApplicant() {
		return this.applicant;
	}
	
	public String getApplicantName() {
		return UsernameCache.getDisplayName(applicant);
	}
	
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public Date getApplyDate() {
		return this.applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getApplyDept() {
		return this.applyDept;
	}

	public void setApplyDept(String applyDept) {
		this.applyDept = applyDept;
	}

	public String getPayee() {
		return this.payee;
	}
	
	public String getPayeeName() {
		return UsernameCache.getDisplayName(payee);
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getCausa() {
		return this.causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
	}


	public Double getLoanSum() {
		return this.loanSum;
	}

	public void setLoanSum(Double loanSum) {
		this.loanSum = loanSum;
	}

	public Double getReimbursementSum() {
		return this.reimbursementSum;
	}

	public void setReimbursementSum(Double reimbursementSum) {
		this.reimbursementSum = reimbursementSum;
	}

	public Boolean getPassed() {
		return this.passed;
	}

	public void setPassed(Boolean passed) {
		this.passed = passed;
	}

	public Date getArchiveDate() {
		return this.archiveDate;
	}

	public void setArchiveDate(Date archiveDate) {
		this.archiveDate = archiveDate;
	}

	
	public Set<ReimItem> getReimItems() {
		return reimItems;
	}

	public void setReimItems(Set<ReimItem> reimItems) {
		this.reimItems = reimItems;
	}

	public void setOutlayListsAndCount(Reimbursement reimbursement,Set<ReimItem> reimItems) {
			
			Set<ReimItem> setReimItems=new HashSet<ReimItem>();
			double sum = 0.0;
			if(reimItems != null && reimItems.size() > 0){
				for(ReimItem r :  reimItems){
					Set<OutlayList> newOutlayLists = new HashSet<OutlayList>();
					Set<OutlayList> outlayLists = r.getOutlayLists();
					if(outlayLists != null && outlayLists.size() > 0){
						for (OutlayList ol : outlayLists) {
							ol.setReimbursement(reimbursement);
							newOutlayLists.add(ol);
							if (ol.getOutlaySum() != null) {
								sum = sum + ol.getOutlaySum();
							}
						}
						r.setOutlayLists(newOutlayLists);
					}
					setReimItems.add(r);
				}
				
			}
			reimbursement.reimItems = setReimItems;
			reimbursement.setReimbursementSum(new Double(sum));
		}
	public String getFlowInstanceId() {
		return flowInstanceId;
	}

	public void setFlowInstanceId(String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Reimbursement other = (Reimbursement) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String toString() {
		StringBuffer str = new StringBuffer(this.getClass().getName());
		str.append("[")
				.append("id=").append(getId())
				.append(",finance=").append(getFinance())
				.append(",applicant=").append(getApplicant())
				.append(",applyDate=").append(getApplyDate())
				.append(",applyDept=").append(getApplyDept())
				.append(",payee=").append(getPayee())
				.append(",reimbursementSum=").append(getReimbursementSum())
				.append("]");
		return str.toString();
	}
	public String getAppointTo() {
		return appointTo;
	}
	
	public String getAppointToName() {
		if (StringUtils.isBlank(appointTo)) {
			return "";
		}
		return UsernameCache.getDisplayName(appointTo);
	}
	
	public void setAppointTo(String appointTo) {
		this.appointTo = appointTo;
	}
	
	public Integer getFormStatus() {
		return formStatus;
	}

	public void setFormStatus(Integer formStatus) {
		this.formStatus = formStatus;
	}

	public String getFinanceName() {
		Map<String ,DataDictInfo> areaMap = SysCodeDictLoader.getInstance().getAreaMap();
		if(areaMap != null) {
			DataDictInfo dict = areaMap.get(this.finance);
			if(dict != null) {
				return dict.getDictName();
			}
		}
		return "";
	}
}