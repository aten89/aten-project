package org.eapp.util.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharacterEncodingFilter implements Filter {

	protected String requestEncoding;/////要制定的编码，在web.xml中配置
	protected String responseEncoding;/////要制定的编码，在web.xml中配置
	
	public void init(FilterConfig config) throws ServletException {
		requestEncoding = config.getInitParameter("requestEncoding");///得到在web.xml中配置的编码
		responseEncoding = config.getInitParameter("responseEncoding");///得到在web.xml中配置的编码
	}
	
	public void destroy() {
		requestEncoding = null;
		responseEncoding = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (requestEncoding != null) {
			request.setCharacterEncoding(requestEncoding);
		}
		if (responseEncoding != null) {
			response.setCharacterEncoding(responseEncoding);
		}
		chain.doFilter(request, response);//执行下一个filter
	}
}
