package org.eapp.oa.flow.handler.device;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.device.blo.IDeviceDiscardBiz;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.handler.IActionHandler;

/**
 * 设备报废单作废
 * 
 * @author sds
 * @version 2009-09-10
 */
public class ScrapCancellationEvent implements IActionHandler {

	private static final long serialVersionUID = -8615138881113389571L;
	private static final Log log = LogFactory.getLog(ScrapCancellationEvent.class);


	@Override
	public void execute(Execution exe) throws Exception {
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_FORMID);
		String formId = var.getValue();//单号
		IDeviceDiscardBiz deviceDiscardBiz = (IDeviceDiscardBiz) SpringHelper.getSpringContext().getBean(
			"deviceDiscardBiz");
		//作废报废单
		deviceDiscardBiz.txCancellDiscardForm(formId);
		/*
		//结束流程
		ITaskBiz taskBiz = (ITaskBiz)SpringHelper.getBean("taskBiz");
		Task task = taskBiz.getDealingTaskByFormID(formId);
		WfmContext context = WfmConfiguration.getInstance().getWfmContext(); 
		context.getFlowInstance(task.getTaskInstanceID()).end();
		taskBiz.txEndTask(task.getTaskInstanceID(), "ps_end", new Date(), task.getComment());
		*/
		log.info("CancellationEvent..");
	}

}
