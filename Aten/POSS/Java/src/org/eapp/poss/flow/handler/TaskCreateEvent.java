/**
 * 
 */
package org.eapp.poss.flow.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.blo.ITaskBiz;
import org.eapp.poss.config.SysConstants;
import org.eapp.poss.hbean.Task;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.handler.IActionHandler;


/**
 * 任务实例创建事件
 * 
 * 
 * <pre>
 * 修改日期    修改人    修改原因
 * 2013-7-17    fang    新建
 * </pre>
 */
public class TaskCreateEvent implements IActionHandler {
    /**
     * 
     */
    private static final long serialVersionUID = 2421709870881116995L;
    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(TaskCreateEvent.class);

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public void execute(Execution exe) throws Exception {
        TaskInstance ti = exe.getTaskInstance();
        FlowInstance fi = exe.getFlowInstance();
        ContextVariable var = fi.getVariable(SysConstants.FLOW_VARNAME_FORMID);
        ITaskBiz taskBiz = (ITaskBiz) SpringHelper.getSpringContext().getBean("taskBiz");
        // 创建任务
        Task t = new Task();
        t.setTaskInstanceId(ti.getId());
        t.setFlowInstanceId(fi.getId());
        t.setFlowDefineId(fi.getFlowDefine().getId());
        t.setFlowKey(fi.getFlowDefine().getFlowKey());
        t.setTaskState(ti.getProcessState());
        t.setCreateTime(ti.getCreateTime());
        t.setFormId(var.getValue());
        t.setNodeName(exe.getNode().getName());
        t.setTaskName(ti.getTaskName());
        t.setFlowName(fi.getFlowName());
        t.setViewFlag(Boolean.FALSE);

        t.setTaskType(Task.TASK_TYPE_WORKFLOW);
        var = fi.getVariable(SysConstants.FLOW_VARNAME_FLOWCLASS);
        t.setFlowClass(var.getValue());

        var = fi.getVariable(SysConstants.FLOW_VARNAME_TASKDESCRIPTION);
        t.setDescription(var.getValue());

        taskBiz.addTask(t);

        log.info("任务创建TaskAssignEvent:" + ti.getTaskName());
    }
}
