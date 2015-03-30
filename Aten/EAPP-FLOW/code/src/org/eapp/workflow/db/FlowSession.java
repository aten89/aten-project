/**
 * 
 */
package org.eapp.workflow.db;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.eapp.workflow.WfmException;
import org.eapp.workflow.def.FlowDefine;
import org.eapp.workflow.def.FlowDraft;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.FlowToken;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Query;


/**
 * 流程相关的持久化操作
 * @author 卓诗垚
 * @version 1.0
 */
public class FlowSession {
	protected HibernateSessionFactory dbService;
	public FlowSession(HibernateSessionFactory dbService) {
		this.dbService = dbService;
	}
	
	/**
	 * 保存对像
	 * @param object
	 */
	public void saveFlowInstance(FlowInstance flowInstance) {
		dbService.getSession(true).save(flowInstance);
		//处理子流程实例的保存
		saveSubFlowInstance(flowInstance);
	}
	
	/**
	 * 存储流程关联的子流程
	 * 发起一个递归保存
	 * @param flowInstance
	 */
	private void saveSubFlowInstance(FlowInstance flowInstance) {
		Set<FlowInstance> savedFlowInstances = new HashSet<FlowInstance>();
		//把当前主流程实例列入已保存的流程实例
		savedFlowInstances.add(flowInstance);
		//递归保持子流程实例
		cascadeSave(flowInstance.removeCascadeFlowInstances() , savedFlowInstances);
	}
	
	/**
	 * 保存子流程
	 * @param cascadeFlowInstances
	 * @param savedFlowInstances
	 */
	private void cascadeSave(List<FlowInstance> cascadeFlowInstances, Set<FlowInstance> savedFlowInstances) {
		if (cascadeFlowInstances!=null) {
			for(FlowInstance cascadeInstance : cascadeFlowInstances){
				saveCascadeInstance(cascadeInstance, savedFlowInstances);
			}
		}
	}
	
	/**
	 * 递归保存子流程实例
	 * 排除已经保存过的流程实例
	 * @param cascadeInstance
	 * @param savedFlowInstances
	 */
	private void saveCascadeInstance(FlowInstance cascadeInstance,  Set<FlowInstance> savedFlowInstances) {
		if (! savedFlowInstances.contains(cascadeInstance)) {
			//取出当前子流程实例的子流程实例,并移除与当前子流程实例的关联
			List<FlowInstance> cascadeFlowInstances = cascadeInstance.removeCascadeFlowInstances();
			//保存当前子流程实例
			saveFlowInstance(cascadeInstance);
			//将当前子流程实例加入已处理（已保存）的流程实例列表中
			savedFlowInstances.add(cascadeInstance);
			//存储当前子流程实例的子流程实例（递归调用）
			cascadeSave(cascadeFlowInstances , savedFlowInstances);
		}
	}
	
	/**
	 * 发布流程定义
	 * 1.清除ID
	 * 2.赋版本号
	 * @param flowDefine
	 */
	public void saveFlowDefine(FlowDefine flowDefine) {
		//清除ID
	//	flowDefine.cleanId();
		//生成流程标识
		flowDefine.setFlowKey(UUID.randomUUID().toString());
		//生成版本号
		flowDefine.setVersion(System.currentTimeMillis());
		//设置启用
		flowDefine.setState(FlowDefine.FLOW_STATE_ENABLED);
		
		//保存
		dbService.getSession(true).save(flowDefine);
	}
	
	/**
	 * 保存流程草图
	 * @param flowDefine 新解析的或数据加载的
	 */
	public FlowDraft saveFlowDraft(FlowDefine flowDefine) {
		FlowDraft flowDraft = null;
		FlowDefine fd = null;
		String flowKey = flowDefine.getFlowKey();
		if (flowKey != null) {
			flowDraft = findFlowDraft(flowKey);
			if (flowDraft == null) {
				//验证流程标识是否有效
				fd = findFlowDefine(flowKey);
				if (fd == null) {
					throw new WfmException("不存在流程标识:" + flowKey);
				}
			}
		}
		
		if (flowDraft == null) {
			flowDraft = new FlowDraft();
			flowDraft.setFlowKey(fd != null ? fd.getFlowKey() : UUID.randomUUID().toString());
			flowDraft.setVersion(fd != null ? fd.getVersion() : System.currentTimeMillis());//记录复本版本
			flowDraft.setNewVersion(System.currentTimeMillis());//生面新版本
		}
		flowDraft.setTextDefine(flowDefine.getTextDefine());
		flowDraft.setName(flowDefine.getName());//同步名称
		dbService.getSession(true).saveOrUpdate(flowDraft);

		return flowDraft;
	}
	
