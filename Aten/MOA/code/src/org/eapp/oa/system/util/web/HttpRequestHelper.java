/*
 * 创建日期 2005-12-13
 *
 */
package org.eapp.oa.system.util.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;


/**
 * HttpRequestHelper
 *  
 * HTTP requst参数读取辅助类
 * 实现各种常用数据类型的参数转换及空值处理
 * 
 * @author linliangyi@team of miracle
 * @version 1.0
 * modify by zhuoshiyao 2007.8.22
 */

public class HttpRequestHelper {
	/**
	 * 空字符串
	 */
	private static final String NULLSTRING = "";
	/**
	 * 默认编码
	 */
	public static final String DEFAULT_ENCODING;
	/**
	 * 目录编码
	 */
	public static final String TARGET_ENCODING;
	
	static {
		Properties prop = new Properties();
		InputStream pin = null;
		try {
			pin = HttpRequestHelper.class.getResourceAsStream("HttpRequestHelper.properties");
			prop.load(pin);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pin != null) {
				try {
					pin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String key = prop.getProperty("DEFAULT_ENCODING");
		if (key != null && !key.trim().equals(NULLSTRING)) {
			DEFAULT_ENCODING = key;
		} else {
			DEFAULT_ENCODING = "ISO-8859-1";
		}
		key = prop.getProperty("TARGET_ENCODING");
		if (key != null && !key.trim().equals(NULLSTRING)) {
			TARGET_ENCODING = key;
		} else {
			TARGET_ENCODING = null;
		}
	}
	
	/**
	 * 对HTTP接收的参数进行编码转换 
	 * @param request HttpServletRequest
	 * @param name 参数名
	 * @param encoding 编码
	 * @param defautlValue 默认值
	 * @return 参数值
	 */
	public static String getEncodedParameter(HttpServletRequest request,
			String name, String encoding, String defautlValue) {
		String temp = request.getParameter(name);
		if (temp == null || temp.trim().equals(NULLSTRING)) {
			return defautlValue;
		}
		if (encoding == null) {
			return temp;
		}
		try {
			temp = new String(temp.getBytes(DEFAULT_ENCODING), encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return defautlValue;
		}
		return temp;
	}

	/**
	 * 对HTTP接收的参数进行编码转换 
	 * @param request HttpServletRequest
	 * @param name 参数名
	 * @param encoding 编码
	 * @return 参数值
	 */
	public static String getEncodedParameter(HttpServletRequest request,
			String name, String encoding) {
		return getEncodedParameter(request, name, encoding, null);
	}
	
	/**
	 * 取得HTTP参数，值为空字符串时返加null
	 * @param request HttpServletRequest
	 * @param name 参数名
	 * @return 参数值
	 */
	public static String getParameter(HttpServletRequest request, String name) {
		return getEncodedParameter(request, name, TARGET_ENCODING, null);
	}

	/**
	 * 取得HTTP参数，值为空字符串或null时返回默认值
	 * @param request HttpServletRequest
	 * @param name 参数名
	 * @param defaultValue 默认值
	 * @return 参数值
	 */
	public static String getParameter(HttpServletRequest request, String name,
			String defaultValue) {
		return getEncodedParameter(request, name, TARGET_ENCODING, defaultValue);
	}

	/**
	 * 对HTTP接收的参数数组进行编码转换
	 * @param request HttpServletRequest
	 * @param name 参数名
	 * @param encoding 编码
	 * @return 参数值
	 */
	public static String[] getEncodedParameters(HttpServletRequest request,
			String name, String encoding) {

		String[] temp = request.getParameterValues(name);
		if (temp == null) {
			return null;
		}
		if (encoding == null) {
			return temp;
		}
		try {
			for (int i = 0; i < temp.length; i++) {
				if (temp[i] != null) {
					temp[i] = new String(temp[i].getBytes(DEFAULT_ENCODING), encoding);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return temp;
	}
	
	/**
	 * 对HTTP接收的参数数组进行编码转换
	 * @param request HttpServletRequest
	 * @param name 参数名
	 * @return 参数值
	 */
	public static String[] getParameters(HttpServletRequest request,
			String name) {
		return getEncodedParameters(request, name, TARGET_ENCODING);
	}

	/**
	 * 值为"trur"或'y"时返回true，否则返回false
	 * @param request HttpServletRequest
	 * @param name 参数名
	 * @param defaultVal 默认值
	 * @return 参数值
	 */
	public static boolean getBooleanParameter(HttpServletRequest request,
			String name, boolean defaultVal) {
		String temp = request.getParameter(name);
		if ("true".equalsIgnoreCase(temp) || "y".equalsIgnoreCase(temp)) {
			return true;
		} else if ("false".equalsIgnoreCase(temp) || "n".equalsIgnoreCase(temp)) {
			return false;
		} else {
			return defaultVal;
		}
	}
	
	/**
	 * 把取得的参数传化为int类型
	 * @param request HttpServletRequest
	 * @param name 参数名
	 * @param defaultNum 默认值
	 * @return 参数值
	 */
	public static int getIntParameter(HttpServletRequest request, String name,
			int defaultNum) {
		String temp = request.getParameter(name);
		if (temp == null || temp.trim().equals(NULLSTRING)) {
			return defaultNum;
		}
		try {
			defaultNum = Integer.parseInt(temp);
		} catch (Exception ex) {
			System.err.println(ex.toString());
		}
		return defaultNum;
	}

	/**
	 * 把取得的参数传化为double类型
	 * @param request HttpServletRequest
	 * @param name 参数名
	 * @param defaultNum 默认值
	 * @return 参数值
	 */
	public static Double getDoubleParameter(HttpServletRequest request,
			String name, Double defaultNum) {
		String temp = request.getParameter(name);
		if (temp == null || temp.trim().equals(NULLSTRING)) {
			return defaultNum;
		}
		try {
			defaultNum = Double.valueOf(temp);
		} catch (Exception ex) {
			System.err.println(ex.toString());
		}
		return defaultNum;
	}

	/**
	 * 把取得的参数传化为long类型
	 * @param request HttpServletRequest
	 * @param name 参数名
	 * @param defaultNum 默认值
	 * @return 参数值
	 */
	public static long getLongParameter(HttpServletRequest request,
			String name, long defaultNum) {
		String temp = request.getParameter(name);
		if (temp == null || temp.trim().equals(NULLSTRING)) {
			return defaultNum;
		}
		try {
			defaultNum = Long.parseLong(temp);
		} catch (Exception ex) {
			System.err.println(ex.toString());
		}
		return defaultNum;
	}
}