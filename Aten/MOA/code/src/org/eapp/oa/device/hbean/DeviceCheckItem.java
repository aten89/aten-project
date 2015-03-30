package org.eapp.oa.device.hbean;
// default package



/**
 * DeviceCheckItem entity. @author MyEclipse Persistence Tools
 */

public class DeviceCheckItem  implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8814494647527838154L;
	
	// Fields    

     private String id;
     private String itemName;
     private DeviceClass deviceClass;
     private Integer displayOrder;
     private String remark;
     
     private transient String deviceClassName;
    // Constructors

    /** default constructor */
    public DeviceCheckItem() {
    }
    
    /** full constructor */
    public DeviceCheckItem(String itemName, DeviceClass deviceClass, Integer displayOrder) {
        this.itemName = itemName;
        this.deviceClass = deviceClass;
        this.displayOrder = displayOrder;
    }
   
    // Property accessors

    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public DeviceClass getDeviceClass() {
		return deviceClass;
	}

	public void setDeviceClass(DeviceClass deviceClass) {
		this.deviceClass = deviceClass;
	}

	public String getDeviceClassName() {
		return deviceClassName;
	}

	public void setDeviceClassName(String deviceClassName) {
		this.deviceClassName = deviceClassName;
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
		final DeviceCheckItem other = (DeviceCheckItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}