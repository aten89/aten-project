package org.eapp.flow.handler.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.flow.db.FlowTask;
import org.eapp.flow.db.FlowTaskBiz;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.handler.HandlerException;
import org.eapp.workflow.handler.IActionHandler;

/**
 * 任务实例放弃事件执行（认领完，如果放弃处理时触发）
 * @author Administrator
 * @version Dec 4, 2014
 */
public class TaskGiveUpEvent implements IActionHandler {
	private static final Log log = LogFactory.getLog(TaskGiveUpEvent.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -2791438084888603844L;

	@Override
	public void execute(Execution exe) throws HandlerException {
		TaskInstance ti = exe.getTaskInstance();
		
		FlowTaskBiz taskBiz = FlowTaskBiz.getInstance();
		FlowTask task = taskBiz.getByTaskInstanceId(ti.getId());
		if (task == null) {
			throw new IllegalArgumentException("本地任务不存在");
		}
		task.setTaskState(ti.getProcessState());
		task.setStartTime(ti.getStartTime());
		
		task.setTransactor(ti.getActorId());
		taskBiz.modifyTask(task);
		log.info("放弃任务事件：TaskGiveUpEvent,任务实例ID:"+ti.getId()+",任务名称：" + ti.getTaskName());
	}
}
