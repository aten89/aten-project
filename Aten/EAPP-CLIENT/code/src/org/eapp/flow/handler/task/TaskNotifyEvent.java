/**
 * 
 */
package org.eapp.flow.handler.task;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.flow.db.FlowTask;
import org.eapp.flow.db.FlowTaskAssign;
import org.eapp.flow.db.FlowTaskBiz;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.PooledActor;
import org.eapp.workflow.exe.PooledRole;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.handler.HandlerException;
import org.eapp.workflow.handler.IActionHandler;

/**
 * 任务实例通知事件执行（即任务分配给哪些人处理）
 * 注意：多人串行、多人并行任务是不会触发这个事件，因为这两个类型的任务只有单用户直接授权，所以没有pooled分配
 * @author zsy
 * @version Nov 28, 2008
 */
public class TaskNotifyEvent implements IActionHandler {
	private static final Log log = LogFactory.getLog(TaskNotifyEvent.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 2271020741630443722L;

	@Override
	public void execute(Execution exe) throws HandlerException {
		TaskInstance ti = exe.getTaskInstance();
		
		FlowTaskBiz taskBiz = FlowTaskBiz.getInstance();
		FlowTask task = taskBiz.getByTaskInstanceId(ti.getId());
		if (task == null) {
			throw new IllegalArgumentException("本地任务不存在");
		}
		
		//授权pooled用户
		Set<FlowTaskAssign> taskAssigns = new HashSet<FlowTaskAssign>();
		Set<PooledActor> actors = ti.getPooledActors();
		if (actors != null) {
			for (PooledActor actor : actors) {
				taskAssigns.add(new FlowTaskAssign(FlowTaskAssign.TYPE_USER, actor.getActorId()));
			}
		}
		//授权pooled角色
		Set<PooledRole> roles = ti.getPooledRoles();
		if (roles != null) {
			for (PooledRole role : roles) {
				taskAssigns.add(new FlowTaskAssign(FlowTaskAssign.TYPE_ROLE, role.getActorRoleId()));
			}
		}
		task.setTaskAssigns(taskAssigns);
		taskBiz.modifyTask(task);
		
		//任务通知
		taskBiz.notifyToDoTask(task);
		
		log.info("任务通知事件:TaskNotifyEvent：" + ti.getTaskName());
	}
}
