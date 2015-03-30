package org.eapp.oa.flow.blo.impl;

import java.util.List;

import org.eapp.oa.flow.blo.IFlowMetaBiz;
import org.eapp.oa.flow.dao.IFlowMetaDAO;
import org.eapp.oa.flow.hbean.FlowMeta;


/**
 * <p>Description: 流程元数据业务逻辑层</p>
 * @version 1.0 
 */
public class FlowMetaBiz implements IFlowMetaBiz {
	
	private IFlowMetaDAO flowMetaDAO;
	
	public void setFlowMetaDAO(IFlowMetaDAO flowMetaDAO) {
		this.flowMetaDAO = flowMetaDAO;
	}

	public List<FlowMeta> getByFlowCategory(String flowCateory){
		return (List<FlowMeta>)flowMetaDAO.findByFlowCategory(flowCateory);
	}
}
