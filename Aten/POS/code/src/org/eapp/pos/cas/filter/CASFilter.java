/**
 * 
 */
package org.eapp.pos.cas.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.client.cas.filter.SubSystemCASFilter;
import org.eapp.pos.action.BaseAction.Message;


/**
 * 子系统采用的过CAS过滤器（支持Struts2+JSON框架）
 * @author zsy
 *
 */
public class CASFilter  extends SubSystemCASFilter {

    /**
     * 通过访问的URL提取得模块KEY
     * @param request
     * @return
     */
	@Override
    public String getModuleKey(HttpServletRequest request) {
    	String requestURL = request.getRequestURL().toString();
		int start = requestURL.indexOf("/m/");
		if (start < 0) {
			 return null;
		}
		int end = requestURL.indexOf("/", start + 3);
		if (end > start + 2) {
			return requestURL.substring(start + 3, end);
		} else {
			return requestURL.substring(start + 3);
		}
    }
    
    /**
     * 通过访问的URL提取得动作KEY
     * @param request
     * @return
     */
	@Override
    public String getActionKey(HttpServletRequest request) {
		String requestURL = request.getRequestURL().toString();
		int start = requestURL.lastIndexOf("/");
		if (start < 0) {
			 return null;
		}
		String actionKey = requestURL.substring(start + 1);
		start = actionKey.indexOf("_");
		if (start == -1) {
			start = actionKey.indexOf(".");
		}
		if (start > 0) {
			actionKey = actionKey.substring(0, start);
		}
		return actionKey;
    }
    /**
     * 发送错误
     * @param response
     * @param code
     * @param text
     * @throws IOException
     */
	@Override
    protected void sendError(HttpServletResponse response, int code, String text) throws IOException {
		/**
    	 * 输出格式如
    	 * {"msg":{"code":1,"text":"操作成功"}}
    	 */
		StringBuffer json = new StringBuffer();
		json.append("{\"msg\":{\"code\":")
				.append(code)
				.append(",\"text\":\"")
				.append(text)
				.append("\"}}");
	
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().print(json.toString());
//	    Servlet流不需要半闭
    }
	
    /**
     * 发送错误
     * @param request
     * @param response
     * @param text
     * @throws IOException
     * @throws ServletException
     */
	@Override
    protected void sendError(HttpServletRequest request,HttpServletResponse response, String text) throws IOException, ServletException {
    	request.setAttribute("msg", new Message(text, 0));
		request.getRequestDispatcher("/page/error.jsp").forward(request, response);
    }
}
