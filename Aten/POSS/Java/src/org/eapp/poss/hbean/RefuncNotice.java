package org.eapp.poss.hbean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * RefuncNotice entity. @author MyEclipse Persistence Tools
 */

public class RefuncNotice implements java.io.Serializable {

	// Fields

	/**
     * 
     */
    private static final long serialVersionUID = 4237727854252992210L;
    private String id;
	private String trustCompany;
	private String refundNotice;
	private String linkman;
	private Date createTime;
	private Set<Attachment> reFuncNoticAttachments = new HashSet<Attachment>(0);
	
	private String createTimeStr;
	private Integer attachmentCount = 0;
	private transient String linkmanName;

	// Constructors

	/** default constructor */
	public RefuncNotice() {
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTrustCompany() {
		return this.trustCompany;
	}

	public void setTrustCompany(String trustCompany) {
		this.trustCompany = trustCompany;
	}

	public String getRefundNotice() {
		return this.refundNotice;
	}

	public void setRefundNotice(String refundNotice) {
		this.refundNotice = refundNotice;
	}

	public String getLinkman() {
		return this.linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

    /**
     * get the reFuncNoticAttachments
     * @return the reFuncNoticAttachments
     */
    public Set<Attachment> getReFuncNoticAttachments() {
        return reFuncNoticAttachments;
    }

    /**
     * set the reFuncNoticAttachments to set
     * @param reFuncNoticAttachments the reFuncNoticAttachments to set
     */
    public void setReFuncNoticAttachments(Set<Attachment> reFuncNoticAttachments) {
        this.reFuncNoticAttachments = reFuncNoticAttachments;
    }

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public Integer getAttachmentCount() {
		return attachmentCount;
	}

	public void setAttachmentCount(Integer attachmentCount) {
		this.attachmentCount = attachmentCount;
	}

	public String getLinkmanName() {
		return linkmanName;
	}

	public void setLinkmanName(String linkmanName) {
		this.linkmanName = linkmanName;
	}
}