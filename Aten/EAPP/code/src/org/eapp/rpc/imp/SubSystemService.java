/**
 * 
 */
package org.eapp.rpc.imp;

import org.eapp.blo.ISubSystemBiz;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.hbean.SubSystem;
import org.eapp.rpc.ISubSystemService;
import org.eapp.rpc.dto.SubSystemInfo;
import org.eapp.rpc.session.RPCAuthorizationFilter;
import org.eapp.sys.config.SysConstants;
import org.eapp.sys.util.ServerStartupInit;
import org.eapp.sys.util.ServerStartupInit.SubSystemConfig;

import com.caucho.hessian.server.HessianServlet;

/**
 * 子系统服务的Hessian服务
 * @author zsy
 * @version 
 */
public class SubSystemService extends HessianServlet implements
		ISubSystemService {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2475894087634367100L;
	private static final String MODULE_KEY = "subsystem";
	private ISubSystemBiz subSystemBiz;
	private ServerStartupInit serverStartupInit;

	public void setSubSystemBiz(ISubSystemBiz subSystemBiz) {
		this.subSystemBiz = subSystemBiz;
	}

	public void setServerStartupInit(ServerStartupInit serverStartupInit) {
		this.serverStartupInit = serverStartupInit;
	}

	@Override
	public SubSystemInfo getEAPPSystemInfo(String sessionID) throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.VIEW);
//		InterfaceLogger.log(sessionID, MODULE_KEY, SysConstants.QUERY, null,"方法:getSubSystemConfig 参数:sessionID="+sessionID);
		SubSystem sys = subSystemBiz.getSubSystem(SysConstants.EAPP_SUBSYSTEM_ID);
		if (sys == null) {
			return null;
		}
		SubSystemInfo sysCfg = copy(sys);
		
//		String casServerName = serverStartupInit.getCasServerName();
//		if (StringUtils.isNotBlank(casServerName)) {
//			//是EAPP系统时，域名与IP以配置的为准
//			String[] strArr = casServerName.split(":");
//			sysCfg.setDomainName(strArr[0]);
//			if (strArr.length > 1) {
//				sysCfg.setPort(Integer.valueOf(strArr[1]));
//			}
//		}
		return sysCfg;
	}
	
	@Override
	public SubSystemInfo getSubSystemInfo(String sessionID,
			String subSystemID) throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID,MODULE_KEY, SysConstants.VIEW);
//		InterfaceLogger.log(sessionID, MODULE_KEY, SysConstants.QUERY, null,"方法:getSubSystemConfig 参数:sessionID="+sessionID);
		if (subSystemID == null) {
			return null;
		}
		SubSystem sys = subSystemBiz.getSubSystem(subSystemID);
		if (sys == null) {
			return null;
		}
		return copy(sys);
	}
	
	/**
	 * 复制
	 * @param sys
	 * @return
	 */
	private SubSystemInfo copy(SubSystem sys) {
		SubSystemInfo to = new SubSystemInfo();
		to.setSubSystemID(sys.getSubSystemID());
		to.setName(sys.getName());
		to.setServerName(sys.getServerName());
		to.setIpAddress(sys.getIpAddress());
		to.setDomainName(sys.getDomainName());
		to.setPort(sys.getPort());

		//CAS配置信息
		to.setCasLoginUrl(serverStartupInit.getCasLoginUrl());
		to.setCasLogoutUrl(serverStartupInit.getCasLogoutUrl());
		to.setCasValidateUrl(serverStartupInit.getCasValidateUrl());
		
		//子系统中配置信息覆盖
		SubSystemConfig sysConf = serverStartupInit.getSubSystemConfig().get(sys.getSubSystemID());
		if (sysConf != null) {
			to.setDomainName(sysConf.getDomainName());
			to.setPort(sysConf.getPort());
		}
		return to;
	}
	
}
