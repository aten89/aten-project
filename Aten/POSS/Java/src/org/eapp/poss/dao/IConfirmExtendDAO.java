package org.eapp.poss.dao;

import org.eapp.poss.dao.param.ConfirmExtendQueryParameters;
import org.eapp.poss.hbean.ConfirmExtend;
import org.eapp.util.hibernate.ListPage;

public interface IConfirmExtendDAO extends IBaseHibernateDAO {
	
	ConfirmExtend findById(String id);
	
	ListPage<ConfirmExtend> findPage(ConfirmExtendQueryParameters qp);
	
	public void deleteAll(String prodId, String orgName);
}
