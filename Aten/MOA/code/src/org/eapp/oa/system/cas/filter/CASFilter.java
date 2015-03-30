/**
 * 
 */
package org.eapp.oa.system.cas.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.eapp.client.cas.filter.SubSystemCASFilter;
import org.eapp.oa.system.config.SysConstants;

/**
 * 子系统采用的过CAS过滤器
 * 
 * @author zsy
 * @version 
 */
public class CASFilter extends SubSystemCASFilter {
	
	@Override
    protected void sendError(HttpServletResponse response, int code, String text) throws IOException {
    	/**
    	 * 输出格式如
    	 * <?xml version="1.0" encoding="utf-8" ?> 
    	 * <root>
    	 * 	<message code="1">保存成功</message>
    	 * </root>
    	 */
    	Document doc = DocumentHelper.createDocument();
    	doc.setXMLEncoding("utf-8");
    	Element root = doc.addElement("root");
    	root.addElement("message").addAttribute("code", Integer.toString(code)).addCDATA(text);
    	
    	
    	OutputFormat formatter = new OutputFormat();
        formatter.setEncoding("utf-8");
        
        response.setHeader("Content-type","text/xml;charset=utf-8");
        XMLWriter xmlWriter = new XMLWriter(response.getOutputStream(), formatter);
        xmlWriter.write(doc);
//        Servlet流不需要半闭
//        xmlWriter.close();
    }
    
    @Override
    protected void sendError(HttpServletRequest request,HttpServletResponse response, String text) throws IOException, ServletException {
    	request.setAttribute(SysConstants.REQUEST_ERROR_MSG, text);
		request.getRequestDispatcher("/page/error.jsp").forward(request, response);
    }
    
	@Override
    public String getModuleKey(HttpServletRequest request) {
    	StringBuffer requestURL = request.getRequestURL();
		if (requestURL == null) {
			return null;
		}
		int start = requestURL.indexOf("/m/");
		if (start < 0) {
			 return null;
		}
		int end = requestURL.lastIndexOf("/");
		if (end - start > 2) {
			return requestURL.substring(start + 3, end);
		} else {
			return requestURL.substring(start + 3);
		}
    }
    
	@Override
    public String getActionKey(HttpServletRequest request) {
    	String actionKey = request.getParameter("act");
    	if (actionKey == null) {
    		return null;
    	}
    	int start = actionKey.indexOf("_");
		if (start > 0) {
			actionKey = actionKey.substring(0, start);
		}
    	return actionKey;
    }
}
