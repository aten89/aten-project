package org.eapp.oa.device.hbean;

// default package

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.rpc.dto.DataDictInfo;


/**
 * DeviceClass entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DeviceClass implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6717380016430926989L;
	
	// Fields
	public static final int STATUS_DELETE = 0; //已删除
	public static final int STATUS_NORMAL = 1; //正常
	private String id;
	private String name;
	private String remark;
	private String deviceType;
	private Integer status;
	private Set<AreaDeviceCfg> areaDeviceCfgs = new HashSet<AreaDeviceCfg>(0);
	private Set<DeviceCfgItem> deviceCfgItems = new HashSet<DeviceCfgItem>(0);
	private Set<DeviceCheckItem> deviceCheckItems = new HashSet<DeviceCheckItem>(0);
	private Set<DeviceClassAssignArea> deviceClassAssignAreas = new HashSet<DeviceClassAssignArea>(0);
	
	private transient String classItemName;
	private transient String deviceCheckItemStr;
	private transient String deviceCheckItemRemark;
	private transient Boolean mainDevFlag;
	// Constructors

	/** default constructor */
	public DeviceClass() {
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getClassItemName() {
		return classItemName;
	}

	public void setClassItemName(String classItemName) {
		this.classItemName = classItemName;
	}

	public String getDeviceCheckItemStr() {
		return deviceCheckItemStr;
	}

	public void setDeviceCheckItemStr(String deviceCheckItemStr) {
		this.deviceCheckItemStr = deviceCheckItemStr;
	}

	public String getDeviceCheckItemRemark() {
		return deviceCheckItemRemark;
	}

	public void setDeviceCheckItemRemark(String deviceCheckItemRemark) {
		this.deviceCheckItemRemark = deviceCheckItemRemark;
	}

	@JSON(serialize=false)
	public Set<DeviceCfgItem> getDeviceCfgItems() {
		return deviceCfgItems;
	}

	public void setDeviceCfgItems(Set<DeviceCfgItem> deviceCfgItems) {
		this.deviceCfgItems = deviceCfgItems;
	}

	@JSON(serialize=false)
	public Set<DeviceCheckItem> getDeviceCheckItems() {
		return deviceCheckItems;
	}

	public void setDeviceCheckItems(Set<DeviceCheckItem> deviceCheckItems) {
		this.deviceCheckItems = deviceCheckItems;
	}
	
	@JSON(serialize=false)
	public Set<AreaDeviceCfg> getAreaDeviceCfgs() {
		return areaDeviceCfgs;
	}

	public void setAreaDeviceCfgs(Set<AreaDeviceCfg> areaDeviceCfgs) {
		this.areaDeviceCfgs = areaDeviceCfgs;
	}

	@JSON(serialize=false)
	public Set<DeviceClassAssignArea> getDeviceClassAssignAreas() {
		return deviceClassAssignAreas;
	}

	public void setDeviceClassAssignAreas(
			Set<DeviceClassAssignArea> deviceClassAssignAreas) {
		this.deviceClassAssignAreas = deviceClassAssignAreas;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceTypeName() {
		Map<String,DataDictInfo> map = SysCodeDictLoader.getInstance().getDeviceType();
		if(map!=null){
			if (deviceType != null && map.containsKey(deviceType)) {
				return map.get(deviceType).getDictName();
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
		final DeviceClass other = (DeviceClass) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getMainDevFlag() {
		return mainDevFlag;
	}

	public void setMainDevFlag(Boolean mainDevFlag) {
		this.mainDevFlag = mainDevFlag;
	}
}