/**
 * 
 */
package org.eapp.rmi.util.hession;

import org.springframework.remoting.caucho.HessianProxyFactoryBean;

import com.caucho.hessian.client.HessianProxyFactory;


/**
 * 继承客户端spring HessianProxyFactoryBean代理类添加密码设置
 * @author zsy
 *
 */
public class SecureHessianProxyFactoryBean extends HessianProxyFactoryBean {
    private RMIClientConfig rmiClientConfig;  
    private String servicePath;

    public void setRmiClientConfig(RMIClientConfig rmiClientConfig) {
    	if (rmiClientConfig == null) {
    		throw new IllegalArgumentException();
    	}
		this.rmiClientConfig = rmiClientConfig;
		if (servicePath != null) {
    		this.setServiceUrl(rmiClientConfig.getServiceBasePath() + servicePath);
    	}
	}

	public void setServicePath(String servicePath) {
		if (servicePath == null) {
    		throw new IllegalArgumentException();
    	}
		this.servicePath = servicePath;
		if (rmiClientConfig != null) {
    		this.setServiceUrl(rmiClientConfig.getServiceBasePath() + servicePath);
    	}
	}


	public void afterPropertiesSet() {
		HessianProxyFactory proxyFactory = new HessianProxyFactory();
//        proxyFactory.setReadTimeout(0);  
//        proxyFactory.setConnectTimeOut(connectTimeOut);  
//        proxyFactory.setAuthentication(new PasswordAuthentication(rmiClientConfig.getUserName(), rmiClientConfig.getPassword().toCharArray()));  
        //4.0.7 设置验证用户名，密码
        proxyFactory.setUser(rmiClientConfig.getUserName());
        proxyFactory.setPassword(rmiClientConfig.getPassword());
//        proxyFactory.setOverloadEnabled(true); 
        setProxyFactory(proxyFactory);  
        super.afterPropertiesSet();  
    }
}
