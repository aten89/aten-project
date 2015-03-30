package org.eapp.poss.hbean;

/**
 * ExpectYearYield entity. @author MyEclipse Persistence Tools
 */

public class ExpectYearYield implements java.io.Serializable {

	// Fields

	/**
     * 
     */
    private static final long serialVersionUID = 1504936146328966718L;
    private String id;
	private ProdInfo prodInfo;
	private Double lowerLimit;
	private Double upperLimit;
	private Double yearYield;
	private String benefitType;

	// Constructors

	/** default constructor */
	public ExpectYearYield() {
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

	public Double getLowerLimit() {
		return this.lowerLimit;
	}

	public void setLowerLimit(Double lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public Double getUpperLimit() {
		return this.upperLimit;
	}

	public void setUpperLimit(Double upperLimit) {
		this.upperLimit = upperLimit;
	}

	public Double getYearYield() {
		return this.yearYield;
	}

	public void setYearYield(Double yearYield) {
		this.yearYield = yearYield;
	}

	public String getBenefitType() {
		return this.benefitType;
	}

	public void setBenefitType(String benefitType) {
		this.benefitType = benefitType;
	}

}