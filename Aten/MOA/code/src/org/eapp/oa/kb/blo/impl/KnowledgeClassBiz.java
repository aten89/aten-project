/**
 * 
 */
package org.eapp.oa.kb.blo.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eapp.oa.kb.blo.IKnowledgeClassBiz;
import org.eapp.oa.kb.dao.IKnowledgeClassDAO;
import org.eapp.oa.kb.dao.IKnowledgeDAO;
import org.eapp.oa.kb.hbean.KnowledgeClass;
import org.eapp.oa.kb.hbean.KnowledgeClassAssign;

import org.eapp.oa.system.exception.OaException;

/**
 * 知识库类别业务逻辑接口
 * 
 */
public class KnowledgeClassBiz implements IKnowledgeClassBiz {

    /**
     * 知识库类别DAO
     */
    private IKnowledgeClassDAO knowledgeClassDAO;
    /**
     * 知识库相关DAO
     */
    private IKnowledgeDAO knowledgeDAO;

    public void setKnowledgeClassDAO(IKnowledgeClassDAO knowledgeClassDAO) {
        this.knowledgeClassDAO = knowledgeClassDAO;
    }

    public void setKnowledgeDAO(IKnowledgeDAO knowledgeDAO) {
        this.knowledgeDAO = knowledgeDAO;
    }

    @Override
    public List<KnowledgeClass> getAssignClass(String userAccountId, List<String> groupNames, List<String> postNames,
            int flag) {
        return knowledgeClassDAO.findAssignClass(userAccountId, groupNames, postNames, flag);
    }

    @Override
    public List<KnowledgeClass> getAllClass() {
        return knowledgeClassDAO.findAllClass();
    }

    @Override
    public List<String> getSearchableClassId(String userAccountId, List<String> groupNames, List<String> postNames) {
        return knowledgeClassDAO.findSearchableClassId(userAccountId, groupNames, postNames);
    }

    @Override
    public KnowledgeClass txAddKnowledgeClass(String parentId, String nodeText, String description) throws OaException {
        int nameCount = knowledgeClassDAO.getNodeNumByName(parentId, nodeText);
        if (nameCount > 0) {
            throw new OaException("同级下类别名称不能重复");
        }

        KnowledgeClass parentClass = null;
        if (parentId != null) {
            parentClass = knowledgeClassDAO.findById(parentId);
        }
        // 新增操作
        KnowledgeClass knowClass = new KnowledgeClass();
        if (parentClass != null) {
            Set<KnowledgeClassAssign> parentAssigns = parentClass.getAssigns();
            Set<KnowledgeClassAssign> assigns = new HashSet<KnowledgeClassAssign>();
            for (KnowledgeClassAssign pAssign : parentAssigns) {
                KnowledgeClassAssign assign = new KnowledgeClassAssign();
                assign.setAssignKey(pAssign.getAssignKey());
                assign.setFlag(pAssign.getFlag());
                assign.setType(pAssign.getType());
                assign.setKnowledgeClass(knowClass);
                assigns.add(assign);
            }
            knowClass.setAssigns(assigns);
        }
        int maxOrder = knowledgeClassDAO.getMaxOrder(parentId);
        knowClass.setDisplayOrder(maxOrder + 1);

        knowClass.setName(nodeText);
        knowClass.setDescription(description);

        knowClass.setParentClass(parentClass);
        if (parentClass == null) {
            knowClass.setTreeLevel(1);
        } else {
            knowClass.setTreeLevel(parentClass.getTreeLevel() + 1);
        }
        knowledgeClassDAO.save(knowClass);
        return knowClass;
    }

    @Override
    public List<KnowledgeClass> getAllChildById(String id) {
        if (StringUtils.isNotEmpty(id)) {
            return knowledgeClassDAO.getAllChildList(id);
        } else {
            throw new IllegalArgumentException("根据id 为空");
        }
    }

