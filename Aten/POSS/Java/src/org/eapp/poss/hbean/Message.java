package org.eapp.poss.hbean;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

/**
 * Message entity. @author MyEclipse Persistence Tools
 */

public class Message implements java.io.Serializable {

	// Fields

	/**
     * 
     */
    private static final long serialVersionUID = -4813696692422512141L;
    private String id;
	private ProdInfo prodInfo;
	private String prodName;
	private String title;
	private String content;
	private Date sendTime;
	private String sendNo;
	private String salesManager;
	private String receiverNo;

	// Constructors

	/** default constructor */
	public Message() {
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProdName() {
		return this.prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@JSON(format="yyyy-MM-dd hh:mm:ss")
	public Date getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendNo() {
		return this.sendNo;
	}

	public void setSendNo(String sendNo) {
		this.sendNo = sendNo;
	}

	public String getSalesManager() {
		return this.salesManager;
	}

	public void setSalesManager(String salesManager) {
		this.salesManager = salesManager;
	}

	public String getReceiverNo() {
		return this.receiverNo;
	}

	public void setReceiverNo(String receiverNo) {
		this.receiverNo = receiverNo;
	}

    /**
     * get the prodInfo
     * @return the prodInfo
     */
	@JSON(serialize = false)
    public ProdInfo getProdInfo() {
        return prodInfo;
    }

    /**
     * set the prodInfo to set
     * @param prodInfo the prodInfo to set
     */
    public void setProdInfo(ProdInfo prodInfo) {
        this.prodInfo = prodInfo;
    }

}