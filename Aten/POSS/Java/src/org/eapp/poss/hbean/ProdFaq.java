package org.eapp.poss.hbean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

/**
 * ProdFaq entity. @author MyEclipse Persistence Tools
 */

public class ProdFaq implements java.io.Serializable {

	// Fields

	/**
     * 
     */
    private static final long serialVersionUID = 8102484130575862784L;
    private String id;
	private ProdInfo prodInfo;
	private ProdFaq parentProdFaq;
	private String title;
	private String content;
	private Date createTime;
	private String creator;
	private Set<ProdFaq> childProdFaqs = new HashSet<ProdFaq>(0);
	
	private transient String prodInfoName;
	
	private transient String creatorName;
	
	private transient boolean hasAnswer;

	// Constructors

	/** default constructor */
	public ProdFaq() {
	}

    /**
     * get the id
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * set the id to set
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
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

    /**
     * get the parentProdFaq
     * @return the parentProdFaq
     */
    @JSON(serialize = false)
    public ProdFaq getParentProdFaq() {
        return parentProdFaq;
    }

    /**
     * set the parentProdFaq to set
     * @param parentProdFaq the parentProdFaq to set
     */
    public void setParentProdFaq(ProdFaq parentProdFaq) {
        this.parentProdFaq = parentProdFaq;
    }

    /**
     * get the title
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * set the title to set
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * get the content
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * set the content to set
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * get the createTime
     * @return the createTime
     */
    @JSON(format = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * set the createTime to set
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * get the creator
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * set the creator to set
     * @param creator the creator to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * get the childProdFaqs
     * @return the childProdFaqs
     */
    @JSON(serialize = false)
    public Set<ProdFaq> getChildProdFaqs() {
        return childProdFaqs;
    }

    /**
     * set the childProdFaqs to set
     * @param childProdFaqs the childProdFaqs to set
     */
    public void setChildProdFaqs(Set<ProdFaq> childProdFaqs) {
        this.childProdFaqs = childProdFaqs;
    }

    /**
     * get the prodInfoName
     * @return the prodInfoName
     */
    public String getProdInfoName() {
        return prodInfoName;
    }

    /**
     * set the prodInfoName to set
     * @param prodInfoName the prodInfoName to set
     */
    public void setProdInfoName(String prodInfoName) {
        this.prodInfoName = prodInfoName;
    }

    /**
     * get the creatorName
     * @return the creatorName
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * set the creatorName to set
     * @param creatorName the creatorName to set
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    /**
     * get the hasAnswer
     * @return the hasAnswer
     */
    public boolean isHasAnswer() {
        return hasAnswer;
    }

    /**
     * set the hasAnswer to set
     * @param hasAnswer the hasAnswer to set
     */
    public void setHasAnswer(boolean hasAnswer) {
        this.hasAnswer = hasAnswer;
    }

	// Property accessors

}