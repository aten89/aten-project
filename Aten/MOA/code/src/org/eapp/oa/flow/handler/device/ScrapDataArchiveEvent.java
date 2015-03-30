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
 * 设备报废单归档
 * 
 * @author sds
 * @version 2009-09-10
 */
public class ScrapDataArchiveEvent implements IActionHandler {
	
	private static final long serialVersionUID = 2107029437206608072L;
	private static final Log log = LogFactory.getLog(ScrapDataArchiveEvent.class);


	@Override
	public void execute(Execution exe) throws Exception {
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_FORMID);
		String formId = var.getValue();//单号
		IDeviceDiscardBiz deviceDiscardBiz = (IDeviceDiscardBiz) SpringHelper.getSpringContext().getBean("deviceDiscardBiz");
		deviceDiscardBiz.txPublishDiscardForm(formId);
		log.info(".........DataArchiveEvent:");
	}
}
