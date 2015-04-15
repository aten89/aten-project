package org.eapp.pos.captcha;

import java.io.IOException;
import java.util.Map;

/**
 * 验证码识别器
 * @author zsy
 *
 */
public class CaptchaReader {

	private static UUAPI uuAPI;
	private static int CODE_TYPE = 1004;//1004是codeType,http://www.uuwise.com/price.html
	
	public static void init(Map<String, String> props) {
		uuAPI = new UUAPI();
		uuAPI.setSoftID(Integer.parseInt(props.get("SOFT_ID")));
		uuAPI.setSoftKey(props.get("SOFT_KEY"));
		uuAPI.setDllVerifyKey(props.get("DLL_VERIFY_KEY"));
		uuAPI.setUsername(props.get("USERNAME"));
		uuAPI.setPassword(props.get("PASSWORD"));
		//校验API，必须调用一次，校验失败，打码不成功
		try {
			if(!uuAPI.checkAPI()) {
				throw new RuntimeException("API文件校验失败，无法使用打码服务");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取验证码
	 * @param data
	 * @return
	 */
	public static String[] getCaptcha(byte[] imgDate) {
		try {
			String[] result = uuAPI.getCaptcha(imgDate, CODE_TYPE);
			if (Integer.parseInt(result[0]) < 0) {
				throw new RuntimeException("读取验证码出错：" + result[0] + "。请参阅提供商信息：http://www.uuwise.com/allErrorCode.html");
			}
			return result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 验证码获取不对报告
	 * @param codeID
	 * @return
	 */
	public static void reportError(String codeID) {
		uuAPI.reportError(Integer.parseInt(codeID));
	}
}




