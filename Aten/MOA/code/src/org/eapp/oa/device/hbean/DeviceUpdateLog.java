package org.eapp.oa.device.hbean;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eapp.client.util.UsernameCache;

// default package

/**
 * DeviceUpdateLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DeviceUpdateLog implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3266924730899033672L;
	public static final int OPTTYPE_REG = 0; //登记
	public static final int OPTTYPE_UPDATE = 1; //更新
	
	public static Map<Integer, String> optTypeMap = new HashMap<Integer, String>();
	static{		
		optTypeMap.put(OPTTYPE_REG, "登记");
		optTypeMap.put(OPTTYPE_UPDATE, "更新");
	}	
	
	// Fields

	private String id;
	private Device device;
	private String operator;
	private Date operateDate;
	private Integer operateType;
	private String updateContent;

	static {
		
	}
	
	// Constructors

	/** default constructor */
	public DeviceUpdateLog() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public Integer getOperateType() {
		return operateType;
	}

	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}

	public String getUpdateContent() {
		return updateContent;
	}

	public void setUpdateContent(String updateContent) {
		this.updateContent = updateContent;
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
		final DeviceUpdateLog other = (DeviceUpdateLog) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getOptTypeDisplayName() {
		return optTypeMap.get(this.operateType);
	}

	public String getOperatorDisplayName() {
		return UsernameCache.getDisplayName(operator);
	}

}