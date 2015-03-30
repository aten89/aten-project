package org.eapp.blo.imp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IFlowConfigBiz;
import org.eapp.dao.IFlowConfigDAO;
import org.eapp.dao.IUserAccountDAO;
import org.eapp.dao.param.FlowConfigQueryParameters;
import org.eapp.dto.FlowTracePointBean;
import org.eapp.exception.EappException;
import org.eapp.hbean.FlowConfig;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.WfmException;
import org.eapp.workflow.def.FlowDefine;
import org.eapp.workflow.def.FlowDraft;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.trace.TracePoint;

/**
 * 流程配置业务逻辑层
 */
public class FlowConfigBiz implements IFlowConfigBiz {
	private static final Log log = LogFactory.getLog(FlowConfigBiz.class);

	public IFlowConfigDAO flowConfigDAO;
	public IUserAccountDAO userAccountDAO;

	public void setFlowConfigDAO(IFlowConfigDAO flowConfigDAO) {
		this.flowConfigDAO = flowConfigDAO;
	}
	
	public void setUserAccountDAO(IUserAccountDAO userAccountDAO) {
		this.userAccountDAO = userAccountDAO;
	}
	
	@Override
	public FlowConfig getFlowByFlowKey(String flowKey, int flowFlag) {
		return flowConfigDAO.findByFlowKey(flowKey, flowFlag);
	}
	
	@Override
	public List<FlowConfig> getFlowsByFlowClass(String flowClass, int flowFlag) {
		return flowConfigDAO.findByflowClass(flowClass, flowFlag);
	}

	@Override
	public ListPage<FlowConfig> queryFlowConfig(FlowConfigQueryParameters qp) {
		if (qp == null) {
			throw new IllegalArgumentException();
		}
		return flowConfigDAO.queryFlowConfig(qp);
	}

