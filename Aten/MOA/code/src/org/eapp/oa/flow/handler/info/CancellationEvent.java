/**
 * 
 */
package org.eapp.oa.flow.handler.info;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.info.blo.IInfoFormBiz;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.handler.IActionHandler;

/**
 * 作废信息审批单
 * @author zsy
 * @version Nov 30, 2008
 */
public class CancellationEvent implements IActionHandler {
	private static final Log log = LogFactory.getLog(CancellationEvent.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 5609321207037277719L;

	@Override
	public void execute(Execution exe) throws Exception {
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_FORMID);
		String formId = var.getValue();//信息单号
		IInfoFormBiz infoFormBiz = (IInfoFormBiz) SpringHelper.getBean("infoFormBiz");
		//作废信息单
		infoFormBiz.txCancellationInfoForm(formId);
		log.info("公告信息审批作废事件");
	}

}
