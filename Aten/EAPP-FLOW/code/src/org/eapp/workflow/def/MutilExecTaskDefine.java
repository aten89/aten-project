/**
 * 
 */
package org.eapp.workflow.def;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eapp.workflow.WfmException;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.exe.TaskManagement;
import org.eapp.workflow.expression.ExpressionEvaluator;
import org.eapp.workflow.handler.IMutiAssignmentHandler;


/**
 * 多执行任务定义抽象类
 * 它是ParallelTaskDefine 和 SerialTaskDefine的超类
 * @author 林良益
 * @version 2.0
 */
public abstract class MutilExecTaskDefine extends TaskDefine {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8247649755420706986L;
	
	public abstract TaskInstance[] createTaskInstance(TaskManagement tm , Execution execution);
	
	/**
	 * 获取并发分配的人员列表
	 * @param assignedHandler
	 * @param assignedExpression
	 * @param execution
	 * @return
	 */
	String[] getAssignedActors(String assignmentHandler , String assignmentExpression , Execution execution){
		String[] actors = null;
		try {
			if (assignmentHandler!=null) {
				actors = performAssignmentHandler(assignmentHandler, execution);
			} else {
				//处理条件表达式授权
				actors = performAssignmentExpression(assignmentExpression, execution);
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
		return actors;
	}

	/**
	 * 动态回调任务分配实现类,获取被分配的人员名单
	 * @param assignmentHandler
	 * @param execution
	 * @return
	 * @throws Exception
	 */
	private String[] performAssignmentHandler(String assignmentHandler, Execution execution)throws Exception {
		// 实例化任务分派类
		Class<?> c = Class.forName(assignmentHandler);
		IMutiAssignmentHandler handlerInstance = (IMutiAssignmentHandler)c.newInstance();
		// 调用分配方法
		return handlerInstance.getAssignedActors(execution);
	}
	
	/**
	 * 表达式处理
	 * 格式如 actor001,actor002
	 * @param execution
	 * @return
	 */
	private String[] performAssignmentExpression(String assignmentExpression, Execution execution){
		//返回格式如：ACTORS:user2,user3
		String result = ExpressionEvaluator.evaluate(assignmentExpression, execution, String.class);
	    if (result == null) {
	    	throw new WfmException("条件表达式“"+assignmentExpression+"”执行结果为null");
	    }
	    String[] strArr = result.split(";");
	    List<String> ators = new ArrayList<String>();
    	for (String assinStr : strArr) {
    		String[] str = assinStr.split(":");
	    	if (str.length < 2) {
	    		continue;
	    	}
	    	if ("ACTORS".equals(str[0])) {
	    		ators.addAll(Arrays.asList(str[1].split(",")));
	    	}
    	}
	    return ators.toArray(new String[0]);
	    

//	    if (result instanceof List) {
//	    	List<Object> col = (List<Object>)result;
//	    	actors = new String[col.size()];
//	    	actors = col.toArray(actors);
//	    	return actors;
//	    } else if(result instanceof String){
//	    	actors = ((String)result).split(",");
//	    }
//	    return actors;
	}
}
