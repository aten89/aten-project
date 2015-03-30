/**
 * 
 */
package org.eapp.dto;

import java.util.Collection;

import org.eapp.hbean.Group;


/**
 * 生成子系统列表
 * @author zsy
 * @version 
 */
public class GroupSelect extends HTMLSelect {
	
	private Collection<Group> gropus;
	
	public GroupSelect(Collection<Group> gropus) {
		this.gropus = gropus;
	}
	
	/**
	 * 格式：
	 * <div>8a283d001a52213f011a5255d2f60002**dsfdf</div>
	 * <div>zxt00001**datacenter</div>
	 * <div>004**ddd</div>
	 */
	public String toString() {
		StringBuffer out = new StringBuffer();
		if (gropus == null || gropus.size() < 1) {
			return out.toString();
		}
		for (Group g : gropus) {
			if( g.getType().equals("D")){
				out.append(createOption(g.getGroupID(), g.getGroupName()));
			}else{
				continue;
			}
		}
		for (Group g : gropus) {
			if( g.getType().equals("J")){
				out.append(createOption(g.getGroupID(), g.getGroupName()));
			}else{
				continue;
			}
		}
		return out.toString();
	}
}
