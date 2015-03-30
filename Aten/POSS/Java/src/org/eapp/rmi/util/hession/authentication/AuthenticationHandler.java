/**
 * 
 */
package org.eapp.rmi.util.hession.authentication;

import java.security.SignatureException;

/**
 * 远程接口，安全认证
 * @author zsy
 *
 */
public interface AuthenticationHandler {
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @throws LoginException
	 */
	public void authenticateInternal(String userName, String password) throws SignatureException;
	
	/**
	 * hessian 4.0.7 认证
	 * @param authorization 用户名,密码 经base64加密后的字符串
	 * @throws SignatureException
	 */
	public void authenticateInternal(String authorization) throws SignatureException;
}
