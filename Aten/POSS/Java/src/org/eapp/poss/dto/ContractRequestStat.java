package org.eapp.poss.dto;

import java.text.DecimalFormat;

public class ContractRequestStat {
	private DecimalFormat df = new DecimalFormat("0.0%");
	
	private String prodId;
	private String prodName;
	private String orgName;//所属机构
	private int regNums;//登记次数
	private int extendNums;//配送合同数
	private int signNums;//已签合同数
	private int remainNums;//剩余空白合同数
	
	public ContractRequestStat() {
		
	}
	public ContractRequestStat(String prodId, String prodName, String orgName, Number regNums) {
		this.prodId = prodId;
		this.prodName = prodName;
		this.orgName = orgName;
		this.regNums = regNums.intValue();
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
	public int getExtendNums() {
		return extendNums;
	}
	public void setExtendNums(int extendNums) {
		this.extendNums = extendNums;
	}
	public int getSignNums() {
		return signNums;
	}
	public void setSignNums(int signNums) {
		this.signNums = signNums;
	}
	
	public int getRemainNums() {
		return remainNums;
	}
	public void setRemainNums(int remainNums) {
		this.remainNums = remainNums;
	}
	public String getSignRateStr() {
		if (this.extendNums == 0) {
			return "";
		}
		float rate = (float)this.signNums / this.extendNums;
		return df.format(rate);
	}
}
