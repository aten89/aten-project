/**
 * 
 */
package org.eapp.workflow.handler;

import org.eapp.workflow.exe.Execution;


/**
 * 流程选择判定接口
 * @author 林良益
 * 2008-09-04
 * @version 2.0
 */
public interface IDecisionHandler {
	  String decide(Execution execution) throws HandlerException;
}
