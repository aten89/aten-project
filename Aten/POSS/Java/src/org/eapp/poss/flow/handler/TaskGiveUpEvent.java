package org.eapp.poss.flow.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.blo.ITaskBiz;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.handler.IActionHandler;

/**
 * 放弃任务事件
 * 
 * 
 * <pre>
 * 修改日期    修改人    修改原因
 * 2013-7-17    fang    新建
 * </pre>
 */
public class TaskGiveUpEvent implements IActionHandler {
    /**
     * 
     */
    private static final long serialVersionUID = 3167846731447401825L;
    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(TaskGiveUpEvent.class);

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public void execute(Execution exe) throws Exception {

        TaskInstance ti = exe.getTaskInstance();
        ITaskBiz taskBiz = (ITaskBiz) SpringHelper.getSpringContext().getBean("taskBiz");
        taskBiz.txStartTask(ti.getId(), ti.getProcessState(), ti.getStartTime(), ti.getActorId());

        log.info("放弃任务TaskGiveUpEvent:" + ti.getTaskName());
    }


}
