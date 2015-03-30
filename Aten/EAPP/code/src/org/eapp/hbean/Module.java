package org.eapp.hbean;

import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

/**
 * Module entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Module implements java.io.Serializable, Comparable<Module> {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1906614362721713891L;

	// MODULE_ID,VARCHAR2(36),不能为NULL --模块ID
	private String moduleID;
	// PARENTMODULE_ID,VARCHAR2(36) --父模块ID
	private Module parentModule;
	// SUBSYSTEM_ID,VARCHAR2(36),不能为NULL --所属子系统ID
	private SubSystem subSystem;
	// MODULEKEY_,VARCHAR2(36) --模块KEY
	private String moduleKey;
	// NAME_ ,VARCHAR2(100),不能为NULL --模块名称
	private String name;
	// DISPLAYORDER_,INTEGER --显示顺序
	private Integer displayOrder;
	// URL_,VARCHAR2(1024) --模块链接
	private String url;
	// TREELEVEL_,INTEGER,不能为NULL --树层次
	private Integer treeLevel;
	// DESCRIPTION_,VARCHAR2(1024) --备注
	private String description;
	private Set<Module> childModules;
	private Set<ModuleAction> moduleActions;
	
	private Module quoteModule;//引用模块
	private Set<Module> quotedModules;
	
	private transient String quoteModulePath;
	// Constructors

	/** default constructor */
	public Module() {
	}

	/** minimal constructor */
	public Module(String moduleID, SubSystem subSystem, String name, Integer treeLevel) {
		this.moduleID = moduleID;
		this.subSystem = subSystem;
		this.name = name;
		this.treeLevel = treeLevel;
	}

	/** full constructor */
	public Module(String moduleID, Module parentModule, SubSystem subSystem, String moduleKey, String name,
			Integer displayOrder, String url, Integer treeLevel, String description, Set<Module> childModules,
			Set<ModuleAction> moduleActions) {
		this.moduleID = moduleID;
		this.parentModule = parentModule;
		this.subSystem = subSystem;
		this.moduleKey = moduleKey;
		this.name = name;
		this.displayOrder = displayOrder;
		this.url = url;
		this.treeLevel = treeLevel;
		this.description = description;
		this.childModules = childModules;
		this.moduleActions = moduleActions;
	}

	// Property accessors

	public String getModuleID() {
		return this.moduleID;
	}

	public void setModuleID(String moduleID) {
		this.moduleID = moduleID;
	}

	public Module getParentModule() {
		return this.parentModule;
	}

	public void setParentModule(Module fatherModule) {
		this.parentModule = fatherModule;
	}

	public SubSystem getSubSystem() {
		return this.subSystem;
	}

	public void setSubSystem(SubSystem subSystem) {
		this.subSystem = subSystem;
	}

	public String getModuleKey() {
		return this.moduleKey;
	}

	public void setModuleKey(String moduleKey) {
		this.moduleKey = moduleKey;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getTreeLevel() {
		return this.treeLevel;
	}

	public void setTreeLevel(Integer treeLevel) {
		this.treeLevel = treeLevel;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JSON(serialize=false)
	public Set<Module> getChildModules() {
		return this.childModules;
	}

	public void setChildModules(Set<Module> modules) {
		this.childModules = modules;
	}

	@JSON(serialize=false)
	public Set<ModuleAction> getModuleActions() {
		return this.moduleActions;
	}

	public void setModuleActions(Set<ModuleAction> moduleActions) {
		this.moduleActions = moduleActions;
	}

	@JSON(serialize=false)
	public Module getQuoteModule() {
		return quoteModule;
	}

	public void setQuoteModule(Module quoteModule) {
		this.quoteModule = quoteModule;
	}

	@JSON(serialize=false)
	public Set<Module> getQuotedModules() {
		return quotedModules;
	}

	public void setQuotedModules(Set<Module> quotedModules) {
		this.quotedModules = quotedModules;
	}

	public String getQuoteModulePath() {
		return quoteModulePath;
	}

	public void setQuoteModulePath(String quoteModulePath) {
		this.quoteModulePath = quoteModulePath;
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
		result = prime * result + ((moduleID == null) ? 0 : moduleID.hashCode());
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
		final Module other = (Module) obj;
		if (moduleID == null) {
			if (other.moduleID != null)
				return false;
		} else if (!moduleID.equals(other.moduleID))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Module module) {
		if (getDisplayOrder() > module.getDisplayOrder()) {
			return 1;
		} else if (getDisplayOrder() == module.getDisplayOrder()) {
			return 0;
		} else {
			return -1;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(this.getClass().getName());
		sb.append("[");
		sb.append("moduleID=");
		sb.append(getModuleID());
		sb.append(",");
		sb.append("moduleKey=");
		sb.append(getModuleKey());
		sb.append(",");
		sb.append("name=");
		sb.append(getName());
		sb.append(",");
		sb.append("displayOrder=");
		sb.append(getDisplayOrder());
		sb.append(",");
		sb.append("url=");
		sb.append(getUrl());
		sb.append(",");
		sb.append("treeLevel=");
		sb.append(getTreeLevel());
		sb.append(",");
		sb.append("description=");
		sb.append(getDescription());
		sb.append("]");
		return sb.toString();
	}
}