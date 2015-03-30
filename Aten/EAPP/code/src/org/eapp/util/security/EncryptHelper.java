/**
 * 
 */
package org.eapp.util.security;

/**
 * 加密辅助类
 * @author zsy
 * @version Jun 4, 2009
 */
public final class EncryptHelper {
	/**
	 * 不需要被创建实例
	 */
	private EncryptHelper() {
		
	}
	
	/**
	 * 默认编码
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * 二进制转字符串
	 * @param bytes 二进制
	 * @return 字符串
	 */
	public static String byte2hex(byte[] bytes) {
		StringBuffer retString = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			//逐字节转换
			retString.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1));
		}
		//返回大写
		return retString.toString();
	}

	/**
	 * 字符串转二进制
	 * @param hex 字符串
	 * @return 二进制
	 */
	public static byte[] hex2byte(String hex) {
		byte[] bts = new byte[hex.length() / 2];
		for (int i = 0; i < bts.length; i++) {
			//逐字符转换
			bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bts;
	}
}
