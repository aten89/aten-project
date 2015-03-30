package org.eapp.oa.flow.handler.device;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.device.blo.IDeviceAllocateBiz;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.handler.IActionHandler;

/**
 * 作废设备调拨审批单
 * @author Tim
 * @version 2009-09-08
 */
public class AlloCancellationEvent implements IActionHandler {

	private static final long serialVersionUID = 3022870336720098769L;
	private static final Log log = LogFactory.getLog(AlloCancellationEvent.class);

	@Override
	public void execute(Execution exe) throws Exception {
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_FORMID);
		String formId = var.getValue();//单号
		IDeviceAllocateBiz deviceAllocateBiz = (IDeviceAllocateBiz) SpringHelper.getSpringContext().getBean("deviceAllocateBiz");
		//作废调拨单
		deviceAllocateBiz.txCancellAllocateForm(formId);
		log.info("CancellationEvent..");
	}
}
