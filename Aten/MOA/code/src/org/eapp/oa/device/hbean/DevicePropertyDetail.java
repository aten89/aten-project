package org.eapp.oa.device.hbean;

/**
 * 设备属性实体层
 * @author: ZhengChao
 * Email: zhengchao730@163.com
 */

public class DevicePropertyDetail implements java.io.Serializable {

	/**
	 * @author ZhengChao
	 */
	private static final long serialVersionUID = 208807988424560784L;
	
	public static final int PROPERTYTYPE_V = 0;//虚拟属性
	public static final int PROPERTYTYPE_F = 1;//设备属性

	/** 主键ID   ID_  */
	private String id;

	/** 属性名称   PROPERTYNAME_  */
	private String propertyName;
	
	/** 属性值   PropertyValue_  */
	private String propertyValue;

	/** 属性描述   REMARK_  */
	private String remark;
	
	private Device device;
	
	private PurchaseDevice purchaseDevice;

	

	public DevicePropertyDetail() {
	}

	public DevicePropertyDetail(String id) {
		this.id = id;
	}

	public DevicePropertyDetail(String propertyName,String remark) {
		this.propertyName = propertyName;
		this.remark = remark;
	}

//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		final DevicePropertyDetail bo = (DevicePropertyDetail) obj;
//		if (id == null) {
////			if (bo.getId() != null)
//				return false;
//		} else if (!id.equals(bo.getId()))
//			return false;
//		return true;
//	}

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
        DevicePropertyDetail other = (DevicePropertyDetail) obj;
        if (id == null) {
//            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    //生成get/set方法
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public PurchaseDevice getPurchaseDevice() {
		return purchaseDevice;
	}

	public void setPurchaseDevice(PurchaseDevice purchaseDevice) {
		this.purchaseDevice = purchaseDevice;
	}

}
