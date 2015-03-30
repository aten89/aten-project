/**
 * 
 */
package org.eapp.client.hessian;

import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.util.SystemProperties;
import org.eapp.exception.EappException;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.IGroupService;
import org.eapp.rpc.dto.GroupInfo;


/**
 * @author zsy
 * 
 */
public class GroupService extends BaseEAPPService {
    private static Log log = LogFactory.getLog(GroupService.class);

    private static IGroupService service;


    private IGroupService getService() throws MalformedURLException {
        if (service == null) {
            synchronized (GroupService.class) {
                service = (IGroupService) factory.create(IGroupService.class, getServiceUrl(SystemProperties.SERVICE_GROUP));
            }
        }
        return service;
    }

    /**
     * 根据群组名称获得群组远程对象
     * 
     * @param sessionID
     * @param groupName
     * @return
     * @throws RpcAuthorizationException
     * @throws MalformedURLException
     */
    public GroupInfo getGroup(String groupName) throws RpcAuthorizationException, MalformedURLException {
        try {
            return getService().getGroup(getDefaultSessionID(false), groupName);
        } catch (RpcAuthorizationException e) {
            if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
                log.warn("帐号登陆超时,重新登录");
                return getService().getGroup(getDefaultSessionID(true), groupName);
            } else {
                throw e;
            }
        }
    }
    
    /**
     * 根据群组ID获得群组信息
     * 
     * @param groupID
     * @return 
     * @throws RpcAuthorizationException 异常
     * @throws MalformedURLException 异常

     */
    public GroupInfo getGroupByID(String groupID) throws RpcAuthorizationException, MalformedURLException {
        try {
            return getService().getGroupByID(getDefaultSessionID(false), groupID);
        } catch (RpcAuthorizationException e) {
            if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
                log.warn("帐号登陆超时,重新登录");
                return getService().getGroupByID(getDefaultSessionID(true), groupID);
            } else {
                throw e;
            }
        }

    }
    
    /**
     * 通过群组名称取得的子群组列表， 当群组ID为空时从根级取起 类型为空时查找取得所有类型 此方法取不到关联对象，如父群组，子群组集合等，都返回NULL
     * 
     * @param groupID 群组ID
     * @param type 类型
     * @return
     * @throws RpcAuthorizationException
     * @throws MalformedURLException
     */
    public List<GroupInfo> getChildGroups(String groupName, String type) throws RpcAuthorizationException,
            MalformedURLException {
        try {
            return getService().getChildGroups(getDefaultSessionID(false), groupName, type);
        } catch (RpcAuthorizationException e) {
            if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
                log.warn("帐号登陆超时,重新登录");
                return getService().getChildGroups(getDefaultSessionID(true), groupName, type);
            } else {
                throw e;
            }
        }
    }
    
    /**
     * 新增群组
     * 
     * @param parentGroupID 父ID
     * @param managerID 管理者ID
     * @param groupName 群组名称
     * @param type 群组类型
     * @param description 描述
     * @throws MalformedURLException
     * @throws EappException 
     */
    public String addGroup(String parentGroupID, String managerRostID, String groupName, String type, String description) throws MalformedURLException, EappException {
        try {
            return getService().addGroup(getDefaultSessionID(false), parentGroupID, managerRostID, groupName, type,
                    description);
        } catch (RpcAuthorizationException e) {
            if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
                log.warn("帐号登陆超时,重新登录");
                return getService().addGroup(getDefaultSessionID(true), parentGroupID, managerRostID, groupName, type,
                        description);
            } else {
                throw e;
            }
        }
    }

    /**
     * 修改群组
     * 
     * @param groupID 群组ID
     * @param parentGroupID 父ID
     * @param managerID 管理者ID
     * @param groupName 群组名称
     * @param type 群组类型
     * @param description 描述
     * @throws MalformedURLException
     * @throws EappException 
     */
    public void modifyGroup(String groupID, String parentGroupID, String managerRostID, String groupName, String type,
            String description) throws MalformedURLException, EappException {
        try {
            getService().modifyGroup(getDefaultSessionID(false), groupID, parentGroupID, managerRostID, groupName,
                    type, description);
        } catch (RpcAuthorizationException e) {
            if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
                log.warn("帐号登陆超时,重新登录");
                getService().modifyGroup(getDefaultSessionID(true), groupID, parentGroupID, managerRostID, groupName,
                        type, description);
            } else {
                throw e;
            }
        }
    }
    
    /**
     * 删除群组 根据ID删除群组 有子机构的不能删除 有人员绑定的不能删除 有角色绑定的，要级联删除对角色的关联
     * 
     * @param groupIDs 群组ID
     * @throws MalformedURLException
     * @throws EappException 
     */
    public void deleteGroups(String[] groupIDs) throws MalformedURLException, EappException {
        try {
            getService().deleteGroups(getDefaultSessionID(false), groupIDs);
        } catch (RpcAuthorizationException e) {
            if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
                log.warn("帐号登陆超时,重新登录");
                getService().deleteGroups(getDefaultSessionID(true), groupIDs);
            } else {
                throw e;
            }
        }
    }


    /**
     * 绑定用户帐号 追加绑定
     * 
     * @param groupID
     * @param accountIDs
     * @throws RpcAuthorizationException
     * @throws MalformedURLException
     */
    public void bindUser(String groupID, String[] accountIDs) throws RpcAuthorizationException, MalformedURLException {
        try {
            getService().bindUser(getDefaultSessionID(false), groupID, accountIDs);
        } catch (RpcAuthorizationException e) {
            if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
                log.warn("帐号登陆超时,重新登录");
                getService().bindUser(getDefaultSessionID(true), groupID, accountIDs);
            } else {
                throw e;
            }
        }
    }

    /**
     * 解除绑定用户帐号
     * 
     * @param groupID
     * @param accountIDs
     * @throws RpcAuthorizationException
     * @throws MalformedURLException
     */
    public void unBindUser(String groupID, String[] accountIDs) throws RpcAuthorizationException, MalformedURLException {
        try {
            getService().unBindUser(getDefaultSessionID(false), groupID, accountIDs);
        } catch (RpcAuthorizationException e) {
            if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
                log.warn("帐号登陆超时,重新登录");
                getService().unBindUser(getDefaultSessionID(true), groupID, accountIDs);
            } else {
                throw e;
            }
        }

    }
}