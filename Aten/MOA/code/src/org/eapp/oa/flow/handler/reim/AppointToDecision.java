/**
 * 
 */
package org.eapp.oa.flow.handler.reim;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.handler.IDecisionHandler;

/**
 * 是否指定审批
 * @author zsy
 * @version Nov 30, 2008
 */
public class AppointToDecision implements IDecisionHandler {
	public static final String TRANSITION_YES = "是";
	public static final String TRANSITION_NO = "否";
	
	private static final Log log = LogFactory.getLog(AppointToDecision.class);
	
	@Override
	public String decide(Execution exe) throws Exception {
		log.info("FGManagerDecision:" + exe.getNode().getName());
		
		ContextVariable var = exe.getFlowInstance().getVariable(SysConstants.FLOW_VARNAME_APPOINTTO);
		String appointTo = var.getValue();//指定审批人
		
		if (StringUtils.isNotBlank(appointTo)) {
			log.info(TRANSITION_YES);
			return TRANSITION_YES;
		} else {
			log.info(TRANSITION_NO);
			return TRANSITION_NO;
		}
	}
}
