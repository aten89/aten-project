package org.eapp.oa.system.util.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class HTMLResponse {
	public static final String DEFAULT_ENCODING = "UTF-8";		//字符集名称
	
	/**
	 * 输出HTML
	 * @param response
	 * @param sContent
	 */
	public static void outputHTML(HttpServletResponse response, String sContent, String encoding)
			throws IOException {
		if (response == null || sContent == null) {
			throw new IllegalArgumentException("Null Argument");
		}
		if(encoding == null){
    		encoding = DEFAULT_ENCODING;
    	}
		response.setContentType("text/html;charset=" + encoding);
		PrintWriter out = response.getWriter();
		out.print(sContent);
	}
	
	/**
	 * 输出HTML
	 * @param response
	 * @param sContent
	 * @throws IOException
	 */
	public static void outputHTML(HttpServletResponse response, String sContent)
			throws IOException {
		outputHTML(response,sContent , DEFAULT_ENCODING);
	}
	
	/**
	 * 输出文本
	 * @param response
	 * @param sContent
	 */
	public static void outputText(HttpServletResponse response, String sContent)
			throws IOException {
		if (response == null || sContent == null) {
			throw new IllegalArgumentException("Null Argument");
		}
		PrintWriter out = response.getWriter();
		out.print(sContent);
	}
}
