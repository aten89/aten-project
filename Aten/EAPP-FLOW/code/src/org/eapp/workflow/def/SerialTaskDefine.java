/**
 * 
 */
package org.eapp.workflow.def;

import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.FlowToken;
import org.eapp.workflow.exe.OrderExecTaskInstance;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.exe.TaskManagement;


/**
 * 多人顺序执行任务定义
 * @author 林良益
 * 2008-10-16
 * @version 2.0
 */
public class SerialTaskDefine extends MutilExecTaskDefine{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4359042201739255879L;

	@Override
	public TaskInstance[] createTaskInstance(TaskManagement tm , Execution execution){
		TaskInstance[] taskInstances = null;
		String[] actors = null;
		//处理分配代理,取得分配人员对象集数组
		if(this.taskAssignment != null){
			String assignmentHandler = taskAssignment.getClassName();
			String assignmentExpression = taskAssignment.getExpression();
			
			actors = getAssignedActors(assignmentHandler , assignmentExpression , execution);
		}
		//生成并发的任务实例
		if(actors != null){
			taskInstances = new TaskInstance[actors.length];
			//获取流程实例
			FlowInstance flowInstance = tm.getFlowInstance();

			for(int i = 0 ; i < actors.length ; i++){
				//新建顺序任务实例
				TaskInstance taskInstance = new OrderExecTaskInstance();
				
				//根据任务分配顺序，设置任务执行顺序
				taskInstance.setExecuteOrder(i);				
				//绑定任务实例和流程实例
				flowInstance.addTaskInstance(taskInstance);
				//绑定任务定义，同时复制任务定义的属性
				taskInstance.setTaskDefine(this);				
				//仅当处于最后执行的任务实例可以触发节点下行,其它任务实例均为不可触发
				//注：该语句应该在taskInstance.setTaskDefine(this)执行之后执行，否则无效
				if(i < actors.length - 1){
					taskInstance.setIsSignalling(false);
				}				
				//将任务实例保存到数据库，同时付给ID
				WfmConfiguration.getInstance().getWfmContext().save(taskInstance);
				
				//对任务进行分配，绑定任务分配对象ID，该方法将触发任务分配事件				        
				taskInstance.setActorId(actors[i]);
				
				//绑定执行上下文
				if (execution != null) {
					FlowToken token = execution.getFlowToken();
					//绑定令牌
					taskInstance.setFlowToken(token);
					
					//对排在第一位的任务实例初始化
					if(taskInstance.getExecuteOrder() != null &&
							taskInstance.getExecuteOrder() == 0){
						try {
							execution.setTaskDefine(this);
							execution.setTaskInstance(taskInstance);
							execution.setEventFirer(this);
							//触发任务实例创建过程，包括事件
					        taskInstance.create(execution);
					        
						} finally {
			
							//清除上下文
							execution.setTaskDefine(null);
							execution.setTaskInstance(null);
							execution.setEventFirer(null);
						}
					}
					
				} else {
					//排在第一位的任务，设置任务状态为待办
					if(taskInstance.getExecuteOrder() != null &&
							taskInstance.getExecuteOrder() == 0){
							//在没有上下文的情况下创建任务实例，不包括事件
					        taskInstance.create(null);
					}
				}			
				
				//将任务实例放回给数组
				taskInstances[i] = taskInstance;
			}
		}
		return taskInstances;
	}		

}
