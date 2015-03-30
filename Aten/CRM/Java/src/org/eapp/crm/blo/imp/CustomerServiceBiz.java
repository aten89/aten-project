/**
 * 
 */
package org.eapp.crm.blo.imp;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eapp.client.hessian.GroupService;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.comobj.SessionAccount;
import org.eapp.crm.blo.ICustomerServiceBiz;
import org.eapp.crm.dao.ICustomerServiceDAO;
import org.eapp.crm.dao.param.CustomerInfoQueryParameters;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.CustomerInfo;
import org.eapp.crm.hbean.ReturnVist;
import org.eapp.crm.hbean.UserAccountExt;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.dto.GroupInfo;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Hibernate;
import org.springframework.util.CollectionUtils;

/**
 * 客户信息业务逻辑层实现
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-4	黄云耿	新建
 * </pre>
 */
public class CustomerServiceBiz implements ICustomerServiceBiz {

    /**
     * 客服管理DAO接口
     */
    private ICustomerServiceDAO customerServiceDAO;
    /**
     * 组织机构
     */
    private GroupService gs = new GroupService();
    /**
     * 用户账号服务接口
     */
    private UserAccountService uas = new UserAccountService();
  
    @SuppressWarnings("null")
    @Override
    public ListPage<CustomerInfo> queryCustomerInfoList(CustomerInfoQueryParameters qp, SessionAccount user) 
            throws CrmException {
        //客户对应销售人员账号
        List<String> accountIds = new ArrayList<String>(0);
        List<UserAccountExt> UserAccountExts = customerServiceDAO.queryUserAccountExtList(user.getAccountID());
        for(UserAccountExt userAccountExt : UserAccountExts){
            //销售人员账号
            String accountId = userAccountExt.getAccountId();
            accountIds.add(accountId);
        }
        //如果客服对应的销售人员为空，则返回空
        if(accountIds == null || accountIds.isEmpty()){
            return null;
        }
        
        qp.setSaleMans(accountIds);
        
        // 通过组织机构ID查找组织机构
        if (StringUtils.isNotEmpty(qp.getSaleGroupId())) {
//            accountIds.clear();
            try {
                GroupInfo groupInfo = gs.getGroupByID(qp.getSaleGroupId());
                List<UserAccountInfo> userAccountInfos = uas.getUserAccountsByGroup(groupInfo.getGroupName());
                List<String> accountIdList = new ArrayList<String>(0);
                if(userAccountInfos != null){
                    for(UserAccountInfo u : userAccountInfos){
                    	if(accountIds.contains(u.getAccountID())) {
                    		accountIdList.add(u.getAccountID());
                    	}
                    }
                }
                if(accountIdList == null || accountIdList.isEmpty()){
                    return null;
                }
                qp.setSaleMans(accountIdList);
            } catch (RpcAuthorizationException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
       
        
        
        ListPage<CustomerInfo> customerInfos = customerServiceDAO.queryCustomerInfoList(qp);
        if(customerInfos != null && customerInfos.getDataList() != null){
            for(CustomerInfo customerInfo : customerInfos.getDataList()){
                // 初始化投资顾问部门
                try {
                    List<GroupInfo> groups = uas.getBindedGroups(customerInfo.getSaleMan());
                    List<String> deptNameList = new ArrayList<String>();
                    if (!CollectionUtils.isEmpty(groups)) {
                        GroupInfo g = groups.get(0);
                        deptNameList.add(g.getGroupName());
                        getParentDept(g, deptNameList );
                        String fullDeptName = "";
                        for (String dept : deptNameList) {
                            fullDeptName += dept + "/";
                        }
                        if (StringUtils.isNotEmpty(fullDeptName)) {
                            customerInfo.setFullDeptName(fullDeptName.substring(0, fullDeptName.length()-1));
                        }
                    }
                } catch (RpcAuthorizationException e) {
                    throw new CrmException("初始化投资顾问部门失败");
                } catch (MalformedURLException e) {
                    throw new CrmException("初始化投资顾问部门失败");
                }
                
                List<ReturnVist> returnVists = customerServiceDAO.queryVisitRecordList(customerInfo.getId(), null);
                if (returnVists.size() > 0) {
	                customerInfo.setReturnVisitCount(returnVists.size());//回访次数
	                customerInfo.setLastReturnVisitTime(returnVists.get(0).getReturnVistTime());//最后回访时间
            	}
                
                Hibernate.initialize(customerInfo.getCustomerAppointments());
                Hibernate.initialize(customerInfo.getCustomerConsults());
                Hibernate.initialize(customerInfo.getReturnVists());
            }
        }
        return customerInfos;
    }
    
    private void getParentDept(GroupInfo g, List<String> deptNameList) throws RpcAuthorizationException, MalformedURLException{
        if (g != null && StringUtils.isNotEmpty(g.getParentGroupID())) {
            GroupInfo group = gs.getGroupByID(g.getParentGroupID());
            deptNameList.add(group.getGroupName());
            getParentDept(group, deptNameList);
        }
    }
    
    public void setCustomerServiceDAO(ICustomerServiceDAO customerServiceDAO) {
        this.customerServiceDAO = customerServiceDAO;
    }
   
}
