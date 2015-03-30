package org.eapp.oa.device.hbean;

/**
 * PurchaseDevPurpose entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PurchaseDevPurpose implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -334662917147771530L;
	// Fields

	private String id;
	private DevPurchaseForm devPurchaseForm;
	private String purpose;
	private Boolean selectedFlag;
	private Boolean manyTimeFlag;  
	private transient Integer useCount;
	private transient String deviceClassName;
	private transient Integer useFuzhouCount;
	private transient Integer useXiamenCount;
	private transient Integer useShangHaiCount;
	// Constructors

	public Integer getUseCount() {
		return useCount;
	}

	public void setUseCount(Integer useCount) {
		this.useCount = useCount;
	}

	/** default constructor */
	public PurchaseDevPurpose() {
	}

	/** full constructor */
	public PurchaseDevPurpose(DevPurchaseForm devPurchaseForm,
			String purpose, Boolean selectedFlag) {
		this.devPurchaseForm = devPurchaseForm;
		this.purpose = purpose;
		this.selectedFlag = selectedFlag;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevPurchaseForm getDevPurchaseForm() {
		return this.devPurchaseForm;
	}

	public void setDevPurchaseForm(DevPurchaseForm devPurchaseForm) {
		this.devPurchaseForm = devPurchaseForm;
	}
	
	public String getPurpose() {
		return this.purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public Boolean getSelectedFlag() {
		return this.selectedFlag;
	}

	public void setSelectedFlag(Boolean selectedFlag) {
		this.selectedFlag = selectedFlag;
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
		final PurchaseDevPurpose other = (PurchaseDevPurpose) obj;
		if (id == null) {
//			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Boolean getManyTimeFlag() {
		return manyTimeFlag;
	}

	public void setManyTimeFlag(Boolean manyTimeFlag) {
		this.manyTimeFlag = manyTimeFlag;
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

	public String getDeviceClassName() {
		return deviceClassName;
	}

	public void setDeviceClassName(String deviceClassName) {
		this.deviceClassName = deviceClassName;
	}

	public Integer getUseFuzhouCount() {
		return useFuzhouCount;
	}

	public void setUseFuzhouCount(Integer useFuzhouCount) {
		this.useFuzhouCount = useFuzhouCount;
	}

	public Integer getUseXiamenCount() {
		return useXiamenCount;
	}

	public void setUseXiamenCount(Integer useXiamenCount) {
		this.useXiamenCount = useXiamenCount;
	}

	public Integer getUseShangHaiCount() {
		return useShangHaiCount;
	}

	public void setUseShangHaiCount(Integer useShangHaiCount) {
		this.useShangHaiCount = useShangHaiCount;
	}
	

}