/**
 * 
 */
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
 * 领用单单入库
 * @author Tim
 * @version 2009-10-19
 */
public class ReciDataArchiveEvent implements IActionHandler {
	private static final long serialVersionUID = 39934613044145386L;
	private static final Log log = LogFactory.getLog(ReciDataArchiveEvent.class);
	@Override
	public void execute(Execution exe) throws Exception {
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_FORMID);
		String formId = var.getValue();//单号
		IDeviceApplyBiz deviceApplyBiz = (IDeviceApplyBiz) SpringHelper.getSpringContext().getBean("deviceApplyBiz");
		deviceApplyBiz.txPublishApplyForm(formId);
		log.info(".........DataArchiveEvent:");
	}
}
