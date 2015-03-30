package org.eapp.hbean;

import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;
import org.eapp.sys.util.ServerStartupInit;
import org.eapp.sys.util.ServerStartupInit.SubSystemConfig;
import org.eapp.util.spring.SpringHelper;

/**
 * SubSystem entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SubSystem implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -2574023599157594828L;
	//SUBSYSTEM_ID,VARCHAR2(36),Nullable=N  --子系统ID
	private String subSystemID;
	//NAME_,VARCHAR2(100),Nullable=N        --系统名称
	private String name;
	//LOGOURL_,VARCHAR2(1024),Nullable=Y    --图标链接 
	private String logoURL;
	//IPADDRESS_,VARCHAR2(40),Nullable=Y    --IP地址
	private String ipAddress;
	//SERVERNAME_,VARCHAR2(100),Nullable=Y  --服务器名
	private String serverName;
	//DOMAINNAME_,VARCHAR2(100),Nullable=Y  --域名
	private String domainName;
	//PORT_,INTEGER,Nullable=Y              --端口
	private Integer port;
	//DESCRIPTION_,VARCHAR2(1024),Nullable=Y--备注
	private String description;
	//ISVALID_,INTEGER,Nullable=Y           --子系统是否可用
	private Boolean isValid = Boolean.TRUE;
//	private String logoutURL;
	// DISPLAYORDER_,INTEGER --显示顺序
	private Integer displayOrder;
	private Set<DataDictionary> dataDictionaries = new HashSet<DataDictionary>(0);
	private Set<Module> modules = new HashSet<Module>(0);

	// Constructors

	/** default constructor */
	public SubSystem() {
	}

	/** minimal constructor */
	public SubSystem(String subSystemID, String name) {
		this.subSystemID = subSystemID;
		this.name = name;
	}

	/** full constructor */
	public SubSystem(String subSystemID, String name, String logoURL,
			String ipAddress, String serverName, String domainName,
			Integer port, String description, Boolean isValid,
			Set<DataDictionary> dataDictionaries, Set<Module> modules) {
		this.subSystemID = subSystemID;
		this.name = name;
		this.logoURL = logoURL;
		this.ipAddress = ipAddress;
		this.serverName = serverName;
		this.domainName = domainName;
		this.port = port;
		this.description = description;
		this.isValid = isValid;
		this.dataDictionaries = dataDictionaries;
		this.modules = modules;
	}

	// Property accessors

	public String getSubSystemID() {
		return this.subSystemID;
	}

	public void setSubSystemID(String subSystemID) {
		this.subSystemID = subSystemID;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogoURL() {
		return this.logoURL;
	}
	public String getLogoURLPath() {
		if (logoURL == null) {
			return null;
		}
		return getBasePath() + logoURL;
	}

	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getServerName() {
		return this.serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getDomainName() {
		return this.domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public Integer getPort() {
		return this.port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsValid() {
		return this.isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}
	@JSON(serialize=false)
	public Set<DataDictionary> getDataDictionaries() {
		return this.dataDictionaries;
	}

	public void setDataDictionaries(Set<DataDictionary> dataDictionaries) {
		this.dataDictionaries = dataDictionaries;
	}
	@JSON(serialize=false)
	public Set<Module> getModules() {
		return this.modules;
	}

	public void setModules(Set<Module> modules) {
		this.modules = modules;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((subSystemID == null) ? 0 : subSystemID.hashCode());
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
		final SubSystem other = (SubSystem) obj;
		if (subSystemID == null) {
			if (other.subSystemID != null)
				return false;
		} else if (!subSystemID.equals(other.subSystemID))
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(this.getClass().getName());
		sb.append("[");
		sb.append("subSystemID=");
		sb.append(getSubSystemID());
		sb.append(",");
		sb.append("name=");
		sb.append(getName());
		sb.append(",");
		sb.append("logoURL=");
		sb.append(getLogoURL());
		sb.append(",");
		sb.append("ipAddress=");
		sb.append(getIpAddress());
		sb.append(",");
		sb.append("serverName=");
		sb.append(getServerName());
		sb.append(",");
		sb.append("domainName=");
		sb.append(getDomainName());
		sb.append(",");
		sb.append("port=");
		sb.append(getPort());
		sb.append(",");
		sb.append("description=");
		sb.append(getDescription());
		sb.append(",");
		sb.append("isValid=");
		sb.append(getIsValid());
		sb.append("]");
		return sb.toString();
	}

//	public String getLogoutURL() {
//		return logoutURL;
//	}
//	public String getLogoutURLPath() {
//		if (logoutURL == null) {
//			return null;
//		}
//		return getBasePath() + logoutURL;
//	}
//
//	public void setLogoutURL(String logoutURL) {
//		this.logoutURL = logoutURL;
//	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	public String getBasePath() {
		ServerStartupInit serverStartupInit = (ServerStartupInit)SpringHelper.getBean("serverStartupInit");
		SubSystemConfig sysConf = serverStartupInit.getSubSystemConfig().get(subSystemID);
		String domainName = null;
		int port = 80;
		if (sysConf != null) {
			//以配置文件中的优先
			domainName = sysConf.getDomainName();
			port = sysConf.getPort();
		} else {
			domainName = this.domainName;
			port = this.port;
		}
		if (domainName == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer("http://");
		sb.append(domainName).append(":").append(port);
		if (serverName != null) {
			sb.append( "/").append(serverName);
		}
		sb.append("/");
		return sb.toString();
	}
}