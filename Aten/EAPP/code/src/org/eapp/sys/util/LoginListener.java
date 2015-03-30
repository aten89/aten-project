package org.eapp.sys.util;

import java.util.Calendar;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.eapp.blo.IActionLogBiz;
import org.eapp.blo.ILoginLogBiz;
import org.eapp.comobj.SessionAccount;
import org.eapp.sys.config.SysConstants;
import org.eapp.util.spring.SpringHelper;

/**
 * 用户登录日志监听器
 * @author Administrator
 * @version May 8, 2014
 */
public class LoginListener implements HttpSessionListener, HttpSessionAttributeListener, ServletContextListener {

	private ILoginLogBiz loginLogBiz;
	private IActionLogBiz actionLogBiz;
	
	private ILoginLogBiz getLoginLogBiz() {
		if (loginLogBiz == null) {
			loginLogBiz = (ILoginLogBiz)SpringHelper.getBean("loginLogBiz");
		}
		return loginLogBiz;
	}
	
	private IActionLogBiz getActionLogBiz() {
		if (actionLogBiz == null) {
			actionLogBiz = (IActionLogBiz)SpringHelper.getBean("actionLogBiz");
		}
		return actionLogBiz;
	}
	
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent eve) {
		//Session销毁时记录用户退出时间
		SessionAccount user = (SessionAccount)eve.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user != null) {
			getLoginLogBiz().txSetLogoutTime(eve.getSession().getId());
		}
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent eve) {
		//启用登录后绑定用户信息到Session时记录登录日志
		if (eve.getName().equals(SysConstants.SESSION_USER_KEY)) {
			SessionAccount user = (SessionAccount)eve.getValue();
			String loginInfo = null;
			if (user.isLock()) {
				loginInfo = "帐号已被锁定";
			} else if (user.getInvalidDate() != 0 && user.getInvalidDate() <= System.currentTimeMillis()) {
				loginInfo = "帐号已过期";
			} else if (user.getLoginIpLimit() != null && user.getLoginIpLimit().indexOf(user.getLoginIpAddr()) < 0) {
				loginInfo = "帐号登录IP受限制";
			}
			getLoginLogBiz().addLog(eve.getSession().getId(), user.getAccountID(), user.getDisplayName(), 
					user.getLoginIpAddr(), user.isVaild(), loginInfo);
		}
		
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent arg0) {
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent arg0) {
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		//更新所有当前在线用户的登出时间
		getLoginLogBiz().txSetAllLogoutTime();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//上次强制关闭系统时，更新上次半闭时在线用户的登出时间
		getLoginLogBiz().txSetAllLogoutTime();
		
		//检查当年备份表是否存在，不存在则创建
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		getActionLogBiz().addLogTable(year);
		getLoginLogBiz().addLogTable(year);
		//备份上次服务停止时所有未备份的日志
		getActionLogBiz().txStartUpBackUp();
		getLoginLogBiz().txStartUpBackUp();
		
	}

}
