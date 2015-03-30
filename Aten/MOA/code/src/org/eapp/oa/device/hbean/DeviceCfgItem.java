package org.eapp.oa.device.hbean;
// default package



/**
 * DeviceCfgItem entity. @author MyEclipse Persistence Tools
 */

public class DeviceCfgItem  implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5687236209890910440L;
	
	// Fields    

     private String id;
     private DeviceClass deviceClass;
     private DeviceProperty deviceProperty;
     private Integer displayOrder;
     private String remark;


    // Constructors

    /** default constructor */
    public DeviceCfgItem() {
    }
    
    /** full constructor */
    public DeviceCfgItem(DeviceClass deviceClass, DeviceProperty deviceProperty, Integer displayOrder, String remark) {
        this.deviceClass = deviceClass;
        this.deviceProperty = deviceProperty;
        this.displayOrder = displayOrder;
        this.remark = remark;
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

	

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
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
		final DeviceCfgItem other = (DeviceCfgItem) obj;
		if (id == null) {
//			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public DeviceProperty getDeviceProperty() {
		return deviceProperty;
	}

	public void setDeviceProperty(DeviceProperty deviceProperty) {
		this.deviceProperty = deviceProperty;
	}
}