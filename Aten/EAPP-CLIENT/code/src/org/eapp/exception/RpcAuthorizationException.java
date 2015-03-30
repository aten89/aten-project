/**
 * 
 */
package org.eapp.exception;

import java.io.Serializable;


/**
 * @author 林良益 2008-06-22
 * @version 1.0
 */
public final class RpcAuthorizationException extends EappException implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3398383331664088837L;
	
	public static final int CODE_ELSE = 0;
	public static final int CODE_OVERTIME = 1;//登录超时
	public static final int CODE_REPEATLOGIN = 2;//重复登录
	public static final int CODE_NOACCOUNT = 3;//帐号不存在
	public static final int CODE_WRONGPASSWORD = 4;//密码不正确
	public static final int CODE_ACCOUNTINVALID = 5;//帐号无效
	public static final int CODE_NORIGHT = 6;//帐号无权限
	
	
	private int code;
	
	public RpcAuthorizationException() {
		super();
		this.code = CODE_ELSE;
	}

	public RpcAuthorizationException(String message) {
		super(message);
		this.code = CODE_ELSE;
	}
	
	public RpcAuthorizationException(Exception e) {
		super(e);
		this.code = CODE_ELSE;
	}
	
	public RpcAuthorizationException(int code, String message) {
		super(message);
		this.code = code;
	}
	
	public RpcAuthorizationException(int code, Exception e) {
		super(e);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
}
