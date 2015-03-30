/*
 * 创建日期 2005-12-13
 *
 */
package org.eapp.oa.system.util;

import java.io.UnsupportedEncodingException;
import java.util.List;



public class FileUploadHelper {

	private static String NULLSTRING = "";
	
	public static final String DEFAULT_ENCODING = "ISO-8859-1";
	
//	public static final String TARGET_ENCODING = null;
	
	/**
	 * 对文件上传对像接收的参数进行编码转换 
	 * @param request
	 * @param name
	 * @param encoding
	 * @param defautlValue
	 * @return
	 */
	public static String getEncodedParameter(SimpleFileUpload upload,
			String name, String encoding) {
		String[] values = getEncodedParameters(upload, name, encoding);
		if (values == null || values.length < 1) {
			return null;
		}
		String value = values[0];
		if (value == null || value.trim().equals(NULLSTRING)) {
			return null;
		}
		return value;
		
	}
	
	/**
	 * 取得文件上传对像参数，值为空字符串时返加null
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getParameter(SimpleFileUpload upload, String name) {
		return getEncodedParameter(upload, name, null);
	}
	
	/**
	 * 对文件上传对像接收的参数数组进行编码转换
	 * @param request
	 * @param name
	 * @param encoding
	 * @return
	 */
	public static String[] getEncodedParameters(SimpleFileUpload upload,
			String name, String encoding) {
		List<String> values = upload.getParameters().get(name);
		if (values == null) {
			return null;
		}
		if (encoding == null) {
			return values.toArray(new String[0]);
		}
		try {
			String[] rs = new String[values.size()];
			for (int i = 0; i < values.size(); i++) {
				String temp = (String)values.get(i);
				if (temp != null) {
					rs[i] = new String(temp.getBytes(DEFAULT_ENCODING), encoding);
				}
			}
			return rs;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return values.toArray(new String[0]);
	}
	
	/**
	 * 对文件上传对像接收的参数数组进行编码转换
	 * @param request
	 * @param name
	 * @return
	 */
	public static String[] getParameters(SimpleFileUpload upload,
			String name) {
		return getEncodedParameters(upload, name, null);
	}
}