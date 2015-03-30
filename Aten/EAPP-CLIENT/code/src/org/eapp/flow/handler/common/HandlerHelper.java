/**
 * 
 */
package org.eapp.flow.handler.common;

import java.util.ArrayList;
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
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.Execution;


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
    
    private static GroupService groupService = new GroupService();
    private static PostService postService = new PostService();
    private static UserAccountService userAccountService = new UserAccountService();
    
    /**
     * 判断Handle的转向线名
     */
	public static final String TRANSITION_YES = "是";
	public static final String TRANSITION_NO = "否";
	
    public static final String DEPT_TYPE = "D";//部门类型
    /**
     * 流程引擎上下文变量
     */
    // 绑定在流程引擎上下文变量名称---表单ID
    public static final String FLOW_VARNAME_FORMID = "formId";
    // 绑定在流程引擎上下文变量的名称---发起人帐号
    public static final String FLOW_VARNAME_USERACCOUNTID = "userAccountId";
    // 绑定在流程引擎上下文变量的名称---任务说明
    public static final String FLOW_VARNAME_TASKDESCRIPTION = "taskDescription";
    // 绑定在流程引擎上下文变量的名称---所属部门
    public static final String FLOW_VARNAME_GROUPNAME = "groupName";

    /**
     * 私有构造函数
     */
    private HandlerHelper() {

    }
    
    /**
     * 取得发起人帐号
     * @param exe
     * @return
     */
    public static String getUserAccountId(Execution exe) {
    	ContextVariable var = exe.getFlowInstance().getVariable(HandlerHelper.FLOW_VARNAME_USERACCOUNTID);
		return var.getValue();//填单人
    }
    
    /**
     * 取得所属部门
     * @param exe
     * @return
     */
    public static String getGroupName(Execution exe) {
    	ContextVariable var = exe.getFlowInstance().getVariable(HandlerHelper.FLOW_VARNAME_GROUPNAME);
		return var.getValue();//填单人
    }
    
    /**
     * 取得任务说明
     * @param exe
     * @return
     */
    public static String getTaskDescription(Execution exe) {
    	ContextVariable var = exe.getFlowInstance().getVariable(HandlerHelper.FLOW_VARNAME_TASKDESCRIPTION);
		return var.getValue();//填单人
    }
    
    /**
     * 取得表单ID
     * @param exe
     * @return
     */
    public static String getFormId(Execution exe) {
    	ContextVariable var = exe.getFlowInstance().getVariable(HandlerHelper.FLOW_VARNAME_FORMID);
		return var.getValue();//填单人
    }
    
    /**
     * 取得用户的直接领导
     * @param userAccountId
     * @param level 层级，如，部门领导传0，分管领导传1
     * @return
     * @throws Exception
     */
    public static List<UserAccountInfo> getUserManagers(String userAccountId, int level) throws Exception {
    	List<UserAccountInfo> users = new ArrayList<UserAccountInfo>();
    	List<GroupInfo> groups = userAccountService.getBindedGroups(userAccountId);
    	if (groups == null || groups.isEmpty()) {
    		return users;
    	}
		for(GroupInfo g : groups) {
			if (HandlerHelper.DEPT_TYPE.equals(g.getType())) {//如果是部门
				PostInfo pDto = postService.getPostByID(g.getManagerPostID());//取得部门管理都职位
				if (pDto == null) {
					continue;
				}
				if (level > 0) {
					for (int i = 0; i < level; i++) {
						pDto = postService.getPostByID(pDto.getParentPostID());//取高一级的职位
						if (pDto == null) {
							break;
						}
					}
				}
				if (pDto == null) {
					continue;
				}
				List<UserAccountInfo> us = userAccountService.getUserAccountsByPost(pDto.getPostName());//取得职位的用户
				if (us != null) {
					users.addAll(us);
				}
			}
		}
    	return users;
    }
    
    /**
     * 取得部门的领导
     * @param groupName
     * @param level 层级，如，部门领导传0，分管领导传1
     * @return
     * @throws Exception
     */
    public static List<UserAccountInfo> getGroupManagers(String groupName, int level) throws Exception {
    	GroupInfo gDto = groupService.getGroup(groupName);
        if (gDto == null) {
            return null;
        }
        PostInfo pDto = postService.getPostByID(gDto.getManagerPostID());
        if (pDto == null) {
            return null;
        }
        if (level > 0) {
			for (int i = 0; i < level; i++) {
				pDto = postService.getPostByID(pDto.getParentPostID());//取高一级的职位
				if (pDto == null) {
					break;
				}
			}
		}
        if (pDto == null) {
            return null;
        }
		return userAccountService.getUserAccountsByPost(pDto.getPostName());//取得职位的用户
    }

    /**
     * 用户列表授权
     * 
     * @param assignable assignable
     * @param users 账号
     */
    public static void assign(IAssignable assignable, List<UserAccountInfo> users) {
        if (users == null || users.isEmpty()) {
        	return;
        }
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
