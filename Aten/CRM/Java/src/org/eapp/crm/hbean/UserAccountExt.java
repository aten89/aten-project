package org.eapp.crm.hbean;

import org.eapp.client.util.UsernameCache;

/**
 * UserAccountExt entity. @author MyEclipse Persistence Tools
 */

public class UserAccountExt implements java.io.Serializable {

	// Fields

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5217692843481393461L;
	private String userId;//主键
	private String accountId;//销售账号
	private String serviceAccountId;//客服账号
	private String remark;//备注
	/**
	 * 分配客户数量
	 */
	private transient int allotCustomerNum;

	// Constructors

	/** default constructor */
	public UserAccountExt() {
	}

	/** full constructor */
	public UserAccountExt(String accountId, String serviceAccountId,
			String remark) {
		this.accountId = accountId;
		this.serviceAccountId = serviceAccountId;
		this.remark = remark;
	}
	
	// Property accessors

    /**
     * get the userId
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * set the userId to set
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * get the accountId
     * @return the accountId
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * set the accountId to set
     * @param accountId the accountId to set
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * get the serviceAccountId
     * @return the serviceAccountId
     */
    public String getServiceAccountId() {
        return serviceAccountId;
    }

    /**
     * set the serviceAccountId to set
     * @param serviceAccountId the serviceAccountId to set
     */
    public void setServiceAccountId(String serviceAccountId) {
        this.serviceAccountId = serviceAccountId;
    }

    /**
     * get the remark
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * set the remark to set
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * get the allotCustomerNum
     * @return the allotCustomerNum
     */
    public int getAllotCustomerNum() {
        return allotCustomerNum;
    }

    /**
     * set the allotCustomerNum to set
     * @param allotCustomerNum the allotCustomerNum to set
     */
    public void setAllotCustomerNum(int allotCustomerNum) {
        this.allotCustomerNum = allotCustomerNum;
    }

    /**
     * get the displayName
     * @return the displayName
     */
    public String getDisplayName() {
        return UsernameCache.getDisplayName(accountId);
    }

}