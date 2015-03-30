package org.eapp.crm.hbean;

/**
 * GroupExt entity. @author MyEclipse Persistence Tools
 */

public class GroupExt implements java.io.Serializable {

	// Fields

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2782430941719688681L;
	private String groupId;
	private String groupName;
	private String businessType;
	private String remark;
	
	public static final String BUSINESS_TYPE_SALE = "S";//销售
	public static final String BUSINESS_TYPE_SERVER = "C";//客服
	public static final String BUSINESS_TYPE_RUNER = "R";//运营

	// Constructors

	/** default constructor */
	public GroupExt() {
	}

	/** full constructor */
	public GroupExt(String businessType, String remark) {
		this.businessType = businessType;
		this.remark = remark;
	}
	
	// Property accessors

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * @return the businessType
	 */
	public String getBusinessType() {
		return businessType;
	}

	/**
	 * @param businessType the businessType to set
	 */
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

}