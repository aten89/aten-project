/**
 * 基于cas-client-java-2.1.1修改
 */
package org.eapp.cas;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.comobj.SessionAccount;
import org.eapp.exception.EappException;

/**
 * 基于cas-client-java-2.1.1修改
 * 
 * @author zsy
 * @version 
 */
public abstract class AbstractCASFilter implements Filter {
	private static Log log = LogFactory.getLog(AbstractCASFilter.class);

	private final static String LOGIN_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.loginUrl";
	private final static String VALIDATE_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.validateUrl";
	private final static String SERVICE_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.serviceUrl";
	private final static String SERVERNAME_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.serverName";
	private final static String RENEW_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.renew";
	private final static String AUTHORIZED_PROXY_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.authorizedProxy";
	private final static String PROXY_CALLBACK_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.proxyCallbackUrl";
	private final static String GATEWAY_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.gateway";
	
	private static final String CAS_FILTER_GATEWAYED = "edu.yale.its.tp.cas.client.filter.didGateway";
	
	//用于记录在REQUEST中，当前请求的moduleKey值
	public static final String CURRENT_REQUEST_MODULEKEY = "edu.yale.its.tp.cas.client.filter.currentRequestModuleKey";
	
	
	protected String casLogin;
	protected String casValidate;
	protected String casServiceUrl;
	protected String casServerName;
	protected String casProxyCallbackUrl;
	protected boolean casRenew;
	protected boolean casGateway = false;
	protected List<String> authorizedProxies = new ArrayList<String>();

    
    // Initialization 
    public void init(FilterConfig config) throws ServletException {
        casLogin = config.getInitParameter(LOGIN_INIT_PARAM);
        casValidate = config.getInitParameter(VALIDATE_INIT_PARAM);
        casServiceUrl = config.getInitParameter(SERVICE_INIT_PARAM);
        String casAuthorizedProxy = config.getInitParameter(AUTHORIZED_PROXY_INIT_PARAM);
        casRenew = Boolean.valueOf(config.getInitParameter(RENEW_INIT_PARAM)).booleanValue();
        casServerName = config.getInitParameter(SERVERNAME_INIT_PARAM);
        casProxyCallbackUrl = config.getInitParameter(PROXY_CALLBACK_INIT_PARAM);
        casGateway = Boolean.valueOf(config.getInitParameter(GATEWAY_INIT_PARAM)).booleanValue();
        
        if (casAuthorizedProxy != null){
            // parse and remember authorized proxies
            StringTokenizer casProxies = new StringTokenizer(casAuthorizedProxy);
            while (casProxies.hasMoreTokens()) {
                String anAuthorizedProxy = casProxies.nextToken();
//                if (!anAuthorizedProxy.startsWith("https://")) {
//                    throw new ServletException("CASFilter initialization parameter for authorized proxies " +
//                            "must be a whitespace delimited list of authorized proxies.  " +
//                            "Authorized proxies must be secure (https) addresses.  This one wasn't: [" + anAuthorizedProxy + "]");
//                }
                this.authorizedProxies.add(anAuthorizedProxy);
            }
        }
        
        initCasParamters();
    }
    
