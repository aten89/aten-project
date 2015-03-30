package org.eapp.oa.system.ctrl;

import java.io.IOException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.client.hessian.SubSystemService;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.rpc.dto.SubSystemInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * @author zsy
 */
public class ServletListener extends HttpServlet implements
		ServletContextListener {
	 /**
     * 日志
     */
	private static Logger log = LoggerFactory.getLogger(ServletListener.class);
	/**
	 * <code>serialVersionUID</code> 的注释
	 */
	private static final long serialVersionUID = 2849931988267060670L;
	
//	private static Logger log = LoggerFactory.getLogger(ServletListener.class);
	
	@Override
	public void contextInitialized(final ServletContextEvent event) {
		//从EAPP系统中获取子系统信息与加载字典信息需要访问EAPP系统
		//当EAPP系统与子系统部署在同一WEB服务器下时，有时候EAPP系统会晚于子系统启动，
		//这样就是导致服务死锁（子系统访问EAPP服务，而EAPP还示启运完成）
		//所以需要在线程是访问服务
		Thread thread = new Thread() {
			public void run() {
				try {
					SubSystemService sysSer = new SubSystemService();
					SubSystemInfo eappInfo = sysSer.getEAPPSystemInfo();
					StringBuffer eappPath = new StringBuffer();
					eappPath.append("http://")
							.append(eappInfo.getDomainName())
							.append(eappInfo.getPort() == null ? "" : ":" + eappInfo.getPort())
							.append(eappInfo.getServerName() == null ? "" : "/" + eappInfo.getServerName())
							.append("/");
					event.getServletContext().setAttribute("ermpPath", eappPath.toString());
					
					log.info("初始化数据字典表");
					SysCodeDictLoader.getInstance().initAllCodeDict();
				} catch(Exception e) {
					log.error("Servlit listener 初始化失败", e);
				}
			}
		};
		thread.start();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
		
		
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("act");
		if(SysConstants.REDEPLOY.equals(action)) {
			if (SysCodeDictLoader.getInstance().initAllCodeDict()) {	
				XMLResponse.outputStandardResponse(response, 1, "热加载成功");
			} else {
				XMLResponse.outputStandardResponse(response, 0, "热加载失败");
			}
		}
	}

	
	
}
