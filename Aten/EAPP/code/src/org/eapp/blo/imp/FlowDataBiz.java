package org.eapp.blo.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IFlowDataBiz;
import org.eapp.dao.IUserAccountDAO;
import org.eapp.dto.FlowTaskBean;
import org.eapp.flow.handler.common.HandlerHelper;
import org.eapp.hbean.UserAccount;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.WfmException;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.TaskInstance;

/**
 * 流程配置业务逻辑层
 */
public class FlowDataBiz implements IFlowDataBiz {
	private static final Log log = LogFactory.getLog(FlowDataBiz.class);

	public IUserAccountDAO userAccountDAO;

	public void setUserAccountDAO(IUserAccountDAO userAccountDAO) {
		this.userAccountDAO = userAccountDAO;
	}
	
	@Override
	public void clearFlowInstanceData(Date clearDate) {
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			context.getFlowSession().clearFlowInstance(clearDate);
		} catch (WfmException e) {
			log.error(e.getMessage(), e);
			context.rollback();
			throw e;
		} finally {
			context.close();
		}
	}

	@Override
	public List<FlowTaskBean> queryOpeningTasks(Date startCreateTime,
			Date endCreateTime, String taskName) {
		List<FlowTaskBean> tasks = null;
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			List<TaskInstance> tis = context.getTaskSession().getOpeningTasks(startCreateTime, endCreateTime, taskName);
			if (tis == null || tis.isEmpty()) {
				return tasks;
			}
			UserAccount user = null;
			tasks = new ArrayList<FlowTaskBean>();
			//Bean转换
			for (TaskInstance ti : tis ) {
				FlowTaskBean task = new FlowTaskBean();
				task.setTaskInstanceId(ti.getId());
				task.setFlowInstanceId(ti.getFlowInstance().getId());
				task.setTaskName(ti.getTaskName());
				task.setCreateTime(ti.getCreateTime());
				task.setActorId(ti.getActorId());
				if (StringUtils.isNotBlank(task.getActorId())) {
					user = userAccountDAO.findByAccountID(task.getActorId());
					task.setActorName(user != null ? user.getDisplayName() : task.getActorId());
				}
				ContextVariable var = ti.getFlowInstance().getVariable(HandlerHelper.FLOW_VARNAME_FORMID);
				if (var != null) {
					task.setFormId(var.getValue());
				}
				var = ti.getFlowInstance().getVariable(HandlerHelper.FLOW_VARNAME_USERACCOUNTID);
				if (var != null) {
					task.setUserAccountId(var.getValue());
					user = userAccountDAO.findByAccountID(task.getUserAccountId());
					task.setUserAccountName(user != null ? user.getDisplayName() : task.getUserAccountId());
				}
				var = ti.getFlowInstance().getVariable(HandlerHelper.FLOW_VARNAME_TASKDESCRIPTION);
				if (var != null) {
					task.setTaskDescription(var.getValue());
				}
				tasks.add(task);
			}
		} finally {
			context.close();
		}
		return tasks;
	}
}
