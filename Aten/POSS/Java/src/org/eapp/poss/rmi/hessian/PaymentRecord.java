package org.eapp.poss.rmi.hessian;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 划款记录
 *
 */
public class PaymentRecord implements Serializable {
	
	private static final long serialVersionUID = -6516581134339410135L;
	private String paymentId;
	private String prodName;
	private Double transferAmount;
	private Double totalRefundAmount;
	private Date transferDate;
	private String remark;
	
	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public Double getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(Double transferAmount) {
		this.transferAmount = transferAmount;
	}

	public Double getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(Double totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}

	public Date getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTransferDateStr() {
		if (this.transferDate == null) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(transferDate);
	}
}
