/**
 * 
 */
package org.eapp.oa.flow.handler.hr;

import org.eapp.oa.flow.blo.IFlowNotifyBiz;
import org.eapp.oa.flow.hbean.FlowNotify;
import org.eapp.oa.hr.blo.IHolidayApplyBiz;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.handler.IActionHandler;


public class HoliCanCancellationEvent implements IActionHandler {

	private static final long serialVersionUID = 3022870336720098769L;



	@Override
	public void execute(Execution exe) throws Exception {
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_FORMID);
		
		IHolidayApplyBiz holidayApplyBiz = (IHolidayApplyBiz) SpringHelper.getSpringContext().getBean(
				"holidayApplyBiz");
		String formId = var.getValue();//信息单号
		holidayApplyBiz.txModifyCancelHolidayApply(formId, false);
		
		//触发流程结束知会
		IFlowNotifyBiz flowNotifyBiz = (IFlowNotifyBiz) SpringHelper.getSpringContext().getBean("flowNotifyBiz");
		flowNotifyBiz.txUpdateNotifyStatus(formId, FlowNotify.FLOW_STATUS_CANCEL);
	}

}
