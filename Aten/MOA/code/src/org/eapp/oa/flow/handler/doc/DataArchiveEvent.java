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
 * 公文数据入库
 * @author Tim
 * @version 2009-05-12
 */
public class DataArchiveEvent implements IActionHandler {
	
	/**
	 * @author Tim
	 * 2009-May 12, 2009
	 */
	private static final long serialVersionUID = -7311181355743964027L;
	private static final Log log = LogFactory.getLog(DataArchiveEvent.class);


	@Override
	public void execute(Execution exe) throws Exception {
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_FORMID);
		String formId = var.getValue();//信息单号
		IDocFormBiz docFormBiz = (IDocFormBiz) SpringHelper.getBean("docFormBiz");
		
		//发布信息，启动流程flowKey为空就是归档操作
		docFormBiz.txStartFlow(formId, null,null,null);
		log.info("文件审批归档事件");
	}

}
