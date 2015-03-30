package org.eapp.poss.hbean;

import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

/**
 * Supplier entity. @author MyEclipse Persistence Tools
 */

public class BlankContract implements java.io.Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 2180773728253568486L;
    private String id;
	private ProdInfo prodInfo;
	private Integer regNums;
	private Integer contractNums;
	private Integer remainNums;
	private Integer latestDas;
	private Boolean returnFlag;
	private Set<ContractRegDetail> contractRegDetails;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@JSON(serialize = false)
	public ProdInfo getProdInfo() {
		return prodInfo;
	}
	public void setProdInfo(ProdInfo prodInfo) {
		this.prodInfo = prodInfo;
	}
	public Integer getRegNums() {
		return regNums;
	}
	public void setRegNums(Integer regNums) {
		this.regNums = regNums;
	}
	public Integer getContractNums() {
		return contractNums;
	}
	public void setContractNums(Integer contractNums) {
		this.contractNums = contractNums;
	}
	public Integer getRemainNums() {
		return remainNums;
	}
	public void setRemainNums(Integer remainNums) {
		this.remainNums = remainNums;
	}
	public Integer getLatestDas() {
		return latestDas;
	}
	public void setLatestDas(Integer latestDas) {
		this.latestDas = latestDas;
	}
	public Boolean getReturnFlag() {
		return returnFlag;
	}
	public void setReturnFlag(Boolean returnFlag) {
		this.returnFlag = returnFlag;
	}
	@JSON(serialize = false)
	public Set<ContractRegDetail> getContractRegDetails() {
		return contractRegDetails;
	}
	public void setContractRegDetails(Set<ContractRegDetail> contractRegDetails) {
		this.contractRegDetails = contractRegDetails;
	}

	public String getProdId() {
		return prodInfo.getId();
	}
	public String getProdName() {
		return prodInfo.getProdName();
	}
}