	/**
	 * 根据流程标识删除流程草图，即修改中的流程
	 * 
	 * @param flowKey
	 */
	public void delFlowDraft(String flowKey) {
		if (flowKey == null) {
			return;
		}
		try {
			//删除流程草图
			FlowDraft flowDraft = findFlowDraft(flowKey);
			if (flowDraft != null) {
				dbService.getSession(true).delete(flowDraft);
			}
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * 根据流程定义禁用所有版本的流程定义
	 * @param flowKey
	 */
	public void disableFlowDefine(String flowKey) {
		if (flowKey == null) {
			return;
		}
		Query query = dbService.getSession(true).getNamedQuery("FlowSession.disableFlowDefine");
		query.setString("flowKey", flowKey);
		query.setString("state", FlowDefine.FLOW_STATE_DISABLED);
		query.executeUpdate();

	}
	
	/**
	 * 根据流程标识与版本号禁用流程定义
	 * @param flowKey
	 * @param version
	 */
	public void disableFlowDefine(String flowKey, long version) {
		setFlowDefineState(flowKey, version, FlowDefine.FLOW_STATE_DISABLED);
	}
	
	/**
	 * 根据流程标识与版本号启用流程定义
	 * @param flowKey
	 * @param version
	 */
	public void enableFlowDefine(String flowKey, long version) {
		setFlowDefineState(flowKey, version, FlowDefine.FLOW_STATE_ENABLED);
	}
	
	/**
	 * 设置流程定义状态
	 * @param flowKey
	 * @param version
	 * @param state
	 */
	private void setFlowDefineState(String flowKey, long version, String state) {
		if (flowKey == null) {
			return;
		}
		Query query = dbService.getSession(true).getNamedQuery("FlowSession.setFlowDefineState");
		query.setString("flowKey", flowKey);
		query.setLong("version", version);
		query.setString("state", state);
		query.executeUpdate();
	}
	
	/**
	 * 根据流程标识查找最高版本，已启用状态的流程定义
	 * @param flowKey
	 * @return 流程定义
	 */
	public FlowDefine findFlowDefine(String flowKey) {
		if (flowKey == null) {
			return null;
		}
		Query query = dbService.getSession(true).getNamedQuery("FlowSession.findFlowDefine");
		query.setString("flowKey", flowKey);
		query.setString("state", FlowDefine.FLOW_STATE_ENABLED);
		query.setMaxResults(1);
		return (FlowDefine)query.uniqueResult();
	}
	
	/**
	 * 根据流程标识及版本号查找流程定义
	 * @param flowKey
	 * @param version
	 * @return 流程定义
	 */
	public FlowDefine findFlowDefine(String flowKey, long version) {
		if (flowKey == null) {
			return null;
		}
		Query query = dbService.getSession(true).getNamedQuery("FlowSession.findFlowDefineWithVersion");
		query.setString("flowKey", flowKey);
		query.setLong("version", version);
		query.setMaxResults(1);
		return (FlowDefine)query.uniqueResult();
	}
	
	/**
	 * 根据流程名称查找最高版本，已启用状态的流程定义
	 * @param name
	 * @return 流程定义
	 */
	@SuppressWarnings("unchecked")
	public List<FlowDefine> findFlowDefineByName(String name) {
		if (name == null) {
			return null;
		}
		Query query = dbService.getSession(true).getNamedQuery("FlowSession.findFlowDefineByName");
		query.setString("name", name);
		query.setString("state", FlowDefine.FLOW_STATE_ENABLED);
		return query.list();
	}
	
	/**
	 * 根据流程名称及版本号查找流程定义
	 * @param name
	 * @param version
	 * @return 流程定义
	 */
	@SuppressWarnings("unchecked")
	public List<FlowDefine> findFlowDefineByName(String name, long version) {
		if (name == null) {
			return null;
		}
		Query query = dbService.getSession(true).getNamedQuery("FlowSession.findFlowDefineByNameWithVersion");
		query.setString("name", name);
		query.setLong("version", version);
		return query.list();
	}
	
	
	/**
	 * 根据流程标识查找流程定义的XML描述
	 * @param flowKey
	 * @return 流程定义的XML描述
	 */
	public String findFlowTextDefine(String flowKey) {
		FlowDefine flowDefine = findFlowDefine(flowKey);
		if (flowDefine == null) {
			return null;
		}
		
//		//流程定义的名称以表中的名称字段为准
//		//如果xml中的名称属性与表中的名称字段不符，以表中的名称字段覆盖
//		try {
//			InputSource xmlSource = new InputSource(new StringReader(flowDefine.getXmlDefine()));
//			Document doc = new SAXReader().read(xmlSource);
//			Element root = doc.getRootElement();
//			root.addAttribute("name", flowDefine.getName());
//			return doc.asXML();
//		} catch (DocumentException e) {
//			e.printStackTrace();
//			throw new WfmException(e);
//		}
		return flowDefine.getTextDefine();
	}
	
	public String findFlowDraftTextDefine(String flowKey) {
		FlowDraft flowDraft = findFlowDraft(flowKey);
		if (flowDraft == null) {
			return null;
		}
		return flowDraft.getTextDefine();
	}
	
	/**
	 * 根据流程标识查找流程草图
	 * @param flowKey
	 * @return 流程草图
	 */
	public FlowDraft findFlowDraft(String flowKey) {
		if (flowKey == null) {
			return null;
		}
		Query query = dbService.getSession(true).getNamedQuery("FlowSession.findFlowDraft");
		query.setString("flowKey", flowKey);
		query.setMaxResults(1);
		return (FlowDraft)query.uniqueResult();
	}
	
	/**
	 * 发布流程
	 * 1.并删除流程草图
	 * 2.设置上一个版本为不可用
	 * @param flowDefine
	 * @return 流程定义
	 */
	public FlowDefine publishFlowDefine(FlowDefine flowDefine) {
		String flowKey = flowDefine.getFlowKey();
		if (flowKey == null) {
			throw new WfmException("流程标识能为空");
		}
		FlowDraft flowDraft = findFlowDraft(flowKey);
		if (flowDraft == null) {
			throw new WfmException("不存在已修改过的流程标识：" + flowKey);
		}
//		FlowDefine flowDefine = FlowDefine.parseXmlString(flowDraft.getXmlDefine());
		flowDefine.setFlowKey(flowDraft.getFlowKey());
		flowDefine.setVersion(flowDraft.getNewVersion());
		flowDefine.setState(FlowDefine.FLOW_STATE_ENABLED);
		disableFlowDefine(flowDraft.getFlowKey());//禁用之前流程
		
//		//更新所有版本的名称
//		Query query = dbService.getSession(true).getNamedQuery("FlowSession.updateFlowDefineName");
//		query.setString("flowKey", flowKey);
//		query.setString("name", flowDefine.getName());
//		query.executeUpdate();
		
		dbService.getSession(true).save(flowDefine);
		dbService.getSession(true).delete(flowDraft);
		return flowDefine;
	}
	
	/**
	 * 通过流程定义ID取得所有流程实例
	 * @param flowDefineId
	 * @return 流程实例列表
	 */
	@SuppressWarnings("unchecked")
	public List<FlowInstance> findFlowInstances(String flowDefineId) {
		if (flowDefineId == null) {
			return null;
		}
		Query query = dbService.getSession(true).getNamedQuery("FlowSession.findFlowInstances");
		query.setString("flowDefineId", flowDefineId);
		return query.list();
	}
	
	/**
	 * 通过流程定义标识及版本号取得所有流程实例
	 * @param flowKey
	 * @param version
	 * @return 流程实例
	 */
	@SuppressWarnings("unchecked")
	public List<FlowInstance> findFlowInstances(String flowKey, long version) {
		if (flowKey == null) {
			return null;
		}
		Query query = dbService.getSession(true).getNamedQuery("FlowSession.findFlowInstances-ByFlow");
		query.setString("flowKey", flowKey);
		query.setLong("version", version);
		return query.list();
	}
	
	/**
	 * 通过ID取得流程实例
	 * @param flowInstanceId
	 * @return 流程实例
	 */
	public FlowInstance findFlowInstance(String flowInstanceId) {
		if (flowInstanceId == null) {
			return null;
		}
		return (FlowInstance)dbService.getSession(true).get(FlowInstance.class, flowInstanceId);
	}
	
	/**
	 * 通过ID取得任务实例
	 * @param taskInstanceId
	 * @return 流程实例
	 */
	public TaskInstance findTaskInstance(String taskInstanceId) {
		if (taskInstanceId == null) {
			return null;
		}
		return (TaskInstance)dbService.getSession(true).get(TaskInstance.class, taskInstanceId);
	}
	
	/**
	 * 通过ID取得令牌
	 * @param flowTokenId
	 * @return 流程令牌
	 */
	public FlowToken findFlowToken(String flowTokenId) {
		if (flowTokenId == null) {
			return null;
		}
		return (FlowToken)dbService.getSession(true).get(FlowToken.class, flowTokenId);
	}
	
	/**
	 * 删除流程定义及对应实例
	 * @param flowDefines
	 * @param flowKey
	 */
	private void delFlowDefine(List<FlowDefine> flowDefines, String flowKey) {
		if (flowDefines == null || flowDefines.isEmpty()) {
			return;
		}
		//轨迹删除Query
		Query tpQuery = dbService.getSession(true).getNamedQuery("FlowSession.delFlowDefine-deleteTracePoint");
		//异步任务删除Query
		Query ajQuery = dbService.getSession(true).getNamedQuery("FlowSession.delFlowDefine-deleteAsynJob");
		List<FlowInstance> fis = null;
		for (FlowDefine df : flowDefines) {
			//删除流程实例
			fis = findFlowInstances(df.getId());
			if (fis != null) {
				for (FlowInstance fi : fis) {
					//删除异步任务
					ajQuery.setString("flowInstance", fi.getId());
					ajQuery.executeUpdate();
					
					dbService.getSession(true).delete(fi);
				}
			}
			dbService.getSession(true).delete(df);
			
			//删除轨迹
			tpQuery.setString("flowDefineId", df.getId());
			tpQuery.executeUpdate();
			//刷新缓存到数据库
			dbService.getSession().flush();
		}
		//删除流程草图
		FlowDraft fd = findFlowDraft(flowKey);
		if (fd != null) {
			dbService.getSession(true).delete(fd);
		}
	}
	
	/**
	 * 删除所有版本流程定义及实例
	 * @param flowKey
	 */
	@SuppressWarnings("unchecked")
	public void delFlowDefine(String flowKey) {
		Query query = dbService.getSession(true).getNamedQuery(
				"FlowSession.delFlowDefine-findAllByFlowKey");
		query.setString("flowKey", flowKey);
		//流程定义
		delFlowDefine((List<FlowDefine>)query.list(), flowKey);
	}
	
	/**
	 * 删除流程定义及实例
	 * @param flowKey
	 * @param version
	 */
	@SuppressWarnings("unchecked")
	public void delFlowDefine(String flowKey, long version) {
		Query query = dbService.getSession(true).getNamedQuery("FlowSession.findFlowDefineWithVersion");
		query.setString("flowKey", flowKey);
		query.setLong("version", version);
		//流程定义
		delFlowDefine((List<FlowDefine>)query.list(), flowKey);
	}
	
	/**
	 * 清除给定时间之前的流程实例数据
	 * 使用SQL批量删除
	 * @param beforeDate
	 */
	public void clearFlowInstance(Date beforeDate) {
		// 1.解除 流程实例 关联流程令牌的外键
        Query query = dbService.getSession(true).getNamedQuery("FlowSession.clearFlowInstance-updateFlowInstance");
        query.setParameter("date", beforeDate);
        query.executeUpdate();
        // 2.解除 执行令牌 关联父令牌的外键
        query = dbService.getSession(true).getNamedQuery("FlowSession.clearFlowInstance-updateFlowToken");
        query.setParameter("date", beforeDate);
        query.executeUpdate();
        // 3.删除 流程轨迹点表
        query = dbService.getSession(true).getNamedQuery("FlowSession.clearFlowInstance-deleteTracePoint");
        query.setParameter("date", beforeDate);
        query.executeUpdate();
        // 4.删除 异步任务表
        query = dbService.getSession(true).getNamedQuery("FlowSession.clearFlowInstance-deleteAsynJob");
        query.setParameter("date", beforeDate);
        query.executeUpdate();
        // 5.删除 流程上下文变量表
        query = dbService.getSession(true).getNamedQuery("FlowSession.clearFlowInstance-deleteContextVar");
        query.setParameter("date", beforeDate);
        query.executeUpdate();
        // 6.删除 集合操作者表
        query = dbService.getSession(true).getNamedQuery("FlowSession.clearFlowInstance-deletePoolActor");
        query.setParameter("date", beforeDate);
        query.executeUpdate();
        // 7.删除 集合操作角色表
        query = dbService.getSession(true).getNamedQuery("FlowSession.clearFlowInstance-deletePoolRole");
        query.setParameter("date", beforeDate);
        query.executeUpdate();
        // 8.删除 任务实例表
        query = dbService.getSession(true).getNamedQuery("FlowSession.clearFlowInstance-deleteTaskInstance");
        query.setParameter("date", beforeDate);
        query.executeUpdate();
        // 9.删除 执行令牌表
        query = dbService.getSession(true).getNamedQuery("FlowSession.clearFlowInstance-deleteFlowToken");
        query.setParameter("date", beforeDate);
        query.executeUpdate();
        // 10.删除 流程实例表
        query = dbService.getSession(true).getNamedQuery("FlowSession.clearFlowInstance-deleteFlowInstance");
        query.setParameter("date", beforeDate);
        query.executeUpdate();
	}
}
