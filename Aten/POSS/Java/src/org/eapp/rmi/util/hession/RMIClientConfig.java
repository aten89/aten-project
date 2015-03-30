/**
 * 
 */
package org.eapp.rmi.util.hession;

/**
 * @author zsy
 *
 */
public class RMIClientConfig {
	private String serviceBasePath;
	private String userName;
	private String password;
	public String getServiceBasePath() {
		return serviceBasePath;
	}
	public void setServiceBasePath(String serviceBasePath) {
		this.serviceBasePath = serviceBasePath;
	}
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
	
}
