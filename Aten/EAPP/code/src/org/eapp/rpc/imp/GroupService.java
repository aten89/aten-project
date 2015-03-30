/**
 * 
 */
package org.eapp.rpc.imp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eapp.blo.IGroupBiz;
import org.eapp.exception.EappException;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.hbean.Group;
import org.eapp.rpc.IGroupService;
import org.eapp.rpc.dto.GroupInfo;
import org.eapp.rpc.session.RPCAuthorizationFilter;
import org.eapp.sys.config.SysConstants;

import com.caucho.hessian.server.HessianServlet;

/**
 * @author zsy
 * @version
 */
public class GroupService extends HessianServlet implements IGroupService {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4637414733912286108L;
    private static final String MODULE_KEY = "rbac_group";
    private IGroupBiz groupBiz;
    
    public void setGroupBiz(IGroupBiz groupBiz) {
		this.groupBiz = groupBiz;
	}

    @Override
    public GroupInfo getGroup(String sessionID, String groupName) throws RpcAuthorizationException {
        RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.VIEW);
//        InterfaceLogger.log(sessionID, MODULE_KEY, SysConstants.VIEW, null,"方法:getGroupByName 参数:sessionID="+sessionID+",groupName="+groupName);
        Group group = groupBiz.getGroupByName(groupName);
        if (group == null) {
        	return null;
        }
        // 复制bean属性
        return copy(group);
    }
    
    @Override
    public GroupInfo getGroupByID(String sessionID, String groupID) throws RpcAuthorizationException {
    	RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.VIEW);
//      InterfaceLogger.log(sessionID, MODULE_KEY, SysConstants.VIEW, null,"方法:getGroupById 参数:sessionID="+sessionID+",groupName="+groupName);
    	Group group = groupBiz.getGroupByID(groupID);
    	if (group == null) {
        	return null;
        }
        // 复制bean属性
        return copy(group);
    }
    
    @Override
    public List<GroupInfo> getChildGroups(String sessionID, String groupName, String type)
            throws RpcAuthorizationException {
        RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.QUERY);
