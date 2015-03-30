package org.eapp.oa.flow.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.flow.blo.ITaskBiz;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.handler.IActionHandler;


public class TaskEndEvent implements IActionHandler {
	private static final Log log = LogFactory.getLog(TaskEndEvent.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -3439837759513504591L;

	@Override
	public void execute(Execution exe) throws Exception {
		TaskInstance ti = exe.getTaskInstance();
		ITaskBiz taskBiz = (ITaskBiz) SpringHelper.getBean("taskBiz");
		Task t = taskBiz.getByTaskInstanceId(ti.getId());
		if (t == null) {
			throw new IllegalArgumentException();
		}
		t.setTaskState(ti.getProcessState());
		t.setEndTime(ti.getEndTime());
		t.setComment(ti.getComment());
		taskBiz.modifyTask(t);
		log.info("任务结束事件：TaskEndEvent,任务实例ID:"+ti.getId()+",任务名称：" + ti.getTaskName());
	}

}
