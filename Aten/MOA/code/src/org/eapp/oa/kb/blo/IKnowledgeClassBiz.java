/**
 * 
 */
package org.eapp.oa.kb.blo;

import java.util.List;

import org.eapp.oa.kb.hbean.KnowledgeClass;
import org.eapp.oa.system.exception.OaException;


/**
 * 知识分类权限业务层
 */
public interface IKnowledgeClassBiz {

    /**
     * 取得有权限的分类
     * 
     * @param userAccountId 用户账号
     * @param groupNames 部门名称
     * @param postNames 岗位名称
     * @param flag 标识
     * @return List<KnowledgeClass>知识分类list
     */
    public List<KnowledgeClass> getAssignClass(String userAccountId, List<String> groupNames, List<String> postNames,
            int flag);

    /**
     * 取得有查询权限的分类
     * 
     * @param userAccountId 用户账号
     * @param groupNames 部门名称
     * @param postNames 岗位名称
     * @return List<String>权限list
     */
    public List<String> getSearchableClassId(String userAccountId, List<String> groupNames, List<String> postNames);

    /**
     * 取得用户在某分类下的权限值
     * 
     * @param userAccountId 用户账号
     * @param groupNames 部门名称
     * @param postNames 岗位名称
     * @param classId 分类id
     * @return List<Integer> 权限值list
     */
    public List<Integer> getKnowledgeClassRight(String userAccountId, List<String> groupNames, List<String> postNames,
            String classId);

    /**
     * 新增或节点
     * 
     * @param parentId 父节点id
     * @param nodeText 节点名称
     * @param description 描述
     * @return KnowledgeClass知识点分类
     * @throws OaException oa异常
     */
    public KnowledgeClass txAddKnowledgeClass(String parentId, String nodeText, String description) throws OaException;

    /**
     * 修改节点
     * 
     * @param nodeId 节点id
     * @param nodeText 节点名称
     * @param description 描述
     * @return 知识点分类
     * @throws OaException oa异常
     */
    public KnowledgeClass txModifyKnowledgeClass(String nodeId, String nodeText, String description) throws OaException;

    /**
     * 通过节点id查找节点信息
     * 
     * @param id 知识点分类id
     * @return 知识点分类
     */
    public KnowledgeClass getKnowledgeClassById(String id);

    /**
     * 删除节点
     * 
     * @param id 知识点分类id
     * @throws OaException oa异常
     */
    public void txDelKnowledgeClass(String id) throws OaException;

    /**
     * 移动节点
     * 
     * @param id 知识点分类id
     * @param targetId 目标id
     * @param moveStatus moveStatus
     */
    public void txMoveKnowledgeClassNode(String id, String targetId, int moveStatus);

    /**
     * 获取所有知识点分类列表
     * 
     * @return 知识点分类列表 List<KnowledgeClass>
     */
    List<KnowledgeClass> getAllClass();

    /**
     * 获取该分类下所有子分类
     * 
     * @param id
     * @return 知识类别 list
     */
    List<KnowledgeClass> getAllChildById(String id);

    /**
     * 取该知识类别的 父 知识类别 ,并 按 父->子 层级排序.
     * 
     * @param list 待返回的list
     * @param knowledgeClass 知识类别
     * @param includeFlag 是否包含自身. true:包含,false:不包含
     * @return
     */
    List<KnowledgeClass> getFullClassByKnowledgeClass(List<KnowledgeClass> list, KnowledgeClass knowledgeClass,
            boolean includeFlag);
}
