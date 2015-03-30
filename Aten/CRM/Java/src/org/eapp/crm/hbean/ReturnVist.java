package org.eapp.crm.hbean;

import java.util.Date;

/**
 * ReturnVist entity. @author MyEclipse Persistence Tools
 */

public class ReturnVist implements java.io.Serializable {

	// Fields

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1944882220510397130L;
	private String id;
	private CustomerInfo customerInfo;
	private String content;
	private Date returnVistTime;
	private String returnVistUser;
	/**
     * 回访时间设置格式显示
     */
    private transient String returnVistTimeStr;
    private transient String returnVistUserName;
    
	// Constructors

	public String getReturnVistUserName() {
        return returnVistUserName;
    }

    public void setReturnVistUserName(String returnVistUserName) {
        this.returnVistUserName = returnVistUserName;
    }

    /** default constructor */
	public ReturnVist() {
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
	 * @return the customerInfo
	 */
	public CustomerInfo getCustomerInfo() {
		return customerInfo;
	}

	/**
	 * @param customerInfo the customerInfo to set
	 */
	public void setCustomerInfo(CustomerInfo customerInfo) {
		this.customerInfo = customerInfo;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the returnVistTime
	 */
	public Date getReturnVistTime() {
		return returnVistTime;
	}

	/**
	 * @param returnVistTime the returnVistTime to set
	 */
	public void setReturnVistTime(Date returnVistTime) {
		this.returnVistTime = returnVistTime;
	}

	/**
	 * @return the returnVistUser
	 */
	public String getReturnVistUser() {
		return returnVistUser;
	}

	/**
	 * @param returnVistUser the returnVistUser to set
	 */
	public void setReturnVistUser(String returnVistUser) {
		this.returnVistUser = returnVistUser;
	}

    public String getReturnVistTimeStr() {
        return returnVistTimeStr;
    }

    public void setReturnVistTimeStr(String returnVistTimeStr) {
        this.returnVistTimeStr = returnVistTimeStr;
    }
	

}