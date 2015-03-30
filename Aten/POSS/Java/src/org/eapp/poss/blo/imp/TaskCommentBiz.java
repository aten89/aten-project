/**
 * 
 */
package org.eapp.poss.blo.imp;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.eapp.poss.blo.ITaskCommentBiz;
import org.eapp.poss.dao.ITaskCommentDAO;
import org.eapp.poss.dao.ITaskDAO;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Task;
import org.eapp.poss.hbean.TaskComment;


/**
 * TaskCommentService实现
 * 
 * 
 * <pre>
 * 修改日期    修改人    修改原因
 * 2013-7-17    fang    新建
 * </pre>
 */
public class TaskCommentBiz implements ITaskCommentBiz {
    /**
     * taskCommentDAO
     */
    private ITaskCommentDAO taskCommentDAO;
    /**
     * taskDAO
     */
    private ITaskDAO taskDAO;

    /*
     * (non-Javadoc)
     * 
     * java.lang.String, java.lang.String)
     */
    @Override
    public TaskComment txCommitTaskComment(int commitFlag, String id, String tiid, String comment, String taskClass) {
        if (tiid == null || tiid.length() == 0) {
            throw new IllegalArgumentException();
        }
        TaskComment comm;
        if (id == null || "".equals(id)) {
            comm = new TaskComment();
        } else {
            comm = taskCommentDAO.findById(id);
        }

        comm.setFlag(commitFlag);
        comm.setAddtime(new Date());
        comm.setComment(comment);
        comm.setTaskClass(taskClass);
        comm.setDataType(TaskComment.DATA_TYPE_TASK_COMM);
        Task task = taskDAO.findByTaskInstanceId(tiid);
        comm.setTask(task);
        taskCommentDAO.saveOrUpdate(comm);
        return comm;
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public TaskComment getTaskCommById(String commId) {
        if (StringUtils.isEmpty(commId)) {
            throw new IllegalArgumentException("参数错误：commId为空");
        }
        return taskCommentDAO.findById(commId);
    }

    @Override
    public TaskComment txCommitTaskCommentRemark(String id, String tiid, String comment, String taskClass,
            String userAccountId) {
        if (tiid == null || tiid.length() == 0) {
            throw new IllegalArgumentException();
        }
        TaskComment comm;
        if (StringUtils.isEmpty(id)) {
            comm = new TaskComment();
        } else {
            comm = taskCommentDAO.findById(id);
        }
        comm.setFlag(1);
        if (StringUtils.isEmpty(id)) {
            comm.setAddtime(new Date());
        }
        Task task = taskDAO.findByTaskInstanceId(tiid);
        comm.setTask(task);
        comm.setComment(comment);
        comm.setTaskClass(taskClass);
        comm.setCreator(userAccountId);
        comm.setDataType(TaskComment.DATA_TYPE_FORM_REMARK);
        taskCommentDAO.saveOrUpdate(comm);
        return comm;
    }

    @Override
    public void txDelTaskCommentById(String id) throws PossException {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("参数错误：commId为空");
        }
        TaskComment comm = taskCommentDAO.findById(id);
        if (comm == null) {
            throw new PossException("备注不存在");
        }
        taskCommentDAO.delete(comm);
    }
      

    /**
     * 设置 taskCommentDAO
     * 
     * @param taskCommentDAO the taskCommentDAO to set
     */
    public void setTaskCommentDAO(ITaskCommentDAO taskCommentDAO) {
        this.taskCommentDAO = taskCommentDAO;
    }

    /**
     * 设置 taskDAO
     * 
     * @param taskDAO the taskDAO to set
     */
    public void setTaskDAO(ITaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

 

}