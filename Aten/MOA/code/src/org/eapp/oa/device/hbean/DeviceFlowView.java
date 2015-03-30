package org.eapp.oa.device.hbean;
// default package

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eapp.client.util.UsernameCache;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.rpc.dto.DataDictInfo;

/**
 * DevPurchaseForm entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DeviceFlowView implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1670749607476500733L;

	public static final int FORMSTATUS_UNPUBLISH = 0;// 未发布
	public static final int FORMSTATUS_APPROVAL = 1;// 审批中
	public static final int FORMSTATUS_PUBLISH = 2;// 已归档
	public static final int FORMSTATUS_CANCELLATION = 3;// 作废
	
	
	public static final int FORM_TYPE_USE = 0;// 领用
	public static final int FORM_TYPE_PERCHASE = 1;// 申购
	public static final int FORM_TYPE_ALLOCATE = 2;// 调拨
	public static final int FORM_TYPE_DISCARD = 3;// 报废
	public static final int FORM_TYPE_LEAVE = 4;// 离职处理
	
	public static Map<Integer, String> formTypeMap = new HashMap<Integer, String>();
	static{		
		formTypeMap.put(FORM_TYPE_USE, "领用");
		formTypeMap.put(FORM_TYPE_PERCHASE, "申购");
		formTypeMap.put(FORM_TYPE_ALLOCATE, "调拨");
		formTypeMap.put(FORM_TYPE_DISCARD, "报废");
		formTypeMap.put(FORM_TYPE_LEAVE, "离职处理");
	}	
	public static Map<Integer, String> statusMap = new HashMap<Integer, String>();
	static{		
		statusMap.put(FORMSTATUS_PUBLISH, "归档");
		statusMap.put(FORMSTATUS_CANCELLATION, "作废");
		statusMap.put(FORMSTATUS_APPROVAL, "审批中");
	}	
	// Fields

	private String id;
	private String regAccountID;
	private Date regTime;
	private String applicant;
	private String applyGroupName;
	private Date applyDate;
	private String flowInstanceID;
	private Integer formStatus;
	private Boolean passed;
	private Date archiveDate;
	private Integer applyFormNO;
	private String deviceType;
	private Integer formType;
	private Integer sequenceYear;
	private String applyCode;
	
	private transient Task task;;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegAccountID() {
		return regAccountID;
	}

	public void setRegAccountID(String regAccountID) {
		this.regAccountID = regAccountID;
	}

	public String getRegAccountDisplayName() {
		return UsernameCache.getDisplayName(regAccountID);
	}
	
	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public String getApplicantDisplayName() {
		return UsernameCache.getDisplayName(applicant);
	}
	public String getApplyGroupName() {
		return applyGroupName;
	}

	public void setApplyGroupName(String applyGroupName) {
		this.applyGroupName = applyGroupName;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public Integer getFormStatus() {
		return formStatus;
	}

	public void setFormStatus(Integer formStatus) {
		this.formStatus = formStatus;
	}

	public String getFlowInstanceID() {
		return flowInstanceID;
	}

	public void setFlowInstanceID(String flowInstanceID) {
		this.flowInstanceID = flowInstanceID;
	}

	public Boolean getPassed() {
		return passed;
	}

	public void setPassed(Boolean passed) {
		this.passed = passed;
	}

	public Date getArchiveDate() {
		return archiveDate;
	}

	public void setArchiveDate(Date archiveDate) {
		this.archiveDate = archiveDate;
	}

	public Integer getApplyFormNO() {
		return applyFormNO;
	}

	public void setApplyFormNO(Integer applyFormNO) {
		this.applyFormNO = applyFormNO;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DeviceFlowView other = (DeviceFlowView) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public Integer getSequenceYear() {
		return sequenceYear;
	}

	public void setSequenceYear(Integer sequenceYear) {
		this.sequenceYear = sequenceYear;
	}

	public Integer getFormType() {
		return formType;
	}

	public void setFormType(Integer formType) {
		this.formType = formType;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public String getApplyCode() {
		return applyCode;
	}

	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}
	public String getDeviceTypeName() {
		Map<String ,DataDictInfo> areaMap = SysCodeDictLoader.getInstance().getDeviceType();
		if(areaMap != null) {
			DataDictInfo dict = areaMap.get(this.deviceType);
			if(dict != null) {
				String dictKey = dict.getDictName();
				if(dictKey != null) {
					return dictKey;
				}
			}
		}
		return "";
	}
}