	@Override
	public FlowConfig deleteFlowDraft(String flowKey) {
		if (StringUtils.isBlank(flowKey)) {
			throw new IllegalArgumentException("流程标识不能为空");
		}
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			//删除流程草稿
			context.getFlowSession().delFlowDraft(flowKey);
			//删除外部草稿
			FlowConfig flowConfig = flowConfigDAO.findByFlowKey(flowKey, FlowConfig.FLOW_FLAG_DRAFT);
			if (flowConfig != null) {
				flowConfigDAO.delete(flowConfig);
			}
			return flowConfig;
		} catch (RuntimeException e) {
			log.error(e.getMessage(), e);
			context.rollback();
			throw e;
		} finally {
			context.close();
		}
	}
	
	@Override
	public FlowConfig txPublishFlowDraft(String flowKey) throws EappException {
		if (StringUtils.isBlank(flowKey)) {
			throw new IllegalArgumentException("流程标识不能为空");
		}
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			// 如果存在相同flowKey 的已发布流程，则删除原先的已发布流程
			FlowConfig lastPublishedFlow = flowConfigDAO.findByFlowKey(flowKey, FlowConfig.FLOW_FLAG_PUBLISHED);
			if (lastPublishedFlow != null) {
				flowConfigDAO.delete(lastPublishedFlow);
			}
	
			// 通过flowKey 和draftFlag 找到相应的流程
			FlowConfig flowConfig = flowConfigDAO.findByFlowKey(flowKey, FlowConfig.FLOW_FLAG_DRAFT);
			if (flowConfig == null) {
				throw new IllegalArgumentException("流程标识为" + flowKey + "的流程草稿不存在");
			}
			flowConfig.setDraftFlag(FlowConfig.FLOW_FLAG_PUBLISHED);// 发布完成后，取消草稿状态
			flowConfigDAO.update(flowConfig);
			
			FlowDefine flowDefine = FlowDefine.parseJsonString(context.getFlowSession().findFlowDraftTextDefine(flowKey));
			flowDefine.setFlowKey(flowKey);
			context.getFlowSession().publishFlowDefine(flowDefine);// 发布流程
			return flowConfig;
		} catch (RuntimeException e) {
			log.error(e.getMessage(), e);
			context.rollback();
			throw e;
		} finally {
			context.close();
		}
	}
	
	@Override
	public FlowConfig txDisableFlow(String flowKey) {
		if (StringUtils.isBlank(flowKey)) {
			throw new IllegalArgumentException("流程标识不能为空");
		}
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			// 通过flowKey 和draftFlag 找到相应的流程
			FlowConfig flowConfig = flowConfigDAO.findByFlowKey(flowKey, FlowConfig.FLOW_FLAG_PUBLISHED);
			if (flowConfig == null) {
				throw new IllegalArgumentException("流程标识为" + flowKey + "的流程草稿不存在");
			}
//			flowConfigDAO.delete(flowConfig);//删除
			flowConfig.setDraftFlag(FlowConfig.FLOW_FLAG_DISABLE);
			flowConfigDAO.update(flowConfig);
			
			context.getFlowSession().disableFlowDefine(flowKey);
			return flowConfig;
		} catch (WfmException e) {
			context.rollback();
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			context.close();
		}
	}

	@Override
	public String getFlowAsString(String flowKey, boolean isDraft) {
		if (StringUtils.isBlank(flowKey)) {
			throw new IllegalArgumentException("流程标识不能为空");
		}
		String flowStr = null;
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			if (isDraft) {
				flowStr = context.getFlowSession().findFlowDraftTextDefine(flowKey);
			} else {
				flowStr = context.getFlowSession().findFlowTextDefine(flowKey);
			}
		} finally {
			context.close();
		}
		if (flowStr == null) {
			throw new IllegalArgumentException("流程不存在：" + flowKey);
		}
		return flowStr;
	}
	
	@Override
	public String getFlowAsString(String flowInstanceId) {
		if (StringUtils.isBlank(flowInstanceId)) {
			throw new IllegalArgumentException("流程实例ID不能为空");
		}
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			FlowInstance flowInstance = context.getFlowInstance(flowInstanceId);
			if (flowInstance != null) {
				FlowDefine flowDefine = flowInstance.getFlowDefine();
				if (flowDefine != null) {
					return flowDefine.getTextDefine();
				}
			}
		} finally {
			context.close();
		}
		throw new IllegalArgumentException("实例对应流程不存在：" + flowInstanceId);
	}
	
	@Override
	public Set<FlowTracePointBean> getFlowTracePoints(String flowInstanceId) {
		if (StringUtils.isBlank(flowInstanceId)) {
			throw new IllegalArgumentException("流程实例ID不能为空");
		}
		Set<FlowTracePointBean> traces = new HashSet<FlowTracePointBean>();
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			List<TracePoint> tracePoints= context.getTracePoints(flowInstanceId);
			if (tracePoints != null && !tracePoints.isEmpty()) {
				for (TracePoint tracePoint : tracePoints) {
					traces.add(new FlowTracePointBean(tracePoint));
				}
			}
		} finally {
			context.close();
		}
		return traces;
	}
	
	@Override
	public void txCopyFlowToDraft(String flowKey) throws EappException {
		if (flowKey == null) {
			throw new IllegalArgumentException("流程标识不能为空");
		}
		// 如果存在相同flowKey的流程草稿，则直接返回
		FlowConfig draftConfig = flowConfigDAO.findByFlowKey(flowKey, FlowConfig.FLOW_FLAG_DRAFT);
		if (draftConfig != null) {
			return;
		}

		// 不存在相同flowKey的流程草稿，则从已发布流程中创建草稿
		FlowConfig publishedConfig = flowConfigDAO.findByFlowKey(flowKey, FlowConfig.FLOW_FLAG_PUBLISHED);
		if (publishedConfig == null) {
			throw new IllegalArgumentException("流程标识为" + flowKey + "的已发布流程在数据库中不存在");
		}
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			//从流程引擎中取定义文本
			String json = context.getFlowSession().findFlowTextDefine(flowKey);
			if (json == null) {
				throw new EappException("流程标识为" + flowKey + "的已发布流程在流程引擎中不存在");
			}

			// 解析并保存流程XML定义
			FlowDefine flowDefine = FlowDefine.parseJsonString(json);
			flowDefine.setFlowKey(flowKey);// 设置相同的flowKey
			FlowDraft flowDraft = context.getFlowSession().saveFlowDraft(flowDefine);	//创建流程引擎中的草稿
			
			// 拷贝外部流程配置为草稿
			FlowConfig flowConfig = new FlowConfig();
			flowConfig.setFlowClass(publishedConfig.getFlowClass());
			flowConfig.setFlowKey(publishedConfig.getFlowKey());
			flowConfig.setFlowName(publishedConfig.getFlowName());
			flowConfig.setFlowVersion(flowDraft.getNewVersion());
			flowConfig.setDraftFlag(FlowConfig.FLOW_FLAG_DRAFT);
			flowConfigDAO.save(flowConfig);
			
		} catch (RuntimeException e) {
			log.error(e.getMessage(), e);
			context.rollback();
			throw e;
		} finally {
			context.close();
		}
		
	}
	
	@Override
	public FlowConfig addFlowDraft(String json) throws EappException {
		if (json == null) {
			throw new IllegalArgumentException("流程定义JSON内容不能为空！");
		}
		
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			// 解析流程定义xml文件为流程定义对象
			FlowDefine flowDefine = FlowDefine.parseJsonString(json);
			if (flowDefine.getCategory() == null) {
				throw new IllegalArgumentException("流程类别不能为空！");
			}
			FlowDraft flowDraft = context.getFlowSession().saveFlowDraft(flowDefine);

			// 验证约束
			if (flowConfigDAO.testFlowNameRepeat(flowDefine.getName(), flowDraft.getFlowKey())) {
				throw new EappException("流程名称不能重复");
			}
			if (flowConfigDAO.testFlowKeyRepeat(flowDraft.getFlowKey())) {
				throw new EappException("流程标识不能重复");
			}

			FlowConfig flowConfig = new FlowConfig();
			flowConfig.setFlowClass(flowDefine.getCategory());
			flowConfig.setFlowKey(flowDraft.getFlowKey());
			flowConfig.setFlowName(flowDefine.getName());
			flowConfig.setFlowVersion(flowDraft.getNewVersion());
			flowConfig.setDraftFlag(FlowConfig.FLOW_FLAG_DRAFT);
			flowConfigDAO.save(flowConfig);
			return flowConfig;
		} catch (WfmException e) {
			log.error(e.getMessage(), e);
			context.rollback();
			throw e;
		} finally {
			context.close();
		}
	}
	
	@Override
	public FlowConfig modifyFlowDraft(String flowKey, String json) throws EappException {
		if (json == null) {
			throw new IllegalArgumentException("流程定义JSON内容不能为空");
		}
		
		FlowConfig flowConfig = flowConfigDAO.findByFlowKey(flowKey, FlowConfig.FLOW_FLAG_DRAFT);
		if (flowConfig == null) {
			throw new IllegalArgumentException("流程草稿不存在");
		}
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			// 解析流程定义xml文件为流程定义对象
			FlowDefine flowDefine = FlowDefine.parseJsonString(json);
			flowDefine.setFlowKey(flowKey);
			if (flowDefine.getCategory() == null) {
				throw new IllegalArgumentException("流程类别不能为空");
			}
			FlowDraft flowDraft = context.getFlowSession().saveFlowDraft(flowDefine);
			// 验证约束
			if (flowConfigDAO.testFlowNameRepeat(flowDefine.getName(), flowKey)) {
				throw new EappException("流程名称不能重复");
			}

			flowConfig.setFlowClass(flowDefine.getCategory());
			flowConfig.setFlowKey(flowDraft.getFlowKey());
			flowConfig.setFlowName(flowDefine.getName());
			flowConfig.setFlowVersion(flowDraft.getNewVersion());
			flowConfigDAO.update(flowConfig);
			
		} catch (WfmException e) {
			log.error(e.getMessage(), e);
			context.rollback();
			throw e;
		} finally {
			context.close();
		}
		return flowConfig;
	}
}
