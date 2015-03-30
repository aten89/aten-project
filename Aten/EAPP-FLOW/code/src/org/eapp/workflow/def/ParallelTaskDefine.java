/**
 * 
 */
package org.eapp.workflow.def;

import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.FlowToken;
import org.eapp.workflow.exe.TaskInstance;
import org.eapp.workflow.exe.TaskManagement;



/**
 * 多人并行处理的任务定义
 * @author 林良益
 * 2008-10-16
 * @version 2.0 
 */
public class ParallelTaskDefine extends MutilExecTaskDefine {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6919087354660995539L;
	
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
				//新建任务实例
				TaskInstance taskInstance = new TaskInstance();
				//绑定任务实例和流程实例
				flowInstance.addTaskInstance(taskInstance);
				//绑定任务定义，同时复制任务定义的属性
				taskInstance.setTaskDefine(this);
				//将任务实例保存到数据库，同时付给ID
				WfmConfiguration.getInstance().getWfmContext().save(taskInstance);

				//绑定执行上下文
				if (execution != null) {
					FlowToken token = execution.getFlowToken();
					//绑定令牌
					taskInstance.setFlowToken(token);

					try {
						execution.setTaskDefine(this);
						execution.setTaskInstance(taskInstance);
						execution.setEventFirer(this);
						//对任务进行分配，绑定任务分配对象ID，该方法将触发任务分配事件				        
						taskInstance.setActorId(actors[i]);
						//触发任务实例创建过程，包括事件
				        taskInstance.create(execution);
					} finally {
		
						//清除上下文
						execution.setTaskDefine(null);
						execution.setTaskInstance(null);
						execution.setEventFirer(null);
					}
				} else {
					//在没有上下文的情况下创建并触发任务实例，包括事件
					taskInstance.create(null);
				}
				//将任务实例放回给数组
				taskInstances[i] = taskInstance;
			}
		}
		return taskInstances;
	}	
}
