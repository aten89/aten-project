package org.eapp.crm.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.util.SystemProperties;
import org.eapp.crm.config.SysConstants;
import org.eapp.crm.timer.QuartZTimer;
import org.eapp.util.mail.JMailProxy;
import org.eapp.util.mail.MailConfig;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.UploadConfig;

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
	 * uploadConfig
	 * 上传文件配置信息
	 */
	private UploadConfig uploadConfig;
	/**
	 * mailConfig
	 * 邮件配置信息
	 */
	private MailConfig mailConfig;
	
	public void setUploadConfig(UploadConfig uploadConfig) {
		this.uploadConfig = uploadConfig;
	}

	public void setMailConfig(MailConfig mailConfig) {
		this.mailConfig = mailConfig;
	}

	/**
	 * 初始化方法
	 * 配置为Spring的init-method
	 */
	public void init() {
		log.info("EAPP Server start...");
		
	    SysConstants.SESSION_USER_KEY = SystemProperties.SESSION_USER_KEY;

		//启动定时器
		QuartZTimer.getInstance().init();
		
		//初始化上传文件管理的配置信息
		FileDispatcher.setConfig(uploadConfig);
		//初始化邮件服务的配置信息
		List<MailConfig> mailConfigs = new ArrayList<MailConfig>();
		mailConfigs.add(mailConfig);
		JMailProxy.setMailConfigs(mailConfigs);

		
	}
	
	/**
	 * 销毁
	 * 配置为Spring的destroy-method
	 */
	public void destroy() {
		//销毁定时器
		QuartZTimer.getInstance().destroy();
	}

}
