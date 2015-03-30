package org.eapp.oa.device.hbean;
// default package

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eapp.client.util.UsernameCache;
import org.eapp.oa.system.util.SerialNoCreater;

/**
 * DevValidateForm entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DevValidateForm implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1819125607296808039L;
	public static final int VALITYPE_MOVE = 0;     //调拨验收
	public static final int VALITYPE_BUY = 1;      //采购验收
	public static final int VALITYPE_DIRECT_IN = 2;//登记验收
	public static final int VALITYPE_DISCARD = 3;  //报废验收
	private static Map<Integer , String> valiTypeMap= new HashMap<Integer , String>();
	static{		
		valiTypeMap.put(VALITYPE_MOVE, "调拨验收");
		valiTypeMap.put(VALITYPE_BUY, "采购验收");
		valiTypeMap.put(VALITYPE_DIRECT_IN, "登记验收");
		valiTypeMap.put(VALITYPE_DISCARD, "报废验收");
	}
	// Fields

	private String id;
	private Device device;
	private Integer valiType;
	private String accountID;
	private Date valiDate;
	private String remark;
	private Integer validFormNO;
	private Integer sequenceYear;
	
	private DevAllocateForm devAllocateForm;
	private PurchaseDevice purchaseDevice; 
	private Set<DeviceValiDetail> deviceValiDetails = new HashSet<DeviceValiDetail>(0);
	private DevDiscardForm devDiscardForm;
	// Constructors

	/** default constructor */
	public DevValidateForm() {
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

	public Integer getValiType() {
		return valiType;
	}

	public void setValiType(Integer valiType) {
		this.valiType = valiType;
	}

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public Date getValiDate() {
		return valiDate;
	}

	public void setValiDate(Date valiDate) {
		this.valiDate = valiDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getValidFormNO() {
		return validFormNO;
	}

	public void setValidFormNO(Integer validFormNO) {
		this.validFormNO = validFormNO;
	}

	public Integer getSequenceYear() {
		return sequenceYear;
	}

	public void setSequenceYear(Integer sequenceYear) {
		this.sequenceYear = sequenceYear;
	}
	public Set<DeviceValiDetail> getDeviceValiDetails() {
		return deviceValiDetails;
	}

	public void setDeviceValiDetails(Set<DeviceValiDetail> deviceValiDetails) {
		this.deviceValiDetails = deviceValiDetails;
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
		final DevValidateForm other = (DevValidateForm) obj;
		if (id == null) {
//			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public String getAccountName() {
		return UsernameCache.getDisplayName(this.accountID);
	}

	public DevAllocateForm getDevAllocateForm() {
		return devAllocateForm;
	}

	public void setDevAllocateForm(DevAllocateForm devAllocateForm) {
		this.devAllocateForm = devAllocateForm;
	}


	public DevDiscardForm getDevDiscardForm() {
		return devDiscardForm;
	}

	public void setDevDiscardForm(DevDiscardForm devDiscardForm) {
		this.devDiscardForm = devDiscardForm;
	}

	public PurchaseDevice getPurchaseDevice() {
		return purchaseDevice;
	}

	public void setPurchaseDevice(PurchaseDevice purchaseDevice) {
		this.purchaseDevice = purchaseDevice;
	}

	public String getFullFormNO() {
		return this.sequenceYear+SerialNoCreater.getIsomuxByNum(String.valueOf(this.validFormNO), 4);
	}
	
}