/**
 * 
 */
package org.eapp.client.hessian;

import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.util.SystemProperties;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.IFlowConfigService;
import org.eapp.rpc.dto.FlowConfigInfo;

/**
 * @author zsy
 *
 */
public class FlowConfigService extends BaseEAPPService {
	private static Log log = LogFactory.getLog(FlowConfigService.class);
	
	private static IFlowConfigService service;
	
	private IFlowConfigService getService() throws MalformedURLException{
		if (service == null) {
			synchronized (FlowConfigService.class) {
				service = (IFlowConfigService) factory.create(IFlowConfigService.class, getServiceUrl(SystemProperties.SERVICE_FLOW_CONFIG));
			}
		}
		return service;
	}
	
	/**
	 * 通过流程分类取得已发布的流程信息
	 * @param flowClass
	 * @return
	 * @throws RpcAuthorizationException
	 * @throws MalformedURLException
	 */
	public List<FlowConfigInfo> getFlowConfigs(String flowClass) throws RpcAuthorizationException, MalformedURLException {
		try {
			return getService().getFlowConfigs(getDefaultSessionID(false), flowClass);
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				return getService().getFlowConfigs(getDefaultSessionID(true), flowClass);
			} else {
				throw e;
			}
		}
	}
	
	/**
	 * 通过flowKey取得流程信息
	 * @param flowKey
	 * @return
	 * @throws RpcAuthorizationException
	 * @throws MalformedURLException
	 */
	public FlowConfigInfo getFlowConfig(String flowKey) throws RpcAuthorizationException, MalformedURLException {
		try {
			return getService().getFlowConfig(getDefaultSessionID(false), flowKey);
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				return getService().getFlowConfig(getDefaultSessionID(true), flowKey);
			} else {
				throw e;
			}
		}
	}
}
