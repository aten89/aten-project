package org.eapp.crm.hbean;

import java.util.Date;

import org.eapp.client.util.UsernameCache;

/**
 * CustomerConsult entity. @author MyEclipse Persistence Tools
 */

public class CustomerConsult implements java.io.Serializable {

	// Fields

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4101282817982442167L;
	private String id;
	private CustomerInfo customerInfo;
	private String content;
	private Date consultTime;
	private String createor;
	private String inpnutType;
	/**
	 * 登记时间设置格式显示
	 */
	private transient String consultTimeStr;

	// Constructors

	/** default constructor */
	public CustomerConsult() {
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
	 * @return the consultTime
	 */
	public Date getConsultTime() {
		return consultTime;
	}

	/**
	 * @param consultTime the consultTime to set
	 */
	public void setConsultTime(Date consultTime) {
		this.consultTime = consultTime;
	}

	/**
	 * @return the createor
	 */
	public String getCreateor() {
		return createor;
	}

	/**
	 * @param createor the createor to set
	 */
	public void setCreateor(String createor) {
		this.createor = createor;
	}

	/**
	 * @return the inpnutType
	 */
	public String getInpnutType() {
		return inpnutType;
	}

	/**
	 * @param inpnutType the inpnutType to set
	 */
	public void setInpnutType(String inpnutType) {
		this.inpnutType = inpnutType;
	}

	/**
     * @return the consultTimeStr
     */
    public String getConsultTimeStr() {
        return consultTimeStr;
    }

    /**
     * @param consultTimeStr the consultTimeStr to set
     */
    public void setConsultTimeStr(String consultTimeStr) {
        this.consultTimeStr = consultTimeStr;
    }

    public String getCreateorName() {
        return UsernameCache.getDisplayName(createor);
    }

	
    
}