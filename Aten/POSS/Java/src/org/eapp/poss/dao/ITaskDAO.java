package org.eapp.poss.dao;

import java.util.List;

import org.eapp.poss.dao.param.TaskQueryParameters;
import org.eapp.poss.hbean.Task;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;

/**
 * ITaskDAO任务dao接口
 * 
 * 
 * <pre>
 * 修改日期    修改人    修改原因
 * 2013-7-17    fang    新建
 * </pre>
 */
public interface ITaskDAO extends IBaseHibernateDAO {
    /**
     * 通过ID查找
     * 
     * @param id id
     * @return Task
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public Task findById(String id);

    /**
     * 通过属性查询
     * 
     * @param instance 实体
     * @return List<Task>
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public List<Task> findByExample(Task instance);

    /**
     * 查找所有
     * 
     * @return List<Task>
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public List<Task> findAll();

    /**
     * 通过任务实例ID查找
     * 
     * @param taskInstanceId taskInstanceId
     * @return Task
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public Task findByTaskInstanceId(Object taskInstanceId);

    /**
     * 根据流程实例id取得已结束的任务
     * 
     * @param flowInstanceId flowInstanceId
     * @return List<Task>
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public List<Task> findEndedTasks(String flowInstanceId);

    /**
     * 根据表单id取得已结束的任务
     * 
     * @param formId formId
     * @return List<Task>
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public List<Task> findEndedTasksByFormId(String formId);

    /**
     * 搜索待办任务列表
     * 
     * @param tqp 查询参数
     * @param flowClass 流程分类
     * @param userAccountId 用户账户
     * @return ListPage<Task>
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public ListPage<Task> queryToDoTaskPage(TaskQueryParameters tqp, String flowClass, String userAccountId);

    /**
     * 查询待办任务
     * 
     * @param tqp 查询参数
     * @param userAccountId 用户账户
     * @return
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public ListPage<Task> queryToDoTaskPage(QueryParameters tqp, String userAccountId);

    /**
     * 根据表单id查询任务
     * 
     * @param formId formId
     * @return List<Task>
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public List<Task> findTasks(String formId);

    /**
     * 查询处理中
     * 
     * @param formId formId
     * @return Task
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public Task findDealingTasks(String formId);

    /**
     * findCutTasks
     * 
     * @param formId formId
     * @return List<Task>
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public List<Task> findCutTasks(String formId);

    /**
     * 搜索未办理的任务分页数据
     * 
     * @param parms parms
     * @return ListPage<Task>
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public ListPage<Task> queryUnFinishTask(TaskQueryParameters parms);

    /**
     * 获取用户的待办任务
     * 
     * @param formId formId
     * @param userId userId
     * @return Task
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public Task findDealingTask(String formId, String userId);

    /**
     * 根据解决人查询此表单的最后已解决的任务
     * 
     * @param formId 表单id
     * @return Task
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public Task getLastTaskByFormId(String formId);

    /**
     * 获取指定用户所有的待办任务。如果有指定流程类别，则只获取指定流程类别的待办任务。如果有指定节点名称，则只获取指定名称的待办任务
     * 
     * @param userAccountId 用户
     * @param flowClassList 流程类别列表
     * @param nodeNameList 节点名称列表
     * @return
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public List<Task> queryToDoTasks(String userAccountId, List<String> flowClassList, List<String> nodeNameList);

    /**
     * 获取指定用户所有的待办任务的条数。如果有指定流程类别，则只获取指定流程类别的待办任务。如果有指定节点名称，则只获取指定名称的待办任务
     * 
     * @param userAccountId 用户id
     * @param flowClassList 流程类别列表
     * @param nodeNameList 节点名称列表
     * @return
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public int queryToDoTasksNum(String userAccountId, List<String> flowClassList, List<String> nodeNameList);

    /**
     * 根据流程实例id获取所有的任务
     * 
     * @param flowInstanceId flowInstanceId
     * @return List<Task>
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public List<Task> findAllTasksByFlowInstanceId(String flowInstanceId);

    /**
     * 根据流程实例ID获取任务节点为[处理问题]任务
     * @param flowInstanceId
     * @return
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2013-12-9	钟华杰	新建
     * </pre>
     */
    public List<Task> findDealTasksByFlowInstanceId(String flowInstanceId);
    /**
     * 根据流程类别查找待办任务
     * 
     * @param flowClass flowClass
     * @return List<Task>
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public List<Task> findDealingTasksByFlowClass(String flowClass);

    /**
     * 根据流程分类查找指定办理人的未完成任务
     * 
     * @param transactor transactor
     * @param flowClass flowClass
     * @return List<Task>
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public List<Task> findDealingTasks(String transactor, String flowClass);

    /**
     * 根据流程实例ID，FormID获取已结束任务
     * 
     * @param flowInstanceId flowInstanceId
     * @param formId formId
     * @return List<Task>
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public List<Task> findTasksByFlowInstanceIdAndFormId(String flowInstanceId, String formId);

    /**
     * 根据流程实例id 获取当前处理的所有任务
     * 
     * @param flowInstanceId 流程实例id
     * @return 当期处理的任务
     * 
     *         <pre>
     * 修改日期		修改人	   修改原因
     * 2012-10-8	方文伟  	新建
     * </pre>
     */
    List<Task> findDealingTaskList(String flowInstanceId);
}