package org.eapp.hbean;

import java.util.Set;
import java.util.TreeSet;

import org.apache.struts2.json.annotations.JSON;

/**
 * DataDictionary entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DataDictionary implements java.io.Serializable,Comparable<DataDictionary>{

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -2173446270745455097L;
	//DATADICT_ID,VARCHAR2(36),不为空   --条目ID
	private String dataDictID;
	//PARENTDATADICT_ID,VARCHAR2(36)   --父条目ID
	private DataDictionary parentDataDictionary;
	//SUBSYSTEM_ID,VARCHAR2(36),不为空   --子系统ID
	private SubSystem subSystem;
	//DICTNAME_,VARCHAR2(100),不为空      --字典名称
	private String dictName;
	//DICTCODE_,VARCHAR2(100),不为空    --字典代码
	private String dictCode;
	//CEILVALUE_,VARCHAR2(100)          --最大值
	private String ceilValue;
	//FLOORVALUE_,VARCHAR2(100)         --最小值
	private String floorValue;          
	//DISPLAYSORT_,INTEGER,不为空	        --显示序列
	private Integer displaySort;
	//DICTTYPE_,VARCHAR2(20),不为空       --条目类型
	private String dictType;
	//TREELEVEL_,INTEGER,不为空	        --树层次
	private Integer treeLevel;
	//DESCRIPTION_,VARCHAR2(1024)       --备注
	private String description;
	private Set<DataDictionary> childDataDictionaries = new TreeSet<DataDictionary>();

	// Constructors

	/** default constructor */
	public DataDictionary() {
	}

	// Property accessors
	
	public String getDataDictID() {
		return this.dataDictID;
	}

	public void setDataDictID(String dataDictID) {
		this.dataDictID = dataDictID;
	}

	public DataDictionary getParentDataDictionary() {
		return this.parentDataDictionary;
	}

	public void setParentDataDictionary(DataDictionary parentDataDictionary) {
		this.parentDataDictionary = parentDataDictionary;
	}

	public SubSystem getSubSystem() {
		return this.subSystem;
	}

	public void setSubSystem(SubSystem subSystem) {
		this.subSystem = subSystem;
	}

	public String getDictName() {
		return this.dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getDictCode() {
		return this.dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getCeilValue() {
		return this.ceilValue;
	}

	public void setCeilValue(String ceilValue) {
		this.ceilValue = ceilValue;
	}

	public String getFloorValue() {
		return this.floorValue;
	}

	public void setFloorValue(String floorValue) {
		this.floorValue = floorValue;
	}

	public Integer getDisplaySort() {
		return this.displaySort;
	}

	public void setDisplaySort(Integer displaySort) {
		this.displaySort = displaySort;
	}

	public String getDictType() {
		return this.dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
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
	public Set<DataDictionary> getChildDataDictionaries() {
		return this.childDataDictionaries;
	}

	public void setChildDataDictionaries(Set<DataDictionary> childDataDictionaries) {
		this.childDataDictionaries = childDataDictionaries;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataDictID == null) ? 0 : dataDictID.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		final DataDictionary other = (DataDictionary) obj;
		if (dataDictID == null) {
			if (other.dataDictID != null)
				return false;
		} else if (!dataDictID.equals(other.dataDictID))
			return false;
		return true;
	}
	
	public int compareTo(DataDictionary dataDict) {
		if(displaySort < dataDict.displaySort){
			return -1;
		}else if(displaySort == dataDict.displaySort && dataDictID.equals(dataDict.dataDictID)){
			return 0;
		}else{
			return 1;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(this.getClass().getName());
		sb.append("[");
		sb.append("dataDictID=");
		sb.append(getDataDictID());
		sb.append(",");
		sb.append("dictName=");
		sb.append(getDictName());
		sb.append(",");
		sb.append("dictCode=");
		sb.append(getDictCode());
		sb.append(",");
		sb.append("ceilValue=");
		sb.append(getCeilValue());
		sb.append(",");
		sb.append("floorValue=");
		sb.append(getFloorValue());
		sb.append(",");
		sb.append("displaySort=");
		sb.append(getDisplaySort());
		sb.append(",");
		sb.append("dictType=");
		sb.append(getDictType());
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