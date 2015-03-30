package org.eapp.oa.flow.dao.impl;

import java.util.Date;

import org.eapp.oa.flow.dao.IFlowDataDAO;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.db.HibernateSessionFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 流程数据库 操作DAO实现,不使用 OA 系统的 Hibernate ,使用 流程引擎 中 的 WfmConfiguration 获取 session 手动关闭.
 */
public class FlowDataDAO implements IFlowDataDAO {

	/**
     * 日志
     */
	private static Logger log = LoggerFactory.getLogger(FlowDataDAO.class);
    /**
     * 流程引擎数据库删除完成 标识 0:正在执行 1:执行成功 -1:执行失败 默认 为1
     */
    private static Integer FLOW_DB_DELETE_RUNNING = 1;

    @Override
    public Integer getRunningFlag() {
        return FLOW_DB_DELETE_RUNNING;
    }

    /**
     * 获取流程引擎 的 SessionFactory
     * 
     * @return SessionFactory
     */
    private HibernateSessionFactory getSessionFactory() {
        return WfmConfiguration.getInstance().getSessionFactory();
    }

    /**
     * 获取 Hibernate Session
     * 
     * @return Hibernate Session
     */
    private Session getSession() {
        return getSessionFactory().getSession(true);
    }

    @Override
    public void deleteFlowData(Date date) {
        try {
            // 设置开始标识
            FLOW_DB_DELETE_RUNNING = 0;
            // 解除 流程实例 关于流程令牌的外键
            updateFlowInstanceByDate(date);
            // 解除 任务实例 关于流程令牌的外键
            updateTaskInstanceByDate(date);
            // 删除 流程轨迹点表
            deleteTracePointByDate();
            // 删除 节点动作日志表
//            deleteNodeActionLogByDate(date);
            // 删除 流程上下文变量表
            deleteContextVariableByDate(date);
            // 删除 集合操作者表
            deletePooledActorByDate(date);
            // 删除 集合操作角色表
            deletePooledRoleByDate(date);
            // 删除 任务实例表
            deleteTaskInstanceByDate(date);
            // 删除 执行令牌表
            deleteFlowTokenByDate(date);
            // 删除 流程实例表
            deleteFlowInstanceByDate(date);
            // 设置当前事物不回滚
            getSessionFactory().setRollback(false);
            log.info("删除流程数据完成!..................................");
            // 设置完成标识
            FLOW_DB_DELETE_RUNNING = 1;
        } catch (RuntimeException e) {
            log.info("删除流程数据失败!", e);
            // 设置当前事物回滚
            getSessionFactory().setRollback(true);
            // 设置标识为 运行失败
            FLOW_DB_DELETE_RUNNING = -1;
            throw e;
        } finally {
            // 关闭session,并处理事物
            getSessionFactory().closeSession();
        }
    }

    /**
     * 解除 流程实例 关于流程令牌的外键
     * 
     * @param date
     */
    private void updateFlowInstanceByDate(Date date) {
        log.info("开始解除流程实例关于流程令牌的外键....................");
        StringBuffer hql = new StringBuffer();
        hql.append(" update WF_FLOWINSTANCE fs set fs.flowtoken_ = null,fs.superflowtoken_ = null  where");
        hql.append(" fs.ENDTIME_ < :date ");

        Query query = getSession().createSQLQuery(hql.toString());
        query.setParameter("date", date);
        // 执行hql
        query.executeUpdate();

    }

    /**
     * 解除 任务实例 关于流程令牌的外键
     * 
     * @param date
     */
    private void updateTaskInstanceByDate(Date date) {
        log.info("开始解除任务实例关于流程令牌的外键....................");
        StringBuffer hql = new StringBuffer();
        hql.append("update WF_TASKINSTANCE ts set ts.flowtoken_ = null,ts.taskdefine_ = null where  exists");
        hql.append("(select fi.id_ from WF_FLOWINSTANCE fi where fi.ENDTIME_ < :date and fi.id_ = ts.FLOWINSTANCE_ )");
        
        Query query = getSession().createSQLQuery(hql.toString());
        query.setParameter("date", date);
        // 执行hql
        query.executeUpdate();
    }

    /**
     * 删除 流程轨迹点表
     * 
     * @param date 指定时间
     */
    private void deleteTracePointByDate() {
        log.info("开始删除流程轨迹点表..........................");
        StringBuffer hql = new StringBuffer();
        // 删除 流程轨迹点表 全部数据
        hql.append(" truncate table WF_TRACEPOINT");
        
        Query query = getSession().createSQLQuery(hql.toString());
        // 执行hql
        query.executeUpdate();
    }

