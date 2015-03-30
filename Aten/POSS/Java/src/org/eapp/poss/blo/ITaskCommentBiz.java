/**
 * 
 */
package org.eapp.poss.blo;

import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.TaskComment;

/**
 * TaskCommentservice接口
 * 
 * <pre>
 * 修改日期    修改人    修改原因
 * 2013-7-17    fang    新建
 * </pre>
 */
public interface ITaskCommentBiz {
    /**
     * 提交处理说明
     * 
     * @param commitFlag 0:暂存;1:提交
     * @param id id
     * @param tiid tiid
     * @param comment comment
     * @param taskClass 分类
     * @return
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public TaskComment txCommitTaskComment(int commitFlag, String id, String tiid, String comment, String taskClass);
    
    /**
     * 根据commId获取TaskComment
     * 
     * @param commId commId
     * @return TaskComment
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public TaskComment getTaskCommById(String commId);

    /**
     * 添加、修改任务备注
     * 
     * @param id 任务备注ID
     * @param tiid 流程实例ID
     * @param comment 备注内容
     * @param taskClass 分类
     * @param userAccountId 创建者
     * @return 任务备注说明
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2013-7-30	李海根	新建
     * </pre>
     */
    public TaskComment txCommitTaskCommentRemark(String id, String tiid, String comment, String taskClass,
            String userAccountId);

    /**
     * 删除备注
     * 
     * @param id 备注ID
     * @throws CITException 异常
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2013-7-30	李海根	新建
     * </pre>
     */
    public void txDelTaskCommentById(String id) throws PossException;
}
