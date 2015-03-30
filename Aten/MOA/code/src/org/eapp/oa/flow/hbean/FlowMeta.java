package org.eapp.oa.flow.hbean;

/**
 * FlowMeta entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class FlowMeta implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1408330630979928273L;
	private String metaId;			//元数据标识
	private String flowCategory;	//流程类别
	private String metaName;		//元数据名称
	private String displayName;	//显示名称
	private String type;			//类型
	private Boolean isNull;		//能否为空
	
	/**
	 * 可为空
	 */
	public static Boolean NULLABLE = true; 
	
	/**
	 * 非空
	 */
	public static Boolean NOT_NULL = false;

	// Constructors

	/** default constructor */
	public FlowMeta() {
	}

	/** minimal constructor */
	public FlowMeta(String metaId) {
		this.metaId = metaId;
	}

	/** full constructor */
	public FlowMeta(String metaId, String flowCategory, String metaName,
			String displayName, String type, Boolean isNull) {
		this.metaId = metaId;
		this.flowCategory = flowCategory;
		this.metaName = metaName;
		this.displayName = displayName;
		this.type = type;
		this.isNull = isNull;
	}

	// Property accessors

	public String getMetaId() {
		return this.metaId;
	}

	public void setMetaId(String metaId) {
		this.metaId = metaId;
	}

	public String getFlowCategory() {
		return this.flowCategory;
	}

	public void setFlowCategory(String flowCategory) {
		this.flowCategory = flowCategory;
	}

	public String getMetaName() {
		return this.metaName;
	}

	public void setMetaName(String metaName) {
		this.metaName = metaName;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getIsNull() {
		return this.isNull;
	}

	public void setIsNull(Boolean isNull) {
		this.isNull = isNull;
	}

}