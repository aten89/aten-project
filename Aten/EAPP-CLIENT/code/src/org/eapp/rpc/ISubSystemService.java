/**
 * 
 */
package org.eapp.rpc;

import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.dto.SubSystemInfo;


/**
 * @version 
 */
public interface ISubSystemService {
	/**
	 * 取得EAPP系统配置信息
	 * @param sessionID
	 * @return
	 * @throws RpcAuthorizationException
	 */
	public SubSystemInfo getEAPPSystemInfo(String sessionID) throws RpcAuthorizationException; 
	
	/**
	 * 取得子系统配置信息
	 * @param sessionID
	 * @param subSystemID
	 * @return
	 * @throws RpcAuthorizationException
	 */
	public SubSystemInfo getSubSystemInfo(String sessionID, String subSystemID) throws RpcAuthorizationException; 

}
