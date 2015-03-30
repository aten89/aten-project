package org.eapp.oa.kb.blo;

import java.util.List;

import org.eapp.oa.kb.hbean.KnowledgeClassAssign;


/**
 * 知识分类 权限 业务逻辑 接口
 * 
 * 
 * <pre>
 * 修改日期	修改人		修改原因
 * 2012-11-26	卢凯宁		修改注释
 * </pre>
 */
public interface IKnowledgeClassAssignBiz {
    /**
     * 获取已绑定的用户
     * 
     * @param id id
     * @return 知识分类权限 list
     * 
     *         <pre>
     * 修改日期	修改人		修改原因
     * 2012-11-26	卢凯宁		修改注释
     * </pre>
     */
    List<KnowledgeClassAssign> getBindingUsers(String id);

    /**
     * 获取已绑定的机构
     * 
     * @param id 知识分类权限id
     * @return 知识分类权限 list
     * 
     *         <pre>
     * 修改日期	修改人		修改原因
     * 2012-11-26	卢凯宁		修改注释
     * </pre>
     */
    List<KnowledgeClassAssign> getBindingGroups(String id);

    /**
     * 获取已绑定的职位
     * 
     * @param id 知识分类权限id
     * @return 知识分类权限 list
     * 
     *         <pre>
     * 修改日期	修改人		修改原因
     * 2012-11-26	卢凯宁		修改注释
     * </pre>
     */
    List<KnowledgeClassAssign> getBindingPosts(String id);

    /**
     * 绑定用户
     * 
     * @param id 知识类别id
     * @param userAssigns 待绑定的权限
     * 
     *            <pre>
     * 修改日期	修改人		修改原因
     * 2012-11-26	卢凯宁		修改注释
     * </pre>
     */
    void txBindingUsers(String id, String[] userAssigns);

    /**
     * 绑定职位
     * 
     * @param id 知识类别id
     * @param postAssigns 待绑定的权限
     * 
     *            <pre>
     * 修改日期	修改人		修改原因
     * 2012-11-26	卢凯宁		修改注释
     * </pre>
     */
    void txBindingPosts(String id, String[] postAssigns);

    /**
     * 绑定机构
     * 
     * @param id 知识类别id
     * @param postAssigns 待绑定的权限
     * 
     *            <pre>
     * 修改日期	修改人		修改原因
     * 2012-11-26	卢凯宁		修改注释
     * </pre>
     */
    void txBindingGroups(String id, String[] postAssigns);

    /**
     * 同步权限; 并且 若有 强制评分配置权限,也 同步 强制评分配置
     * 
     * @param id 知识类别id
     */
    void txSynAssign(String id);

}
