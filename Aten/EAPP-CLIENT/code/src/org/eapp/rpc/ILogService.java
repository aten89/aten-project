/**
 * 
 */
package org.eapp.rpc;

import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.dto.LogSaveInfo;


/**
 * @author zsy
 * @version 
 */
public interface ILogService {
	/**
	 * 写入接口日志
	 * @param log
	 */
	public void addInterfaceLog(String sessionID, LogSaveInfo log) throws RpcAuthorizationException;
	
	/**
	 * 写入系统日志
	 * @param log
	 */
	public void addActionLog(String sessionID, LogSaveInfo log) throws RpcAuthorizationException;
}
