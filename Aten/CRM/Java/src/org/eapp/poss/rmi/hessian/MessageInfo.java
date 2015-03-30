package org.eapp.poss.rmi.hessian;

import java.io.Serializable;
import java.util.Date;


public class MessageInfo implements Serializable {
	
	private static final long serialVersionUID = -6516581134339410315L;
	
	private String id;
	private String prodName;
	private String title;
	private String content;
	private Date sendTime;
	private String sendNo;
	private String salesManager;
	private String receiverNo;
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
	 * @return the prodName
	 */
	public String getProdName() {
		return prodName;
	}
	/**
	 * @param prodName the prodName to set
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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
	 * @return the sendTime
	 */
	public Date getSendTime() {
		return sendTime;
	}
	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	/**
	 * @return the sendNo
	 */
	public String getSendNo() {
		return sendNo;
	}
	/**
	 * @param sendNo the sendNo to set
	 */
	public void setSendNo(String sendNo) {
		this.sendNo = sendNo;
	}
	/**
	 * @return the salesManager
	 */
	public String getSalesManager() {
		return salesManager;
	}
	/**
	 * @param salesManager the salesManager to set
	 */
	public void setSalesManager(String salesManager) {
		this.salesManager = salesManager;
	}
	/**
	 * @return the receiverNo
	 */
	public String getReceiverNo() {
		return receiverNo;
	}
	/**
	 * @param receiverNo the receiverNo to set
	 */
	public void setReceiverNo(String receiverNo) {
		this.receiverNo = receiverNo;
	}
	
}
