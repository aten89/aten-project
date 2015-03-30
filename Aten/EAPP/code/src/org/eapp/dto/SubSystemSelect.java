/**
 * 
 */
package org.eapp.dto;

import java.util.Collection;

import org.eapp.hbean.SubSystem;


/**
 * 生成子系统列表
 * @author zsy
 * @version 
 */
public class SubSystemSelect extends HTMLSelect {
	
	private Collection<SubSystem> subSystems;
	
	public SubSystemSelect(Collection<SubSystem> subSystems) {
		this.subSystems = subSystems;
	}

	/**
	 * 
	 * @return
	 */
	public Collection<SubSystem> getSubSystems() {
		return subSystems;
	}

	/**
	 * @param subSystems the subSystems to set
	 */
	public void setSubSystems(Collection<SubSystem> subSystems) {
		this.subSystems = subSystems;
	}
	
	/**
	 * 格式：
	 * <div>8a283d001a52213f011a5255d2f60002**dsfdf</div>
	 * <div>zxt00001**datacenter</div>
	 * <div>004**matural</div>
	 */
	public String toString() {
		StringBuffer out = new StringBuffer();
		if (subSystems == null || subSystems.size() < 1) {
			return out.toString();
		}
		for (SubSystem g : subSystems) {
			out.append(createOption(g.getSubSystemID(), g.getName()));
		}
		return out.toString();
	}
}
