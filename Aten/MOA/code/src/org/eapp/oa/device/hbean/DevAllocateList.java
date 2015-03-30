package org.eapp.oa.device.hbean;

import java.util.Map;

import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.rpc.dto.DataDictInfo;

// default package

/**
 * DevAllocateList entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DevAllocateList implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4281681172895587958L;
	
	// Fields
	public static final int STATUS_TYPE_NO_USE  = 0;//闲置
	public static final int STATUS_TYPE_USE  = 1;//领用
	private String id;
	private DevAllocateForm devAllocateForm;
	private String deviceCfgDesc;
	private String purpose;
	private String areaCodeBef;
	private String areaCode;
	private Device device;
	private String purposeBef;
	private Integer devStatusBef; 
	private Integer displayOrder; 
	
	// Constructors

	/** default constructor */
	public DevAllocateList() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevAllocateForm getDevAllocateForm() {
		return devAllocateForm;
	}

	public void setDevAllocateForm(DevAllocateForm devAllocateForm) {
		this.devAllocateForm = devAllocateForm;
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
	
	public String getAreaCodeBef() {
		return areaCodeBef;
	}

	public void setAreaCodeBef(String areaCodeBef) {
		this.areaCodeBef = areaCodeBef;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getAreaNameBef() {
		Map<String ,DataDictInfo> areaMap = SysCodeDictLoader.getInstance().getAreaMap();
		if(areaMap != null && this.areaCodeBef != null) {
			DataDictInfo dict = areaMap.get(this.areaCodeBef);
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
		if(areaMap != null && this.areaCode != null) {
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
		final DevAllocateList other = (DevAllocateList) obj;
		if (id == null) {
//			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getPurposeBef() {
		return purposeBef;
	}

	public void setPurposeBef(String purposeBef) {
		this.purposeBef = purposeBef;
	}

//	public String getPurposeBefDisplayName() {
//		Map<String ,DataDictInfo> devUseMap = SysCodeDictLoader.getInstance().getDevUseTypeMap();
//		if(devUseMap != null) {
//			DataDictInfo dict = devUseMap.get(this.purposeBef);
//			if(dict != null) {
//				String dictKey = dict.getDictName();
//				if(dictKey != null) {
//					return dictKey;
//				}
//			}
//		}
//		return "";
//	}

	public Integer getDevStatusBef() {
		return devStatusBef;
	}

	public void setDevStatusBef(Integer devStatusBef) {
		this.devStatusBef = devStatusBef;
	}

	@SuppressWarnings("static-access")
	public String getDevStatusBefStr() {
		if(devStatusBef == null || devStatusBef == this.STATUS_TYPE_NO_USE){
			return "闲置";
		}else if(devStatusBef==this.STATUS_TYPE_USE){
			return "已领用";
		}
		return "";
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	
}