package org.eapp.crm.hbean;

import java.util.Date;

/**
 * ImportCustomer entity. @author MyEclipse Persistence Tools
 */

public class ImportCustomer implements java.io.Serializable {

	// Fields

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2753689019857427951L;
	private String id;
	private String custName;
	private Character sex;
	private String tel;
	private String email;
	private String batchNumber;
	private Boolean allocateFlag;
	private String importUser;
	private Date importTime;
	private String allocateTo;
	private Date allocateTime;
	private String relateCustid;
	
	private String allocateTimeStr;
	private transient String allocateToName;
	
	/**
     * 未推送状态
     */
    public static final boolean STATUS_UNALLOCATE = false;
    /**
     * 推送状态
     */
    public static final boolean STATUS_ALLOCATE = true;

	// Constructors

	/** default constructor */
	public ImportCustomer() {
	}
	
	// Property accessors

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the custName
	 */
	public String getCustName() {
		return custName;
	}

	/**
	 * @param custName the custName to set
	 */
	public void setCustName(String custName) {
		this.custName = custName;
	}

	/**
	 * @return the sex
	 */
	public Character getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(Character sex) {
		this.sex = sex;
	}

	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the batchNumber
	 */
	public String getBatchNumber() {
		return batchNumber;
	}

	/**
	 * @param batchNumber the batchNumber to set
	 */
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	/**
	 * @return the allocateFlag
	 */
	public Boolean getAllocateFlag() {
		return allocateFlag;
	}

	/**
	 * @param allocateFlag the allocateFlag to set
	 */
	public void setAllocateFlag(Boolean allocateFlag) {
		this.allocateFlag = allocateFlag;
	}

	/**
	 * @return the importUser
	 */
	public String getImportUser() {
		return importUser;
	}

	/**
	 * @param importUser the importUser to set
	 */
	public void setImportUser(String importUser) {
		this.importUser = importUser;
	}

	/**
	 * @return the importTime
	 */
	public Date getImportTime() {
		return importTime;
	}

	/**
	 * @param importTime the importTime to set
	 */
	public void setImportTime(Date importTime) {
		this.importTime = importTime;
	}

	/**
	 * @return the allocateTo
	 */
	public String getAllocateTo() {
		return allocateTo;
	}

	/**
	 * @param allocateTo the allocateTo to set
	 */
	public void setAllocateTo(String allocateTo) {
		this.allocateTo = allocateTo;
	}

	/**
	 * @return the allocateTime
	 */
	public Date getAllocateTime() {
		return allocateTime;
	}

	/**
	 * @param allocateTime the allocateTime to set
	 */
	public void setAllocateTime(Date allocateTime) {
		this.allocateTime = allocateTime;
	}

	/**
	 * @return the relateCustid
	 */
	public String getRelateCustid() {
		return relateCustid;
	}

	/**
	 * @param relateCustid the relateCustid to set
	 */
	public void setRelateCustid(String relateCustid) {
		this.relateCustid = relateCustid;
	}

	/**
	 * @return the allocateTimeStr
	 */
	public String getAllocateTimeStr() {
		return allocateTimeStr;
	}

	/**
	 * @param allocateTimeStr the allocateTimeStr to set
	 */
	public void setAllocateTimeStr(String allocateTimeStr) {
		this.allocateTimeStr = allocateTimeStr;
	}

	public String getAllocateToName() {
		return allocateToName;
	}

	public void setAllocateToName(String allocateToName) {
		this.allocateToName = allocateToName;
	}

}