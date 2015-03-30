/**
 * 
 */
package org.eapp.rpc.dto;

import java.io.Serializable;


/**
 * @version 
 */
public class SubSystemInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4088462312681584949L;
	private String subSystemID;
	private String name;
	private String ipAddress;
	private String serverName;
	private String domainName;
	private Integer port;
	
	private String casLoginUrl;
	private String casLogoutUrl;
	private String casValidateUrl;
	
	public String getSubSystemID() {
		return subSystemID;
	}
	public void setSubSystemID(String subSystemID) {
		this.subSystemID = subSystemID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getCasLoginUrl() {
		return casLoginUrl;
	}
	public void setCasLoginUrl(String casLoginUrl) {
		this.casLoginUrl = casLoginUrl;
	}
	public String getCasLogoutUrl() {
		return casLogoutUrl;
	}
	public void setCasLogoutUrl(String casLogoutUrl) {
		this.casLogoutUrl = casLogoutUrl;
	}
	public String getCasValidateUrl() {
		return casValidateUrl;
	}
	public void setCasValidateUrl(String casValidateUrl) {
		this.casValidateUrl = casValidateUrl;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subSystemID == null) ? 0 : subSystemID.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SubSystemInfo other = (SubSystemInfo) obj;
		if (subSystemID == null) {
			if (other.subSystemID != null)
				return false;
		} else if (!subSystemID.equals(other.subSystemID))
			return false;
		return true;
	}
	
	
}
