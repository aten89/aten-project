/**
 * 
 */
package org.eapp.rpc.dto;

import java.io.Serializable;
/**
 * @author zsy
 * @version 1.0
 */
public class PostInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8232841671757303354L;
	
	//职位ID
	private String postID;
	//职位名称
	private String postName;
	//排序
	private Integer displayOrder;
	//树层级
	private Integer treeLevel;
	//描述
	private String description;
	//关联的部门群组ID
	private String bandingGroupID;
	//上级职位ID
	private String parentPostID;
	
	
	public String getPostID() {
		return postID;
	}
	public void setPostID(String postID) {
		this.postID = postID;
	}
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public Integer getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	public Integer getTreeLevel() {
		return treeLevel;
	}
	public void setTreeLevel(Integer treeLevel) {
		this.treeLevel = treeLevel;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBandingGroupID() {
		return bandingGroupID;
	}
	public void setBandingGroupID(String bandingGroupID) {
		this.bandingGroupID = bandingGroupID;
	}
	public String getParentPostID() {
		return parentPostID;
	}
	public void setParentPostID(String parentPostID) {
		this.parentPostID = parentPostID;
	}
	
	public int hashCode(){
		if( this.postID != null){
			return this.postID.hashCode();
		}else{
			return PostInfo.class.hashCode();
		}
	}

	public boolean equals(Object other){
		if(other == null){
			return false;
		}
		
		if(other == this){
			return true;
		}
		
		if(other instanceof PostInfo){
			
			PostInfo otherPost = (PostInfo)other;
			if(this.postID == null){
				return false;
			}else {
				return this.postID.equals(otherPost.getPostID());
			}
			
		}else{
			return false;
		}
	}
	
}
