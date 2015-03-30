package org.eapp.crm.blo.imp;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.GroupService;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.crm.blo.IGroupExtBiz;
import org.eapp.crm.dao.IGroupExtDAO;
import org.eapp.crm.dao.param.GroupExtQueryParameters;
import org.eapp.crm.dto.GroupExtInfo;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.GroupExt;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.dto.GroupInfo;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.util.hibernate.ListPage;
import org.springframework.beans.BeanUtils;

public class GroupExtBiz implements IGroupExtBiz {

    private Log log = LogFactory.getLog(GroupExtBiz.class);

    private IGroupExtDAO groupExtDAO;
    /**
     * 用户账号服务接口
     */
    private UserAccountService uas = new UserAccountService();

    public IGroupExtDAO getGroupExtDAO() {
        return groupExtDAO;
    }

    public void setGroupExtDAO(IGroupExtDAO groupExtDAO) {
        this.groupExtDAO = groupExtDAO;
    }

    @Override
    public ListPage<GroupExt> getGroupExtsByBusinessType(String businessType) {
        return getGroupExtsByBusinessType(null,businessType);
    }
    
    @Override
    public ListPage<GroupExt> getGroupExtsByBusinessType(String saleDeptID,String businessType) {
    	GroupExtQueryParameters groupExtQP = new GroupExtQueryParameters();
    	if(StringUtils.isNotBlank(saleDeptID)) {
    		groupExtQP.setGroupId(saleDeptID);
    	}
    	
    	if(StringUtils.isNotBlank(businessType)) {
    		groupExtQP.setBusinessType(businessType);
    	}
    	
        return this.groupExtDAO.queryGroupExt(groupExtQP);
    }

