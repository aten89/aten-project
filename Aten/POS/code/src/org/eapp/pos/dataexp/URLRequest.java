package org.eapp.pos.dataexp;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class URLRequest {
	protected String keystoreFile;//证书文件
	protected String keystorePassword;//证书密码
	
	protected String loginUsername;//登录用户名
	protected String loginPassword;//登录密码
	
	private SSLSocketFactory ssf;//证书工厂
	private String sessionID;//保持的SessionID
	
	public void setKeystoreFile(String keystoreFile) {
		this.keystoreFile = keystoreFile;
	}

	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	public void setLoginUsername(String loginUsername) {
		this.loginUsername = loginUsername;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	/**
	 * 清空Session
	 */
	public void clearSession() {
		sessionID = null;
	}
	
	/**
	 * 初始化证书
	 * @param keystoreFile
	 * @param keystorePassword
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	public void initSSLSocketFactory() throws GeneralSecurityException, IOException {
		KeyStore ks = KeyStore.getInstance("PKCS12"); 
        ks.load(new FileInputStream(keystoreFile),keystorePassword.toCharArray());
        
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509"); 
        kmf.init(ks,keystorePassword.toCharArray()); 
        
        TrustManager[] trustAllCerts = new TrustManager[] { 
        	new X509TrustManager() { 
        		public X509Certificate[] getAcceptedIssuers() { 
        			return null; 
        		} 
        		public void checkServerTrusted(X509Certificate[] certs, String authType) {
        			
        		} 
        		public void checkClientTrusted(X509Certificate[] certs, String authType) {
        			
        		} 
        	}
        };
        
		SSLContext ctx = SSLContext.getInstance("TLS");            
        ctx.init(kmf.getKeyManagers(), trustAllCerts, null); 
        ssf = ctx.getSocketFactory(); 
	}

	/**
	 * 请求URL
	 * @param urlStr
	 * @param method
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public byte[] getURLData(String urlStr, String method, String params) throws IOException {
		HttpURLConnection conn = null;
		PrintWriter out = null;
		InputStream in = null;
		try {
			conn = (HttpURLConnection) new URL(urlStr).openConnection();
			if (conn instanceof HttpsURLConnection) {
				HttpsURLConnection sconn = (HttpsURLConnection)conn;
				sconn.setSSLSocketFactory(ssf);
			}
			conn.setUseCaches(false);
			conn.setDoOutput(true); 
		    conn.setDoInput(true); 
			conn.setRequestMethod(method);
			if (sessionID != null) {
	     		conn.setRequestProperty("Cookie", sessionID);
	     	} else {
	     		String sessionCookie = conn.getHeaderField("Set-Cookie");
	     		sessionID = sessionCookie.substring(0, sessionCookie.indexOf(";"));
	     	}
			if (params != null) {
				out = new PrintWriter(conn.getOutputStream()); 
				out.print(params);//发送请求参数 
				out.flush();//flush输出流的缓冲
			}
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("请求URL失败:" + urlStr);
			}
			
			in = conn.getInputStream();
			ByteArrayOutputStream bStream = new ByteArrayOutputStream(); 
			byte[] buff = new byte[1024];
			int length = 0; 
			while ((length = in.read(buff)) > 0) { 
				bStream.write(buff, 0, length);
			} 
			
			return bStream.toByteArray();
		} finally {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
	
	/**
	 * 返回匹配正则表达式的给定参数的值
	 * @param pageDate
	 * @param regex 正则表达式
	 * @param arg 要返回正则表达式中的哪个参数值
	 * @return 返回arg给定参数的值
	 */
	public static String getMatchText(String pageText, String regex, String arg) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(pageText);
		if (matcher.find()) {
			String strContent = matcher.group();
			Matcher subMatcher = pattern.matcher(strContent);
			return subMatcher.replaceAll(arg);
		}
		return "";
	}
	
	/**
	 * 返回匹配正则表达式的给定参数的值
	 * @param pageDate
	 * @param regex 正则表达式
	 * @param arg 要返回正则表达式中的哪个参数值
	 * @return 返回arg给定参数的值
	 */
	public static List<String> getMatchTexts(String pageText, String regex, String arg) {
		List<String> matStrs = new ArrayList<String>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(pageText);
		String strContent = null;
		Matcher subMatcher = null;
		while (matcher.find()) {
			strContent = matcher.group();
			subMatcher = pattern.matcher(strContent);
			matStrs.add(subMatcher.replaceAll(arg));
		}
		return matStrs;
	}
}
