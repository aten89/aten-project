/**
 * 
 */
package org.eapp.poss.dto;

import java.util.Collection;

import org.eapp.poss.hbean.ProdInfo;


/**
 * @author zsy
 * @version Nov 26, 2008
 */
public class ProdInfoSelect extends HTMLSelect {

	private Collection<ProdInfo> prodInfos;
	
	public ProdInfoSelect(Collection<ProdInfo> prodInfos) {
		this.prodInfos = prodInfos;
	}
	
	/**
	 * 有默认值的情况
	 * 格式：
	 * <div isselected="true">8a283d001a52213f011a5255d2f60002**dsfdf</div>
	 * <div>zxt00001**datacenter</div>
	 */
	public String toString() {
		StringBuffer out = new StringBuffer();
		if (prodInfos == null || prodInfos.size() < 1) {
			return out.toString();
		}
		for (ProdInfo p : prodInfos) {
			out.append(createOption(p.getId(), p.getProdName()));
		}
		return out.toString();
	}
		
}
