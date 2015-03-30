/**
 * 
 */
package org.eapp.flow.handler.task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.flow.db.FlowTask;
import org.eapp.flow.db.FlowTaskBiz;
import org.eapp.flow.handler.common.HandlerHelper;
import org.eapp.workflow.def.FlowDefine;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.handler.HandlerException;
import org.eapp.workflow.handler.IActionHandler;

/**
 * 任务实例创建事件执行
 * @author zsy
 * @version Nov 28, 2008
 */
public class TaskCreateEvent implements IActionHandler {
	private static final Log log = LogFactory.getLog(TaskCreateEvent.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 2271020741630443722L;

	@Override
	public void execute(Execution exe) throws HandlerException {
		TaskInstance ti = exe.getTaskInstance();
		FlowInstance fi = exe.getFlowInstance();
		FlowDefine fd = fi.getFlowDefine();
		
		//创建任务
		FlowTask task = new FlowTask();
		task.setFlowClass(fd.getCategory());
		task.setFlowKey(fd.getFlowKey());
		task.setTaskInstanceId(ti.getId());
		task.setFlowInstanceId(fi.getId());
		task.setFlowDefineId(fd.getId());
		task.setTaskState(ti.getProcessState());
		task.setFormId(HandlerHelper.getFormId(exe));
		task.setCreateTime(ti.getCreateTime());
		task.setNodeName(exe.getNode().getName());
		task.setTaskName(ti.getTaskName());
		task.setFlowName(fi.getFlowName());
		task.setViewFlag(Boolean.FALSE);
		task.setDescription(HandlerHelper.getTaskDescription(exe));
		//串行/并行任务是先触发授权事件后再创建事件，所以这里要同步授权者
		task.setTransactor(ti.getActorId());
		FlowTaskBiz taskBiz = FlowTaskBiz.getInstance();
		taskBiz.addTask(task);
		if (task.getTransactor() != null) {
			//串行/并行任务是先触发授权事件后再创建事件，所以任在务创建时已经授权了，
			//而且串行/并行任务不会触发TaskNotifyEvent事件，所以任务通知要在这里实现，其它类型任务的通知在TaskNotifyEvent里实现
			//任务通知
			taskBiz.notifyToDoTask(task);
		}
		log.info("任务创建事件:TaskCreateEvent：" + ti.getTaskName());
	}
}
