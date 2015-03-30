package org.eapp.poss.dao.imp;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.dao.ITaskCommentDAO;
import org.eapp.poss.hbean.TaskComment;
import org.hibernate.Query;


/**
 * 产品版本数据访问对象层
 * 
 * 
 * <pre>
 * 修改日期    修改人    修改原因
 * 2013-7-17    fang    新建
 * </pre>
 */
public class TaskCommentDAO extends BaseHibernateDAO implements ITaskCommentDAO {

    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(TaskCommentDAO.class);

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public TaskComment findById(java.lang.String id) {
        log.debug("getting TaskComment instance with id: " + id);
        try {
            TaskComment instance = (TaskComment) getSession().get(TaskComment.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    /**
     * 查询taskcomment
     * 
     * @param propertyName 属性名称
     * @param value 值
     * @return list
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    @SuppressWarnings({ "unchecked", "unused" })
    private List<TaskComment> findByProperty(String propertyName, Object value) {
        log.debug("finding TaskComment instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from TaskComment as m where m." + propertyName + "= ?";
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
    @SuppressWarnings("unchecked")
    public List<TaskComment> findAll() {
        log.debug("finding all TaskComment instances");
        try {
            String queryString = "from TaskComment";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

}
