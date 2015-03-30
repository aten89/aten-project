package org.eapp.oa.flow.blo.impl;

import java.util.List;

import org.eapp.oa.flow.blo.IFlowConfigBiz;
import org.eapp.oa.flow.dao.IFlowConfigDAO;
import org.eapp.oa.flow.dto.FlowConfigQueryParameters;
import org.eapp.oa.flow.hbean.FlowConfig;
import org.eapp.oa.system.exception.OaException;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.WfmException;
import org.eapp.workflow.def.FlowDefine;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: 流程配置业务逻辑层
 * </p>
 */
public class FlowConfigBiz implements IFlowConfigBiz {

	public IFlowConfigDAO flowConfigDAO;

	public void setFlowConfigDAO(IFlowConfigDAO flowConfigDAO) {
		this.flowConfigDAO = flowConfigDAO;
	}

	/*
	 * 查询流程配置 (non-Javadoc)
	 */
	@Override
	public ListPage<FlowConfig> queryFlow(FlowConfigQueryParameters qp, boolean loadFlowDef) {
		if (qp == null) {
			throw new IllegalArgumentException();
		}
		ListPage<FlowConfig> listPage = flowConfigDAO.queryFlowConfig(qp);
		if (!loadFlowDef) {
			return listPage;
		}
		List<FlowConfig> dataList = listPage.getDataList();
		if (dataList == null) {
			return listPage;
		}
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			for (int i = 0; i < dataList.size(); i++) {
				FlowConfig flowConfig = (FlowConfig) dataList.get(i);
				String flowKey = flowConfig.getFlowKey();

				FlowDefine flowDefine = null;
				if (flowConfig.getDraftFlag().equals(FlowConfig.FLOW_FLAG_DRAFT)) {
					flowDefine = context.getFlowSession().findModifingFlowDefine(flowKey);
				} else if (flowConfig.getDraftFlag().equals(FlowConfig.FLOW_FLAG_PUBLISHED)) {
					flowDefine = context.getFlowSession().findFlowDefine(flowKey);
				}

				flowConfig.setFlowDefine(flowDefine);
			}
		} finally {
			context.close();
		}
		return listPage;
	}

	/*
	 * 判断修改流程时，名称是否重复 (non-Javadoc)
	 */
	private boolean isFlowNameRepeat(String flowName, String flowKey) {
		return flowConfigDAO.isFlowNameRepeat(flowName, flowKey);
	}

	/*
	 * (non-Javadoc)
	 */
	private boolean isFlowKeyRepeat(String flowKey, Integer draftFlag) {
		FlowConfig flowConfig = flowConfigDAO.findFlowByFlowKey(flowKey, draftFlag);
		return flowConfig != null;
	}

	/**
	 * 创建流程配置
	 * 
	 * @param flowClass
	 * @param flowKey
	 * @param flowName
	 * @param flowVersion
	 * @param draftFlag
	 * @return
	 * @throws OaException
	 */
	private FlowConfig createFlow(String flowClass, String flowKey, String flowName, Long flowVersion, Integer draftFlag)
			throws OaException {
		// 验证非空字段
		if (flowClass == null) {
			throw new IllegalArgumentException("非法参数:流程类别不能为空！");
		}
		if (flowKey == null) {
			throw new IllegalArgumentException("非法参数:流程标识不能为空！");
		}
		if (flowName == null) {
			throw new IllegalArgumentException("非法参数:流程名称不能为空");
		}
		if (flowVersion == null) {
			throw new IllegalArgumentException("非法参数:流程版本不能为空");
		}
		// 验证约束
		if (isFlowNameRepeat(flowName, null)) {
			throw new OaException("非法参数:流程名称不能重复！");
		}
		if (isFlowKeyRepeat(flowKey, FlowConfig.FLOW_FLAG_DRAFT)) {
			throw new OaException("非法参数:流程标识不能重复！");
		}

		FlowConfig flowConfig = new FlowConfig();
		flowConfig.setFlowClass(flowClass);
		flowConfig.setFlowKey(flowKey);
		flowConfig.setFlowName(flowName);
		flowConfig.setFlowVersion(flowVersion);
		flowConfig.setDraftFlag(draftFlag);

		flowConfigDAO.save(flowConfig);
		return flowConfig;
	}

	/*
	 * 创建流程草稿 (non-Javadoc)
	 */
	@Override
	public FlowConfig addFlowDraft(String workflowXml, String flowClass) throws OaException {
		if (workflowXml == null) {
			throw new IllegalArgumentException("非法参数：流程定义XML内容不能为空！");
		}
		if (flowClass == null) {
			throw new IllegalArgumentException("非法参数:流程类别不能为空！");
		}

		
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			// 解析流程定义xml文件为流程定义对象
			FlowDefine flowDefine = FlowDefine.parseXmlString(workflowXml);
			flowDefine.setCategory(flowClass);
			context.getFlowSession().saveFlowDraft(flowDefine);

			// 获取流程信息
			String flowKey = flowDefine.getFlowKey();
			String flowName = flowDefine.getName();
			long flowVersion = flowDefine.getVersion();
			int draftFlag = FlowConfig.FLOW_FLAG_DRAFT;// 设置为流程草稿

			// 保存流程
			FlowConfig flowConfig = createFlow(flowClass, flowKey, flowName, flowVersion, draftFlag);

			return flowConfig;

		} catch (WfmException e) {
			// add by fangwenwei 添加输出异常日志
			e.printStackTrace();
			context.rollback();
			throw e;
		} finally {
			context.close();
		}
	}

	/*
	 * 获取流程草稿的XML定义 (non-Javadoc)
	 */
	@Override
	public String getFlowDraftXmlDefine(String flowKey) {

		if (flowKey == null) {
			throw new IllegalArgumentException("非法参数:流程标识不能为空！");
		}
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			FlowDefine flow = context.getFlowSession().findModifingFlowDefine(flowKey);
			if (flow == null) {
				throw new IllegalArgumentException("流程草稿不存在：" + flowKey);
			}
			return flow.getXmlDefine();
		} finally {
			context.close();
		}
	}

	/*
	 * 获取已发布流程的XML定义 (non-Javadoc)
	 */
	@Override
	public String getFlowXmlDefine(String flowKey) {

		if (flowKey == null || "".equalsIgnoreCase(flowKey)) {
			throw new IllegalArgumentException("非法参数:流程标识不能为空！");
		}
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			String workflowString = context.getFlowSession().findFlowXmlDefine(flowKey);
			return workflowString;
		} finally {
			context.close();
		}
	}

	/*
	 * 修改流程配置 (non-Javadoc)
	 */
	private void modifyFlow(String flowKey, String flowClass, String flowName, Long flowVersion, Integer draftFlag)
			throws OaException {
		// 验证非空字段
		if (flowClass == null) {
			throw new IllegalArgumentException("非法参数:流程类别不能为空！");
		}
		if (flowKey == null) {
			throw new IllegalArgumentException("非法参数:流程标识不能为空！");
		}
		if (flowName == null) {
			throw new IllegalArgumentException("非法参数:流程名称不能为空");
		}
		if (flowVersion == null) {
			throw new IllegalArgumentException("非法参数:流程版本不能为空");
		}

		// 通过flowKey 和draftFlag 找到相应的流程
		FlowConfig flowConfigSrc = flowConfigDAO.findFlowByFlowKey(flowKey, draftFlag);
		if (flowConfigSrc == null) {
			throw new IllegalArgumentException("非法参数:流程标识为" + flowKey + "的流程在数据库不存在！");
		}

		// 验证约束
		if (isFlowNameRepeat(flowName, flowKey)) {
			throw new OaException("非法参数:流程名称不能重复！");
		}

		flowConfigSrc.setFlowClass(flowClass);
		flowConfigSrc.setFlowKey(flowKey);
		flowConfigSrc.setFlowName(flowName);
		flowConfigSrc.setFlowVersion(flowVersion);
		flowConfigSrc.setDraftFlag(draftFlag);

		flowConfigDAO.update(flowConfigSrc);
	}

	/*
	 * 修改流程草稿 (non-Javadoc)
	 */
	@Override
	public void modifyFlowDraft(String flowKey, String workflowXml, String flowClass) throws OaException {

		if (workflowXml == null) {
			throw new IllegalArgumentException("非法参数：流程定义XML内容不能为空！");
		}
		if (flowClass == null) {
			throw new IllegalArgumentException("非法参数:流程类别不能为空！");
		}

		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			// 解析流程定义xml文件为流程定义对象
			FlowDefine flowDefine = FlowDefine.parseXmlString(workflowXml);
			flowDefine.setCategory(flowClass);
			flowDefine.setFlowKey(flowKey);
			context.getFlowSession().saveFlowDraft(flowDefine);

			// 获取流程信息
			String flowName = flowDefine.getName();
			Long flowVersion = flowDefine.getVersion();
			Integer draftFlag = FlowConfig.FLOW_FLAG_DRAFT;// 设置为草稿

			// 验证非空字段
			if (flowKey == null) {
				throw new IllegalArgumentException("非法参数:流程标识不能为空！");
			}
			if (flowName == null) {
				throw new IllegalArgumentException("非法参数:流程名称不能为空");
			}
			if (flowVersion == null) {
				throw new IllegalArgumentException("非法参数:流程版本不能为空");
			}

			FlowConfig flowConfigSrc = flowConfigDAO.findFlowByFlowKey(flowKey, FlowConfig.FLOW_FLAG_DRAFT);
			if (flowConfigSrc == null) {
				createFlow(flowClass, flowKey, flowName, flowVersion, draftFlag);
			} else {
				// 如果已存在当前草稿，则修改当前流程
				modifyFlow(flowKey, flowClass, flowName, flowVersion, draftFlag);
			}
		} catch (WfmException e) {
			// add by fangwenwei 添加输出异常日志
			e.printStackTrace();
			context.rollback();
			throw e;
		} finally {
			context.close();
		}
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public FlowConfig deleteFlowDraft(String flowKey) {
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			context.getFlowSession().delFlowDraft(flowKey);

			if (flowKey == null) {
				throw new IllegalArgumentException("非法参数:流程标识不能为空！");
			}
			FlowConfig flowConfig = flowConfigDAO.findFlowByFlowKey(flowKey, FlowConfig.FLOW_FLAG_DRAFT);
			if (flowConfig == null) {
				throw new IllegalArgumentException("非法参数:流程标识为" + flowKey + "的流程在数据库不存在！");
			}
			flowConfigDAO.delete(flowConfig);
			return flowConfig;

		} catch (RuntimeException e) {
			e.printStackTrace();
			context.rollback();
			throw e;
		} finally {
			context.close();
		}
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public void txPublishFlowDraft(String flowKey) throws OaException {

		if (flowKey == null) {
			throw new IllegalArgumentException("非法参数:流程标识不能为空！");
		}

		// 如果存在相同flowKey 的已发布流程，则删除原先的已发布流程
		FlowConfig lastPublishedFlow = flowConfigDAO.findFlowByFlowKey(flowKey, FlowConfig.FLOW_FLAG_PUBLISHED);
		if (lastPublishedFlow != null) {
			flowConfigDAO.delete(lastPublishedFlow);
		}

		// 通过flowKey 和draftFlag 找到相应的流程
		FlowConfig flowConfig = flowConfigDAO.findFlowByFlowKey(flowKey, FlowConfig.FLOW_FLAG_DRAFT);
		if (flowConfig == null) {
			throw new IllegalArgumentException("非法参数:流程标识为" + flowKey + "的流程草稿在数据库中不存在！");
		}
		flowConfig.setDraftFlag(FlowConfig.FLOW_FLAG_PUBLISHED);// 发布完成后，取消草稿状态
		flowConfigDAO.update(flowConfig);
		
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			context.getFlowSession().enableModifidFlowDefine(flowKey);// 发布流程
		} catch (Exception e) {
			e.printStackTrace();
			context.rollback();
			throw new OaException(e.getMessage());
		} finally {
			context.close();
		}
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public void txDisableFlow(String flowKey) {

		if (flowKey == null || "".equalsIgnoreCase(flowKey)) {
			throw new IllegalArgumentException("非法参数：流程标识不能为空！");
		}

		// 通过flowKey 和draftFlag 找到相应的流程
		FlowConfig flowConfig = flowConfigDAO.findFlowByFlowKey(flowKey, FlowConfig.FLOW_FLAG_PUBLISHED);
		if (flowConfig == null) {
			throw new IllegalArgumentException("非法参数:流程标识为" + flowKey + "的流程草稿在数据库中不存在！");
		}
		
		flowConfig.setDraftFlag(FlowConfig.FLOW_FLAG_DISABLE);// 设置为禁用状态
		flowConfigDAO.update(flowConfig);
		
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			context.getFlowSession().disableFlowDefine(flowKey);
		} catch (WfmException e) {
			context.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			context.close();
		}

	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public FlowConfig getFlowByFlowKey(java.lang.String flowKey, Integer flowFlag) {
		return flowConfigDAO.findFlowByFlowKey(flowKey, flowFlag);
	}
	
	@Override
	public List<FlowConfig> getFlowsByFlowClass(String flowClass) {
		return flowConfigDAO.findByflowClass(flowClass);
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public FlowConfig addOrGetFlowDraft(String flowKey) throws OaException {

		// 检查传入参数
		if (flowKey == null) {
			throw new IllegalArgumentException("非法参数：流程标识不能为空！");
		}

		// ------------------------------------------------------------------
		// 1.如果存在相同flowKey的流程草稿，则直接返回
		// ------------------------------------------------------------------
		FlowConfig flowDraft = flowConfigDAO.findFlowByFlowKey(flowKey, FlowConfig.FLOW_FLAG_DRAFT);
		if (flowDraft != null) {
			return flowDraft;
		}

		// ------------------------------------------------------------------
		// 2.不存在相同flowKey的流程草稿，则为已发布流程创建草稿
		// ------------------------------------------------------------------
		FlowConfig flowPublished = flowConfigDAO.findFlowByFlowKey(flowKey, FlowConfig.FLOW_FLAG_PUBLISHED);
		if (flowPublished == null) {
			throw new IllegalArgumentException("非法参数：流程标识为" + flowKey + "的已发布流程在数据库中不存在！无法创建流程草稿！");
		}
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			// context.getFlowSession().disableFlowDefine(flowKey);

			String publishedFlowXmlDefine = context.getFlowSession().findFlowXmlDefine(flowKey);
			if (publishedFlowXmlDefine == null) {
				throw new OaException("流程标识为" + flowKey + "的已发布流程在流程引擎中不存在！无法创建流程草稿！");
			}

			// 解析并保存流程XML定义
			FlowDefine flowDefine = FlowDefine.parseXmlString(publishedFlowXmlDefine);
			flowDefine.setCategory(flowPublished.getFlowClass());
			flowDefine.setFlowKey(flowKey);// 设置相同的flowKey
			context.getFlowSession().saveFlowDraft(flowDefine);
		} catch (RuntimeException e) {
			// modify by fangwenwei 添加捕获异常并回滚 原：没有捕获异常如果处理失败没有回滚
			e.printStackTrace();
			context.rollback();
			throw new RuntimeException("流程处理出现异常", e);
		} finally {
			context.close();
		}
		// 获取流程配置信息
		String flowClass = flowPublished.getFlowClass();
		String flowName = flowPublished.getFlowName();
		long flowVersion = flowPublished.getFlowVersion();
		int draftFlag = FlowConfig.FLOW_FLAG_DRAFT;// 设置标志为流程草稿

		// 保存流程草稿配置
		 return createFlow(flowClass, flowKey, flowName, flowVersion, draftFlag);
	}

}
