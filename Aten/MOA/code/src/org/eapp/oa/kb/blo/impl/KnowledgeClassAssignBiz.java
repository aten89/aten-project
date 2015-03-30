package org.eapp.oa.kb.blo.impl;

import java.util.List;
import java.util.Set;

import org.eapp.oa.kb.blo.IKnowledgeClassAssignBiz;
import org.eapp.oa.kb.dao.IKnowledgeClassAssignDAO;
import org.eapp.oa.kb.dao.IKnowledgeClassDAO;
import org.eapp.oa.kb.hbean.KnowledgeClass;
import org.eapp.oa.kb.hbean.KnowledgeClassAssign;


/**
 * 知识分类权限 业务逻辑实现
 * 
 * 
 * <pre>
 * 修改日期	修改人		修改原因
 * 2012-11-26	卢凯宁		修改注释
 * </pre>
 */
public class KnowledgeClassAssignBiz implements IKnowledgeClassAssignBiz {

    /**
     * 知识分类权限 DAO 访问接口
     */
    private IKnowledgeClassAssignDAO knowledgeClassAssignDAO;
    /**
     * 知识类别 DAO 访问接口
     */
    private IKnowledgeClassDAO knowledgeClassDAO;

    /**
     * 设置 knowledgeClassAssignDAO
     * 
     * @param knowledgeClassAssignDAO the knowledgeClassAssignDAO to set
     */
    public void setKnowledgeClassAssignDAO(IKnowledgeClassAssignDAO knowledgeClassAssignDAO) {
        this.knowledgeClassAssignDAO = knowledgeClassAssignDAO;
    }

    /**
     * 设置 knowledgeClassDAO
     * 
     * @param knowledgeClassDAO the knowledgeClassDAO to set
     */
    public void setKnowledgeClassDAO(IKnowledgeClassDAO knowledgeClassDAO) {
        this.knowledgeClassDAO = knowledgeClassDAO;
    }

    @Override
    public List<KnowledgeClassAssign> getBindingUsers(String id) {
        if (id == null) {
            return null;
        } else {
            return knowledgeClassAssignDAO.findBindById(id, KnowledgeClassAssign.TYPE_USER);
        }
    }

    @Override
    public List<KnowledgeClassAssign> getBindingGroups(String id) {
        if (id == null) {
            return null;
        } else {
            return knowledgeClassAssignDAO.findBindById(id, KnowledgeClassAssign.TYPE_GROUP);
        }
    }

    @Override
    public List<KnowledgeClassAssign> getBindingPosts(String id) {
        if (id == null) {
            return null;
        } else {
            return knowledgeClassAssignDAO.findBindById(id, KnowledgeClassAssign.TYPE_POST);
        }
    }

    @Override
    public void txBindingUsers(String id, String[] userAssigns) {
        if (id == null) {
            throw new IllegalArgumentException("非法参数：id不能为空！");
        }
        KnowledgeClass knowClass = knowledgeClassDAO.findById(id);
        if (knowClass == null) {
            throw new IllegalArgumentException("类别不存在!");
        }
        knowledgeClassAssignDAO.delBindById(id, KnowledgeClassAssign.TYPE_USER);
        // 如果要绑定的用户为空，直接返回
        if (userAssigns == null || userAssigns.length == 0) {
            return;
        }
        // 保存绑定用户
        for (String userAssign : userAssigns) {
            String[] userArray = userAssign.split("\\*\\*");
            if (userArray != null && userArray.length == 2) {
                String userId = userArray[0];
                String assigns = userArray[1];
                String[] assignArray = assigns.split("_");
                for (String flag : assignArray) {
                    KnowledgeClassAssign knowAssign = new KnowledgeClassAssign();
                    knowAssign.setAssignKey(userId);
                    knowAssign.setFlag(Integer.parseInt(flag));
                    knowAssign.setKnowledgeClass(knowClass);
                    knowAssign.setType(KnowledgeClassAssign.TYPE_USER);
                    knowledgeClassAssignDAO.save(knowAssign);
                }
            }
        }

    }

