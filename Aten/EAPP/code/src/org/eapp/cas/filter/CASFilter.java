/**
 * 
 */
package org.eapp.cas.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.blo.ISubSystemBiz;
import org.eapp.cas.AbstractCASFilter;
import org.eapp.comobj.SessionAccount;
import org.eapp.hbean.SubSystem;
import org.eapp.action.BaseAction.Message;
import org.eapp.rpc.session.ISessionAccountInfoManage;
import org.eapp.sys.config.SysConstants;
import org.eapp.sys.util.ServerStartupInit;
import org.eapp.sys.util.ServerStartupInit.SubSystemConfig;
import org.eapp.util.spring.SpringHelper;

/**
 * EAPP系统采用的过CAS过滤器
 * 
 * @author zsy
 * @version 
 */
public class CASFilter extends AbstractCASFilter {
	private ISessionAccountInfoManage sessionAccountInfoManage = (ISessionAccountInfoManage) SpringHelper.getBean("sessionAccountInfoBiz");

    @Override
	protected void initCasParamters() throws ServletException {
    	ServerStartupInit serverStartupInit = (ServerStartupInit) SpringHelper.getBean("serverStartupInit");
        if (casLogin == null) {
        	casLogin = serverStartupInit.getCasLoginUrl();
        }
        if (casValidate == null) {
        	casValidate = serverStartupInit.getCasValidateUrl();
        }
        if (casServerName == null) {
        	SubSystemConfig sysConf = serverStartupInit.getSubSystemConfig().get(SysConstants.EAPP_SUBSYSTEM_ID);
    		if (sysConf != null) {
    			casServerName = sysConf.getDomainName() + ":" + sysConf.getPort();
    		} else {
    			ISubSystemBiz subSystemBiz = (ISubSystemBiz) SpringHelper.getBean("subSystemBiz");
        		SubSystem sys = subSystemBiz.getSubSystem(SysConstants.EAPP_SUBSYSTEM_ID);
        		if (sys.getDomainName() == null) {
        			throw new IllegalArgumentException("找不到casServerName信息");
        		}
        		casServerName = sys.getDomainName() + ":" + sys.getPort();
    		}
//        	casServerName = serverStartupInit.getCasServerName();
//        	if (StringUtils.isBlank(casServerName)) {
//        		ISubSystemBiz subSystemBiz = (ISubSystemBiz) SpringHelper.getBean("subSystemBiz");
//        		SubSystem sys = subSystemBiz.getSubSystem(SysConstants.EAPP_SUBSYSTEM_ID);
//        		if (sys.getDomainName() == null) {
//        			throw new IllegalArgumentException("找不到casServerName信息");
//        		}
//        		casServerName = sys.getDomainName() + ":" + sys.getPort();
//        	}
        }
        
        //验证非空参数
        vailedCasParamters();
	}

    @Override
    protected String getSessionAccountBindKey() {
		return SysConstants.SESSION_USER_KEY;
	}
    
    @Override
	protected SessionAccount loadSessionAccount(String accountID, String ipAddr) throws Exception {
    	return sessionAccountInfoManage.csLoadSessionAccountInfo(accountID, ipAddr);
	}

	@Override
	protected SessionAccount reloadSessionAccount(SessionAccount account, String moduleKey) throws Exception {
		return sessionAccountInfoManage.csReloadSessionAccountInfo(account, moduleKey);
	}
	
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
