/**
 * 
 */
package org.eapp.util.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * DESede/DES对称加密算法的工具
 * @author zsy
 * @version Jun 3, 2009
 */
public class SymmetricAlgorithm {
	/**
	 * 默认加密KEY
	 */
	private String strKey = "&^%$*#@~";
	/**
	 * 加密或解密信息
	 */
	private String info;
	
	/**
	 * 使用默认密钥
	 * @param info 调用加密方法（desEncrypt）时info为明文，
	 * 		调用解密方法（desDecrypt）时info为密文
	 */
	public SymmetricAlgorithm(String info) {
		this.info = info;
	}
	
	/**
	 * 使用自定义密钥
	 * @param info 调用加密方法（desEncrypt）时info为明文，
	 * 		调用解密方法（desDecrypt）时info为密文
	 * @param strKey 自定义密钥
	 */
	public SymmetricAlgorithm(String info, String strKey) {
		this.info = info;
		this.strKey = strKey;
	}

	/**
	 * 取得密钥
	 * @return 密钥
	 */
	private Key getKey() {
		byte[] keyBtye = strKey.getBytes();
		//创建一个空的8位字节数组，只支持8位
		byte[] _keyByte = new byte[8];
		//转换为8位
		for (int i = 0; i < keyBtye.length && i < _keyByte.length; i++) {
			_keyByte[i] = keyBtye[i];
		}
		// 生成密钥
		return new SecretKeySpec(_keyByte, "DES");
	}
	
	/**
	 * 进行DES加密
	 * 使用默认的字符编码
	 * @return 加密后字符串
	 */
	public String desEncrypt() {
		return desEncrypt(info, EncryptHelper.DEFAULT_ENCODING);
	}
	
	/**
	 * DES加密
	 * @param origin 原始字符串
	 * @param encoding 编码
	 * @return 加密后字符串
	 */
	public String desEncrypt(String origin, String encoding) {
		if (origin == null || encoding == null) {
    		return null;
    	}
		try {
			//DES加密
			return encrypt(origin.getBytes(encoding), "DES");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 进行DES解密
	 * 使用默认的字符编码
	 * @return 加密后字符串
	 */
	public String desDecrypt() {
		//DES解密
		return desDecrypt(info, EncryptHelper.DEFAULT_ENCODING);
	}
	
	/**
	 * DES解密
	 * @param ciperData 加密后字符串
	 * @param encoding 编码
	 * @return 解密后字符串
	 */
	public String desDecrypt(String ciperData, String encoding) {
		if (ciperData == null || encoding == null) {
    		return null;
    	}
		//DES解密
		byte[] b = decrypt(EncryptHelper.hex2byte(ciperData), "DES");
		try {
			//转为String
			return new String(b, encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * 加密
	 * @param data 二进制原始数据
	 * @param algorithm 算法
	 * @return 加密后字符串
	 */
	private String encrypt(byte[] data, String algorithm) {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		try {
//			KeyGenerator keygen = KeyGenerator.getInstance(algorithm);
//			SecretKey deskey = keygen.generateKey();
			// 生成密钥
			Key key = getKey();
			// 加密
			Cipher c1 = Cipher.getInstance(algorithm);
			c1.init(Cipher.ENCRYPT_MODE, key);
			byte[] cipherByte = c1.doFinal(data);
			//转为字符串
			return EncryptHelper.byte2hex(cipherByte);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 解密
	 * @param data 二进制加密后数据
	 * @param algorithm 算法
	 * @return 解密后字符串
	 */
	private byte[] decrypt(byte[] data, String algorithm) {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		try {
//			SecretKeySpec mydeskey = new SecretKeySpec(key,algorithm);
			//密钥
			Key key = getKey();
			//解密
			Cipher c1 = Cipher.getInstance(algorithm);
			c1.init(Cipher.DECRYPT_MODE, key);
			return c1.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 调试
	 * @param args 参数
	 */
	public static void main(String[] args)  {
//		String encryptString = "aa";
//		SymmetricAlgorithm sa = new SymmetricAlgorithm(encryptString);
//		String p = sa.desEncrypt();
//
//		System.out.println("加密字符窜：" + encryptString);
//		System.out.println("加密后字符窜：" + p);
//		
//		sa = new SymmetricAlgorithm(p);
//		System.out.println("..:" + sa.desDecrypt());
		
		

		String encryptString = "eapp";
		//使用加密的字符
		String useKey = (args.length > 1) ? args[1] : null;
		SymmetricAlgorithm sa = null;
		if (useKey != null) {
			sa = new SymmetricAlgorithm(encryptString, useKey);
		} else {
			sa = new SymmetricAlgorithm(encryptString);
		}
		String info = sa.desEncrypt();
		System.out.println("明文:" + encryptString);
		System.out.println("密钥:" + useKey);
		System.out.println("密文:" + info);
		
		//测试解密
		if (useKey != null) {
			sa = new SymmetricAlgorithm(info, useKey);
		} else {
			sa = new SymmetricAlgorithm(info);
		}
		System.out.println("解密测试:" + sa.desDecrypt().equals(encryptString));
	}
}
