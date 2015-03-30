package org.eapp.crm.rmi.hessian;

import java.io.Serializable;


public class CustInfo implements Serializable {
	
	private static final long serialVersionUID = -8003127622550259256L;
	
	/**
     * 不通过客户状态
     */
    public static final String UNPASS_STATUS = "-1";

    /**
     * 未提交客户状态
     */
    public static final String UNCOMMIT_STATUS = "0";
    /**
     * 回访中客户状态
     */
    public static final String RETURNVISIT_STATUS = "1";
    /**
     * 驳回客户状态
     */
    public static final String REJECT_STATUS = "2";
    /**
     * 潜在客户状态
     */
    public static final String POTENTIAL_STATUS = "3";
    /**
     *待完善客户状态
     */
    public static final String TOPERFECT_STATUS = "4";
    /**
     * 正式客户状态
     */
    public static final String OFFICIAL_STATUS = "5";
    
	private String id;
	private String custName;
	private String tel;
	private String recommendProduct;
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
	 * @return the recommendProduct
	 */
	public String getRecommendProduct() {
		return recommendProduct;
	}
	/**
	 * @param recommendProduct the recommendProduct to set
	 */
	public void setRecommendProduct(String recommendProduct) {
		this.recommendProduct = recommendProduct;
	}
	
}
