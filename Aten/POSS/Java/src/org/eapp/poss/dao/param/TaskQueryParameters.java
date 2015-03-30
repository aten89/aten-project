/**
 * 
 */
package org.eapp.poss.dao.param;

import java.util.Date;

import org.eapp.util.hibernate.QueryParameters;


/**
 * 任务查询参数
 * 
 * 
 * <pre>
 * 修改日期    修改人    修改原因
 * 2013-7-17    fang    新建
 * </pre>
 */
public class TaskQueryParameters extends QueryParameters {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2818919806231533436L;

    /**
     * 设置formid
     * 
     * @param formId formId
     * 
     *            <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public void setFormId(String formId) {
        if (formId != null && !"".equals(formId)) {
            this.addParameter("formId", formId);
        }
    }

    /**
     * 获取formid
     * 
     * @return formid
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public String getFormId() {
        return (String) this.getParameter("formId");
    }

    /**
     * 设置开始时间
     * 
     * @param beginCreateTime beginCreateTime
     * 
     *            <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public void setBeginCreateTime(Date beginCreateTime) {
        if (beginCreateTime != null) {
            this.addParameter("beginCreateTime", beginCreateTime);
        }
    }

    /**
     * 获取开始时间
     * 
     * @return begincreatetime
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public Date getBeginCreateTime() {
        return (Date) this.getParameter("beginCreateTime");
    }

    /**
     * 设置结束时间
     * 
     * @param endCreateTime endCreateTime
     * 
     *            <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public void setEndCreateTime(Date endCreateTime) {
        if (endCreateTime != null) {
            this.addParameter("endCreateTime", endCreateTime);
        }
    }

    /**
     * 获取结束时间
     * 
     * @return endcreatetime
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public Date getEndCreateTime() {
        return (Date) this.getParameter("endCreateTime");
    }

    /**
     * 设置处理人
     * 
     * @param transactor transactor
     * 
     *            <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public void setTransactor(String transactor) {
        if (transactor != null && !"".equals(transactor)) {
            this.addParameter("transactor", transactor);
        }
    }

    /**
     * 获取处理人
     * 
     * @return transactor
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public String getTransactor() {
        return (String) this.getParameter("transactor");
    }

    /**
     * 设置状态
     * 
     * @param state state
     * 
     *            <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public void setStates(String[] state) {
        if (state != null && state.length > 0) {
            this.addParameter("states", state);
        }
    }

    /**
     * 获取状态
     * 
     * @return state[]
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public String[] getState() {
        return (String[]) this.getParameter("states");
    }

    // 流程类别
    /**
     * 设置流程类别
     * 
     * @param flowClass flowClass
     * 
     *            <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public void setFlowClass(String[] flowClass) {
        if (flowClass != null && flowClass.length > 0) {
            this.addParameter("flowClass", flowClass);
        }
    }

    /**
     * 获取流程类别
     * 
     * @return flowclass
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public String[] getFlowClass() {
        return (String[]) this.getParameter("flowClass");
    }

    // 流程节点
    /**
     * 设置流程节点
     * 
     * @param nodeName nodeName
     * 
     *            <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public void setNodeName(String[] nodeName) {
        if (nodeName != null && nodeName.length > 0) {
            this.addParameter("nodeName", nodeName);
        }
    }

    /**
     * 获取流程节点
     * 
     * @return nodeName
     * 
     *         <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-17    fang    新建
     * </pre>
     */
    public String[] getNodeName() {
        return (String[]) this.getParameter("nodeName");
    }
}
