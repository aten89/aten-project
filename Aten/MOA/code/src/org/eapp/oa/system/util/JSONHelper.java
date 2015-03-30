/**
 * 
 */
package org.eapp.oa.system.util;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.json.JSONUtil;
import org.dom4j.Document;
import org.eapp.oa.system.util.web.HttpRequestHelper;


/**
 * 输出JSON格式，解决跨域问题
 * @author zsy
 * @version 
 */
public class JSONHelper {
	/**
	 * 以JSON格式输出HTML片段
	 * @param request
	 * @param response
	 * @param html
	 * @return
	 * @throws Exception
	 */
	public static boolean outputHtmlJSON(HttpServletRequest request,
			HttpServletResponse response, String html) throws Exception {
		String format = HttpRequestHelper.getParameter(request, "format");
		if ("json".equals(format)) {
			String jsoncallback = HttpRequestHelper.getParameter(request, "jsoncallback");
			Map<String, String> obj = new HashMap<String, String>();
			obj.put("htmlValue", html);
			String json = JSONUtil.serialize(obj, null, null, false, true);
			if (jsoncallback != null) {
				json = jsoncallback + "(" + json + ")";
			}
			response.setContentType("text/javascript;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(json);
			return true;
		}
		return false;
	}
	
	/**
	 * 以JSON格式输出XML片段
	 * @param request
	 * @param response
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public static boolean outputXmlJSON(HttpServletRequest request,
			HttpServletResponse response, Document doc) throws Exception {
		if (doc == null) {
			return true;
		}
		String format = HttpRequestHelper.getParameter(request, "format");
		if ("json".equals(format)) {
			String jsoncallback = HttpRequestHelper.getParameter(request, "jsoncallback");
			Map<String, String> obj = new HashMap<String, String>();
			obj.put("xmlValue", doc.asXML());
			String json = JSONUtil.serialize(obj, null, null, false, true);
			if (jsoncallback != null) {
				json = jsoncallback + "(" + json + ")";
			}
			response.setContentType("text/javascript;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(json);
			return true;
		}
		return false;
	}
}