    @Override
    public GroupExtInfo getGroupByID(String groupID) throws CrmException {
        GroupExtInfo extInfo = new GroupExtInfo();

        try {
            GroupService groupService = new GroupService();
            GroupInfo groupInfo = groupService.getGroupByID(groupID);
            GroupExt groupExt = getGroupExtByID(groupID);

            if (groupInfo == null) {
                return null;
            } else {
                BeanUtils.copyProperties(groupInfo, extInfo);
                if (groupExt != null) {
                    extInfo.setBusinessType(groupExt.getBusinessType());
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new CrmException(ex);
        }

        return extInfo;
    }

    private GroupExt getGroupExtByID(String groupID) {
        GroupExtQueryParameters qp = new GroupExtQueryParameters();
        qp.setGroupId(groupID);

        ListPage<GroupExt> listGroupExt = groupExtDAO.queryGroupExt(qp);
        if (listGroupExt.getDataList() == null || listGroupExt.getDataList().isEmpty()) {
            return null;
        } else {
            return listGroupExt.getDataList().get(0);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eapp.crm.blo.IGroupExtBiz#querySaleGroup(java.lang.String)
     */
    @Override
    public List<GroupExt> querySaleGroup(String userAccountId) throws CrmException {
        // 参数判断
        if (StringUtils.isEmpty(userAccountId)) {
            throw new IllegalArgumentException("非法参数：当前用户账号为空");
        }
        List<GroupExt> salseGroups = new ArrayList<GroupExt>(0);
        // 获取用户管理的部门和下属部门
        List<GroupInfo> groupInfos = null;
        try {
            groupInfos = uas.getManageGroups(userAccountId);
        } catch (RpcAuthorizationException e) {
            log.error("getBindedGroups by accountID failed: ", e);
            throw new CrmException(e.getMessage());
        } catch (MalformedURLException e) {
            log.error("getBindedGroups by accountID failed: ", e);
            throw new CrmException(e.getMessage());
        }
        // 判断该用户管理的部门和下属部门哪些是销售部门,如果是销售部门，则加入salseGroups里
        if (groupInfos != null) {
            for (GroupInfo groupInfo : groupInfos) {
                GroupExt groupExt = groupExtDAO.findById(groupInfo.getGroupID());
                if (groupExt != null && GroupExt.BUSINESS_TYPE_SALE.equals(groupExt.getBusinessType())) {
                    salseGroups.add(groupExt);
                }
            }
        }
        return salseGroups;
    }
    
    @Override
    public List<GroupExt> queryAllSaleGroupSel(String businessType) throws CrmException {
        // 获取全部销售部门信息
        List<GroupExt> groupExts = groupExtDAO.findByBusinessType(businessType);
        return groupExts;
    }
    
//    @Override
//    public List<UserAccountInfo> queryGroupStaffByGroupId(String groupId, String userAccountId) throws CrmException {
//        // 参数判断
//        if (StringUtils.isEmpty(groupId)) {
//            return null;
//            // throw new IllegalArgumentException("非法参数：组织机构ID为空");
//        }
//        List<UserAccountInfo> userAccountInfos = null;
//        try {
//            // 通过组织机构ID查找组织机构
//            GroupInfo groupInfo = gs.getGroupByID(groupId);
//            if (groupInfo == null) {
//                throw new CrmException("组织机构不存在");
//            }
//            // 通过组织机构名称找出绑定的用户
//            userAccountInfos = uas.getUserAccountsByGroup(groupInfo.getGroupName());
//            if (userAccountInfos != null) {
//                // 排除当前登录账号
//                UserAccountInfo currentUserAccountInfo = null;
//                for (UserAccountInfo userAccountInfo : userAccountInfos) {
//                    if (userAccountInfo.getAccountID().equals(userAccountId)) {
//                        currentUserAccountInfo = userAccountInfo;
//                        break;
//                    }
//                }
//                if (currentUserAccountInfo != null) {
//                    userAccountInfos.remove(currentUserAccountInfo);
//                }
//            }
//        } catch (RpcAuthorizationException e) {
//            log.error("getGroupByID failed: ", e);
//            throw new CrmException(e.getMessage());
//        } catch (MalformedURLException e) {
//            log.error("getGroupByID failed: ", e);
//            throw new CrmException(e.getMessage());
//        }
//        return userAccountInfos;
//    }
    
    @Override
    public List<UserAccountInfo> queryGroupStaffByGroupName(String groupName) throws CrmException {
        // 参数判断
        if (StringUtils.isEmpty(groupName)) {
            return null;
        }
        List<UserAccountInfo> userAccountInfos = null;
        try {
            // 通过组织机构名称找出绑定的用户
            userAccountInfos = uas.getUserAccountsByGroup(groupName);
        } catch (RpcAuthorizationException e) {
            log.error("getGroupByID failed: ", e);
            throw new CrmException(e.getMessage());
        } catch (MalformedURLException e) {
            log.error("getGroupByID failed: ", e);
            throw new CrmException(e.getMessage());
        }
        return userAccountInfos;
    }

    @Override
    public List<UserAccountInfo> queryManSaleStaffs(String userAccountId) throws CrmException {
        // 参数判断
        if (StringUtils.isEmpty(userAccountId)) {
            throw new IllegalArgumentException("非法参数：当前用户账号为空");
        }
        List<UserAccountInfo> saleStaffs = new ArrayList<UserAccountInfo>(0);
        // 根据当前登录者获取销售部门信息
        List<GroupExt> groupExts = querySaleGroup(userAccountId);
        if (groupExts != null) {
            for (GroupExt groupExt : groupExts) {
                // 根据销售部门ID获取销售人员
                List<UserAccountInfo> userAccountInfos = queryGroupStaffByGroupName(groupExt.getGroupName());
                if (userAccountInfos != null) {
                    saleStaffs.addAll(userAccountInfos);
                }
            }
        }
        return saleStaffs;
    }
    
    @Override
    public List<UserAccountInfo> queryAllSaleStaff() throws CrmException {
       
        List<UserAccountInfo> saleStaffs = new ArrayList<UserAccountInfo>(0);
        // 获取全部销售部门信息
        List<GroupExt> groupExts = groupExtDAO.findByBusinessType(GroupExt.BUSINESS_TYPE_SALE);
        if (groupExts != null) {
            for (GroupExt groupExt : groupExts) {
                // 根据销售部门ID获取销售人员
                List<UserAccountInfo> userAccountInfos = queryGroupStaffByGroupName(groupExt.getGroupName());
                if (userAccountInfos != null) {
                    saleStaffs.addAll(userAccountInfos);
                }
            }
        }
        return saleStaffs;
    }

	@Override
	public void txSaveGroupExt(String groupID, String groupName,
			String businessType) {
		GroupExt groupExt = getGroupExtByID(groupID);
		if(groupExt == null) {
			if (StringUtils.isBlank(businessType)) {
				return;
			}
			groupExt = new GroupExt();
			groupExt.setGroupId(groupID);
			groupExt.setGroupName(groupName);
			groupExt.setBusinessType(businessType);
			
			this.groupExtDAO.save(groupExt);
		}else {
			if (StringUtils.isBlank(businessType)) {
				groupExtDAO.delete(groupExt);
			} else {
				groupExt.setBusinessType(businessType);
				groupExtDAO.update(groupExt);
			}
		}
	}
}
