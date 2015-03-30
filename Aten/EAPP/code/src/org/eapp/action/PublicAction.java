package org.eapp.action;

import org.eapp.sys.util.ServerStartupInit;


/**
 * 公开访问的方法
 * @author zsy
 * @version Dec 3, 2008
 */
public class PublicAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 894932421380316916L;
	
	private ServerStartupInit serverStartupInit;
	
	private String casLogoutUrl;
	
	public void setServerStartupInit(ServerStartupInit serverStartupInit) {
		this.serverStartupInit = serverStartupInit;
	}

	public String getCasLogoutUrl() {
		return casLogoutUrl;
	}

	/**
	 * 退出系统，注销session
	 * @return
	 */
	public String logoutCAS() {
		String basePath = getRequest().getScheme() + "://"
			+ getRequest().getServerName() + ":"
			+ getRequest().getServerPort() + getRequest().getContextPath();
		getSession().invalidate();
		if (serverStartupInit.getCasLogoutUrl() != null) {
			casLogoutUrl = serverStartupInit.getCasLogoutUrl() + "?url=" + basePath + "/l/frame/index";
		}
		return success();
	}
}
