package org.eapp.oa.kb.hbean;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eapp.client.util.UsernameCache;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.rpc.dto.DataDictInfo;


/**
 * Knowledge 知识
 */
public class Knowledge implements java.io.Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1917011933726411816L;

    /**
     * //临时
     */
    public static final int STATUS_TEMP = 0;
    /**
     * /正式
     */
    public static final int STATUS_FINAL = 1;
   
    /**
     * //内容地址
     */
    public static final int DISPLAYMODE_ADDR = 0;
    /**
     * //直接显示内容
     */
    public static final int DISPLAYMODE_CONTENT = 1;
    /**
     * //默认
     */
    public static final int DISPLAYMODE_DEF = 2;
    /**
     * 0
     */
    public static final int PROPERTY_COMM = 0;
    /**
     * 1
     */
    public static final int PROPERTY_NO_REPLY = 1;
    /**
     * 2
     */
    public static final int PROPERTY_READONLY = 2;

    /** 主键 */
    private String id;
    /** 知识类别 */
    private KnowledgeClass knowledgeClass;
    /** 标题 */
    private String subject;
    /** 标签 */
    private String labels;
    /** 概述 */
    private String remark;
    /** 内容 */
    private String content;
    /** 内容地址 */
    private String contentUrl;
    /** 显示模式 0：内容地址， 1：直接显示内容， 2:默认 */
    private Integer displayMode;
    /** 发布人机构 */
    private String groupName;
    /** 发布人 */
    private String publisher;
    /** 发布时间 */
    private Date publishDate;
    /** 知识属性 0：普通 1：不可回复 2：只读 */
    private Integer property;
    /** 状态0：收集中1：正式 */
    private Integer status;
    /**
     * 修改时间
     */
    private Date modifyDate;
    /** 一级分类 */
    private String firstType;
    /** 二级分类 */
    private String secondType;
    /**
     * knowledgeAttachments
     */
    private Set<Attachment> knowledgeAttachments = new HashSet<Attachment>(0);
    /**
     * contentImgAttachments
     */
    private Set<Attachment> contentImgAttachments = new HashSet<Attachment>(0);
    /**
     * 知识点回复
     */
    private Set<KnowledgeReply> replys = new HashSet<KnowledgeReply>(0);

    /**
     * /搜索匹配的内容
     */
    private transient String matchSubject;
    /**
     * //搜索匹配的内容
     */
    private transient String matchText;
    /**
     * //搜索匹配的类别名称
     */
    private transient String matchClassPathName;
    
    /**
     * 构造方法
     */
    public Knowledge() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public KnowledgeClass getKnowledgeClass() {
        return knowledgeClass;
    }

    public void setKnowledgeClass(KnowledgeClass knowledgeClass) {
        this.knowledgeClass = knowledgeClass;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Integer getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(Integer displayMode) {
        this.displayMode = displayMode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPublisher() {
        return publisher;
    }

    /**
     * 获取起草人名字
     * </pre>
     */
    public String getPublisherName() {
        return UsernameCache.getDisplayName(publisher);
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Integer getProperty() {
        return property;
    }

    public void setProperty(Integer property) {
        this.property = property;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Set<Attachment> getKnowledgeAttachments() {
        return knowledgeAttachments;
    }

    public void setKnowledgeAttachments(Set<Attachment> knowledgeAttachments) {
        this.knowledgeAttachments = knowledgeAttachments;
    }

    public Set<Attachment> getContentImgAttachments() {
        return contentImgAttachments;
    }

    public void setContentImgAttachments(Set<Attachment> contentImgAttachments) {
        this.contentImgAttachments = contentImgAttachments;
    }


    /**
     * 取知识点类别名称
     */
    public String getKnowledgeClassName() {
        if (knowledgeClass == null) {
            return "";
        }
        return this.knowledgeClass.getName();
    }

    /**
     * getClassPathName
     */
    public String getClassPathName() {
        StringBuffer sb = new StringBuffer();
        getClassPathName(sb, getKnowledgeClass());
        if (sb.length() > 0) {
            sb.delete(sb.length() - 4, sb.length());
        }
        return sb.toString();
    }

    /**
     * 取类别路径名称
     */
    private void getClassPathName(StringBuffer sb, KnowledgeClass dbc) {
        if (dbc.getParentClass() != null) {
            getClassPathName(sb, dbc.getParentClass());
        }
        sb.append(dbc.getName()).append(" -> ");
    }

    public Set<KnowledgeReply> getReplys() {
        return replys;
    }

    public void setReplys(Set<KnowledgeReply> replys) {
        this.replys = replys;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getFirstType() {
        return firstType;
    }

    public String getSecondType() {
        return secondType;
    }

    public void setFirstType(String firstType) {
        this.firstType = firstType;
    }

    public void setSecondType(String secondType) {
        this.secondType = secondType;
    }
    
    public String getFirstTypeName() {
    	Map<String ,DataDictInfo> areaMap = SysCodeDictLoader.getInstance().getFirstTypeMap();
		if(areaMap != null) {
			DataDictInfo dict = areaMap.get(this.firstType);
			if(dict != null) {
				String dictKey = dict.getDictName();
				if(dictKey != null) {
					return dictKey;
				}
			}
		}
		return "";
    }

    public String getSecondTypeName() {
    	Map<String ,DataDictInfo> areaMap = SysCodeDictLoader.getInstance().getSecondTypeMap();
		if(areaMap != null) {
			DataDictInfo dict = areaMap.get(this.secondType);
			if(dict != null) {
				String dictKey = dict.getDictName();
				if(dictKey != null) {
					return dictKey;
				}
			}
		}
		return "";
    }

	public String getMatchSubject() {
		return matchSubject;
	}

	public void setMatchSubject(String matchSubject) {
		this.matchSubject = matchSubject;
	}

	public String getMatchText() {
		return matchText;
	}

	public void setMatchText(String matchText) {
		this.matchText = matchText;
	}

	public String getMatchClassPathName() {
		return matchClassPathName;
	}

	public void setMatchClassPathName(String matchClassPathName) {
		this.matchClassPathName = matchClassPathName;
	}
    
    
}
