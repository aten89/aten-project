/**
 * 
 */
package org.eapp.workflow.handler;

import org.eapp.workflow.exe.Execution;


/**
 * 视图表单URL获取接口
 * @author 林良益
 * 2008-08-18
 * @version 1.0
 */
public interface IViewHandler {
	String calcViewURL(Execution execution) throws HandlerException;
}
