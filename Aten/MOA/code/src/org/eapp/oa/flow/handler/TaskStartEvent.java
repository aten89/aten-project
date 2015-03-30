package org.eapp.oa.flow.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.flow.blo.ITaskBiz;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.handler.IActionHandler;


public class TaskStartEvent implements IActionHandler {
	private static final Log log = LogFactory.getLog(TaskStartEvent.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -2791438084888603844L;

	@Override
	public void execute(Execution exe) throws Exception {
		TaskInstance ti = exe.getTaskInstance();
		ITaskBiz taskBiz = (ITaskBiz) SpringHelper.getBean("taskBiz");
		Task t = taskBiz.getByTaskInstanceId(ti.getId());
		if (t == null) {
			throw new IllegalArgumentException();
		}
		t.setTaskState(ti.getProcessState());
		t.setStartTime(ti.getStartTime());
		
		t.setTransactor(ti.getActorId());
		taskBiz.modifyTask(t);
		log.info("任务开始事件：TaskStartEvent,任务实例ID:"+ti.getId()+",任务名称：" + ti.getTaskName());
	}

}