//        InterfaceLogger.log(sessionID, MODULE_KEY, SysConstants.QUERY, null,"方法:getSubGroupsByName 参数:sessionID="+sessionID
//        		+",groupName="+groupName+",type="+type);
        List<Group> gList = groupBiz.getSubGroupsByName(groupName, type);
        if (gList == null) {
        	return null;
        }
        List<GroupInfo> gis = new ArrayList<GroupInfo>();
        for (Group g : gList) {
            gis.add(copy(g));
        }
        return gis;
    }
    
	@Override
    public String addGroup(String sessionID, String parentGroupID, String managerRostID, String groupName, String type,
            String description) throws EappException, RpcAuthorizationException {
        RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.ADD);
        RPCLogger.interfaceLog(sessionID, MODULE_KEY, SysConstants.ADD, null,"方法:GroupService.addGroup 参数:sessionID="+sessionID
        		+",parentGroupID="+parentGroupID+",managerRostID="+managerRostID+",groupName="+groupName+",type="+type+",description="+description);
        Group group = groupBiz.addGroup(parentGroupID, managerRostID, groupName, type, description);
        
        if (group == null) {
        	return null;
        }
        RPCLogger.actionLog(sessionID, MODULE_KEY, SysConstants.ADD, group.getGroupID(), group.toString());
        return group.getGroupID();
    }

    @Override
    public void modifyGroup(String sessionID, String groupID, String parentGroupID, String managerRostID, String groupName,
            String type, String description) throws EappException, RpcAuthorizationException {
        RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.MODIFY);
        RPCLogger.interfaceLog(sessionID, MODULE_KEY, SysConstants.MODIFY, null,"方法:GroupService.modifyGroup 参数:sessionID="+sessionID
        		+",groupID="+groupID+",parentGroupID="+parentGroupID+",managerRostID="+managerRostID+",groupName="+groupName+",type="
        		+type+",description="+description);
        Group group = groupBiz.modifyGroup(groupID, parentGroupID, managerRostID, groupName, type, description);
        if (group == null) {
        	return;
        }
        RPCLogger.actionLog(sessionID, MODULE_KEY, SysConstants.MODIFY, group.getGroupID(), group.toString());
    }

    @Override
    public void deleteGroups(String sessionID, String[] groupIDs) throws EappException, RpcAuthorizationException {
        RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.DELETE);
        RPCLogger.interfaceLog(sessionID, MODULE_KEY, SysConstants.DELETE, null,"方法:GroupService.deleteGroups 参数:sessionID="+sessionID
        		+",groupIDs="+Arrays.toString(groupIDs));
        if (groupIDs == null || groupIDs.length == 0) {
        	return;
        }
        List<Group> groups = groupBiz.deleteGroups(groupIDs);
        if (groups != null) {
            for (Group group : groups) {
                RPCLogger.actionLog(sessionID, MODULE_KEY, SysConstants.DELETE, group.getGroupID(), group.toString());
            }
        }
    }

    @Override
    public void bindUser(String sessionID, String groupID, String[] accountIDs) throws RpcAuthorizationException {
        RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.BIND_USER);
        RPCLogger.interfaceLog(sessionID, MODULE_KEY, SysConstants.BIND_USER, null,"方法:GroupService.bindUser 参数:sessionID="+sessionID
        		+",groupID="+groupID+",accountIDs="+Arrays.toString(accountIDs));
        if (groupID == null || accountIDs == null) {
            return;
        }
        Group group = groupBiz.addBindUser(groupID, accountIDs);
        // 写入日志
        if (group != null) {
            StringBuffer sbf = new StringBuffer(group.toString());
            if (accountIDs.length > 0) {
                sbf.append("\n添加绑定帐号：");
                for (String s : accountIDs) {
                    sbf.append("\n").append(s);
                }
            }
            RPCLogger.actionLog(sessionID, MODULE_KEY, SysConstants.BIND_USER, group.getGroupID(), group.toString());
        }
    }

    @Override
    public void unBindUser(String sessionID, String groupID, String[] accountIDs) throws RpcAuthorizationException {
        RPCAuthorizationFilter.authorize(sessionID, SysConstants.EAPP_SUBSYSTEM_ID, MODULE_KEY, SysConstants.BIND_USER);
        RPCLogger.interfaceLog(sessionID, MODULE_KEY, SysConstants.BIND_USER, null,"方法:GroupService.unBindUser 参数:sessionID="+sessionID
        		+",groupID="+groupID+",accountIDs="+Arrays.toString(accountIDs));
        if (groupID == null || accountIDs == null) {
            return;
        }
        Group group = groupBiz.txUnBindUser(groupID, accountIDs);
        // 写入日志
        if (group != null) {
            StringBuffer sbf = new StringBuffer(group.toString());
            if (accountIDs.length > 0) {
                sbf.append("\n解除绑定帐号：");
                for (String s : accountIDs) {
                    sbf.append("\n").append(s);
                }
            }
            RPCLogger.actionLog(sessionID, MODULE_KEY, SysConstants.BIND_USER, group.getGroupID(), group.toString());
        }

    }

    /**
     * 复制本地pojo的属性到远程数据传输对象
     * 
     * @param org
     * @param dest
     */
    static GroupInfo copy(Group org) {
    	GroupInfo dest = new GroupInfo();
        dest.setDescription(org.getDescription());
        dest.setDisplayOrder(org.getDisplayOrder());
        dest.setGroupID(org.getGroupID());
        dest.setGroupName(org.getGroupName());
        if (org.getManagerPost() != null) {
            dest.setManagerPostID(org.getManagerPost().getPostID());
        }
        if (org.getParentGroup() != null) {
            dest.setParentGroupID(org.getParentGroup().getGroupID());
        }
        dest.setTreeLevel(org.getTreeLevel());
        dest.setType(org.getType());
        return dest;
    }

}
