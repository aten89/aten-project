/**
 * 
 */
package org.eapp.oa.flow.handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.flow.blo.ITaskBiz;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.handler.IActionHandler;


/**
 * 任务实例授权事件执行
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
	public void execute(Execution exe) throws Exception {
		TaskInstance ti = exe.getTaskInstance();
		FlowInstance fi  = exe.getFlowInstance();
		ContextVariable var = fi.getVariable(SysConstants.FLOW_VARNAME_FORMID);
		
		ITaskBiz taskBiz = (ITaskBiz) SpringHelper.getBean("taskBiz");
		
		//创建任务
		Task t = new Task();
		t.setTaskInstanceID(ti.getId());
		t.setFlowInstanceID(fi.getId());
		t.setFlowDefineID(fi.getFlowDefine().getId());
		t.setFlowKey(fi.getFlowDefine().getFlowKey());
		t.setTaskState(ti.getProcessState());
		t.setCreateTime(ti.getCreateTime());
		t.setFormID(var.getValue());
		t.setNodeName(exe.getNode().getName());
		t.setTaskName(ti.getTaskName());
		t.setFlowName(fi.getFlowName());
		t.setViewFlag(Boolean.FALSE);
		
		var = fi.getVariable(SysConstants.FLOW_VARNAME_TASKDESCRIPTION);
		t.setDescription(var.getValue());
		taskBiz.addTask(t);
		log.info("任务创建事件:TaskCreateEvent,任务实例ID:"+ti.getId()+",任务名称：" + ti.getTaskName());
	}

}
