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
 * 信息数据入库
 * @author zsy
 * @version Nov 30, 2008
 */
public class DataArchiveEvent implements IActionHandler {
	private static final Log log = LogFactory.getLog(DataArchiveEvent.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 7607867174344512171L;

	@Override
	public void execute(Execution exe) throws Exception {
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_FORMID);
		String formId = var.getValue();//信息单号
		IInfoFormBiz infoFormBiz = (IInfoFormBiz) SpringHelper.getBean("infoFormBiz");
		
		//发布信息，启动流程flowKey为空就是归档操作
		infoFormBiz.txStartFlow(formId, null);
		// 2013-03-12增加发送邮件
//        infoFormBiz.txSendEmail(formId);
		log.info(".........DataArchiveEvent:");
		log.info("公告信息审批归档事件");
	}

}
