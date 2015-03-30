/**
 * 
 */
package org.eapp.workflow.handler;

import org.eapp.workflow.exe.Execution;

/**
 * 多人任务分配接口
 * @author 林良益
 * 2008-10-16
 * @version 2.0
 */
public interface IMutiAssignmentHandler {
	
	String[] getAssignedActors(Execution execution) throws HandlerException;
	
}
