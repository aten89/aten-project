/**
 * 
 */
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
 * 任务实例授权事件执行（即任务认领，认领完其它人就不能处理）
 * @author zsy
 * @version Nov 28, 2008
 */
public class TaskAssignEvent implements IActionHandler {
    /**
     * 日志
     */
	private static final Log log = LogFactory.getLog(TaskAssignEvent.class);
	/**
	 * 序列
	 */
	private static final long serialVersionUID = 2271020741630443722L;

	@Override
	public void execute(Execution exe) throws HandlerException {
		TaskInstance ti = exe.getTaskInstance();
		
		FlowTaskBiz taskBiz = FlowTaskBiz.getInstance();
		FlowTask task = taskBiz.getByTaskInstanceId(ti.getId());
		if (task == null) {
			//多人串行任务是按顺序执行，所以不是同时创建，一开始只创建第一个任务实例，但授权是一开始统一处理的，
			//所以为类任务这里任务是有可能不存在的，是正常情况
			log.info("多人串行任务授权时外部任务不存在，忽略处理，任务授权TaskAssignEvent：" + ti.getTaskName());
//			throw new IllegalArgumentException("本地任务不存在");
			return;
		}
		//授权用户
		task.setTransactor(ti.getActorId());
		taskBiz.modifyTask(task);
		
		log.info("任务授权TaskAssignEvent：" + ti.getTaskName());
	}
}
