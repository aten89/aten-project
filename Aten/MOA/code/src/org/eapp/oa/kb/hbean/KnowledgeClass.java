package org.eapp.oa.kb.hbean;

import java.util.HashSet;
import java.util.Set;

import org.eapp.oa.system.util.Tree.Node;

/**
 * BudgetList entity. Description:知识分类
 * 
 * @author MyEclipse Persistence Tools
 */

public class KnowledgeClass implements Node {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3201469921880066528L;
    /**
     * 正式库ID
     */
    public static final String FINAL_KB_ID = "final_knowledge_id";
    /**
     * 临时库ID
     */
    public static final String TEMP_KB_ID = "temp_knowledge_id";

    /**
     * MOVESTATUS_UP
     */
    public static int MOVESTATUS_UP = 0;
    /**
     * MOVESTATUS_DOWN
     */
    public static int MOVESTATUS_DOWN = 1;
    /**
     * MOVESTATUS_FIST
     */
    public static int MOVESTATUS_FIST = 2;
    /**
     * 移到最后
     */
    public static int MOVESTATUS_LAST = 3;

    /** 主键 */
    private String id;
    /** 父ID */
    private KnowledgeClass parentClass;
    /** 名称 */
    private String name;
    /** 排序 */
    private Integer displayOrder;
    /** 层级 */
    private Integer treeLevel;
    /** 描述 */
    private String description;
    /** 授权 */
    private Set<KnowledgeClassAssign> assigns = new HashSet<KnowledgeClassAssign>(0);
    /**
     * 子类别
     */
    private Set<KnowledgeClass> childClsases = new HashSet<KnowledgeClass>(0);
//    /**
//     * 是否强制评分标识
//     */
//    private boolean forcedScoreFlag = false;

    /**
     * 类别全称
     */
//    private transient String classFullName;

    public KnowledgeClass() {
    }

    // 生成get/set方法
    /**
     * 获取 id
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置 id
     * 
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取 parentClass
     * 
     * @return the parentClass
     */
    public KnowledgeClass getParentClass() {
        return parentClass;
    }

    /**
     * 设置 parentClass
     * 
     * @param parentClass the parentClass to set
     */
    public void setParentClass(KnowledgeClass parentClass) {
        this.parentClass = parentClass;
    }

    /**
     * 获取 name
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置 name
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取 displayOrder
     * 
     * @return the displayOrder
     */
    public Integer getDisplayOrder() {
        return displayOrder;
    }

    /**
     * 设置 displayOrder
     * 
     * @param displayOrder the displayOrder to set
     */
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    /**
     * 获取 treeLevel
     * 
     * @return the treeLevel
     */
    public Integer getTreeLevel() {
        return treeLevel;
    }

    /**
     * 设置 treeLevel
     * 
     * @param treeLevel the treeLevel to set
     */
    public void setTreeLevel(Integer treeLevel) {
        this.treeLevel = treeLevel;
    }

    /**
     * 获取 description
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置 description
     * 
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取 assigns
     * 
     * @return the assigns
     */
    public Set<KnowledgeClassAssign> getAssigns() {
        return assigns;
    }

    /**
     * 设置 assigns
     * 
     * @param assigns the assigns to set
     */
    public void setAssigns(Set<KnowledgeClassAssign> assigns) {
        this.assigns = assigns;
    }

    /**
     * 获取 childClsases
     * 
     * @return the childClsases
     */
    public Set<KnowledgeClass> getChildClsases() {
        return childClsases;
    }

    /**
     * 设置 childClsases
     * 
     * @param childClsases the childClsases to set
     */
    public void setChildClsases(Set<KnowledgeClass> childClsases) {
        this.childClsases = childClsases;
    }

    /**
     * 获取 classFullName
     * 
     * @return the classFullName
     */
    public String getClassFullName() {
    	StringBuffer name = new StringBuffer();
        parentClassName(this, name);
        return name.toString();
    }
    
    private void parentClassName(KnowledgeClass c, StringBuffer n) {
    	if (c == null) {
    		return;
    	}
    	if (c.getParentClass() != null) {
    		parentClassName(c.getParentClass(), n);
    	}
    	if (n.length() > 0) {
    		n.append(" > ");
    	}
    	n.append(c.getName());
    }

//    /**
//     * 设置 classFullName
//     * 
//     * @param classFullName the classFullName to set
//     */
//    public void setClassFullName(String classFullName) {
//        this.classFullName = classFullName;
//    }

    @Override
    public Node getParentNode() {
        return parentClass;
    }

//    /**
//     * 获取 forcedScoreFlag
//     * @return the forcedScoreFlag
//     */
//    public boolean isForcedScoreFlag() {
//        return forcedScoreFlag;
//    }
//
//    /**
//     * 设置 forcedScoreFlag
//     * @param forcedScoreFlag the forcedScoreFlag to set
//     */
//    public void setForcedScoreFlag(boolean forcedScoreFlag) {
//        this.forcedScoreFlag = forcedScoreFlag;
//    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final KnowledgeClass other = (KnowledgeClass) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int compareTo(Node o) {
        KnowledgeClass kc = (KnowledgeClass) o;
        if (getDisplayOrder() > kc.getDisplayOrder()) {
            return 1;
        } else if (getDisplayOrder() == kc.getDisplayOrder()) {
            return 0;
        } else {
            return -1;
        }
    }

}
