/**
 * 
 */
package org.eapp.oa.rmi.hessian;


/**
 * 通讯录远程服务
 * @author zsy
 *
 */
public interface IAddressListPoint {

	/**
	 * 通过用户帐号取得用户的联系方式
	 * @param userAccount
	 * @return
	 */
	public Contact getUserContact(String userAccount);

}
