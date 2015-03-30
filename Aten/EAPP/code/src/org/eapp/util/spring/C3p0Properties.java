package org.eapp.util.spring;

import java.util.Properties;

import org.eapp.util.security.SymmetricAlgorithm;


/**
 * 此类来替换数据源密码
 * @author zsy
 *
 */
public class C3p0Properties extends Properties {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = -7948742761676310651L;

	/**
	 * 重载父类实现
	 * @param properties 属性信息
	 */
	public void setProperties(Properties properties) {
		String encrypt = properties.getProperty("encrypt");
		if ("true".equals(encrypt)) {
			String password = properties.getProperty("password");
			//解密
			password = new SymmetricAlgorithm(password).desDecrypt();
			//设置解密后密码
			properties.put("password", password);
		}
		this.putAll(properties);
	}
	
}
