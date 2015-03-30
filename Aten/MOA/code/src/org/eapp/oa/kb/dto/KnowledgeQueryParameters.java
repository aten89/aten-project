package org.eapp.oa.kb.dto;

import java.sql.Timestamp;
import java.util.List;

import org.eapp.util.hibernate.QueryParameters;


/**
 * 知识点查询参数
 * </pre>
 */
public class KnowledgeQueryParameters extends QueryParameters {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5190052232767643068L;

	/**
     * 获取标题
     * 
     * @return 标题
     */
    public String getSubject() {
        return (String) this.getParameter("subject");
    }

    /**
     * 设置标题
     * 
     * @param subject 标题
     */
    public void setSubject(String subject) {
        this.addParameter("subject", subject);
    }

    /**
     * 获取发布者
     * 
     * @return 发布者
     */
    public String getPublisher() {
        return (String) this.getParameter("publisher");
    }

    /**
     * 设置发布者
     * 
     * @param publisher 发布者
     */
    public void setPublisher(String publisher) {
        this.addParameter("publisher", publisher);
    }

    /**
     * 获取知识点分类
     * 
     * @return 知识点分类
     */
    public String getKnowledgeClass() {
        return (String) this.getParameter("knowledgeClass");
    }

    /**
     * 设置知识点分类
     * 
     * @param knowledgeClass 知识点分类
     */
    public void setKnowledgeClass(String knowledgeClass) {
        this.addParameter("knowledgeClass", knowledgeClass);
    }

    /**
     * 设置状态
     * 
     * @param status 状态
     */
    public void setStatus(Integer status) {
        this.addParameter("status", status);
    }

    /**
     * 获取状态
     * 
     * @return 状态
     */
    public Integer getStatus() {
        return (Integer) this.getParameter("status");
    }


    /**
     * 获取知识点分类ids
     * 
     * @return List<String>知识点分类ids
     */
    @SuppressWarnings("unchecked")
    public List<String> getKnowledgeClassIds() {
        return (List<String>) this.getParameter("knowledgeClassIds");
    }

    /**
     * 设置知识点分类ids
     * 
     * @param knowledgeClassIds 知识点分类ids
     */
    public void setKnowledgeClassIds(List<String> knowledgeClassIds) {
        this.addParameter("knowledgeClassIds", knowledgeClassIds);
    }

    /**
     * 设置开始修改时间
     * 
     * @param beginModifyDate 开始修改时间
     */
    public void setBeginPublishDate(Timestamp beginPublishDate) {
        this.addParameter("beginPublishDate", beginPublishDate);
    }

    /**
     * 获取开始修改时间
     * 
     * @return 开始修改时间
     */
    public Timestamp getBeginPublishDate() {
        return (Timestamp) this.getParameter("beginPublishDate");
    }
    
    /**
     * 设置开始修改时间
     * 
     * @param beginModifyDate 开始修改时间
     */
    public void setEndPublishDate(Timestamp endPublishDate) {
        this.addParameter("endPublishDate", endPublishDate);
    }

    /**
     * 获取开始修改时间
     * 
     * @return 开始修改时间
     */
    public Timestamp getEndPublishDate() {
        return (Timestamp) this.getParameter("endPublishDate");
    }
    
    public String getFirstType() {
        return (String) this.getParameter("firstType");
    }

    public void setFirstType(String firstType) {
        this.addParameter("firstType", firstType);
    }
    
    public String getSecondType() {
        return (String) this.getParameter("secondType");
    }

    public void setSecondType(String secondType) {
        this.addParameter("secondType", secondType);
    }
}
