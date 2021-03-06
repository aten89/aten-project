/**
 * 
 */
package org.eapp.poss.flow.handler.custPayment;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.blo.ICustPaymentBiz;
import org.eapp.poss.config.SysConstants;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.handler.IActionHandler;

/**
 * 划款流程结束handler
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-24	黄云耿	新建
 * </pre>
 */
public class DataArchiveEvent implements IActionHandler {
	
	/**
     * 
     */
    private static final long serialVersionUID = -3337522982301455101L;
    /**
     * 日志
     */
	private static final Log log = LogFactory.getLog(DataArchiveEvent.class);

	@Override
	public void execute(Execution exe) throws Exception {
	    log.info("将问题关闭。。。");
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_FORMID);
		String formId = var.getValue();
		ICustPaymentBiz custPaymentBiz = (ICustPaymentBiz)SpringHelper.getBean("custPaymentBiz");
		custPaymentBiz.txArchiveCustPayment(formId);
	}

}
