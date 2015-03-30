package org.eapp.oa.kb.hbean;

import java.util.Date;

import org.eapp.client.util.UsernameCache;


/**
 * 知识点日志实体
 * 
 * <pre>
 * 修改日期	              修改人		修改原因
 * 2012-8-10		卢凯宁		修改注释
 * </pre>
 */
public class KnowledgeLog implements java.io.Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1297661313764411641L;
    /**
     * 新增
     */
    public static final String TYPE_ADD = "新增";
    /**
     * 修改
     */
    public static final String TYPE_MODIFY = "修改";
    /**
     * 删除
     */
    public static final String TYPE_DELETE = "删除";

    // Fields
    /**
     * 主键id
     */
    private String id;
    /**
     * 用户id
     */
    private String userid;
    /**
     * 日志类型
     */
    private String type;
    /**
     * 知识点id
     */
    private String knowledgeid;
    /**
     * 知识点标题
     */
    private String knowledgetitle;
    /**
     * 记录时间
     */
    private Date operatetime;

    // Constructors

    /**
     * 默认构造函数
     */
    public KnowledgeLog() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKnowledgeid() {
        return this.knowledgeid;
    }

    public void setKnowledgeid(String knowledgeid) {
        this.knowledgeid = knowledgeid;
    }

    public String getKnowledgetitle() {
        return this.knowledgetitle;
    }

    public void setKnowledgetitle(String knowledgetitle) {
        this.knowledgetitle = knowledgetitle;
    }

    public Date getOperatetime() {
        return operatetime;
    }

    public void setOperatetime(Date operatetime) {
        this.operatetime = operatetime;
    }

    /**
     * 获取用户名
     * 
     * @return 用户名称
     * 
     *         <pre>
     * 修改日期	              修改人		修改原因
     * 2012-8-10		卢凯宁		修改日志
     * </pre>
     */
    public String getUserName() {
        return UsernameCache.getDisplayName(userid);
    }
}