 // Filter processing
    public void doFilter(ServletRequest req, ServletResponse res, 
    		FilterChain fc) throws ServletException, IOException {

        // make sure we've got an HTTP request
    	HttpServletRequest request = (HttpServletRequest)req;
    	HttpServletResponse response = (HttpServletResponse)res;

    	// Is this a request for the proxy callback listener?  If so, pass
        // it through
        if (casProxyCallbackUrl != null 
        		&& casProxyCallbackUrl.endsWith(request.getRequestURI())
        		&& request.getParameter("pgtId") != null
        		&& request.getParameter("pgtIou") != null) {
        	log.trace("passing through what we hope is CAS's request for proxy ticket receptor.");
            fc.doFilter(request, response);
            return;
        }
        
        HttpSession session = request.getSession();

        // if our attribute's already present, don't do anything
    	SessionAccount user = (SessionAccount)session.getAttribute(getSessionAccountBindKey());
    	
    	if (user != null) {//用户不为空不需要到CAS验证
    		String moduleKey = getModuleKey(request);
    		if (user.isVaild() && moduleKey != null) {//用户有效
    			//重载用户缓存信息
        		try {
					user = reloadSessionAccount(user, moduleKey);
					//注意：通过Hession调用后user已经重新构建一个，不是原来那个了，所以需要替换session中的
					session.setAttribute(getSessionAccountBindKey(), user);
				} catch (Exception e) {
					log.error("重载用户缓存信息失败", e);
					throw new ServletException(e);
				}
    		} 
    		if(!user.isVaild()) {//用户失效
    			user.clear();
    			errorHandler(request, response, "您的帐号已过期或被锁定或IP登录限制");
    			return;
    		}
    		
    		if (moduleKey != null) {//权限判断
    			String actionKey = getActionKey(request);
    			if(!user.hasRight(moduleKey, actionKey)) {//没权限
    				errorHandler(request, response, "您没有操作权限");
        			return;
    			}
    			
    			//权限验证通过后，将当前moduleKey绑定到request中，并转到请求页面
                request.setAttribute(CURRENT_REQUEST_MODULEKEY, moduleKey);
    		}
    		
            fc.doFilter(req, res);
            return;
    	}
        

        // otherwise, we need to authenticate via CAS
        String ticket = request.getParameter("ticket");

        // no ticket?  abort request processing and redirect
        if (ticket == null || ticket.equals("")) {
        	// did we go through the gateway already?
            boolean didGateway = Boolean.valueOf((String) session.getAttribute(CAS_FILTER_GATEWAYED)).booleanValue();
            if (!didGateway) {
            	session.setAttribute(CAS_FILTER_GATEWAYED, "true");
            	redirectToCAS(request, response);
            	// abort chain
            	return;
            } else {
            	// if we should be logged in, make sure validation succeeded
            	if (casGateway || user != null) {
            		// continue processing the request
            		fc.doFilter(request, response);
            		return;
            	} else {
            		// unknown state... redirect to CAS
            		session.setAttribute(CAS_FILTER_GATEWAYED, "true");
            		redirectToCAS(request, response);
            		// abort chain
            		return;
            	}
            }
        }

        // Yay, ticket!  Validate it.
        String accountID = null;
		try {
			accountID = getAuthenticatedUser(request);
		} catch (EappException e) {
			// session超时，且ticket验证失败时，重新验证
			log.warn("session超时，重新登录：" + e.getMessage());
			redirectToCAS(request, response);
			return;
		}
        if (accountID == null) {
            throw new ServletException("Unexpected CAS authentication error");
        }
        try {
        	user = loadSessionAccount(accountID, request.getRemoteAddr());
		} catch (Exception e) {
			log.error("加载用户缓存信息失败", e);
			throw new ServletException(e);
		}
        // Store the authenticated user in the session
        if (session != null) {// probably unncessary
            session.setAttribute(getSessionAccountBindKey(), user);
         // don't store extra unnecessary session state
            session.removeAttribute(CAS_FILTER_GATEWAYED);
        }
        
        //帐号有效性
        if (user == null || !user.isVaild()) {
        	errorHandler(request, response, "您的帐号已过期或被锁定或IP登录限制");
        	return;
        }
        
        //权限判断
        String moduleKey = getModuleKey(request);
        if (moduleKey != null) {//权限判断
			String actionKey = getActionKey(request);
			if(!user.hasRight(moduleKey, actionKey)) {//没权限
				errorHandler(request, response, "您没有操作权限");
    			return;
			}
			
			//权限验证通过后，将当前moduleKey绑定到request中，并转到请求页面
	        request.setAttribute(CURRENT_REQUEST_MODULEKEY, moduleKey);
		}
        
        // continue processing the request
        fc.doFilter(request, response);
    }

    // Destruction

    public void destroy() {
    	
    }

    // Utility methods

