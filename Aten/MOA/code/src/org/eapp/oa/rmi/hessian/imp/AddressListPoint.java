/**
 * 
 */
package org.eapp.oa.rmi.hessian.imp;

import org.eapp.oa.address.blo.IAddressListBiz;
import org.eapp.oa.address.hbean.AddressList;
import org.eapp.oa.rmi.hessian.Contact;
import org.eapp.oa.rmi.hessian.IAddressListPoint;

import com.caucho.hessian.server.HessianServlet;

/**
 * @author zsy
 *
 */
public class AddressListPoint extends HessianServlet implements IAddressListPoint {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2720632888548144385L;

	private IAddressListBiz addressListBiz;
	
	public void setAddressListBiz(IAddressListBiz addressListBiz) {
		this.addressListBiz = addressListBiz;
	}

	@Override
	public Contact getUserContact(String userAccount) {
		AddressList addr = addressListBiz.getByAccountId(userAccount);
		Contact c = new Contact();
		c.setUserAccount(userAccount);
		if (addr != null) {
			c.setEmail(addr.getUserEmail());
			c.setMobile(addr.getUserMobile());
			c.setMsn(addr.getUserMSN());
			c.setOfficeTel(addr.getUserOfficeTel());
			c.setQq(addr.getUserQQ());
		}
		return c;
	}
}
