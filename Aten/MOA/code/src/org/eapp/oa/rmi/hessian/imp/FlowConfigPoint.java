/**
 * 
 */
package org.eapp.oa.rmi.hessian.imp;

import java.util.ArrayList;
import java.util.List;

import org.eapp.oa.flow.blo.IFlowConfigBiz;
import org.eapp.oa.flow.hbean.FlowConfig;
import org.eapp.oa.rmi.hessian.FlowInfo;
import org.eapp.oa.rmi.hessian.IFlowConfigPoint;

import com.caucho.hessian.server.HessianServlet;

/**
 * @author zsy
 *
 */
public class FlowConfigPoint extends HessianServlet implements IFlowConfigPoint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5361249587937323044L;

	private IFlowConfigBiz flowConfigBiz;
	
	public void setFlowConfigBiz(IFlowConfigBiz flowConfigBiz) {
		this.flowConfigBiz = flowConfigBiz;
	}

	@Override
	public List<FlowInfo> getFlowInfos(String flowClass) {
		List<FlowConfig> fcs = flowConfigBiz.getFlowsByFlowClass(flowClass);
		if (fcs == null || fcs.isEmpty()) {
			return null;
		}
		List<FlowInfo> fis = new ArrayList<FlowInfo>(fcs.size());
		for (FlowConfig fc : fcs) {
			fis.add(copy(fc));
		}
		return fis;
	}

	@Override
	public FlowInfo getFlowInfo(String flowKey) {
		FlowConfig fc = flowConfigBiz.getFlowByFlowKey(flowKey, FlowConfig.FLOW_FLAG_PUBLISHED);
		if (fc == null) {
			return null;
		}
		return copy(fc);
	}

	private FlowInfo copy(FlowConfig fc) {
		FlowInfo fi = new FlowInfo();
		fi.setFlowClass(fc.getFlowClass());
		fi.setFlowKey(fc.getFlowKey());
		fi.setFlowName(fc.getFlowName());
		fi.setFlowVersion(fc.getFlowVersion());
		return fi;
	}
}
