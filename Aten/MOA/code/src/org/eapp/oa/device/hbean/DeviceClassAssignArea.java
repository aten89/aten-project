package org.eapp.oa.device.hbean;


/**
 * DeviceClassAssignArea entity.Description:设备分类授权
 * 
 */
public class DeviceClassAssignArea implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4049979186085558310L;
	private String id; //主键
	private DeviceClass deviceClass;
	private DeviceClassAssign deviceClassAssign;
	// Constructors

	/** default constructor */
	public DeviceClassAssignArea() {
	}

	// Property accessors

	public String getId() {
		return this.id;
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
		final DeviceClassAssignArea other = (DeviceClassAssignArea) obj;
		if (id == null) {
//			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public DeviceClassAssign getDeviceClassAssign() {
		return deviceClassAssign;
	}

	public void setDeviceClassAssign(DeviceClassAssign deviceClassAssign) {
		this.deviceClassAssign = deviceClassAssign;
	}

}