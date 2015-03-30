/**
 * 
 */
package org.eapp.workflow.svr;

import java.util.List;

import org.eapp.workflow.WfmContext;
import org.eapp.workflow.WfmException;
import org.eapp.workflow.db.FlowSession;
import org.eapp.workflow.def.FlowDefine;


/**
 * 数据库操作服务
 * @author 林良益
 * 2008-10-31
 * @version 2.0
 */
public class SubFlowService {
	private FlowSession flowSession;
	
	public SubFlowService(WfmContext context){
		this.flowSession = context.getFlowSession();
	}
	
	/**
	 * 根据流程名称，获取并实例化流程定义
	 * @param subFlowName 流程名称
	 * @param subFlowVersion 流程版本
	 * @return
	 */
	public FlowDefine findSubFlowDefine(String subFlowName , Long subFlowVersion) {
		List<FlowDefine> flowDefines = null;
		if (subFlowName == null) {
			throw new WfmException("子流程名字为空");
		}
	  	// 通过名称查找子流程
    	if (subFlowVersion != null) {//名称与版本号查找
    		flowDefines = flowSession.findFlowDefineByName(subFlowName, subFlowVersion.longValue());
        } else { //只通过名称查找
        	flowDefines = flowSession.findFlowDefineByName(subFlowName);
        }

		if (flowDefines != null && flowDefines.size() > 0) {//取第一个
			return flowDefines.get(0);
		} else {
			return null;
		}
	}
	
}
