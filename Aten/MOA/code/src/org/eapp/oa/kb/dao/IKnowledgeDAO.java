package org.eapp.oa.kb.dao;

import java.util.List;

import org.eapp.oa.kb.dao.IKnowledgeDAO;
import org.eapp.oa.kb.dto.KnowledgeQueryParameters;
import org.eapp.oa.kb.hbean.Knowledge;
import org.eapp.util.hibernate.ListPage;

import org.eapp.oa.system.dao.IBaseHibernateDAO;

/**
 * 知识点DAO
 * 
 */
public interface IKnowledgeDAO extends IBaseHibernateDAO {

    /**
     * 通过主键查找 返回Knowledge对象
     * 
     * @param id 知识点id
     * @return 知识点
     */
    Knowledge findById(String id);

    /**
     * 通过例子找知识点
     * 
     * @param instance 知识点例子
     * @return 知识点
     */
    List<Knowledge> findByExample(Knowledge instance);

    /**
     * 通过字段查找知识点
     * 
     * @param propertyName 字段名称
     * @param value 字段值
     * @return 知识点列表对象
     */
    List<Knowledge> findByProperty(String propertyName, Object value);

    /**
     * 查询所有数据信息
     * 
     * @return List<Knowledge>结果集
     */
    List<Knowledge> findAll();

    /**
     * 翻页根据条件查询
     * 
     * @param kqp 条件查询类
     * @return ListPage<Knowledge>结果集
     */
    ListPage<Knowledge> queryKnowledge(KnowledgeQueryParameters kqp);

    /**
     * 子节点个数
     * 
     * @param classId classId
     * @return 子节点个数
     */
    public int getCountByKnowledgeClass(String classId);


    /**
     * 找该知识点类别下第一级子类别
     * 
     * @param KnowledgeClassId 知识点类别id
     * @return 知识点类别List
     */
    public List<String> iteratorKnowledgeClassList(String KnowledgeClassId);


}
