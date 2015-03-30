package org.eapp.oa.device.hbean;
// default package

/**
 * DeviceValiDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DeviceValiDetail implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5116190755723895569L;

	// Fields

	private String id;
	private DevValidateForm devValidateForm;
	private String item;
	private Boolean isEligibility;
	private String remark;

	// Constructors

	/** default constructor */
	public DeviceValiDetail() {
	}

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

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Boolean getIsEligibility() {
		return isEligibility;
	}

	public void setIsEligibility(Boolean isEligibility) {
		this.isEligibility = isEligibility;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
		final DeviceValiDetail other = (DeviceValiDetail) obj;
		if (id == null) {
//			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}