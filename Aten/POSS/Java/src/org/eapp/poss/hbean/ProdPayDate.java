package org.eapp.poss.hbean;

import java.util.Date;

/**
 * ProdPayDate entity. @author MyEclipse Persistence Tools
 */

public class ProdPayDate implements java.io.Serializable {

	// Fields

	/**
     * 
     */
    private static final long serialVersionUID = 4700848253331326033L;
    private String id;
	private ProdInfo prodInfo;
	private Date expectPayDate;
	private Date actualPayDate;

	// Constructors

	/** default constructor */
	public ProdPayDate() {
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProdInfo getProdInfo() {
		return this.prodInfo;
	}

	public void setProdInfo(ProdInfo prodInfo) {
		this.prodInfo = prodInfo;
	}

	public Date getExpectPayDate() {
		return this.expectPayDate;
	}

	public void setExpectPayDate(Date expectPayDate) {
		this.expectPayDate = expectPayDate;
	}

	public Date getActualPayDate() {
		return this.actualPayDate;
	}

	public void setActualPayDate(Date actualPayDate) {
		this.actualPayDate = actualPayDate;
	}

}