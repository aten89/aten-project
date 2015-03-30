package org.eapp.crm.blo.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.crm.blo.IUserAccountExtBiz;
import org.eapp.crm.dao.IUserAccountExtDAO;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.UserAccountExt;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.util.hibernate.ListPage;

public class UserAccountExtBiz implements IUserAccountExtBiz {

	private Log log = LogFactory.getLog(UserAccountExtBiz.class);
	
	private IUserAccountExtDAO userAccountExtDAO;
	
	public IUserAccountExtDAO getUserAccountExtDAO() {
		return userAccountExtDAO;
	}

	public void setUserAccountExtDAO(IUserAccountExtDAO userAccountExtDAO) {
		this.userAccountExtDAO = userAccountExtDAO;
	}

	@Override
	public ListPage<UserAccountExt> getAll() {
		return this.userAccountExtDAO.getAll();
	}

	@Override
	public List<UserAccountExt> txModifyServiceAccount(String[] accountIDs,
			String serviceAccountID) throws CrmException {
		Map<String,UserAccountExt> mapExistAccountExt = new HashMap<String,UserAccountExt>();
		List<UserAccountExt> listExt = userAccountExtDAO.queryUserAccountExtByAccountIDs(accountIDs);
		if(listExt != null) {
			for(UserAccountExt ext : listExt) {
				ext.setServiceAccountId(serviceAccountID);
				mapExistAccountExt.put(ext.getAccountId(), ext);
			}
		}
		
		List<String> addAccountID = new ArrayList<String>();
		for(String accountID : accountIDs) {
			if(!mapExistAccountExt.containsKey(accountID)) {
				addAccountID.add(accountID);
			}
		}
		List<UserAccountExt> listNewAccountExt = new ArrayList<UserAccountExt>();
		try {
			UserAccountService userAccountService = new UserAccountService();
			for(String newAccountID : addAccountID) {
				UserAccountInfo accountInfo = userAccountService.getUserAccount(newAccountID);
				if(accountInfo != null) {
					UserAccountExt newExt = new UserAccountExt();
					//取不到UserId，暂时先用accountID 代替
					newExt.setUserId(accountInfo.getAccountID());
					newExt.setAccountId(accountInfo.getAccountID());
					newExt.setServiceAccountId(serviceAccountID);
					
					listNewAccountExt.add(newExt);
				}
			}
		}catch(Exception ex) {
			log.error(ex.getMessage(),ex);
			throw new CrmException(ex);
		}
		
		List<UserAccountExt> listUpdateAccountExt = new ArrayList<UserAccountExt>(mapExistAccountExt.values());
		
		for(UserAccountExt accountExt : listUpdateAccountExt) {
			this.userAccountExtDAO.update(accountExt);
		}
		
		for(UserAccountExt item : listNewAccountExt) {
			this.userAccountExtDAO.save(item);
		}
		
		return listUpdateAccountExt;
	}
	
	@Override
	public List<UserAccountExt> getByServiceAccountId(String serviceAccountId) {
		return userAccountExtDAO.findByServiceAccountId(serviceAccountId);
	}

}
