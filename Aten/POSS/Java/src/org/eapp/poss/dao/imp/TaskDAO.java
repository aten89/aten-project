package org.eapp.poss.dao.imp;

// default package
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.dao.ITaskDAO;
import org.eapp.poss.dao.param.TaskQueryParameters;
import org.eapp.poss.hbean.Task;
import org.eapp.poss.hbean.TaskAssign;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Query;
import org.hibernate.criterion.Example;


/**
 * 任务dao实现
 * 
 * 
 * <pre>
 * 修改日期    修改人    修改原因
 * 2013-7-17    fang    新建
 * </pre>
 */
public class TaskDAO extends BaseHibernateDAO implements ITaskDAO {
    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(TaskDAO.class);
    /**
     * 任务实例
     */
    private static final String TASK_INSTANCE_ID = "taskInstanceId";

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public Task findById(java.lang.String id) {
        log.debug("getting Task instance with id: " + id);
        try {
            Task instance = (Task) getSession().get(Task.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Task> findByExample(Task instance) {
        log.debug("finding Task instance by example");
        try {
            List<Task> results = getSession().createCriteria("Task").add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Task> findAll() {
        log.debug("finding all Task instances");
        try {
            String queryString = "from Task";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * 查询任务
     * 
     * @param propertyName 属性名称
     * @param value 值
     * @return 任务list
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    @SuppressWarnings("unchecked")
    private List<Task> findByProperty(String propertyName, Object value) {
        log.debug("finding Task instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from Task as model where model." + propertyName + "= ?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public Task findByTaskInstanceId(Object taskInstanceId) {
        List<Task> tasks = findByProperty(TASK_INSTANCE_ID, taskInstanceId);
        if (tasks == null || tasks.isEmpty()) {
            return null;
        }
        return tasks.get(0);
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Task> findEndedTasks(String flowInstanceID) {
        if (flowInstanceID == null) {
            return null;
        }
        try {
            Query query = getSession().createQuery(
                    "from Task as t where t.flowInstanceId=:flowInstanceID and "
                            + "t.endTime is not null order by t.endTime");
            query.setString("flowInstanceID", flowInstanceID);
            return query.list();
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Task> findEndedTasksByFormId(String formId) {
        if (formId == null) {
            return null;
        }
        try {
            Query query = getSession().createQuery(
                    "from Task as t where t.formId=:formId and " + "t.endTime is not null order by t.endTime");
            query.setString("formId", formId);
            return query.list();
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Task> findTasks(String formId) {
        if (formId == null) {
            return null;
        }
        try {
            Query query = getSession()
                    .createQuery("from Task as t where t.formId=:formId  " + " order by t.createTime");
            query.setString("formId", formId);
            return query.list();
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * java.lang.String)
     */
    @Override
    public ListPage<Task> queryToDoTaskPage(TaskQueryParameters tqp, String flowClass, String userAccountId) {
        String queryString = "from Task as t left join t.taskAssigns as ta "
                + "where t.flowClass=:flowClass and (t.taskState=:createState or t.taskState=:startState) "
                + "and ( (t.transactor=:userAccountId) or (ta.assignKey=:userAccountId and t.transactor is null and ta.type=:type)"
                + ") order by t.createTime desc";
        tqp.addParameter("flowClass", flowClass);
        tqp.addParameter("createState", TaskInstance.PROCESS_STATE_CREATE);
        tqp.addParameter("startState", TaskInstance.PROCESS_STATE_START);
        tqp.addParameter("userAccountId", userAccountId);
        tqp.addParameter("type", TaskAssign.TYPE_USER);
        return new CommQuery<Task>().queryListPage(tqp, "select distinct t " + queryString, "select count(distinct t) "
                + queryString, getSession());
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public ListPage<Task> queryToDoTaskPage(QueryParameters tqp, String userAccountId) {
        String queryString = "from Task as t left join t.taskAssigns as ta "
                + "where (t.taskState=:createState or t.taskState=:startState) "
                + "and ( (t.transactor=:userAccountId) or (ta.assignKey=:userAccountId and t.transactor is null and ta.type=:type)"
                + ") order by t.createTime desc";
        tqp.addParameter("createState", TaskInstance.PROCESS_STATE_CREATE);
        tqp.addParameter("startState", TaskInstance.PROCESS_STATE_START);
        tqp.addParameter("userAccountId", userAccountId);
        tqp.addParameter("type", TaskAssign.TYPE_USER);
        return new CommQuery<Task>().queryListPage(tqp, "select distinct t " + queryString, "select count(distinct t) "
                + queryString, getSession());
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public Task findDealingTasks(String formId) {
        if (formId == null) {
            return null;
        }
        try {
            Query query = getSession().createQuery(
                    "from Task as t where t.formId=:formId and "
                            + "(t.taskState=:createState or t.taskState=:startState) order by t.createTime desc");
            query.setString("formId", formId);
            query.setString("createState", TaskInstance.PROCESS_STATE_CREATE);
            query.setString("startState", TaskInstance.PROCESS_STATE_START);
            query.setMaxResults(1);
            return (Task) query.uniqueResult();
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public Task findDealingTask(String formId, String userId) {
        if (formId == null) {
            return null;
        }
        try {
            Query query = getSession()
                    .createQuery(
                            "select t from Task as t left join t.taskAssigns as ta where t.formId=:formId and "
                                    + " (t.taskState=:createState or t.taskState=:startState) and ( (t.transactor=:userAccountId) or (ta.assignKey=:userAccountId and t.transactor is null and ta.type=:type)) order by t.createTime desc");
            query.setParameter("formId", formId);
            query.setParameter("createState", TaskInstance.PROCESS_STATE_CREATE);
            query.setParameter("startState", TaskInstance.PROCESS_STATE_START);
            query.setParameter("userAccountId", userId);
            query.setParameter("type", TaskAssign.TYPE_USER);
            query.setMaxResults(1);
            return (Task) query.uniqueResult();
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Task> findCutTasks(String formId) {
        if (formId == null) {
            return null;
        }
        try {
            Query query = getSession().createQuery(
                    "from Task as t where t.formId=:formId and "
                            + "(t.taskState=:createState or t.taskState=:startState) order by t.createTime desc");
            query.setString("formId", formId);
            query.setString("createState", TaskInstance.PROCESS_STATE_CREATE);
            query.setString("startState", TaskInstance.PROCESS_STATE_START);
            return query.list();
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public ListPage<Task> queryUnFinishTask(TaskQueryParameters parms) {
        if (parms == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }

        StringBuffer hql = new StringBuffer("from Task as t left join t.taskAssigns as ta where 1=1");

        if (parms.getFormId() != null) {
            hql.append(" and t.formId = :formId");
        }

        if (parms.getEndCreateTime() != null) {
            hql.append(" and t.createTime <= :endCreateTime");
        }

        if (parms.getBeginCreateTime() != null) {
            hql.append(" and t.createTime >= :beginCreateTime");
        }

        if (parms.getTransactor() != null) {
            hql.append(" and ("
                    + "(t.transactor=:transactor ) or (ta.assignKey=:transactor and t.transactor is null and ta.type=:type)"
                    + ")");
        }
        if (parms.getState() != null && parms.getState().length > 0) {
            hql.append(" and t.taskState in (:states)");
        }

        try {
            return new CommQuery<Task>().queryListPage(parms, "select distinct t " + parms.appendOrders(hql, "t"),
                    "select count(distinct t) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            re.printStackTrace();
            return new ListPage<Task>();
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public Task getLastTaskByFormId(String formId) {
        if (formId == null) {
            return null;
        }
        try {
            Query query = getSession().createQuery(
                    "  from Task as t " + " where t.formId=:formId " + "   and t.taskState=:taskState "
                            + "   and t.nodeName like :nodeName " + " order by t.endTime desc ");

            query.setString("formId", formId);
            query.setString("nodeName", Task.TASK_NODE_NAME + "%");
            query.setString("taskState", TaskInstance.PROCESS_STATE_END);
            query.setMaxResults(1);
            return (Task) query.uniqueResult();
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Task> queryToDoTasks(String userAccountId, List<String> flowClassList, List<String> nodeNameList) {
        StringBuffer hql = new StringBuffer(
                "from Task as t left join t.taskAssigns as ta "
                        + "where ((t.taskState=:createState and (t.transactor=:userAccountId or ta.assignKey=:userAccountId and t.transactor is null and ta.type=:type))"
                        + " or (t.taskState=:startState and t.transactor=:userAccountId and ta.assignKey=:userAccountId and ta.type=:type))");
        if (flowClassList != null && !flowClassList.isEmpty()) {
            hql.append(" and t.flowClass in (:flowClassList)");
        }
        if (nodeNameList != null && !nodeNameList.isEmpty()) {
            hql.append(" and t.nodeName in (:nodeNameList)");
        }
        hql.append(" order by t.createTime desc");
        Query query = getSession().createQuery(hql.toString());
        query.setParameter("createState", TaskInstance.PROCESS_STATE_CREATE);
        query.setParameter("startState", TaskInstance.PROCESS_STATE_START);
        query.setParameter("userAccountId", userAccountId);
        query.setParameter("type", TaskAssign.TYPE_USER);
        if (flowClassList != null && !flowClassList.isEmpty()) {
            query.setParameterList("flowClassList", flowClassList);
        }
        if (nodeNameList != null && !nodeNameList.isEmpty()) {
            query.setParameterList("nodeNameList", nodeNameList);
        }
        List<Task> tasks = new ArrayList<Task>();
        List<Object[]> objects = query.list();
        for (Object[] o : objects) {
            Task task = (Task) o[0];
            tasks.add(task);
        }

        return tasks;
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public int queryToDoTasksNum(String userAccountId, List<String> flowClassList, List<String> nodeNameList) {
        StringBuffer hql = new StringBuffer(
                "from Task as t left join t.taskAssigns as ta "
                        + "where (t.taskState=:createState or t.taskState=:startState) "
                        + "and ( (t.transactor=:userAccountId) or (ta.assignKey=:userAccountId and t.transactor is null and ta.type=:type)"
                        + ") ");
        if (flowClassList != null && !flowClassList.isEmpty()) {
            hql.append(" and t.flowClass in (:flowClassList)");
        }
        if (nodeNameList != null && !nodeNameList.isEmpty()) {
            hql.append(" and t.nodeName in (:nodeNameList)");
        }
        hql.append(" order by t.createTime desc");
        Query query = getSession().createQuery(hql.toString());
        query.setParameter("createState", TaskInstance.PROCESS_STATE_CREATE);
        query.setParameter("startState", TaskInstance.PROCESS_STATE_START);
        query.setParameter("userAccountId", userAccountId);
        query.setParameter("type", TaskAssign.TYPE_USER);
        if (flowClassList != null && !flowClassList.isEmpty()) {
            query.setParameterList("flowClassList", flowClassList);
        }
        if (nodeNameList != null && !nodeNameList.isEmpty()) {
            query.setParameterList("nodeNameList", nodeNameList);
        }
        return query.list().size();
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Task> findAllTasksByFlowInstanceId(String flowInstanceId) {
        if (flowInstanceId == null) {
            return null;
        }
        try {
            Query query = getSession().createQuery(
                    "from Task as t where t.flowInstanceId=:flowInstanceId order by t.endTime");
            query.setString("flowInstanceId", flowInstanceId);
            return query.list();
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Task> findDealTasksByFlowInstanceId(String flowInstanceId) {
        if (flowInstanceId == null) {
            return null;
        }
        try {
            Query query = getSession().createQuery(
                    "from Task as t where t.flowInstanceId=:flowInstanceId and t.nodeName = '处理问题' order by t.startTime desc");
            query.setString("flowInstanceId", flowInstanceId);
            return query.list();
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Task> findDealingTasksByFlowClass(String flowClass) {
        if (flowClass == null) {
            return null;
        }
        try {
            Query query = getSession()
                    .createQuery(
                            "from Task as t where (t.taskState=:createState or t.taskState=:startState) and t.flowClass=:flowClass");
            query.setParameter("createState", TaskInstance.PROCESS_STATE_CREATE);
            query.setParameter("startState", TaskInstance.PROCESS_STATE_START);
            query.setString("flowClass", flowClass);
            return query.list();
        } catch (RuntimeException re) {
            log.error("findDealingTasksByFlowClass failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Task> findDealingTasks(String transactor, String flowClass) {
        try {
            String hql = "select t from Task t left join t.taskAssigns ta where t.taskState <>:taskState and ((t.transactor=:transactor) or (ta.assignKey=:transactor and t.transactor is null)) and t.flowClass =:flowClass) ";
            Query query = getSession().createQuery(hql);
            query.setParameter("taskState", Task.TASK_STATE_END);
            query.setParameter("transactor", transactor);
            query.setParameter("flowClass", flowClass);
            return query.list();
        } catch (RuntimeException re) {
            re.printStackTrace();
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Task> findTasksByFlowInstanceIdAndFormId(String flowInstanceId, String formId) {
        if (flowInstanceId == null || formId == null) {
            return null;
        }
        try {
            Query query = getSession().createQuery(
                    "from Task as t where t.flowInstanceId=:flowInstanceId "
                            + "and t.formId =:formId and t.endTime is not null order by t.endTime desc");
            query.setString("flowInstanceId", flowInstanceId);
            query.setString("formId", formId);
            return query.list();
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Task> findDealingTaskList(String flowInstanceId) {
        if (flowInstanceId == null) {
            return null;
        }
        try {
            Query query = getSession().createQuery(
                    "from Task as t where t.flowInstanceId=:flowInstanceID and "
                            + "(t.taskState=:createState or t.taskState=:startState) order by t.createTime desc");
            query.setString("flowInstanceID", flowInstanceId);
            query.setString("createState", TaskInstance.PROCESS_STATE_CREATE);
            query.setString("startState", TaskInstance.PROCESS_STATE_START);
            return query.list();
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }


}