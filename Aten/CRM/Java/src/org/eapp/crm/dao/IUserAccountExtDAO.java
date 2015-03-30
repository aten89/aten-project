package org.eapp.crm.dao;

import java.util.List;

import org.eapp.crm.hbean.UserAccountExt;
import org.eapp.util.hibernate.ListPage;

public interface IUserAccountExtDAO extends IBaseHibernateDAO {

	ListPage<UserAccountExt> getAll();

	List<UserAccountExt> queryUserAccountExtByAccountIDs(String[] accountIDs);

	List<UserAccountExt> findByServiceAccountId(String serviceAccountId);
}
