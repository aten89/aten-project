package org.eapp.blo;

import java.util.List;
import java.util.Set;

import org.eapp.dao.param.FlowConfigQueryParameters;
import org.eapp.dto.FlowTracePointBean;
import org.eapp.exception.EappException;
import org.eapp.hbean.FlowConfig;
import org.eapp.util.hibernate.ListPage;

/**
 * 流程配置业务逻辑接口层
 */
public interface IFlowConfigBiz {

	/**
	 * 获取流程对象
	 * @param flowKey 流程标识
	 * @param flowFlag 流程标志
	 * @return
	 */
	public FlowConfig getFlowByFlowKey(String flowKey,int flowFlag);
	
	/**
	 * 通过流程类别取得已发布的流程列表
	 * @param flowClass
	 * @return
	 */
	public List<FlowConfig> getFlowsByFlowClass(String flowClass, int flowFlag);
	
	/**
	 * 查询流程列表
	 * @param qp 查询参数
	 * @return
	 */
	public ListPage<FlowConfig> queryFlowConfig(FlowConfigQueryParameters qp);
	
	/**
	 * 删除流程草稿
	 * @param id 流程ID
	 * @return
	 */
	public FlowConfig deleteFlowDraft(String flowKey);
	
	/**
	 * 发布流程草稿
	 * @param flowKey 流程标识
	 * @throws OaException OA业务异常
	 * 如果存在相同flowKey的已发布流程，则把原先存在的已发布流程删除掉
	 */
	public FlowConfig txPublishFlowDraft(String flowKey) throws EappException;
	
	/**
	 * 禁用流程
	 * @param flowKey 流程标识
	 */
	public FlowConfig txDisableFlow(String flowKey);
	
	/**
	 * 获取流程定义的文本格式
	 * @param flowKey
	 * @param draftFlag
	 * @return
	 */
	public String getFlowAsString(String flowKey, boolean isDraft);
	
	/**
	 * 获取流程定义的文本格式
	 * @param flowKey
	 * @param draftFlag
	 * @return
	 */
	public String getFlowAsString(String flowInstanceId);
	
	/**
	 * 获取流程实例已执行过的节点或转向
	 * @param flowInstanceId
	 * @return
	 */
	public Set<FlowTracePointBean> getFlowTracePoints(String flowInstanceId);
	
	/**
	 * 修改正式流程时，如果不存在草稿则创建
	 * @param flowKey
	 */
	public void txCopyFlowToDraft(String flowKey) throws EappException;
	
	/**
	 * 创建流程草稿
	 * @param workflowXml 工作流xml定义文件
	 * @param flowClass 流程类别
	 * @return
	 * @throws OaException OA业务异常
	 */
	public FlowConfig addFlowDraft(String json) throws EappException;

	/**
	 * 修改流程草稿
	 * @param flowKey 流程标识
	 * @return
	 * 如果草稿不存在，则新建一个草稿
	 */
	public FlowConfig modifyFlowDraft(String flowKey, String json) throws EappException;

}