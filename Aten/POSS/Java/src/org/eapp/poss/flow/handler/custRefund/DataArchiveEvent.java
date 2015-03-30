/**
 * 
 */
package org.eapp.poss.flow.handler.custRefund;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.blo.ICustRefundBiz;
import org.eapp.poss.config.SysConstants;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.handler.IActionHandler;

/**
 * 退款流程结束handler
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-24	钟华杰	新建
 * </pre>
 */
public class DataArchiveEvent implements IActionHandler {
	
	/**
     * 
     */
    private static final long serialVersionUID = 500393631301174858L;
    /**
     * 日志
     */
	private static final Log log = LogFactory.getLog(DataArchiveEvent.class);

	@Override
	public void execute(Execution exe) throws Exception {
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_FORMID);
		String formId = var.getValue();
		ICustRefundBiz custRefundBiz = (ICustRefundBiz)SpringHelper.getBean("custRefundBiz");
		custRefundBiz.txArchiveCustRefund(formId);
		log.info("exe org.eapp.poss.flow.handler.custRefund.DataArchiveEvent");
	}

}
