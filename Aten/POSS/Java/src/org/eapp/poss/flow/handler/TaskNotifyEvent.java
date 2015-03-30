/**
 * 
 */
package org.eapp.poss.flow.handler;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.blo.ITaskBiz;
import org.eapp.poss.hbean.TaskAssign;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.PooledActor;
import org.eapp.workflow.exe.PooledRole;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.handler.IActionHandler;


/**
 * 任务实例授权事件
 * 
 *
 * <pre>
 * 修改日期    修改人    修改原因
 * 2013-7-17    fang    新建
 * </pre>
 */
public class TaskNotifyEvent implements IActionHandler {
    /**
     * 
     */
    private static final long serialVersionUID = 6107917472947677929L;
    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(TaskNotifyEvent.class);

    /*
     * (non-Javadoc)
     */
    @Override
    public void execute(Execution exe) throws Exception {
        TaskInstance ti = exe.getTaskInstance();
        Set<PooledActor> actors = ti.getPooledActors();
        Set<PooledRole> roles = ti.getPooledRoles();
        
        ITaskBiz taskBiz = (ITaskBiz) SpringHelper.getSpringContext().getBean("taskBiz");
        
        //授权pooled用户
        Set<TaskAssign> assigns = new HashSet<TaskAssign>();
        if (actors != null) {
            for (PooledActor actor : actors) {
                TaskAssign tp = new TaskAssign();
                tp.setType(TaskAssign.TYPE_USER);
                tp.setAssignKey(actor.getActorId());
                assigns.add(tp);
            }
        }
        //授权pooled角色
        if (roles != null) {
            for (PooledRole role : roles) {
                TaskAssign tp = new TaskAssign();
                tp.setType(TaskAssign.TYPE_ROLE);
                tp.setAssignKey(role.getActorRoleId());
                assigns.add(tp);
            }
        }
        taskBiz.txTaskAssign(ti.getId(), assigns);
        
        log.info("任务通知TaskNotifyEvent:" + ti.getTaskName());
    }

 
}
