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
 * 任务实例结束事件执行
 * @author zsy
 * @version Nov 28, 2008
 */
public class TaskEndEvent implements IActionHandler {
	private static final Log log = LogFactory.getLog(TaskEndEvent.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -3439837759513504591L;

	@Override
	public void execute(Execution exe) throws HandlerException {
		TaskInstance ti = exe.getTaskInstance();
		
		FlowTaskBiz taskBiz = FlowTaskBiz.getInstance();
		FlowTask task = taskBiz.getByTaskInstanceId(ti.getId());
		if (task == null) {
			throw new IllegalArgumentException("本地任务不存在");
		}
		
		task.setTaskState(ti.getProcessState());
		task.setEndTime(ti.getEndTime());
		task.setComment(ti.getComment());
		taskBiz.modifyTask(task);
		log.info("任务结束事件：TaskEndEvent：" + ti.getTaskName());
	}
}
