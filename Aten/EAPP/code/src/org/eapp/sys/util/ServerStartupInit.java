package org.eapp.sys.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.rpc.session.RPCPrincipal;
import org.eapp.rpc.session.RPCSessionContainer;
import org.eapp.sys.config.SysCodeDictLoader;
import org.eapp.sys.timer.QuartZTimer;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.UploadConfig;
import org.eapp.workflow.WfmConfiguration;
import org.hibernate.SessionFactory;

/**
 * 报表中心，WEB服务器启动初始化内容
 * 通过Spring的IoC容器的初始化来加载
 * 在Spring配置文件中配置实现，将Bean的初始化方法设为init，销毁方法设为destroy
 * 初始化文件组件的配置等信息
 * 
 * @author zsy
 *
 */
public class ServerStartupInit {
	/**
	 * 日志
	 */
	private static final Log log = LogFactory.getLog(ServerStartupInit.class);
	
	/**
	 * 流程引擎SessionFactory
	 */
	private SessionFactory wfSessionFactory;
	/**
	 * uploadConfig
	 * 上传文件配置信息
	 */
	private UploadConfig uploadConfig;
	/**
	 * rpc session失效间隔时间，单位：秒
	 */
	private int rpcSessionInvalid;
	/**
	 * rpc principal更新间隔时间，单位：秒
	 */
	private int rpcPrincipalRefresh;
	
	/**
	 * CAS登出URL
	 */
	private String casLogoutUrl;
	/**
	 * CAS登录URL
	 */
	private String casLoginUrl;
	/**
	 * CAS验证URL
	 */
	private String casValidateUrl;
	/**
	 * 子系统配置，优先于数据库配置
	 */
	private Map<String, SubSystemConfig> subSystemConfig = new HashMap<String, SubSystemConfig>();

	/**
	 * 快捷菜单图标加载路径
	 */
	private String shortCutIconDir;
	
	public void setWfSessionFactory(SessionFactory wfSessionFactory) {
		this.wfSessionFactory = wfSessionFactory;
	}
	
	/**
	 * 从SPRING里注入
	 * @param uploadConfig 传文件配置信息
	 */
	public void setUploadConfig(UploadConfig uploadConfig) {
		this.uploadConfig = uploadConfig;
	}
	
	public void setRpcSessionInvalid(int rpcSessionInvalid) {
		this.rpcSessionInvalid = rpcSessionInvalid;
	}

	public void setRpcPrincipalRefresh(int rpcPrincipalRefresh) {
		this.rpcPrincipalRefresh = rpcPrincipalRefresh;
	}

	
	public String getCasLogoutUrl() {
		return casLogoutUrl;
	}

	public void setCasLogoutUrl(String casLogoutUrl) {
		this.casLogoutUrl = casLogoutUrl;
	}

	public String getCasLoginUrl() {
		return casLoginUrl;
	}

	public void setCasLoginUrl(String casLoginUrl) {
		this.casLoginUrl = casLoginUrl;
	}

	public String getCasValidateUrl() {
		return casValidateUrl;
	}

	public void setCasValidateUrl(String casValidateUrl) {
		this.casValidateUrl = casValidateUrl;
	}

	public Map<String, SubSystemConfig> getSubSystemConfig() {
		return subSystemConfig;
	}

	public void setSubSystemConfig(String subSystemConfigStr) {
		if (StringUtils.isBlank(subSystemConfigStr)) {
			return;
		}
		for (String confStr : subSystemConfigStr.split(";")) {
			if (StringUtils.isBlank(confStr)) {
				continue;
			}
			SubSystemConfig sys = new SubSystemConfig(confStr);
			subSystemConfig.put(sys.getSubSystemID(), sys);
		}
		
	}

	public String getShortCutIconDir() {
		return shortCutIconDir;
	}

	public void setShortCutIconDir(String shortCutIconDir) {
		this.shortCutIconDir = shortCutIconDir;
	}
	
	/**
	 * 获取快捷方式图标
	 * @return
	 */
	public List<String> getShortCutIcons(ServletContext context) {
		List<String> shortCutIcons = new ArrayList<String>();
		
		String basePath = context.getRealPath("/");
		File dir = new File(basePath, shortCutIconDir);
		if (dir.exists()){
			String[] iconNames = dir.list();	//这是所有的文件名
			for (String iconName : iconNames){
				if (iconName == null) {
					continue;
				}
				if (iconName.toLowerCase().endsWith(".png") 
						|| iconName.toLowerCase().endsWith(".jpg") 
						|| iconName.toLowerCase().endsWith(".gif")){
					shortCutIcons.add(shortCutIconDir + iconName);
				}
			}
		}
		return shortCutIcons;
	}

	/**
	 * 初始化方法
	 * 配置为Spring的init-method
	 */
	public void init() {
		log.info("EAPP Server start...");
		//启动定时器
		QuartZTimer.getInstance().init();
		//初始化数据字典表
		SysCodeDictLoader.getInstance().initAllCodeDict();
		//流程引擎数SessionFactory
		WfmConfiguration.getInstance().init(wfSessionFactory, null);
		//初始化上传文件管理的配置信息
		FileDispatcher.setConfig(uploadConfig);
		//RPC
		RPCSessionContainer.setMaxInactiveInterval(rpcSessionInvalid);
		RPCPrincipal.setRpcPrincipalRefreshInterval(rpcPrincipalRefresh);
		
	}
	
	/**
	 * 销毁
	 * 配置为Spring的destroy-method
	 */
	public void destroy() {
		//销毁定时器
		QuartZTimer.getInstance().destroy();
		//销毁流程引擎
		WfmConfiguration.getInstance().release();
	}

	//配置文件中的子系统配置信息
	public static class SubSystemConfig {
		private String subSystemID;
		private String domainName;
		private int port;
		private boolean isValid;
		
		public SubSystemConfig(String configString) {
			if (StringUtils.isBlank(configString)) {
				return;
			}
			String[] arrs = configString.trim().split("\\s+");
			subSystemID = arrs[0];
			domainName = arrs[1];
			if (arrs.length == 2) {
				port = 80;
				isValid = true;
				return;
			} 
			port = Integer.parseInt(arrs[2]);
			if (arrs.length == 3) {
				isValid = true;
				return;
			}
			isValid = Boolean.valueOf(arrs[3]);
		}
		public String getSubSystemID() {
			return subSystemID;
		}
		public void setSubSystemID(String subSystemID) {
			this.subSystemID = subSystemID;
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
		public Boolean getIsValid() {
			return isValid;
		}
		public void setIsValid(Boolean isValid) {
			this.isValid = isValid;
		}
	}
}