    /**
     * Converts a ticket parameter to a username, taking into account an
     * optionally configured trusted proxy in the tier immediately in front
     * of us.
     * @throws MatrixException 
     */
	private String getAuthenticatedUser(HttpServletRequest request) throws ServletException, EappException {
        ProxyTicketValidator pv = new ProxyTicketValidator();
        pv.setCasValidateUrl(casValidate);
        pv.setServiceTicket(request.getParameter("ticket"));
        pv.setService(getService(request));
        pv.setRenew(casRenew);
        if (casProxyCallbackUrl != null) {
            pv.setProxyCallbackUrl(casProxyCallbackUrl);
        }
        
        if (!pv.isAuthenticationSuccesful()) {
            try {
                pv.validate();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new EappException("Unable to validate ProxyTicketValidator [" + pv + "]");
            }
        }
        
        if (!pv.isAuthenticationSuccesful()) {
        	throw new EappException("Unable to validate ProxyTicketValidator [" + pv + "]");
        }
        
        
        if (pv.getUser() == null 
        		|| pv.getProxyList() == null 
        		|| (casRenew && !pv.getProxyList().isEmpty())) {
        	throw new EappException("Validation of [" + pv + "] did not result in an internally consistent CASReceipt.");
        }
        
        if (!pv.getProxyList().isEmpty()) {
            // ticket was proxied
            if (authorizedProxies.isEmpty()) {
                throw new ServletException("this page does not accept proxied tickets");
            }
           
            String proxy = (String)pv.getProxyList().get(0);
            if (!authorizedProxies.contains(proxy)) {
            	throw new ServletException("unauthorized top-level proxy: '" + pv.getProxyList().get(0) + "'");
            }
        }
        
        return pv.getUser();
    }

    /**
     * Returns either the configured service or figures it out for the current
     * request.  The returned service is URL-encoded.
     */
	private String getService(HttpServletRequest request) throws ServletException {
		String serviceString = null;
		// use the given string if it's provided
        if (casServiceUrl != null) {
            try {
            	serviceString = URLEncoder.encode(casServiceUrl, "utf-8");
        	} catch (UnsupportedEncodingException e) {
        	    log.error(e.getMessage(), e);
        	}
        } else {
            // otherwise, return our best guess at the service
        	serviceString = getService(request, casServerName);
        }
        return serviceString;
    }

    /**
     * 转到CAS服务器登录
     * @param response
     * @param serviceUrl
     * @throws ServletException
     * @throws IOException
     */
    private void redirectToCAS(HttpServletRequest request, HttpServletResponse response) 
    		throws ServletException, IOException {
    	if (!isRedirectRequest(request)) {
    		sendError(response, -1, "会话超时,请重新登录");
        } else {
        	String casLoginString = casLogin + "?service="
                    + getService((HttpServletRequest) request)
                    + ((casRenew) ? "&renew=true" : "")
                    + (casGateway ? "&gateway=true" : "");
	        response.sendRedirect(casLoginString);
        }
    }
    
