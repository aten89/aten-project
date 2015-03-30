/**
 * 
 */
package org.eapp.rpc;

import java.util.List;

import org.eapp.exception.EappException;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.dto.GroupInfo;


/**
 * 
 * @author zsy
 * @version 0.9 Modify 2008-11-24
 */
public interface IGroupService {

    /**
     * 根据群组名称获得群组信息
     * 
     * @param sessionID
     * @param groupName
     * @return
     * @throws RpcAuthorizationException
     */
    public GroupInfo getGroup(String sessionID, String groupName) throws RpcAuthorizationException;
    
    /**
     * 根据群组ID获得群组信息
     * 
     * @param sessionID
     * @param groupID
     * @return
     * @throws RpcAuthorizationException
     */
    public GroupInfo getGroupByID(String sessionID, String groupID) throws RpcAuthorizationException;

    /**
     * 根据群组的名称取得子群组列表
     * 
     * @param sessionID
     * @param groupName 群组名称
     * @param type 类型（部门、项目组）
     * @return
     * @throws RpcAuthorizationException
     */
    public List<GroupInfo> getChildGroups(String sessionID, String groupName, String type)
            throws RpcAuthorizationException;
    
    /**
     * 新增群组
     * 
     * @param parentGroupID 父ID
     * @param managerRostID 管理者职位ID
     * @param groupName 群组名称
     * @param type 群组类型
     * @param description 描述
     * @return 群组ID
     */
    public String addGroup(String sessionID, String parentGroupID, String managerRostID, 
    		String groupName, String type, String description) throws EappException, RpcAuthorizationException;

    /**
     * 修改群组
     * 
     * @param groupID 群组ID
     * @param parentGroupID 父ID
     * @param managerRostID 管理者职位ID
     * @param groupName 群组名称
     * @param type 群组类型
     * @param description 描述
     */
    public void modifyGroup(String sessionID, String groupID, String parentGroupID, String managerRostID, 
    		String groupName, String type, String description) throws EappException, RpcAuthorizationException;

    /**
     * 删除群组 根据ID删除群组 有子机构的不能删除 有人员绑定的不能删除 有角色绑定的，要级联删除对角色的关联
     * 
     * @param groupIDs 群组ID
     */
    public void deleteGroups(String sessionID, String[] groupIDs) throws EappException, RpcAuthorizationException;

    /**
     * 绑定用户帐号到群组
     * 
     * @param sessionID
     * @param groupID
     * @param userAccountID
     */
    public void bindUser(String sessionID, String groupID, String[] accountIDs) throws RpcAuthorizationException;

    /**
     * 解除绑定用户帐号到群组
     * 
     * @param sessionID
     * @param groupID
     * @param accountIDs
     * @throws RpcAuthorizationException
     */
    public void unBindUser(String sessionID, String groupID, String[] accountIDs) throws RpcAuthorizationException;

}
