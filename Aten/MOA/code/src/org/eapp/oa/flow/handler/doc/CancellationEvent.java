/**
 * 
 */
package org.eapp.oa.flow.handler.doc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.doc.blo.IDocFormBiz;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.handler.IActionHandler;

/**
 * 作废公文审批单
 * @author Tim
 * @version 2009-05-12
 */
public class CancellationEvent implements IActionHandler {

	private static final long serialVersionUID = 3022870336720098769L;
	private static final Log log = LogFactory.getLog(CancellationEvent.class);

	@Override
	public void execute(Execution exe) throws Exception {
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_FORMID);
		String formId = var.getValue();//信息单号
		IDocFormBiz docFormBiz = (IDocFormBiz) SpringHelper.getBean("docFormBiz");
		//作废信息单
		docFormBiz.txCancellationDocForm(formId);
		
		log.info("文件审批作废事件");
	}

}
