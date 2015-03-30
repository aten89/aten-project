package org.eapp.util.mail;

import java.util.Properties;

/**
 * 邮件配置信息
 * 如果是web应用，可以在应用启动时，从配置文件解析初始化
 * 配置文件格式如：
 * <!--配置邮箱服务器-->
 * <emails>
 * 	<email>
 * 		<property name="mail.smtp.host">smtp.163.com</property>
 * 		<property name="mail.smtp.auth">true</property>
 * 		<property name="mail.smtp.port">25</property>
 * 		<property name="mail.smtp.quitwait">false</property>
 * 		
 * 		<sendaddr>发送者</sendaddr>
 * 		<username>zhuoshiyao</username>
 * 		<password>123456</password>
 * 	</email>
 *  <emails>
 *  	...
 *  </emails>
 * </emails>
 * 
 * @author 卓诗垚
 * @version Dec 30, 2008
 */
public class MailConfig {
	/**
	 * 邮件服务配置属性
	 */
	private Properties props;
	/**
	 * 发送邮箱地址
	 */
	private String sendAddress;
	/**
	 * 邮箱用户名
	 */
	private String username;
	/**
	 * 邮箱密码
	 */
	private String password;
	/**
	 * 获取属性
	 * @return 邮件服务配置属性
	 */
	public Properties getProps() {
		return props;
	}
	/**
	 * 设置属性
	 * @param props 邮件服务配置属性
	 */
	public void setProps(Properties props) {
		this.props = props;
	}
	/**
	 * 获取属性
	 * @return 发送邮箱地址
	 */
	public String getSendAddress() {
		return sendAddress;
	}
	/**
	 * 设置属性
	 * @param sendAddress 发送邮箱地址
	 */
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}
	/**
	 * 获取属性
	 * @return 邮箱用户名
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 设置属性
	 * @param username 邮箱用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取属性
	 * @return 邮箱密码
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 设置属性
	 * @param password 邮箱密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 添加服务配置属性
	 * @param key 键
	 * @param value 值
	 */
	public void addProperty(String key, String value) {
		if (props == null) {
			props = new Properties();
		}
		//添加属性
		props.put(key, value);
	}
}
