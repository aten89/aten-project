/**
 * 
 */
package org.eapp.client.cas.filter;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.cas.AbstractCASFilter;
import org.eapp.client.hessian.SessionAccountService;
import org.eapp.client.hessian.SubSystemService;
import org.eapp.client.util.SystemProperties;
import org.eapp.comobj.SessionAccount;
import org.eapp.rpc.dto.SubSystemInfo;

/**
 * 子系统采用的过CAS过滤器
 * 
 * @author zsy
 * @version 
 */
public abstract class SubSystemCASFilter extends AbstractCASFilter {
	private static Log log = LogFactory.getLog(SubSystemCASFilter.class);
	private SessionAccountService sessionAccountService = new SessionAccountService();

    @Override
	protected void initCasParamters() throws ServletException {
    	//从EAPP系统中获取子系统信息与加载字典信息需要访问EAPP系统
		//当EAPP系统与子系统部署在同一WEB服务器下时，有时候EAPP系统会晚于子系统启动，
		//这样就是导致服务死锁（子系统访问EAPP服务，而EAPP还示启运完成）
		//所以需要在线程是访问服务
    	Thread thread = new Thread() {
			public void run() {
		    	try {
			        SubSystemService sysSer = new SubSystemService();
			        SubSystemInfo cfg = sysSer.getSubSystemInfo(SystemProperties.SYSTEM_ID);
			        if (casLogin == null) {
			        	casLogin = cfg.getCasLoginUrl();
			        }
			        if (casValidate == null) {
			        	casValidate = cfg.getCasValidateUrl();
			        }
			        if (casServerName == null) {
			        	casServerName = cfg.getDomainName() + ":" + cfg.getPort();
			        }
			        //验证非空参数
			        vailedCasParamters();
		        } catch(Exception e) {
		        	log.error("初始化CASFilter参数失败", e);
		        }
			}
		};
		thread.start();
	}

    @Override
    protected String getSessionAccountBindKey() {
		return SystemProperties.SESSION_USER_KEY;
	}
    
    @Override
	protected SessionAccount loadSessionAccount(String accountID, String ipAddr) throws Exception {
    	return sessionAccountService.getSessionAccount(accountID, ipAddr);
	}

	@Override
	protected SessionAccount reloadSessionAccount(SessionAccount account, String moduleKey) throws Exception {
		return sessionAccountService.reloadSessionAccount(account, SystemProperties.SYSTEM_ID, moduleKey);
	}
}
