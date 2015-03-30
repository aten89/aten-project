/**
 * 
 */
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
 * 调拨单入库
 * @author Tim
 * @version 2009-09-08
 */
public class AlloDataArchiveEvent implements IActionHandler {
	
	private static final long serialVersionUID = -6856081147728770477L;
	private static final Log log = LogFactory.getLog(AlloDataArchiveEvent.class);


	@Override
	public void execute(Execution exe) throws Exception {
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_FORMID);
		String formId = var.getValue();//单号
		var =exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_OUTACCOUNTID);
		//String outUserId =  var.getValue();
		var =exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_INACCOUNTID);
		//String inUserId =  var.getValue();
		IDeviceAllocateBiz deviceAllocateBiz = (IDeviceAllocateBiz) SpringHelper.getSpringContext().getBean("deviceAllocateBiz");
		deviceAllocateBiz.txPublishAllocateForm(formId);
		log.info(".........DataArchiveEvent:");
	}

}
