package org.eapp.crm.system.util.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * HTMLResponse
 * 
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-4	    lhg		新建
 * </pre>
 */
public class HTMLResponse {

    /**
     * 字符集名称
     */
    public static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * 输出HTML
     * 
     * @param response
     * @param sContent
     * @param encoding
     * @throws IOException
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-4	    lhg		新建
     * </pre>
     */
    public static void outputHTML(HttpServletResponse response, String sContent, String encoding) throws IOException {
        if (response == null || sContent == null) {
            throw new IllegalArgumentException("Null Argument");
        }
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        response.setContentType("text/html;charset=" + encoding);
        PrintWriter out = response.getWriter();
        out.print(sContent);
    }

    /**
     * 输出HTML
     * 
     * @param response
     * @param sContent
     * @throws IOException
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-4	    lhg		新建
     * </pre>
     */
    public static void outputHTML(HttpServletResponse response, String sContent) throws IOException {
        outputHTML(response, sContent, DEFAULT_ENCODING);
    }

    /**
     * 输出文本
     * 
     * @param response
     * @param sContent
     * @throws IOException
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-4	    lhg		新建
     * </pre>
     */
    public static void outputText(HttpServletResponse response, String sContent) throws IOException {
        if (response == null || sContent == null) {
            throw new IllegalArgumentException("Null Argument");
        }
        PrintWriter out = response.getWriter();
        out.print(sContent);
    }
}
