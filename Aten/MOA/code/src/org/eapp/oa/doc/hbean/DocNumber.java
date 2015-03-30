package org.eapp.oa.doc.hbean;

import org.eapp.oa.system.hbean.Attachment;
import org.eapp.util.web.upload.FileDispatcher;

/**
 * DocNumber entity. 
 * Description:发文编号
 * @author songdingsong
 */

public class DocNumber implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4323653244330155881L;
	// ID_,VARCHAR2(36),不能为空     --主键ID
	private String id;
	// DocWord_,VARCHAR2(128)      --文件字
	private String docWord;
	// YearPrefix_,VARCHAR2(32)    --年份前缀
	private String yearPrefix;
	// CurrentYear_,INTEGER        --当年年份
	private Integer currentYear;
	// YearPostfix_,VARCHAR2(32)   --年份后缀
	private String yearPostfix;
	// OrderPrefix_,VARCHAR2(32)   --流水号前缀
	private String orderPrefix;
	// OrderNumber_,INTEGER        --流水号
	private Integer orderNumber;
	// OrderPostfix_,VARCHAR2(32)  --流水号后缀
	private String orderPostfix;
	// HeadTemplate_,VARCHAR2(32)  --红头模板
	private Attachment headTemplate;
	// Description_,VARCHAR2(1024) --描述
	private String description;

	// Constructors

	/** default constructor */
	public DocNumber() {
	}

	/** full constructor */
	public DocNumber(String docWord, String yearPrefix, Integer currentYear,
			String yearPostfix, String orderPrefix, Integer orderNumber,
			String orderPostfix, Attachment headTemplate, String description) {
		this.docWord = docWord;
		this.yearPrefix = yearPrefix;
		this.currentYear = currentYear;
		this.yearPostfix = yearPostfix;
		this.orderPrefix = orderPrefix;
		this.orderNumber = orderNumber;
		this.orderPostfix = orderPostfix;
		this.headTemplate = headTemplate;
		this.description = description;
	}

	// Property accessors

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDocWord() {
		return docWord;
	}

	public void setDocWord(String docWord) {
		this.docWord = docWord;
	}

	public String getYearPrefix() {
		return yearPrefix;
	}

	public void setYearPrefix(String yearPrefix) {
		this.yearPrefix = yearPrefix;
	}

	public Integer getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(Integer currentYear) {
		this.currentYear = currentYear;
	}

	public String getYearPostfix() {
		return yearPostfix;
	}

	public void setYearPostfix(String yearPostfix) {
		this.yearPostfix = yearPostfix;
	}

	public String getOrderPrefix() {
		return orderPrefix;
	}

	public void setOrderPrefix(String orderPrefix) {
		this.orderPrefix = orderPrefix;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderPostfix() {
		return orderPostfix;
	}

	public void setOrderPostfix(String orderPostfix) {
		this.orderPostfix = orderPostfix;
	}

	public Attachment getHeadTemplate() {
		return headTemplate;
	}

	public void setHeadTemplate(Attachment headTemplate) {
		this.headTemplate = headTemplate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DocNumber other = (DocNumber) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	//其他自定义方法
	public String getHeadTemplateUrl() {
		if (headTemplate == null) {
			return null;
		}
		return FileDispatcher.getAbsPath(headTemplate.getFilePath());
	}

	@Override
	public String toString() {
		StringBuffer description = new StringBuffer();
		description.append(docWord).append(yearPrefix).append(
				currentYear).append(yearPostfix).append(orderPrefix).append(
						orderNumber).append(orderPostfix);
		return description.toString();
	}
}