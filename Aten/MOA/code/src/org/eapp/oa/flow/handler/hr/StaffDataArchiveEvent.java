/**
 * 
 */
package org.eapp.oa.flow.handler.hr;

import org.eapp.oa.flow.blo.IFlowNotifyBiz;
import org.eapp.oa.flow.hbean.FlowNotify;
import org.eapp.oa.hr.blo.IStaffFlowApplyBiz;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.handler.IActionHandler;

/**
 * 
 * 数据入库
 * 
 */
public class StaffDataArchiveEvent implements IActionHandler {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7311181355743964027L;

    @Override
    public void execute(Execution exe) throws Exception {
    	ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_FORMID);
		
    	IStaffFlowApplyBiz staffFlowApplyBiz = (IStaffFlowApplyBiz) SpringHelper.getSpringContext().getBean(
		"staffFlowApplyBiz");
		String formId = var.getValue();//信息单号
		staffFlowApplyBiz.txModifyStaffFlowApply(formId, true);
		
		//触发流程结束知会
		IFlowNotifyBiz flowNotifyBiz = (IFlowNotifyBiz) SpringHelper.getSpringContext().getBean("flowNotifyBiz");
		flowNotifyBiz.txUpdateNotifyStatus(formId, FlowNotify.FLOW_STATUS_PASS);
    }

}
