package org.eapp.oa.device.hbean;
// default package

import java.util.Date;
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

public class DevFlowApplyProcess implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1670749607476500733L;

	public static final int FORMSTATUS_UNPUBLISH = 0;// 未发布
	public static final int FORMSTATUS_APPROVAL = 1;// 审批中
	public static final int FORMSTATUS_PUBLISH = 2;// 已归档
	public static final int FORMSTATUS_CANCELLATION = 3;// 作废
	
	public static final String FLOW_NAME_IT_SCRAP = "IT设备报废流程";
	public static final String FLOW_NAME_IT_ALLOT = "IT设备调拨流程";
	public static final String FLOW_NAME_IT_USE = "设备领用/申购流程";
	public static final String FLOW_NAME_IT_PURCHASE = "设备领用/申购流程";
	public static final String FLOW_NAME_IT_LEAVE = "IT设备报废流程";
	
	public static final int PRINT_TYPE_PURCH = 0;//打印申购单
	public static final int PRINT_TYPE_FUND = 1;//打印请款单
	public static final int PRINT_TYPE_VALID = 2;//打印验收单
	public static final int PRINT_TYPE_BACKBUY = 3;//打印回购通知单
	
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
	
	private Integer sequenceYear;
	
	private Task task;
	
	private transient String deviceClass;//在单个设备时需要设置设备类别
	
	private transient String deviceClassDisplayName;
	
	private transient String areaName;
	
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

	/**
	 * 获取申请完整编号：年度+3位序号
	 * @return
	 */
	public String getFullFormNO() {
		String tempNO = String.valueOf(applyFormNO == null ? "" : applyFormNO);
		while(tempNO.length() < 4) {
			tempNO = "0" + tempNO;
		}
		return String.valueOf(sequenceYear == null ? "" : sequenceYear) + tempNO;
	}
	
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	public String getDeviceClass() {
		return deviceClass;
	}

	public void setDeviceClass(String deviceClass) {
		this.deviceClass = deviceClass;
	}
	
	public static String getDeviceTypeDisplayName(String deviceTypeCode) {
		if (deviceTypeCode != null && !"".equals(deviceTypeCode)) {
			Map<String ,DataDictInfo> deviceTypeMap = SysCodeDictLoader.getInstance().getDeviceType();
			if(deviceTypeMap != null) {
				DataDictInfo dict = deviceTypeMap.get(deviceTypeCode);
				if(dict != null) {
					String dictKey = dict.getDictName();
					if(dictKey != null) {
						return dictKey;
					}
				}
			}
		}
		return "";
	}
	
	public String getDeviceClassDisplayName() {
		return deviceClassDisplayName;
	}

	public void setDeviceClassDisplayName(String deviceClassDisplayName) {
		this.deviceClassDisplayName = deviceClassDisplayName;
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
		final DevFlowApplyProcess other = (DevFlowApplyProcess) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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

	public Integer getSequenceYear() {
		return sequenceYear;
	}

	public void setSequenceYear(Integer sequenceYear) {
		this.sequenceYear = sequenceYear;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	
	
}