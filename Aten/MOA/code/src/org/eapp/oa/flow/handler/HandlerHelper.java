/**
 * 
 */
package org.eapp.oa.flow.handler;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.GroupService;
import org.eapp.client.hessian.PostService;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.rpc.dto.GroupInfo;
import org.eapp.rpc.dto.PostInfo;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.workflow.def.IAssignable;


/**
 * 
 * 流程处理帮助类
 * 
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * Nov 30, 2008	zsy	           新建
 * 2013-03-22   kjc     修改注释
 * 
 * </pre>
 */
public final class HandlerHelper {
    /**
     * 日志
     */
    private static Log log = LogFactory.getLog(HandlerHelper.class);

    /**
     * 私有构造函数
     */
    private HandlerHelper() {

    }

    /**
     * 取得组织机构管理者职位
     * old name: getDeptManagePost
     * @param groupName 组织机构名称
     * @return 职位
     * @throws Exception 异常
     */
    public static PostInfo getGroupManagePost(String groupName) throws Exception {
        // hessian查找部门
        GroupService gs = new GroupService();
        GroupInfo gDto = gs.getGroup(groupName);
        if (gDto == null) {
            log.warn("部门不存在：" + groupName);
            return null;
        }
        // hessian查找部门管理者
        PostService ps = new PostService();
        PostInfo pDto = ps.getPostByID(gDto.getManagerPostID());
        if (pDto == null) {
            log.warn("职位不存在：" + gDto.getManagerPostID());
            return null;
        }
        return pDto;
    }

    /**
     * 取得分管的职位
     * old name: getDeptFGPost
     * @param groupName 组织机构名称
     * @return 职位
     * @throws Exception 异常
     */
    public static PostInfo getGroupHighLevelPost(String groupName) throws Exception {
        PostInfo pDto = HandlerHelper.getGroupManagePost(groupName);
        if (pDto == null) {
            return null;
        }
        PostService ps = new PostService();
        // 部门上级职位
        return ps.getPostByID(pDto.getParentPostID());

    }
    
    /**
     * 取得二级分管的职位
     * old name: getDeptFGPost
     * @param groupName 组织机构名称
     * @return 职位
     * @throws Exception 异常
     */
    public static PostInfo getGroupHighLevel2Post(String groupName) throws Exception {
        PostInfo pDto = HandlerHelper.getGroupManagePost(groupName);
        if (pDto == null) {
            return null;
        }
        PostService ps = new PostService();
        // 部门上级职位
        PostInfo highLevelPost = ps.getPostByID(pDto.getParentPostID());
        return ps.getPostByID(highLevelPost.getParentPostID());
    }

 
    /**
     * 判断是否是组织机构经理
     * 
     * @param userAccountId 账号
     * @param groupName 机构名称
     * @return 是部门经理：true，不是部门经理：false
     * @throws Exception 异常
     */
    public static boolean isGroupManager(String userAccountId, String groupName) throws Exception {
        // 账号或者groupName为空，则返回false
        if (userAccountId == null || groupName == null) {
            return false;
        }
        // 取得该用户账号所管理的机构（包括下属管理的机构，即分管机构）
        UserAccountService uas = new UserAccountService();
        List<GroupInfo> rbacGroups = uas.getManageGroups(userAccountId);
        // 如果该用户管理的部门和下属部门为空，则说明不是部门经理
        if (rbacGroups == null || rbacGroups.isEmpty()) {
            return false;
        }
        for (GroupInfo rbacGroupDTO : rbacGroups) {
            // 如果所管理的机构名称和指定的groupName一样
            if (rbacGroupDTO.getGroupName().equals(groupName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 用户列表授权
     * 
     * @param assignable assignable
     * @param users 账号
     */
    public static void assign(IAssignable assignable, List<UserAccountInfo> users) {
        if (users != null && !users.isEmpty()) {
            if (users.size() == 1) {// 授权为必办
                log.info("actor授权:" + users.get(0).getAccountID());
                assignable.setActorId(users.get(0).getAccountID());
            } else {// 授权为待办
                String[] actorIds = new String[users.size()];
                for (int i = 0; i < users.size(); i++) {
                    log.info("pooledactor授权:" + users.get(i).getAccountID());
                    actorIds[i] = users.get(i).getAccountID();
                }
                assignable.setPooledActors(actorIds);
            }
        }
    }

    /**
     * 职位授权
     * 
     * @param assignable assignable
     * @param postName 职位名称
     * @throws Exception 异常
     */
    public static void assign(IAssignable assignable, String postName) throws Exception {
        UserAccountService ps = new UserAccountService();
        // 取得职位的人员
        List<UserAccountInfo> users = ps.getUserAccountsByPost(postName);
        // 授权
        assign(assignable, users);
    }
}
