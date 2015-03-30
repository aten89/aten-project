package org.eapp.pos.dataexp;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.pos.captcha.CaptchaReader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 快钱交易数据抽取
 * @author zhuoshiyao
 *
 */
public class POS99Bill extends URLRequest {
	private static final Log log = LogFactory.getLog(POS99Bill.class);
	private String pageEncoding = "utf-8";
	private long reLoginwaitTime = 5000;//超时重新登录时等待时间
	
	/**
	 * 登录快钱
	 * @throws IOException
	 */
	private void login() throws IOException {
		
		//重新登录前清除记录的Session
		clearSession();
		
		byte[] data = getURLData("https://mrs.99bill.com/Mrs/captcha", "GET", null);
//		FileOutputStream img = new FileOutputStream("D:/code.png");
//		img.write(data);
//		img.close();
        
		String[] codeRel = CaptchaReader.getCaptcha(data);
		String code = codeRel[1];
		data = getURLData("https://mrs.99bill.com/Mrs/login.do", "POST", "loginController=1&userName=" + loginUsername + "&loginPassword=" + loginPassword + "&captcheCode=" + code);
		String str = new String(data, pageEncoding);
		if (str.indexOf("附加码错误") > 0) {
			CaptchaReader.reportError(codeRel[0]);
			log.warn("登录验证码错误，重新尝试：" + Arrays.toString(codeRel));
			
    		login();
    	}
	}
	
	/**
	 * 等待几秒重新登录
	 * @throws IOException
	 */
	private void waitToLogin() throws IOException {
		try {
			Thread.sleep(reLoginwaitTime);
		} catch(Exception e) {
			log.error(e);
		}
		login();
	}
	
	/**
	 * 请求是否超时，是的话要重新登录
	 * @param retStr
	 * @return
	 */
	private boolean isLoginTimeout(String retStr) {
		if (retStr == null) {
			return true;
		}
		return retStr.indexOf("<li class=\"logintoL\">密码：</li>") > 0;
	}
	
	/**
	 * 查询列表
	 * @param startDate
	 * @param endDate
	 * @param pageSize
	 * @param pageNo
	 * @return
	 * @throws IOException
	 */
	public String loadList(String startDate, String endDate, int pageSize, int pageNo) throws IOException {
		byte[] data = getURLData("https://mrs.99bill.com/Mrs/cardPayTxnList/search_s", "GET", 
				"statmentTypes=ALL&max=" + pageSize + "&pageNo=" + pageNo + "&startTxnDate=" + startDate + "&endTxnDate=" + endDate);
		String str = new String(data, pageEncoding);
		if (isLoginTimeout(str)) {
			log.warn("Session超时，重新登录，查询条件：" + startDate + "到" + endDate);
			//如果超时重新登录
			waitToLogin();
			str = loadList(startDate, endDate, pageSize, pageNo);
		}
		return str;
	}
	
	/**
	 * 查询详情
	 * @param recordId
	 * @return
	 * @throws IOException
	 */
	public String loadDetail(String recordId) throws IOException {
		byte[] data = getURLData("https://mrs.99bill.com/Mrs/cardPayTxnList/show/" + recordId, "GET", "orSettleMerchant=&idTxnCtrl=" + recordId);
		String str = new String(data, pageEncoding);
		if (isLoginTimeout(str)) {
			log.warn("Session超时，重新登录，查询条件：" + recordId);
			//如果超时重新登录
			waitToLogin();
			str = loadDetail(recordId);
		}
		return str;
	}
	
	private void readFields(String listStr) throws IOException {
		List<String> matTexts = getMatchTexts(listStr, "<a href=\"#nulllink\" onclick=\"showeditDiv(.*?);\" id=\"linkdialog_(.*?)\" >明细</a>", "$2");
		String detailStr = null;
		for (String tex : matTexts) {
			detailStr = loadDetail(tex);
			System.out.println(getMatchText(detailStr, "<td class=\"tableBg\">系统参考编号</td>[\\s]+<td>(.*?)</td>", "$1"));
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Map<String, String> props = new HashMap<String, String>();
		props.put("SOFT_ID", "104468");
		props.put("SOFT_KEY", "d909a3bc62894ccf9ccc9623a69007b8");
		props.put("DLL_VERIFY_KEY", "B678C850-501A-4537-A6B3-35FDA0D50C75");
		props.put("USERNAME", "ato200");
		props.put("PASSWORD", "1314520");
		CaptchaReader.init(props);
		
		//一个快钱账号初始化一个实例
		POS99Bill test = new POS99Bill();
		test.setKeystoreFile("D:/1.pfx");
		test.setKeystorePassword("vpos123");
		test.setLoginUsername("001");
		test.setLoginPassword("99bill@99bill");
		
		//初始化证书
		test.initSSLSocketFactory();
		
		long t = System.currentTimeMillis();
		//登录
		test.login();
		
		long t1 = System.currentTimeMillis();
		System.out.println("login time:" + (t1-t));
		
		String str = test.loadList("2015-03-28 00:00:00", "2015-03-28 10:59:59", 20, 1);
		int pageCounts = Integer.parseInt(getMatchText(str, "&nbsp;共(.*?)页&nbsp;", "$1"));
		
		
		System.out.println("===========================");
		test.readFields(str);
		for (int i = 2; i<=pageCounts; i++) {
			System.out.println("page:" +i);
			str = test.loadList("2015-03-28 00:00:00", "2015-03-28 10:59:59", 20, i);
			test.readFields(str);
		}
		System.out.println("===========================\n");
		long t2 = System.currentTimeMillis();
		System.out.println("load time:" + (t2-t1));
	}
}
