/**
 * 
 */
package org.eapp.util.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 消息摘要
 * 使用MD5和SHA算法
 * @author zsy
 * @version Jun 3, 2009
 */
public final class DigestAlgorithm {
	/**
	 * 不需要被创建实例
	 */
	private DigestAlgorithm() {
		
	}
	/**
	 * 生成MD5摘要
	 * 使用默认的字符编码
	 * @param origin 原始字符串
	 * @return MD5摘要后字符串
	 */
	public static String md5Digest(String origin) {
		//MD5摘要
		return md5Digest(origin, EncryptHelper.DEFAULT_ENCODING);
	}
	
	/**
	 * 生成MD5摘要
	 * @param origin 原始字符串
	 * @param encoding 编码
	 * @return MD5摘要后字符串
	 */
	public static String md5Digest(String origin, String encoding) {
		if (origin == null || encoding == null) {
    		return null;
    	}
		try {
			//MD5摘要
			return digest(origin.getBytes(encoding), "MD5");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 生成SHA摘要
	 * 使用系统默认的字符编码
	 * @param origin 原始字符串
	 * @return SHA摘要后字符串
	 */
	public static String shaDigest(String origin) {
		return shaDigest(origin, EncryptHelper.DEFAULT_ENCODING);
	}
	
	/**
	 * 生成SHA摘要
	 * @param origin 原始字符串
	 * @param encoding 编码
	 * @return SHA摘要后字符串
	 */
	public static String shaDigest(String origin, String encoding) {
		if (origin == null || encoding == null) {
    		return null;
    	}
		try {
			//SHA摘要
			return digest(origin.getBytes(encoding), "SHA");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 完成MD5或SHA的消息摘要
	 * @param data 摘要数据
	 * @param algorithm 算法
	 * @return 摘要后字符串
	 */
	private static String digest(byte[] data, String algorithm) {
		try {
			//根据算法取得MessageDigest
			MessageDigest md = MessageDigest.getInstance(algorithm);
			//摘要
			byte[] b = md.digest(data);
			//转字符串
			return EncryptHelper.byte2hex(b);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
