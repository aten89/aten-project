/**
 * 
 */
package org.eapp.oa.rmi.hessian;

import java.util.List;

/**
 * @author zsy
 *
 */
public interface IFlowConfigPoint {

	/**
	 * 通过流程分类取得已发布的流程信息
	 * @param flowClass
	 * @return
	 */
	public List<FlowInfo> getFlowInfos(String flowClass);
	
	/**
	 * 通过flowKey取得流程信息
	 * @param flowKey
	 * @return
	 */
	public FlowInfo getFlowInfo(String flowKey);
}
