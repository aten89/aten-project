package org.eapp.oa.flow.handler.device;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.device.blo.IDeviceApplyBiz;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.handler.IActionHandler;


/**
 * 作废设备领用审批单
 * @author Tim
 * @version 2009-09-08
 */
public class ReciCancellationEvent implements IActionHandler {
	private static final long serialVersionUID = -4757225176884980170L;
	private static final Log log = LogFactory.getLog(ReciCancellationEvent.class);

	@Override
	public void execute(Execution exe) throws Exception {
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_FORMID);
		String formId = var.getValue();//单号
		IDeviceApplyBiz deviceApplyBiz = (IDeviceApplyBiz) SpringHelper.getSpringContext().getBean("deviceApplyBiz");
		//作废调拨单
		deviceApplyBiz.txCancellDevPurchaseForm(formId);
		log.info("CancellationEvent..");
	}
}
