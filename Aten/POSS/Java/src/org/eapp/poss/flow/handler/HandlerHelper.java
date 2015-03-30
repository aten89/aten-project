package org.eapp.poss.flow.handler;

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

public final class HandlerHelper
{
  private static Log log = LogFactory.getLog(HandlerHelper.class);

  public static PostInfo getGroupManagePost(String groupName)
    throws Exception
  {
    GroupService gs = new GroupService();
    GroupInfo gDto = gs.getGroup(groupName);
    if (gDto == null) {
      log.warn("部门不存在：" + groupName);
      return null;
    }

    PostService ps = new PostService();
    PostInfo pDto = ps.getPostByID(gDto.getManagerPostID());
    if (pDto == null) {
      log.warn("职位不存在：" + gDto.getManagerPostID());
      return null;
    }
    return pDto;
  }

  public static PostInfo getGroupHighLevelPost(String groupName)
    throws Exception
  {
    PostInfo pDto = getGroupManagePost(groupName);
    if (pDto == null) {
      return null;
    }
    PostService ps = new PostService();

    return ps.getPostByID(pDto.getParentPostID());
  }

  public static boolean isGroupManager(String userAccountId, String groupName)
    throws Exception
  {
    if ((userAccountId == null) || (groupName == null)) {
      return false;
    }

    UserAccountService uas = new UserAccountService();
    List<GroupInfo> rbacGroups = uas.getManageGroups(userAccountId);

    if ((rbacGroups == null) || (rbacGroups.isEmpty())) {
      return false;
    }
    for (GroupInfo rbacGroupDTO : rbacGroups)
    {
      if (rbacGroupDTO.getGroupName().equals(groupName)) {
        return true;
      }
    }
    return false;
  }

  public static void assign(IAssignable assignable, List<UserAccountInfo> users)
  {
    if ((users != null) && (!users.isEmpty()))
      if (users.size() == 1) {
        log.info("actor授权:" + ((UserAccountInfo)users.get(0)).getAccountID());
        assignable.setActorId(((UserAccountInfo)users.get(0)).getAccountID());
      } else {
        String[] actorIds = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
          log.info("pooledactor授权:" + ((UserAccountInfo)users.get(i)).getAccountID());
          actorIds[i] = ((UserAccountInfo)users.get(i)).getAccountID();
        }
        assignable.setPooledActors(actorIds);
      }
  }

  public static void assign(IAssignable assignable, String postName)
    throws Exception
  {
    UserAccountService ps = new UserAccountService();

    List<UserAccountInfo> users = ps.getUserAccountsByPost(postName);

    assign(assignable, users);
  }
}