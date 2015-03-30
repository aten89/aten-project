package org.eapp.poss.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;


public class XMLResponse {
	
	public static final String DEFAULT_ENCODING = "utf-8";
    
	/**
	 * 生成基本的请求XML文档对象，返回前台的格式如：
	 * 
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1">保存成功</message>
	 * </root>
	 * 
	 * @param dto
	 * @return
	 */
    public static Document createStandardResponse(int code, String message){ 	
    	Document doc = DocumentHelper.createDocument();
    	doc.setXMLEncoding(DEFAULT_ENCODING);
    	Element root = doc.addElement("root");
    	root.addElement("message").addAttribute("code", Integer.toString(code))
    			.addCDATA(message);
    	return doc;
    }
    
    /**
     * 输出基本的XML形式的操作信息
     * @param response
     * @param code
     * @param message
     * @throws IOException
     */
    public static void outputStandardResponse(HttpServletResponse response, int code, 
    		String message) throws IOException {
    	XMLResponse.outputXML(response, 
				XMLResponse.createStandardResponse(code, message));
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
	
	/**
	 * 输出XML信息
	 * @param response HttpServletResponse
	 * @param xmlDoc XML Document文档对象
	 * @throws IOException
	 */
    public static void outputXML(HttpServletResponse response, Document xmlDoc)
    		throws IOException {
    	response.setHeader("Content-type","text/xml;charset=" + DEFAULT_ENCODING);
    	outputXML(response, xmlDoc, DEFAULT_ENCODING);
    }
    
    /**
	 * 向HttpServletResponse 输出XML Document
	 * @param response HttpServletResponse
	 * @param xmlDoc XML Document文档对象
	 * @param encoding 输出编码格式
	 * @throws IOException
	 */
    public static void outputXML(HttpServletResponse response, Document xmlDoc, String encoding)
    		throws IOException {
    	if (response == null || xmlDoc == null) {
    		throw new IllegalArgumentException("Null Argument");
    	}
    	if(encoding == null){
    		encoding = DEFAULT_ENCODING;
    	}
    	ServletOutputStream out = response.getOutputStream();
    	writeXMLResponse(out, xmlDoc, encoding);
    }
    
    /**
     * 向指定的输出流输出XML Document对象
     * @param out 输出流
     * @param xmlDoc XML Document文档对象
     * @param encoding 输出编码格式
     * @throws IOException IO异常
     */
    public static void writeXMLResponse(OutputStream out, Document xmlDoc, 
    			String encoding) throws IOException {        
    	if (out == null || xmlDoc == null) {
    		throw new IllegalArgumentException("Null Argument");
    	}
    	if(encoding == null){
    		encoding = DEFAULT_ENCODING;
    	}
    	
        OutputFormat formatter = new OutputFormat();
        formatter.setEncoding(encoding);
        
        XMLWriter xmlWriter = null;
        try {
            xmlWriter = new XMLWriter(out , formatter);
            xmlWriter.write(xmlDoc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