    /**
     * Returns a service ID (URL) as a composite of the preconfigured server
     * name and the runtime request, removing the request parameter "ticket".
     */
    public static String getService(HttpServletRequest request, String server) throws ServletException {
    	// ensure we have a server name
    	if (server == null) {
    		throw new IllegalArgumentException("name of server is required");
    	}

    	// now, construct our best guess at the string
    	StringBuffer sb = new StringBuffer();
    	if (request.isSecure()) {
    		sb.append("https://");
    	} else {
    		sb.append("http://");
    	}
    	sb.append(server);
    	sb.append(request.getRequestURI());

    	if (request.getQueryString() != null) {
    		// first, see whether we've got a 'ticket' at all
    		int ticketLoc = request.getQueryString().indexOf("ticket=");

    		// if ticketLoc == 0, then it's the only parameter and we ignore
    		// the whole query string

    		// if no ticket is present, we use the query string wholesale
    		if (ticketLoc == -1) {
    			sb.append("?" + request.getQueryString());
    		} else if (ticketLoc > 0) {
    			ticketLoc = request.getQueryString().indexOf("&ticket=");
    			if (ticketLoc == -1) {
    				// there was a 'ticket=' unrelated to a parameter named 'ticket'
    				sb.append("?" + request.getQueryString());
    			} else if (ticketLoc > 0) {
    				// otherwise, we use the query string up to "&ticket="
    				sb.append("?" + request.getQueryString().substring(0, ticketLoc));
    			}
    		}
    	}
		try {
			return URLEncoder.encode(sb.toString(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
			throw new IllegalArgumentException("name of server is required");
		}
    }
    
    /**
     * 错误处理
     * @param request
     * @param response
     * @param text
     * @throws IOException
     * @throws ServletException
     */
    private void errorHandler(HttpServletRequest request, HttpServletResponse response, String text) throws IOException, ServletException {
    	if (!isRedirectRequest(request)) {
			sendError(response, 0, text);
        } else {
        	sendError(request, response, text);
        }
    }
    
    /**
     * 是转向请求还是异步请求
     * @param request
     * @return
     */
    private boolean isRedirectRequest(HttpServletRequest request) {
    	String redirect = request.getParameter("redirect");//是否重定向到错误页面
    	if (redirect != null && !redirect.trim().equals("")) {
    		return "Y".equalsIgnoreCase(redirect) || "true".equalsIgnoreCase(redirect);
    	} else {
    		//参数为空，则GET请求为转向，其它为XML信息
	    	String requestMethod = request.getMethod();
	    	if ("GET".equalsIgnoreCase(requestMethod)) {
	    		return true;
	    	}
    	}
    	return false;
    }

    /**
     * 从外部加载CAS参数
     * @throws ServletException
     */
    protected void initCasParamters() throws ServletException {
    	vailedCasParamters();
    }
    
    /**
     * 验证CAS参数
     * @throws ServletException
     */
    protected void vailedCasParamters() throws ServletException {
        if (casGateway && casRenew) {
            throw new ServletException("gateway and renew cannot both be true in filter configuration");
        }
        
        if (casLogin == null) {
        	throw new ServletException("casLogin must be set.");
        }
        
        if (casServerName != null && casServiceUrl != null) {
            throw new ServletException("serverName and serviceUrl cannot both be set: choose one.");
        }
        if (casServerName == null && casServiceUrl == null) {
            throw new ServletException("one of serverName or serviceUrl must be set.");
        }
        if (casServiceUrl != null){
            if (! (casServiceUrl.startsWith("https://") || (casServiceUrl.startsWith("http://") ))){
                throw new ServletException("service URL must start with http:// or https://; its current value is [" + casServiceUrl + "]");
            }
        }
        
        if (casValidate == null){
            throw new ServletException("validateUrl parameter must be set.");
        }
    }
    
    /**
     * 获取SessionAccount对象绑定在Session中Key值
     * @return
     */
    protected abstract String getSessionAccountBindKey();
    /**
     * 加载SessionAccount对象
     * @param accountID
     * @return SessionAccount对象
     */
    protected abstract SessionAccount loadSessionAccount(String accountID, String ipAddr) throws Exception;
    
    /**
     * 重新加载SessionAccount对象
     * @param account 要重载的SessionAccount对象
     * @param moduleKey 要重载的模块
     * @return SessionAccount对象
     */
    protected abstract SessionAccount reloadSessionAccount(SessionAccount account, String moduleKey) throws Exception;
    
    /**
     * 发送错误
     * @param response
     * @param code
     * @param text
     * @throws IOException
     */
    protected abstract void sendError(HttpServletResponse response, int code, String text) throws IOException;
    
    /**
     * 发送错误
     * @param request
     * @param response
     * @param text
     * @throws IOException
     * @throws ServletException
     */
    protected abstract void sendError(HttpServletRequest request,HttpServletResponse response, String text) throws IOException, ServletException;
    
    /**
     * 通过访问的URL提取得模块KEY
     * @param request
     * @return
     */
    public abstract String getModuleKey(HttpServletRequest request);
    
    /**
     * 通过访问的URL提取得动作KEY
     * @param request
     * @return
     */
    public abstract String getActionKey(HttpServletRequest request);
}
