/**
 * 
 */
package org.eapp.workflow;

import org.eapp.workflow.svr.DBMessageServiceImpl;
import org.eapp.workflow.svr.LogService;
import org.eapp.workflow.svr.MessageService;
import org.eapp.workflow.svr.SubFlowService;

/**
 * 工作流服务模块访问工具类
 * @author 林良益
 * 2008-10-29
 * @version 2.0
 */
public class WfmServices {
	private static LogService logService;
	private static MessageService msgService;
	private static SubFlowService dbService;
	/**
	 * 取得消息服务
	 * @return
	 */
	public static MessageService getMessageSerivce(){
		if(msgService == null){
			msgService = new DBMessageServiceImpl(WfmConfiguration.getInstance().getWfmContext());
		}
		return msgService;
	}
	
	/**
	 * 取得数据库服务
	 * @return
	 */
	public static SubFlowService getSubFlowService(){
		if(dbService == null){
			dbService = new SubFlowService(WfmConfiguration.getInstance().getWfmContext());
		}
		return dbService;
	}
	
	/**
	 * 取得轨迹记录服务
	 * @return
	 */
	public static LogService getLogService(){
		if(logService == null){
			logService = new LogService(WfmConfiguration.getInstance().getWfmContext());
		}
		return logService;
	}
}
