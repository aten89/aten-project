/*
 * 创建日期 2005-12-16
 * AJAX简单应答工具类
 * 林良益 @ miracle
 */
package org.eapp.oa.system.util.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * 
 * XML应答输出工具类
 * 
 * @author zsy
 * 
 */

public class XMLResponse {
	
	public static final String DEFAULT_ENCODING = "utf-8";
	
    /**
     * 输出基本的XML形式的操作信息，格式如
     * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1">保存成功</message>
	 * </root>
     * @param response
     * @param code
     * @param message
     * @throws IOException
     */
    public static void outputStandardResponse(HttpServletResponse response, int code, 
    		String message) throws IOException {
    	Document doc = DocumentHelper.createDocument();
    	doc.setXMLEncoding(DEFAULT_ENCODING);
    	Element root = doc.addElement("root");
    	root.addElement("message").addAttribute("code", Integer.toString(code)).addCDATA(message);
    	XMLResponse.outputXML(response, doc);
    }
	
	/**
	 * 输出XML信息
	 * @param response HttpServletResponse
	 * @param xmlDoc XML Document文档对象
	 * @throws IOException
	 */
    public static void outputXML(HttpServletResponse response, Document xmlDoc)
    		throws IOException {
    	response.setHeader("Content-type","text/xml;charset=" + DEFAULT_ENCODING);
    	
    	ServletOutputStream out = response.getOutputStream();
   	 	OutputFormat formatter = new OutputFormat();
        formatter.setEncoding(DEFAULT_ENCODING);
        
        XMLWriter xmlWriter = new XMLWriter(out , formatter);
        xmlWriter.write(xmlDoc);
    }
    
    /**
	 * 输出XML信息
	 * @param response HttpServletResponse
	 * @param xmlStr XML字符串
	 * @throws IOException
	 */
	public static void outputXML(HttpServletResponse response, String xmlStr)
			throws IOException {
		if (response == null || xmlStr == null) {
			throw new IllegalArgumentException("Null Argument");
		}
		response.setContentType("text/xml;charset=" + DEFAULT_ENCODING);
		PrintWriter out = response.getWriter();
		out.println(xmlStr);
	}
}
