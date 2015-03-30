package org.eapp.oa.device.hbean;
// default package

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.util.Tools;
import org.eapp.rpc.dto.DataDictInfo;


/**
 * Device entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PurchaseDevice implements java.io.Serializable {

	// Fields
	public static final String DEVICECLASS_IT ="DEVICE-IT";
	public static final String DEVICECLASS_MEET ="DEVICE-MEET";
	public static final String DEVICECLASS_OFFICE ="DEVICE-OFFICE";
	public static final String BUY_TYPE_SELF ="BUY-TYPE-SELF"; 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6678723542478008014L;
	private String id;
	private String buyType;
	private DeviceClass deviceClass;
	private String deviceName;
	private String deviceModel;
	private String description;
	private Date buyTime;
	private Boolean deductFlag;
	private Double deductMoney;
	private Date inDate;
	private Double price;
	private String areaCode;
	private String deviceCfgDesc;
	private String belongtoAreaCode;
	private String deviceID;
	private String purpose;
	private Date planUseDate;
	private Date returnBackDate;//归还日期
	private Integer displayOrder;
	
	private DevValidateForm devValidateForm;
	private Set<DevicePropertyDetail> devicePropertyDetails = new HashSet<DevicePropertyDetail>(0);
	private DevPurchaseForm devPurchaseForm;
	private transient String deviceClassDisplayName;
	
	private transient Double buyPrice;
	private transient Double remaining;
	private transient String deviceNo;
	private transient boolean deviceClassMainFlag;
	
	
	/**
	 * 配置列表
	 */
	private transient String configList;
	
	private transient String configListStr;
	
	// Constructors

	/** default constructor */
	public PurchaseDevice() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Date getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPriceStr() {
		return Tools.doubleToString(this.price);
	}
	
	public DevValidateForm getDevValidateForm() {
		return devValidateForm;
	}

	public void setDevValidateForm(DevValidateForm devValidateForm) {
		this.devValidateForm = devValidateForm;
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
		final PurchaseDevice other = (PurchaseDevice) obj;
		if (id == null) {
//			if (other.id != null)
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

	public DevPurchaseForm getDevPurchaseForm() {
		return devPurchaseForm;
	}

	public void setDevPurchaseForm(DevPurchaseForm devPurchaseForm) {
		this.devPurchaseForm = devPurchaseForm;
	}

	public String getBelongtoAreaCode() {
		return belongtoAreaCode;
	}

	public void setBelongtoAreaCode(String belongtoAreaCode) {
		this.belongtoAreaCode = belongtoAreaCode;
	}
	
	public String getBelongtoAreaName() {
		Map<String ,DataDictInfo> areaMap = SysCodeDictLoader.getInstance().getAreaMap();
		if(areaMap != null) {
			DataDictInfo dict = areaMap.get(this.belongtoAreaCode);
			if(dict != null) {
				String dictKey = dict.getDictName();
				if(dictKey != null) {
					return dictKey;
				}
			}
		}
		return "";
	}
	
	public String getDeviceCfgDesc() {
		return deviceCfgDesc;
	}

	public void setDeviceCfgDesc(String deviceCfgDesc) {
		this.deviceCfgDesc = deviceCfgDesc;
	}
	
	public String getPurpose() {
		return purpose;
	}
	
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

//	public String getPurposeDisplayName() {
//		Map<String ,DataDictInfo> devUseMap = SysCodeDictLoader.getInstance().getDevUseTypeMap();
//		if(devUseMap != null) {
//			DataDictInfo dict = devUseMap.get(this.purpose);
//			if(dict != null) {
//				String dictKey = dict.getDictName();
//				if(dictKey != null) {
//					return dictKey;
//				}
//			}
//		}
//		return "";
//	}
	
	public Date getPlanUseDate() {
		return planUseDate;
	}

	public void setPlanUseDate(Date planUseDate) {
		this.planUseDate = planUseDate;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
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

	public Date getReturnBackDate() {
		return returnBackDate;
	}

	public void setReturnBackDate(Date returnBackDate) {
		this.returnBackDate = returnBackDate;
	}

	public String getConfigListStr() {
		return configListStr;
	}

	public void setConfigListStr(String configListStr) {
		this.configListStr = configListStr;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public boolean isDeviceClassMainFlag() {
		return deviceClassMainFlag;
	}

	public void setDeviceClassMainFlag(boolean deviceClassMainFlag) {
		this.deviceClassMainFlag = deviceClassMainFlag;
	}
	
	
}