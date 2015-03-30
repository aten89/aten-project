package org.eapp.crm.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.comobj.SessionAccount;
import org.eapp.crm.blo.IGroupExtBiz;
import org.eapp.crm.blo.IUserAccountExtBiz;
import org.eapp.crm.config.SysConstants;
import org.eapp.crm.dto.UserAccountExtInfo;
import org.eapp.crm.dto.UserAccountExtSelect;
import org.eapp.crm.hbean.GroupExt;
import org.eapp.crm.hbean.UserAccountExt;
import org.eapp.crm.system.util.web.HTMLResponse;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.util.hibernate.ListPage;
import org.springframework.beans.BeanUtils;

public class UserAccountExtAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7355961603989992352L;
	private static final Log log = LogFactory.getLog(UserAccountExtAction.class);
	
	private String[] accountIDs;
	private String serviceAccountID;
	private String saleDeptID;

	private IGroupExtBiz groupExtBiz;
	private IUserAccountExtBiz	userAccountExtBiz;

	private ListPage<UserAccountExtInfo> userAccountPage;
	
	public String initQuery() {
	    return success();
	}
	
	/**
	 * 只查询出 销售人员，全部显示，没有翻页。
	 * @return
	 */
	public String queryUser() {
	    try {
	    	int count =0;
	    	ListPage<GroupExt> listGroupExt = groupExtBiz.getGroupExtsByBusinessType(this.saleDeptID,GroupExt.BUSINESS_TYPE_SALE);
	    	
			if(listGroupExt != null && listGroupExt.getDataList() != null) {
				Set<UserAccountInfo> listUserAccount = new LinkedHashSet<UserAccountInfo>();
				UserAccountService userAccountService = new UserAccountService();
				for(GroupExt item:listGroupExt.getDataList()) {
					List<UserAccountInfo> users = userAccountService.getUserAccountsByGroup(item.getGroupName());
					if (users != null && !users.isEmpty()) {
						listUserAccount.addAll(users);
					}
				}
				ListPage<UserAccountExt> listUserAccountExt = userAccountExtBiz.getAll();
				
				Map<String,UserAccountExt> mapUserAccountExt = new HashMap<String,UserAccountExt>();
				if(listUserAccountExt.getDataList() != null) {
					for(UserAccountExt accountExt : listUserAccountExt.getDataList()) {
						mapUserAccountExt.put(accountExt.getAccountId(), accountExt);
					}
				}
				
				userAccountPage = new ListPage<UserAccountExtInfo>();
				userAccountPage.setDataList(new ArrayList<UserAccountExtInfo>());
				
				for(UserAccountInfo userInfo : listUserAccount) {
					UserAccountExtInfo newAccountExtInfo = new UserAccountExtInfo();
					
					BeanUtils.copyProperties(userInfo, newAccountExtInfo);
					UserAccountExt item = mapUserAccountExt.get(userInfo.getAccountID());
					if(item != null) {
						newAccountExtInfo.setServiceAccountId(item.getServiceAccountId());
					}
					
					this.userAccountPage.getDataList().add(newAccountExtInfo);
					count++;
				}
				
				this.userAccountPage.setTotalCount(count);
				this.userAccountPage.setCurrentPageSize(count);
				this.userAccountPage.setCurrentPageNo(1);
			}
			
			return success();
	    } catch (Exception e) {
	    	log.error(e.getMessage(), e);
	    }
	    return error();
	}
	
	public void querySaleManSel() {
		SessionAccount user = (SessionAccount) getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		try {
			List<UserAccountExt> userExts = userAccountExtBiz.getByServiceAccountId(user.getAccountID());
            if (userExts == null) {
                return;
            }
            HTMLResponse.outputHTML(getResponse(), new UserAccountExtSelect(userExts).toString());
        } catch (Exception e) {
            log.error("querySaleManSel failed: ", e);
        }
	}
	
	public String modifyServiceAccount() {
		if ((this.accountIDs == null) || (this.accountIDs.length < 1)) {
	      return error("accountIDs参数不能为空");
		}
		
		if(StringUtils.isEmpty(this.serviceAccountID)) {
			return error("serviceAccountID参数不能为空");
		}
		
		try {
			this.userAccountExtBiz.txModifyServiceAccount(this.accountIDs,this.serviceAccountID);
			return success();
		}catch(Exception ex) {
			log.error(ex.getMessage(),ex);
		}
		
		return error();
	}
	
	public void setAccountIDs(String[] accountIDs) {
		this.accountIDs = accountIDs;
	}

	public void setServiceAccountID(String serviceAccountID) {
		this.serviceAccountID = serviceAccountID;
	}
	
	public void setSaleDeptID(String saleDeptID) {
		this.saleDeptID = saleDeptID;
	}

	public void setGroupExtBiz(IGroupExtBiz groupExtBiz) {
		this.groupExtBiz = groupExtBiz;
	}

	public void setUserAccountExtBiz(IUserAccountExtBiz userAccountExtBiz) {
		this.userAccountExtBiz = userAccountExtBiz;
	}
	
	public ListPage<UserAccountExtInfo> getUserAccountPage() {
		return userAccountPage;
	}
}