    @Override
    public void txBindingPosts(String id, String[] postAssigns) {
        if (id == null) {
            throw new IllegalArgumentException("非法参数：id不能为空！");
        }
        KnowledgeClass knowClass = knowledgeClassDAO.findById(id);
        if (knowClass == null) {
            throw new IllegalArgumentException("类别不存在!");
        }
        knowledgeClassAssignDAO.delBindById(id, KnowledgeClassAssign.TYPE_POST);
        // 如果要绑定的用户为空，直接返回
        if (postAssigns == null || postAssigns.length == 0) {
            return;
        }
        // 保存绑定用户
        for (String postAssign : postAssigns) {
            String[] userArray = postAssign.split("\\*\\*");
            String postId = userArray[0];
            String assigns = userArray[1];
            String[] assignArray = assigns.split("_");
            for (String flag : assignArray) {
                KnowledgeClassAssign knowAssign = new KnowledgeClassAssign();
                knowAssign.setAssignKey(postId);
                knowAssign.setFlag(Integer.parseInt(flag));
                knowAssign.setKnowledgeClass(knowClass);
                knowAssign.setType(KnowledgeClassAssign.TYPE_POST);
                knowledgeClassAssignDAO.save(knowAssign);
            }
        }

    }

    @Override
    public void txBindingGroups(String id, String[] groupAssigns) {
        if (id == null) {
            throw new IllegalArgumentException("非法参数：id不能为空！");
        }
        KnowledgeClass knowClass = knowledgeClassDAO.findById(id);
        if (knowClass == null) {
            throw new IllegalArgumentException("类别不存在!");
        }
        knowledgeClassAssignDAO.delBindById(id, KnowledgeClassAssign.TYPE_GROUP);
        // 如果要绑定的用户为空，直接返回
        if (groupAssigns == null || groupAssigns.length == 0) {
            return;
        }
        // 保存绑定用户
        for (String groupAssign : groupAssigns) {
            String[] userArray = groupAssign.split("\\*\\*");
            String groupId = userArray[0];
            String assigns = userArray[1];
            String[] assignArray = assigns.split("_");
            for (String flag : assignArray) {
                KnowledgeClassAssign knowAssign = new KnowledgeClassAssign();
                knowAssign.setAssignKey(groupId);
                knowAssign.setFlag(Integer.parseInt(flag));
                knowAssign.setKnowledgeClass(knowClass);
                knowAssign.setType(KnowledgeClassAssign.TYPE_GROUP);
                knowledgeClassAssignDAO.save(knowAssign);
            }
        }

    }

    @Override
    public void txSynAssign(String id) {
        if (id == null) {
            throw new IllegalArgumentException("非法参数：id不能为空！");
        }
        // 查找知识分类
        KnowledgeClass kbc = knowledgeClassDAO.findById(id);
        if (kbc != null) {
            // 递归同步子分类
            txSynChildClassAssign(kbc.getChildClsases(), kbc.getAssigns());
        }
    }

    /**
     * 递归同步 子知识分类 权限 以及 是否强制评分
     * 
     * @param childClasses 子知识分类
     * @param kbcAssigns 当前知识分类 权限
     */
    private void txSynChildClassAssign(Set<KnowledgeClass> childClasses, Set<KnowledgeClassAssign> kbcAssigns) {
        if (childClasses == null || childClasses.size() == 0) {
            return;
        }
        for (KnowledgeClass kbc : childClasses) {
            // 删除旧权限
            knowledgeClassAssignDAO.delBindById(kbc.getId());
            for (KnowledgeClassAssign parentAssign : kbcAssigns) {
                KnowledgeClassAssign knowAssign = new KnowledgeClassAssign();
                knowAssign.setAssignKey(parentAssign.getAssignKey());
                knowAssign.setFlag(parentAssign.getFlag());
                knowAssign.setKnowledgeClass(kbc);
                knowAssign.setType(parentAssign.getType());
                knowledgeClassAssignDAO.save(knowAssign);
            }
            // 递归调用自身
            txSynChildClassAssign(kbc.getChildClsases(), kbcAssigns);
        }
    }

}
