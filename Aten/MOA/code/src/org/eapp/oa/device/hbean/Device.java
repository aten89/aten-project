package org.eapp.oa.device.hbean;
// default package

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eapp.client.util.UsernameCache;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.rpc.dto.DataDictInfo;

/**
 * Device entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Device implements java.io.Serializable {

	// Fields
	public static final String DEVICECLASS_IT ="DEVICE-IT";
	public static final String DEVICECLASS_MEET ="DEVICE-MEET";
	public static final String DEVICECLASS_OFFICE ="DEVICE-OFFICE";
	public static final String BUY_TYPE_SELF ="BUY-TYPE-SELF"; 
	public static final String BUY_TYPE_SUB ="BUY-TYPE-SUB"; 
	public static final String BUY_TYPE_COMP ="BUY-TYPE-COMP"; 
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6678723542478008014L;
	private String id;
	private String deviceNO;
	private String buyType;
	private String deviceType;
	private DeviceClass deviceClass;
	private String deviceName;
	private String deviceModel;
	private String description;
	private String regAccountID;
	private Date regTime;
	private Date buyTime;
	private Boolean deductFlag;
	private Double deductMoney;
	private Date inDate;
	private String areaCode;
	private Integer sequence;
	private Double price;
	private Double financeOriginalVal;//财务原值
	private Set<DeviceUpdateLog> deviceUpdateLogs = new HashSet<DeviceUpdateLog>(0);
	private DeviceCurStatusInfo deviceCurStatusInfo;
	private DevValidateForm devValidateForm;
	private Set<DevRepairForm> devRepairForms = new HashSet<DevRepairForm>(0);
	private Set<DevicePropertyDetail> devicePropertyDetails = new HashSet<DevicePropertyDetail>(0);
	private Set<DiscardDevList> discardDevLists = new HashSet<DiscardDevList>(0);
	
	private transient String deviceClassDisplayName;
	private transient Double buyPrice;
	private transient Double remaining;
	
	
	/**
	 * 配置列表
	 */
	private transient String configList;
	
	// Constructors

	/** default constructor */
	public Device() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeviceNO() {
		return deviceNO;
	}

	public void setDeviceNO(String deviceNO) {
		this.deviceNO = deviceNO;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	public DeviceClass getDeviceClass() {
		return deviceClass;
	}

	public void setDeviceClass(DeviceClass deviceClass) {
		this.deviceClass = deviceClass;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRegAccountID() {
		return regAccountID;
	}

	public void setRegAccountID(String regAccountID) {
		this.regAccountID = regAccountID;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public Date getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}

	public Boolean getDeductFlag() {
		return deductFlag;
	}

	public void setDeductFlag(Boolean deductFlag) {
		this.deductFlag = deductFlag;
	}

	public Double getDeductMoney() {
		return deductMoney;
	}

	public void setDeductMoney(Double deductMoney) {
		this.deductMoney = deductMoney;
	}

	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getFinanceOriginalVal() {
		return financeOriginalVal;
	}

	public void setFinanceOriginalVal(Double financeOriginalVal) {
		this.financeOriginalVal = financeOriginalVal;
	}

	public Set<DeviceUpdateLog> getDeviceUpdateLogs() {
		return deviceUpdateLogs;
	}

	public void setDeviceUpdateLogs(Set<DeviceUpdateLog> deviceUpdateLogs) {
		this.deviceUpdateLogs = deviceUpdateLogs;
	}

	public DeviceCurStatusInfo getDeviceCurStatusInfo() {
		return deviceCurStatusInfo;
	}

	public void setDeviceCurStatusInfo(DeviceCurStatusInfo deviceCurStatusInfo) {
		this.deviceCurStatusInfo = deviceCurStatusInfo;
	}

	public DevValidateForm getDevValidateForm() {
		return devValidateForm;
	}

	public void setDevValidateForm(DevValidateForm devValidateForm) {
		this.devValidateForm = devValidateForm;
	}

	public Set<DevRepairForm> getDevRepairForms() {
		return devRepairForms;
	}

	public void setDevRepairForms(Set<DevRepairForm> devRepairForms) {
		this.devRepairForms = devRepairForms;
	}
	public String getBuyType() {
		return buyType;
	}

	public void setBuyType(String buyType) {
		this.buyType = buyType;
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
		final Device other = (Device) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static String getAreaName(String areaCode) {
		Map<String ,DataDictInfo> areaMap = SysCodeDictLoader.getInstance().getAreaMap();
		if(areaMap != null) {
			DataDictInfo dict = areaMap.get(areaCode);
			if(dict != null) {
				String dictKey = dict.getDictName();
				if(dictKey != null) {
					return dictKey;
				}
			}
		}
		return "";
	}
	
	public String getAreaName() {
		Map<String ,DataDictInfo> areaMap = SysCodeDictLoader.getInstance().getAreaMap();
		if(areaMap != null) {
			DataDictInfo dict = areaMap.get(this.areaCode);
			if(dict != null) {
				String dictKey = dict.getDictName();
				if(dictKey != null) {
					return dictKey;
				}
			}
		}
		return "";
	}

	public String getRegAccountName() {
		return UsernameCache.getDisplayName(this.regAccountID);
	}

	public String getDeviceClassDisplayName() {
		return deviceClassDisplayName;
	}

	public void setDeviceClassDisplayName(String deviceClassDisplayName) {
		this.deviceClassDisplayName = deviceClassDisplayName;
	}

	public Set<DevicePropertyDetail> getDevicePropertyDetails() {
		return devicePropertyDetails;
	}

	public void setDevicePropertyDetails(
			Set<DevicePropertyDetail> devicePropertyDetails) {
		this.devicePropertyDetails = devicePropertyDetails;
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

	public String getBuyTypeName() {
		Map<String ,DataDictInfo> areaMap = SysCodeDictLoader.getInstance().getBuyType();
		if(areaMap != null) {
			DataDictInfo dict = areaMap.get(this.buyType);
			if(dict != null) {
				String dictKey = dict.getDictName();
				if(dictKey != null) {
					return dictKey;
				}
			}
		}
		return "";
	}

	public void setConfigList(String configList) {
		this.configList = configList;
	}

	/**
	 * 获取设备配置信息
	 * @return
	 */
	public String getConfigList() {
		return this.configList;
	}

	public Set<DiscardDevList> getDiscardDevLists() {
		return discardDevLists;
	}

	public void setDiscardDevLists(Set<DiscardDevList> discardDevLists) {
		this.discardDevLists = discardDevLists;
	}

	public Double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(Double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public Double getRemaining() {
		return remaining;
	}

	public void setRemaining(Double remaining) {
		this.remaining = remaining;
	}
	
	
}