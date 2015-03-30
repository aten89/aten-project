/**
 * 
 */
package org.eapp.rpc.imp;

import java.util.List;

import org.eapp.blo.IModuleBiz;
import org.eapp.comobj.ModuleMenuTree;
import org.eapp.comobj.SessionAccount;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.hbean.Module;
import org.eapp.rpc.ISessionAccountService;
import org.eapp.rpc.session.ISessionAccountInfoManage;
import org.eapp.rpc.session.RPCAuthorizationFilter;
import org.eapp.sys.config.SysConstants;
import org.eapp.sys.util.ModuleTreeFacory;

import com.caucho.hessian.server.HessianServlet;

/**
 * 提供外系统权限认证的接口
 * @author zsy
 * @version 
 */
public class SessionAccountService extends HessianServlet implements ISessionAccountService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String MODULE_KEY = "user_right";
	
	private ISessionAccountInfoManage sessionAccountInfoBiz;
	private IModuleBiz moduleBiz;
	
	public void setSessionAccountInfoBiz(
			ISessionAccountInfoManage sessionAccountInfoBiz) {
		this.sessionAccountInfoBiz = sessionAccountInfoBiz;
	}

	public void setModuleBiz(IModuleBiz moduleBiz) {
		this.moduleBiz = moduleBiz;
	}

	@Override
	public SessionAccount getSessionAccount(String sessionID, String accountID, String ipAddr) throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID,MODULE_KEY, SysConstants.QUERY);
		if (accountID == null) {
			throw new IllegalArgumentException();
		}
		return sessionAccountInfoBiz.csLoadSessionAccountInfo(accountID, ipAddr);
	}
	
	@Override
	public SessionAccount reloadSessionAccount(String sessionID, SessionAccount account,
			String systemID, String moduleKey) throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID,MODULE_KEY, SysConstants.QUERY);
		if (account == null || systemID == null || moduleKey == null) {
			throw new IllegalArgumentException();
		}
		return sessionAccountInfoBiz.csReloadSessionAccountInfo(account, systemID, moduleKey);
	}


	@Override
	public String[] getActionRightStr(String sessionID, String systemID, String moduleKey,
			List<String> roleIDs) throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID,MODULE_KEY, SysConstants.QUERY);
		if (roleIDs == null || systemID == null || moduleKey == null) {
			throw new IllegalArgumentException();
		}
		return sessionAccountInfoBiz.getActions(systemID, moduleKey, roleIDs);
	}

	@Override
	public ModuleMenuTree getModuleMenuTree(String sessionID, String systemID, List<String> roleIDs) throws RpcAuthorizationException{
		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID,MODULE_KEY, SysConstants.QUERY);
		if (roleIDs == null || systemID == null) {
			throw new IllegalArgumentException();
		}
		List<Module> modules = moduleBiz.getHasRightModules(roleIDs, systemID);
		return ModuleTreeFacory.createModueMenuTree(modules);
	}
}
