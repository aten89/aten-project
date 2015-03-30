/**
 * 
 */
package org.eapp.rmi.util.hession;

import java.io.IOException;
import java.security.SignatureException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.rmi.util.hession.authentication.AuthenticationHandler;
import org.springframework.remoting.caucho.HessianServiceExporter;


/**
 * 继承服务端spring HessianServiceExporter代理类添加用户名与密码验证
 * @author zsy
 *
 */
public class SecureHessianServiceExporter extends HessianServiceExporter {
    private AuthenticationHandler authentication;  

    public void setAuthentication(AuthenticationHandler authenticationHandler) {
		this.authentication = authenticationHandler;
	}

	public void handleRequest(HttpServletRequest request,  
            HttpServletResponse response) throws ServletException, IOException {  
		if (authentication == null) {
			super.handleRequest(request, response);
			return;
		}
//		String username = request.getHeader("hessian-username");
//        String password = request.getHeader("hessian-password");
//        System.out.println("user:" + username+","+password);
		
		//hessian 封装的用户名，密码加密后的字符串
        String authorization = request.getHeader("Authorization");
        try {
//        	authentication.authenticateInternal(username, password);
        	authentication.authenticateInternal(authorization);
		} catch (SignatureException e) {
			throw new ServletException(e);
		}
        super.handleRequest(request, response);  
    }  

}
