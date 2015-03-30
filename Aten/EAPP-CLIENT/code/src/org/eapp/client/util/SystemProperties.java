package org.eapp.client.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 配置文件处理器
 * @version 1.0
 */
public class SystemProperties {
	private static Log log = LogFactory.getLog(SystemProperties.class);

	public static final String SYSTEM_ID;// 注册在EAPP中的系统ID
	public static final String SERVICE_URL;// Hessian服务地址
	public static final String SERVICE_USERNAME;// 用户名称
	public static final String SERVICE_CREDENCE;// 用户密码
	
	public static final String SESSION_USER_KEY;//绑定HTTPSESSION的用户KEY
//	public static final String REQUEST_ERROR_MSG;//绑定HTTPSESSION的错误信息KEY
	
	//服务名称
	public static final String SERVICE_ACTOR_LOGIN;
	public static final String SERVICE_DATA_DICTIONARY;
	public static final String SERVICE_GROUP;
	public static final String SERVICE_LOG;
	public static final String SERVICE_POST;
	public static final String SERVICE_RIGHT;
	public static final String SERVICE_SESSION_ACCOUNT;
	public static final String SERVICE_SUB_SYSTEM;
	public static final String SERVICE_USER_ACCOUNT;
	public static final String SERVICE_FLOW_CONFIG;

	static {
		Properties prop = new Properties();
		InputStream pin = null;
		try {
			pin = SystemProperties.class.getResourceAsStream("/eapp-client.properties");
			prop.load(pin);
		} catch (Exception e) {
			log.error("加载属性文件失败", e);
		} finally {
			if (pin != null) {
				try {
					pin.close();
				} catch (IOException e) {
					log.error("关闭属性文件失败", e);
				}
			}
		}
		SYSTEM_ID = prop.getProperty("SYSTEM_ID");
		SERVICE_URL = prop.getProperty("SERVICE_URL");
		SERVICE_USERNAME = prop.getProperty("SERVICE_USERNAME");
		SERVICE_CREDENCE = prop.getProperty("SERVICE_CREDENCE");
		
		SESSION_USER_KEY = prop.getProperty("SESSION_USER_KEY");
		
		SERVICE_ACTOR_LOGIN = prop.getProperty("SERVICE_ACTOR_LOGIN");
		SERVICE_DATA_DICTIONARY = prop.getProperty("SERVICE_DATA_DICTIONARY");
		SERVICE_GROUP = prop.getProperty("SERVICE_GROUP");
		SERVICE_LOG = prop.getProperty("SERVICE_LOG");
		SERVICE_POST = prop.getProperty("SERVICE_POST");
		SERVICE_RIGHT = prop.getProperty("SERVICE_RIGHT");
		SERVICE_SESSION_ACCOUNT = prop.getProperty("SERVICE_SESSION_ACCOUNT");
		SERVICE_SUB_SYSTEM = prop.getProperty("SERVICE_SUB_SYSTEM");
		SERVICE_USER_ACCOUNT = prop.getProperty("SERVICE_USER_ACCOUNT");
		SERVICE_FLOW_CONFIG = prop.getProperty("SERVICE_FLOW_CONFIG");
	}
}