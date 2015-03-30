package org.eapp.poss.hbean;

// default package

import java.util.Date;

import org.eapp.client.util.UsernameCache;


/**
 * TaskComment
 * 
 * 
 * <pre>
 * 修改日期    修改人    修改原因
 * 2013-7-17    fang    新建
 * </pre>
 */
public class TaskComment implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2735285716378272112L;
    /**
     * 保存标志 否
     */
    public static final int FLAG_UNSAVE = 0;
    /**
     * 保存标志 是
     */
    public static final int FLAG_SAVED = 1;
    /**
     * 任务说明
     */
    public static final int DATA_TYPE_TASK_COMM = 0;
    /**
     * 表单备注
     */
    public static final int DATA_TYPE_FORM_REMARK = 1;
    /**
     * 步骤添加备注
     */
    public static final String ADD_REMARK = "步骤添加备注";
    
    /**
     * 主键
     */
    private String id;
    /**
     * 任务
     */
    private Task task;
    /**
     * 任务分类
     */
    private String taskClass;
    /**
     * 评论
     */
    private String comment;
    /**
     * 添加事件
     */
    private Date addtime;
    /**
     * 暂时保存标志
     */
    private Integer flag;// 0:暂存;1:已提交
    /**
     * 创建人
     */
    private String creator;
    /**
     * 数据类型
     */
    private Integer dataType;

    // Constructors

    /**
     * default constructor
     */
    public TaskComment() {
    }

    // Property accessors

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the task
     */
    public Task getTask() {
        return task;
    }

    /**
     * @param task the task to set
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * @return the taskClass
     */
    public String getTaskClass() {
        return taskClass;
    }

    /**
     * @param taskClass the taskClass to set
     */
    public void setTaskClass(String taskClass) {
        this.taskClass = taskClass;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the addtime
     */
    public Date getAddtime() {
        return addtime;
    }

    /**
     * @param addtime the addtime to set
     */
    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    /**
     * @return the flag
     */
    public Integer getFlag() {
        return flag;
    }

    /**
     * @param flag the flag to set
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    /**
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * @return the dataType
     */
    public Integer getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    /**
     * @return the creatorName
     */
    public String getCreatorName() {
        return UsernameCache.getDisplayName(this.creator);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TaskComment other = (TaskComment) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}