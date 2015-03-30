/**
 * 
 */
package org.eapp.poss.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.eapp.poss.hbean.CustPayment;


/**
 * @author zsy
 * @version Nov 26, 2008
 */
public class CustPaymentSelect extends HTMLSelect {

	private Collection<CustPayment> custPayments;
	
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	public CustPaymentSelect(Collection<CustPayment> custPayments) {
		this.custPayments = custPayments;
	}
	
	/**
	 * 有默认值的情况
	 * 格式：
	 * <div isselected="true">8a283d001a52213f011a5255d2f60002**dsfdf</div>
	 * <div>zxt00001**datacenter</div>
	 */
	public String toString() {
		StringBuffer out = new StringBuffer();
		if (custPayments == null || custPayments.size() < 1) {
			return out.toString();
		}
		for (CustPayment cp : custPayments) {
			out.append(createOption(cp.getId(), "[" + df.format(cp.getTransferDate()) + "]" + cp.getTransferAmount()));
		}
		return out.toString();
	}
		
}
