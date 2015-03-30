/**
 * 
 */
package org.eapp.util.web.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 附件过滤器
 * 需配置在web.xml里
 * 过滤UploadConfig.rootFolder设置的路径，如/upload/*
 * @author 卓诗垚
 * @version May 8, 2007
 */
public class FileFilter extends HttpServlet implements Filter {

	/**
	 * 序列ID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 要制定的编码，
	 * 在web.xml中配置
	 */
	private String fileNameEncoding; 
	/**
	 * 要制定的编码，
	 * 在web.xml中配置
	 */
	private String fileNameToEncoding; 
	
	/**
	 * 过滤器初始化
	 * @param config 过滤器配置
	 * @exception ServletException Servlet异常
	 */
	public void init(FilterConfig config) throws ServletException {
		 //得到在web.xml中配置的编码
		fileNameEncoding = config.getInitParameter("fileNameEncoding");
		//得到在web.xml中配置的编码
		fileNameToEncoding = config.getInitParameter("fileNameToEncoding");
	}
	
	/**
	 * 重写父类实现
	 */
	public void destroy() {
		//销毁置空
		fileNameEncoding = null;
		fileNameToEncoding = null;
	}
	
	/**
	 * 重写父类实现
	 */
	public void doFilter(ServletRequest request, ServletResponse response, 
			FilterChain filterChain) throws IOException, ServletException {
		//转为HttpServletRequest
		HttpServletRequest httpReq = (HttpServletRequest) request;
		//转为HttpServletResponse
		HttpServletResponse httpResp = (HttpServletResponse) response;
		//链接防盗
		if (FileDispatcher.getConfig().isEnableRefererCheck()) {
			//如果启用
			//取得HTTP头部的referer属性
			String referer = httpReq.getHeader("referer");
			if (referer != null && !FileDispatcher.getConfig().isAccreditHost(referer)) {
				//输出页面不存在的错误
				httpResp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
		}
		
		//附件转向
		String reqStr = httpReq.getServletPath();
		//中文文件名编码转换
		if (fileNameEncoding != null && fileNameToEncoding != null) {
			reqStr = new String(reqStr.getBytes(fileNameEncoding), fileNameToEncoding);
		}
		FileInputStream imgStream = null;
		try {
			File file = FileDispatcher.findFile(reqStr);
			if (file != null) {
				imgStream = new FileInputStream(file);
				 //得到文件大小
				int i = imgStream.available();
				try {
					String type = reqStr.substring(reqStr.lastIndexOf('.'));
					if(FileDispatcher.getConfig().isImgExt(type)) {
						//设置文件类型
						httpResp.setContentType("image/" + type.substring(1).toLowerCase());
					} else if (".html.htm".indexOf(type.toLowerCase()) >= 0) {
						//设置HTML类型
						httpResp.setContentType("text/html");
					} else {
						//其它情部分设为附件类型
						httpResp.setContentType("application/*");
						//默认附件(attachment)下载(TXT默认是内联(inline))
						
						String filename = httpReq.getParameter("filename");
						String headerInfo = "attachment";
						if (filename != null && !filename.trim().equals("")) {
							//filename中不能有中文，IE使用两次encodeURI，
							//如：'../upload/attachment/123.doc?filename=' + encodeURI(encodeURI("中文.doc"))
							//FIREFOX使用“MIME + Base64” 或 “MIME + Quoted-printable”编码 ，
							//如：'../upload/attachment/123.doc?filename=' + encodeURIComponent('=?GBK?B?1tDOxA==?=')
							headerInfo += ";filename=" + filename;
						}
						httpResp.setHeader("Content-disposition", headerInfo);
					}
				} catch(Exception e) {
					httpResp.setContentType("application/*");
				}
				//设置文件大小
				httpResp.setContentLength(i);
				//得到输出流Servlet流不用关闭
				OutputStream toClientStream = httpResp.getOutputStream();
				byte data[] = new byte[1024];
				//逐步输出数据
				while( (i = imgStream.read(data)) > 0){
					toClientStream.write(data,0,i);
				}
				toClientStream.flush();
			} else {
				filterChain.doFilter(httpReq, httpResp);
			}
//		} catch(IOException e) {
//			throw e;
		} finally {
			if (imgStream != null) {
				imgStream.close();
			}
		}
	}
}