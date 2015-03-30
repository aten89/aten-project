/**
 * 
 */
package org.eapp.oa.flow.handler;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.flow.blo.ITaskBiz;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.flow.hbean.TaskAssign;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.PooledActor;
import org.eapp.workflow.exe.PooledRole;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.handler.IActionHandler;



/**
 * 任务实例授权事件执行
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
	public void execute(Execution exe) throws Exception {
		TaskInstance ti = exe.getTaskInstance();
		Set<PooledActor> actors = ti.getPooledActors();
		Set<PooledRole> roles = ti.getPooledRoles();
		
		ITaskBiz taskBiz = (ITaskBiz) SpringHelper.getBean("taskBiz");
		Task t = taskBiz.getByTaskInstanceId(ti.getId());
		if (t == null) {
			throw new IllegalArgumentException();
		}
		
		//授权pooled用户
		Set<TaskAssign> tps = new HashSet<TaskAssign>();
		if (actors != null) {
			for (PooledActor actor : actors) {
				TaskAssign tp = new TaskAssign();
				tp.setType(TaskAssign.TYPE_USER);
				tp.setAssignKey(actor.getActorId());
				tps.add(tp);
			}
		}
		//授权pooled角色
		if (roles != null) {
			for (PooledRole role : roles) {
				TaskAssign tp = new TaskAssign();
				tp.setType(TaskAssign.TYPE_ROLE);
				tp.setAssignKey(role.getActorRoleId());
				tps.add(tp);
			}
		}
		t.setTaskAssigns(tps);
		taskBiz.modifyTask(t);
		log.info("任务通知事件:TaskNotifyEvent,任务实例ID:"+ti.getId()+",任务名称：" + ti.getTaskName());
	}

}
