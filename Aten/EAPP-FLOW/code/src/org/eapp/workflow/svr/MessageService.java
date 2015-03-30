/**
 * 
 */
package org.eapp.workflow.svr;

import org.eapp.workflow.asyn.AsynJob;

/**
 * 消息服务接口
 * @author 林良益
 * 2008-10-29
 * @version 2.0
 */
public interface MessageService {
	
	void send(AsynJob job);
	
	void close();

}
