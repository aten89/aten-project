/**
 * 
 */
package org.eapp.oa.flow.handler.reim;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.reimburse.blo.IReimbursementBiz;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.handler.IActionHandler;

/**
 * 报销数据入库
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
//		ContextVariable varUser = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_USERACCOUNTID);
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_FORMID);
		// 做好准备,先把系统配置中关于邮件内容的模板读出来
//		SysRuntimeParams srtp = SysRuntimeParams.loadSysRuntimeParams();
		String formId = var.getValue();//报销单号
//		String userAccountId = varUser.getValue();//流程发起人
		
		IReimbursementBiz reimbursementBiz = (IReimbursementBiz) SpringHelper.getSpringContext().getBean(
				"reimbursementBiz");
		
//		IAddressListBiz addressListBiz = (IAddressListBiz) SpringHelper.getSpringContext().getBean("addressListBiz");
		
		//更新报销单
		reimbursementBiz.txModifyReim(formId, true);
		
		// 发送邮件给流程发起人
//		String content = srtp.getReimEnd();
//		content = content.replaceAll("@reiID", formId);
//		content = content.replaceAll("@finance", reim.getFinanceName());
//		content = content.replaceAll("@budgetItem", reim.getBudget().getName());
//		exe.getFlowToken().signal();
//		AddressList address = addressListBiz.getByAccountId(userAccountId);
//		String emailAddr = "";
//		if (address != null && address.getUserEmail() != null) {
//		    emailAddr = address.getUserEmail();
//		} else {
//		    emailAddr = TransformTool.getEmail(userAccountId);
//		}
//		
//		// 发送邮件提醒
//		JMailProxy.daemonSend(new MailMessage(emailAddr, "OA系统提醒：报销流程结束提醒。", content));
//		log.info("发送邮件地址: " + emailAddr + "。 邮件内容: " + content);
		log.info(".........DataArchiveEvent");
	}

}
