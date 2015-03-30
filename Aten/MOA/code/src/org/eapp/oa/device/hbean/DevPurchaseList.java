package org.eapp.oa.device.hbean;
import java.util.Date;
import java.util.Map;

import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.rpc.dto.DataDictInfo;


// default package

/**
 * DevPurchaseList entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DevPurchaseList implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -7592902975829449759L;
	private String id;
	private DevPurchaseForm devPurchaseForm;
	private String purpose;
	private String areaCode;
	private Date planUseDate;
	private Device device;
	private Date returnBackDate;//归还日期
	private Integer displayOrder;
	
	// Constructors

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	/** default constructor */
	public DevPurchaseList() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevPurchaseForm getDevPurchaseForm() {
		return devPurchaseForm;
	}

	public void setDevPurchaseForm(DevPurchaseForm devPurchaseForm) {
		this.devPurchaseForm = devPurchaseForm;
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
	
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Date getPlanUseDate() {
		return planUseDate;
	}

	public void setPlanUseDate(Date planUseDate) {
		this.planUseDate = planUseDate;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Date getReturnBackDate() {
		return returnBackDate;
	}

	public void setReturnBackDate(Date returnBackDate) {
		this.returnBackDate = returnBackDate;
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
		final DevPurchaseList other = (DevPurchaseList) obj;
		if (id == null) {
			//if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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
	

}