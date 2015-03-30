package org.eapp.oa.device.hbean;
// default package

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.util.Tools;
import org.eapp.rpc.dto.DataDictInfo;

/**
 * DevPurchaseForm entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DevPurchaseForm extends DevFlowApplyProcess implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1670749607476500733L;
	// Fields
	public static final int APPLY_TYPE_USE = 0;//领用
	public static final int APPLY_TYPE_PURCHASE = 1;//申购
	public static final int APPLY_TYPE_ALLOT = 2;//调拨生成的
	
	/**
     * 设备用途：研发设备
     */
    public static final String DEV_USETYPE_RESEARCH = "DEV-RESEARCH";
	
	public static Map<Integer, String> applyTypeMap = new HashMap<Integer, String>();
	
	static {
		applyTypeMap.put(APPLY_TYPE_USE, "设备领用");
		applyTypeMap.put(APPLY_TYPE_PURCHASE, "设备申购");
		applyTypeMap.put(APPLY_TYPE_ALLOT, "设备调拨");
	}
	
	private String id;
	private DevValidateForm devValidateForm;
	private Integer applyType;
	private String buyType;
	private Double budgetMoney;
	private Date deliveryDate;
	private String areaCode;
	private String remark;
	private DeviceClass purchaseDeviceClass;
	private String workAreaCode;
	private transient Date planUseDate;
	private transient DevPurchaseList devPurchase;
	private transient PurchaseDevice purchaseDevice;
	private transient String devCfgDesc;
	private transient String purposes;
	private transient Integer useCount;
	private transient String areaCodePurpose;
	private transient String purpose;
	private transient Double tempBuyMoney;//购买金额
	private transient Date tempBuyDate;//取申购设备清单中的最早购买时间
	private Set<DevPurchaseList> devPurchaseLists = new HashSet<DevPurchaseList>(0);
	
	private Set<PurchaseDevice> purchaseDevices = new HashSet<PurchaseDevice>(0);
	
	private Set<PurchaseDevPurpose> purchaseDevPurposes = new HashSet<PurchaseDevPurpose>(0);

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevValidateForm getDevValidateForm() {
		return devValidateForm;
	}

	public void setDevValidateForm(DevValidateForm devValidateForm) {
		this.devValidateForm = devValidateForm;
	}

	public Integer getApplyType() {
		return applyType;
	}

	public void setApplyType(Integer applyType) {
		this.applyType = applyType;
	}

	public String getBuyType() {
		return buyType;
	}

	public void setBuyType(String buyType) {
		this.buyType = buyType;
	}

	public String getBuyTypeDisplayName() {
		if (buyType != null && !"".equals(buyType)) {
			Map<String ,DataDictInfo> buyTypeMap = SysCodeDictLoader.getInstance().getBuyType();
			if(buyTypeMap != null) {
				DataDictInfo dict = buyTypeMap.get(buyType);
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
	
	public static String getBuyTypeDisplayName(String buyType) {
		if (buyType != null && !"".equals(buyType)) {
			Map<String ,DataDictInfo> buyTypeMap = SysCodeDictLoader.getInstance().getBuyType();
			if(buyTypeMap != null) {
				DataDictInfo dict = buyTypeMap.get(buyType);
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
	
	public Double getBudgetMoney() {
		return budgetMoney;
	}

	public void setBudgetMoney(Double budgetMoney) {
		this.budgetMoney = budgetMoney;
	}

	public String getBudgetMoneyStr() {
		return Tools.doubleToString(this.budgetMoney);
	}
	
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDevCfgDesc() {
		return devCfgDesc;
	}

	public void setDevCfgDesc(String devCfgDesc) {
		this.devCfgDesc = devCfgDesc;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<DevPurchaseList> getDevPurchaseLists() {
		return devPurchaseLists;
	}

	public void setDevPurchaseLists(Set<DevPurchaseList> devPurchaseLists) {
		this.devPurchaseLists = devPurchaseLists;
	}

	public Date getPlanUseDate() {
		return planUseDate;
	}

	public void setPlanUseDate(Date planUseDate) {
		this.planUseDate = planUseDate;
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
		final DevPurchaseForm other = (DevPurchaseForm) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getBuyTypeName() {
		Map<String ,DataDictInfo> buyTypeMap = SysCodeDictLoader.getInstance().getBuyType();
		if(buyTypeMap != null) {
			DataDictInfo dict = buyTypeMap.get(this.buyType);
			if(dict != null) {
				String dictKey = dict.getDictName();
				if(dictKey != null) {
					return dictKey;
				}
			}
		}
		return "";
	}

	public String getApplyTypeName() {
		if(this.applyType==APPLY_TYPE_USE){
			return "领用";
		} else if(this.applyType==APPLY_TYPE_PURCHASE){
			return "申购";
		} else if(this.applyType==APPLY_TYPE_PURCHASE){
			return "调拨";
		}
		return "";
	}

	public DevPurchaseList getDevPurchase() {
		return devPurchase;
	}

	public void setDevPurchase(DevPurchaseList devPurchase) {
		this.devPurchase = devPurchase;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Set<PurchaseDevice> getPurchaseDevices() {
		return purchaseDevices;
	}

	public void setPurchaseDevices(Set<PurchaseDevice> purchaseDevices) {
		this.purchaseDevices = purchaseDevices;
	}

	public PurchaseDevice getPurchaseDevice() {
		return purchaseDevice;
	}

	public void setPurchaseDevice(PurchaseDevice purchaseDevice) {
		this.purchaseDevice = purchaseDevice;
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
	
	public String getWorkAreaName() {
		Map<String ,DataDictInfo> areaMap = SysCodeDictLoader.getInstance().getAreaMap();
		if(areaMap != null) {
			DataDictInfo dict = areaMap.get(this.workAreaCode);
			if(dict != null) {
				String dictKey = dict.getDictName();
				if(dictKey != null) {
					return dictKey;
				}
			}
		}
		return "";
	}
	
	public String getAreaNamePurpose() {
		Map<String ,DataDictInfo> areaMap = SysCodeDictLoader.getInstance().getAreaMap();
		if(areaMap != null) {
			DataDictInfo dict = areaMap.get(this.areaCodePurpose);
			if(dict != null) {
				String dictKey = dict.getDictName();
				if(dictKey != null) {
					return dictKey;
				}
			}
		}
		return "";
	}

	public Set<PurchaseDevPurpose> getPurchaseDevPurposes() {
		return purchaseDevPurposes;
	}

	public void setPurchaseDevPurposes(Set<PurchaseDevPurpose> purchaseDevPurposes) {
		this.purchaseDevPurposes = purchaseDevPurposes;
	}

	public String getPurposes() {
		return purposes;
	}

	public void setPurposes(String purposes) {
		this.purposes = purposes;
	}

	public Integer getUseCount() {
		return useCount;
	}

	public void setUseCount(Integer useCount) {
		this.useCount = useCount;
	}

	public String getAreaCodePurpose() {
		return areaCodePurpose;
	}

	public void setAreaCodePurpose(String areaCodePurpose) {
		this.areaCodePurpose = areaCodePurpose;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
//	public String getPurposeName() {
//		Map<String ,DataDictInfo> devUseTypeMap = SysCodeDictLoader.getInstance().getDevUseTypeMap();
//		if(devUseTypeMap != null) {
//			DataDictInfo dict = devUseTypeMap.get(this.purpose);
//			if(dict != null) {
//				String dictKey = dict.getDictName();
//				if(dictKey != null) {
//					return dictKey;
//				}
//			}
//		}
//		return "";
//	}

	public Double getTempBuyMoney() {
		return tempBuyMoney;
	}

	public void setTempBuyMoney(Double tempBuyMoney) {
		this.tempBuyMoney = tempBuyMoney;
	}
	
	public String getTempBuyMoneyStr() {
		return Tools.doubleToString(tempBuyMoney);
	}

	public Date getTempBuyDate() {
		return tempBuyDate;
	}

	public void setTempBuyDate(Date tempBuyDate) {
		this.tempBuyDate = tempBuyDate;
	}
	
	public DeviceClass getPurchaseDeviceClass() {
		return purchaseDeviceClass;
	}

	public void setPurchaseDeviceClass(DeviceClass purchaseDeviceClass) {
		this.purchaseDeviceClass = purchaseDeviceClass;
	}

	public String getWorkAreaCode() {
		return workAreaCode;
	}

	public void setWorkAreaCode(String workAreaCode) {
		this.workAreaCode = workAreaCode;
	}
}