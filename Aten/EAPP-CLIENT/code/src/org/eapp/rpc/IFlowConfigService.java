/**
 * 
 */
package org.eapp.rpc;

import java.util.List;

import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.dto.FlowConfigInfo;

/**
 * @author zsy
 *
 */
public interface IFlowConfigService {

	/**
	 * 通过流程分类取得已发布的流程信息
	 * @param sessionID
	 * @param flowClass
	 * @return
	 */
	public List<FlowConfigInfo> getFlowConfigs(String sessionID, String flowClass) throws RpcAuthorizationException;
	
	/**
	 * 通过flowKey取得流程信息
	 * @param sessionID
	 * @param flowKey
	 * @return
	 */
	public FlowConfigInfo getFlowConfig(String sessionID, String flowKey) throws RpcAuthorizationException;
}
