/**
 * 
 */
package org.eapp.workflow.handler;

import org.eapp.workflow.def.IAssignable;
import org.eapp.workflow.exe.Execution;




/**
 * 任务授权接口
 * 提供任务分派回调
 * @author  林良益
 * 2008-08-27
 * @version 1.0
 */
public interface IAssignmentHandler extends java.io.Serializable{
	void assign(IAssignable assignable , Execution execution) throws HandlerException;
}
