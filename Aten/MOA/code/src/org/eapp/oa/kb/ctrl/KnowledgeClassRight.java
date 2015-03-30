/**
 * 
 */
package org.eapp.oa.kb.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eapp.comobj.SessionAccount;
import org.eapp.comobj.SessionAccount.Name;
import org.eapp.oa.kb.blo.IKnowledgeClassBiz;
import org.eapp.oa.kb.hbean.KnowledgeClass;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.Tree;
import org.eapp.util.spring.SpringHelper;

/**
 * 知识类别权限servlet
 */
public class KnowledgeClassRight {

    /**
     * 知识类别权限
     */
    private static final String SESSION_ATTR_RIGHT = "knowledgeClassRights";
    /**
     * 知识类别树
     */
    private static final String SESSION_ATTR_TREE = "knowledgeClassTree";
    /**
     * 知识类别所有树
     */
    private static final String SESSION_ATTR_ALL_TREE = "knowledgeClassATree";

    /**
     * 取得有权限的缓存树
     * 
     * @param request request
     * @param rightFlag 权限标志
     * @return 有权限的缓存树
     */
    @SuppressWarnings("unchecked")
    public static Tree getRightTree(HttpServletRequest request, int rightFlag) {
        Map<Integer, Tree> treeMap = (Map<Integer, Tree>) request.getSession().getAttribute(SESSION_ATTR_TREE);
        Tree tree = null;
        if (treeMap == null) {
            treeMap = new HashMap<Integer, Tree>();
            request.getSession().setAttribute(SESSION_ATTR_TREE, treeMap);
        } else {
            tree = treeMap.get(rightFlag);
            if (tree != null) {
                return tree;
            }
        }
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        List<String> groupNames = new ArrayList<String>();
        if (user.getGroups() != null) {
            for (Name n : user.getGroups()) {
                groupNames.add(n.getName());
            }
        }
        List<String> postNames = new ArrayList<String>();
        if (user.getPosts() != null) {
            for (Name n : user.getPosts()) {
                postNames.add(n.getName());
            }
        }
        IKnowledgeClassBiz knowledgeClassBiz = (IKnowledgeClassBiz) SpringHelper.getSpringContext().getBean(
                "knowledgeClassBiz");
        List<KnowledgeClass> kbClass = knowledgeClassBiz.getAssignClass(user.getAccountID(), groupNames, postNames,
                rightFlag);
        tree = Tree.createTree(kbClass.toArray(new KnowledgeClass[0]));
        // tree = Tree.createTree((KnowledgeClass [])kbClass.toArray());
        treeMap.put(rightFlag, tree);
        return tree;

    }

    /**
     * 取得的缓存树（所有）
     * 
     * @param request request
     * @param rightFlag 权限标志
     * @return 缓存树
     */
    @SuppressWarnings("unchecked")
    public static Tree getAllTree(HttpServletRequest request, int rightFlag) {
        Map<Integer, Tree> treeMap = (Map<Integer, Tree>) request.getSession().getAttribute(SESSION_ATTR_ALL_TREE);
        Tree tree = null;
        if (treeMap == null) {
            treeMap = new HashMap<Integer, Tree>();
            request.getSession().setAttribute(SESSION_ATTR_ALL_TREE, treeMap);
        } else {
            tree = treeMap.get(rightFlag);
            if (tree != null) {
                return tree;
            }
        }
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        List<String> groupNames = new ArrayList<String>();
        if (user.getGroups() != null) {
            for (Name n : user.getGroups()) {
                groupNames.add(n.getName());
            }
        }
        List<String> postNames = new ArrayList<String>();
        if (user.getPosts() != null) {
            for (Name n : user.getPosts()) {
                postNames.add(n.getName());
            }
        }
        IKnowledgeClassBiz knowledgeClassBiz = (IKnowledgeClassBiz) SpringHelper.getSpringContext().getBean(
                "knowledgeClassBiz");
        List<KnowledgeClass> kbClass = knowledgeClassBiz.getAllClass();
        tree = Tree.createTree(kbClass.toArray(new KnowledgeClass[0]));
        treeMap.put(rightFlag, tree);
        return tree;

    }

    /**
     * 取得类别的权限
     * 
     * @param request request
     * @param classId 类别id
     * @return 类别的权限
     */
    @SuppressWarnings("unchecked")
    public static String getRigths(HttpServletRequest request, String classId) {
        Map<String, String> classRights = (Map<String, String>) request.getSession().getAttribute(SESSION_ATTR_RIGHT);
        if (classRights == null) {
            classRights = new HashMap<String, String>();
            request.getSession().setAttribute(SESSION_ATTR_RIGHT, classRights);
        } else {
            String rights = classRights.get(classId);
            if (rights != null) {
                return rights;
            }
        }
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        List<String> groupNames = new ArrayList<String>();
        if (user.getGroups() != null) {
            for (Name n : user.getGroups()) {
                groupNames.add(n.getName());
            }
        }
        List<String> postNames = new ArrayList<String>();
        if (user.getPosts() != null) {
            for (Name n : user.getPosts()) {
                postNames.add(n.getName());
            }
        }

        IKnowledgeClassBiz knowledgeClassBiz = (IKnowledgeClassBiz) SpringHelper.getSpringContext().getBean(
                "knowledgeClassBiz");
        List<Integer> rigths = knowledgeClassBiz.getKnowledgeClassRight(user.getAccountID(), groupNames, postNames,
                classId);
        if (rigths.isEmpty()) {
            return "";
        }
        StringBuffer sb = new StringBuffer(",");
        for (Integer r : rigths) {
            sb.append(r).append(",");
        }
        classRights.put(classId, sb.toString());
        return sb.toString();
    }

    /**
     * 是否有权限
     * 
     * @param request request
     * @param classId 类别id
     * @param flag 标志
     * @return 是否有权限 true|false
     */
    public static boolean hasRigth(HttpServletRequest request, String classId, int flag) {
        String rights = getRigths(request, classId);
        return rights.indexOf("," + flag + ",") >= 0;
    }

    /**
     * 清空权限缓存权限
     * 
     * @param request request
     */
    @SuppressWarnings("unchecked")
    public static void clearRightCache(HttpServletRequest request) {
        // 清空权限缓存
        Map<String, String> classRights = (Map<String, String>) request.getSession().getAttribute(SESSION_ATTR_RIGHT);
        if (classRights != null) {
            classRights.clear();
        }
    }

    /**
     * 清空树缓存权限
     * 
     * @param request request
     */
    @SuppressWarnings("unchecked")
    public static void clearTreeCache(HttpServletRequest request) {
        // 清空树缓存
        Map<Integer, Tree> treeMap = (Map<Integer, Tree>) request.getSession().getAttribute(SESSION_ATTR_TREE);
        if (treeMap != null) {
            treeMap.clear();
        }
    }
}
