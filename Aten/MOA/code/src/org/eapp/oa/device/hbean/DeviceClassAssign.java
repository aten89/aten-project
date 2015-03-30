package org.eapp.oa.device.hbean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * DeviceClassAssign entity.Description:设备分类授权
 * 
 * @author sds
 * @version Sep 1, 2009
 */
public class DeviceClassAssign implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4049979186085558310L;
	private String id; //主键
	private String areaCode;//所属地区
	private Date configTime;
	private String remark;
	private String assignEdValue;
	private String deviceType;
	private Set<DeviceClassAssignDetail> deviceClassAssignDetails = new HashSet<DeviceClassAssignDetail>();
	private Set<DeviceClassAssignArea> deviceClassAssignAreas = new HashSet<DeviceClassAssignArea>();
	
	private transient String deviceTypeName;
	private transient String classNames;
	private transient String classIds;
	private transient String areaName;
	// Constructors

	/** default constructor */
	public DeviceClassAssign() {
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Date getConfigTime() {
		return configTime;
	}

	public void setConfigTime(Date configTime) {
		this.configTime = configTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<DeviceClassAssignArea> getDeviceClassAssignAreas() {
		return deviceClassAssignAreas;
	}

	public void setDeviceClassAssignAreas(
			Set<DeviceClassAssignArea> deviceClassAssignAreas) {
		this.deviceClassAssignAreas = deviceClassAssignAreas;
	}

	public Set<DeviceClassAssignDetail> getDeviceClassAssignDetails() {
		return deviceClassAssignDetails;
	}

	public void setDeviceClassAssignDetails(
			Set<DeviceClassAssignDetail> deviceClassAssignDetails) {
		this.deviceClassAssignDetails = deviceClassAssignDetails;
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
		final DeviceClassAssign other = (DeviceClassAssign) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getClassNames() {
		return classNames;
	}

	public void setClassNames(String classNames) {
		this.classNames = classNames;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getClassIds() {
		return classIds;
	}

	public void setClassIds(String classIds) {
		this.classIds = classIds;
	}

	public String getAssignEdValue() {
		return assignEdValue;
	}

	public void setAssignEdValue(String assignEdValue) {
		this.assignEdValue = assignEdValue;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceTypeName() {
		return deviceTypeName;
	}

	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}

}