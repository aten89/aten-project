package org.eapp.blo;

import java.util.Date;
import java.util.List;

import org.eapp.dto.FlowTaskBean;

/**
 * 流程配置业务逻辑接口层
 */
public interface IFlowDataBiz {
	
	/**
	 * 从流程引擎里查询所有待办任务
	 * @param startCreateTime
	 * @param endCreateTime
	 * @param taskName
	 * @return
	 */
	public List<FlowTaskBean> queryOpeningTasks(Date startCreateTime, Date endCreateTime, String taskName);
	
	/**
	 * 清除流程历史数据
	 * @param clearDate
	 */
	public void clearFlowInstanceData(Date clearDate);

}