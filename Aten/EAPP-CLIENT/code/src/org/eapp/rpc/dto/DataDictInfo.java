/**
 * 
 */
package org.eapp.rpc.dto;

import java.util.List;

/**
 * @version 
 */
public class DataDictInfo implements java.io.Serializable,Comparable<DataDictInfo>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3583710458807733943L;
	//条目ID
	private String dataDictID;
	//父条目ID
	private String parentDataDictID;
	//子系统ID
	private String subSystemID;
	//条目KEY
	private String dictName;
	//条目值
	private String dictCode;
	//最大值
	private String ceilValue;
	//最小值
	private String floorValue;          
	//显示序列
	private Integer displaySort;
	//条目类型
	private String dictType;
	//树层次
	private Integer treeLevel;
	//备注
	private String description;
	private List<DataDictInfo> childDataDicts;
	/**
	 * @return the dataDictID
	 */
	public String getDataDictID() {
		return dataDictID;
	}
	/**
	 * @param dataDictID the dataDictID to set
	 */
	public void setDataDictID(String dataDictID) {
		this.dataDictID = dataDictID;
	}
	/**
	 * @return the parentDataDictID
	 */
	public String getParentDataDictID() {
		return parentDataDictID;
	}
	/**
	 * @param parentDataDictID the parentDataDictID to set
	 */
	public void setParentDataDictID(String parentDataDictID) {
		this.parentDataDictID = parentDataDictID;
	}
	/**
	 * @return the subSystemID
	 */
	public String getSubSystemID() {
		return subSystemID;
	}
	/**
	 * @param subSystemID the subSystemID to set
	 */
	public void setSubSystemID(String subSystemID) {
		this.subSystemID = subSystemID;
	}
	/**
	 * @return the dictName
	 */
	public String getDictName() {
		return dictName;
	}
	/**
	 * @param dictName the dictName to set
	 */
	public void setDictName(String dictName) {
		this.dictName = dictName;
	}
	/**
	 * @return the dictCode
	 */
	public String getDictCode() {
		return dictCode;
	}
	/**
	 * @param dictCode the dictCode to set
	 */
	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}
	/**
	 * @return the ceilValue
	 */
	public String getCeilValue() {
		return ceilValue;
	}
	/**
	 * @param ceilValue the ceilValue to set
	 */
	public void setCeilValue(String ceilValue) {
		this.ceilValue = ceilValue;
	}
	/**
	 * @return the floorValue
	 */
	public String getFloorValue() {
		return floorValue;
	}
	/**
	 * @param floorValue the floorValue to set
	 */
	public void setFloorValue(String floorValue) {
		this.floorValue = floorValue;
	}
	/**
	 * @return the displaySort
	 */
	public Integer getDisplaySort() {
		return displaySort;
	}
	/**
	 * @param displaySort the displaySort to set
	 */
	public void setDisplaySort(Integer displaySort) {
		this.displaySort = displaySort;
	}
	/**
	 * @return the dictType
	 */
	public String getDictType() {
		return dictType;
	}
	/**
	 * @param dictType the dictType to set
	 */
	public void setDictType(String dictType) {
		this.dictType = dictType;
	}
	/**
	 * @return the treeLevel
	 */
	public Integer getTreeLevel() {
		return treeLevel;
	}
	/**
	 * @param treeLevel the treeLevel to set
	 */
	public void setTreeLevel(Integer treeLevel) {
		this.treeLevel = treeLevel;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the childDataDictionaries
	 */
	public List<DataDictInfo> getChildDataDicts() {
		return childDataDicts;
	}
	/**
	 * @param childDataDictionaries the childDataDictionaries to set
	 */
	public void setChildDataDicts(List<DataDictInfo> childDataDicts) {
		this.childDataDicts = childDataDicts;
	}
	public int compareTo(DataDictInfo o) {
		if(treeLevel > o.treeLevel){
			return 1;
		}else if(treeLevel < o.treeLevel){
			return -1;
		}else{
			if(displaySort > o.displaySort){
				return 1;
			}else if(displaySort < o.displaySort){
				return -1;
			}else{
				if (dataDictID != null) {
					return dataDictID.compareTo(o.dataDictID);
				}
				return 0;
			}
		}
	}
	
}
