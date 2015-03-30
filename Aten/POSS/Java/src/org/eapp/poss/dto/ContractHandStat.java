package org.eapp.poss.dto;

public class ContractHandStat {
	private String prodId;
	private String prodName;
	private String orgName;//所属机构
	private int regNums;//登记次数
	private int signNums;//签署合同数
	private int blankNums;//空白合同数
	private int invalidNums;//签废合同数
	
	public ContractHandStat() {
		
	}
	public ContractHandStat(String prodId, String prodName, String orgName, Number regNums) {
		this.prodId = prodId;
		this.prodName = prodName;
		this.orgName = orgName;
		this.regNums = regNums.intValue();
	}
	
	public ContractHandStat(Number signNums, Number blankNums, Number invalidNums) {
		if (signNums != null) {
			this.signNums = signNums.intValue();
		}
		
		if (blankNums != null) {
			this.blankNums = blankNums.intValue();
		}
		
		if (invalidNums != null) {
			this.invalidNums = invalidNums.intValue();
		}
	}
	public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public int getRegNums() {
		return regNums;
	}
	public void setRegNums(int regNums) {
		this.regNums = regNums;
	}
	public int getSignNums() {
		return signNums;
	}
	public void setSignNums(int signNums) {
		this.signNums = signNums;
	}
	public int getBlankNums() {
		return blankNums;
	}
	public void setBlankNums(int blankNums) {
		this.blankNums = blankNums;
	}
	public int getInvalidNums() {
		return invalidNums;
	}
	public void setInvalidNums(int invalidNums) {
		this.invalidNums = invalidNums;
	}

	public Integer getHandNums() {
		return signNums + blankNums + invalidNums;
	}
}
