package org.eapp.oa.device.hbean;
/**
 * DeviceClassAssign entity.Description:设备分类授权
 * 
 * @author sds
 * @version Sep 1, 2009
 */
public class DeviceClassAssignDetail implements java.io.Serializable {

	public static final int TYPE_USER = 0;//用户
	public static final int TYPE_GROUP = 1;//群组
	public static final int TYPE_POST = 2;//职位
	
	public static final int ASSIGN_MANAGER = 0;//管理授权
	public static final int ASSIGN_SELECT = 1;//查询授权
	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4049979186085558310L;
	private String id; //主键
	private Integer assignClass; // 授权类别
	private Integer type; //授权类型
	private String assignKey;//授权键值
	private DeviceClassAssign deviceClassAssign;

	// Constructors

	/** default constructor */
	public DeviceClassAssignDetail() {
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getAssignKey() {
		return this.assignKey;
	}

	public void setAssignKey(String assignKey) {
		this.assignKey = assignKey;
	}
	
	public DeviceClassAssign getDeviceClassAssign() {
		return deviceClassAssign;
	}

	public void setDeviceClassAssign(DeviceClassAssign deviceClassAssign) {
		this.deviceClassAssign = deviceClassAssign;
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
		final DeviceClassAssignDetail other = (DeviceClassAssignDetail) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Integer getAssignClass() {
		return assignClass;
	}

	public void setAssignClass(Integer assignClass) {
		this.assignClass = assignClass;
	}

}