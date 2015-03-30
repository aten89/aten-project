package org.eapp.poss.hbean;

import org.eapp.client.util.UsernameCache;

// default package

/**
 * TaskAssign实体
 * 
 * 
 * <pre>
 * 修改日期    修改人    修改原因
 * 2013-7-17    fang    新建
 * </pre>
 */
public class TaskAssign implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8502217287836600917L;
    /**
     * 用户授权
     */
    public static final int TYPE_USER = 0;
    // 用户授权
    /**
     * 角色授权
     */
    public static final int TYPE_ROLE = 1;
    // 角色授权
    /**
     * 主键
     */
    private String id;
    /**
     * 任务
     */
    private Task task;
    /**
     * 类型
     */
    private int type;
    /**
     * 授权key
     */
    private String assignKey;

    // Constructors

    /** default constructor */
    public TaskAssign() {
    }

    /** full constructor */
    public TaskAssign(Task task, int type, String assignKey) {
        this.task = task;
        this.type = type;
        this.assignKey = assignKey;
    }

    // Property accessors

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
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 获取处理人中文名
     * 
     * @return 处理人中文名
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-18    fang    新建
     * </pre>
     */
    public String getTransactorDisplayName() {
        if (this.type == TYPE_USER && assignKey != null) {
            return UsernameCache.getDisplayName(assignKey);
        }
        return assignKey;
    }

    /**
     * @return the assignKey
     */
    public String getAssignKey() {
        return assignKey;
    }

    /**
     * @param assignKey the assignKey to set
     */
    public void setAssignKey(String assignKey) {
        this.assignKey = assignKey;
    }

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
        final TaskAssign other = (TaskAssign) obj;
        if (id == null) {
            // if (other.id != null)
            return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}