/**
 * 
 */
package org.eapp.poss.blo;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Task;
import org.eapp.poss.hbean.TaskAssign;

import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;
import org.eapp.workflow.exe.ContextVariable;


/**
 * 流程任务 处理 service 接口
 * 
 * 
 * <pre>
 * 修改日期		修改人	    修改原因
 * 2012-11-7	卢凯宁	    修改注释
 * </pre>
 */
public interface ITaskBiz {

    /**
     * 创建任务
     * 
     * @param task 任务
     * 
     *            <pre>
     * 修改日期		修改人	修改原因
     * 2012-11-7	卢凯宁	修改注释
     * </pre>
     */
    public void addTask(Task task);

    /**
     * 开始任务
     * 
     * @param taskInstanceId taskInstanceId
     * @param taskState taskState
     * @param startTime startTime
     * @param transactor transactor
     * 
     *            <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public void txStartTask(String taskInstanceId, String taskState, Date startTime, String transactor);

    /**
     * 结束任务
     * 
     * @param taskInstanceId taskInstanceId
     * @param taskState taskState
     * @param endTime endTime
     * @param comment comment
     * 
     *            <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public void txEndTask(String taskInstanceId, String taskState, Date endTime, String comment);

    /**
     * 领取任务
     * 
     * @param taskInstanceId taskInstanceId
     * @param transactor transactor
     * 
     *            <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public void txReceiveTask(String taskInstanceId, String transactor);

    /**
     * 根据任务实例获取任务
     * 
     * @param taskInstanceId taskInstanceId
     * @return Task
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public Task getByTaskInstanceId(String taskInstanceId);

    /**
     * 根据流程实例id获取已办理的任务
     * 
     * @param flowInstanceId flowInstanceId
     * @return List<Task>
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public List<Task> getEndedTasks(String flowInstanceId);

    /**
     * 取得任务的转向名称
     * 
     * @param taskInstanceId taskInstanceId
     * @return List<String>
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public List<String> getTaskTransitions(String taskInstanceId);

    /**
     * 处理审批任务
     * 
     * @param taskInstanceId 任务实例id
     * @param comment 审批意见
     * @param transitionName 出口
     * 
     *            <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public void txDealApproveTask(String taskInstanceId, String comment, String transitionName);

    /**
     * 开始流程
     * 
     * @param taskId taskId
     * @param userAccountId userAccountId
     * @return String
     * @throws Exception 
     * @throws CITException cit系统异常
     * 
     *             <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public String txStartTask(String taskId, String userAccountId, List<String> userRoles) throws Exception, PossException;

    /**
     * 根据id查找任务
     * 
     * @param id id
     * @return Task
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public Task getById(String id);

    /**
     * 根据文档id查找待处理的任务
     * 
     * @param formId formId
     * @return Task
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public Task getDealingTasks(String formId);

    /**
     * 查询处理中的任务
     * 
     * @param tqp 查询参数
     * @param userAccountId 用户账号
     * @return ListPage<Task>
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public ListPage<Task> queryDealingTasks(QueryParameters tqp, String userAccountId);

    /**
     * 授权
     * 
     * @param taskInstanceId taskInstanceId
     * @param assigns assigns
     * 
     *            <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public void txTaskAssign(String taskInstanceId, Set<TaskAssign> assigns);


    /**
     * 根据流程实例id和环境变量key获取环境变量值
     * @param flowInstanceId 流程湿漉漉id
     * @param contextVariableKey 环境变量key
     * @return 环境变量值
     * @throws Exception 
     * @throws CITException cit异常
     *
     * <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-24    fangwenwei    新建
     * </pre>
     */
    public String getContextVariable(String flowInstanceId, String contextVariableKey) throws PossException, Exception;
    /**
     * 更新流程实例的环境变量的值
     * @param flowInstanceId 流程实例id
     * @param variable 流程实例变量
     * @throws Exception 
     * @throws CITException 异常
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2013-7-25	kejianchun	新建
     * </pre>
     */
    public void txUpdateContextVariable(String flowInstanceId, ContextVariable variable) throws PossException, Exception;
}
