package org.eapp.crm.dao.imp;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.crm.dao.IUserAccountExtDAO;
import org.eapp.crm.hbean.UserAccountExt;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;
import org.hibernate.Query;

public class UserAccountExtDAO extends BaseHibernateDAO implements
		IUserAccountExtDAO {

	private Log log = LogFactory.getLog(UserAccountExtDAO.class);
	
	@Override
	public ListPage<UserAccountExt> getAll() {
		QueryParameters qp = new QueryParameters();
		
		try {
			StringBuffer hql = new StringBuffer();
			
			hql.append("select r from UserAccountExt as r where 1=1");
			
			return new CommQuery<UserAccountExt>().queryListPage(qp, qp.appendOrders(hql, "r"),
					getSession());
		} catch (RuntimeException re) {
			log.error("query listPage error", re);
		}
		
		return new ListPage<UserAccountExt>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserAccountExt> queryUserAccountExtByAccountIDs(
			String[] accountIDs) {
		StringBuffer hql = new StringBuffer();
		hql.append("FROM UserAccountExt WHERE accountId in (:ids) " );
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("ids", accountIDs);
		List<UserAccountExt> list = query.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UserAccountExt> findByServiceAccountId(String serviceAccountId) {
		Query query = this.getSession().createQuery("FROM UserAccountExt WHERE serviceAccountId=:serviceAccountId");
		query.setString("serviceAccountId", serviceAccountId);
		return query.list();
	}
}
