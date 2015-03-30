/**
 * 
 */
package org.eapp.rmi.util.hession.authentication;

import java.security.SignatureException;

import org.eapp.util.security.DigestAlgorithm;


/**
 * @author zsy
 *
 */
public class SimpleAuthenticationHandler implements AuthenticationHandler {
	private String userName;
	private String password;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public void authenticateInternal(String userName,
			String password) throws SignatureException {
		 if (password == null || userName == null) {
	        	throw new SignatureException("hessian访问用户名或密码为空");
	        }
	        if (!userName.equals(getUserName())) {
	        	throw new SignatureException("hessian访问用户名不正确");
	        }
	        if (!password.equals(DigestAlgorithm.md5Digest(new String(getPassword())))) {
	        	throw new SignatureException("hessian访问密码不正确");
	        	
	        }
		
	}
	
	@Override
	public void authenticateInternal(String authorization)
			throws SignatureException {
		if (authorization == null) {
        	throw new SignatureException("hessian访问用户名或密码为空");
        }
		String[] array = authorization.split(" "); 
		if (array.length != 2) {
			throw new SignatureException("hessian用户,密码数据错误");
		}
		//构造字符串
		String strNew = getUserName()+":"+getPassword();
        if (!base64(strNew).equals(array[1])) {
        	throw new SignatureException("hessian访问用户名或密码不正确");
        }
		
	}
	
	/**
	   * Creates the Base64 value.
	   * Base64 加密
	   */
	  private String base64(String value)
	  {
	    StringBuffer cb = new StringBuffer();

	    int i = 0;
	    for (i = 0; i + 2 < value.length(); i += 3) {
	      long chunk = (int) value.charAt(i);
	      chunk = (chunk << 8) + (int) value.charAt(i + 1);
	      chunk = (chunk << 8) + (int) value.charAt(i + 2);

	      cb.append(encode(chunk >> 18));
	      cb.append(encode(chunk >> 12));
	      cb.append(encode(chunk >> 6));
	      cb.append(encode(chunk));
	    }

	    if (i + 1 < value.length()) {
	      long chunk = (int) value.charAt(i);
	      chunk = (chunk << 8) + (int) value.charAt(i + 1);
	      chunk <<= 8;

	      cb.append(encode(chunk >> 18));
	      cb.append(encode(chunk >> 12));
	      cb.append(encode(chunk >> 6));
	      cb.append('=');
	    }
	    else if (i < value.length()) {
	      long chunk = (int) value.charAt(i);
	      chunk <<= 16;

	      cb.append(encode(chunk >> 18));
	      cb.append(encode(chunk >> 12));
	      cb.append('=');
	      cb.append('=');
	    }

	    return cb.toString();
	  }
	  
	  public static char encode(long d)
	  {
	    d &= 0x3f;
	    if (d < 26)
	      return (char) (d + 'A');
	    else if (d < 52)
	      return (char) (d + 'a' - 26);
	    else if (d < 62)
	      return (char) (d + '0' - 52);
	    else if (d == 62)
	      return '+';
	    else
	      return '/';
	  }
	  
}
