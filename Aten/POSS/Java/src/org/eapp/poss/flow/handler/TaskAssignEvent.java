/**
 * 
 */
package org.eapp.poss.flow.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.blo.ITaskBiz;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.handler.IActionHandler;

public class TaskAssignEvent
  implements IActionHandler
{
    /**
     * 
     */
    private static final long serialVersionUID = -1650691902135080304L;
    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(TaskAssignEvent.class);
    
    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public void execute(Execution exe) throws Exception {
        TaskInstance ti = exe.getTaskInstance();
        String actorId = ti.getActorId();
        ITaskBiz taskBiz = (ITaskBiz) SpringHelper.getSpringContext().getBean("taskBiz");
        taskBiz.txReceiveTask(ti.getId(), actorId);
        log.info("任务授权TaskAssignEvent:" + ti.getTaskName());
    }
}