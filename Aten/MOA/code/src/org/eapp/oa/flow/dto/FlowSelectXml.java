package org.eapp.oa.flow.dto;

import java.util.List;

import org.eapp.oa.flow.hbean.FlowConfig;
import org.eapp.oa.system.dto.HTMLOptionsDTO;


public class FlowSelectXml extends HTMLOptionsDTO {

	private List<FlowConfig> flowList;

	public List<FlowConfig> getFlowList() {
		return flowList;
	}

	public void setFlowList(List<FlowConfig> flowList) {
		this.flowList = flowList;
	}
	
	public FlowSelectXml(List<FlowConfig> flowList){
		this.flowList = flowList;
	}
	
	/**
	 * 格式：
	 * <div>8a283d001a52213f011a5255d2f60002**信息中心报销流程</div>
	 * <div>zxt00001**流程名称</div>
	 */
	public String toString() {
		
		StringBuffer out = new StringBuffer();
		if (flowList == null || flowList.size() < 1) {
			return out.toString();
		}
		for (FlowConfig item : flowList) {
			out.append(createOption(item.getFlowKey(),item.getFlowName()));
		}
		return out.toString();
	}
}
