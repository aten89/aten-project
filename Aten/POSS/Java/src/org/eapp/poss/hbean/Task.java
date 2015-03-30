package org.eapp.poss.hbean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;
import org.eapp.client.util.UsernameCache;
import org.eapp.poss.util.Tools;


/**
 * 任务实体
 * 
 * 
 * <pre>
 * 修改日期    修改人    修改原因
 * 2013-7-17    fang    新建
 * </pre>
 */
public class Task implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1724484173413827526L;
    /**
     * 流程引擎任务
     */
    public static final int TASK_TYPE_WORKFLOW = 0;
    /**
     * 外部构造的模拟任务
     */
    public static final int TASK_TYPE_OUTSIDE = 1;
    /**
     * 改动过的任务，与流程引擎任务可能不会对应
     */
    public static final int TASK_TYPE_DIRTY = 2;
    /**
     * 节点名称  
     */
    public static final String TASK_NODE_NAME = "处理";
    /**
     * 处理完毕
     */
    public static final String TASK_NODE_DONE_NAME = "处理完毕";
    /**
     * 任务结束
     */
    public static final String TASK_STATE_END = "ps_end";
    /**
     * 主键
     */
    private String id;
    /**
     * 任务实例id
     */
    private String taskInstanceId;
    /**
     * 流程实例id
     */
    private String flowInstanceId;
    /**
     * 流程key
     */
    private String flowKey;
    /**
     * 流程定义id
     */
    private String flowDefineId;
    /**
     * 任务状态
     */
    private String taskState;
    /**
     * 表单id
     */
    private String formId;
    /**
     * 处理人
     */
    private String transactor;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 审批意见
     */
    private String comment;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 流程名称
     */
    private String flowName;
    /**
     * 是否查看标识
     */
    private Boolean viewFlag;
    /**
     * 描述
     */
    private String description;
    /**
     * 流程分类
     */
    private String flowClass;
    /**
     * 任务类型
     */
    private int taskType;
    /**
     * 授权
     */
    private Set<TaskAssign> taskAssigns = new HashSet<TaskAssign>(0);
    /**
     * taskComments
     */
    private Set<TaskComment> taskComments = new HashSet<TaskComment>();
    /**
     * 流程分类名称
     */
    private transient String flowClassName;

    // Constructors

    /**
     * default constructor
     */
    public Task() {
    }

    // Property accessors
    /**
     * 处理过程　中文显示
     * 
     * @return 处理过程中文
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public String getTaskStateStr() {
        if ("ps_create".equals(taskState) || "ps_start".equals(taskState)) {
            return "处理中";
        } else if ("ps_end".equals(taskState)) {
            return "处理完毕";
        }
        return "";
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
     * @return the taskInstanceId
     */
    public String getTaskInstanceId() {
        return taskInstanceId;
    }

    /**
     * @param taskInstanceId the taskInstanceId to set
     */
    public void setTaskInstanceId(String taskInstanceId) {
        this.taskInstanceId = taskInstanceId;
    }

    /**
     * @return the flowInstanceId
     */
    public String getFlowInstanceId() {
        return flowInstanceId;
    }

    /**
     * @param flowInstanceId the flowInstanceId to set
     */
    public void setFlowInstanceId(String flowInstanceId) {
        this.flowInstanceId = flowInstanceId;
    }

    /**
     * @return the flowKey
     */
    public String getFlowKey() {
        return flowKey;
    }

    /**
     * @param flowKey the flowKey to set
     */
    public void setFlowKey(String flowKey) {
        this.flowKey = flowKey;
    }

    /**
     * @return the flowDefineId
     */
    public String getFlowDefineId() {
        return flowDefineId;
    }

    /**
     * @param flowDefineId the flowDefineId to set
     */
    public void setFlowDefineId(String flowDefineId) {
        this.flowDefineId = flowDefineId;
    }

    /**
     * @return the taskState
     */
    public String getTaskState() {
        return taskState;
    }

    /**
     * @param taskState the taskState to set
     */
    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    /**
     * @return the formId
     */
    public String getFormId() {
        return formId;
    }

    /**
     * @param formId the formId to set
     */
    public void setFormId(String formId) {
        this.formId = formId;
    }

    /**
     * @return the transactor
     */
    public String getTransactor() {
        return transactor;
    }

    /**
     * @param transactor the transactor to set
     */
    public void setTransactor(String transactor) {
        this.transactor = transactor;
    }

    /**
     * 获取处理人中文名
     * 
     * @return 处理人中文名
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public String getTransactorDisplayName() {
        String transactorName = "";
        if ((this.transactor == null) && (this.taskAssigns != null))
          for (TaskAssign assign : this.taskAssigns) {
            if (!"".equals(transactorName)) {
              transactorName = transactorName + ",";
            }
            transactorName = transactorName + assign.getAssignKey();
          }
        else {
          transactorName = UsernameCache.getDisplayName(this.transactor);
        }
        return transactorName;
      }
    

    /**
     * @return the startTime
     */
    @JSON(format = "yyyy-MM-dd HH:mm")
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the createTime
     */
    @JSON(format = "yyyy-MM-dd HH:mm")
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the endTime
     */
    @JSON(format = "yyyy-MM-dd HH:mm")
    public Date getEndTime() {
        return this.endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
     * @return the nodeName
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * @param nodeName the nodeName to set
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * @return the taskName
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @param taskName the taskName to set
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * @return the flowName
     */
    public String getFlowName() {
        return flowName;
    }

    /**
     * @param flowName the flowName to set
     */
    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    /**
     * @return the viewFlag
     */
    public Boolean getViewFlag() {
        return viewFlag;
    }

    /**
     * @param viewFlag the viewFlag to set
     */
    public void setViewFlag(Boolean viewFlag) {
        this.viewFlag = viewFlag;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the taskAssigns
     */
    @JSON(serialize = false)
    public Set<TaskAssign> getTaskAssigns() {
        return taskAssigns;
    }

    /**
     * @param taskAssigns the taskAssigns to set
     */
    public void setTaskAssigns(Set<TaskAssign> taskAssigns) {
        this.taskAssigns = taskAssigns;
    }

    /**
     * @return the flowClassName
     */
    public String getFlowClassName() {
        return flowClassName;
    }

    /**
     * @param flowClassName the flowClassName to set
     */
    public void setFlowClassName(String flowClassName) {
        this.flowClassName = flowClassName;
    }
    
    /**
     * @return the taskComments
     */
    @JSON(serialize = false)
    public Set<TaskComment> getTaskComments() {
        return taskComments;
    }

    /**
     * @param taskComments the taskComments to set
     */
    public void setTaskComments(Set<TaskComment> taskComments) {
        this.taskComments = taskComments;
    }
    
    
    public String getFlowClass() {
        return flowClass;
    }

    public void setFlowClass(String flowClass) {
        this.flowClass = flowClass;
    }

    
    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    /**
     * 响应时间:开始处理时间-创建时间
     * 
     * @return 响应时间
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public String getResponseTime() {
        if (startTime == null) {
            return "";
        }
        double responseTime_ = ((this.startTime == null ? 0 : this.startTime.getTime()) - (this.createTime == null ? 0
                : this.createTime.getTime())) * 1.0 / 60000;
        if (responseTime_ != 0 && responseTime_ < 1) {
            return "小于1分钟";
        }
        if (((this.startTime == null ? 0 : this.startTime.getTime()) - (this.createTime == null ? 0 : this.createTime
                .getTime())) * 1.0 % 60000 == 0) {
            responseTime_ += 1;
        }
        return Tools.formatMinute((int) responseTime_);
    }

    /**
     * 任务处理时间:处理完成时间-创建时间
     * 
     * @return 任务处理时间
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public String getDealTime() {
        if (endTime == null || createTime == null) {
            return "";
        }

        double dealTime_ = (this.endTime.getTime() - this.createTime.getTime()) * 1.0 / 60000;
        if (dealTime_ != 0 && dealTime_ < 1) {
            return "小于1分钟";
        }
        if ((this.endTime.getTime() - this.createTime.getTime()) * 1.0 % 60000 != 0) {
            dealTime_ += 1;// 有余数+1分钟
        }
        return Tools.formatMinute((int) dealTime_);
    }

    /*
     * 
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /*
     * 
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Task other = (Task) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
}