package org.eapp.poss.rmi.hessian;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 成交记录
 * @author zhj
 *
 */
public class TransactionRecord implements Serializable {
	
	private static final long serialVersionUID = -6516581134339410135L;
	private String prodName;
	private Double pamentMoneySum;
	private Date transactionDate;
	private Date prodSetUpDate;
	private Date actualCashDate;
	private Integer sellTimeLimit;
	
//	private String transactionDateStr;
//	private String prodSetUpDateStr;
//	private String actualCashDateStr;
	
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public Double getPamentMoneySum() {
		return pamentMoneySum;
	}
	public void setPamentMoneySum(Double pamentMoneySum) {
		this.pamentMoneySum = pamentMoneySum;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public Date getProdSetUpDate() {
		return prodSetUpDate;
	}
	public void setProdSetUpDate(Date prodSetUpDate) {
		this.prodSetUpDate = prodSetUpDate;
	}
	public Date getActualCashDate() {
		return actualCashDate;
	}
	public void setActualCashDate(Date actualCashDate) {
		this.actualCashDate = actualCashDate;
	}
	public Integer getSellTimeLimit() {
		return sellTimeLimit;
	}
	public void setSellTimeLimit(Integer sellTimeLimit) {
		this.sellTimeLimit = sellTimeLimit;
	}
	public String getTransactionDateStr() {
		if (this.transactionDate == null) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(transactionDate);
	}
	public String getProdSetUpDateStr() {
		if (this.prodSetUpDate == null) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(prodSetUpDate);
	}
	public String getActualCashDateStr() {
		if (this.actualCashDate == null) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(actualCashDate);
	}
}
