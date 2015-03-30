/**
 * 
 */
package org.eapp.workflow.handler;

import org.eapp.workflow.exe.Execution;



/**
 * 流程动作接口
 * 提供动作绑定的回调
 * @author  林良益
 * 2008-08-27
 * @version 1.0
 */
public interface IActionHandler extends java.io.Serializable{
	void execute(Execution execution) throws HandlerException;
}
