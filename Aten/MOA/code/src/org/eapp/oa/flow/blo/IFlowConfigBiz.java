package org.eapp.oa.flow.blo;

import java.util.List;

import org.eapp.oa.flow.dto.FlowConfigQueryParameters;
import org.eapp.oa.flow.hbean.FlowConfig;
import org.eapp.oa.system.exception.OaException;
import org.eapp.util.hibernate.ListPage;


/**
 * <p>Title: </p>
 * <p>Description: 流程配置业务逻辑接口层</p>
 * @version 1.0 
 */
public interface IFlowConfigBiz {

	/**
	 * 查询流程列表
	 * @param qp 查询参数
	 * @return
	 */
	public ListPage<FlowConfig> queryFlow(FlowConfigQueryParameters qp, boolean loadFlowDef);

	/**
	 * 创建流程草稿
	 * @param workflowXml 工作流xml定义文件
	 * @param flowClass 流程类别
	 * @return
	 * @throws OaException OA业务异常
	 */
	public FlowConfig addFlowDraft(String workflowXml, String flowClass) throws OaException;

	/**
	 * 获取流程草稿的XML定义文件
	 * @param flowKey 流程标识
	 * @return
	 */
	public String getFlowDraftXmlDefine(String flowKey);
	
	/**
	 * 获取流程的XML定义文件
	 * @param flowKey 流程标识
	 * @return
	 */
	public String getFlowXmlDefine(String flowKey);

	/**
	 * 修改流程草稿
	 * @param flowKey 流程标识
	 * @return
	 * 如果草稿不存在，则新建一个草稿
	 */
	public void modifyFlowDraft(String flowKey,String workflowXml,String flowClass) throws OaException;

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
	 * 如果行前存在相同flowKey的已发布流程，则把原先存在的已发布流程删除掉
	 */
	public void txPublishFlowDraft(String flowKey) throws OaException;

	/**
	 * 禁用流程
	 * @param flowKey 流程标识
	 */
	public void txDisableFlow(String flowKey);

	
	/**
	 * 获取流程对象
	 * @param flowKey 流程标识
	 * @param flowFlag 流程标志
	 * @return
	 */
	public FlowConfig getFlowByFlowKey(String flowKey,Integer flowFlag);
	
	/**
	 * 通过流程类别取得已发布的流程列表
	 * @param flowClass
	 * @return
	 */
	public List<FlowConfig> getFlowsByFlowClass(String flowClass);
	
	/**
	 * 获取已发布流程的草稿
	 * 
	 * 1.如果相同flowKey的草稿不存在，则创建一份并返回
	 * 2.如果存在相同flowKey的草稿，则返回此草稿
	 * @param flowKey 流程标识
	 * @return 当前flowKey的草稿对象
	 * @throws OaException 
	 */
	public FlowConfig addOrGetFlowDraft(String flowKey) throws OaException;
}