package org.eapp.poss.dao;

import java.util.List;

import org.eapp.poss.hbean.TaskComment;

/**
 * ITaskComment 数据访问对象接口
 * 
 * 
 * <pre>
 * 修改日期    修改人    修改原因
 * 2013-7-17    fang    新建
 * </pre>
 */
public interface ITaskCommentDAO extends IBaseHibernateDAO {

    /**
     * 通过主键查找 返回Engineer对象
     * 
     * @param id
     * @return TaskComment
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public TaskComment findById(java.lang.String id);

    /**
     * 查询所有数据信息
     * 
     * @return List<TaskComment>
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public List<TaskComment> findAll();
}
