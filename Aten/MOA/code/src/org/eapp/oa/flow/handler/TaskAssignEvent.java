/**
 * 
 */
package org.eapp.oa.flow.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.flow.blo.ITaskBiz;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.handler.IActionHandler;


/**
 * 任务实例授权事件执行
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
	public void execute(Execution exe) throws Exception {
		TaskInstance ti = exe.getTaskInstance();
//		FlowInstance fi  = exe.getFlowInstance();
		String actorId = ti.getActorId();
		ITaskBiz taskBiz = (ITaskBiz) SpringHelper.getBean("taskBiz");
		Task t = taskBiz.getByTaskInstanceId(ti.getId());
		if (t == null) {
			throw new IllegalArgumentException();
		}
		
//		IPeriodTaskBiz periodTaskBiz= (IPeriodTaskBiz) SpringHelper.getSpringContext().getBean("periodTaskBiz");
//		ContextVariable executionTime = fi.getVariable(SysConstants.FLOW_VARNAME_EXECUTIONTIME);
//		ContextVariable executionClass = fi.getVariable(SysConstants.FLOW_VARNAME_EXECUTIONCLASS);
//		PeriodTask ptask =  periodTaskBiz.findById(t.getId());
//		
//		if(ptask!=null){
//		    periodTaskBiz.txDelete(ptask);
//		}
//		
//		boolean flag = true;
//		if(executionTime !=null && executionClass!=null){
//			//判断任务是否需要定时执行！
//			if(executionTime.getValue() !=null && executionClass.getValue()!=null){
//				//高层领导不生成定时任务
//				
//				//将公司高层改成xml配置
//				SysRuntimeParams sysRuntimeParams = SysRuntimeParams.loadSysRuntimeParams();
//				List<String> leaders = sysRuntimeParams.getLeaders();
//				if(StringUtils.isNotEmpty(actorId) && leaders.contains(actorId)){
//					flag = false;
//				}
//				if(flag){
//					PeriodTask pt = new PeriodTask();
//					Timestamp timestamp =Timestamp.valueOf(DataFormatUtil.formatTime( executionTime.getValue()));
//					Calendar ca = Calendar.getInstance();
//					ca.setTimeInMillis(timestamp.getTime());
//					ca.add(Calendar.DAY_OF_YEAR, 1);
//					pt.setExecutionTime(ca.getTime());
//					pt.setHandler(executionClass.getValue());
//					pt.setTask(t);
//					periodTaskBiz.txSaveOrUpdate(pt);
//				}
//			}
//		}
		//授权用户
		t.setTransactor(actorId);
		taskBiz.modifyTask(t);
		log.info("任务授权TaskAssignEvent,任务实例ID:【"+ti.getId()+"】,任务名称：" + ti.getTaskName());
		String decription = t.getDescription();
		//发送待办任务通知
		taskBiz.csSendTaskNotice(t, decription);
		
	}

}
