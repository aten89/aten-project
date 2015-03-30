package org.eapp.oa.flow.blo;

import java.util.List;

import org.eapp.oa.flow.hbean.FlowMeta;

/**
 * <p>Title: </p>
 * <p>Description: 流程元数据业务逻辑接口层</p>
 * @author 
 * @version 1.0 
 */
public interface IFlowMetaBiz {

	/**
	 * 获取元数据列表 
	 * @param flowCateory 流程类别
	 * @return
	 */
	public List<FlowMeta> getByFlowCategory(String flowCateory);

}