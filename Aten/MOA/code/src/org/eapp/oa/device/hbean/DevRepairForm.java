package org.eapp.oa.device.hbean;
import java.util.Date;
import java.util.Map;


import org.eapp.client.util.UsernameCache;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.util.SerialNoCreater;
import org.eapp.rpc.dto.DataDictInfo;

// default package

/**
 * DevRepairForm entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DevRepairForm implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1286439457893537114L;

	// Fields

	private String id;
	private Device device;
	private String deviceName;
	private Integer repairDeviceNO;
	private String regAccountID;//登记人
	private Date createTime;
	private String accountID;//申请人
	private String groupName;//申请部门
	private Date applyTime;
	private Integer status;
	private Double budgetMoney;
	private String reason;
	private String remark;
	private String repairMan;
	private Integer sequenceYear;

	private transient String deviceTypeCode;
	
	// Constructors

	/** default constructor */
	public DevRepairForm() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public Integer getRepairDeviceNO() {
		return repairDeviceNO;
	}

	public void setRepairDeviceNO(Integer repairDeviceNO) {
		this.repairDeviceNO = repairDeviceNO;
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
	
	public String getAccountDisplayName() {
		return UsernameCache.getDisplayName(accountID);
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getBudgetMoney() {
		return budgetMoney;
	}

	public void setBudgetMoney(Double budgetMoney) {
		this.budgetMoney = budgetMoney;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRepairMan() {
		return repairMan;
	}

	public void setRepairMan(String repairMan) {
		this.repairMan = repairMan;
	}
	
	public String getDeviceTypeCode() {
		return deviceTypeCode;
	}

	public void setDeviceTypeCode(String deviceTypeCode) {
		this.deviceTypeCode = deviceTypeCode;
	}

	public String getDeviceTypeDisplayName() {
		if (this.deviceTypeCode != null && !"".equals(this.deviceTypeCode)) {
			Map<String ,DataDictInfo> deviceTypeMap = SysCodeDictLoader.getInstance().getDeviceType();
			if(deviceTypeMap != null) {
				DataDictInfo dict = deviceTypeMap.get(this.deviceTypeCode);
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
		final DevRepairForm other = (DevRepairForm) obj;
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

	public String getRepairDeviceCode() {
		return sequenceYear+SerialNoCreater.getIsomuxByNum(repairDeviceNO.toString(),4);
	}
}