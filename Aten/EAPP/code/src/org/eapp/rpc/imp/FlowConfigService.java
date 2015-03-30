/**
 * 
 */
package org.eapp.rpc.imp;

import java.util.ArrayList;
import java.util.List;

import org.eapp.blo.IFlowConfigBiz;
import org.eapp.rpc.dto.FlowConfigInfo;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.hbean.FlowConfig;
import org.eapp.rpc.IFlowConfigService;
import org.eapp.rpc.session.RPCAuthorizationFilter;
import org.eapp.sys.config.SysConstants;

import com.caucho.hessian.server.HessianServlet;

/**
 * @version
 */
public class FlowConfigService extends HessianServlet implements IFlowConfigService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1312864028619931972L;
	private static final String MODULE_KEY = "flow_pub";
	private IFlowConfigBiz flowConfigBiz;

	public void setFlowConfigBiz(IFlowConfigBiz flowConfigBiz) {
		this.flowConfigBiz = flowConfigBiz;
	}

	@Override
	public List<FlowConfigInfo> getFlowConfigs(String sessionID, String flowClass) 
			throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.QUERY);
		List<FlowConfig> flowConfigs = flowConfigBiz.getFlowsByFlowClass(flowClass, FlowConfig.FLOW_FLAG_PUBLISHED);
		
		List<FlowConfigInfo> flowConfigInfos = new ArrayList<FlowConfigInfo>();
		for(FlowConfig flowConfig : flowConfigs){
			flowConfigInfos.add(copy(flowConfig));
		}
		return flowConfigInfos;
	}

	@Override
	public FlowConfigInfo getFlowConfig(String sessionID, String flowKey) throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID,MODULE_KEY, SysConstants.QUERY);
		FlowConfig flowConfig = flowConfigBiz.getFlowByFlowKey(flowKey, FlowConfig.FLOW_FLAG_PUBLISHED);
		return copy(flowConfig);
	}
	
	/**
	 * 复制
	 * @param fc
	 * @return
	 */
	private FlowConfigInfo copy(FlowConfig fc) {
		FlowConfigInfo fci = new FlowConfigInfo();
        fci.setFlowClass(fc.getFlowClass());
        fci.setFlowKey(fc.getFlowKey());
        fci.setFlowName(fc.getFlowName());
        fci.setFlowVersion(fc.getFlowVersion());
        return fci;
    }
}
