package org.eapp.oa.kb.dao;

import java.util.List;

import org.eapp.oa.kb.hbean.KnowledgeClass;

import org.eapp.oa.system.dao.IBaseHibernateDAO;

/**
 * 知识类别DAO接口
 */
public interface IKnowledgeClassDAO extends IBaseHibernateDAO {

    /**
     * 通过主键查找 返回Knowledgeclass对象
     * 
     * @param id 知识类别id
     * @return 知识类别
     */
    KnowledgeClass findById(String id);

    /**
     * findByExample
     * 
     * @param instance instance
     * @return 知识类别list
     */
    List<KnowledgeClass> findByExample(KnowledgeClass instance);

    /**
     * 通过字段查找 返回List<Knowledgeclass>对象
     * 
     * @param propertyName 字段
     * @param value 值
     * @return List<Knowledgeclass>对象
     */
    List<KnowledgeClass> findByProperty(String propertyName, Object value);

    /**
     * 查询所有数据信息 返回List<Knowledgeclass>
     * 
     * @return List<Knowledgeclass>
     */
    List<KnowledgeClass> findAll();

    /**
     * 取得用户有权限的分类
     * 
     * @param userAccountId 用户帐号
     * @param groupNames 用户组
     * @param postNames postNames
     * @param flag 标识
     * @return 知识点分类
     */
    List<KnowledgeClass> findAssignClass(String userAccountId, List<String> groupNames, List<String> postNames, int flag);

    /**
     * 取得用户有查询权限的分类
     * 
     * @param userAccountId 用户帐号
     * @param groupNames 分组
     * @param postNames postNames
     * @return String List
     */
    List<String> findSearchableClassId(String userAccountId, List<String> groupNames, List<String> postNames);

    /**
     * 修改排序
     * 
     * @param parentKnowClass 父类别
     * @param moveStatus 移动状态?
     * @param oldOrder 旧排序
     */
    void updateOrder(KnowledgeClass parentKnowClass, int moveStatus, int oldOrder);

    /**
     * 查询最大的排序值
     * 
     * @param parentKnowClassId 父模块id
     * @return 最大的排序值
     */
    int getMaxOrder(String parentKnowClassId);

    /**
     * 取得用户在某分类的权限值
     * 
     * @param userAccountId 用户帐号
     * @param groupNames 分组名
     * @param postNames postNames
     * @param classId 类别id
     * @return 分类的权限值
     */
    List<Integer> findKnowledgeClassRight(String userAccountId, List<String> groupNames, List<String> postNames,
            String classId);

    /**
     * 查询同级节点下的有相同名称的个数
     * 
     * @param parentId 父id
     * @param text 名称
     * @return 个数 int
     */
    int getNodeNumByName(String parentId, String text);

    /**
     * 取该类别下的子类别
     * 
     * @param id 父类别id
     * @return 知识类别list
     */
    List<KnowledgeClass> getChildList(String id);

    /**
     * 查找所有分类
     * 
     * @return 返回所有知识点分类list List<KnowledgeClass>
     */
    List<KnowledgeClass> findAllClass();

    /**
     * 获取该id分类下所有分类
     * 
     * @param id
     * @return 知识点分类list
     * 
     */
    List<KnowledgeClass> getAllChildList(String id);

    /**
     * 根据 父字段,本身字段查找知识类别
     * 
     * @param parentPropertyName 父字段名
     * @param parentValue 父值
     * @param pLikeFlag 是否使用like查询
     * @param propertyName 自身字段名
     * @param value 自身值
     * @param likeFlag 是否使用like查询
     * @return 知识类别
     */
    KnowledgeClass getByParentPropertyAndThisProperty(String parentPropertyName, Object parentValue, boolean pLikeFlag,
            String propertyName, Object value, boolean likeFlag);
}