    @Override
    public KnowledgeClass txModifyKnowledgeClass(String nodeId, String nodeText, String description) throws OaException {
        if (nodeId == null) {
            throw new IllegalArgumentException();
        }
        KnowledgeClass knowClass = knowledgeClassDAO.findById(nodeId);
        if (knowClass == null) {
            throw new IllegalArgumentException();
        }
        String parentId = null;
        if (knowClass.getParentNode() == null) {
            parentId = null;
        } else {
            parentId = knowClass.getParentNode().getId();
        }
        if (!nodeText.equals(knowClass.getName())) {
            int nameCount = knowledgeClassDAO.getNodeNumByName(parentId, nodeText);
            if (nameCount > 0) {
                throw new OaException("同级下类别名称不能重复");
            }

        }
        knowClass.setName(nodeText);
        knowClass.setDescription(description);

        knowledgeClassDAO.update(knowClass);
        return knowClass;
    }

    @Override
    public KnowledgeClass getKnowledgeClassById(String id) {
        return knowledgeClassDAO.findById(id);
    }

    @Override
    public void txDelKnowledgeClass(String id) throws OaException {
        KnowledgeClass knowClass = knowledgeClassDAO.findById(id);
        if (knowClass == null) {
            return;
        }
        if (knowClass.getChildClsases().size() > 0) {
            throw new OaException("存在下级类别，不允许删除");
        } else if (knowledgeDAO.getCountByKnowledgeClass(id) > 0) {
            throw new OaException("存在知识点，不允许删除");
        }
        knowledgeClassDAO.delete(knowClass);
    }

    @Override
    public void txMoveKnowledgeClassNode(String id, String targetId, int moveStatus) {
        KnowledgeClass knowClass = knowledgeClassDAO.findById(id);
        if (knowClass == null) {
            throw new IllegalArgumentException();
        }
        if (moveStatus == KnowledgeClass.MOVESTATUS_UP || moveStatus == KnowledgeClass.MOVESTATUS_DOWN) {
            KnowledgeClass targetKnowClass = knowledgeClassDAO.findById(targetId);
            if (knowClass != null && targetKnowClass != null) {
                int tmpOrder = knowClass.getDisplayOrder();
                knowClass.setDisplayOrder(targetKnowClass.getDisplayOrder());
                knowledgeClassDAO.saveOrUpdate(knowClass);
                targetKnowClass.setDisplayOrder(tmpOrder);
                knowledgeClassDAO.saveOrUpdate(targetKnowClass);
            }
        } else {
            int oldOrder = knowClass.getDisplayOrder();
            knowledgeClassDAO.updateOrder(knowClass.getParentClass(), moveStatus, oldOrder);
            if (moveStatus == KnowledgeClass.MOVESTATUS_FIST) {
                knowClass.setDisplayOrder(1);
            } else if (moveStatus == KnowledgeClass.MOVESTATUS_LAST) {
                String classId = null;
                if (knowClass.getParentClass() == null) {
                    classId = null;
                } else {
                    classId = knowClass.getParentClass().getId();
                }
                int maxOrder = knowledgeClassDAO.getMaxOrder(classId);// maxOrder已经被减一
                knowClass.setDisplayOrder(maxOrder + 1);
            }
            knowledgeClassDAO.saveOrUpdate(knowClass);
        }
    }

    @Override
    public List<Integer> getKnowledgeClassRight(String userAccountId, List<String> groupNames, List<String> postNames,
            String classId) {
        if (userAccountId == null || classId == null) {
            throw new IllegalArgumentException();
        }
        return knowledgeClassDAO.findKnowledgeClassRight(userAccountId, groupNames, postNames, classId);
    }

    @Override
    public List<KnowledgeClass> getFullClassByKnowledgeClass(List<KnowledgeClass> list, KnowledgeClass knowledgeClass,
            boolean includeFlag) {
        if (includeFlag) {
            list.add(knowledgeClass);
        }
        if (knowledgeClass.getParentClass() != null) {
            list = getFullClassByKnowledgeClass(list, knowledgeClass.getParentClass(), true);
        }
        return list;
    }

}
