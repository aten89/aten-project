package org.eapp.crm.blo;

import java.util.List;

import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.UserAccountExt;
import org.eapp.util.hibernate.ListPage;

public interface IUserAccountExtBiz {

	ListPage<UserAccountExt> getAll();

	List<UserAccountExt> txModifyServiceAccount(String[] accountIDs,
			String serviceAccountID) throws CrmException;
	
	List<UserAccountExt> getByServiceAccountId(String serviceAccountId);
}