    /**
     * 删除 节点动作日志表
     * 
     * @param date 指定时间
     */
//    private void deleteNodeActionLogByDate(Date date) {
//        log.info("开始删除节点动作日志表..........................");
//        StringBuffer hql = new StringBuffer();
//        // 删除 节点动作日志表
//        hql.append(" delete from WF_NODEACTIONlog na where exists");
//        hql.append(" (select fi.id_ from WF_FLOWINSTANCE fi where fi.ENDTIME_ < :date and fi.id_ = na.FLOWINSTANCE_ )");
//        try {
//            Query query = getSession().createSQLQuery(hql.toString());
//            query.setParameter("date", date);
//            // 执行hql
//            query.executeUpdate();
//        } catch (RuntimeException e) {
//            log.error("deleteNodeActionLogByDate get failed", e);
//            // 抛出异常
//            throw e;
//        }
//    }

    /**
     * 删除 流程上下文变量表
     * 
     * @param date 指定时间
     */
    private void deleteContextVariableByDate(Date date) {
        log.info("开始删除流程上下文变量表..........................");
        StringBuffer hql = new StringBuffer();
        // 删除 节点动作日志表
        hql.append(" delete from WF_CONTEXTVARIABLE where exists");
        hql.append(" (select fi.id_ from WF_FLOWINSTANCE fi where fi.ENDTIME_ < :date and fi.id_ = FLOWINSTANCE_ )");
        
        Query query = getSession().createSQLQuery(hql.toString());
        query.setParameter("date", date);
        // 执行hql
        query.executeUpdate();
    }

    /**
     * 删除 集合操作者表
     * 
     * @param date 指定时间
     */
    private void deletePooledActorByDate(Date date) {
        log.info("开始删除集合操作者表..........................");
        StringBuffer hql = new StringBuffer();
        // 删除 节点动作日志表
        hql.append(" delete from WF_POOLEDACTOR where exists");
        hql.append(" (select s.id_ from (");
        hql.append(" select tp.id_ from WF_TASKINSTANCE tp where exists");
        hql.append(" (select fi.id_ from WF_FLOWINSTANCE fi where fi.ENDTIME_ < :date and fi.id_ = tp.FLOWINSTANCE_ )");
        hql.append(" ) s where s.id_ = TASKINSTANCE_)");
        
        Query query = getSession().createSQLQuery(hql.toString());
        query.setParameter("date", date);
        // 执行hql
        query.executeUpdate();
    }

    /**
     * 删除 集合操作角色表
     * 
     * @param date 指定时间
     */
    private void deletePooledRoleByDate(Date date) {
        log.info("开始删除集合操作角色表..........................");
        StringBuffer hql = new StringBuffer();
        // 删除 节点动作日志表
        hql.append(" delete from WF_POOLEDROLE where exists");
        hql.append(" (select s.id_ from (");
        hql.append(" select tp.id_ from WF_TASKINSTANCE tp where exists");
        hql.append(" (select fi.id_ from WF_FLOWINSTANCE fi where fi.ENDTIME_ < :date and fi.id_ = tp.FLOWINSTANCE_ )");
        hql.append(" ) s where s.id_ = TASKINSTANCE_)");
        
        Query query = getSession().createSQLQuery(hql.toString());
        query.setParameter("date", date);
        // 执行hql
        query.executeUpdate();
    }

    /**
     * 删除 任务实例表
     * 
     * @param date 指定时间
     */
    private void deleteTaskInstanceByDate(Date date) {
        log.info("开始删除任务实例表..........................");
        StringBuffer hql = new StringBuffer();
        // 删除 节点动作日志表
        hql.append(" delete from WF_TASKINSTANCE where exists");
        hql.append(" (select fi.id_ from WF_FLOWINSTANCE fi where fi.ENDTIME_ < :date and fi.id_ = FLOWINSTANCE_ )");
        
        Query query = getSession().createSQLQuery(hql.toString());
        query.setParameter("date", date);
        // 执行hql
        query.executeUpdate();
    }

    /**
     * 删除 执行令牌表
     * 
     * @param date 指定时间
     */
    private void deleteFlowTokenByDate(Date date) {
        log.info("开始删除执行令牌表..........................");
        StringBuffer hql = new StringBuffer();
        // 删除 节点动作日志表
        hql.append(" delete from WF_FLOWTOKEN where exists");
        hql.append(" (select fi.id_ from WF_FLOWINSTANCE fi where fi.ENDTIME_ < :date and fi.id_ = FLOWINSTANCE_ )");
        
        Query query = getSession().createSQLQuery(hql.toString());
        query.setParameter("date", date);
        // 执行hql
        query.executeUpdate();
    }

    /**
     * 删除 流程实例表
     * 
     * @param date 指定时间
     */
    private void deleteFlowInstanceByDate(Date date) {
        log.info("开始删除流程实例表..........................");
        StringBuffer hql = new StringBuffer();
        // 删除 节点动作日志表
        hql.append(" delete from WF_FLOWINSTANCE where ");
        hql.append(" ENDTIME_ < :date");
        
        Query query = getSession().createSQLQuery(hql.toString());
        query.setParameter("date", date);
        // 执行hql
        query.executeUpdate();
    }
}
